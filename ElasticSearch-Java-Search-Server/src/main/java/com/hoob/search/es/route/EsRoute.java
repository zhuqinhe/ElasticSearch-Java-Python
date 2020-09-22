package com.hoob.search.es.route;

import org.elasticsearch.client.RestHighLevelClient;

import com.hoob.search.es.client.EsRestHighLevelClient;

public class EsRoute {
//	
	public static RestHighLevelClient getEsClient() throws Exception {

		return EsRestHighLevelClient.getSingleClient();

	}

}
