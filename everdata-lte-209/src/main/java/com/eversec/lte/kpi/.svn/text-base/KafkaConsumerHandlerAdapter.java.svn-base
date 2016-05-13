package com.eversec.lte.kpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;

public class KafkaConsumerHandlerAdapter implements KafkaConsumerHandler {
	List<KafkaConsumerHandler> handlers = new ArrayList<KafkaConsumerHandler>();
	
	public KafkaConsumerHandlerAdapter(KafkaConsumerHandler[] array) {
		handlers = Arrays.asList(array);
	}
	
	public void add(KafkaConsumerHandler e){
		this.handlers.add(e);
	}

	@Override
	public void messageReceived(byte[] message) throws Exception {
		for (KafkaConsumerHandler e : handlers) {
			e.messageReceived(message);
		}
	}

}
