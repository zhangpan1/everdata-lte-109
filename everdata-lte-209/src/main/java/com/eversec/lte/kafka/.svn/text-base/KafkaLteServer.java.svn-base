package com.eversec.lte.kafka;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.kafka.consumer.KafkaConsumer;
import com.eversec.lte.kafka.consumer.RawKafkaMessageHandler;
import com.eversec.lte.kafka.consumer.XdrKafkaMessageHandler;
import com.eversec.lte.main.LteMain;

/**
 * 
 * @author bieremayi
 * 
 */
public class KafkaLteServer {
	/**
	 * 从kafka获取xdr数据
	 */
	public static void initXdrDataConsumer() {
		String topic = SdtpConfig.getKafkaXdrTopic();// "xdr";
		for (int i = 0; i < SdtpConfig.getKafkaXdrConsumerThread(); i++) {
			LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, SdtpConfig
					.getKafkaConsumerHostAndPort(),
					new XdrKafkaMessageHandler()));
		}
	}

	/**
	 * 从kafka获取原始码流数据
	 */
	public static void initRawDataConsumer() {
		String topic = SdtpConfig.getKafkaRawTopic();// "raw";
		for (int i = 0; i < SdtpConfig.getKafkaRawConsumerThread(); i++) {
			LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, SdtpConfig
					.getKafkaRawConsumerHostAndPort(),
					new RawKafkaMessageHandler()));
		}
	}
}
