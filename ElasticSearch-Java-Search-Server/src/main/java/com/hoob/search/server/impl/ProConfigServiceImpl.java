package com.hoob.search.server.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.hoob.search.common.Constants;
import com.hoob.search.server.ProConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.hoob.search.utils.FileUtils;
import com.hoob.search.utils.OsCheck;
import com.hoob.search.utils.StringUtils;

@Component("proConfigService")
public class ProConfigServiceImpl implements InitializingBean, ProConfigService {

	private final Logger logger = LoggerFactory.getLogger(ProConfigServiceImpl.class);
	private Properties proConfig = new Properties();
	private Map<String, Long> fileModifyTimeMap = new HashMap<>();
	private static String fileName;

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {

		fileName = Constants.LIUNX_FILE_NAME;


		File file = new File(fileName);
		try {
			Properties p = FileUtils.getResource(file);
			if (null != p) {
				proConfig = p;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PropertiesConfigThread configThread = new PropertiesConfigThread();
		configThread.start();
	}

	private class PropertiesConfigThread extends Thread {
//		
		public PropertiesConfigThread() {
			Thread.currentThread().setName("propertiesConfigThread");
		}

		@Override
		public void run() {
			while (true) {
				try {
					File file = new File(fileName);
					Long lastModifyTime = file.lastModified();
					Long oldLastModifyTime = fileModifyTimeMap.get(fileName);
					if (oldLastModifyTime == null || lastModifyTime > oldLastModifyTime) {
						Properties p = FileUtils.getResource(file);
						if (null != p) {
							proConfig = p;
							fileModifyTimeMap.put(fileName, lastModifyTime);
							logger.info("[{}] update success", fileName);
						}
					}
				} catch (Exception e) {
					logger.error("load search.properties error:", e);
				}
				//
				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
					logger.error("propertiesConfigThread sleep throws exception:", e);
				}
			}
		}

	}

	@Override
	public String getString(String key, String defaultValue) {
		String value = null;
		if (null != proConfig) {
			value = proConfig.getProperty(key);
		}
		if (StringUtils.paramIsNull(value)) {
			return defaultValue;
		}
		return value.trim();
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = null;
		if (null != proConfig) {
			value = proConfig.getProperty(key);
		}
		if (StringUtils.paramIsNull(value)) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}

}
