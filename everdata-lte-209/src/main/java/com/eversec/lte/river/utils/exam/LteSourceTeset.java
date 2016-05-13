package com.eversec.lte.river.utils.exam;

import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
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
public class LteSourceTeset {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 9200;
		String index = "lte_source";

		// mapping
		EsMappingCurlUtil emcu = new EsMappingCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 由于继承父类中有部分属性，父类的属性会先放在前面，如果不用集成则不用重写createClazzFields
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		};
		//emcu.setDateFormat( DateUtil.COMPACT_SECOND_FORMAT);
		emcu.setDateTypeLong(true);
		
		emcu.printMapping(host, port, index, XdrSingleSourceS10S11.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS1MME.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS6a.class);
		emcu.printMapping(host, port, index, XdrSingleSourceSGs.class);

		// flatfile
		EsFlatFileCurlUtil cec = new EsFlatFileCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 同上
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		}; 
		cec.printRiverNames(XdrSingleSourceS10S11.class,XdrSingleSourceS1MME.class,XdrSingleSourceS6a.class,XdrSingleSourceSGs.class);
		
		cec.printRiver(host, "/home/river/s11data", false, "|", ".*\\.txt$", index,
				XdrSingleSourceS10S11.class);
		cec.printRiver(host, "/home/river/s1mmedata", false, "|", ".*\\.txt$", index,
				XdrSingleSourceS1MME.class);
		cec.printRiver(host, "/home/river/s6adata", false, "|", ".*\\.txt$", index,
				XdrSingleSourceS6a.class);
		cec.printRiver(host, "/home/river/sgsdata", false, "|", ".*\\.txt$", index,
				XdrSingleSourceSGs.class);

	}
}
