package com.eversec.lte.river.utils.exam;

import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSourceCellMR;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.river.utils.EsFlatFileCurlUtil;
import com.eversec.lte.river.utils.EsMappingCurlUtil;
import com.eversec.lte.river.utils.EsRiverCurlUtil;
import com.eversec.lte.river.utils.FieldType;

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
@SuppressWarnings("rawtypes") 
public class Examle {
	public static void main(String[] args) throws Exception {
		String host = "172.168.3.34";
		int port = 9200;
		String index = "gbiups_source";

		// mapping
		EsMappingCurlUtil emcu = new EsMappingCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 由于继承父类中有部分属性，父类的属性会先放在前面，如果不用集成则不用重写createClazzFields
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		};
		emcu.printMapping(host, port, index, XdrSingleSourceS1MME.class);

		// flatfile
		EsFlatFileCurlUtil cec = new EsFlatFileCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 同上
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		};
		cec.printRiverName(XdrSingleSourceS1MME.class);
		cec.printRiver(host, "/root/gbrun/", false, ",", ".*\\.csv$", index,
				XdrSingleSourceCellMR.class);

		// mina
		EsRiverCurlUtil ri = new EsRiverCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 同上
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		};
		ri.printRiver(host, 9009, index, XdrSingleSourceS1MME.class);
	}
}
