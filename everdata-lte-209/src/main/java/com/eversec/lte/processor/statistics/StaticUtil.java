package com.eversec.lte.processor.statistics;

import static com.eversec.lte.processor.data.StaticData.CXDR_TYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.S1U_APPTYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.XDR_PROCEDURE_TYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.XDR_TYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.CXDR_SIGNALING_CACHE;

import com.eversec.lte.model.compound.XdrCompoundSourceSignaling;

/**
 * 统计工具类
 */
public class StaticUtil {

	public static void statXdrType(int Interface) {
		try {
			XDR_TYPE_CACHE.get(Interface + "").getValue().incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void statXdrProcedureType(String procedureType) {
		try {
			XDR_PROCEDURE_TYPE_CACHE.get(procedureType + "").getValue()
					.incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void statS1uAppType(int apptype) {
		try {
			S1U_APPTYPE_CACHE.get(apptype + "").getValue().incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void statCompType(int xdrType) {
		try {
			CXDR_TYPE_CACHE.get(xdrType + "").getValue().incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void statCompSignaling(XdrCompoundSourceSignaling cxdr) {
		try {
			String keyValue = null;
			short proType = cxdr.getProcedureType();
			if(proType == 17 || proType == 18){
				short keyword = cxdr.getKeyword1();
				keyValue = proType+"-"+keyword;
			}else{
				keyValue = proType+"";
			}
			
			CXDR_SIGNALING_CACHE.get(keyValue).getValue().incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
