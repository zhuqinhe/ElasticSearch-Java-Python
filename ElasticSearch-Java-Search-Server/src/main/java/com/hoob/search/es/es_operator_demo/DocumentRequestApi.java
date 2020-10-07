package com.hoob.search.es.es_operator_demo;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuqinhe
 * 创建文档相关的api
 */
public class DocumentRequestApi {
    /**构建IndexRequest多种方式 1**/
    public IndexRequest createIndex1() {
        /***/
        IndexRequest indexRequest = new IndexRequest("hoobindex");
        indexRequest.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy1\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        indexRequest.source(jsonString, XContentType.JSON);
        indexRequest.timeout("2s");
        //indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        indexRequest.opType(DocWriteRequest.OpType.CREATE);
        return indexRequest;
    }
    /**构建IndexRequest多种方式 3**/
    public IndexRequest createIndex3() throws IOException {
        /***/
        IndexRequest indexRequest = new IndexRequest("hoobindex");
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy3");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        indexRequest = new IndexRequest("hoobindex").id("3").source(builder);
        indexRequest.timeout("2s");
        //indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        indexRequest.opType(DocWriteRequest.OpType.CREATE);
        return indexRequest;
    }
    /**构建IndexRequest多种方式 2**/
    public IndexRequest createIndex2() throws IOException {
        /***/
        IndexRequest indexRequest = new IndexRequest("hoobindex");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy2");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        indexRequest = new IndexRequest("hoobindex").id("2").source(jsonMap);
        indexRequest.timeout("2s");
        //indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        indexRequest.opType(DocWriteRequest.OpType.CREATE);
        return indexRequest;

    }
    /**构建IndexRequest多种方式 4**/
    public IndexRequest createIndex4() throws IOException {
        /***/
        IndexRequest indexRequest = new IndexRequest("hoobindex");
        indexRequest = new IndexRequest("hoobindex")
                .id("4")
                .source("user", "kimchy4",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch");
        /**设置超时时间**/
        indexRequest.timeout("2s");
        indexRequest.opType(DocWriteRequest.OpType.CREATE);
        //indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        return indexRequest;
    }

    /**创建索引**/
    public static  void  createIndex() throws IOException{
        /**创建索引1**/
        DocumentRequestApi indexRequestApi = new DocumentRequestApi();
        try {
            IndexResponse indexResponse = TestEsHighLevelClient.getClient().index(indexRequestApi.createIndex1(), RequestOptions.DEFAULT);
            String index = indexResponse.getIndex();
            String id = indexResponse.getId();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {

            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {

            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure :
                        shardInfo.getFailures()) {
                    String reason = failure.reason();
                }
            }
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {

            }
            e.printStackTrace();
        }
        //测试创建索引 2
        /**创建索引2**/
        try {
            IndexResponse indexResponse = TestEsHighLevelClient.getClient().index(indexRequestApi.createIndex2(),
                    //自定义请求头
                    TestEsHighLevelClient.getRequestOptions());
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {

            }
            e.printStackTrace();
        }

        /**创建索引3**/
        //测试创建索引 3
        ActionListener listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        try {
             //异步提交请求
             TestEsHighLevelClient.getClient().indexAsync(indexRequestApi.createIndex3(), RequestOptions.DEFAULT, listener);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {

            }
            e.printStackTrace();
        }

        /**创建索引4**/
        try {
            IndexResponse indexResponse = TestEsHighLevelClient.getClient().index(
                    indexRequestApi.createIndex4(),
                    RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
            }
            e.printStackTrace();
        }
    }
   /**通过Id获取一个文档**/
   public static void getDocumentById() throws Exception {
       GetRequest getRequest = new GetRequest("hoobindex", "1");
       //禁用索引源
       //getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);

       //只获取索引源里面的部分属性
       String[] includes = new String[]{"message", "*Date"};
       String[] excludes = Strings.EMPTY_ARRAY;
       FetchSourceContext fetchSourceContext =
               new FetchSourceContext(true, includes, excludes);
       getRequest.fetchSourceContext(fetchSourceContext);

       GetResponse getResponse = TestEsHighLevelClient.getClient().get(getRequest, RequestOptions.DEFAULT);
       String index = getResponse.getIndex();
       String id = getResponse.getId();
       if (getResponse.isExists()) {
           long version = getResponse.getVersion();
           String sourceAsString = getResponse.getSourceAsString();
           Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
           System.out.println(sourceAsString);
       } else {
           System.out.println("not document");
       }
   }

    /**通过Id获取一个文档**/
    public static void existsDocumentById() throws Exception {
        GetRequest getRequest = new GetRequest("hoobindex", "1");
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists= TestEsHighLevelClient.getClient().exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("exists:"+exists);

    }
    /**删除一个**/
    public static void deleteDocument() throws Exception {
        DeleteRequest getRequest = new DeleteRequest ("hoobindex", "1");
        DeleteResponse deleteResponse = TestEsHighLevelClient.getClient().delete(getRequest, RequestOptions.DEFAULT);
        System.out.println("delete:"+deleteResponse.getIndex());

    }
    /**删除一个**/
    public static void updateRequest() throws Exception {
        UpdateRequest request = new UpdateRequest("hoobindex", "1").doc("updated", new Date(),
                "reason", "daily update");
        UpdateResponse response = TestEsHighLevelClient.getClient().update(request, RequestOptions.DEFAULT);
        GetResult result = response.getGetResult();
        if (result.isExists()) {
            String sourceAsString = result.sourceAsString();
            Map<String, Object> sourceAsMap = result.sourceAsMap();
            System.out.println("update :" + sourceAsString);
            byte[] sourceAsBytes = result.source();
        } else {

        }

        String jsonString = "{" +
                "\"updated\":\"2017-01-01\"," +
                "\"reason\":\"daily update\"" +
                "}";
        request = new UpdateRequest("hoobindex", "1");
        request.doc(jsonString, XContentType.JSON);//只更新
        //request.upsert(jsonString, XContentType.JSON);//存在就更新不存在新增
        response = TestEsHighLevelClient.getClient().update(request, RequestOptions.DEFAULT);
        result = response.getGetResult();
        if (result.isExists()) {
            String sourceAsString = result.sourceAsString();
            Map<String, Object> sourceAsMap = result.sourceAsMap();
            System.out.println("update :" + sourceAsString);
            byte[] sourceAsBytes = result.source();
        } else {

        }


        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("reason", "daily update");
        request = new UpdateRequest("hoobindex", "1").doc(jsonMap);
        response = TestEsHighLevelClient.getClient().update(request, RequestOptions.DEFAULT);
        result = response.getGetResult();
        if (result.isExists()) {
            String sourceAsString = result.sourceAsString();
            Map<String, Object> sourceAsMap = result.sourceAsMap();
            System.out.println("update :" + sourceAsString);
            byte[] sourceAsBytes = result.source();
        } else {

        }

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.timeField("updated", new Date());
            builder.field("reason", "daily update");
        }
        builder.endObject();
        request = new UpdateRequest("hoobindex", "1").doc(builder);
        response = TestEsHighLevelClient.getClient().update(request, RequestOptions.DEFAULT);
        result = response.getGetResult();
        if (result.isExists()) {
            String sourceAsString = result.sourceAsString();
            Map<String, Object> sourceAsMap = result.sourceAsMap();
            System.out.println("update :" + sourceAsString);
            byte[] sourceAsBytes = result.source();
        } else {

        }
        System.out.println("update :" + response.getIndex());

    }

    public static void bulkRequest() throws IOException {
        BulkRequest request = new BulkRequest();

        request.add(new IndexRequest("hoobindex").id("1")
                .source(XContentType.JSON,"field", "foo"));
        request.add(new IndexRequest("hoobindex").id("2")
                .source(XContentType.JSON,"field", "bar"));
        request.add(new IndexRequest("hoobindex").id("3")
                .source(XContentType.JSON,"field", "baz"));

        request.add(new DeleteRequest("hoobindex", "3"));
        request.add(new UpdateRequest("hoobindex", "2")
                .doc(XContentType.JSON,"other", "test"));
        request.add(new IndexRequest("hoobindex").id("4")
                .source(XContentType.JSON,"field", "baz"));

        BulkResponse bulkResponse = TestEsHighLevelClient.getClient().bulk(request, RequestOptions.DEFAULT);
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            switch (bulkItemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
            }
        }
    }
}
