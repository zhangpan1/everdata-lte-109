package com.eversec.lte.main.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;

import com.eversec.common.constant.CommonConstants;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.sdtp.file.BaseTest;

public class WriteTest extends BaseTest{
	public static void main(String[] args) {
		try {
			long a = 1024 * 1024 * 1024 * 10L;
//			System.out.println(a);
			File f1 = new File(args[0]);
			String type = args[1];
			switch (type) {
			case "1":
				test1(f1);
				break;
			case "2":
				test2(f1);
				break;
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private static void test3(File f) throws Exception {
//		XdrSingleSourceS1U s1u = createS1U();
//		int len = 0;
//		for (String s : s1u.toStringArr()) {
//			len += s.getBytes().length;
//		}
//		long t = System.currentTimeMillis();
//		int total = 0;
//
//		CSVWriter w1 = new CSVWriter(
//				new BufferedWriter(new FileWriter(f, true)),
//				CommonConstants.VERTICAL_CHAR, CSVWriter.NO_QUOTE_CHARACTER,
//				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
//
//		while (true) {
//			StringBuilder sb = new StringBuilder();
//			for (String s : s1u.toStringArr()) {
//				sb.append(s);
//				sb.append("|");
//			}
//			w1.writeNext(new String[] { sb.toString() });
//			total += len;
//			if (total > 1024 * 1024 * 1024 ) {
//				break;
//			}
//		}
//		w1.flush();
//		w1.close();
//
//		System.out.println("speed:"
//				+ (total * 1.0d * 1000 / (System.currentTimeMillis() - t)
//						/ 1024 / 1024) + "MB");
//
//	}

	private static void test2(File f) throws Exception {
		XdrSingleSourceS1U s1u = createS1U();
		int len = 0;
		for (String s : s1u.toStringArr()) {
			len += s.getBytes().length;
		}
		long t = System.currentTimeMillis();
		long total = 0;

		CSVWriter w1 = new CSVWriter(
				new BufferedWriter(new FileWriter(f, true)),
				CommonConstants.VERTICAL_CHAR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		while (true) {
			StringBuilder sb = new StringBuilder();
			for (String s : s1u.toStringArr()) {
				sb.append(s);
				sb.append("|");
			}
			w1.writeNext(new String[] { sb.toString() });
			total += len;
			if (total > 1024 * 1024 * 1024 *  10L) {
				break;
			}
		}
		w1.flush();
		w1.close();

		System.out.println("speed:"
				+ (total * 1.0d * 1000 / (System.currentTimeMillis() - t)
						/ 1024 / 1024 /  10L ) + "MB");

	}

	// speed:0.16883767136639047MB
	private static void test1(File f) throws Exception {
		String line = "2290|024|11|0000000000000000d9083313a1b02cd3|6|460077105352406|866367020266223|8615710572294|1|100.74.250.83|100.74.239.42|2152|2152|2462298118|25444444|16651|249687563|CMNET.MNC007.MCC460.GPRS|103|1440123824865|1440123824941|1|99|1|255|2|10.50.135.108||1829|0|183.232.121.161||80|904|142|1|2|0|0|0|0|76|58|0|0|0|76|65535|1460|0|0|2|3|0|200|76|76|349|pingfore.qq.com|http%3A%2F%2Fpingfore.qq.com%2Fpingd%3Fdm%3Dhuanle.qq.com%26url%3D%2Fact%2Fa20150423calenda%2Findex.html%26arg%3D-%26rdm%3D-%26rurl%3D-%26rarg%3D-%26ied_rf%3D--%26ied_qq%3D-%26pvid%3D6280069952%26scr%3D1440x900%26scl%3D32-bit%26lang%3Dzh-cn%26java%3D1%26cc%3Dx86%26pf%3DWin32%26tz%3D-8%26flash%3D12.0%26ct%3Dlan%26vs%3D3.0.2%26custvar%3D-%26ext%3D0%26reserved1%3D%26rand%3D28544%26tt%3D||Mozilla%2F4.0+%28compatible%3B+MSIE+7.0%3B+Windows+NT+5.1%3B+Trident%2F4.0%3B+InfoPath.2%29||http%3A%2F%2Fhuanle.qq.com%2Fact%2Fa20150423calenda%2Findex.html|pt2gguin%3Do0344700644%3B+RK%3DkYPbys%2FIYf%3B+ptcz%3D0f79f751505a1c2f507ae3af4a8d1ba6ac5961c65ec34d4e5047ac1c435024ed%3B+pgv_pvid%3D6280069952%3B+o_cookie%3D344700644%3B+uin_cookie%3D344700644%3B+euin_cookie%3D5459F7D611F24B8EB234696DBFE468E0844DC7EA3935B251%3B+uin%3Do0344700644%3B+skey%3D%40|0|1|0|0|||3|0|76|0|0";
		int len = line.getBytes().length;
		long t = System.currentTimeMillis();
		long total = 0;

		CSVWriter w1 = new CSVWriter(
				new BufferedWriter(new FileWriter(f, true)),
				CommonConstants.VERTICAL_CHAR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		while (true) {
			w1.writeNext(new String[] { line });
			total += len;
			if (total > 1024 * 1024 * 1024 *  10L) {
				break;
			}
		}
		w1.flush();
		w1.close();

		System.out.println("speed:"
				+ (total * 1.0d * 1000 / (System.currentTimeMillis() - t)
						/ 1024 / 1024 /  10L ) + "MB");

	}

	 
}
