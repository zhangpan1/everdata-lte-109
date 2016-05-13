package com.eversec.lte.kafka.consumer;

public interface KafkaConsumerHandler {

	public void messageReceived(byte[] message) throws Exception;

}
