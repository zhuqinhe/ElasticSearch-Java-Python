package com.hoob.search.es.es_operator_demo;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.*;

/**
 * @author zhuqinhe
 */
public class TestEsHighLevelClient {
    /**客户端IP**/
    private static final String httpAddress = "localhost";
    /**端口**/
    private static final Integer httpPort = 9200;
    /**获取客户端连接**/
    private static RestHighLevelClient singleClient;
    /**请求头设置**/
    private static  RequestOptions COMMON_OPTIONS;
    /**设置认证信息**/
    private static final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    //credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

    /**实例化客户端连接**/
    public static RestHighLevelClient getClient() {
        singleClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(httpAddress, httpPort, "http")
                        //,new HttpHost(httpAddress, httpPort, "http")
                ).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                            @Override
                            public HttpAsyncClientBuilder customizeHttpClient(
                                    HttpAsyncClientBuilder httpClientBuilder) {
                                httpClientBuilder.disableAuthCaching();
                                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                            }
                        }));

        return singleClient;
    }
    /**创建RequestOptions**/
    public static RequestOptions getRequestOptions(){
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("TOKEN", "TOKEN" );
        //builder.setHttpAsyncResponseConsumerFactory(
         //       new HttpAsyncResponseConsumerFactory
         //               .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
        return COMMON_OPTIONS;
    }
    /**关闭连接**/
    public static void  closeClient() throws Exception {
        if (singleClient != null) {
            singleClient.close();
        }
    }



    public static  void main(String[] args) throws Exception {
        DocumentRequestApi api=new DocumentRequestApi();

        //创建文档
        //DocumentRequestApi.createIndex();
        //获取文档
        //DocumentRequestApi.getDocumentById();
        //判断文档是否存在
        //DocumentRequestApi.existsDocumentById();
        //删除文档
        //DocumentRequestApi.deleteDocument();
        //更新文档
        //DocumentRequestApi.updateRequest();
        //bulk 更新
        //DocumentRequestApi.bulkRequest();
        //创建索引设置settings和mapping
        //IndexSettingsMapping.createIndexSettingsMapping();
        //IndexSettingsMapping.createIndexSettings();
        IndexSettingsMapping.createIndexRequest();
        closeClient();
        System.exit(0);
    }
}
