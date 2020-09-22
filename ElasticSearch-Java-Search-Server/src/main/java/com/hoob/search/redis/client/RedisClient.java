package com.hoob.search.redis.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hoob.search.redis.vo.BaseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.redis.utils.CommonConsts;
import com.hoob.search.redis.utils.JsonUtil;
import com.hoob.search.redis.utils.SerializeUtil;
import com.hoob.search.redis.utils.StringUtil;

import redis.clients.jedis.Jedis;


public class RedisClient<V> {

	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

	/**
	 * redis状态
	 */
	private boolean status = false;

	/**
	 * Ip
	 */
	private String ip;

	/**
	 * 端口
	 */
	private int port;
	/**
	 * 
	 */
	private int expiredTime;
	/**
	 * 
	 */
	private int expiredTimeArr;
	/**
	 * 密码
	 */
	private String password;

	public String getPassword() {
		return this.password;
	}

	/**
	 * 获取redis客户端
	 * 
	 * @param ip
	 *            IP
	 * @param port
	 *            端口
	 * @param password
	 *            密码
	 */
	public RedisClient(String ip, int port, String password, String[] redisExpiredTimeArr) {
		status = false;
		this.ip = ip;
		this.port = port;
		this.password = password;

		if (null != redisExpiredTimeArr && redisExpiredTimeArr.length == CommonConsts.NUMBER_TWO) {
			this.expiredTime = StringUtil.isEmpty(redisExpiredTimeArr[0]) ? 0
					: Integer.parseInt(redisExpiredTimeArr[0]);
			this.expiredTimeArr = StringUtil.isEmpty(redisExpiredTimeArr[1]) ? 0
					: Integer.parseInt(redisExpiredTimeArr[1]);
		}

		// 检查redis状态
		initRedisStatus();

	}

	/**
	 * 检查Redis状态
	 * 
	 * @see
	 */
	public void checkRedisStatus() {
		boolean status = false;

		Jedis jedis = null;

		try {
			jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

			LOGGER.debug("Starting Check redis status.");

			String value = jedis.get("checkRedisStatusKey");

			if ((StringUtil.isEmpty(value)) || (!"checkRedisStatusValue".equals(value))) {
				jedis.set("checkRedisStatusKey", "checkRedisStatusValue");
				value = jedis.get("checkRedisStatusKey");

				if ((!StringUtil.isEmpty(value)) && ("checkRedisStatusValue".equals(value))) {
					status = true;
				}
			} else {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.error("checkRedisStatus failed!", e);
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
			}
		}

		this.status = status;
		LOGGER.debug("End Check redis status.");
		LOGGER.info("The redis status is:" + status + ", IP:" + this.ip + ", Port:" + this.port);
	}
//
	public void initRedisStatus() {
		boolean status = false;

		Jedis jedis = null;

		try {
			jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);
			String value = jedis.get("checkRedisStatusKey");
			if ((StringUtil.isEmpty(value)) || (!"checkRedisStatusValue".equals(value))) {
				jedis.set("checkRedisStatusKey", "checkRedisStatusValue");
				value = jedis.get("checkRedisStatusKey");

				if ((!StringUtil.isEmpty(value)) && ("checkRedisStatusValue".equals(value))) {
					status = true;
				}
			} else {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
			}
		}
		this.status = status;
	}

	/**
	 * 设置缓存数据
	 * 
	 * @param dataType
	 *            dataType
	 * @param key
	 *            key
	 * @param obj
	 *            对象
	 */
	public void setObj(String dataType, String key, BaseVO obj) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting set value to redis, obj is:" + obj);

				key = dataType + CommonConsts.DOWN_LINE + key;

				jedis.set(key.getBytes(), SerializeUtil.serialize(obj));

				// 设置超时时间 TODO
				jedis.expire(key, 1800);
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! obj is:" + obj, e);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End set value to redis, obj is:" + obj);
	}

	/**
	 * 设置缓存数据 json
	 * 
	 * @param dataType
	 *            dataType
	 * @param key
	 *            key
	 * @param obj
	 *            对象
	 */
	public void setObjClass(String dataType, String key, BaseVO obj) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting set value to redis, obj is:" + obj);

				key = dataType + CommonConsts.DOWN_LINE + key;

				jedis.set(key, JsonUtil.writeObject2JSON(obj));

				if (CommonConsts.NUMBER_ZERO != expiredTime) {
					// 设置超时时间
					jedis.expire(key, expiredTime);
				}
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! obj is:" + obj, e);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End set value to redis, obj is:" + obj);
	}

	/**
	 * 设置缓存数据
	 * 
	 * @param key
	 *            key
	 * @param str
	 *            字符串
	 */
	public void setStr(String key, String str) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting setStr value to redis, key is:" + key + "Str is:" + str);

				jedis.set(key, str);

				if (CommonConsts.NUMBER_ZERO != expiredTime) {
					// 设置超时时间
					jedis.expire(key, expiredTime);
				}
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! key is:" + key + "Str is:" + str);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error. key is:" + key + "Str is:" + str);
		}
	}

	/**
	 * 设置缓存数据
	 * 
	 * @param key
	 *            key
	 * @param str
	 *            字符串
	 */
	public void expiredTime(String key, int time) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				jedis.expire(key, time);

			} catch (Exception e) {

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error. key is:" + key);
		}
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 * @return value
	 */
	public String getStr(String key) {
		String str = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, key is:" + key);

				str = jedis.get(key);
			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! key is:" + key, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, key is:" + key);

		return str;
	}

	/**
	 * 设置哈希缓存数据
	 * 
	 * @param key
	 *            key
	 * @param str
	 *            字符串
	 */
	public void setHashStr(String key, String field, String value) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting setHashStr value to redis, key is:" + key + "field is:" + field);

				jedis.hset(key, field, value);

				if (CommonConsts.NUMBER_ZERO != expiredTime) {
					// 设置超时时间
					jedis.expire(key, expiredTime);
				}
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! key is:" + key + "field is:" + field);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error. key is:" + key + "field is:" + field);
		}
	}

	/**
	 * 设置哈希缓存数据
	 * 
	 * @param key
	 *            key
	 * @param Map
	 *            Map<String, String>
	 */
	public void setHashStr(String key, Map<String, String> hash) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting setHashStr value to redis, key is:" + key);

				jedis.hmset(key, hash);

				if (CommonConsts.NUMBER_ZERO != expiredTime) {
					// 设置超时时间
					jedis.expire(key, expiredTime);
				}
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! key is:" + key);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error. key is:" + key);
		}
	}

	/**
	 * 获取哈希缓存数据
	 * 
	 * @param key
	 *            key
	 * @param str
	 *            字符串
	 */
	public String getHashStr(String key, String field) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting setHashStr value to redis, key is:" + key + "field is:" + field);

				String value = jedis.hget(key, field);

				return value;
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! key is:" + key + "field is:" + field);
				e.printStackTrace();

				setStatus(false);
				return null;
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error. key is:" + key + "field is:" + field);
			return null;
		}
	}

	/**
	 * 获取哈希缓存数据
	 * 
	 * @param key
	 *            key
	 * @param str
	 *            字符串
	 */
	public Map<String, String> getHashStr(String key) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting getHashStr value to redis, key is:" + key);

				Map<String, String> hget = jedis.hgetAll(key);

				return hget;
			} catch (Exception e) {
				LOGGER.error("setObj to redis failed! key is:" + key);
				e.printStackTrace();
				setStatus(false);
				return null;
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error. key is:" + key);
			return null;
		}
	}

	/**
	 * 缓存批量key，用于分页查询
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 */
	public void rpush(String dataType, String key) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting rpush value to redis, dataType is:" + dataType + ",key is:" + key);
				jedis.rpush(dataType, key);

				if (CommonConsts.NUMBER_ZERO != expiredTime) {
					if (CommonConsts.NUMBER_ZERO > jedis.ttl(dataType)) {
						// 设置超时时间
						jedis.expire(dataType, expiredTime);
					}
				}
			} catch (Exception e) {
				LOGGER.error("rpush to redis failed! dataType is:" + dataType + ",key is:" + key, e);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}

		LOGGER.debug("End rpush value to redis, dataType is:" + dataType + ",key is:" + key);
	}

	/**
	 * 获取jedis对象
	 * 
	 * @return jedis对象
	 * @see
	 */
	public Jedis getJedis() {
		Jedis jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);
		return jedis;
	}

	/**
	 * 返回jedis对象
	 * 
	 * @param jedis
	 *            对象
	 * @return 返回jedis
	 * @see
	 */
	public Jedis returnJedis(Jedis jedis) {
		JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
		return jedis;
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 * @return 对象
	 * @param objClass
	 *            对象class
	 * @return
	 * @see
	 */
	public BaseVO getObjClass(String dataType, String key, Class objClass) {
		BaseVO obj = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, dataType is" + dataType + "key is:" + key);

				key = dataType + CommonConsts.DOWN_LINE + key;

				String keyStr = jedis.get(key);

				if (!StringUtil.isEmpty(keyStr)) {
					obj = (BaseVO) JsonUtil.writeJSON2Object(keyStr, objClass);
				}

			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! dataType is" + dataType + "key is:" + key, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, dataType is" + dataType + "key is:" + key);

		return obj;
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 * @return 对象
	 */
	public BaseVO getObj(String dataType, String key) {
		BaseVO obj = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, dataType is" + dataType + "key is:" + key);

				key = dataType + CommonConsts.DOWN_LINE + key;

				byte[] objByte = jedis.get(key.getBytes());

				if (null != objByte) {
					obj = (BaseVO) SerializeUtil.unserialize(objByte);
				}

			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! dataType is" + dataType + "key is:" + key, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, dataType is" + dataType + "key is:" + key);

		return obj;
	}

	/**
	 * 通过字符串类型key获取对象
	 * 
	 * @param key
	 * @return
	 * @see
	 */
	public BaseVO getObjString(String key) {
		BaseVO obj = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, key is:" + key);

				byte[] objByte = jedis.get(key.getBytes());

				if (null != objByte) {
					obj = (BaseVO) SerializeUtil.unserialize(objByte);
				}

			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! key is:" + key, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}

		LOGGER.debug("End get value to redis, key is:" + key);

		return obj;
	}

	/**
	 * 通过byte key获取对象
	 * 
	 * @param key
	 *            key
	 * @return 对象
	 * @see
	 */
	public BaseVO getObjByByteKey(byte[] key) {
		BaseVO obj = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, byte[] key is:" + String.valueOf(key));

				byte[] objByte = jedis.get(key);

				if (null != objByte) {
					obj = (BaseVO) SerializeUtil.unserialize(objByte);
				}

			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! byte[] key is:" + String.valueOf(key), e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, byte[] key is:" + String.valueOf(key));

		return obj;
	}

	/**
	 * zadd
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 */
	public void zadd(String dataType, String value) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting zadd value to redis, dataType is:" + dataType + ",value is:" + value);

				jedis.zadd(dataType, jedis.zcard(dataType), value);

				if (CommonConsts.NUMBER_ZERO != expiredTime) {
					if (CommonConsts.NUMBER_ZERO > jedis.ttl(dataType)) {
						// 设置超时时间
						jedis.expire(dataType, expiredTime);
					}
				}
			} catch (Exception e) {
				LOGGER.error("zadd to redis failed! dataType is:" + dataType + ",value is:" + value, e);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}

		LOGGER.debug("End zadd value to redis, dataType is:" + dataType + ",value is:" + value);
	}

	/**
	 * 分页数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @param start
	 *            开始下班
	 * @param end
	 *            结束下标
	 * @return 对象集合
	 */
	public Set<String> zrange(String dataType) {
		Set<String> bvo = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting zrange value to redis, dataType is" + dataType);

				bvo = jedis.zrange(dataType, 0, -1);
			} catch (Exception e) {
				LOGGER.error("zrange to redis failed! dataType is" + dataType, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End zrange value to redis, dataType is" + dataType);

		return bvo;
	}

	/**
	 * 分页数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @param start
	 *            开始下班
	 * @param end
	 *            结束下标
	 * @return 对象集合
	 */
	public List<String> lrange(String dataType, int start, int end) {
		List<String> bvo = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, dataType is" + dataType);

				bvo = jedis.lrange(dataType, start, end);
			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! dataType is" + dataType, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, dataType is" + dataType);

		return bvo;
	}

	/**
	 * 获取集合大小
	 * 
	 * @param dataType
	 *            类型
	 * @return 集合大小
	 */
	public int llen(String dataType) {
		int len = 0;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get llen to redis, dataType is" + dataType);

				len = jedis.llen(dataType).intValue();
			} catch (Exception e) {
				LOGGER.error("llen to redis failed! dataType is" + dataType, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get llen to redis, dataType is" + dataType);

		return len;
	}

	/**
	 * 获取指定数据类型的所有Key
	 * 
	 * @param dataType
	 *            数据类型
	 * @return key集合
	 */
	public Set<String> getObjAllKey(String dataType) {
		Set<String> keys = null;
		String keyStr = dataType + "_*";
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting get value to redis, dataType is" + dataType + "key is:" + keyStr);

				keys = jedis.keys(keyStr);
			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! dataType is" + dataType + "key is:" + keyStr, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, dataType is" + dataType + "key is:" + keyStr);

		return keys;
	}

	/**
	 * 获取指定数据类型的所有Key
	 *
	 * 数据类型
	 * 
	 * @return key集合
	 */
	public Set<String> getAllKey(String keyPattern) {
		Set<String> keys = null;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);
				LOGGER.debug("Starting get value to redis, key is:" + keyPattern);
				keys = jedis.keys(keyPattern);
			} catch (Exception e) {
				LOGGER.error("getObj to redis failed! key is:" + keyPattern, e);
				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End get value to redis, key is:" + keyPattern);
		return keys;
	}

	/**
	 * 通过dataType+key删除一条数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @param key
	 *            key
	 * @see
	 */
	public void delObj(String dataType, String key) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting del value to redis, dataType is" + dataType + "key is:" + key);

				key = dataType + CommonConsts.DOWN_LINE + key;

				jedis.del(key.getBytes());
			} catch (Exception e) {
				LOGGER.error("delObj to redis failed! dataType is" + dataType + "key is:" + key, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End del value to redis, dataType is" + dataType + "key is:" + key);
	}

	/**
	 * 删除一个集合key 中的某个value
	 * 
	 * @param key
	 * @param value
	 * @see
	 */
	public void lrem(String key, String value) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting lrem redis, key is" + key + "value is:" + value);

				jedis.lrem(key, 0, value);
			} catch (Exception e) {
				LOGGER.error("delObj to redis failed! key is" + key + "value is:" + value, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error, key is" + key + "key is:" + value);
		}
	}

	/**
	 * 删除一个类型的所有数据
	 * 
	 * @param dataType
	 *            数据类型
	 * @see
	 */
	public void delStr(String dataType) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting del value to redis, dataType is" + dataType);

				jedis.del(dataType);
			} catch (Exception e) {
				LOGGER.error("delObj to redis failed! dataType is" + dataType);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End del value to redis, dataType is" + dataType);
	}

	/**
	 * 删除一个数据类型中所有数据并返回行数
	 * 
	 * @param dataType
	 *            数据类型
	 * @return
	 * @see
	 */
	public long delByDataType(String dataType) {
		long count = 0L;
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting delByDataType to redis, dataType is" + dataType);

				Set<String> keys = jedis.keys(dataType + "*");

				String[] str = new String[0];

				str = (String[]) keys.toArray(str);

				if (str.length > 0) {
					count = jedis.del(str).longValue();
				}
			} catch (Exception e) {
				LOGGER.error("delObj to redis failed! dataType is" + dataType, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}

		LOGGER.debug("End del value to redis, dataType is" + dataType);
		return count;
	}

	/**
	 * 重置redis
	 * 
	 * @see
	 */
	public void resetRedis() {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting resetRedis to redis");

				jedis.flushAll();
			} catch (Exception e) {
				LOGGER.error("resetRedis to redis failed!", e);
				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {

					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
	}
//
	public void delObjClass(String dataType, String key) {
		if (this.status) {
			Jedis jedis = null;
			try {
				jedis = JedisUtil.getInstance().getJedis(this.ip, this.port, this.password);

				LOGGER.debug("Starting del value to redis, dataType is" + dataType + "key is:" + key);

				key = dataType + "_" + key;

				jedis.del(key);
			} catch (Exception e) {
				LOGGER.error("delObj to redis failed! dataType is" + dataType + "key is:" + key, e);

				e.printStackTrace();

				setStatus(false);
			} finally {
				if (null != jedis) {
					JedisUtil.getInstance().returnJedis(jedis, this.ip, this.port, this.password);
				}
			}
		} else {
			LOGGER.error("The redis status is error.");
		}
		LOGGER.debug("End del value to redis, dataType is" + dataType + "key is:" + key);
	}

	public boolean isStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getIp() {
		return this.ip;
	}
}