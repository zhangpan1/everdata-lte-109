package com.eversec.lte.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.sdtp.model.SdtpHeader;

/**
 * 
 * @author bieremayi
 * 
 */
public class SdtpUtils {

	public static int VERSION = SdtpConfig.getSdtpVersion();
	public static int SUB_VERSION = SdtpConfig.getSdtpSubVersion();

	/**
	 * 
	 * sdtp 版本检查 1: 版本协商通过。 2: 版本过高。 3: 版本过低。
	 * 
	 * @param version
	 * @param subVersion
	 * @return
	 */
	public static byte checkSdtpVersion(int version, int subVersion) {
		byte result = 1;
		if (version > VERSION) {
			result = 2;
		} else if (version < VERSION) {
			result = 3;
		} else {
			if (subVersion > SUB_VERSION) {
				result = 2;
			} else if (subVersion < SUB_VERSION) {
				result = 3;
			}
		}
		return result;
	}

	/**
	 * Digest=SHA256(LoginID+SHA256(Shared secret)+Timestamp+"rand="+RAND)
	 * 
	 * @return
	 */
	public static String getDigestReq(long timestamp, int rand) {
		String ret = null;
		try {
			MessageDigest md = MessageDigest
					.getInstance(MessageDigestAlgorithms.SHA_256);
			String loginID = new String(FormatUtils.getFixedBytes(
					SdtpConfig.getLoginID(), 12, ' '));
			byte[] pwd = md.digest(SdtpConfig.getPassword().getBytes());
			String arg = loginID + Hex.encodeHexString(pwd) + timestamp
					+ "rand=" + rand;
			byte[] dst = Arrays.copyOf(md.digest(arg.getBytes()), 64);
			ret = Hex.encodeHexString(dst);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Digest=SHA256(LoginID+SHA256(Shared secret)+"rand="+RAND+Timestamp)
	 * 
	 * @return
	 */
	public static String getDigestResp(String loginID, long timestamp, int rand) {
		String ret = null;
		try {
			MessageDigest md = MessageDigest
					.getInstance(MessageDigestAlgorithms.SHA_256);
			loginID = new String(FormatUtils.getFixedBytes(loginID, 12, ' '));
			byte[] pwd = md.digest(SdtpConfig.getPassword().getBytes());
			String arg = loginID + Hex.encodeHexString(pwd) + "rand=" + rand
					+ timestamp;
			byte[] dst = Arrays.copyOf(md.digest(arg.getBytes()), 64);
			ret = Hex.encodeHexString(dst);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte checkSdtpAuth(String loginID, String digest,
			long timestamp, int rand) {
		byte result = 3;
		if (!loginID.trim().equals(SdtpConfig.getLoginID().trim())) {
			result = 2;
		} else if (digest.equals(getDigestReq(timestamp, rand))) {
			result = 1;
		}
		return result;
	}

	/**
	 * 读取sdtp头
	 * 
	 * @param in
	 * @return
	 */
	public static SdtpHeader getHeader(IoBuffer in) {
		int totalLength = in.getUnsignedShort();
		int messageType = in.getUnsignedShort();
		long sequenceId = in.getUnsignedInt();
		short totalContents = in.getUnsigned();
		return new SdtpHeader(totalLength, messageType, sequenceId,
				totalContents);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException,
			DecoderException {
		int rand = 17767;
		long timestamp = 1411459806;
		// System.out.println(MessageDigestAlgorithms.SHA_256);
		System.out.println(getDigestReq(timestamp, rand));
		// System.out.println(Arrays.toString(Hex.decodeHex(getDigestReq(timestamp,
		// rand).toCharArray())));
		// System.out.println(getDigestResp("test      ", timestamp, rand));
		// byte[] resp = Hex.decodeHex(getDigestResp("test", timestamp,
		// rand).toCharArray());
		// System.out.println(resp.length);
		// System.out.println(Arrays.toString(resp));
	}
}
