package com.eversec.lte.kafka.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.processor.decoder.XdrCustomBytesDecoder;
import com.eversec.lte.vo.compound.CompMessage;

public class KafkaConsumer implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
	public final static List<KafkaConsumer> CONSUMERS = new ArrayList<>();

	protected ConsumerConnector consumer;
	protected String topic = "mykafka";
	protected String hostAndPort = "192.168.200.127:12181";
	protected KafkaConsumerHandler handler;

	protected AtomicLong count = new AtomicLong(0);

	public KafkaConsumer(String topic, String hostAndPort,
			KafkaConsumerHandler handler) {
		this.topic = topic;
		this.hostAndPort = hostAndPort;
		this.handler = handler;

		CONSUMERS.add(this);
	}

	public static String countInfo() {
		StringBuffer sb = new StringBuffer();
		int i = 1;
		long sum = 0;
		for (KafkaConsumer c : CONSUMERS) {
			sb.append(i++);
			sb.append(":");
			sb.append(c.count.get());
			sb.append(",");

			sum += c.count.get();
		}
		sb.append(" sum:" + sum);
		return sb.toString();
	}
	
	public Properties getProperties(){
		Properties props = new Properties();
		props.put("zookeeper.connect", hostAndPort);
		props.put("group.id", SdtpConfig.getKafkaXdrConsumerGroupId());
		props.put("auto.offset.reset",
				SdtpConfig.getKafkaConsumerAutoOffsetReset());
		props.put("zookeeper.session.timeout.ms",
				SdtpConfig.getZookeeperTimeout());
		props.put("zookeeper.connection.timeout.ms",
				SdtpConfig.getZookeeperTimeout());
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		return props;
	}

	@Override
	public void run() {
		Properties props = getProperties();

		while (true) {
			try {
				consumer = kafka.consumer.Consumer
						.createJavaConsumerConnector(new ConsumerConfig(props));

				Map<String, Integer> topickMap = new HashMap<String, Integer>();
				topickMap.put(topic, 1);
				Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer
						.createMessageStreams(topickMap);
				KafkaStream<byte[], byte[]> stream = streamMap.get(topic)
						.get(0);
				ConsumerIterator<byte[], byte[]> it = stream.iterator();
				LOGGER.info(" KafkaConsumer topic：{} start!", topic);
				while (it.hasNext()) {
					MessageAndMetadata<byte[], byte[]> n = it.next();
					byte[] m = n.message();
					handler.messageReceived(m);
					count.incrementAndGet();
				}
			} catch (Exception e) {
				LOGGER.error("KafkaConsumer error", e);
			} finally {
				if (consumer != null) {
					LOGGER.info(" KafkaConsumer topic：{} shutown!", topic);
					CONSUMERS.remove(consumer);
					consumer.shutdown();
				}
			}
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e1) {
			}
		}
	}

	public static void main(String[] args) {
		final XdrCustomBytesDecoder decoder = new XdrCustomBytesDecoder();
		final AtomicLong xdrcount = new AtomicLong();
		final AtomicLong mrcount = new AtomicLong();
		new KafkaConsumer("s1u-cxdr-with-uemr", "192.168.200.234:2181",
				new KafkaConsumerHandler() {
					@Override
					public void messageReceived(byte[] message)
							throws Exception {
						CompMessage msg = decoder.decode(message);
						mrcount.addAndGet(msg.getMrInfos().size());
						xdrcount.addAndGet(msg.getXdrs().size());
						System.out.println("mrcount : " + mrcount);
						System.out.println("xdrcount : " + xdrcount);
					}
				}).run();
	}
}
