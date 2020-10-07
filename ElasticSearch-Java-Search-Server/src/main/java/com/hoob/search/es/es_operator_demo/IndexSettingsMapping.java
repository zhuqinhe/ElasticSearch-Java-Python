package com.hoob.search.es.es_operator_demo;

import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuqinhe
 * request.source("{\n" +
 *         "    \"settings\" : {\n" +
 *         "        \"number_of_shards\" : 1,\n" +
 *         "        \"number_of_replicas\" : 0\n" +
 *         "    },\n" +
 *         "    \"mappings\" : {\n" +
 *         "        \"properties\" : {\n" +
 *         "            \"message\" : { \"type\" : \"text\" }\n" +
 *         "        }\n" +
 *         "    },\n" +
 *         "    \"aliases\" : {\n" +
 *         "        \"twitter_alias\" : {}\n" +
 *         "    }\n" +
 *         "}", XContentType.JSON);
 */
public class IndexSettingsMapping {
    /**定制索引settings  和 mapping**/
    public static void createIndexSettingsMapping() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("twitter");
        //设置settings
     /*   request.settings(Settings.builder()
                //index.number_of_shards 分片
                .put("index.number_of_shards", 3)
                //index.number_of_replicas 副本集
                .put("index.number_of_replicas", 2));*/
        //设置mapping
       /* Map<String, Object> message = new HashMap<>();
        message.put("type", "text");
        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);*/

        request.source(
                        "{\n" +
                        " \"settings\" : \n" +
                        "{\n" +
                        "        \"number_of_shards\" : 1,\n" +
                        "        \"number_of_replicas\" : 0,\n" +
                        "        \"max_ngram_diff\": 50,\n"+
                        "        \"max_result_window\": 10000000,\n"+
                        "        \"analysis\": {\n" +
                        "                    \"filter\": {\n"+
                        "                               \"edgeNgramFilter\": {\n"+
                        "                                                       \"type\": \"edge_ngram\",\n"+
                        "                                                       \"min_gram\": 1,\n"+
                        "                                                       \"max_gram\": 50\n"+
                        "                                                    },\n"+
                        "                                 \"original_full_pinyin\": {\n"+
                        "                                                            \"type\": \"pinyin\",\n"+
                        "                                                             \"keep_separate_first_letter\": \"false\",\n"+
                        "                                                             \"keep_joined_full_pinyin\": \"true\",\n"+
                        "                                                             \"keep_none_chinese_in_joined_full_pinyin\": \"false\",\n"+
                        "                                                             \"keep_full_pinyin\": \"true\",\n"+
                        "                                                             \"keep_original\": \"true\",\n"+
                        "                                                             \"keep_none_chinese_together\": \"true\" \n"+
                        "                                                             },\n"+
                        "                                 \"original_joined_full_pinyin\": {\n"+
                        "                                                                   \"type\": \"pinyin\", \n"+
                        "                                                                   \"keep_separate_first_letter\": \"false\",\n"+
                        "                                                                   \"keep_joined_full_pinyin\": \"true\",\n"+
                        "                                                                   \"keep_none_chinese_in_joined_full_pinyin\": \"true\",\n"+
                        "                                                                   \"keep_full_pinyin\": \"false\",\n"+
                        "                                                                   \"keep_original\": \"true\",\n"+
                        "                                                                   \"keep_none_chinese_together\": \"false\",\n"+
                        "                                                                   \"keep_none_chinese\": \"false\" \n"+
                        "                                                                    }\n"+
                        "                                 },\n"+
                        "                   \"analyzer\": {\n"+
                        "                                \"edge_ngram_analyzer\": {\n"+
                        "                                                       \"type\": \"custom\",\n"+
                        "                                                        \"tokenizer\": \"edge_ngram_tokenizer\",\n"+
                        "                                                        \"filter\": [\"original_full_pinyin\"]\n"+
                        "                                                          },\n"+
                        "                                 \"ik_analyzer\": {\n"+
                        "                                                 \"type\": \"custom\",\n"+
                        "                                                 \"tokenizer\": \"ik_max_word\",\n"+
                        "                                                 \"filter\": [\"original_full_pinyin\"]\n"+
                        "                                                   },\n"+
                        "                                   \"original_keyword_pinyin_analyzer\": {\n"+
                        "                                                                       \"type\": \"custom\",\n"+
                        "                                                                       \"tokenizer\": \"keyword\",\n"+
                        "                                                                       \"filter\": [\"original_joined_full_pinyin\"]\n"+
                        "                                                                         },\n"+
                        "                                    \"ngramIndex\": {\n"+
                        "                                                  \"type\": \"custom\",\n"+
                        "                                                  \"tokenizer\": \"keyword\",\n"+
                        "                                                  \"filter\": [ \"edgeNgramFilter\", \"lowercase\"]\n"+
                        "                                                    },\n"+
                        "                                    \"ngramLowercase\": {\n"+
                        "                                                      \"filter\": [ \"lowercase\"],\n"+
                        "                                                      \"type\": \"custom\",\n"+
                        "                                                      \"tokenizer\": \"ngramTokenizer\" \n"+
                        "                                                        },\n"+
                        "                                    \"keywordLowercase\": {\n"+
                        "                                                       \"type\": \"custom\",\n"+
                        "                                                       \"filter\": [ \"lowercase\"],\n"+
                        "                                                        \"tokenizer\": \"keyword\"\n"+
                        "                                                         }\n"+
                        "                                    },\n"+
                        "                     \"tokenizer\": {\n"+
                                                             //ngram是从每一个字符开始,按照步长,进行分词,适合前缀中缀检索
                        "                                    \"ngramTokenizer\": {\n"+

                        "                                                     \"type\": \"nGram\",\n"+
                        "                                                     \"min_gram\": \"1\",\n"+
                        "                                                     \"max_gram\": \"50\"\n"+
                        "                                                        },\n"+
                                                            // edge_ngram是从第一个字符开始,按照步长,进行分词,适合前缀匹配场景,比如:订单号,手机号,邮政编码的检索
                        "                                     \"edge_ngram_tokenizer\": {\n"+
                        "                                                              \"type\": \"edge_ngram\",\n"+
                        "                                                               \"max_gram\": \"50\",\n"+
                        "                                                               \"mim_gram\": \"2\" \n"+
                        "                                                               }\n"+
                        "                                         }\n"+
                        "                       }\n"+
                        "},\n" +
                        "\"mappings\":{\n"+
                        "         \"properties\": {\n"+
                                                //字段名
                        "                        \"namekeyword\": {\n"+
                        "                                     \"type\": \"text\",\n"+
                        "                                     \"analyzer\": \"ik_analyzer\",\n"+
                        "                                     \"fields\": {\n"+
                        "                                              \"english\": {\n"+
                        "                                                     \"type\": \"text\",\n"+
                        "                                                      \"analyzer\": \"english\"\n"+
                        "                                                        },\n"+
                        "                                               \"pinyin\": {\n"+
                        "                                                         \"type\": \"text\",\n"+
                        "                                                         \"analyzer\": \"pinyin\"\n"+
                        "                                                           },\n"+
                        "                                                \"gram\": {\n"+
                        "                                                       \"type\": \"text\",\n"+
                        "                                                        \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                         },\n"+
                        "                                                 \"ngram_index\": {\n"+
                        "                                                               \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                \"type\": \"text\" \n"+
                        "                                                                   },\n"+
                        "                                                  \"keyword\": {\n"+
                        "                                                             \"type\": \"keyword\",\n"+
                        "                                                             \"ignore_above\": 256\n"+
                        "                                                               },\n"+
                        "                                                  \"ngram_keyword\": {\n"+
                        "                                                                   \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                   \"type\": \"text\"\n"+
                        "                                                                      }\n"+
                        "                                                     }\n"+
                        "                           },\n"+
                        "                        \"contentId\": {\n"+
                        "                                      \"type\": \"keyword\",\n"+
                        "                                      \"ignore_above\": 256\n"+
                        "                                        },\n"+
                        "                        \"seriesId\": {\n"+
                        "                                      \"type\": \"keyword\",\n"+
                        "                                      \"ignore_above\": 256\n"+
                        "                                       },\n"+
                        "                         \"counts\": {\n"+
                        "                                     \"type\": \"long\"\n"+
                        "                                     },\n"+
                        "                         \"status\": {\n"+
                        "                                     \"type\": \"integer\"\n"+
                        "                                     },\n"+
                        "                          \"name\": {\n"+
                        "                                    \"type\": \"text\",\n"+
                        "                                    \"analyzer\": \"ik_analyzer\",\n"+
                        "                                    \"fields\": {\n"+
                        "                                                \"suggest\": {\n"+
                        "                                                            \"type\": \"text\",\n"+
                        "                                                            \"analyzer\": \"edge_ngram_analyzer\"\n"+
                        "                                                              },\n"+
                        "                                                \"keyword\": {\n"+
                        "                                                             \"type\": \"keyword\",\n"+
                        "                                                             \"ignore_above\": 256\n"+
                        "                                                             },\n"+
                        "                                                 \"pinyin\": {\n"+
                        "                                                             \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"pinyin\"\n"+
                        "                                                              },\n"+
                        "                                                  \"ngram\": {\n"+
                        "                                                              \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                              },\n"+
                        "                                                   \"ngram_index\": {\n"+
                        "                                                                 \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                  \"type\": \"text\"\n"+
                        "                                                                    },\n"+
                        "                                                   \"ngram_keyword\": {\n"+
                        "                                                                    \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                    \"type\": \"text\"\n"+
                        "                                                                       },\n"+
                        "                                                   \"standard\": {\n"+
                        "                                                                 \"analyzer\": \"standard\",\n"+
                        "                                                                 \"type\": \"text\"\n"+
                        "                                                                 }\n"+
                        "                                                  }\n"+
                        "                                       },\n"+
                        "                           \"tags\": {\n"+
                        "                                     \"type\": \"text\",\n"+
                        "                                      \"analyzer\": \"ik_analyzer\",\n"+
                        "                                      \"fields\": {\n"+
                        "                                                 \"keyword\": {\n"+
                        "                                                              \"type\": \"keyword\",\n"+
                        "                                                              \"ignore_above\": 256 \n"+
                        "                                                               },\n"+
                        "                                                 \"pinyin\": {\n"+
                        "                                                              \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"pinyin\"\n"+
                        "                                                              },\n"+
                        "                                                  \"ngram\": {\n"+
                        "                                                             \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                              },\n"+
                        "                                                  \"ngram_index\": {\n"+
                        "                                                                \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                \"type\": \"text\"\n"+
                        "                                                                   },\n"+
                        "                                                   \"ngram_keyword\": {\n"+
                        "                                                                   \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                   \"type\": \"text\"\n"+
                        "                                                                       },\n"+
                        "                                                    \"standard\": {\n"+
                        "                                                                   \"analyzer\": \"standard\",\n"+
                        "                                                                   \"type\": \"text\"\n"+
                        "                                                                   }\n"+
                        "                                                    }\n"+
                        "                               },\n"+
                        "                               \"kind\": {\n"+
                        "                                         \"type\": \"text\",\n"+
                        "                                          \"analyzer\": \"ik_analyzer\",\n"+
                        "                                           \"fields\": {\n"+
                        "                                                       \"keyword\": {\n"+
                        "                                                                    \"type\": \"keyword\",\n"+
                        "                                                                     \"ignore_above\": 256\n"+
                        "                                                                     },\n"+
                        "                                                       \"pinyin\": {\n"+
                        "                                                                    \"type\": \"text\",\n"+
                        "                                                                    \"analyzer\": \"pinyin\"\n"+
                        "                                                                    },\n"+
                        "                                                         \"ngram\": {\n"+
                        "                                                                     \"type\": \"text\",\n"+
                        "                                                                     \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                                     },\n"+
                        "                                                         \"ngram_index\": {\n"+
                        "                                                                         \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                         \"type\": \"text\"\n"+
                        "                                                                          },\n"+
                        "                                                          \"ngram_keyword\": {\n"+
                        "                                                                            \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                             \"type\": \"text\"\n"+
                        "                                                                              },\n"+
                        "                                                           \"standard\": {\n"+
                        "                                                                          \"analyzer\": \"standard\",\n"+
                        "                                                                          \"type\": \"text\"\n"+
                        "                                                                          }\n"+
                        "                                                         }\n"+
                        "                             },\n"+
                        "                             \"actors\": {\n"+
                        "                                         \"type\": \"text\",\n"+
                        "                                          \"analyzer\": \"ik_analyzer\",\n"+
                        "                                           \"fields\": {\n"+
                        "                                                       \"keyword\": {\n"+
                        "                                                                    \"type\": \"keyword\",\n"+
                        "                                                                     \"ignore_above\": 256\n"+
                        "                                                                     },\n"+
                        "                                                       \"pinyin\": {\n"+
                        "                                                                    \"type\": \"text\",\n"+
                        "                                                                    \"analyzer\": \"pinyin\"\n"+
                        "                                                                    },\n"+
                        "                                                         \"ngram\": {\n"+
                        "                                                                     \"type\": \"text\",\n"+
                        "                                                                     \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                                     },\n"+
                        "                                                         \"ngram_index\": {\n"+
                        "                                                                         \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                         \"type\": \"text\"\n"+
                        "                                                                          },\n"+
                        "                                                          \"ngram_keyword\": {\n"+
                        "                                                                            \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                             \"type\": \"text\"\n"+
                        "                                                                              },\n"+
                        "                                                           \"standard\": {\n"+
                        "                                                                          \"analyzer\": \"standard\",\n"+
                        "                                                                          \"type\": \"text\"\n"+
                        "                                                                          }\n"+
                        "                                                         }\n"+
                        "                             },\n"+
                        "                            \"directors\": {\n"+
                        "                                         \"type\": \"text\",\n"+
                        "                                          \"analyzer\": \"ik_analyzer\",\n"+
                        "                                           \"fields\": {\n"+
                        "                                                       \"keyword\": {\n"+
                        "                                                                    \"type\": \"keyword\",\n"+
                        "                                                                     \"ignore_above\": 256\n"+
                        "                                                                     },\n"+
                        "                                                       \"pinyin\": {\n"+
                        "                                                                    \"type\": \"text\",\n"+
                        "                                                                    \"analyzer\": \"pinyin\"\n"+
                        "                                                                    },\n"+
                        "                                                         \"ngram\": {\n"+
                        "                                                                     \"type\": \"text\",\n"+
                        "                                                                     \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                                     },\n"+
                        "                                                         \"ngram_index\": {\n"+
                        "                                                                         \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                         \"type\": \"text\"\n"+
                        "                                                                          },\n"+
                        "                                                          \"ngram_keyword\": {\n"+
                        "                                                                            \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                             \"type\": \"text\"\n"+
                        "                                                                              },\n"+
                        "                                                           \"standard\": {\n"+
                        "                                                                          \"analyzer\": \"standard\",\n"+
                        "                                                                          \"type\": \"text\"\n"+
                        "                                                                          }\n"+
                        "                                                         }\n"+
                        "                             },\n"+
                        "                             \"programType\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                              \"ignore_above\": 256 \n"+
                        "                               },\n"+
                        "                            \"detailPicUrl\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                              \"ignore_above\": 256 \n"+
                        "                             },\n"+
                        "                            \"posterPicUrl\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                              \"ignore_above\": 256 \n"+
                        "                             },\n"+
                        "                            \"totalNum\": {\n"+
                        "                                              \"type\": \"long\" \n"+
                        "                             },\n"+
                        "                             \"updateNum\": {\n"+
                        "                                             \"type\": \"long\" \n"+
                        "                             },\n"+
                        "                             \"releaseYear\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                               \"ignore_above\": 256 \n"+
                        "                              },\n"+
                        "                            \"createDate\": { \n"+
                        "                                              \"type\": \"date\",\n"+
                        "                                               \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\" \n"+
                        "                                             }\n"+
                        "                             }\n"+
                        " },\n"+
                        "    \"aliases\" : {\n" +
                        "        \"twitter_alias\" : {}\n" +
                        "    }\n" +
                        "}",
                XContentType.JSON);


        CreateIndexResponse createIndexResponse = TestEsHighLevelClient.getClient().indices().create(request, RequestOptions.DEFAULT);
        String index=createIndexResponse.index();
        System.out.println("index:settingsAndMapping");
    }
    public static void createIndexSettings(CreateIndexRequest request) throws IOException {
        //CreateIndexRequest request = new CreateIndexRequest("twitter");
        request.settings(
                "{\n" +
                        "        \"number_of_shards\" : 1,\n" +
                        "        \"number_of_replicas\" : 0,\n" +
                        "        \"max_ngram_diff\": 50,\n"+
                        "        \"max_result_window\": 10000000,\n"+
                        "        \"analysis\": {\n" +
                        "                    \"filter\": {\n"+
                        "                               \"edgeNgramFilter\": {\n"+
                        "                                                       \"type\": \"edge_ngram\",\n"+
                        "                                                       \"min_gram\": 1,\n"+
                        "                                                       \"max_gram\": 50\n"+
                        "                                                    },\n"+
                        "                                 \"original_full_pinyin\": {\n"+
                        "                                                            \"type\": \"pinyin\",\n"+
                        "                                                             \"keep_separate_first_letter\": \"false\",\n"+
                        "                                                             \"keep_joined_full_pinyin\": \"true\",\n"+
                        "                                                             \"keep_none_chinese_in_joined_full_pinyin\": \"false\",\n"+
                        "                                                             \"keep_full_pinyin\": \"true\",\n"+
                        "                                                             \"keep_original\": \"true\",\n"+
                        "                                                             \"keep_none_chinese_together\": \"true\" \n"+
                        "                                                             },\n"+
                        "                                 \"original_joined_full_pinyin\": {\n"+
                        "                                                                   \"type\": \"pinyin\", \n"+
                        "                                                                   \"keep_separate_first_letter\": \"false\",\n"+
                        "                                                                   \"keep_joined_full_pinyin\": \"true\",\n"+
                        "                                                                   \"keep_none_chinese_in_joined_full_pinyin\": \"true\",\n"+
                        "                                                                   \"keep_full_pinyin\": \"false\",\n"+
                        "                                                                   \"keep_original\": \"true\",\n"+
                        "                                                                   \"keep_none_chinese_together\": \"false\",\n"+
                        "                                                                   \"keep_none_chinese\": \"false\" \n"+
                        "                                                                    }\n"+
                        "                                 },\n"+
                        "                   \"analyzer\": {\n"+
                        "                                \"edge_ngram_analyzer\": {\n"+
                        "                                                       \"type\": \"custom\",\n"+
                        "                                                        \"tokenizer\": \"edge_ngram_tokenizer\",\n"+
                        "                                                        \"filter\": [\"original_full_pinyin\"]\n"+
                        "                                                          },\n"+
                        "                                 \"ik_analyzer\": {\n"+
                        "                                                 \"type\": \"custom\",\n"+
                        "                                                 \"tokenizer\": \"ik_max_word\",\n"+
                        "                                                 \"filter\": [\"original_full_pinyin\"]\n"+
                        "                                                   },\n"+
                        "                                   \"original_keyword_pinyin_analyzer\": {\n"+
                        "                                                                       \"type\": \"custom\",\n"+
                        "                                                                       \"tokenizer\": \"keyword\",\n"+
                        "                                                                       \"filter\": [\"original_joined_full_pinyin\"]\n"+
                        "                                                                         },\n"+
                        "                                    \"ngramIndex\": {\n"+
                        "                                                  \"type\": \"custom\",\n"+
                        "                                                  \"tokenizer\": \"keyword\",\n"+
                        "                                                  \"filter\": [ \"edgeNgramFilter\", \"lowercase\"]\n"+
                        "                                                    },\n"+
                        "                                    \"ngramLowercase\": {\n"+
                        "                                                      \"filter\": [ \"lowercase\"],\n"+
                        "                                                      \"type\": \"custom\",\n"+
                        "                                                      \"tokenizer\": \"ngramTokenizer\" \n"+
                        "                                                        },\n"+
                        "                                    \"keywordLowercase\": {\n"+
                        "                                                       \"type\": \"custom\",\n"+
                        "                                                       \"filter\": [ \"lowercase\"],\n"+
                        "                                                        \"tokenizer\": \"keyword\"\n"+
                        "                                                         }\n"+
                        "                                    },\n"+
                        "                     \"tokenizer\": {\n"+
                        //ngram是从每一个字符开始,按照步长,进行分词,适合前缀中缀检索
                        "                                    \"ngramTokenizer\": {\n"+

                        "                                                     \"type\": \"nGram\",\n"+
                        "                                                     \"min_gram\": \"1\",\n"+
                        "                                                     \"max_gram\": \"50\"\n"+
                        "                                                        },\n"+
                        // edge_ngram是从第一个字符开始,按照步长,进行分词,适合前缀匹配场景,比如:订单号,手机号,邮政编码的检索
                        "                                     \"edge_ngram_tokenizer\": {\n"+
                        "                                                              \"type\": \"edge_ngram\",\n"+
                        "                                                               \"max_gram\": \"50\",\n"+
                        "                                                               \"mim_gram\": \"2\" \n"+
                        "                                                               }\n"+
                        "                                         }\n"+
                        "                       }\n"+
                        "}\n"
                , XContentType.JSON);

        //CreateIndexResponse createIndexResponse = TestEsHighLevelClient.getClient().indices().create(request, RequestOptions.DEFAULT);
        System.out.println("createIndexSettings");

    }
    public static void createIndexMpping(CreateIndexRequest request) throws IOException {
        //CreateIndexRequest request = new CreateIndexRequest("twitter");
        request.mapping(
                "{\n"+
                        "         \"properties\": {\n"+
                        //字段名
                        "                        \"namekeyword\": {\n"+
                        "                                     \"type\": \"text\",\n"+
                        "                                     \"analyzer\": \"ik_analyzer\",\n"+
                        "                                     \"fields\": {\n"+
                        "                                              \"english\": {\n"+
                        "                                                     \"type\": \"text\",\n"+
                        "                                                      \"analyzer\": \"english\"\n"+
                        "                                                        },\n"+
                        "                                               \"pinyin\": {\n"+
                        "                                                         \"type\": \"text\",\n"+
                        "                                                         \"analyzer\": \"pinyin\"\n"+
                        "                                                           },\n"+
                        "                                                \"gram\": {\n"+
                        "                                                       \"type\": \"text\",\n"+
                        "                                                        \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                         },\n"+
                        "                                                 \"ngram_index\": {\n"+
                        "                                                               \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                \"type\": \"text\" \n"+
                        "                                                                   },\n"+
                        "                                                  \"keyword\": {\n"+
                        "                                                             \"type\": \"keyword\",\n"+
                        "                                                             \"ignore_above\": 256\n"+
                        "                                                               },\n"+
                        "                                                  \"ngram_keyword\": {\n"+
                        "                                                                   \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                   \"type\": \"text\"\n"+
                        "                                                                      }\n"+
                        "                                                     }\n"+
                        "                           },\n"+
                        "                        \"contentId\": {\n"+
                        "                                      \"type\": \"keyword\",\n"+
                        "                                      \"ignore_above\": 256\n"+
                        "                                        },\n"+
                        "                        \"seriesId\": {\n"+
                        "                                      \"type\": \"keyword\",\n"+
                        "                                      \"ignore_above\": 256\n"+
                        "                                       },\n"+
                        "                         \"counts\": {\n"+
                        "                                     \"type\": \"long\"\n"+
                        "                                     },\n"+
                        "                         \"status\": {\n"+
                        "                                     \"type\": \"integer\"\n"+
                        "                                     },\n"+
                        "                          \"name\": {\n"+
                        "                                    \"type\": \"text\",\n"+
                        "                                    \"analyzer\": \"ik_analyzer\",\n"+
                        "                                    \"fields\": {\n"+
                        "                                                \"suggest\": {\n"+
                        "                                                            \"type\": \"text\",\n"+
                        "                                                            \"analyzer\": \"edge_ngram_analyzer\"\n"+
                        "                                                              },\n"+
                        "                                                \"keyword\": {\n"+
                        "                                                             \"type\": \"keyword\",\n"+
                        "                                                             \"ignore_above\": 256\n"+
                        "                                                             },\n"+
                        "                                                 \"pinyin\": {\n"+
                        "                                                             \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"pinyin\"\n"+
                        "                                                              },\n"+
                        "                                                  \"ngram\": {\n"+
                        "                                                              \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                              },\n"+
                        "                                                   \"ngram_index\": {\n"+
                        "                                                                 \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                  \"type\": \"text\"\n"+
                        "                                                                    },\n"+
                        "                                                   \"ngram_keyword\": {\n"+
                        "                                                                    \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                    \"type\": \"text\"\n"+
                        "                                                                       },\n"+
                        "                                                   \"standard\": {\n"+
                        "                                                                 \"analyzer\": \"standard\",\n"+
                        "                                                                 \"type\": \"text\"\n"+
                        "                                                                 }\n"+
                        "                                                  }\n"+
                        "                                       },\n"+
                        "                           \"tags\": {\n"+
                        "                                     \"type\": \"text\",\n"+
                        "                                      \"analyzer\": \"ik_analyzer\",\n"+
                        "                                      \"fields\": {\n"+
                        "                                                 \"keyword\": {\n"+
                        "                                                              \"type\": \"keyword\",\n"+
                        "                                                              \"ignore_above\": 256 \n"+
                        "                                                               },\n"+
                        "                                                 \"pinyin\": {\n"+
                        "                                                              \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"pinyin\"\n"+
                        "                                                              },\n"+
                        "                                                  \"ngram\": {\n"+
                        "                                                             \"type\": \"text\",\n"+
                        "                                                              \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                              },\n"+
                        "                                                  \"ngram_index\": {\n"+
                        "                                                                \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                \"type\": \"text\"\n"+
                        "                                                                   },\n"+
                        "                                                   \"ngram_keyword\": {\n"+
                        "                                                                   \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                   \"type\": \"text\"\n"+
                        "                                                                       },\n"+
                        "                                                    \"standard\": {\n"+
                        "                                                                   \"analyzer\": \"standard\",\n"+
                        "                                                                   \"type\": \"text\"\n"+
                        "                                                                   }\n"+
                        "                                                    }\n"+
                        "                               },\n"+
                        "                               \"kind\": {\n"+
                        "                                         \"type\": \"text\",\n"+
                        "                                          \"analyzer\": \"ik_analyzer\",\n"+
                        "                                           \"fields\": {\n"+
                        "                                                       \"keyword\": {\n"+
                        "                                                                    \"type\": \"keyword\",\n"+
                        "                                                                     \"ignore_above\": 256\n"+
                        "                                                                     },\n"+
                        "                                                       \"pinyin\": {\n"+
                        "                                                                    \"type\": \"text\",\n"+
                        "                                                                    \"analyzer\": \"pinyin\"\n"+
                        "                                                                    },\n"+
                        "                                                         \"ngram\": {\n"+
                        "                                                                     \"type\": \"text\",\n"+
                        "                                                                     \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                                     },\n"+
                        "                                                         \"ngram_index\": {\n"+
                        "                                                                         \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                         \"type\": \"text\"\n"+
                        "                                                                          },\n"+
                        "                                                          \"ngram_keyword\": {\n"+
                        "                                                                            \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                             \"type\": \"text\"\n"+
                        "                                                                              },\n"+
                        "                                                           \"standard\": {\n"+
                        "                                                                          \"analyzer\": \"standard\",\n"+
                        "                                                                          \"type\": \"text\"\n"+
                        "                                                                          }\n"+
                        "                                                         }\n"+
                        "                             },\n"+
                        "                             \"actors\": {\n"+
                        "                                         \"type\": \"text\",\n"+
                        "                                          \"analyzer\": \"ik_analyzer\",\n"+
                        "                                           \"fields\": {\n"+
                        "                                                       \"keyword\": {\n"+
                        "                                                                    \"type\": \"keyword\",\n"+
                        "                                                                     \"ignore_above\": 256\n"+
                        "                                                                     },\n"+
                        "                                                       \"pinyin\": {\n"+
                        "                                                                    \"type\": \"text\",\n"+
                        "                                                                    \"analyzer\": \"pinyin\"\n"+
                        "                                                                    },\n"+
                        "                                                         \"ngram\": {\n"+
                        "                                                                     \"type\": \"text\",\n"+
                        "                                                                     \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                                     },\n"+
                        "                                                         \"ngram_index\": {\n"+
                        "                                                                         \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                         \"type\": \"text\"\n"+
                        "                                                                          },\n"+
                        "                                                          \"ngram_keyword\": {\n"+
                        "                                                                            \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                             \"type\": \"text\"\n"+
                        "                                                                              },\n"+
                        "                                                           \"standard\": {\n"+
                        "                                                                          \"analyzer\": \"standard\",\n"+
                        "                                                                          \"type\": \"text\"\n"+
                        "                                                                          }\n"+
                        "                                                         }\n"+
                        "                             },\n"+
                        "                            \"directors\": {\n"+
                        "                                         \"type\": \"text\",\n"+
                        "                                          \"analyzer\": \"ik_analyzer\",\n"+
                        "                                           \"fields\": {\n"+
                        "                                                       \"keyword\": {\n"+
                        "                                                                    \"type\": \"keyword\",\n"+
                        "                                                                     \"ignore_above\": 256\n"+
                        "                                                                     },\n"+
                        "                                                       \"pinyin\": {\n"+
                        "                                                                    \"type\": \"text\",\n"+
                        "                                                                    \"analyzer\": \"pinyin\"\n"+
                        "                                                                    },\n"+
                        "                                                         \"ngram\": {\n"+
                        "                                                                     \"type\": \"text\",\n"+
                        "                                                                     \"analyzer\": \"ngramLowercase\"\n"+
                        "                                                                     },\n"+
                        "                                                         \"ngram_index\": {\n"+
                        "                                                                         \"analyzer\": \"ngramIndex\",\n"+
                        "                                                                         \"type\": \"text\"\n"+
                        "                                                                          },\n"+
                        "                                                          \"ngram_keyword\": {\n"+
                        "                                                                            \"analyzer\": \"keywordLowercase\",\n"+
                        "                                                                             \"type\": \"text\"\n"+
                        "                                                                              },\n"+
                        "                                                           \"standard\": {\n"+
                        "                                                                          \"analyzer\": \"standard\",\n"+
                        "                                                                          \"type\": \"text\"\n"+
                        "                                                                          }\n"+
                        "                                                         }\n"+
                        "                             },\n"+
                        "                             \"programType\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                              \"ignore_above\": 256 \n"+
                        "                               },\n"+
                        "                            \"detailPicUrl\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                              \"ignore_above\": 256 \n"+
                        "                             },\n"+
                        "                            \"posterPicUrl\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                              \"ignore_above\": 256 \n"+
                        "                             },\n"+
                        "                            \"totalNum\": {\n"+
                        "                                              \"type\": \"long\" \n"+
                        "                             },\n"+
                        "                             \"updateNum\": {\n"+
                        "                                             \"type\": \"long\" \n"+
                        "                             },\n"+
                        "                             \"releaseYear\": {\n"+
                        "                                              \"type\": \"keyword\",\n"+
                        "                                               \"ignore_above\": 256 \n"+
                        "                              },\n"+
                        "                            \"createDate\": { \n"+
                        "                                              \"type\": \"date\",\n"+
                        "                                               \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\" \n"+
                        "                                             }\n"+
                        "                             }\n"+
                        " }\n"
                , XContentType.JSON);

        //CreateIndexResponse createIndexResponse = TestEsHighLevelClient.getClient().indices().create(request, RequestOptions.DEFAULT);
        System.out.println("createIndexMpping");
    }

    public static void createIndexRequest() throws Exception {
        CreateIndexRequest request = new CreateIndexRequest("twitter");
        IndexSettingsMapping.createIndexSettings(request);
        IndexSettingsMapping.createIndexMpping(request);
        CreateIndexResponse createIndexResponse = TestEsHighLevelClient.getClient().indices().create(request, RequestOptions.DEFAULT);
    }
}
