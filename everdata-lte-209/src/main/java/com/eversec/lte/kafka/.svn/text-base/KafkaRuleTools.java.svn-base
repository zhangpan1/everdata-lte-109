package com.eversec.lte.kafka;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.cache.ExternalCache;
import com.eversec.lte.cache.ValueGetHandler;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
import com.eversec.lte.kafka.consumer.KafkaSimpleConsumer;
import com.eversec.lte.kafka.producer.KafkaStringProducer;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.vo.DataQueueCache;

/**
 * bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor
 * 1 --partitions 1 --topic RuleX ? for RuleX For Test
 * 
 */
public class KafkaRuleTools implements ExternalCache {
	private static Logger LOGGER = LoggerFactory
			.getLogger(KafkaRuleTools.class);

	public static String TOPIC = "RuleX";

	private ArrayBlockingQueue<String[]> publishQueue = new ArrayBlockingQueue<String[]>(
			2000);

	private ValueGetHandler handler;

	public String identify;

	protected static final String CACHE_TYPE_COMP = "rulex";

	private final ThreadLocal<DataQueueCache<String>> RULEX_CACHE = new ThreadLocal<DataQueueCache<String>>() {
		protected DataQueueCache<String> initialValue() {
			return new DataQueueCache<String>(CACHE_TYPE_COMP) {
				public String[] createCache(int size) {
					return new String[SIZE];
				}
			};
		};
	};

	public KafkaRuleTools(ValueGetHandler handler) {
		super();

		this.handler = handler;

		identify = SdtpConfig.getKafkaRuleToolsIdentify();
		if (identify == null || identify.length() == 0) {
			this.identify = getHost();
		}
		LOGGER.info("KafkaRuleTools identify:" + identify);
		init();
	}

	private String getHost() {
		String host = null;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			host = ia.getHostName();// 获取计算机主机名
		} catch (Exception e) {
			e.printStackTrace();
			try {
				host = java.util.UUID.randomUUID().toString();
			} catch (Exception e1) {
			}
		}
		return host;
	}

	@Override
	public void log(long se) {
		LOGGER.info("KafkaRuleTools publishQueue:" + publishQueue.size());
		KafkaSimpleConsumer.log(se);
	}

	public void init() {
		initProducer();
		initConsumer();
	}

	@Override
	public void set(String type, String key, String value) {

		if (key.contains("|") || value.contains("|")) {
			throw new RuntimeException("error format message contains | : "
					+ key + "," + value);
		}
		try {

			String source = identify + "|" + type + "|" + key + "|" + value;
			String[] r = RULEX_CACHE.get().addAndGet(source);
			if (r != null) {
				publishQueue.put(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void set(String type, String key, String value, int ttl) {
		set(type, key, value);
	}

	@Override
	public String get(String type, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getAsyn(String type, String key, ValueGetHandler handler) {
		throw new UnsupportedOperationException();
	}

	private void initProducer() {
		// only one!!

		for (int i = 0; i < SdtpConfig.getKafkaOutputThread(); i++) {
			LteMain.KAFKA_EXEC.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						KafkaStringProducer producer = null;
						try {
							producer = new KafkaStringProducer();
							while (true) {
								Collection<String[]> list = new ArrayList<>();
								int count = publishQueue.drainTo(list);
								if (count > 0) {
									for (String[] msgs : list) {
										for (String msg : msgs) {
											producer.sendMsg(TOPIC, msg);
										}
										// String msg = publishQueue.take();
									}
								} else {
									try {
										Thread.sleep(1);
									} catch (InterruptedException e) {
									}
								}
								list.clear();
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (producer != null) {
								producer.close();
							}
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
							}
						}
					}
				}
			});
		}

	}

	private void initConsumer() {
		// only one!!

		String topic = TOPIC;
		String host = "comp11";
		List<String> seeds = new ArrayList<String>();
		seeds.add(host);
		int port = 9092;
		for (int i = 0; i < 1; i++) {
			final int partition = i;
			LteMain.KAFKA_EXEC.execute(new KafkaSimpleConsumer(topic,
					partition, seeds, port, new RuleXKafkaConsumerHandler()));

		}
	}

	private class RuleXKafkaConsumerHandler implements KafkaConsumerHandler {

		@Override
		public void messageReceived(byte[] message) throws Exception {
			if (handler != null) {
				String str = new String(message);
				String[] arrs = str.split("\\|");
				String identify = arrs[0];
				if (!KafkaRuleTools.this.identify.equals(identify)) {
					String type = arrs[1];
					String key = arrs[2];
					String value = arrs[3];
					handler.onValueReturn(type, key, value);
				}
			}

		}
	}
}
