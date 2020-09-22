package com.hoob.search.utils;

import com.hoob.search.common.JobLogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Administrator
 *
 */
public class UserInfoLogUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger("UserInfoLogUtil");
	//
	public static void log(String msg) {
		LOGGER.info(msg);
	}

	/**
	 * 用户信息日志记录
	 * 
	 * @param msg
	 */
	public static void record(String msg) {
		//
		Logger log = JobLogFactory.createLogger(0);
		//
		log.info(msg);
		JobLogFactory.stop(0);
	}

}
