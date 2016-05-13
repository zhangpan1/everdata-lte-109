package com.eversec.lte.river.utils.exam;

import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSourceCellMR;
import com.eversec.lte.model.single.XdrSingleSourceGnC;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS5S8C;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.model.single.s1u.XdrSingleS1UBusinessCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UMobileCommon;
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
public class LteSingleFile {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 9200;
		String index = "lte_filled";

		// mapping
		EsMappingCurlUtil emcu = new EsMappingCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 由于继承父类中有部分属性，父类的属性会先放在前面，如果不用集成则不用重写createClazzFields
				if (clazz == XdrSingleSourceS1U.class) {

					return FieldType.toArray(XdrSingleS1UCommon.class,
							XdrSingleS1UMobileCommon.class,
							XdrSingleS1UBusinessCommon.class, new FieldType(
									"app", String.class, null));

				} else {
					return FieldType.toArray(XdrSingleCommon.class,
							clazz.getDeclaredFields());
				}
			}

		};

		// emcu.setDateFormat( DateUtil.COMPACT_SECOND_FORMAT);
		emcu.setDateTypeLong(true);

		emcu.printMapping(host, port, index, XdrSingleSourceCellMR.class);
		emcu.printMapping(host, port, index, XdrSingleSourceGnC.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS10S11.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS1MME.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS1U.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS5S8C.class);
		emcu.printMapping(host, port, index, XdrSingleSourceS6a.class);
		emcu.printMapping(host, port, index, XdrSingleSourceSGs.class);
		emcu.printMapping(host, port, index, XdrSingleSourceUEMR.class);
		emcu.printMapping(host, port, index, XdrSingleSourceUu.class);
		emcu.printMapping(host, port, index, XdrSingleSourceX2.class);

		// flatfile
		EsFlatFileCurlUtil cec = new EsFlatFileCurlUtil() {
			public FieldType[] createClazzFields(Class clazz) {// 同上
				return FieldType.toArray(XdrSingleCommon.class,
						clazz.getDeclaredFields());
			}

		};
		//cec.setPreName("filled_");
		cec.printRiverNames(XdrSingleSourceCellMR.class,
				XdrSingleSourceGnC.class, XdrSingleSourceS10S11.class,
				XdrSingleSourceS1MME.class, XdrSingleSourceS1U.class,
				XdrSingleSourceS5S8C.class, XdrSingleSourceS6a.class,
				XdrSingleSourceSGs.class, XdrSingleSourceUEMR.class,
				XdrSingleSourceUu.class, XdrSingleSourceX2.class);

		cec.printRiver(host, "/home/eversec/sca/cellmr", false, "|",
				".*\\.txt$", index, XdrSingleSourceCellMR.class);
		cec.printRiver(host, "/home/eversec/sig/gnc", false, "|", ".*\\.txt$",
				index, XdrSingleSourceGnC.class);
		cec.printRiver(host, "/home/eversec/sig/s11", false, "|", ".*\\.txt$",
				index, XdrSingleSourceS10S11.class);
		cec.printRiver(host, "/home/eversec/sig/s1mme", false, "|",
				".*\\.txt$", index, XdrSingleSourceS1MME.class);
		cec.printRiver(host, "/home/eversec/s1u/s1u", false, "|", ".*\\.txt$",
				index, XdrSingleSourceS1U.class);
		cec.printRiver(host, "/home/eversec/sig/s5s8c", false, "|",
				".*\\.txt$", index, XdrSingleSourceS5S8C.class);
		cec.printRiver(host, "/home/eversec/sig/s6a", false, "|", ".*\\.txt$",
				index, XdrSingleSourceS6a.class);
		cec.printRiver(host, "/home/eversec/sig/sgs", false, "|", ".*\\.txt$",
				index, XdrSingleSourceSGs.class);
		cec.printRiver(host, "/home/eversec/sca/uemr", false, "|", ".*\\.txt$",
				index, XdrSingleSourceUEMR.class);
		cec.printRiver(host, "/home/eversec/sca/uu", false, "|", ".*\\.txt$",
				index, XdrSingleSourceUu.class);
		cec.printRiver(host, "/home/eversec/sca/x2", false, "|", ".*\\.txt$",
				index, XdrSingleSourceX2.class);

	}
}
