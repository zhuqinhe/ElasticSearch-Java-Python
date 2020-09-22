package com.hoob.search.server.impl;

import com.hoob.search.redis.client.RedisClient;
import com.hoob.search.redis.route.RedisRoute;
import com.hoob.search.server.RedisConfigService;
import com.hoob.search.server.RedisServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("redisServer")
public class RedisServerimpl implements RedisServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisServerimpl.class);
	@Autowired
	private RedisConfigService proConfigService;

	@Override
	public void setString(String key, String value) {
		@SuppressWarnings("rawtypes")
        RedisClient redisClient;
		if (proConfigService.getBoolean("REDIS_SWITCH", false)) {
			if (null != value) {
				try {

					redisClient = RedisRoute.getRedisClient("");
					if (null != redisClient) {
						redisClient.setStr(key, value);
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw new RuntimeException();
				}
			}
			expire(key, Integer.parseInt(proConfigService.getString("REDIS_EXPIRED_TIME", "300")));
		} else {
			LOGGER.debug("redis REDIS_SWITCH is false !");
		}
	}

	@Override
	public String getString(String key) {
		@SuppressWarnings("rawtypes")
		RedisClient redisClient;
		if (proConfigService.getBoolean("REDIS_SWITCH", false)) {

			if (null != key) {
				try {

					redisClient = RedisRoute.getRedisClient("");
					if (null != redisClient) {
						return redisClient.getStr(key);
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw new RuntimeException();
				}
			}
		} else {
			LOGGER.debug("redis REDIS_SEITCH is false");
		}
		return null;
	}

	@Override
	public void expire(String key, int time) {
		@SuppressWarnings("rawtypes")
		RedisClient redisClient;
		if (proConfigService.getBoolean("REDIS_SWITCH", false)) {

			if (null != key) {
				try {
					redisClient = RedisRoute.getRedisClient("");
					if (null != redisClient) {
						redisClient.expiredTime(key, time);
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw new RuntimeException();
				}
			}
		} else {
			LOGGER.debug("redis REDIS_SEITCH is false");
		}

	}

}
