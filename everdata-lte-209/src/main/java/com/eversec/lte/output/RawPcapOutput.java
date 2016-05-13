package com.eversec.lte.output;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.raw.XdrRawPayData;
import com.eversec.lte.sdtp.pcap.PcapUtil;

public class RawPcapOutput {
	private static Logger logger = LoggerFactory.getLogger(RawPcapOutput.class);

	// 存放回填后等待输出的单接口RAW
	public static TransferQueue<XdrRawPayData> RAW_QUEUE = new LinkedTransferQueue<>();

	static {
		if (true) {
			init();
		}
	}

	public static void output(XdrRawPayData payData) {
		if (true) {
			try {
				RAW_QUEUE.put(payData);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void init() {
		LteMain.FILE_EXEC.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("RawPcapOutput start ! ");
				while (true) {
					try {
						XdrRawPayData raw = RAW_QUEUE.take();
						PcapUtil.writeRawToPcap(raw, System.currentTimeMillis());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		LteMain.SCHEDULER.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				PcapUtil.tryFlush();
			}
		}, 1, 1, TimeUnit.MINUTES);
	}

}
