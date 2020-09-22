/**
 * 
 */
package com.hoob.search.common;

import java.io.File;
import java.net.URI;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.web.Log4jServletContextListener;


@WebListener
public class Log4jListener extends Log4jServletContextListener {

	static Logger LOGGER = LogManager.getLogger(Log4jListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		File log4jFile = new File(Constants.LOG4J2_MASTER);
		if (log4jFile.exists()) {
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			context.setConfigLocation(URI.create(Constants.LOG4J2_MASTER));
			context.reconfigure();
			LOGGER.info("Log4j2------>" + Constants.LOG4J2_MASTER);
		}
	}

	// @Override
	// public void contextInitialized(ServletContextEvent arg0) {
	// try {
	// ConfigurationSource source;
	// File log4jFile = new File(LOG4J2_MASTER);
	// if (log4jFile.exists()) {
	// source = new ConfigurationSource(new FileInputStream(log4jFile),
	// log4jFile);
	// Configurator.initialize(source);
	// Configurator.initialize(null, source);
	// System.out.println("Log4j2------>" + LOG4J2_MASTER);
	// }else{
	// InputStream in = this.getClass().getResourceAsStream(LOG4J2_SLAVE);
	// source = new ConfigurationSource(in);
	// Configurator.initialize(null, source);
	// System.out.println("Log4j2------>classpath:" + LOG4J2_SLAVE);
	//
	// logger.warn("Log4j2------>classpath:" + LOG4J2_SLAVE);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

}
