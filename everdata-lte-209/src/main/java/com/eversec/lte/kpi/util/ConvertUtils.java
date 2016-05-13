package com.eversec.lte.kpi.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import com.eversec.lte.kpi.config.KPIConfig;

public class ConvertUtils {

	private static int block;
	private static int block_s1u;
	private static int block_term;
	private static int block_tag;
	
	public static final String BLANK = "";
	public static final byte F = (byte) 0xFF;
	public static final byte ZERO = (byte) 0x00;
	public static final byte[] UNKOWN_CHAR = {(byte)-17,(byte)-65,(byte)-67};

	static {
		int block_time = KPIConfig.getBlockTime();
		if (block_time < 1)
			block_time = 1;
		block = block_time * 60 * 1000;
		
		int block_time_s1u = KPIConfig.getBlockTimeS1u();
		if (block_time_s1u < 1)
			block_time_s1u = 1;
		block_s1u = block_time_s1u * 60 * 1000;
		
		int block_time_term = KPIConfig.getBlockTimeTerm();
		if (block_time_term < 1)
			block_time_term = 1;
		block_term = block_time_term * 60 * 1000;
		
		int block_time_tag = KPIConfig.getBlockTimeTag();
		if (block_time_tag < 1)
			block_time_tag = 1;
		block_tag = block_time_tag * 60 * 1000;
	}

	public final static SimpleDateFormat dateformat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public int getUnsigendShort(byte[] bytes) {
		return (int) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
	}

	public long getUnsigendInt(byte[] bytes) {
		return (0xffL & (long) bytes[3]) | (0xff00L & ((long) bytes[2] << 8))
				| (0xff0000L & ((long) bytes[1] << 16))
				| (0xff000000L & ((long) bytes[0] << 24));
	}

	public long getLong(byte[] bytes) {
		return (0xffL & (long) bytes[7]) | (0xff00L & ((long) bytes[6] << 8))
				| (0xff0000L & ((long) bytes[5] << 16))
				| (0xff000000L & ((long) bytes[4] << 24))
				| (0xff00000000L & ((long) bytes[3] << 32))
				| (0xff0000000000L & ((long) bytes[2] << 40))
				| (0xff000000000000L & ((long) bytes[1] << 48))
				| (0xff00000000000000L & ((long) bytes[0] << 56));
	}

	public byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		bytes[7] = (byte) (data & 0xff);
		bytes[6] = (byte) ((data >> 8) & 0xff);
		bytes[5] = (byte) ((data >> 16) & 0xff);
		bytes[4] = (byte) ((data >> 24) & 0xff);
		bytes[3] = (byte) ((data >> 32) & 0xff);
		bytes[2] = (byte) ((data >> 40) & 0xff);
		bytes[1] = (byte) ((data >> 48) & 0xff);
		bytes[0] = (byte) ((data >> 56) & 0xff);
		return bytes;
	}

	public long time2min(long time) {
		return time / block;
	}

	public static String long2time(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time * block);
		return dateformat.format(c.getTime());
	}

	public static long min2millis(long min) {
		return min * block;
	}
	
	public long time2min_s1u(long time) {
		return time / block_s1u;
	}

	public static String long2time_s1u(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time * block_s1u);
		return dateformat.format(c.getTime());
	}

	public static long min2millis_s1u(long min) {
		return min * block_s1u;
	}
	
	public long time2min_term(long time) {
		return time / block_term;
	}

	public static String long2time_term(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time * block_term);
		return dateformat.format(c.getTime());
	}

	public static long min2millis_term(long min) {
		return min * block_term;
	}
	
	
	
	public long time2min_tag(long time) {
		return time / block_tag;
	}

	public static String long2time_tag(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time * block_tag);
		return dateformat.format(c.getTime());
	}

	public static long min2millis_tag(long min) {
		return min * block_tag;
	}
	
	
	
	
	
	
	
	
	

	public static String long2timeMillis(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return dateformat.format(c.getTime());
	}
	
	
	public static String getBytes2String(byte[] bs){
		String s ="";
		try {
			s = new String(bs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("terfac_id, tertype_id 转换错误 ..."+e);
			s ="-1";
		}
		return s;
	}
	
	/**
	 * 将byte[] 16位 IMEI 转为 String 
	 * @param dst
	 * @return
	 */
	public static String getImeiToString(byte[] dst) {
		if (dst.length == 0 || isAllF(dst) || isAllZero(dst) ) {
			return BLANK;
		} else {
			return new String(Arrays.copyOf(dst , dst.length  - suffixUnkownCount(dst)));
		}
	}
	
	public static int suffixUnkownCount(byte[] unkown) {
		int count = 0;
		for (int i = unkown.length - 1; i > 0; i--) {
			boolean find = false;
			for (int j = 0; j < UNKOWN_CHAR.length; j++) {
				if (unkown[i] == UNKOWN_CHAR[j]){
					find = true;
					break;
				}
			}
			if (find){
				count++;
			}else{
				return count;
			}
		}
		return count;
	}
	
	/**
	 * 字节数组是否为全F
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
    * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用 
    * @param value   要转换的int值 
    * @return byte数组 
    */    
	public static byte[] intToBytes(int value) {   
	    byte[] src = new byte[4];  
	    src[3] =  (byte) ((value>>24) & 0xFF);  
	    src[2] =  (byte) ((value>>16) & 0xFF);  
	    src[1] =  (byte) ((value>>8) & 0xFF);    
	    src[0] =  (byte) (value & 0xFF);                  
	    return src;   
	}  
	/**  
    * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用 
    * @param src    byte数组  
    * @param offset     从数组的第offset位开始  
    * @return int数值  
    */    
	public static int bytesToInt(byte[] src, int offset) {  
	    int value;    
	    value = (int) ((src[offset] & 0xFF)   
	            | ((src[offset+1] & 0xFF)<<8)   
	            | ((src[offset+2] & 0xFF)<<16)   
	            | ((src[offset+3] & 0xFF)<<24));  
	    return value;  
	} 

	public static void main(String[] args) {
		// ConvertUtils con = new ConvertUtils();
		System.out.println(ConvertUtils.long2time(23838404l));
	}
}
