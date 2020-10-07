package com.hoob.search.common;

import org.elasticsearch.client.transport.TransportClient;

/**
 * @author zhuqinhe
 */
public class DbQueue {
	private volatile static DbHotWordReportReqQueue dbHotWordReportReqQueue;
	static{
		if(dbHotWordReportReqQueue == null){
			synchronized (TransportClient.class) {
				if (dbHotWordReportReqQueue == null) {
					try {
						dbHotWordReportReqQueue = new DbHotWordReportReqQueue();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static synchronized DbHotWordReportReqQueue getDBHotWordReportReqQueue() throws Exception{
		return dbHotWordReportReqQueue;
	}
}
