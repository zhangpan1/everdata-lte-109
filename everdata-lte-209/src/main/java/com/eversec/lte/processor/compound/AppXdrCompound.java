package com.eversec.lte.processor.compound;

import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import scala.Tuple2;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.constant.SdtpConstants.CompXDRType;
import com.eversec.lte.constant.SdtpConstants.XDRType;
import com.eversec.lte.model.compound.XdrCompoundCommon;
import com.eversec.lte.model.compound.XdrCompoundSourceApp;
import com.eversec.lte.model.compound.XdrCompoundSourceApp.XdrApplicationCell;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.processor.decoder.XdrCustomBytesDecoder;
import com.eversec.lte.utils.FormatUtils;
import com.eversec.lte.vo.compound.CompInfo;
import com.eversec.lte.vo.compound.CompMessage;
import com.eversec.lte.vo.compound.CompMessage.MrInfo;

/**
 * s1u合成业务xdr功能
 * 
 * @author bieremayi
 * 
 */
public class AppXdrCompound extends Compounder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4748510712368025685L;

	XdrCustomBytesDecoder decoder = new XdrCustomBytesDecoder();

	@Override
	public Iterable<CompInfo> call(Tuple2<String, Iterable<byte[]>> t)
			throws Exception {
		TreeMap<Long, MrInfo> mrInfos = new TreeMap<>();
		List<XdrSingleSource> xdrs = new ArrayList<>();
		for (byte[] load : t._2) {
			CompMessage compMessage = decoder.decode(load);
			mrInfos.putAll(compMessage.getMrInfos());
			xdrs.addAll(compMessage.getXdrs());
		}
		CompInfo compInfo = compoundAppXdr(xdrs, mrInfos);
		return Arrays.asList(compInfo);
	}

	/**
	 * 按用户-业务分组,合成业务xdr
	 * 
	 * @param s1us
	 * @param mrInfos
	 * @return
	 */
	public CompInfo compoundAppXdr(List<XdrSingleSource> xdrs,
			TreeMap<Long, MrInfo> mrInfos) {
		// 按照时间排序xdr
		Collections.sort(xdrs, XdrSingleSource.XDR_SINGLE_SOURCE_COMPARATOR);
		CompInfo compInfo = new CompInfo();
		Map<String, Map<String, List<XdrSingleSourceS1U>>> appMap = new HashMap<>();// 以app为key,value为基站对应的小区xdrs
		int tmp = 0;// 用于区分cellKey的临时变量
		long lastCellID = Long.MIN_VALUE;// 记录最后基站ID
		for (XdrSingleSource xdr : xdrs) {
			XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) xdr;
			if (StringUtils.isNotBlank(s1u.getMobileCommon().getImsi())) {
				short appTypeCode = s1u.getBusinessCommon().getAppTypeCode();
				// 目前只计算 通用100;DNS101;FTP104;RTSP107
				if (appTypeCode == XDRType.XDR_BUSINESS
						|| appTypeCode == XDRType.DNS
						|| appTypeCode == XDRType.FTP
						|| appTypeCode == XDRType.RTSP) {
					int app = s1u.getBusinessCommon().getAppType();
					int subapp = s1u.getBusinessCommon().getAppSubType();
					long cellID = s1u.getMobileCommon().getCellId();
					String appKey = app + "_" + subapp;
					Map<String, List<XdrSingleSourceS1U>> cellXdrMap = appMap
							.get(appKey);
					if (cellXdrMap == null) {
						cellXdrMap = new LinkedHashMap<>();// !!!!注意使用linkedhashmap按照存放顺序排序，避免后面时间取值错误
						appMap.put(appKey, cellXdrMap);
					}
					if (lastCellID != Long.MIN_VALUE && lastCellID != cellID) {// 判断cellKey是否需要改变
						tmp++;
					}
					String cellKey = cellID + "_" + tmp;
					List<XdrSingleSourceS1U> cellXdrs = cellXdrMap.get(cellKey);
					if (cellXdrs == null) {
						cellXdrs = new ArrayList<>();
						cellXdrMap.put(cellKey, cellXdrs);
					}
					cellXdrs.add(s1u);
					lastCellID = cellID;
				}
			}
		}

		for (Entry<String, Map<String, List<XdrSingleSourceS1U>>> appEntry : appMap
				.entrySet()) {
			// 公共信息
			String city = "";
			short rat = MAX_UNSIGNED_BYTE;
			short xdrType = CompXDRType.CXDR_APPLICATION;
			byte[] xdrId = XdrIDGenerater.getXdrID();
			String imsi = "";
			String imei = "";
			String msisdn = "";
			// app信息
			int appType = MAX_UNSIGNED_SHORT;
			int appSubType = MAX_UNSIGNED_SHORT;
			Date startTime = null;
			Date endTime = null;
			double startLongitude = 0;
			double startLatitude = 0;
			double endLongitude = 0;
			double endLatitude = 0;
			String userIpv4 = null;
			String userIpv6 = null;
			short xdrNumber = 0;
			List<XdrApplicationCell> cells = new ArrayList<>();
			boolean isFirst = true;
			Map<String, List<XdrSingleSourceS1U>> cellMap = appEntry.getValue();
			for (Entry<String, List<XdrSingleSourceS1U>> cellEntry : cellMap
					.entrySet()) {
				xdrNumber++;
				byte[] cell_xdrID = null;
				long cell_startTime = Long.MAX_VALUE;
				long cell_endTime = Long.MIN_VALUE;
				// 基站经纬度设置
				double cell_startLongitude = 0;
				double cell_startLatitude = 0;
				double cell_endLongitude = 0;
				double cell_endLatitude = 0;
				int cell_enbID = MAX_UNSIGNED_SHORT;
				long cell_cellID = MAX_UNSIGNED_INT;
				long cell_enbGtpTeid = MAX_UNSIGNED_INT;
				long cell_sgwGtpTeid = MAX_UNSIGNED_INT;
				long cell_uLData = 0;
				long cell_dLData = 0;
				List<XdrSingleSourceS1U> groupXdrs = cellEntry.getValue();// 同一用户基站相同且相邻的xdr
				int size = groupXdrs.size();
				for (int i = 0; i < size; i++) {// 合并同一用户基站相同且相邻的xdr
					XdrSingleSourceS1U s1u = groupXdrs.get(i);
					if (i == 0) {// first
						if (isFirst) {
							isFirst = false;
							city = s1u.getS1uCommon().getCity();
							rat = s1u.getMobileCommon().getRat();
							xdrType = (short) s1u.getBusinessCommon()
									.getXdrType();
							imsi = s1u.getMobileCommon().getImsi();
							imei = s1u.getMobileCommon().getImei();
							msisdn = s1u.getMobileCommon().getMsisdn();
							appType = s1u.getBusinessCommon().getAppType();
							appSubType = s1u.getBusinessCommon()
									.getAppSubType();
							userIpv4 = FormatUtils.getIp(s1u.getBusinessCommon().getUserIpv4());
							userIpv6 = FormatUtils.getIp(s1u.getBusinessCommon().getUserIpv6());
						}
						cell_xdrID = s1u.getCommon().getXdrId();
						Entry<Long, MrInfo> startEntry = mrInfos
								.ceilingEntry(s1u.getProduceStartTime());
						if (startEntry != null) {
							cell_startLongitude = startEntry.getValue()
									.getLongitude();
							cell_startLatitude = startEntry.getValue()
									.getLatitude();
						}
						cell_cellID = s1u.getMobileCommon().getCellId();
						cell_enbGtpTeid = s1u.getMobileCommon()
								.getEnbOrSgsnGtpTeid();
						cell_sgwGtpTeid = s1u.getMobileCommon()
								.getSgwOrGgsnGtpTeid();
					}
					if (i == size - 1) {// last
						Entry<Long, MrInfo> endEntry = mrInfos.ceilingEntry(s1u
								.getProduceEndTime());
						if (endEntry != null) {
							cell_endLongitude = endEntry.getValue()
									.getLongitude();
							cell_endLatitude = endEntry.getValue()
									.getLatitude();
						}
					}
					cell_startTime = Math.min(cell_startTime,
							s1u.getProduceStartTime());
					cell_endTime = Math.max(cell_endTime,
							s1u.getProduceEndTime());
					if (StringUtils.isBlank(imei)) {
						imei = s1u.getMobileCommon().getImei();
					}
					if (StringUtils.isBlank(msisdn)) {
						msisdn = s1u.getMobileCommon().getMsisdn();
					}
					// 流量累加
					cell_uLData += s1u.getBusinessCommon().getUlData();
					cell_dLData += s1u.getBusinessCommon().getDlData();
				}
				XdrApplicationCell appCell = new XdrApplicationCell(cell_xdrID,
						new Date(cell_startTime), new Date(cell_endTime),
						cell_startLongitude, cell_startLatitude,
						cell_endLongitude, cell_endLatitude, cell_enbID,
						cell_cellID, cell_enbGtpTeid, cell_sgwGtpTeid,
						cell_uLData, cell_dLData);
				cells.add(appCell);
			}
			XdrApplicationCell startCellXdr = cells.get(0);
			XdrApplicationCell endCellXdr = cells.get(cells.size() - 1);
			startTime = startCellXdr.getStartTime();
			endTime = endCellXdr.getEndTime();
			startLongitude = startCellXdr.getStartLongitude();
			startLatitude = startCellXdr.getStartLatitude();
			endLongitude = endCellXdr.getEndLongitude();
			endLatitude = endCellXdr.getEndLatitude();
			XdrCompoundSourceApp compAppXdr = new XdrCompoundSourceApp(appType,
					appSubType, startTime, endTime, startLongitude,
					startLatitude, endLongitude, endLatitude, userIpv4,
					userIpv6, xdrNumber, cells);
			XdrCompoundCommon common = new XdrCompoundCommon(0, city, rat,
					xdrType, xdrId, imsi, imei, msisdn);
			common.setLength(compAppXdr.getBodyLength()
					+ common.getBodyLength());// 重新设置length
			compAppXdr.setCommon(common);
			// System.out.println(Hex.encodeHexString(common.toByteArray())
			// + Hex.encodeHexString(compAppXdr.toByteArray()));
			if (common.getLength() > MAX_UNSIGNED_SHORT
					- SdtpConstants.SDTP_HEADER_LENGTH - 1) {
				System.out.println("length : " + common.getLength());
			} else if (cells.size() > MAX_UNSIGNED_BYTE) {
				System.out.println("size : " + cells.size());
			} else {
				compInfo.getCxdrs().add(compAppXdr);
			}
		}
		return compInfo;
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put("1", 1);
		map.put("2", 2);
		map.put("3", 3);
		map.put("7", 4);
		map.put("5", 5);
		map.put("6", 6);
		for (String key : map.keySet()) {
			System.out.println(key);
		}
	}
}
