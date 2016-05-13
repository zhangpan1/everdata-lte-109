package com.eversec.lte.sdtp.pcap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.raw.XdrRawPayData;
import com.eversec.lte.model.raw.XdrRawPayload;
import com.eversec.lte.model.raw.XdrRawSigPayload;

/**
 * Not support muli threads,must syn outside
 * 
 * @author lirongzhi
 * 
 */
public class PcapUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(PcapUtil.class);

	/**
	 * 文件头
	 */
	public static final byte[] PACP_HEADER = new byte[] { (byte) 0xD4,
			(byte) 0xC3, (byte) 0xB2, (byte) 0xA1, (byte) 0x02, (byte) 0x00,
			(byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x01,
			(byte) 0x00, (byte) 0x00, (byte) 0x00 };

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmm");

	private static final long specTime = SdtpConfig.getPcapSpecTime();

	private static File outputFile = null;
	private static FileOutputStream outputStream = null;

	private static String FILE_DIR = SdtpConfig.getPcapDir();

	private static long LAST_REC_TIME = 0;

	static {
		File dir = new File(FILE_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void writeRawToPcap(XdrRawPayData raw, long millions) {

		LAST_REC_TIME = millions;

		Date date = new Date(millions / specTime * specTime);
		String str = FORMAT.format(date);

		initOutputFile(str);

		if (raw != null) {
			if (raw.getInterface() != 11) {
				try {
					ByteArrayOutputStream array = new ByteArrayOutputStream();
					List<XdrRawPayload> payloads = raw.getPayloads();
					for (XdrRawPayload payload : payloads) {
						if (payload instanceof XdrRawSigPayload) {
							XdrRawSigPayload sig = (XdrRawSigPayload) payload;
							array.write(intToBytes((int) sig.getTime()));
							array.write(intToBytes((int) sig.getTime2()));
							array.write(intToBytes(sig.getLoad().length));
							array.write(intToBytes(sig.getLoad().length));
							array.write(sig.getLoad());
						}
					}
					outputStream.write(array.toByteArray());
					outputStream.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void initOutputFile(String str) {
		try {
			File tmp = new File(FILE_DIR, "raw" + str + ".pcap");
			if (outputFile == null) {
				outputFile = tmp;
				outputStream = new FileOutputStream(outputFile);
				writeHeadToFile();
				LOGGER.info("create new pcap file:" + outputFile.getName());

			} else {
				if (outputFile.getName().equals(tmp.getName())) {

				} else {
					outputStream.flush();
					outputStream.close();
					LOGGER.info("last pcap file:" + outputFile.getName()
							+ " to close !");

					outputFile = tmp;
					outputStream = new FileOutputStream(outputFile);
					writeHeadToFile();
					LOGGER.info("create new pcap file:" + outputFile.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeHeadToFile() {
		try {
			outputStream.write(PACP_HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	public static void main(String[] args) {

		List<XdrRawPayload> payloads = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			payloads.add(new XdrRawSigPayload((short) 0, (short) 0, (short) 0,
					(short) 0, (short) 0, System.currentTimeMillis(), System
							.currentTimeMillis(), new byte[10]));
		}
		XdrRawPayData raw = new XdrRawPayData((short) 0, (short) 0, null, 0, 0,
				payloads);
		PcapUtil.writeRawToPcap(raw, System.currentTimeMillis());
		PcapUtil.writeRawToPcap(raw, System.currentTimeMillis());

		PcapUtil.writeRawToPcap(raw, System.currentTimeMillis() + specTime * 2);
		PcapUtil.writeRawToPcap(null, System.currentTimeMillis() + specTime * 4);
	}

	public static void tryFlush() {
		try {
			if (outputFile != null
					&& (System.currentTimeMillis() - LAST_REC_TIME) > 60000) {
				outputStream.flush();
				outputStream.close();
				outputFile = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
