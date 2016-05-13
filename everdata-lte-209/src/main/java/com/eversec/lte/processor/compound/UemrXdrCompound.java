package com.eversec.lte.processor.compound;

import static com.eversec.lte.utils.GeoUtil.DEGREES_TO_RADIANS;
import static com.eversec.lte.utils.GeoUtil.KM_TO_DEG;
import static com.eversec.lte.utils.GeoUtil.pointOnBearingRAD;
import static com.eversec.lte.utils.GeoUtil.toRadians;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.eversec.lte.cache.back.UserUemrCache;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.constant.SdtpConstants.CompXDRType;
import com.eversec.lte.model.compound.XdrCompoundCommon;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR.XdrNeighborCell;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUEMR.Neighbor;
import com.eversec.lte.utils.GeoUtil;
import com.eversec.lte.utils.GeoUtil.Point;

/**
 * 合成uemr工具类
 * 
 */
public class UemrXdrCompound {

	/**
	 * key : cellid value : 经纬度基站信息
	 */
	public static Map<String, CellLonlat> CELL_CACHE;

	/**
	 * 用户aoa、ta缓存
	 */
	public static Map<String, AoaTa> AOA_TA_CACHE;

	/**
	 * 用户经纬度缓存
	 */
	public static UserUemrCache USER_UEMR_CACHE;

	static {
		init();

	}

	private static void init() {
		USER_UEMR_CACHE = new UserUemrCache(SdtpConfig.getUserUemrSize(),
				String.valueOf(SdtpConfig.getUserUemrTTLSecond() * 1000),
				String.valueOf(SdtpConfig.getUserUemrTTLSecond() * 1000));
		CELL_CACHE = new HashMap<String, CellLonlat>();
		AOA_TA_CACHE = new HashMap<>();
		try {
			InputStream is = new FileInputStream(SdtpConfig.getCellResource());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] arr = temp.split(",");
				if (arr.length >= 3) {
					String cellID = arr[0];
					double lon = GeoUtil.toRadians(Double.parseDouble(arr[1]));// 经度
					double lat = GeoUtil.toRadians(Double.parseDouble(arr[2]));// 纬度
					CELL_CACHE.put(cellID, new CellLonlat(cellID, lat, lon));
				} else {
					System.out.println(temp);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static XdrCompoundSourceUEMR compoundUEMR(XdrSingleSourceUEMR uemr) {
		int length = uemr.getCommon().getLength(); // 2
		String city = uemr.getCommon().getCity(); // 2
		short rat = uemr.getCommon().getRat(); // 1
		short xdrType = CompXDRType.CXDR_UEMR; // 1
		byte[] xdrId = XdrIDGenerater.getXdrID(); // 16
		String imsi = uemr.getCommon().getImsi();// 8
		String imei = uemr.getCommon().getImei();// 8
		String msisdn = uemr.getCommon().getMsisdn();// 16
		XdrCompoundCommon common = new XdrCompoundCommon(length, city, rat,
				xdrType, xdrId, imsi, imei, msisdn);
		double longitude = 0; // 8（默认全F）
		double latitude = 0;// 8（默认全F
		long enbId = SdtpConstants.MAX_UNSIGNED_INT;// 4（默认全F）
		long cellID = uemr.getCellID();// 4
		Date dateTime = uemr.getTime();// 8
		short mrType = uemr.getMRType();// 1
		short phr = uemr.getPhr();// 1
		int enbReceivedPower = uemr.getEnbReceivedPower();// 2
		short ulSinr = uemr.getUlSinr();// 1
		int servingFreq = uemr.getServingFreq();// 2
		short servingRsrp = uemr.getServingRsrp();// 1
		short servingRsrq = uemr.getServingRsrp();// 1
		short neighborCellNumber = uemr.getNeighborCellNumber();// 1
		// 回填经纬度逻辑
		int ta = uemr.getTa();
		int aoa = uemr.getAoa();
		AoaTa aoaTa = getAoaTa(imsi, ta, aoa, servingRsrp);// 更新获取aoa,ta
		Point point = null;
		if (aoaTa != null) {
			point = getLonLatPoint(cellID, aoaTa.getTa(), aoaTa.getAoa());
		}
		if (point == null) {
			point = USER_UEMR_CACHE.getIfPresent(msisdn);
		} else {
			USER_UEMR_CACHE.update(imsi, point);
		}
		if (point != null) {
			longitude = point.lon;
			latitude = point.lat;
		}
		List<XdrCompoundSourceUEMR.XdrNeighborCell> neighbors = new ArrayList<XdrCompoundSourceUEMR.XdrNeighborCell>();
		if (neighborCellNumber > 0 && neighborCellNumber < 0xff) {
			for (int i = 0; i < neighborCellNumber; i++) {
				Neighbor neighbor = uemr.getNeighbors().get(i);
				int neighborCellPci = neighbor.getNeighborCellPci();// 2
				int neighborFreq = neighbor.getNeighborFreq();// 2
				short neighborRSRP = neighbor.getNeighborRSRP();// 1
				short neighborRSRQ = neighbor.getNeighborRSRQ();// 1
				neighbors.add(new XdrNeighborCell(neighborCellPci,
						neighborFreq, neighborRSRP, neighborRSRQ));
			}
		}
		XdrCompoundSourceUEMR compound = new XdrCompoundSourceUEMR(longitude,
				latitude, enbId, cellID, dateTime, mrType, phr,
				enbReceivedPower, ulSinr, servingFreq, servingRsrp,
				servingRsrq, neighborCellNumber, neighbors);
		compound.setCommon(common);
		return compound;
	}

	/**
	 * 更新用户ta,aoa规则表,获取ta,aoa
	 * 
	 * @param imsi
	 * @param ta
	 * @param aoa
	 */
	private static AoaTa getAoaTa(String imsi, int ta, int aoa,
			short servingRsrp) {
		AoaTa aoaTa = null;
		if (StringUtils.isNotBlank(imsi)) {
			aoaTa = AOA_TA_CACHE.get(imsi);
			if (aoaTa == null) {
				aoaTa = new AoaTa(aoa, ta);
				AOA_TA_CACHE.put(imsi, aoaTa);
			}
			servingRsrp = (short) (servingRsrp - 140);
			if (servingRsrp >= -60) {
				ta = 0;
			} else if (servingRsrp < -60 && servingRsrp >= -70) {
				ta = 1;
			} else if (servingRsrp < -70 && servingRsrp >= -80) {
				ta = 2;
			} else if (servingRsrp < -80 && servingRsrp >= -90) {
				ta = 3;
			} else if (servingRsrp < -90 && servingRsrp >= -100) {
				ta = 4;
			} else {
				ta = 5;
			}
			aoaTa.setTa(ta);
			if (aoa <= 3600) {
				aoaTa.setAoa(aoa);
			}
		}
		return aoaTa;
	}

	// (ta <= 63 ? 78 : 550)
	// ta 78.12
	// 经纬度计算
	private static Point getLonLatPoint(long cellID, int ta, int aoa) {
		CellLonlat celllonlat = CELL_CACHE.get(String.valueOf(cellID));
		if (celllonlat == null) {
			return null;
		}
		double startLat = celllonlat.lat;
		double startLon = celllonlat.lon;
		double distanceRAD = ta * GeoUtil.TA * KM_TO_DEG / 1000
				* DEGREES_TO_RADIANS;
		double bearingRAD = toRadians(aoa / 10);
		Point point = GeoUtil.toDegrees(pointOnBearingRAD(startLat, startLon,
				distanceRAD, bearingRAD));
		return point;
	}

	public static class AoaTa {
		int aoa;
		int ta;

		public AoaTa(int aoa, int ta) {
			super();
			this.aoa = aoa;
			this.ta = ta;
		}

		@Override
		public String toString() {
			return "AoaTa [aoa=" + aoa + ", ta=" + ta + "]";
		}

		public int getAoa() {
			return aoa;
		}

		public void setAoa(int aoa) {
			this.aoa = aoa;
		}

		public int getTa() {
			return ta;
		}

		public void setTa(int ta) {
			this.ta = ta;
		}

	}

	public static class CellLonlat {
		String cellID;
		double lat;
		double lon;

		public CellLonlat(String cellID, double lat, double lon) {
			this.cellID = cellID;
			this.lat = lat;
			this.lon = lon;
		}

		@Override
		public String toString() {
			return " [" + cellID + "|" + lat + "|" + lon + "]";
		}
	}

	public static void main(String[] args) throws IOException, DecoderException {
		System.out.println(CELL_CACHE.size());
		BufferedReader reader = new BufferedReader(new FileReader(
				"D:/uemr_20150202095340_00000.txt"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] arrs = line.split("\\|", 30);
			int length = Integer.valueOf(arrs[0]);
			int mmeGroupId = -1;
			short mmeCode  = -1;
			String city = arrs[1];
			short interface1 = Short.valueOf(arrs[2]);
			byte[] xdrId = Hex.decodeHex(arrs[3].toCharArray());
			short rat = Short.valueOf(arrs[4]);
			String imsi = arrs[5];
			String imei = arrs[6];
			String msisdn = arrs[7];
			long mmeUeS1apId = Long.valueOf(arrs[8]);
			int enbID = -1;
			long cellID = Long.valueOf(arrs[10]);
			Date time = new Date(Long.valueOf(arrs[11]));
			short mRType = Short.valueOf(arrs[12]);
			short phr = Short.valueOf(arrs[13]);
			int enbReceivedPower = Integer.valueOf(arrs[14]);
			short ulSinr = Short.valueOf(arrs[15]);
			int ta = Integer.valueOf(arrs[16]);
			int aoa = Integer.valueOf(arrs[17]);
			int servingFreq = Integer.valueOf(arrs[18]);
			short servingRsrp = Short.valueOf(arrs[19]);
			short servingRsrq = Short.valueOf(arrs[20]);
			short neighborCellNumber = 0;// Short.valueOf(arrs[21])
			List<Neighbor> neighbors = new ArrayList<>();
			
			XdrSingleSourceUEMR uemr = new XdrSingleSourceUEMR(mmeGroupId,
					mmeCode, mmeUeS1apId, enbID, cellID, time, mRType, phr,
					enbReceivedPower, ulSinr, ta, aoa, servingFreq,
					servingRsrp, servingRsrq, neighborCellNumber, neighbors);
			XdrSingleCommon common = new XdrSingleCommon(length, city,
					interface1, xdrId, rat, imsi, imei, msisdn);
			uemr.setCommon(common);
			XdrCompoundSourceUEMR compUemr = compoundUEMR(uemr);
			System.out
					.println(compUemr.getCommon().toString() + "|" + compUemr);
		}
		reader.close();
	}

}
