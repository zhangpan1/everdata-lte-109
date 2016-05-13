package com.eversec.lte.kpi.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.TBCDUtil;

public class FormatUtils {

	public static final String BLANK = "";

	public static final byte F = (byte) 0xFF;
	public static final byte ZERO = (byte) 0x00;

	public static String TBCDFormat(byte[] tbcd) {
		int index = 0;
		for (byte b : tbcd) {
			index++;
			if (b == -1)
				break;
		}
		if (index == 1) {
			return BLANK;
		} else if (index == tbcd.length) {
			return TBCDUtil.toTBCD(tbcd);
		} else {
			return TBCDUtil.toTBCD(Arrays.copyOfRange(tbcd, 0, index - 1));
		}
	}

	public static String getIp(byte[] ip) {
		String ret = BLANK;
		int count = prefixFCount(ip);
		int len = ip.length;
		InetAddress in = null;
		try {
			if (len == 16) {
				if (count != 16) {
					if (count == 12) {
						in = InetAddress.getByAddress(Arrays.copyOfRange(ip,
								count, len));
					} else {
						in = InetAddress.getByAddress(ip);
					}
					ret = in.getHostAddress();
				}
			} else if (len == 4) {
				if (count != 4) {
					in = InetAddress.getByAddress(ip);
					ret = in.getHostAddress();
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static int suffixCount(byte[] dst, byte suffix) {
		int count = 0;
		for (int i = dst.length - 1; i > 0; i--) {
			if (dst[i] != suffix)
				return count;
			count++;
		}
		return count;
	}

	public static byte[] getBytes(IoBuffer buffer, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("param length illegal.");
		}
		byte[] dst = new byte[length];
		buffer.get(dst);
		return dst;
	}

	public static boolean isAllF(byte[] dst) {
		for (byte b : dst) {
			if (b != F)
				return false;
		}
		return true;
	}

	public static int prefixFCount(byte[] dst) {
		return prefixCount(dst, F);
	}

	public static int suffixFCount(byte[] dst) {
		return suffixCount(dst, F);
	}

	public static String formatStr(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		if (length == 0 || isAllF(dst)) {
			return BLANK;
		} else {
			byte[] str = Arrays.copyOf(dst, dst.length - suffixFCount(dst));
			byte[] str1 = Arrays.copyOf(str, str.length - suffixZeroCount(str));
			return getStringRepZeroWithDot(new String(str1).trim());
		}
	}

	public static String getStringSuffixF(byte[] dst) {
		return new String(Arrays.copyOf(dst, dst.length - suffixFCount(dst)));
	}

	public static String getStringSuffixF(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		if (length == 0 || isAllF(dst)) {
			return BLANK;
		} else {
			return getStringSuffixF(dst);
		}
	}

	public static String getStringSuffixZero(byte[] dst) {
		return new String(Arrays.copyOf(dst, dst.length - suffixZeroCount(dst)));
	}

	public static String getStringSuffixZero(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		if (length == 0 || isAllF(dst)) {
			return BLANK;
		} else {
			return getStringSuffixZero(dst);
		}
	}

	public static int suffixZeroCount(byte[] dst) {
		return suffixCount(dst, ZERO);
	}

	public static int prefixCount(byte[] dst, byte prefix) {
		int count = 0;
		for (byte b : dst) {
			if (b != prefix)
				return count;
			count++;
		}
		return count;
	}

	public static String getStringRepZeroWithDot(String str) {
		if (str == null) {
			return str;
		}
		return str.replaceAll(String.valueOf((char) 0), ".");
	}

	public static String getString(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		if (length == 0 || isAllF(dst)) {
			return BLANK;
		} else {
			return new String(dst);
		}
	}
	
	/**byte[] 数组 转 String
	 */
	public static String byte2String(byte[] bs) {
		if (isAllF(bs)) {
			return BLANK;
		} else {
			return new String(bs);
		}
	}
	
}
