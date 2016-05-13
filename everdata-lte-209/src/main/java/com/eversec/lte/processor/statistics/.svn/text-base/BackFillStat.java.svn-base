package com.eversec.lte.processor.statistics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSource;

/**
 * 回填及时统计，用于计算回填率
 * 
 * @author lirongzhi
 * 
 */
@Deprecated
public class BackFillStat {
	
	public static final boolean TEST = false;

	private static Logger LOGGER = LoggerFactory.getLogger(BackFillStat.class);

	public static ConcurrentHashMap<Short, Count> beforeStat = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Short, Count> afterStat = new ConcurrentHashMap<>();

	static {

		if (TEST) {
			beforeStat.put((short) SdtpConstants.XDRInterface.UU, new Count(
					(short) SdtpConstants.XDRInterface.UU));
			beforeStat.put((short) SdtpConstants.XDRInterface.X2, new Count(
					(short) SdtpConstants.XDRInterface.X2));
			beforeStat.put((short) SdtpConstants.XDRInterface.UE_MR, new Count(
					(short) SdtpConstants.XDRInterface.UE_MR));
			beforeStat.put((short) SdtpConstants.XDRInterface.CELL_MR,
					new Count((short) SdtpConstants.XDRInterface.CELL_MR));
			beforeStat.put((short) SdtpConstants.XDRInterface.S1MME, new Count(
					(short) SdtpConstants.XDRInterface.S1MME));
			beforeStat.put((short) SdtpConstants.XDRInterface.S6A, new Count(
					(short) SdtpConstants.XDRInterface.S6A));
			beforeStat.put((short) SdtpConstants.XDRInterface.S11, new Count(
					(short) SdtpConstants.XDRInterface.S11));
			beforeStat.put((short) SdtpConstants.XDRInterface.S10, new Count(
					(short) SdtpConstants.XDRInterface.S10));
			beforeStat.put((short) SdtpConstants.XDRInterface.SGS, new Count(
					(short) SdtpConstants.XDRInterface.SGS));
			beforeStat.put((short) SdtpConstants.XDRInterface.S5S8, new Count(
					(short) SdtpConstants.XDRInterface.S5S8));
			beforeStat.put((short) SdtpConstants.XDRInterface.S1U, new Count(
					(short) SdtpConstants.XDRInterface.S1U));
			beforeStat.put((short) SdtpConstants.XDRInterface.GNC, new Count(
					(short) SdtpConstants.XDRInterface.GNC));
			afterStat.put((short) SdtpConstants.XDRInterface.UU, new Count(
					(short) SdtpConstants.XDRInterface.UU));
			afterStat.put((short) SdtpConstants.XDRInterface.X2, new Count(
					(short) SdtpConstants.XDRInterface.X2));
			afterStat.put((short) SdtpConstants.XDRInterface.UE_MR, new Count(
					(short) SdtpConstants.XDRInterface.UE_MR));
			afterStat.put((short) SdtpConstants.XDRInterface.CELL_MR,
					new Count((short) SdtpConstants.XDRInterface.CELL_MR));
			afterStat.put((short) SdtpConstants.XDRInterface.S1MME, new Count(
					(short) SdtpConstants.XDRInterface.S1MME));
			afterStat.put((short) SdtpConstants.XDRInterface.S6A, new Count(
					(short) SdtpConstants.XDRInterface.S6A));
			afterStat.put((short) SdtpConstants.XDRInterface.S11, new Count(
					(short) SdtpConstants.XDRInterface.S11));
			afterStat.put((short) SdtpConstants.XDRInterface.S10, new Count(
					(short) SdtpConstants.XDRInterface.S10));
			afterStat.put((short) SdtpConstants.XDRInterface.SGS, new Count(
					(short) SdtpConstants.XDRInterface.SGS));
			afterStat.put((short) SdtpConstants.XDRInterface.S5S8, new Count(
					(short) SdtpConstants.XDRInterface.S5S8));
			afterStat.put((short) SdtpConstants.XDRInterface.S1U, new Count(
					(short) SdtpConstants.XDRInterface.S1U));
			afterStat.put((short) SdtpConstants.XDRInterface.GNC, new Count(
					(short) SdtpConstants.XDRInterface.GNC));
		}
	}

	private static class Count {
		short Interface = -1;
		AtomicLong IMSI = new AtomicLong(0);
		AtomicLong IMEI = new AtomicLong(0);
		AtomicLong MSISDN = new AtomicLong(0);
		AtomicLong ALL = new AtomicLong(0);
		AtomicLong COUNT = new AtomicLong(0);

		public Count(short interface1) {
			super();
			Interface = interface1;
		}

		public void addXdr(XdrSingleSource xdr) {
			XdrSingleCommon common = xdr.getCommon();
			String imsi = common.getImsi();
			String imei = common.getImei();
			String msisdn = common.getMsisdn();

			if (!StringUtils.isEmpty(imsi)) {
				IMSI.incrementAndGet();
			}
			if (!StringUtils.isEmpty(imei)) {
				IMEI.incrementAndGet();
			}
			if (!StringUtils.isEmpty(msisdn)) {
				MSISDN.incrementAndGet();
			}

			if (!StringUtils.isEmpty(imsi) && !StringUtils.isEmpty(imei)
					&& !StringUtils.isEmpty(msisdn)) {
				ALL.incrementAndGet();
			}

			COUNT.incrementAndGet();
		}

		public String toString2() {
			long num = COUNT.get();
			long all = ALL.get();
			long imsi = IMSI.get();
			long imei = IMEI.get();
			long msisdn = MSISDN.get();
			if (num > 0) {
				return "Interface:" + Interface + " \"num:\"" + num
						+ "\",all:\"" + all + "\",all/num:\"" + all * 1.0d
						/ num + "\",imsi:\"" + imsi + "\",imsi/num:\"" + imsi
						* 1.0d / num + "\"," + "imei:\"" + imei
						+ "\",imei/num:\"" + imei * 1.0d / num + "\",msisdn:\""
						+ msisdn + "\",msisdn/num:\"" + msisdn * 1.0d / num;
			} else {
				return "Interface:" + Interface + " num == 0 , not xdrs;";
			}
		}

		@Override
		public String toString() {
			long num = COUNT.get();
			long all = ALL.get();
			if (num > 0) {
				return Interface + ":" + all * 1.0d / num;
			} else {
				return Interface + ":" + 0;
			}
		}

		public void reset() {
			IMSI.set(0);
			IMEI.set(0);
			MSISDN.set(0);
			ALL.set(0);
			COUNT.set(0);
		}
	}

	public static void addBeforeXdr(XdrSingleSource xdr) {
		if (TEST) {
			XdrSingleCommon common = xdr.getCommon();
			short Interface = common.getInterface();
			beforeStat.get(Interface).addXdr(xdr);
		}
	}

	public static void addAfterXdr(XdrSingleSource xdr) {
		if (TEST) {
			XdrSingleCommon common = xdr.getCommon();
			short Interface = common.getInterface();
			afterStat.get(Interface).addXdr(xdr);
		}
	}


	public static void report() {
		if (TEST) {
			StringBuilder sb = new StringBuilder();
			sb.append("beforeStat[");
			for (Count c : beforeStat.values()) {
				sb.append(c);
				sb.append(",");
				c.reset();
			}
			sb.append("]");
			LOGGER.info(sb.toString());
			StringBuilder sba = new StringBuilder();
			sba.append("afterStat[");
			for (Count c : afterStat.values()) {
				sba.append(c);
				sba.append(",");
				c.reset();
			}
			sba.append("]");
			LOGGER.info(sba.toString());
		}
	}

}
