package com.eversec.lte.sdtp.tokafka;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.sdtp.file.BaseTest;
import com.eversec.lte.sdtp.file.SdtpFileGroupingOutputTools;

//420194 tps
public class KafkaFormatTest extends BaseTest {

	public static volatile boolean testEx = false;

	public static void main(String[] args) {
		try {
			final XdrSingleSource xdr = createRandom();
			long SIZE = 20000000;
			long t = System.currentTimeMillis();
			for (int i = 0; i < SIZE; i++) {
				SdtpToKafkaOutputTools.output(xdr);
				SdtpToKafkaOutputTools.outputFilled(xdr);
			}
			System.out.println(":" + (SIZE) * 1000L
					/ (System.currentTimeMillis() - t));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static int index = 0;

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Date d1;
	static Date d2;
	static Date d3;

	static {
		try {
			d1 = format.parse("2015-10-30 09:50:51");
			d2 = format.parse("2015-10-30 10:50:51");
			d3 = format.parse("2015-10-30 11:50:51");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static XdrSingleSource createRandom() {
		final XdrSingleSourceS1U s1u = createS1U();
		index++;
		if (index % 3 == 0) {
			s1u.getBusinessCommon().setStartTime(d1);
		} else if (index % 3 == 1) {
			s1u.getBusinessCommon().setStartTime(d2);
		} else if (index % 3 == 2) {
			s1u.getBusinessCommon().setStartTime(d3);

		}
		s1u.setProduceStartTime(s1u.getBusinessCommon().getStartTime()
				.getTime());
		s1u.setProduceEndTime(s1u.getBusinessCommon().getEndTime().getTime());

		return s1u;
		// if (++index % 2 == 0) {
		// final XdrSingleSourceS1U s1u = createS1U();
		// s1u.setProduceStartTime(s1u.getBusinessCommon().getStartTime()
		// .getTime());
		// s1u.setProduceEndTime(s1u.getBusinessCommon().getEndTime()
		// .getTime());
		//
		// return s1u;
		// } else {
		// final XdrSingleSourceS1MME mme = createS1MME();
		// mme.setProduceStartTime(mme.getProduceStartTime());
		// mme.setProduceEndTime(mme.getProduceEndTime());
		//
		// return mme;
		// }

	}

}
