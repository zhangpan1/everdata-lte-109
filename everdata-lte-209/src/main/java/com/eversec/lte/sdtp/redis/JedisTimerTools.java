package com.eversec.lte.sdtp.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import com.eversec.lte.cache.ValueGetHandler;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.processor.backfill.AbstractBackFill.RuleType;
import com.eversec.lte.vo.HostAndPortVo;

public class JedisTimerTools {
	private static Logger LOGGER = LoggerFactory
			.getLogger(JedisTimerTools.class);

	static ScheduledExecutorService sche = Executors.newScheduledThreadPool(5);

	public static final String EX = "ex";// 单位：秒
	public static final String NX = "nx";// key不存在，则存储，存在则不存。

	public ShardedJedisPool pool;
	public volatile boolean connect = false;

	public static AtomicLong SET_COUNT = new AtomicLong(0);
	public static AtomicLong GET_COUNT = new AtomicLong(0);

	public static void statSet(long period) {
		String str = "_CACHE :";
		str += ",";
		str += "set : " + (SET_COUNT.get() / period) + "/s";
		str += ",";
		str += "get : " + (GET_COUNT.get() / period) + "/s";
		SET_COUNT.set(0);
		GET_COUNT.set(0);
		LOGGER.info(str);
	}

	// // 20万
	protected ArrayBlockingQueue<JedisTimerTools.Data> SET_QUEUE = new ArrayBlockingQueue<JedisTimerTools.Data>(
			1000000);
	// 20万
	protected ArrayBlockingQueue<JedisTimerTools.Data> GET_QUEUE = new ArrayBlockingQueue<JedisTimerTools.Data>(
			1000000);

	protected boolean canSet = false;
	protected boolean canGet = false;

	public JedisTimerTools(boolean canSet, boolean canGet) {
		init();
		int redisBatchNum = SdtpConfig.getRedisBatchNum();
		startConsumer(redisBatchNum, canSet, canGet);
	}

	public JedisTimerTools(int redisBatchNum, boolean canSet, boolean canGet) {
		init();
		startConsumer(redisBatchNum, canSet, canGet);
	}

	private void startConsumer(int redisBatchNum, boolean canSet, boolean canGet) {
		this.canSet = canSet;
		this.canGet = canGet;
		if (this.canSet) {

			sche.scheduleAtFixedRate(new SetWorkerTask(redisBatchNum), 1, 1,
					TimeUnit.SECONDS);
		}
		if (this.canGet) {

			sche.scheduleAtFixedRate(new GetWorkerTask(redisBatchNum), 1, 1,
					TimeUnit.SECONDS);
		}
	}

	public final void init() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		List<HostAndPortVo> hostAndPorts = SdtpConfig.getRedisHostAndPort();
		for (HostAndPortVo vo : hostAndPorts) {
			shards.add(new JedisShardInfo(vo.getHost(), vo.getPort(), 2000 * 10));
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(2);
		pool = new ShardedJedisPool(config, shards);

		connect = true;

		LOGGER.info("JedisTools init," + Thread.currentThread().getName());
	}

	public void destroy() {
		if (pool != null) {
			pool.destroy();
		}
	}

	public void restart() {
		LOGGER.info("JedisTools restart," + Thread.currentThread().getName());

		connect = false;
		if (!connect) {
			synchronized (JedisTimerTools.class) {
				if (!connect) {
					destroy();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
					init();
				}
			}
		}
	}

	private class Data {
		String key;
		String value;
		ValueGetHandler handler;
		int ttl = -1;

		public Data(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public Data(String key, ValueGetHandler handler) {
			this.key = key;
			this.handler = handler;
		}

	}

	public void set(String key, String value) {
		set(key, value, 3600 * 24 * 30);
	}

	public void set(String key, String value, int ttl) {
		try {

			Data data = new Data(key, value);
			data.ttl = ttl;
			SET_QUEUE.put(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void get(String key, ValueGetHandler handler) {
		try {
			GET_QUEUE.put(new Data(key, handler));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SetWorkerTask implements Runnable {

		final int max;

		public SetWorkerTask(int redisBatchNum) {
			this.max = redisBatchNum;
		}

		@Override
		public void run() {

			ArrayList<JedisTimerTools.Data> coll = new ArrayList<>();
			int count = SET_QUEUE.drainTo(coll, max);
			if (count > 0) {
				if (coll != null && coll.size() > 0) {
					System.out.println("SetWorkerTask:"
							+ Thread.currentThread().getId() + ":"
							+ coll.size() +",SET_QUEUE:"+SET_QUEUE.size());
					ShardedJedis one = null;
					try {
						one = pool.getResource();
						ShardedJedisPipeline pipeline = one.pipelined();
						for (int i = 0; i < coll.size(); i++) {
							JedisTimerTools.Data data = coll.get(i);
							if (data.ttl > 0) {
								pipeline.set(data.key, data.value, NX, EX,
										data.ttl);
							} else {
								pipeline.set(data.key, data.value);
							}
							SET_COUNT.incrementAndGet();
						}

						pipeline.sync();// close the pipeline
						System.out.println("SetWorkerTask:"
								+ Thread.currentThread().getId() + " finish!");
					} catch (redis.clients.jedis.exceptions.JedisConnectionException ex) {
						restart();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (one != null) {
								pool.returnResource(one);
							}
						} catch (Exception e) {
						}
					}
				}

				coll.clear();
			}
		}
	}

	private class GetWorkerTask implements Runnable {

		final int max;

		public GetWorkerTask(int redisBatchNum) {
			this.max = redisBatchNum;
		}

		@Override
		public void run() {

			ArrayList<JedisTimerTools.Data> coll = new ArrayList<>();
			int count = GET_QUEUE.drainTo(coll, max);
			if (count > 0) {
				if (coll != null && coll.size() > 0) {
					List<Data> dataList = new ArrayList<Data>();
					System.out.println("GetWorkerTask:"
							+ Thread.currentThread().getId() + ":"
							+ coll.size() +",GET_QUEUE:"+GET_QUEUE.size());
					ShardedJedis one = null;
					try {
						one = pool.getResource();
						ShardedJedisPipeline pipeline = one.pipelined();

						for (int i = 0; i < coll.size(); i++) {
							Data data = coll.get(i);
							dataList.add(data);
							pipeline.get(data.key);

							if (count >= max) {
								List<Object> result = pipeline
										.syncAndReturnAll();// close
								processResultAndClear(result, dataList);
								pipeline = one.pipelined();
								count = 0;
							} else {
								count++;
							}

							SET_COUNT.incrementAndGet();
						}

						List<Object> result = pipeline.syncAndReturnAll();// close
						processResultAndClear(result, dataList);

						System.out.println("GetWorkerTask:"
								+ Thread.currentThread().getId() + " finish!");
					} catch (redis.clients.jedis.exceptions.JedisConnectionException ex) {
						restart();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (one != null) {
								pool.returnResource(one);
							}
						} catch (Exception e) {
						}
					}
				}

				coll.clear();
			}
		}

		private void processResultAndClear(List<Object> result,
				List<Data> dataList) {
			if (result.size() > 0) {
				Iterator<Object> it = result.iterator();
				for (Data adata : dataList) {
					if (it.hasNext()) {
						Object value = it.next();
						if (value != null) {
							adata.handler.onValueReturn(RuleType.IMSI.name(),adata.key,
									value.toString());
							GET_COUNT.incrementAndGet();
						} else {
							adata.handler.onValueReturn(RuleType.IMSI.name(),adata.key, null);
						}
					}
				}
			}
			result.clear();
			dataList.clear();

		}
	}

	public static void main(String[] args) {
		int size = 10000000;
		ArrayBlockingQueue<String> list = new ArrayBlockingQueue<String>(size);
		long t = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			list.add(i + "");
		}
		System.out.println(size * 1000 / (System.currentTimeMillis() - t));

	}
}
