package com.eversec.lte.river.utils.exam;

import java.util.Date;

import com.eversec.lte.river.utils.EsFlatFileCurlUtil;
import com.eversec.lte.river.utils.EsMappingCurlUtil;
import com.eversec.lte.river.utils.EsMetaData;

@EsMetaData(classIndexName="S1_MME_XDR_M_ATTACH_STAT")
public class S1MmeXdrMAttachStat {
	@EsMetaData(dataType="date",format="yyyyMMdd")
	Date startdate;//	开始时间	dateTime	8	开始时间	LTE_S1_MME_XDR的字段type取值为1取字段startdate		★	
	String city;//	城市	string		城市	LTE_S1_MME_XDR的字段type取值为1取字段city		★	
	@EsMetaData(filedName="ever_info_cell")
	String area;//	区域	string		城市下属的区域			★	ever_info_cell
	String mmeip;//	MME IP地址	string		MME IP地址	LTE_S1_MME_XDR的字段type取值为1取字段mmeip		★	
	String eNBip;//	eNB IP地址	string		eNB IP地址	LTE_S1_MME_XDR的字段type取值为1取字段eNBip		★	
	long tac;//	tac	long		tac	LTE_S1_MME_XDR的字段type取值为1取字段tac		★	
	long cellid;//	小区ID	long		小区ID	LTE_S1_MME_XDR的字段type取值为1取字段cellid		★	
	@EsMetaData(filedName="sub_type")
	long subType;//sub_type	子类型	int		子类型	LTE_S1_MME_XDR的字段type取值为1取字段keyword1，keyword1取值1或2或6或7标记为“0”，keyword1取值为1标记为“1”，keyword1取值为2标记为“2”		★	
	@EsMetaData(filedName="failure_cause")
	int failureCause;//failure_cause	失败原因	int		失败原因码	LTE_S1_MME_XDR的字段type取值为1取字段failure_cause		★	
	@EsMetaData(index=false)
	int status;//	流程状态	int		流程状态	LTE_S1_MME_XDR的字段type取值为1，取字段status，不同的值代表不同的流程状态			
	@EsMetaData(filedName="delay_sum",index=false)
	long delaySum;//delay_sum	总时延	long		总时延	LTE_S1_MME_XDR的字段type取值为1，且字段status等于0的条件下，（字段enddate-字段startdate）累加			
	@EsMetaData(filedName="req_num",index=false)
	int reqNum;//req_num	请求次	int		请求次数	LTE_S1_MME_XDR的字段type取值为1的数量			
 
	
	public static void main(String[] args) throws Exception {
		String host = "172.168.3.34";
		int port = 9200;
		String index = "gbiups_source";

		// mapping
		EsMappingCurlUtil emcu = new EsMappingCurlUtil() ;
		emcu.printMapping(host, port, index, S1MmeXdrMAttachStat.class);

		// flatfile
		EsFlatFileCurlUtil cec = new EsFlatFileCurlUtil()  ;
		cec.printRiverName (S1MmeXdrMAttachStat.class );
		cec.printRiver(host, "/root/gbrun/", false, ",", ".*\\.csv$", index,
				S1MmeXdrMAttachStat.class);

		// mina
//		EsRiverCurlUtil ri = new EsRiverCurlUtil() ;
//		ri.printRiver(host, 9009, index, S1MmeXdrMAttachStat.class); 
	}
}
