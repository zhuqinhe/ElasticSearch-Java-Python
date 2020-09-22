package com.hoob.search.sys.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.hoob.search.common.Constants;
import org.apache.log4j.Logger;

import com.hoob.search.utils.OsCheck;

public class ConfigUtil {

	private static final Logger LOG = Logger.getLogger(ConfigUtil.class);
	public static final Properties PROPERTIES = new Properties();
	private static File file = null;

	/**
	 * 获取配置文件属性
	 */
	public static void init() {
		FileReader reader = null;
		try {


			file = new File(Constants.LIUNX_ETC_PATH + "config.properties");

			reader = new FileReader(file);
			PROPERTIES.load(reader);
		} catch (Exception e) {
			LOG.error("init config.properties exception.", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static String getProperties(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		} else {
			return PROPERTIES.getProperty(key.toLowerCase());
		}
	}
}
