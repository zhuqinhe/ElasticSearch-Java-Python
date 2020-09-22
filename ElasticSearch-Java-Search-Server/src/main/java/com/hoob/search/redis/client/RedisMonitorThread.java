// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space
// Source File Name: RedisMonitorThread.java

package com.hoob.search.redis.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.utils.ObjectUtils;


public class RedisMonitorThread extends Thread {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisMonitorThread.class);

	/**
	 * redis客户端对象
	 */
	private RedisClient redisClient;
//
	public RedisMonitorThread(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	@Override
	public void run() {
		do {
			try {
				if (ObjectUtils.isNotEmpty(redisClient)) {
					// 先获取当前状态
					boolean currentStatus = redisClient.isStatus();

					// 然后再去检查一次状态
					redisClient.checkRedisStatus();

					// 再获取一次检查后的状态
					boolean afterStatus = redisClient.isStatus();
					// 如果获取的状态异常，就刷新redis数据
					if (!currentStatus && afterStatus) {
						redisClient.resetRedis();
					}
				}

			} catch (Exception e) {
				LOGGER.error("RedisMonitorThread failed!", e);
			}

			try {
				Thread.sleep(RedisConfig.getCheckTime());
			} catch (Exception e) {
				LOGGER.error(String.valueOf(e));
			}
		} while (true);
	}
}
