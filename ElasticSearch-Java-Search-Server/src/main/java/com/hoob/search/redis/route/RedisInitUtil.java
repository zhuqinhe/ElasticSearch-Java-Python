package com.hoob.search.redis.route;

import javax.annotation.PostConstruct;

import com.hoob.search.redis.client.RedisClient;
import com.hoob.search.redis.client.RedisMonitorThread;
import com.hoob.search.redis.utils.StringUtil;
import com.hoob.search.server.RedisConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RedisInitUtil {
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(RedisInitUtil.class);

	/**
	 * 参数配置类
	 */
	@Autowired
	public RedisConfigService proConfigService;

	//
	public RedisInitUtil() {
	}

	private class RedisUpdateThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					initRedisClient();
				} catch (Exception e) {
					logger.warn(" update redis client throws exception:", e);
				}
				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
					logger.warn("init redis sleep throws exception:", e);
				}
			}
		}
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 * 
	 * @see
	 */
	@PostConstruct
	public void init() throws Exception {
		initRedisClient();
		RedisUpdateThread redisUpdateThread = new RedisUpdateThread();
		redisUpdateThread.start();
		RedisClient redisClient = RedisRoute.getRedisClient("");
		RedisMonitorThread monitorThread = new RedisMonitorThread(redisClient);
		monitorThread.start();
		logger.debug("redis config switch:" + proConfigService.getBoolean("REDIS_SWITCH", false));
		logger.debug("redis expired time :" + proConfigService.getString("REDIS_EXPIRED_TIME", "0"));
		logger.debug("redis connent config:" + proConfigService.getString("REDIS_LIST", "127.0.0.1:6370"));
	}
//
	public void initRedisClient() {
		try {

			boolean isOpen = proConfigService.getBoolean("REDIS_SWITCH", false);

			if (isOpen) {
				// 获取redis集群参数
				String redisList = proConfigService.getString("REDIS_LIST", "127.0.0.1:6370");

				if (!StringUtil.isEmpty(redisList)) {
					// 获取redis集群参数
					String redisExpiredTime = proConfigService.getString("REDIS_EXPIRED_TIME", "0");
					String[] redisExpiredTimeArr = null;
					if (!StringUtil.isEmpty(redisExpiredTime)) {
						redisExpiredTimeArr = redisExpiredTime.split(",");
					}

					// 集群参数格式ip:port:password#ip:port:password
					String[] redisArr = redisList.split("#");

					for (String redisStr : redisArr) {
						String[] redis = redisStr.split(":");

						// ip:port:password#ip:port:password 获取是否有密码位
						if ((redis.length == 2) || (redis.length == 3)) {
							String ip = redis[0].trim();
							int port = Integer.valueOf(redis[1].trim()).intValue();
							String password = "";

							// 如果有密码就带上密码
							if (redis.length == 3) {
								password = redis[2].trim();
							}

							// 获取redis客户端
							RedisClient client = new RedisClient(ip, port, password, redisExpiredTimeArr);

							RedisRoute.getDcsslist().clear();
							// 存入到集合中
							RedisRoute.getDcsslist().add(client);
						}
					}
				}
			} else {

				logger.debug("Redis config is close !, Please check reason!");
			}

		} catch (Exception e) {
			logger.error("Redis server init failed, Please check reason!", e);
			e.printStackTrace();
		}
	}

}
