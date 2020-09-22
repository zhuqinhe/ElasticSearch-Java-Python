package com.hoob.search.common;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardThreadExecutor;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hoob.search.sys.common.ConfigKey;
import com.hoob.search.sys.utils.ConfigUtil;
import com.hoob.search.sys.utils.StringUtils;

/**
 * @Description tomcat参数定制优化
 * @author hoob
 * @date 2018年11月17日下午6:30:09
 */
@Configuration
public class TomcatCustomizer {

	@Bean
	public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		/*
		 * factory.setPort(9988);
		 * factory.setUriEncoding(Charset.forName("UTF-8"));
		 * factory.addConnectorCustomizers(connector ->{ Http11NioProtocol
		 * protocol =(Http11NioProtocol) connector.getProtocolHandler();//NIO的方式
		 * protocol.setDisableUploadTimeout(false); //设置最大连接数
		 * protocol.setMaxConnections(6000); //设置最大线程数
		 * protocol.setMaxThreads(2000); //设置连接超时
		 * protocol.setConnectionTimeout(30000); });
		 */

		String dirUrl = ConfigUtil.getProperties(ConfigKey.TOMCATTEMURL.name());
		if (StringUtils.isEmpty(dirUrl)) {
			dirUrl = "/opt/hoob/3RD/tomcat7.0.63";
		}
		factory.setUriEncoding(Charset.forName("UTF-8"));
		// 设置Tomcat的根目录
		factory.setBaseDirectory(new File(dirUrl));
		// 设置连接数
		factory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
		// 设置访问日志
		factory.addEngineValves(getLogAccessLogValue());
		// 添加错误页面 不配置
		// factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
		// "/404.html"));
		// 初始化一些设置
		factory.addInitializers((servletContext) -> {
			System.out.println("=========servletContext   startup============");
			// servletContext.addListener(className); //添加 监听器
			// servletContext.addFilter(filterName, filter); //添加 过滤器
			// servletContext.setAttribute("startup", "true"); //可以设置 全局变量
			System.out.println(" = = = =服务器信息 = = = " + servletContext.getServerInfo());
		});
		return factory;
	}

	private AccessLogValve getLogAccessLogValue() {

		AccessLogValve log = new AccessLogValve();
		String accesslogpattern = ConfigUtil.getProperties(ConfigKey.ACCESSLOGPATTERN.name());
		if (StringUtils.isEmpty(accesslogpattern)) {
			accesslogpattern = "%{yyyy-MM-dd HH:mm:ss.SSS}t|%a|%h|%H|%r|%{Referer}i|%s|%b|%D";
		}
		String accesslogEnabled = ConfigUtil.getProperties(ConfigKey.ACCESSLOGENABLED.name());
		if (StringUtils.isEmpty(accesslogEnabled)) {
			accesslogEnabled = "false";
		}
		// 日志目录
		log.setDirectory("/logs/");
		// 开始日志 (加载配置)
		log.setEnabled(Boolean.parseBoolean(accesslogEnabled));
		// 日志级别
		log.setPattern(accesslogpattern);
		// 日志前缀
		log.setPrefix("springboot-favorite-access-log");
		// 日志后缀
		log.setSuffix(".txt");
		System.out.println("tomcat访问日志开启");
		return log;

	}
}

class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
	/**
	 * @Title customize @Description @param @return
	 * TomcatConnectorCustomizer @throws
	 */
	@Override
	public void customize(Connector connector) {

		AdvancedThreadExecutor myExecutor = new AdvancedThreadExecutor();
		connector.getService().addExecutor(myExecutor);
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		protocol.setExecutor(myExecutor);
		// 设置最大连接数
		String maxConnections = ConfigUtil.getProperties(ConfigKey.TOMCATCONNECTIONS.name());
		if (StringUtils.isEmpty(maxConnections)) {
			maxConnections = "6000";
		}
		protocol.setMaxConnections(Integer.parseInt(maxConnections));

		String connectionTimeout = ConfigUtil.getProperties(ConfigKey.TOMCATMCONNECTIONTIMEOUT.name());
		if (StringUtils.isEmpty(connectionTimeout)) {
			connectionTimeout = "3000";
		}
		protocol.setConnectionTimeout(Integer.parseInt(connectionTimeout));

		// 设置最大线程数
		String maxThreads = ConfigUtil.getProperties(ConfigKey.TOMCATMAXTHREADS.name());
		if (StringUtils.isEmpty(maxThreads)) {
			maxThreads = "6000";
		}
		protocol.setMaxThreads(Integer.parseInt(maxThreads));

		String minSpareThreads = ConfigUtil.getProperties(ConfigKey.TOMCATMINSPARETHREADS.name());
		if (StringUtils.isEmpty(minSpareThreads)) {
			minSpareThreads = "500";
		}
		protocol.setMinSpareThreads(Integer.parseInt(minSpareThreads));

		String acceptCount = ConfigUtil.getProperties(ConfigKey.TOMCATACCEPTCOUNT.name());
		if (StringUtils.isEmpty(acceptCount)) {
			acceptCount = "20480";
		}
		protocol.setAcceptCount(Integer.parseInt(acceptCount));

		String acceptorThreadCount = ConfigUtil.getProperties(ConfigKey.TOMCATACCEPTORTHREADCOUNT.name());
		if (StringUtils.isEmpty(acceptorThreadCount)) {
			acceptorThreadCount = "2";
		}
		protocol.setAcceptorThreadCount(Integer.parseInt(acceptorThreadCount));

		String maxKeepAliveRequests = ConfigUtil.getProperties(ConfigKey.MAXKEEPALIVEREQUESTS.name());
		if (StringUtils.isEmpty(maxKeepAliveRequests)) {
			maxKeepAliveRequests = "20480";
		}
		protocol.setMaxKeepAliveRequests(Integer.parseInt(maxKeepAliveRequests));

		String keepAliveTimeout = ConfigUtil.getProperties(ConfigKey.KEEPALIVETIMEOUT.name());
		if (StringUtils.isEmpty(keepAliveTimeout)) {
			keepAliveTimeout = "5000";
		}
		protocol.setKeepAliveTimeout(5000);
	}

	class AdvancedThreadExecutor extends StandardThreadExecutor {
		@Override
		protected void startInternal() throws LifecycleException {
			super.namePrefix = "custom-tomcat-";

			String minSpareThreads = ConfigUtil.getProperties(ConfigKey.TOMCATMINSPARETHREADS.name());
			if (StringUtils.isEmpty(minSpareThreads)) {
				minSpareThreads = "500";
			}
			String maxIdleTime = ConfigUtil.getProperties(ConfigKey.TOMCATMINSPARETHREADS.name());
			if (StringUtils.isEmpty(maxIdleTime)) {
				maxIdleTime = "120000";
			}
			super.maxThreads = Integer.parseInt(minSpareThreads);
			super.maxIdleTime = Integer.parseInt(maxIdleTime);
			super.startInternal();
			executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					throw new RejectedExecutionException(
							"Task " + r.toString() + " rejected from " + executor.toString());
				}
			});
		}
	}

}
