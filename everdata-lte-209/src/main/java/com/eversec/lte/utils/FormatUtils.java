package com.eversec.lte.utils;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;

public class FormatUtils {

	public static final String BLANK = "";

	public static final byte F = (byte) 0xFF;
	public static final byte ZERO = (byte) 0x00;

	public static final byte[] UNKOWN_CHAR = { (byte) -17, (byte) -65,
			(byte) -67 };

	public static final String SPACE = " ";

	public static final String LIST_DELIMITER = SdtpConfig.getListDelimiter();

	public static final byte[] EIGHT_BYTES_PADDING_WITH_F = new byte[] { F, F,
			F, F, F, F, F, F };

	public static byte[] createAllFBytes(int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = F;
		}
		return result;
	}

	public static Date getDate(IoBuffer buffer) {
		return new Date(buffer.getLong());
	}

	public static String getString(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		if (length == 0 || isAllF(dst)) {
			return BLANK;
		} else {
			return new String(dst);
		}
	}

	public static void setString(IoBuffer buffer, String str, int length) {
		// 默认全F
		if (StringUtils.isBlank(str)) {
			buffer.put(createAllFBytes(length));
		} else {
			byte[] dst = str.getBytes();
			if (dst.length != length) {
				if (dst.length < length) {
					byte[] dst2 = new byte[length];
					for (int i = 0; i < dst.length; i++) {
						dst2[i] = dst[i];
					}
					dst = dst2;
				} else if (dst.length > length) {// 增加大于判断
					byte[] dst2 = new byte[length];
					for (int i = 0; i < length; i++) {
						dst2[i] = dst[i];
					}
					dst = dst2;
				}
			}
			buffer.put(dst);
		}
	}

	public static byte[] getBytes(IoBuffer buffer, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("param length illegal.");
		}
		byte[] dst = new byte[length];
		buffer.get(dst);
		return dst;
	}

	public static byte[] getReverseBytes(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		ArrayUtils.reverse(dst);
		return dst;
	}

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

	public static byte[] TBCDParse(String str) {

		byte[] result = TBCDUtil.parseTBCD(str);
		return result;
	}

	/**
	 * padding with zeros
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static byte[] getFixedBytes(String str, int length) {
		return Arrays.copyOf(str.getBytes(), length);
	}

	/**
	 * 
	 * @param tbcd
	 * @param length
	 * @param padding
	 * @return
	 */
	public static byte[] getFixedTBCDBytes(String tbcd, int length, byte padding) {
		byte[] ret = new byte[length];
		if (tbcd != null && tbcd.trim().length() > 0) {
			byte[] original = TBCDUtil.parseTBCD(tbcd);
			int len = original.length;
			if (len < length) {
				for (int i = 0; i < len; i++)
					ret[i] = original[i];
				for (int i = len; i < length; i++)
					ret[i] = padding;
			} else {
				ret = Arrays.copyOf(original, length);
			}
		} else {
			for (int i = 0; i < length; i++)
				ret[i] = padding;
		}
		return ret;
	}

	/**
	 * 
	 * @param str
	 * @param length
	 * @param padding
	 * @return
	 */
	public static byte[] getFixedBytes(String str, int length, byte padding) {
		byte[] ret = new byte[length];
		if (str != null && str.trim().length() > 0) {
			byte[] original = str.getBytes();
			int len = original.length;
			if (len < length) {
				for (int i = 0; i < len; i++)
					ret[i] = original[i];
				for (int i = len; i < length; i++)
					ret[i] = padding;
			} else {
				ret = Arrays.copyOf(original, length);
			}
		} else {
			for (int i = 0; i < length; i++)
				ret[i] = padding;
		}
		return ret;
	}

	/**
	 * 数组填充F
	 * 
	 * @param dst
	 * @param length
	 * @param padding
	 * @param isPrefix
	 * @return
	 */
	public static byte[] getFixedBytes(byte[] dst, int length, byte padding,
			boolean isPrefix) {
		byte[] ret = new byte[length];
		int len = dst.length;
		if (len < length) {
			if (!isPrefix) {
				for (int i = 0; i < len; i++)
					ret[i] = dst[i];
				for (int i = len; i < length; i++)
					ret[i] = padding;
			} else {
				for (int i = 0; i < length - len; i++)
					ret[i] = padding;
				for (int i = 0; i < len; i++)
					ret[length - len + i] = dst[i];
			}
		} else {
			ret = Arrays.copyOf(dst, length);
		}
		return ret;
	}

	/**
	 * 
	 * @param str
	 * @param length
	 * @param padding
	 * @return
	 */
	public static byte[] getFixedBytes(String str, int length, char padding) {
		byte[] ret = new byte[length];
		byte[] original = str.getBytes();
		int len = original.length;
		if (len < length) {
			StringBuilder sb = new StringBuilder(str);
			for (int i = 0; i < length - len; i++) {
				sb.append(padding);
			}
			ret = sb.toString().getBytes();
		} else {
			ret = Arrays.copyOf(original, length);
		}
		return ret;
	}

	/**
	 * 字节数组是否为全F
	 * 
	 * @param dst
	 * @return
	 */
	public static boolean isAllF(byte[] dst) {
		for (byte b : dst) {
			if (b != F)
				return false;
		}
		return true;
	}

	/**
	 * 字节数组是否为全Zero
	 * 
	 * @param dst
	 * @return
	 */
	public static boolean isAllZero(byte[] dst) {
		for (byte b : dst) {
			if (b != ZERO)
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param dst
	 * @param prefix
	 * @return
	 */
	public static int prefixCount(byte[] dst, byte prefix) {
		int count = 0;
		for (byte b : dst) {
			if (b != prefix)
				return count;
			count++;
		}
		return count;
	}

	/**
	 * F前綴数量
	 * 
	 * @param dst
	 * @return
	 */
	public static int prefixFCount(byte[] dst) {
		return prefixCount(dst, F);
	}

	/**
	 * 
	 * @param dst
	 * @param suffix
	 * @return
	 */
	public static int suffixCount(byte[] dst, byte suffix) {
		int count = 0;
		for (int i = dst.length - 1; i > 0; i--) {
			if (dst[i] != suffix)
				return count;
			count++;
		}
		return count;
	}

	/**
	 * F后綴数量
	 * 
	 * @param dst
	 * @return
	 */
	public static int suffixFCount(byte[] dst) {
		return suffixCount(dst, F);
	}

	/**
	 * 获取需要去除结尾0xFF填充字节的字符串
	 * 
	 * @param dst
	 * @return
	 */
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

	public static int suffixZeroCount(byte[] dst) {
		return suffixCount(dst, ZERO);
	}

	/**
	 * 获取需要去除结尾0x00填充字节的字符串
	 * 
	 * @param dst
	 * @return
	 */
	public static String getStringSuffixZero(byte[] dst) {
		return new String(Arrays.copyOf(dst, dst.length - suffixZeroCount(dst)));
	}

	public static String getStringSuffixZero(IoBuffer buffer, int length) {
		byte[] dst = getBytes(buffer, length);
		if (length == 0 || isAllF(dst) || isAllZero(dst)) {
			return BLANK;
		} else {
			return getStringSuffixZero(dst);
		}
	}

	/**
	 * 不可见字符部分
	 * 
	 * @param dst
	 * @return
	 */
	public static String getStringSuffixUnkownChar(IoBuffer buffer, int length) {
		byte[] unkown = getStringSuffixZero(buffer, length).getBytes();
		return new String(Arrays.copyOf(unkown, unkown.length
				- suffixUnkownCount(unkown)));
	}

	private static int suffixUnkownCount(byte[] unkown) {
		int count = 0;
		for (int i = unkown.length - 1; i > 0; i--) {
			boolean find = false;
			for (int j = 0; j < UNKOWN_CHAR.length; j++) {
				if (unkown[i] == UNKOWN_CHAR[j]) {
					find = true;
					break;
				}
			}
			if (find) {
				count++;
			} else {
				return count;
			}
		}
		return count;
	}

	/**
	 * 替换String 中 byte 0 为 . 因为sgs还存在不可见字符，故采用持方法 替换 0 为 .
	 * 
	 * @param str
	 * @return
	 */
	public static String getStringRepZeroWithDot(String str) {
		if (str == null) {
			return str;
		}
		return str.replaceAll(String.valueOf((char) 0), ".");
	}

	/**
	 * 字节数组转换点分十进制字符串ip地址</br> 支持4字节ipv4、16字节ipv6、16字节ipv4（前12为填充F）,默认全F
	 * 
	 * @param ip
	 * @return
	 */
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

	/**
	 * 换点分十进制字符串ip地址转字节数组</br>不足位数前缀补F
	 * 
	 * @param ip
	 * @return
	 */
	public static byte[] getIp(String ip, int length) {
		byte[] ret = new byte[length];
		if (StringUtils.isBlank(ip)) {
			for (int i = 0; i < length; i++)
				ret[i] = F;
		} else {
			try {
				InetAddress address = InetAddress.getByName(ip);
				ret = getFixedBytes(address.getAddress(), length, F, true);
			} catch (UnknownHostException e) {
				for (int i = 0; i < length; i++)
					ret[i] = F;
			}
		}
		return ret;

	}

	public static int getInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static long getLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static short getShort(String str) {
		try {
			return Short.parseShort(str);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static double getDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static Date getDate(String str) {
		try {
			return new Date(getLong(str));
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static String listToString(List<?> list) {
		String ret = BLANK;
		if (list != null && list.size() > 0) {
			StringBuilder tmp = new StringBuilder();
			for (Object obj : list) {
				tmp.append(obj).append(LIST_DELIMITER);
			}
			ret = tmp.substring(0, tmp.length() - 1);
		}
		return ret;
	}

	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(x);
		return buffer.array();
	}

	public static long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes);
		buffer.flip();// need flip
		return buffer.getLong();
	}

	public static String getEncodeStr(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 设置经纬度信息
	 * 
	 * @param buffer
	 * @param latlon
	 */
	public static void setFormatLatLon(IoBuffer buffer, double latlon) {
		if (latlon == 0) {
			buffer.put(EIGHT_BYTES_PADDING_WITH_F);
		} else {
			buffer.putDouble(latlon);
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static <T> boolean arrayContans(T[] array, T target) {
		if (array == null || array.length == 0) {
			return true;
		}
		for (T t : array) {
			if (t != null && t.equals(target)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		IoBuffer buffer = IoBuffer.wrap(EIGHT_BYTES_PADDING_WITH_F);
		double d = buffer.getDouble();
		System.out.println(buffer);
		System.out.println(d);
		System.out.println(Arrays.toString(EIGHT_BYTES_PADDING_WITH_F));
		byte[] bs = "CMNET.MNC000.MCC460.GPRS���".getBytes();
		for (int i = 0; i < bs.length; i++) {
			System.out.println((char) bs[i] + "-" + bs[i] + "-"
					+ Integer.toHexString(bs[i]));
		}

		for (int i = 0; i < UNKOWN_CHAR.length; i++) {
			System.out.println((char) UNKOWN_CHAR[i]);
		}

		System.out.println(getStringSuffixUnkownChar(IoBuffer.wrap(bs), 33));

		System.out.println(getStringSuffixUnkownChar(
				IoBuffer.wrap("abcdef".getBytes()), 6));
	}
}
