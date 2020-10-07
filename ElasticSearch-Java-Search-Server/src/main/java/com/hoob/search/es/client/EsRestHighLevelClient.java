package com.hoob.search.es.client;

import java.io.File;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.hoob.search.common.Constants;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.utils.FileUtils;
import com.hoob.search.utils.OsCheck;
import com.hoob.search.utils.StringUtils;

/**
 * @author zhuqinhe
 */
public class EsRestHighLevelClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsRestHighLevelClient.class);

	private Map<String, Long> fileModifyTimeMap = new HashMap<>();

	private boolean status = false;

	private static String fileName;

	private static Properties proConfig = new Properties();

	private volatile static RestHighLevelClient singleClient;

	static {

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

		String httpAddress = getString("elasticsearch_address", "localhost");

		Integer httpPort = Integer.parseInt(getString("elasticsearch_port", "9200"));

		String user = getString("elasticsearch_user", "elastic");

		String password = getString("elasticsearch_password", "FonsView!23+");

		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		//credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

		if (singleClient == null) {
			synchronized (RestHighLevelClient.class) {
				if (singleClient == null) {
					singleClient = new RestHighLevelClient(
							RestClient.builder(new HttpHost(httpAddress, httpPort, "http"))
									.setHttpClientConfigCallback(new HttpClientConfigCallback() {
										@Override
										public HttpAsyncClientBuilder customizeHttpClient(
												HttpAsyncClientBuilder httpClientBuilder) {
											httpClientBuilder.disableAuthCaching();
											return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
										}
									}));
				}
			}
		}
	}

	public static synchronized RestHighLevelClient getSingleClient() throws UnknownHostException {
		return singleClient;
	}

	public static String getString(String key, String defaultValue) {
		String value = null;
		if (null != proConfig) {
			value = proConfig.getProperty(key);
		}
		if (StringUtils.paramIsNull(value)) {
			return defaultValue;
		}
		return value.trim();
	}

	public static Boolean getBoolean(String key, Boolean defaultValue) {
		String value = null;
		if (null != proConfig) {
			value = proConfig.getProperty(key);
		}
		if (StringUtils.paramIsNull(value)) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}

	//
	public void checkEsClient() {
		boolean status = false;
		try {
			ClusterHealthRequest request = new ClusterHealthRequest();

			RestHighLevelClient singleCheckClient = EsRestHighLevelClient.getSingleClient();

			ClusterHealthResponse healths = singleCheckClient.cluster().health(request, RequestOptions.DEFAULT);

			if (healths.getStatus().equals(ClusterHealthStatus.RED)) {
				status = false;
				LOGGER.error("Index is in " + healths.getStatus() + " state");
				throw new RuntimeException("Index is in " + healths.getStatus() + " state");

			} else {
				status = true;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			this.status = status;
		}

	}

	private class PropertiesConfigThread extends Thread {
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
							LOGGER.info("[{}] update success", fileName);
						}
					}
				} catch (Exception e) {
					LOGGER.error("load search.properties error:", e);
				}
				try {
					String httpAddress = getString("elasticsearch_address", "localhost");

					Integer httpPort = Integer.parseInt(getString("elasticsearch_port", "9200"));

					String user = getString("elasticsearch_user", "elastic");

					String password = getString("elasticsearch_password", "FonsView!23+");

					final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
					//credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

					synchronized (RestHighLevelClient.class) {
						if (singleClient == null) {
							singleClient = new RestHighLevelClient(
									RestClient.builder(new HttpHost(httpAddress, httpPort, "http"))
											.setHttpClientConfigCallback(new HttpClientConfigCallback() {
												public HttpAsyncClientBuilder customizeHttpClient(
														HttpAsyncClientBuilder httpClientBuilder) {
													httpClientBuilder.disableAuthCaching();
													return httpClientBuilder
															.setDefaultCredentialsProvider(credentialsProvider);
												}
											}));
						}
					}
				} catch (Exception e) {
					LOGGER.error("update es client exception:", e);
				}

				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
					LOGGER.error("propertiesConfigThread sleep throws exception:", e);
				}
			}
		}

	}

	public boolean isStatus() {
		return this.status;
	}

	//
	public void monitor() {
		checkEsClient();
		EsMonitorThread checkEsClient = new EsMonitorThread(this);
		checkEsClient.start();
	}

	//
	public void checkUpdate() {
		PropertiesConfigThread configThread = new PropertiesConfigThread();
		configThread.start();
	}

}
