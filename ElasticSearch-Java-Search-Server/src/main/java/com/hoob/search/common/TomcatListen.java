package com.hoob.search.common;

import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.es.client.EsRestHighLevelClient;

@WebListener
public class TomcatListen implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatListen.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		EsRestHighLevelClient client = new EsRestHighLevelClient();
		client.monitor();
		LOGGER.info("tomcat服务器初始化。。。" + servletContextEvent.getServletContext());
		SearchRequest searchRequest = new SearchRequest("*");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		try {
			RestHighLevelClient singleCheckClient = EsRestHighLevelClient.getSingleClient();
			singleCheckClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		try {
			EsRestHighLevelClient.getSingleClient().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.info("tomcat服务器关闭了。。。" + servletContextEvent.getServletContext());
	}

}
