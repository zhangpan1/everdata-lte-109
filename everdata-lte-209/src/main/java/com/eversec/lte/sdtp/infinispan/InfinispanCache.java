package com.eversec.lte.sdtp.infinispan;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.infinispan.Cache;
import org.infinispan.commons.util.concurrent.FutureListener;
import org.infinispan.commons.util.concurrent.NotifyingFuture;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.cache.ExternalCache;
import com.eversec.lte.cache.ValueGetHandler;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.processor.backfill.BackFillWithExternalCache.BackFillValueGetHandler;

public class InfinispanCache implements ExternalCache {
	private static Logger LOGGER = LoggerFactory
			.getLogger(InfinispanCache.class);

	public AtomicLong SET_COUNT = new AtomicLong(0);
	public AtomicLong GET_COUNT = new AtomicLong(0);
	public AtomicLong HIT_COUNT = new AtomicLong(0);

	private static InfinispanCache instance = new InfinispanCache();

	public static InfinispanCache getInstance() {
		return instance;
	}

	public void log(long period) {
		String str = "ExternalCache :";
		str += ",";
		str += "set : " + (SET_COUNT.getAndSet(0) / period) + "/s";
		str += ",";
		str += "get : " + (GET_COUNT.getAndSet(0) / period) + "/s";
		str += ",";
		str += "hit : " + (HIT_COUNT.getAndSet(0) / period) + "/s";
		LOGGER.info(str);
		LOGGER.info("now cache member:"
				+ (listener == null ? null : listener.getMembers())
				+ ",local member:"
				+ (listener == null ? null : listener.getLocalMembers())
				+ ",new member:"
				+ (listener == null ? null : listener.getNewMembers())
				+ ",old member:"
				+ (listener == null ? null : listener.getOldMembers()));
	}

	protected Cache<String, String> cache;

	protected ClusterListener listener;

	public InfinispanCache() {
		GlobalConfigurationBuilder global = GlobalConfigurationBuilder
				.defaultClusteredBuilder();
		global.transport().clusterName("sdtp_rule");
		ConfigurationBuilder config = new ConfigurationBuilder();

		config.clustering().cacheMode(CacheMode.DIST_ASYNC);

		boolean usePersistence = SdtpConfig.isExternalCacheUsePersistence();
		if (usePersistence) {

			String dir = SdtpConfig.getExternalCachePersistenceDir();

			new File(dir).mkdirs();

			config.persistence().passivation(false)
					// false 为内存和硬盘都有一份
					.addSingleFileStore().location(dir).async().enable()
					.preload(false)// 启动时候预先读取
					.shared(false);// 如果单台机器有多个程序读一个文件为true;
		}

		DefaultCacheManager cacheManager = new DefaultCacheManager(
				global.build(), config.build());

		this.listener = new ClusterListener();

		cacheManager.addListener(listener);

		cache = cacheManager.getCache();
	}

	public void set(String type, String key, String value) {
		try {
			cache.put(key, value);
			SET_COUNT.incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void set(String type, String key, String value, int ttl) {
		try {
			cache.put(key, value, ttl, TimeUnit.SECONDS);
			SET_COUNT.incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String type, String key) {
		try {
			GET_COUNT.incrementAndGet();
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getAsyn(String type, final String key,
			final ValueGetHandler handler) {
		try {
			GET_COUNT.incrementAndGet();
			NotifyingFuture<String> f = cache.getAsync(key);
			f.attachListener(new FutureListener<String>() {
				@Override
				public void futureDone(Future<String> future) {

					try {
						String value = future.get();
						if (value == null) {

						} else {
							HIT_COUNT.incrementAndGet();
						}
						handler.onValueReturn(null, key, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
}
