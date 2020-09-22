
package com.hoob.search.redis.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.redis.utils.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class JedisUtil {

	/**
	 * 初始化工具类
	 * 
	 * @author jiying
	 * @version 2018年10月29日
	 * @see RedisUtilHolder
	 * @since
	 */
	private static class RedisUtilHolder {

		private static JedisUtil instance = new JedisUtil();

		private RedisUtilHolder() {
		}
	}

	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisUtil.class);

	private static final Map MAPS = new HashMap();

	private JedisUtil() {
	}

	/**
	 * 连接redis，获取JedisPool对象
	 * 
	 * @param ip
	 *            IP
	 * @param port
	 *            端口
	 * @param password
	 *            密码
	 * @return
	 * @see
	 */
	private static JedisPool getPool(String ip, int port, String password) {
		// 拼接IP：端口
		String key = (new StringBuilder()).append(ip).append(":").append(port).toString();

		JedisPool pool = null;

		// 先看是否已经有了
		if (MAPS.containsKey(key)) {
			pool = (JedisPool) MAPS.get(key);

			if (null != pool) {
				return pool;
			}
		}
		LOGGER.warn((new StringBuilder()).append("Get JedisPool for cache map NULL, ip is:").append(ip)
				.append(",port is:").append(port).append(", and then create pool.").toString());

		// 同步处理
		synchronized (JedisUtil.class) {
			// 判断还没有放进去
			if (!MAPS.containsKey(key)) {
				// 设置JedisPoolConfig
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(RedisConfig.getMaxTotal());
				config.setMaxIdle(RedisConfig.getMaxIdle());
				config.setMaxWaitMillis(RedisConfig.getMaxWaitMillis());
				config.setTestOnBorrow(true);
				config.setTestOnReturn(true);

				try {
					// 如果有密码就设置
					if (StringUtil.isNil(password)) {
						pool = new JedisPool(config, ip, port, RedisConfig.getTimeout());
					} else {
						pool = new JedisPool(config, ip, port, RedisConfig.getTimeout(), password);
					}

					LOGGER.info((new StringBuilder()).append("Get JedisPool new JedisPool, ip is:").append(ip)
							.append(",port is:").append(port).toString());

					// 存入map
					MAPS.put(key, pool);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 如果有就直接取出
				pool = (JedisPool) MAPS.get(key);

				LOGGER.info((new StringBuilder()).append("Get JedisPool for cache map, ip is:").append(ip)
						.append(",port is:").append(port).toString());
			}
		}
		return pool;
	}

	/**
	 * 获取JedisUtil对象
	 * 
	 * @return
	 * @see
	 */
	public static JedisUtil getInstance() {
		return RedisUtilHolder.instance;
	}

	/**
	 * 获取redis对象
	 * 
	 * @param ip
	 * @param port
	 * @param password
	 * @return
	 * @see
	 */
	public Jedis getJedis(String ip, int port, String password) {
		Jedis jedis = null;
		int count = 0;

		do {
			try {
				JedisPool pool = getPool(ip, port, password);
				if (null != pool) {
					jedis = pool.getResource();
				}
			} catch (Exception e) {
				LOGGER.error("get redis master failed!", e);
				JedisPool pool = getPool(ip, port, password);
				if (null != pool) {
					pool.returnBrokenResource(jedis);
				}
			}
			count++;
		} while (jedis == null && count < RedisConfig.getRetryNum());

		return jedis;
	}

	/**
	 * 返回redis
	 * 
	 * @param jedis
	 *            jedis对象
	 * @param ip
	 *            ip
	 * @param port
	 *            端口
	 * @param password
	 *            密码
	 * @see
	 */
	public void returnJedis(Jedis jedis, String ip, int port, String password) {
		if (jedis != null) {
			JedisPool pool = getPool(ip, port, password);
			if (null != pool) {
				pool.returnResource(jedis);
			}
		}
	}

}
