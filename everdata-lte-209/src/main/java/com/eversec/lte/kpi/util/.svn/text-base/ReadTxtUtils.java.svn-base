package com.eversec.lte.kpi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eversec.lte.kpi.config.KPIConfig;

public class ReadTxtUtils {
	
	public static List<Terimei> readKpiImeiList(){
		String filePath = KPIConfig.getKpiImeiFile(); //"./target/classes/kpi_imei.txt"
		List<Terimei> alist = new ArrayList<Terimei>();
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//System.out.println(lineTxt);
					String[] sline = lineTxt.split("\\|");
					Terimei ter = new Terimei();
					ter.setImei(sline[0]);
					ter.setTerfac_id(Integer.parseInt(sline[1]));
					ter.setTertype_id(Integer.parseInt(sline[2]));
					ter.setTerclass_id(Integer.parseInt(sline[3]));
					
					alist.add(ter);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件[kpi_imei.txt]");
			}
		} catch (Exception e) {
			System.out.println("读取[kpi_imei.txt]文件内容出错");
			e.printStackTrace();
		}
		return alist;
	}
	public static Map<String, Terimei> readKpiImeiMap(){
		String filePath = KPIConfig.getKpiImeiFile(); //"./target/classes/kpi_imei.txt"
		Map<String, Terimei> map = new HashMap<String, Terimei>();
		
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] sline = lineTxt.split("\\|");
					Terimei ter = new Terimei();
					ter.setImei(sline[0]);
					ter.setTerfac_id(Integer.parseInt(sline[1]));
					ter.setTertype_id(Integer.parseInt(sline[2]));
					ter.setTerclass_id(Integer.parseInt(sline[3]));
					
					map.put(sline[0]+"", ter);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件[kpi_imei.txt]");
			}
		} catch (Exception e) {
			System.out.println("读取[kpi_imei.txt]文件内容出错");
			e.printStackTrace();
		}
		return map;
	}
	
	public static void main(String[] args) {
		long first = System.currentTimeMillis();
		List<Terimei> list = readKpiImeiList();
		Terimei terimei=null;
		for (Terimei ter : list) {
			if("35882858".equals(ter.getImei())){
				terimei = ter;
				break;
			}
		}
		long last = System.currentTimeMillis();
		System.out.println("时间:"+(last-first)+"s, fac:"+terimei.getTerfac_id()+", type:"+terimei.getTertype_id()+", code:"+terimei.getTerclass_id());
		
		first = System.currentTimeMillis();
		Map<String, Terimei> map = readKpiImeiMap();
		Terimei terimei2 = map.get("35882858");
		last = System.currentTimeMillis();
		System.out.println("时间:"+(last-first)+"s, fac:"+terimei2.getTerfac_id()+", type:"+terimei2.getTertype_id()+", code:"+terimei2.getTerclass_id());
		
		
		first = System.currentTimeMillis();
		Terimei terimei3 = map.get("35882858");
		last = System.currentTimeMillis();
		System.out.println("时间:"+(last-first)+"s, fac:"+terimei3.getTerfac_id()+", type:"+terimei3.getTertype_id()+", code:"+terimei3.getTerclass_id());
		
	}
}

