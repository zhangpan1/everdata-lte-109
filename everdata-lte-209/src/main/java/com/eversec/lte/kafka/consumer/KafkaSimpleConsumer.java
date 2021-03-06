package com.eversec.lte.kafka.consumer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;

/**
 * kafka simple consumer
 * 
 * <pre>
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+SimpleConsumer+Example
 * </pre>
 * 
 * @author lirongzhi
 * 
 */
public class KafkaSimpleConsumer implements Runnable {

	private static Logger LOGGER = LoggerFactory
			.getLogger(KafkaSimpleConsumer.class);

	public static void main(String args[]) {
		String topic = "xdr";
		String host = "comp11";
		List<String> seeds = new ArrayList<String>();
		seeds.add(host);
		int port = 9092;

		for (int i = 0; i < 4; i++) {
			final int partition = i;
			final KafkaSimpleConsumer example = new KafkaSimpleConsumer(topic,
					partition, seeds, port, new KafkaConsumerHandler() {

						@Override
						public void messageReceived(byte[] message)
								throws Exception {
							try {
								IoBuffer buffer = IoBuffer.wrap(message);
								buffer.skip(9);
								List<XdrSingleSource> list = new XdrSingleBytesDecoder()
										.decode(buffer);
								// System.out.println(list.size());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			try {
				new Thread(example).start();
			} catch (Exception e) {
				System.out.println("Oops:" + e);
				e.printStackTrace();
			}
		}

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				KafkaSimpleConsumer.log(5);
			}
		}, 5000, 5000);

	}

	public static final AtomicLong count = new AtomicLong();
	public static final List<KafkaSimpleConsumer> consumers = new ArrayList<>();

	public static void log(long i) {
		LOGGER.info(consumers + " " + count.getAndSet(0) / 5 + "/s");
	}

	protected List<String> m_replicaBrokers = new ArrayList<String>();
	protected String topic;
	protected int partition;
	protected List<String> seedBrokers;
	protected int port;
	protected KafkaConsumerHandler handler;
	protected long readOffset = 0;

	public KafkaSimpleConsumer(String topic, int partition,
			List<String> seedBrokers, int port, KafkaConsumerHandler handler) {
		this.topic = topic;
		this.partition = partition;
		this.seedBrokers = seedBrokers;
		this.port = port;
		this.handler = handler;
		consumers.add(this);// no remove type
	}

	public void run() {
		while (true) {
			try {
				// find the meta data about the topic and partition we are
				// interested in
				//
				PartitionMetadata metadata = findLeader(seedBrokers, port,
						topic, partition);
				if (metadata == null || metadata.leader() == null) {
					LOGGER.error("Can't find metadata or Leader for Topic and Partition:"
							+ topic);
					continue;
				}
				String leadBroker = metadata.leader().host();//得到brokerid
				String clientName = "Client_" + topic + "_" + partition;

				SimpleConsumer consumer = new SimpleConsumer(leadBroker, port,
						100000, 64 * 1024, clientName);
				readOffset = getLastOffset(consumer, topic, partition,
						kafka.api.OffsetRequest.LatestTime(), clientName);

				int numErrors = 0;
				while (true) {
					if (consumer == null) {
						consumer = new SimpleConsumer(leadBroker, port, 100000,
								64 * 1024, clientName);
					}
					FetchRequest req = new FetchRequestBuilder()
							.clientId(clientName)
							.addFetch(topic, partition, readOffset, 100000)
							// Note: this fetchSize of 100000 might need to be
							// increased if large batches are written to Kafka
							.build();
					FetchResponse fetchResponse = consumer.fetch(req);

					if (fetchResponse.hasError()) {
						numErrors++;
						// Something went wrong!
						short code = fetchResponse.errorCode(topic, partition);
						LOGGER.error("Error fetching data from the Broker:"
								+ leadBroker + " Reason: " + code);
						if (numErrors > 5) {
							break;
						}
						if (code == ErrorMapping.OffsetOutOfRangeCode()) {
							// We asked for an invalid offset. For simple case
							// ask for the last element to reset
							readOffset = getLastOffset(consumer, topic,
									partition,
									kafka.api.OffsetRequest.LatestTime(),
									clientName);
							continue;
						}
						consumer.close();
						consumer = null;
						leadBroker = findNewLeader(leadBroker, topic,
								partition, port);
						continue;
					}
					numErrors = 0;

					long numRead = 0;
					for (MessageAndOffset messageAndOffset : fetchResponse
							.messageSet(topic, partition)) {
						long currentOffset = messageAndOffset.offset();
						if (currentOffset < readOffset) {
							System.out.println("Found an old offset: "
									+ currentOffset + " Expecting: "
									+ readOffset);
							continue;
						}
						readOffset = messageAndOffset.nextOffset();
						ByteBuffer payload = messageAndOffset.message()
								.payload();

						byte[] bytes = new byte[payload.limit()];
						payload.get(bytes);

						handler.messageReceived(bytes);
						count.incrementAndGet();

						numRead++;
					}

					if (numRead == 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
						}
					}
				}
				if (consumer != null) {
					consumer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
				}
			}

		}
	}

	public void stop() {
		consumers.remove(this);
	}

	public static long getLastOffset(SimpleConsumer consumer, String topic,
			int partition, long whichTime, String clientName) {
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
				partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
				whichTime, 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
				requestInfo, kafka.api.OffsetRequest.CurrentVersion(),
				clientName);
		OffsetResponse response = consumer.getOffsetsBefore(request);

		if (response.hasError()) {
			LOGGER.error("Error fetching data Offset Data the Broker. Reason: "
					+ response.errorCode(topic, partition));
			return 0;
		}
		long[] offsets = response.offsets(topic, partition);
		return offsets[0];
	}

	private String findNewLeader(String a_oldLeader, String a_topic,
			int a_partition, int a_port) throws Exception {
		for (int i = 0; i < 3; i++) {
			boolean goToSleep = false;
			PartitionMetadata metadata = findLeader(m_replicaBrokers, a_port,
					a_topic, a_partition);
			if (metadata == null) {
				goToSleep = true;
			} else if (metadata.leader() == null) {
				goToSleep = true;
			} else if (a_oldLeader.equalsIgnoreCase(metadata.leader().host())
					&& i == 0) {
				// first time through if the leader hasn't changed give
				// ZooKeeper a second to recover
				// second time, assume the broker did recover before failover,
				// or it was a non-Broker issue
				//
				goToSleep = true;
			} else {
				return metadata.leader().host();
			}
			if (goToSleep) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
				}
			}
		}
		LOGGER.error("Unable to find new leader after Broker failure. Exiting");
		throw new Exception(
				"Unable to find new leader after Broker failure. Exiting");
	}

	private PartitionMetadata findLeader(List<String> a_seedBrokers,
			int a_port, String a_topic, int a_partition) {
		PartitionMetadata returnMetaData = null;
		loop: for (String seed : a_seedBrokers) {
			SimpleConsumer consumer = null;
			try {
				consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024,
						"leaderLookup");
				List<String> topics = Collections.singletonList(a_topic);
				TopicMetadataRequest req = new TopicMetadataRequest(topics);
				// TopicMetadataRequest
				kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

				List<TopicMetadata> metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						if (part.partitionId() == a_partition) {
							returnMetaData = part;
							break loop;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error communicating with Broker [" + seed
						+ "] to find Leader for [" + a_topic + ", "
						+ a_partition + "] Reason: " + e);
			} finally {
				if (consumer != null)
					consumer.close();
			}
		}
		if (returnMetaData != null) {
			m_replicaBrokers.clear();
			for (kafka.cluster.Broker replica : returnMetaData.replicas()) {
				m_replicaBrokers.add(replica.host());
			}
		}
		return returnMetaData;
	}

	@Override
	public String toString() {
		return "{broker=" + m_replicaBrokers + ", off=" + readOffset + "}";
	}

}