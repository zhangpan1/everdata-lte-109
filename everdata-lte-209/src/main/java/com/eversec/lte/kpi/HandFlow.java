package com.eversec.lte.kpi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.config.SdtpContext;

public class HandFlow {
	public static final AtomicLong srcXdrCount = new AtomicLong(); //原始xdr个数， 一行为一个xdr
	public static final AtomicLong compXdrCount = new AtomicLong();  //合并后的xdr个数
	private static Logger LOGGER = LoggerFactory.getLogger(HandFlow.class);
	public static ScheduledThreadPoolExecutor SCHEDULER = new ScheduledThreadPoolExecutor(1);
	public static Map<String, List<String>> mapFlow = new HashMap<String, List<String>>();
	public static Map<String, List<Integer>> mapService = new HashMap<String, List<Integer>>();
	public static Map<String, List<Integer>> mapAttach = new HashMap<String, List<Integer>>();
	public static Map<String, List<Integer>> mapPDP = new HashMap<String, List<Integer>>();
	public static Map<String, List<Integer>> mapRAU = new HashMap<String, List<Integer>>();
	public static Map<String, List<Integer>> mapTCP = new HashMap<String, List<Integer>>();
	public static Map<String, List<Integer>> mapDNS = new HashMap<String, List<Integer>>();
	public static Properties PROPERTIES = new Properties();
	public static String sourceDir; 
	public static String destDir; 
	public static List<StaticBean> sbList = new ArrayList<StaticBean>();
	public static String sang = null; //定义当前扫描时间      例:20150804
	public static String time = null; //定义当前扫描时间long类型   例: 1438699320000  
	
	static {
		try {
			SdtpContext.PROFILE = "handfile.properties"; 
			ClassLoader classLoader = SdtpConfig.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream(SdtpContext.PROFILE);
			if (is != null) {
				Properties pro = new Properties();
				pro.load(is);
				List<String> interfaceNameList = Arrays.asList(pro.getProperty("interface_name").split(","));
				List<String> compareCountList = Arrays.asList(pro.getProperty("compare_field_count").split(","));
				List<String> computeCountList = Arrays.asList(pro.getProperty("compute_field_count").split(","));
				for (int i = 0; i < interfaceNameList.size(); i++) {
					StaticBean bean = new StaticBean(interfaceNameList.get(i), compareCountList.get(i), computeCountList.get(i));
					sbList.add(bean);
				}
				sourceDir = "/home/eversec/prestat/flow/flow_"; 		//pro.getProperty("source_dir", "/home/eversec/prestat/flow/");
				destDir = "/home/eversec/prestat/all/S1U_XDR_M_FLOW_STAT_";			//pro.getProperty("dest_dir", "/home/eversec/prestat/s1u/");
			} else {
				LOGGER.error("file not found!");
				System.err.println("file not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
//		while(true){
//			long time = System.currentTimeMillis();
//			if((time%3600000)==0){
//				
//			}
//		}
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sang = sdf.format(date);
		
		
		long current = System.currentTimeMillis();
		long day = current / 86400000;
		time = String.valueOf(day * 86400000);
		
//		sourceDir = sourceDir + number;
		
		sourceDir = sourceDir + "20150923";
		
		File file = new File(sourceDir);
		File[] fs = file.listFiles();
		BufferedReader br = null;
		for (File file2 : fs) {
			StaticBean bean = null;
			String interfaceName = getType(file2.getName());
			for (StaticBean sb : sbList) {
				if(sb.getInterfaceName().equals(interfaceName)){
					bean = sb;
				}
			}
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
				String line = null;  
				while((line = br.readLine()) != null){  
					srcXdrCount.incrementAndGet();
					handleLine(line, bean);
				}  
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		outFile();
		 
		System.out.println("原始xdr数量: " + srcXdrCount);
		System.out.println("合并后的数量: " + compXdrCount);
		System.out.println("当前完 成: "+sang+" 的合并……");
	}
	
	//根据文件名获取类型
	public static String getType(String fileName){
		int p1 = StringUtils.lastIndexOf(fileName, "_STAT");
		String s1 = StringUtils.substring(fileName, 0, p1);
		return StringUtils.substringAfterLast(s1, "_");
	}
	
	//处理读取的一行
	public static void handleLine(String line, StaticBean bean){
		line = time + line.substring(line.indexOf("|"));
		
		String interfaceName = bean.getInterfaceName();
		String temp = null;
		String v1 = null;
		String v2 = null;
		String v3 = null;
		
		switch (interfaceName) {
			case "FLOW":
				//拆分字符串
				int a1 = StringUtils.lastIndexOf(line, "|", line.length())+1;
				v1 = StringUtils.substring(line, a1, line.length());
				temp = StringUtils.substring(line, 0, StringUtils.lastIndexOf(line, "|", line.length()));
				v2 = StringUtils.substring(temp, StringUtils.lastIndexOf(temp, "|", temp.length())+1, temp.length());
				temp = StringUtils.substring(temp, 0, StringUtils.lastIndexOf(temp, "|", temp.length()));
				v3 = StringUtils.substring(temp, StringUtils.lastIndexOf(temp, "|", temp.length())+1, temp.length());
				temp = StringUtils.substring(temp, 0, StringUtils.lastIndexOf(temp, "|", temp.length()));
				
				//累加map的value
				if(mapFlow.containsKey(temp)){
					List<String> list = mapFlow.get(temp);
					for (int i = 0; i < list.size(); i++) {
						switch (i) {
							case 0:
								list.set(i, String.valueOf((Long.parseLong(list.get(i)) + Long.parseLong(v1))));
								break;
							case 1:
								list.set(i, String.valueOf((Long.parseLong(list.get(i)) + Long.parseLong(v2))));
								break;
							case 2:
								list.set(i, String.valueOf((Long.parseLong(list.get(i)) + Long.parseLong(v3))));
								break;
							default:
								break;
						}
					}
					mapFlow.put(temp, list);
				}else{
					//组装集合
					List<String> list = Arrays.asList(String.valueOf(Long.parseLong(v1)),
							String.valueOf(Long.parseLong(v2)),	String.valueOf(Long.parseLong(v3)));
					mapFlow.put(temp, list);
					compXdrCount.incrementAndGet();
				}			
			break;
			
		default:
			break;
		}
	}
	
	//输出合并后的内容文件
	public static void outFile(){
		String newLine = System.getProperty("line.separator", "\n");
		try {
			String path = destDir + sang +".txt";
			//查看目录是否存在，然后开始创建
			File f = new File(path);
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			
			BufferedWriter bwFlow = new BufferedWriter(new FileWriter(path)); 
			Set<String> keySet = mapFlow.keySet();
			for (String key : keySet) {
				List<String> list = mapFlow.get(key);
				bwFlow.write(key + "|" + list.get(0) + "|" + list.get(1) +"|" +list.get(2));
				bwFlow.write(newLine);
			}
			bwFlow.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
