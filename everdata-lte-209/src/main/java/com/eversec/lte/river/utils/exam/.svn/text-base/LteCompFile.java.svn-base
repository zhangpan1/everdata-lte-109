package com.eversec.lte.river.utils.exam;

import com.eversec.lte.model.compound.XdrCompoundCommon;
import com.eversec.lte.model.compound.XdrCompoundSourceApp;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.river.utils.EsFlatFileCurlUtil;
import com.eversec.lte.river.utils.EsMappingCurlUtil;
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
public class LteCompFile {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 9200;
		String index = "lte";

		// mapping
		EsMappingCurlUtil emcu = new EsMappingCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 由于继承父类中有部分属性，父类的属性会先放在前面，如果不用集成则不用重写createClazzFields
				return FieldType.toArray(XdrCompoundCommon.class,
						clazz.getDeclaredFields());
			}

		};

		// emcu.setDateFormat( DateUtil.COMPACT_SECOND_FORMAT);
		emcu.setDateTypeLong(true);

		emcu.printMapping(host, port, index, XdrCompoundSourceApp.class);
		emcu.printMapping(host, port, index, XdrCompoundSourceSignaling.class);
		emcu.printMapping(host, port, index, XdrCompoundSourceUEMR.class);

		// flatfile
		EsFlatFileCurlUtil cec = new EsFlatFileCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 同上
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		};

		cec.printRiverNames(XdrCompoundSourceApp.class,
				XdrCompoundSourceSignaling.class, XdrCompoundSourceUEMR.class);
		// cec.setPreName("filled_");

		cec.printRiver(host, "/home/eversec/hecheng/userdata", false, "|",
				".*\\.txt$", index, XdrCompoundSourceApp.class);
		cec.printRiver(host, "/home/eversec/hecheng/sig", false, "|",
				".*\\.txt$", index, XdrCompoundSourceSignaling.class);
		cec.printRiver(host, "/home/eversec/hecheng/app", false, "|",
				".*\\.txt$", index, XdrCompoundSourceUEMR.class);

	}
}
