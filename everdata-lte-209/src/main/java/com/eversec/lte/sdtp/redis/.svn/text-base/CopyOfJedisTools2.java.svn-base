//package com.eversec.lte.sdtp.redis;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedTransferQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TransferQueue;
//import java.util.concurrent.atomic.AtomicLong;
//
//import redis.clients.jedis.JedisPoolConfig;
//import redis.clients.jedis.JedisShardInfo;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPipeline;
//import redis.clients.jedis.ShardedJedisPool;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.vo.HostAndPortVo;
//
//public class CopyOfJedisTools2 {
//	
//	public static final String EX = "ex";// 单位：秒
//	public static final String NX = "nx";// key不存在，则存储，存在则不存。
//
////	public static BlockingQueue<JedisTools.Data> SET_QUEUE = new ArrayBlockingQueue<JedisTools.Data>(SdtpConfig.getDataQueueCapacity() );
//	public static TransferQueue<CopyOfJedisTools2.Data> SET_QUEUE = new LinkedTransferQueue<CopyOfJedisTools2.Data>();
//	//20万
//	public static TransferQueue<CopyOfJedisTools2.Data> GET_QUEUE = new LinkedTransferQueue<CopyOfJedisTools2.Data>();
//	
//	public static final long DRAIN_TASK_SLEEPMILLS = SdtpConfig.getDrainTaskSleepMills();
//
//	public static ShardedJedisPool pool;
//	
//	public static AtomicLong SET_COUNT = new AtomicLong(0);
//	
//	public static String statSet(long period){
//		String str = "set : "+(SET_COUNT.get()/period)+"/s";
//		SET_COUNT.set(0);
//		return str;
//	}
//	
//	static{
//		CopyOfJedisTools2.init();
//	}
//
//	public static final void init() {
//		int redisBatchNum= SdtpConfig.getRedisBatchNum();
//		
//		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
//		List<HostAndPortVo> hostAndPorts = SdtpConfig.getRedisHostAndPort();
//		for (HostAndPortVo vo : hostAndPorts) {
//			shards.add(new JedisShardInfo(vo.getHost(), vo.getPort(),
//					2000 * 10000));
//		}
//		JedisPoolConfig config = new JedisPoolConfig();
//		pool = new ShardedJedisPool(config, shards);
//
//		for (int i = 0; i < SdtpConfig.getRawProcessThread(); i++) { 
//			LteMain.PROCESS_EXEC.execute(new SetWorkerTransfer(redisBatchNum));
//			LteMain.PROCESS_EXEC.execute(new GetWorkerTransfer(redisBatchNum));
//		}
//	}
//
//	private static class Data {
//		String key;
//		String value;
//		JedisValueGetHandler handler;
//		int ttl = -1;
//
//		public Data(String key, String value) {
//			this.key = key;
//			this.value = value;
//		}
//
//		public Data(String key, JedisValueGetHandler handler) {
//			this.key = key;
//			this.handler = handler;
//		}
//
//	}
//
//	public static void set(String key, String value) {
//		try {
//			SET_QUEUE.transfer(new Data(key, value));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void set(String key, String value, int ttl) {
//		try {
//			Data data = new Data(key, value);
//			data.ttl = ttl;
//			SET_QUEUE.transfer(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void get(String key, JedisValueGetHandler handler) {
//		try {
//			GET_QUEUE.transfer(new Data(key, handler));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	public static void publish(String topic, String owner,String value) {
//		 
//	}
//	
//	public static void subscribe(String topic,  JedisTopicGetHandler handler) {
//		 
//	}
//
//	private static class SetWorkerTransfer implements Runnable {
//		private long index = 1;
//		final int redisBatchNum;
//		public SetWorkerTransfer(int redisBatchNum) {
//			this.redisBatchNum = redisBatchNum;
//		}
//
//		@Override
//		public void run() {
//			while (true) {
//				ShardedJedis one = null;
//				try {
//					one = pool.getResource();
//					ShardedJedisPipeline pipeline = one.pipelined(); 
//					while (true) {
//						try {
//							Data data = SET_QUEUE.take();
//							/*if(data.ttl > 0 ){
////								pipeline .set(data.key, data.value, NX, EX, data.ttl);
//								pipeline.set(data.key, data.value, NX, EX, data.ttl);
//							}else{
//								pipeline.set(data.key, data.value);
//							}
//							index++;
//							if (index %  100000redisBatchNum == 0) {
//								pipeline.sync();//close the pipeline
//								pipeline = one.pipelined();
//							}*/
//							SET_COUNT.incrementAndGet();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					if(one != null){
//						pool.returnResource(one);
//					}
//				}
//			}
//		}
//	}
//	
//	
//	/*private static class SetWorkerBlock implements Runnable {
//		final int redisBatchNum;
//		public SetWorkerBlock(int redisBatchNum) {
//			this.redisBatchNum = redisBatchNum;
//		}
//
//		@Override
//		public void run() {
//			
//			while (true) {
//				ShardedJedis one = null;
//				try {
//					one = pool.getResource();
//					ShardedJedisPipeline pipeline = one.pipelined(); 
//					Collection<Data> coll = new ArrayList<>();
//					while (true) {
//						int count = SET_QUEUE.drainTo(coll,
//								redisBatchNum);
//						if (count > 0) {
//							for (Data data : coll) {
//								if(data.ttl > 0 ){
////									pipeline .set(data.key, data.value, NX, EX, data.ttl);
//									pipeline.set(data.key, data.value, NX, EX, data.ttl);
//								}else{
//									pipeline.set(data.key, data.value);
//								}
//								SET_COUNT.incrementAndGet();
//							}
//							pipeline.sync();//close the pipeline
//							pipeline = one.pipelined();
//							coll.clear();
//						} else {
//							try {
//								TimeUnit.MILLISECONDS
//										.sleep(DRAIN_TASK_SLEEPMILLS);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					if(one != null){
//						pool.returnResource(one);
//					}
//				}
//			}
//		}
//	}*/
//
//	private static class GetWorkerTransfer implements Runnable {
//		private long index = 1;
//
//		final int redisBatchNum;
//		public GetWorkerTransfer(int redisBatchNum) {
//			this.redisBatchNum = redisBatchNum;
//		}
//
//		@Override
//		public void run() {
//			while (true) {
//				List<Data> dataList = new ArrayList<Data>();
//				ShardedJedis one = null;
//				try {
//					one = pool.getResource();
//					ShardedJedisPipeline pipeline = one.pipelined();
//					while (true) {
//						try {
//							Data data = GET_QUEUE.take();
//							dataList.add(data);
//							pipeline.get(data.key);
//							index++;
//							if (index % /*100000*/redisBatchNum == 0) {
//								List<Object> result = pipeline
//										.syncAndReturnAll();//close the pipeline
//								if ( result.size() > 0) {
//									Iterator<Object> it = result.iterator();
//									for (Data adata : dataList) {
//										if (it.hasNext()) {
//											Object value = it.next();
//											if (value != null) {
//												adata.handler.onValueReturn(
//														adata.key,
//														value.toString());
//											}
//										}
//									}
//								}
//								dataList.clear();
//								pipeline = one.pipelined();
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					if(one != null){
//						pool.returnResource(one);
//					}
//				}
//			}
//		}
//	}
//}
