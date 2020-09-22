package com.hoob.search.es.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsMonitorThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsMonitorThread.class);
	//
	private EsRestHighLevelClient esClient;

	//
	public EsMonitorThread(EsRestHighLevelClient esClient) {
		this.esClient = esClient;

	}

	@Override
	public void run() {
		do {
			try {
				// 获取当前状态
				boolean currentStatus = esClient.isStatus();
				// 然后再去检查一次状态
				esClient.checkEsClient();
				// 再获取一次检查后的状态
				boolean afterStatus = esClient.isStatus();

				// 如果获取的状态异常
				if (!currentStatus && afterStatus) {
					LOGGER.warn("es status is:" + currentStatus);
				}
				Thread.sleep(EsConfig.getCheckTime());

			} catch (Exception e) {
				LOGGER.warn(e.getMessage());
			}

			try {
				Thread.sleep(EsConfig.getCheckTime());
			} catch (Exception e) {
				LOGGER.warn(e.getMessage());
			}

		} while (true);
	}

}
