package com.eversec.lte.river.utils.exam;

import java.util.Date;

import com.eversec.lte.river.utils.EsFlatFileCurlUtil;
import com.eversec.lte.river.utils.EsMappingCurlUtil;
import com.eversec.lte.river.utils.EsMetaData;
import com.eversec.lte.river.utils.EsRiverCurlUtil;

/**
 * river util 例子
 * 
 * <pre>
 * 	<dependency>
 * 			<groupId>org.codehaus.jackson</groupId>
 * 			<artifactId>jackson-mapper-asl</artifactId>
 * 			<version>1.9.13</version>
 * 			<scope>provided</scope>
 * 		</dependency>
 * </pre>
 * 
 * @author lirongzhi
 * 
 */
public class ExamleAn {
	
	static class NoAn{
		String name;
		int age;
		Date birth;
		String desc;
	}
	
	static class UseAn{
		@EsMetaData(filedName = "name1")
		String name;
		@EsMetaData(dataType="long",indexType="not_analyzed")
		int age;
		@EsMetaData(use=false)
		Date birth;
		@EsMetaData(dataType="date",format="yyyy-MM-dd-HH-mm")
		String desc;
		@EsMetaData(store=false,index =false)
		int aaa2;
	}
	public static void main(String[] args) throws Exception {
		String host = "172.168.3.34";
		int port = 9200;
		String index = "gbiups_source";

		// mapping
		EsMappingCurlUtil emcu = new EsMappingCurlUtil() ;
		emcu.printMapping(host, port, index, NoAn.class);
		emcu.printMapping(host, port, index, UseAn.class);

		// flatfile
		EsFlatFileCurlUtil cec = new EsFlatFileCurlUtil()  ;
		cec.printRiverNames(NoAn.class, UseAn.class);
		cec.printRiver(host, "/root/gbrun/", false, ",", ".*\\.csv$", index,
				NoAn.class);
		cec.printRiver(host, "/root/gbrun/", false, ",", ".*\\.csv$", index,
				UseAn.class);

		// mina
		EsRiverCurlUtil ri = new EsRiverCurlUtil() ;
		ri.printRiver(host, 9009, index, NoAn.class);
		ri.printRiver(host, 9009, index, UseAn.class);
	}
}
