package com.hoob.search.redis.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hoob.search.redis.client.RedisClient;
import com.hoob.search.redis.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.redis.utils.CommonConsts;


public final class RedisRoute {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisRoute.class);

	/**
	 * redis集群集合
	 */
	private static final List DCSSLIST = new ArrayList();

	//
	public RedisRoute() {
	}

	//
	public static List getDcsslist() {
		return DCSSLIST;
	}

	/**
	 * 通过key来取redis对象
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 * @see
	 */
	//
	public static RedisClient getRedisClient(String key) throws Exception {
		//
		RedisClient client = null;
		try {
			// 如果有初始化值就去获取
			if (!DCSSLIST.isEmpty()) {
				int hashCode = AlgorithmUtil.getFNVHashCode(key);
				int index = Math.abs(hashCode % DCSSLIST.size());
				client = (RedisClient) DCSSLIST.get(index);
			}
		} catch (Exception e) {
			LOGGER.error(String.valueOf(e));
			throw new Exception("Get RedisClient failed!");
		}
		return client;
	}

	/**
	 * 删除指定类型redis
	 * 
	 * @param dataType
	 * @return
	 * @see
	 */
	public static long removeDataByDataType(String dataType) {
		long count = 0L;
		for (Iterator iterator = DCSSLIST.iterator(); iterator.hasNext();) {
			RedisClient redisClient = (RedisClient) iterator.next();
			LOGGER.info((new StringBuilder()).append("Starting to remove ").append(redisClient.getIp()).toString());
			long tempcount = redisClient.delByDataType(dataType);
			LOGGER.info((new StringBuilder()).append("End Remove, data count is: ").append(tempcount).toString());
			count += tempcount;
		}

		return count;
	}

	/**
	 * 删除所有redis服务
	 * 
	 * @see
	 */
	public static void removeAll() {
		RedisClient redisClient;
		for (Iterator iterator = DCSSLIST.iterator(); iterator.hasNext(); LOGGER
				.info((new StringBuilder()).append("End to resetRedis ").append(redisClient.getIp()).toString())) {
			redisClient = (RedisClient) iterator.next();
			LOGGER.info((new StringBuilder()).append("Starting to resetRedis ").append(redisClient.getIp()).toString());
			redisClient.resetRedis();
		}

	}

	/**
	 * 获取key
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 * @return 拼接结果
	 * @see
	 */
	public static String buildKey(String dataType, String key) {
		if (StringUtil.isEmpty(dataType)) {
			return key;
		}

		return dataType + CommonConsts.DOWN_LINE + (key == null ? CommonConsts.EMPTY_STR : key);
	}
}
