package com.eversec.lte.sdtp.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

import com.eversec.lte.cache.ExternalCache;
import com.eversec.lte.cache.ValueGetHandler;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.processor.backfill.AbstractBackFill.RuleType;
import com.eversec.lte.vo.DataQueueCache;
import com.eversec.lte.vo.HostAndPortVo;

public class JedisTools implements ExternalCache {
	private static Logger LOGGER = LoggerFactory.getLogger(JedisTools.class);

	public static final String EX = "ex";// 单位：秒
	public static final String NX = "nx";// key不存在，则存储，存在则不存。

	public ShardedJedisPool pool;
	public volatile boolean connect = false;

	public static AtomicLong SET_COUNT = new AtomicLong(0);
	public static AtomicLong GET_COUNT = new AtomicLong(0);

	// public static ConcurrentHashSet<String> SET_KEYS = new
	// ConcurrentHashSet<String>();
	// public static ConcurrentHashSet<String> GET_KEYS = new
	// ConcurrentHashSet<String>();

	@Override
	public void log(long period) {
		String str = "_CACHE :";
		str += ",";
		str += "set : " + (SET_COUNT.get() / period) + "/s";
		str += ",";
		str += "get : " + (GET_COUNT.get() / period) + "/s";
		// str += ",";
		// str += "all get keys : " + GET_KEYS.size();
		// str += ",";
		// str += "all set keys : " + SET_KEYS.size();
		SET_COUNT.set(0);
		GET_COUNT.set(0);
		LOGGER.info(str);
	}

	private static final int CACHE_SIZE = 10;

	// // 20万
	protected TransferQueue<JedisTools.Data> SET_QUEUE = new LinkedTransferQueue<JedisTools.Data>();
	// 20万
	protected TransferQueue<JedisTools.Data> GET_QUEUE = new LinkedTransferQueue<JedisTools.Data>();

	protected static final String CACHE_TYPE = "jedisSet";

	private final ThreadLocal<DataQueueCache<Data>> _PUBCACHE = new ThreadLocal<DataQueueCache<Data>>() {
		protected DataQueueCache<Data> initialValue() {
			return new DataQueueCache<Data>(CACHE_TYPE) {
				public Data[] createCache(int size) {
					return new Data[CACHE_SIZE];
				}
			};
		};
	};

	protected boolean canSet = false;
	protected boolean canGet = false;

	public JedisTools(boolean canSet, boolean canGet) {
		init();
		int redisBatchNum = SdtpConfig.getRedisBatchNum();
		startConsumer(redisBatchNum, canSet, canGet);
	}

	public JedisTools(int redisBatchNum, boolean canSet, boolean canGet) {
		init();
		startConsumer(redisBatchNum, canSet, canGet);
	}

	private void startConsumer(int redisBatchNum, boolean canSet, boolean canGet) {
		this.canSet = canSet;
		this.canGet = canGet;
		for (int i = 0; i < SdtpConfig.getRedisThreads(); i++) {
			if (this.canSet) {
				LteMain.PROCESS_EXEC.execute(new SetWorkerTransfer(
						redisBatchNum/* / CACHE_SIZE */));
			}
			if (this.canGet) {
				LteMain.PROCESS_EXEC.execute(new GetWorkerTransfer(
						redisBatchNum));
			}
		}
	}

	public final void init() {

		JedisSentinelPool sp;

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
			synchronized (JedisTools.class) {
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

	public void set(String type, String key, String value) {
		set(type, key, value, 3600 * 24 * 30);
	}

	public void set(String type, String key, String value, int ttl) {
		try {
			Data data = new Data(key, value);
			data.ttl = ttl;
			SET_QUEUE.transfer(data);
			// SET_QUEUE.tryTransfer(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String get(String type, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getAsyn(String type, String key, ValueGetHandler handler) {
		try {
			GET_QUEUE.transfer(new Data(key, handler));
			// GET_QUEUE.tryTransfer(new Data(key, handler));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void get(String type,String key, JedisValueGetHandler handler) {
	// try {
	// GET_QUEUE.transfer(new Data(key, handler));
	// // GET_QUEUE.tryTransfer(new Data(key, handler));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Deprecated
	public void publish(String topic, String value) {

		Data[] cache = _PUBCACHE.get().addAndGet(new Data(topic, value));
		if (cache != null) {
			try {
				Collection<Jedis> jedisList = pool.getResource().getAllShards();
				if (jedisList.size() > 0) {
					Jedis j = jedisList.iterator().next();
					Transaction t = j.multi();
					for (int i = 0; i < cache.length; i++) {
						Data data = cache[i];
						t.publish(data.key, data.value);
					}
					t.exec();
				}
			} catch (redis.clients.jedis.exceptions.JedisConnectionException ex) {
				restart();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Deprecated
	public void subscribe(String topic, final JedisTopicGetHandler handler) {
		try {
			Collection<Jedis> jedisList = pool.getResource().getAllShards();
			if (jedisList.size() > 0) {
				Jedis j = jedisList.iterator().next();
				j.subscribe(new JedisPubSub() {
					@Override
					public void onUnsubscribe(String channel,
							int subscribedChannels) {
					}

					@Override
					public void onSubscribe(String channel,
							int subscribedChannels) {
					}

					@Override
					public void onPUnsubscribe(String pattern,
							int subscribedChannels) {
					}

					@Override
					public void onPSubscribe(String pattern,
							int subscribedChannels) {
					}

					@Override
					public void onPMessage(String pattern, String channel,
							String message) {
					}

					@Override
					public void onMessage(String channel, String message) {
						handler.onReceive(channel, message);
					}
				}, topic);
			}
		} catch (redis.clients.jedis.exceptions.JedisConnectionException ex) {
			restart();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SetWorkerTransfer implements Runnable {
		private long index = 1;
		final int redisBatchNum;

		public SetWorkerTransfer(int redisBatchNum) {
			this.redisBatchNum = redisBatchNum;
		}

		@Override
		public void run() {
			while (true) {
				ShardedJedis one = null;
				try {
					one = pool.getResource();
					ShardedJedisPipeline pipeline = one.pipelined();
					while (true) {
						try {
							// Data[] datas = SET_QUEUE.take();
							// for (Data data : datas) {
							Data data = SET_QUEUE.take();
							if (data.ttl > 0) {
								pipeline.set(data.key, data.value, NX, EX,
										data.ttl);
							} else {
								pipeline.set(data.key, data.value);
							}
							index++;
							if (index >= redisBatchNum) {
								pipeline.sync();// close the pipeline
								pipeline = one.pipelined();
								index = 1;
							}
							SET_COUNT.incrementAndGet();
							// SET_KEYS.add(data.key);
							// }
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (redis.clients.jedis.exceptions.JedisConnectionException ex) {
					try {
						if (one != null) {
							pool.returnResource(one);
						}
					} catch (Exception e) {
					}
					restart();
				} catch (Exception e) {
					e.printStackTrace();
					if (one != null) {
						pool.returnResource(one);
					}
				} finally {

				}
			}
		}
	}

	private class GetWorkerTransfer implements Runnable {
		private long index = 1;

		final int redisBatchNum;

		public GetWorkerTransfer(int redisBatchNum) {
			this.redisBatchNum = redisBatchNum;
		}

		@Override
		public void run() {
			while (true) {
				List<Data> dataList = new ArrayList<Data>();
				ShardedJedis one = null;
				try {
					one = pool.getResource();
					ShardedJedisPipeline pipeline = one.pipelined();
					while (true) {
						try {
							Data data = GET_QUEUE.take();
							dataList.add(data);
							pipeline.get(data.key);
							index++;
							if (index >= redisBatchNum) {
								List<Object> result = pipeline
										.syncAndReturnAll();// close the
															// pipeline
								if (result.size() > 0) {
									Iterator<Object> it = result.iterator();
									for (Data adata : dataList) {
										if (it.hasNext()) {
											Object value = it.next();
											if (value != null) {
												adata.handler.onValueReturn(
														RuleType.IMSI.name(),
														adata.key,
														value.toString());
												GET_COUNT.incrementAndGet();
												// GET_KEYS.add(data.key);
											} else {
												adata.handler.onValueReturn(
														RuleType.IMSI.name(),
														adata.key, null);
											}
										}
									}
								}
								index = 1;
								dataList.clear();
								pipeline = one.pipelined();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (redis.clients.jedis.exceptions.JedisConnectionException ex) {
					try {
						if (one != null) {
							pool.returnResource(one);
						}
					} catch (Exception e) {
					}
					restart();
				} catch (Exception e) {
					e.printStackTrace();
					if (one != null) {
						pool.returnResource(one);
					}
				} finally {

				}
			}
		}
	}
}
