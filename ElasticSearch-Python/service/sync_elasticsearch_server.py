# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40

import os
import time
import traceback
import pymysql
from os import walk
from datetime import datetime

from elasticsearch import Elasticsearch
from elasticsearch.helpers import bulk
from scarecrow.globals import db_config, db_name, runlog, elaticsearch_ip, elaticsearch_port, \
    elasticsearch_search_index_alias, suggest_index_alias, Sync_es_num, cast_index_alias


class ElasticObj:
    def __init__(self, ip=elaticsearch_ip, port=elaticsearch_port):
        '''
        :param index_name: 索引名称
        :param index_type: 索引类型
        '''
        # 无用户名密码状态
        self.es = Elasticsearch([ip], port=int(port), timeout=600)
        # 用户名密码状态
        # self.es = Elasticsearch([ip],http_auth=('elastic', 'password'),port=9200)

    def create_series_index(self, index_name="series", index_type="series"):
        # 创建映射
        _index_mappings = {
            "settings": {
                # es7 以后要显示是定
                "max_ngram_diff": 50,
                "number_of_replicas": "1",
                "number_of_shards": "1",
                "max_result_window": "10000000",
                "analysis": {
                    "filter": {
                        "edgeNgramFilter": {
                            "type": "edge_ngram",
                            "min_gram": 1,
                            "max_gram": 50
                        },
                        "original_full_pinyin": {
                            "type": "pinyin",
                            "keep_separate_first_letter": "false",
                            "keep_joined_full_pinyin": "true",
                            "keep_none_chinese_in_joined_full_pinyin": "false",
                            "keep_full_pinyin": "true",
                            "keep_original": "true",
                            "keep_none_chinese_together": "true"
                        },
                        "original_joined_full_pinyin": {
                            "type": "pinyin",
                            "keep_separate_first_letter": "false",
                            "keep_joined_full_pinyin": "true",
                            "keep_none_chinese_in_joined_full_pinyin": "true",
                            "keep_full_pinyin": "false",
                            "keep_original": "true",
                            "keep_none_chinese_together": "false",
                            "keep_none_chinese ": "false"
                        }
                    },
                    "analyzer": {
                        "edge_ngram_analyzer": {
                            "type": "custom",
                            "tokenizer": "edge_ngram_tokenizer",
                            "filter": ["original_full_pinyin"]
                        },
                        "ik_analyzer": {
                            "type": "custom",
                            "tokenizer": "ik_max_word",
                            "filter": ["original_full_pinyin"]
                        },
                        "original_keyword_pinyin_analyzer": {
                            "type": "custom",
                            "tokenizer": "keyword",
                            "filter": ["original_joined_full_pinyin"]
                        },
                        "ngramIndex": {
                            "type": "custom",
                            "tokenizer": "keyword",
                            "filter": [
                                "edgeNgramFilter",
                                "lowercase"
                            ]
                        },
                        "ngramLowercase": {
                            "filter": [
                                "lowercase"
                            ],
                            "type": "custom",
                            "tokenizer": "ngramTokenizer"
                        },
                        "keywordLowercase": {
                            "type": "custom",
                            "filter": [
                                "lowercase"
                            ],
                            "tokenizer": "keyword"
                        }
                    },
                    "tokenizer": {
                        "ngramTokenizer": {
                            #  ngram是从每一个字符开始,按照步长,进行分词,适合前缀中缀检索
                            "type": "nGram",
                            "min_gram": "1",
                            "max_gram": "50"
                        },
                        "edge_ngram_tokenizer": {
                            # edge_ngram是从第一个字符开始,按照步长,进行分词,适合前缀匹配场景,比如:订单号,手机号,邮政编码的检索
                            "type": "edge_ngram",
                            "max_gram": "50",
                            "mim_gram": "2"
                        }
                    }
                }
            },
            "mappings": {
                "properties": {
                    "namekeyword": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "english": {
                                "type": "text",
                                "analyzer": "english"
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            }
                        }
                    },
                    "contentId": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "seriesId": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "counts": {
                        "type": "long"
                    },
                    "status": {
                        "type": "integer"
                    },
                    "name": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "suggest": {
                                "type": "text",
                                "analyzer": "edge_ngram_analyzer"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            },
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "tags": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            }
                            ,
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "kind": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            }
                            ,
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "actors": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "directors": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            }
                            ,
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            }
                            ,
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "programType": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "detailPicUrl": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "posterPicUrl": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "totalNum": {
                        "type": "long"
                    },
                    "updateNum": {
                        "type": "long"
                    },
                    "releaseYear": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "createDate": {
                        "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                    }
                }
            }
        }
        if self.es.indices.exists(index=index_name) is not True:
            res = self.es.indices.create(index=index_name, body=_index_mappings)
            # self.create_series_type(index_name, index_type)
            self.put_search_alias(index_name)

    def create_series_type(self, index_name="series", index_type="series"):
        """
        创建索引,创建索引名称为ott，类型为ott_type的索引
        :param index_type:
        :param index_name:
        :return:
        """
        # 创建映射
        _index_mappings = {
            "mappings": {
                "properties": {
                    "namekeyword": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "english": {
                                "type": "text",
                                "analyzer": "english"
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            }
                        }
                    },
                    "contentId": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "seriesId": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "counts": {
                        "type": "long"
                    },
                    "status": {
                        "type": "integer"
                    },
                    "name": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "suggest": {
                                "type": "text",
                                "analyzer": "edge_ngram_analyzer"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            },
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "tags": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            }
                            ,
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "kind": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            }
                            ,
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "actors": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "directors": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            }
                            ,
                            "ngram": {
                                "type": "text",
                                "analyzer": "ngramLowercase"
                            }
                            ,
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            }
                            ,
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            },
                            "standard": {
                                "analyzer": "standard",
                                "type": "text"
                            }
                        }
                    },
                    "programType": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "detailPicUrl": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "posterPicUrl": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "totalNum": {
                        "type": "long"
                    },
                    "updateNum": {
                        "type": "long"
                    },
                    "releaseYear": {
                        "type": "keyword",
                        "ignore_above": 256
                    },
                    "createDate": {
                        "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                    }
                }
            }
        }

        # if self.es.indices.exists(index=self.index_name) is not True:
        self.es.indices.put_mapping(index_type, _index_mappings, index=index_name)

    def create_cast_type(self, index_name="ott_website_cast", index_type="cast"):
        """
        创建索引,创建索引名称为cast，类型为cast_type的索引
        :param index_type:
        :param index_name:
        :return:
        """
        # 创建映射
        _index_mappings = {
            "mappings": {
                "properties": {
                    "name": {
                        "type": "text",
                        "analyzer": "keyword_pinyin_analyzer"
                    },
                    "createDate": {
                        "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                    }
                }
            }
        }
        self.es.indices.put_mapping(index_type, _index_mappings, index=index_name)

    def create_cast_index(self, index_name="ott_website_cast", index_type="cast"):
        # 创建映射
        _index_mappings = {

            "settings": {
                # es7 以后要显示是定
                "max_ngram_diff": 50,
                "number_of_replicas": "1",
                "analysis": {
                    "analyzer": {
                        "keyword_pinyin_analyzer": {
                            "type": "custom",
                            "tokenizer": "keyword",
                            "filter": ["pinyin_filter"]
                        }
                    },
                    "filter": {
                        "pinyin_filter": {
                            "type": "pinyin",
                            "keep_separate_first_letter": "false",
                            "keep_joined_full_pinyin": "true",
                            "keep_none_chinese_in_joined_full_pinyin": "false",
                            "keep_full_pinyin": "false",
                            "keep_original": "true",
                            "keep_none_chinese_together": "true"
                        }
                    }
                },
                "number_of_shards": "1",
                "max_result_window": "10000000"
            },
            "mappings": {
                "properties": {
                    "name": {
                        "type": "text",
                        "analyzer": "keyword_pinyin_analyzer"
                    },
                    "createDate": {
                        "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                    }
                }
            }
        }
        if self.es.indices.exists(index=index_name) is not True:
            self.es.indices.create(index=index_name, body=_index_mappings)
            # self.create_cast_type(index_name, index_type)
            self.es.indices.put_alias(index_name, cast_index_alias)

    def create_suggest_type(self, index_name, index_type):
        # 创建映射 # 自己加的创建 mappings
        _index_mappings = {
            "mappings": {
                "properties": {
                    "name": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "suggest": {
                                "type": "text",
                                "analyzer": "edge_ngram_analyzer"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "english": {
                                "type": "completion",
                                "analyzer": "english"
                            },
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            }
                        }
                    },
                    "namekeyword": {
                        "type": "text",
                        "analyzer": "ik_max_word",
                        "fields": {
                            "standard": {
                                "type": "text",
                                "analyzer": "standard"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "suggest_pinyin": {
                                "type": "completion",
                                "analyzer": "pinyin"
                            },
                            "suggest_english": {
                                "type": "completion",
                                "analyzer": "english"
                            },
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            }
                        }
                    },
                    "createDate": {
                        "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                    },
                    "describe": {
                        "type": "text",
                        "analyzer": "ik_max_word"
                    }
                }
            }
        }
        self.es.indices.put_mapping(_index_mappings, index_type, index=index_name)

    def create_suggest_index(self, index_name="ott_website_suggest", index_type="suggest"):
        # 创建映射
        _index_mappings = {
            "settings": {
                # es7 以后要显示是定
                "max_ngram_diff": 50,
                "number_of_replicas": "1",
                "analysis": {
                    "filter": {
                        "edgeNgramFilter": {
                            "type": "edge_ngram",
                            "min_gram": "1",
                            "max_gram": "50"
                        },
                        "original_full_pinyin": {
                            "type": "pinyin",
                            "keep_separate_first_letter": "false",
                            "keep_joined_full_pinyin": "true",
                            "keep_none_chinese_in_joined_full_pinyin": "false",
                            "keep_full_pinyin": "true",
                            "keep_original": "true",
                            "keep_none_chinese_together": "true"
                        },
                        "original_joined_full_pinyin": {
                            "type": "pinyin",
                            "keep_separate_first_letter": "false",
                            "keep_joined_full_pinyin": "true",
                            "keep_none_chinese_in_joined_full_pinyin": "true",
                            "keep_full_pinyin": "false",
                            "keep_original": "true",
                            "keep_none_chinese_together": "false",
                            "keep_none_chinese ": "false"
                        }
                    },
                    "analyzer": {
                        "edge_ngram_analyzer": {
                            "type": "custom",
                            "tokenizer": "edge_ngram_tokenizer",
                            "filter": ["original_full_pinyin"]
                        },
                        "ik_analyzer": {
                            "type": "custom",
                            "tokenizer": "ik_max_word",
                            "filter": ["original_full_pinyin"]
                        },
                        "original_keyword_pinyin_analyzer": {
                            "type": "custom",
                            "tokenizer": "keyword",
                            "filter": ["original_joined_full_pinyin"]
                        },
                        "ngramIndex": {
                            "type": "custom",
                            "tokenizer": "keyword",
                            "filter": [
                                "edgeNgramFilter",
                                "lowercase"
                            ]
                        },
                        "ngramLowercase": {
                            "filter": [
                                "lowercase"
                            ],
                            "type": "custom",
                            "tokenizer": "ngramTokenizer"
                        },
                        "keywordLowercase": {
                            "type": "custom",
                            "filter": [
                                "lowercase"
                            ],
                            "tokenizer": "keyword"
                        }
                    },
                    "tokenizer": {
                        "ngramTokenizer": {
                            "type": "nGram",
                            "min_gram": "1",
                            "max_gram": "50"
                        },
                        "edge_ngram_tokenizer": {
                            "type": "edge_ngram",
                            "mim_gram": "2",
                            "max_gram": "50"

                        }
                    }
                },
                "number_of_shards": "1",
                "max_result_window": "10000000"
            },
            # mappings 映射
            "mappings": {
                "properties": {
                    "name": {
                        "type": "text",
                        "analyzer": "ik_analyzer",
                        "fields": {
                            "suggest": {
                                "type": "text",
                                "analyzer": "edge_ngram_analyzer"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "pinyin": {
                                "type": "text",
                                "analyzer": "pinyin"
                            },
                            "english": {
                                "type": "completion",
                                "analyzer": "english"
                            },
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            }
                        }
                    },
                    "namekeyword": {
                        "type": "text",
                        "analyzer": "ik_max_word",
                        "fields": {
                            "standard": {
                                "type": "text",
                                "analyzer": "standard"
                            },
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            },
                            "suggest_pinyin": {
                                "type": "completion",
                                "analyzer": "pinyin"
                            },
                            "suggest_english": {
                                "type": "completion",
                                "analyzer": "english"
                            },
                            "ngram_index": {
                                "analyzer": "ngramIndex",
                                "type": "text"
                            },
                            "ngram_keyword": {
                                "analyzer": "keywordLowercase",
                                "type": "text"
                            }
                        }
                    },
                    "createDate": {
                        "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                    },
                    "describe": {
                        "type": "text",
                        "analyzer": "ik_max_word"
                    }
                }
            }
        }
        if self.es.indices.exists(index=index_name) is not True:
            self.es.indices.create(index=index_name, body=_index_mappings)
            # self.create_suggest_type(index_name, index_type)
            self.es.indices.put_alias(index_name, suggest_index_alias)

    def Index_Data(self):
        """
        数据存储到es
        :return:
        """
        list = [
            {"date": "2017-09-13",
             "source": "慧聪网",
             "link": "http://info.broadcast.hc360.com/2017/09/130859749974.shtml",
             "keyword": "电视",
             "title": "付费 电视 行业面临的转型和挑战"
             },
            {"date": "2017-09-13",
             "source": "中国文明网",
             "link": "http://www.wenming.cn/xj_pd/yw/201709/t20170913_4421323.shtml",
             "keyword": "电视",
             "title": "电视 专题片《巡视利剑》广获好评：铁腕反腐凝聚党心民心"
             }
        ]
        for item in list:
            res = self.es.index(index=self.index_name, doc_type=self.index_type, body=item)

    def bulk_Index_Data(self, index_name, index_type, list):
        """
        用bulk将批量数据存储到es
        :return:
        """
        try:
            ACTIONS = []
            for line in list:
                action = {
                    "_index": index_name,
                    #  "_type": index_type,
                    "_id": line["contentId"],  # _id 也可以默认生成，不赋值
                    "_source": {
                        "contentId": line.get("contentId", "NULL"),
                        "seriesId": line.get("seriesId", "NULL"),
                        "name": line.get("name", "NULL"),
                        "tags": line.get("tags", "NULL"),
                        "actors": line.get("actors", "NULL"),
                        "directors": line.get("directors", "NULL"),
                        "status": line.get("status", "NULL"),
                        "counts": int(line.get("counts", 0) if line.get("counts", 0) else 0),
                        "kind": line.get("kind", "null"),
                        "namekeyword": line.get("namekeyword", "null"),
                        "detailPicUrl": line.get("detailPicUrl", "null"),
                        "posterPicUrl": line.get("posterPicUrl", "null"),
                        "updateNum": line.get("updateNum", "null"),
                        "totalNum": line.get("totalNum", "null"),
                        "releaseYear": line.get("releaseYear", "null"),
                        "programType": line.get("programType", "null"),
                        "createDate": line.get("createDate").strftime("%Y-%m-%d %H:%M:%S") if line.get(
                            "createDate") else datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    }
                }
                ACTIONS.append(action)
                # 批量处理
            success, _ = bulk(self.es, ACTIONS, index=index_name, raise_on_error=True)
            runlog.debug('Performed %d actions' % success)
            self.change_is_sync_series(list)
        except:
            runlog.error(traceback.format_exc())

    def bulk_Suggest_Data(self, index_name, index_type, list):
        '''
        用bulk将批量数据存储到es
        :return:
        '''
        try:
            ACTIONS = []
            for line in list:
                action = {
                    "_index": index_name,
                    # "_type": index_type,
                    "_id": line["id"],  # _id 也可以默认生成，不赋值
                    "_source": {
                        "name": line.get("name", "NULL"),
                        "describe": line.get("describe", "NULL"),
                        "namekeyword": line.get("keywords", "NULL") if line.get("keywords", "NULL") else "NULL",
                        "createDate": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    }
                }
                ACTIONS.append(action)
                # 批量处理
            success, _ = bulk(self.es, ACTIONS, index=index_name, raise_on_error=True)
            runlog.debug('Performed %d actions' % success)
            self.change_is_sync_suggest(list)
        except:
            runlog.error(traceback.format_exc())

    def Delete_Index_Data(self, index_name, index_type, id):
        """
        删除索引中的一条
        :param index_name:
        :param index_type:
        :param id:
        :return:
        """
        try:
            if id:
                # res = self.es.delete(index=index_name, doc_type=index_type, id=id)
                res = self.es.delete(index=index_name, id=id)
                runlog.debug(res)
        except:
            runlog.error(traceback.format_exc())

    def bulk_Cast_Data(self, index_name, index_type, list):
        """
        用bulk将批量数据存储到es
        :return:
        """
        try:
            ACTIONS = []
            for line in list:
                action = {
                    "_index": index_name,
                    # "_type": index_type,
                    "_id": line["castId"],  # _id 也可以默认生成，不赋值
                    "_source": {
                        "castId": line.get("castId", "NULL"),
                        "name": line.get("name", "NULL"),
                        "role": line.get("role", "NULL"),
                        "height": line.get("height", "NULL"),
                        "weight": line.get("weight", "NULL"),
                        "eduction": line.get("eduction", "NULL"),
                        "nation": line.get("nation", "NULL"),
                        "hometown": line.get("hometown", "NULL"),
                        "gender": line.get("gender", "NULL"),
                        "birthday": line.get("birthday", "NULL"),
                        "poster": line.get("poster", "NULL"),
                        "thumbnail": line.get("thumbnail", "NULL"),
                        "constellation": line.get("constellation", "NULL"),
                        "ename": line.get("ename", "NULL"),
                        "tags": line.get("tags", "NULL"),
                        "subname": line.get("subname", "NULL"),
                        "namekeyword": line.get("namekeyword", "NULL"),
                        "description": line.get("description", "NULL"),
                        "describe": line.get("describe", "NULL"),
                        "createDate": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    }
                }
                ACTIONS.append(action)
                # 批量处理
            success, _ = bulk(self.es, ACTIONS, index=index_name, raise_on_error=True)
            runlog.debug('Performed %d actions' % success)
            self.change_is_sync_cast(list)
        except:
            runlog.error(traceback.format_exc())

    def Get_Data_Id(self, id):

        res = self.es.get(index=self.index_name, doc_type=self.index_type, id=id)

        # # 输出查询到的结果
        for hit in res['hits']['hits']:
            # print hit['_source']
            print(hit['_source']['date'], hit['_source']['source'], hit['_source']['link'], hit['_source']['keyword'],
                  hit['_source']['title'])

    def Get_Data_By_Body(self):
        # doc = {'query': {'match_all': {}}}
        doc = {
            "query": {
                "match": {
                    "keyword": "电视"
                }
            }
        }
        _searched = self.es.search(index=self.index_name, doc_type=self.index_type, body=doc)

        for hit in _searched['hits']['hits']:
            # print hit['_source']
            print(hit['_source']['date'], hit['_source']['source'], hit['_source']['link'], hit['_source']['keyword'],
                  hit['_source']['title'])

    def put_search_alias(self, index_name):
        self.es.indices.put_alias(index_name, elasticsearch_search_index_alias)

    def Set_Mysql_Sync_Update_Data(self, index_name, type_name):
        self.create_series_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            sync_updata_sql_count = '''
            SELECT
            count(*) as total
            FROM
            `series`
            WHERE
            `isDelete` = '0'
            AND 
            `isSync` = '0'
            '''
            cursor_conn.execute(sync_updata_sql_count)
            results = cursor_conn.fetchall()

            total = results[0].get("total")
            if int(total) > 0:
                while (True):
                    sync_updata_sql = '''
                    SELECT
                    `ContentId`,
                    `SeriesId`,
                    `Name`,
                    `CreateDate`,
                    `Actors`,
                    `Directors`,
                    `Intro`,
                    `Keywords`,
                    `Tags`,
                    `Status`,
                    `Counts`,
                    `Kind`,
                    `DetailPicUrl`,
                    `PosterPicUrl`,
                    `UpdateNum`,
                    `TotalNum`,
                    `ReleaseYear`,
                    `ProgramType`
                    FROM
                    `series`
                    WHERE
                    `isDelete` = '0'
                    AND 
                    `isSync` = '0'
                    ORDER BY `insertDate`
                    LIMIT
                    %s;
                    ''' % (Sync_es_num)

                    cursor_conn.execute(sync_updata_sql)
                    results = cursor_conn.fetchall()
                    res_list = []
                    if not results:
                        break
                    for result in results:
                        sync_dict = {
                            "name": result.get("Name", "null"),
                            "contentId": result.get("ContentId", "null"),
                            "seriesId": result.get("SeriesId", "null"),
                            "actors": result.get("Actors", "null"),
                            "directors": result.get("Directors", "null"),
                            "namekeyword": result.get("Keywords", "null"),
                            "tags": result.get("Tags", "null"),
                            "status": result.get("Status", "null"),
                            "counts": result.get("Counts", "null"),
                            "kind": result.get("Kind", "null"),
                            "detailPicUrl": result.get("DetailPicUrl", "null"),
                            "posterPicUrl": result.get("PosterPicUrl", "null"),
                            "updateNum": result.get("UpdateNum", "null"),
                            "totalNum": result.get("TotalNum", "null"),
                            "releaseYear": result.get("ReleaseYear", "null"),
                            "programType": result.get("ProgramType", "null"),
                            "createDate": result.get("CreateDate", "null")
                        }
                        res_list.append(sync_dict)

                    if res_list:
                        self.bulk_Index_Data(index_name, type_name, res_list)


        except:
            runlog.error(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def change_is_sync_series(self, res_list):
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            if res_list:
                for res in res_list:
                    cursor_conn.execute(
                        "UPDATE series SET isSync = 1  WHERE SeriesId = '%s'" % (res.get("seriesId", "NULL")))
        except:
            runlog.error(traceback.format_exc())
            series_conn.rollback()
        finally:
            series_conn.commit()
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def change_is_sync_suggest(self, res_list):
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)
        cursor_conn = series_conn.cursor()
        try:
            if res_list:
                for res in res_list:
                    cursor_conn.execute("UPDATE suggest SET isSync = 1  WHERE id = '%s'" % (res.get("id", "NULL")))
        except:
            runlog.error(traceback.format_exc())
            series_conn.rollback()
        finally:
            series_conn.commit()
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def change_is_sync_cast(self, res_list):
        cast_conn = pymysql.connect(**db_config)
        cast_conn.autocommit(1)
        cast_conn.select_db(db_name)
        cursor_conn = cast_conn.cursor()
        try:
            if res_list:
                for res in res_list:
                    cursor_conn.execute("UPDATE cast SET isSync = 1  WHERE castId = '%s'" % (res.get("castId", "NULL")))
        except:
            runlog.error(traceback.format_exc())
            cast_conn.rollback()
        finally:
            cast_conn.commit()
            # 关闭游标连接
            cursor_conn.close()
            cast_conn.close()

    def Set_Mysql_Sync_Delete_Data(self, index_name, type_name):
        self.create_series_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)
        cursor_conn = series_conn.cursor()
        try:
            sync_updata_sql_count = '''
            SELECT
            count(*) as total
            FROM
            `series`
            WHERE
            `isDelete` = '1'
            AND 
            `isSync` = '0'
            '''
            cursor_conn.execute(sync_updata_sql_count)
            results = cursor_conn.fetchall()

            total = results[0].get("total")
            if int(total) > 0:
                while (True):
                    try:
                        sync_updata_sql = '''
                        SELECT
                        `ContentId`,
                        `SeriesId`,
                        `Name`,
                        `CreateDate`,
                        `Actors`,
                        `Directors`,
                        `Intro`,
                        `Keywords`,
                        `Tags`
                        FROM
                        `series`
                        WHERE
                        `isDelete` = '1'
                        AND 
                        `isSync` = '0'
                        ORDER BY `insertDate`
                        LIMIT
                        %s;
                        ''' % (Sync_es_num)
                        cursor_conn.execute(sync_updata_sql)
                        results = cursor_conn.fetchall()
                        res_list = []
                        if not results:
                            break
                        for result in results:
                            sync_dict = {
                                "name": result.get("Name", "null"),
                                "contentId": result.get("ContentId", "null"),
                                "seriesId": result.get("SeriesId", "null"),
                                "actors": result.get("Actors", "null"),
                                "directors": result.get("Directors", "null"),
                                "namekeyword": result.get("Keywords", "null"),
                                "tags": result.get("Tags", "null"),
                            }
                            res_list.append(sync_dict)

                        if res_list:
                            for res in res_list:
                                try:
                                    self.Delete_Index_Data(index_name, type_name, res.get("seriesId"))
                                except:
                                    runlog.warn(traceback.format_exc())
                                    pass
                                cursor_conn.execute(
                                    "UPDATE series SET isSync = 1  WHERE SeriesId = '%s'"
                                    % (res.get("seriesId", "NULL")))
                                series_conn.commit()
                    except:
                        break

        except:
            runlog.error(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def Set_Mysql_Sync_Update_Suggest(self, index_name, type_name):
        """创建索引和type 以及喂索引设定settings和mappings"""
        self.create_suggest_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            sync_updata_sql_count = '''
            SELECT
            count(*) as total
            FROM
            `suggest`
            WHERE
            `isDelete` = '0'
            AND 
            `isSync` = '0'
            '''
            cursor_conn.execute(sync_updata_sql_count)
            results = cursor_conn.fetchall()

            total = results[0].get("total")
            if int(total) > 0:
                while (True):
                    try:
                        sync_updata_sql = '''
                        SELECT
                        `id`,
                        `name`,
                        `describe`,
                        `keywords`
                        FROM
                        `suggest`
                        WHERE
                        `isDelete` = '0'
                        AND 
                        `isSync` = '0'
                        ORDER BY `insertDate`
                        LIMIT
                        %s;
                        ''' % (Sync_es_num)

                        cursor_conn.execute(sync_updata_sql)
                        results = cursor_conn.fetchall()
                        res_list = []
                        if not results:
                            break
                        for result in results:
                            sync_dict = {
                                "id": result.get("id", "null"),
                                "name": result.get("name", "null"),
                                "describe": result.get("describe", "null"),
                                "keywords": result.get("keywords", "null"),
                            }
                            res_list.append(sync_dict)

                        if res_list:
                            self.bulk_Suggest_Data(index_name, type_name, res_list)
                    except:
                        break

        except:
            runlog.error(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def Set_Mysql_Sync_Delete_Suggest(self, index_name, type_name):
        self.create_suggest_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            sync_updata_sql_count = '''
            SELECT
            count(*) as total
            FROM
            `suggest`
            WHERE
            `isDelete` = '1'
            AND 
            `isSync` = '0'
            '''
            cursor_conn.execute(sync_updata_sql_count)
            results = cursor_conn.fetchall()

            total = results[0].get("total")
            if int(total) > 0:
                while (True):
                    try:
                        sync_updata_sql = '''
                        SELECT
                        `id`,
                        `name`,
                        `describe`
                        FROM
                        `suggest`
                        WHERE
                        `isDelete` = '1'
                        AND 
                        `isSync` = '0'
                        ORDER BY `insertDate`
                        LIMIT
                        %s;
                        ''' % (Sync_es_num)

                        cursor_conn.execute(sync_updata_sql)
                        results = cursor_conn.fetchall()
                        res_list = []
                        if not results:
                            break
                        for result in results:
                            sync_dict = {
                                "id": result.get("id", "null"),
                                "name": result.get("name", "null"),
                                "describe": result.get("describe", "null"),
                            }
                            res_list.append(sync_dict)

                        if res_list:
                            for res in res_list:
                                try:
                                    self.Delete_Index_Data(index_name, type_name, res.get("id"))
                                except:
                                    pass
                                cursor_conn.execute(
                                    "UPDATE suggest SET isSync = 1  WHERE id = '%s'"
                                    % (res.get("id", "NULL")))

                            series_conn.commit()
                    except:
                        break
        except:

            runlog.error(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def Set_Mysql_Repeat_Delete_Suggest(self, id, index_name, type_name):
        self.create_suggest_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            try:
                self.Delete_Index_Data(index_name, type_name, id)
            except:
                runlog.warn(traceback.format_exc())
                pass
            cursor_conn.execute("DELETE FROM suggest  WHERE id = '%s'" % (id))
        except:
            runlog.debug(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def Set_Mysql_Sync_Update_Cast(self, index_name, type_name):
        self.create_cast_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            sync_updata_sql_count = '''
            SELECT
            count(*) as total
            FROM
            `cast`
            WHERE
            `isDelete` = '0'
            AND 
            `isSync` = '0'
            '''
            cursor_conn.execute(sync_updata_sql_count)
            results = cursor_conn.fetchall()

            total = results[0].get("total")
            if int(total) > 0:
                while (True):
                    try:
                        sync_updata_sql = '''
                        SELECT
                        `id`,
                        `castId`,
                        `name`,
                        `role`,
                        `height`,
                        `weight`,
                        `eduction`,
                        `nation`,
                        `hometown`,
                        `gender`,
                        `birthday`,
                        `poster`,
                        `thumbnail`,
                        `constellation`,
                        `ename`,
                        `tags`,
                        `subname`,
                        `keyword`,
                        `description`,
                        `isDelete`,
                        `isSync`,
                        `updateTime`,
                        `createTime`
                        FROM
                        `cast`
                        WHERE
                        `isDelete` = '0'
                        AND 
                        `isSync` = '0'
                        ORDER BY `createTime`
                        LIMIT
                        %s;
                        ''' % (Sync_es_num)

                        cursor_conn.execute(sync_updata_sql)
                        results = cursor_conn.fetchall()
                        res_list = []
                        if not results:
                            break
                        for result in results:
                            sync_dict = {
                                "castId": result.get("castId", "null"),
                                "name": result.get("name", "null"),
                                "role": result.get("role", "null"),
                                "height": result.get("height", "null"),
                                "weight": result.get("weight", "null"),
                                "eduction": result.get("eduction", "null"),
                                "nation": result.get("nation", "null"),
                                "hometown": result.get("hometown", "null"),
                                "gender": result.get("gender", "null"),
                                "birthday": result.get("birthday", "null"),
                                "poster": result.get("poster", "null"),
                                "thumbnail": result.get("thumbnail", "null"),
                                "constellation": result.get("constellation", "null"),
                                "ename": result.get("ename", "null"),
                                "tags": result.get("tags", "null"),
                                "subname": result.get("subname", "null"),
                                "namekeyword": result.get("keyword", "null"),
                                "description": result.get("description", "null"),
                                "createTime": result.get("createTime", "null")
                            }
                            res_list.append(sync_dict)

                        if res_list:
                            self.bulk_Cast_Data(index_name, type_name, res_list)
                    except:
                        break

        except:
            runlog.error(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()

    def Set_Mysql_Sync_Delete_Cast(self, index_name, type_name):
        self.create_cast_index(index_name, type_name)
        series_conn = pymysql.connect(**db_config)
        series_conn.autocommit(1)
        series_conn.select_db(db_name)

        cursor_conn = series_conn.cursor()
        try:
            sync_updata_sql_count = '''
            SELECT
            count(*) as total
            FROM
            `cast`
            WHERE
            `isDelete` = '1'
            AND 
            `isSync` = '0'
            '''
            cursor_conn.execute(sync_updata_sql_count)
            results = cursor_conn.fetchall()

            total = results[0].get("total")
            if int(total) > 0:
                while (True):
                    try:
                        sync_updata_sql = '''
                        SELECT
                        `castId`
                        FROM
                        `cast`
                        WHERE
                        `isDelete` = '1'
                        AND 
                        `isSync` = '0'
                        ORDER BY `createTime`
                        LIMIT
                        %s;
                        ''' % (Sync_es_num)

                        cursor_conn.execute(sync_updata_sql)
                        results = cursor_conn.fetchall()
                        res_list = []
                        if not results:
                            break
                        for result in results:
                            sync_dict = {"castId": result.get("castId", "null")}
                            res_list.append(sync_dict)

                        if res_list:
                            for res in res_list:
                                try:
                                    self.Delete_Index_Data(index_name, type_name, res.get("castId"))
                                except:
                                    pass
                                cursor_conn.execute(
                                    "UPDATE cast SET isSync = 1  WHERE id = '%s'" % (res.get("castId", "NULL")))
                            series_conn.commit()
                    except:
                        break
        except:
            runlog.error(traceback.format_exc())
        finally:
            # 关闭游标连接
            cursor_conn.close()
            series_conn.close()
