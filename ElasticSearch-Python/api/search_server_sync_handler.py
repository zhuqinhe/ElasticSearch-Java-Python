# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40

from scarecrow.globals import elasticsearch_search_index_name, elasticsearch_search_type, \
    elaticsearch_sync_time_interval, time_interval, \
    cast_index_name, cast_index_type
from service.analyse_suggest_hot_words import *
from service.hot_words_server import *
from service.sync_elasticsearch_server import *


def sync_data():
    sync_es = threading.Thread(target=sync_es_search_timer)
    sync_es.start()

    sync_es_suggest = threading.Thread(target=elaticsearch_sync_suggest_timer)
    sync_es_suggest.start()

    sync_es_cast = threading.Thread(target=elaticsearch_sync_cast_timer)
    sync_es_cast.start()

    analyse_search_hot_words = threading.Thread(target=analyse_hot_words_timer)
    analyse_search_hot_words.start()


def sync_es_search_timer():
    try:
        if elasticsearch_search_index_name:
            for index_name in elasticsearch_search_index_name:
                runlog.info(u'开始同步 series updated to es ...')
                ElasticObj().Set_Mysql_Sync_Update_Data(index_name, elasticsearch_search_type)
                runlog.info(u'同步结束 series updated to es ...')

                runlog.info(u'开始同步 series deleted to es ...')
                ElasticObj().Set_Mysql_Sync_Delete_Data(index_name, elasticsearch_search_type)
                runlog.info(u'同步结束 series deleted to es ...')
    except:
        runlog.error(traceback.format_exc())

    global elaticsearch_sync
    elaticsearch_sync = threading.Timer(float(elaticsearch_sync_time_interval), sync_es_search_timer)
    elaticsearch_sync.start()


def elaticsearch_sync_suggest_timer():
    try:
        if suggest_index_name:
            for index_name in suggest_index_name:
                runlog.info(u'开始同步 updated suggest to es ...')
                ElasticObj().Set_Mysql_Sync_Update_Suggest(index_name, suggest_index_type)
                runlog.info(u'结束同步 updated suggest to es ...')

                runlog.info(u'开始同步 deleted suggest to es ...')
                ElasticObj().Set_Mysql_Sync_Delete_Suggest(index_name, suggest_index_type)
                runlog.info(u'结束同步 deleted suggest to es ...')
    except:
        runlog.error(traceback.format_exc())

    global elaticsearch_sync_suggest
    elaticsearch_sync_suggest = threading.Timer(float(elaticsearch_sync_time_interval), elaticsearch_sync_suggest_timer)
    elaticsearch_sync_suggest.start()


def elaticsearch_sync_cast_timer():
    try:
        if cast_index_name:
            for index_name in cast_index_name:
                runlog.info(u'开始同步 updated cast to es ...')
                ElasticObj().Set_Mysql_Sync_Update_Cast(index_name, cast_index_type)
                runlog.info(u'结束同步 updated cast to es ...')

                runlog.info(u'开始同步 deleted cast to es ...')
                ElasticObj().Set_Mysql_Sync_Delete_Cast(index_name, cast_index_type)
                runlog.info(u'结束同步 deleted cast to es ...')
    except:
        runlog.error(traceback.format_exc())

    global elaticsearch_sync_cast
    elaticsearch_sync_cast = threading.Timer(float(elaticsearch_sync_time_interval), elaticsearch_sync_cast_timer)
    elaticsearch_sync_cast.start()


def analyse_hot_words_timer():
    try:
        runlog.info(u'开始聚合搜索排行榜 ...')
        analyse_hot_words()
        runlog.info(u'结束聚合搜索排行榜 ...')
    except:
        runlog.error(traceback.format_exc())

    global analyse_hot_words_threard
    analyse_hot_words_threard = threading.Timer(float(time_interval), analyse_hot_words_timer)
    analyse_hot_words_threard.start()
