package com.eversec.lte.sdtp.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;

public class FileWriteTest extends BaseTest {

	public static volatile boolean testEx = false;

	public static void main(String[] args) {
		try {
			int threads = 4;
			try {
				threads = Integer.parseInt(args[0]);
			} catch (Exception ex) {
			}

			try {
				Integer.parseInt(args[1]);
				testEx = true;
			} catch (Exception ex) {
			}

			test2(threads);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void test2(int threads) throws Exception {

		final ExecutorService exec = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < threads; i++) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					final XdrSingleSource xdr = createRandom();
					for (;;) {
						SdtpFileGroupingOutputTools.output(xdr);
					}
				}
			});
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
