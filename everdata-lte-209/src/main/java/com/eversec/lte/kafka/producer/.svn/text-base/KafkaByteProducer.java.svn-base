package com.eversec.lte.kafka.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.eversec.lte.config.SdtpConfig;

/**
 * 
 * @author bieremayi
 * 
 */
public class KafkaByteProducer {

	private ProducerConfig config;
	private Producer<String, byte[]> producer;

	public KafkaByteProducer(String brokerList) {
		Properties props = new Properties();
		props.put("metadata.broker.list", brokerList);
		props.put("serializer.class", "kafka.serializer.DefaultEncoder");
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		props.put("batch.num.messages", "500");// 500
		// 1.async 2.sync(default)
		props.put("producer.type", "async");
		// 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失 值为0,1,-1,可以参考
		// http://kafka.apache.org/08/configuration.html
		props.put("request.required.acks",
				SdtpConfig.getKafkaRequestRequiredAcks());
		config = new ProducerConfig(props);
		producer = new Producer<>(config);
	}

	public void sendBytes(String topic, byte[] bytes) {
		KeyedMessage<String, byte[]> message = new KeyedMessage<String, byte[]>(
				topic, bytes);
		producer.send(message);
	}

	public void close() {
		producer.close();
	}
}
