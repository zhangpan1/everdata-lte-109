package com.eversec.lte.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.vo.HostAndPortVo;

public class JeditClient {
	private static Logger logger = LoggerFactory.getLogger(JeditClient.class);
	public static volatile ShardedJedisPool SHARDED_JEDIS_POOL;
	static {
		if (SHARDED_JEDIS_POOL == null) {
			ArrayList<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
			List<HostAndPortVo> hostAndPorts = SdtpConfig.getRedisHostAndPort();
			for (HostAndPortVo vo : hostAndPorts) {
				list.add(new JedisShardInfo(vo.getHost(), vo.getPort(),
						2000 * 10));
			}
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(1024);
			config.setMaxIdle(200);
			config.setMaxWaitMillis(1000 * 100);
			config.setTestOnBorrow(false);
			config.setTestOnReturn(false);
			SHARDED_JEDIS_POOL = new ShardedJedisPool(config, list);
		}
	}

	public static void init() {
		logger.info("init redis client.");
	}

	public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	// public static void sortedSetTest() {
	// ShardedJedis resource = SHARDED_JEDIS_POOL.getResource();
	// // resource.set("foo","bar");
	// // System.out.println(resource.get("foo"));
	// // SHARDED_JEDIS_POOL.returnResource(resource);
	// String key = "0_15652980918";
	// // resource.zadd(key, 5d, "test5");
	// // Set<String> set = resource.zrange(key, 0, -1);
	// // for(String str : set){
	// // System.out.println(str);
	// // }
	// HostAndPortVo hp = new HostAndPortVo("192.168.200.144", 5566);
	// // resource.zadd(SafeEncoder.encode(key), (double)new
	// // Date().getTime(),ObjectToByte(hp));
	// // resource.zremrangeByRank(key.getBytes(), 0, -1);
	// Kryo kryo = new Kryo();
	// FieldSerializer<?> serializer = new FieldSerializer<HostAndPortVo>(
	// kryo, HostAndPortVo.class);
	// kryo.register(HostAndPortVo.class, serializer);
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	// Output output = new Output(stream);
	// kryo.writeObject(output, hp);
	// output.close(); // Also calls output.flush()
	//
	// byte[] buffer = stream.toByteArray(); // Serialization done, get bytes
	// System.out.println(buffer.length);
	// // resource.zadd(SafeEncoder.encode(key), (double)new
	// // Date().getTime(),buffer);
	// Set<byte[]> bset = resource.zrange(SafeEncoder.encode(key), 0, -1);
	// System.out.println(bset.size());
	// for (byte[] bs : bset) {
	// System.out.println(kryo.readObject(
	// new Input(new ByteArrayInputStream(bs)),
	// HostAndPortVo.class).getHost());
	// System.out.println(bs.length);
	// }
	// // Deserialize the serialized object.
	// HostAndPortVo object = kryo.readObject(new Input(
	// new ByteArrayInputStream(buffer)), HostAndPortVo.class);
	// System.out.println(object.getHost());
	// }

	public static void main(String[] args) throws IOException {
		// int count = 10000;
		// long start = System.currentTimeMillis();
		// ShardedJedis resource = SHARDED_JEDIS_POOL.getResource();
		// for (int i = 0; i < count; i++) {
		// String value = resource.get("key");
		// System.out.println(value);
		// }
		// SHARDED_JEDIS_POOL.returnResource(resource);
		// System.out.println("cost : " + (System.currentTimeMillis() - start));
		// Jedis jedis = new Jedis("192.168.200.234", 6379);
		// System.out.println(jedis.get("tlcb_8583_resp_mills"));
		// String channels = "tv1";
		// JedisPubSub jedisPubSub = new JedisPubSub() {
		//
		// @Override
		// public void onUnsubscribe(String channel, int subscribedChannels) {
		// logger.info(
		// "TYPE:{},channel {} subscribedChannels:{} ",
		// new String[] { "onUnsubscribe", channel,
		// String.valueOf(subscribedChannels) });
		// }
		//
		// @Override
		// public void onSubscribe(String channel, int subscribedChannels) {
		// logger.info(
		// "TYPE:{},channel {} subscribedChannels:{} ",
		// new String[] { "onSubscribe", channel,
		// String.valueOf(subscribedChannels) });
		// }
		//
		// @Override
		// public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// logger.info("TYPE:{},subscribedChannels:{} ", new String[] {
		// "onPUnsubscribe", String.valueOf(subscribedChannels) });
		// }
		//
		// @Override
		// public void onPSubscribe(String pattern, int subscribedChannels) {
		// logger.info("TYPE:{},subscribedChannels:{} ", new String[] {
		// "onPSubscribe", String.valueOf(subscribedChannels) });
		// }
		//
		// @Override
		// public void onPMessage(String pattern, String channel,
		// String message) {
		// logger.info(
		// "TYPE:{},pattern {} channel {} message : {}",
		// new String[] { "onPMessage", pattern, channel, message });
		// }
		//
		// @Override
		// public void onMessage(String channel, String message) {
		// logger.info("TYPE:{},channel {} message : {}", new String[] {
		// "onMessage", channel, message });
		// }
		// };
		// jedis.subscribe(jedisPubSub, channels);
		// jedis.publish("tv2", "publish tv2 test");
	}
}
