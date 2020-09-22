package com.hoob.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hoob.search.sys.utils.ConfigUtil;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan
public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);
	/**
	 * 服务启动类
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//读取系统配置
			ConfigUtil.init();
			log.info("--- start --");
			SpringApplication app = new SpringApplication(Main.class);
			app.setDefaultProperties(ConfigUtil.PROPERTIES);
			app.run(args);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
