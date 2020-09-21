# -*- coding: utf-8 -*-
# !/usr/bin/env python
# author:zhuqinhe
# datetime:2020/09/21 0025 17:40 下午5:40

import os
import threading

import pymysql

from utils import config
from utils.logsuite.logsuite import runlogapi, Logger

cfg = config.Config()
path = os.path.abspath(os.path.dirname(os.path.dirname(__file__)))
cfg.set_config("./etc/config.ini")
print("load:", path + "/utils/config.ini")
# 日志配置，初始化日志
# log_config_path = os.path.abspath(os.path.dirname(os.path.dirname(__file__)))+"/"+cfg.get("LOGSUITE", "log_conf_path")
# 快照文件配置
templates_path = cfg.get("TEMPLATES", "templates_path")
if not templates_path.endswith('/'):
    templates_path = "".join([templates_path, "/"])
if not os.path.exists(templates_path):
    os.makedirs(templates_path)

log_config_path = cfg.get("LOGSUITE", "log_conf_path")
print("log_config_path:", log_config_path)
log_mode = cfg.get("LOGSUITE", "log_mode")
# 日志组件 1：limit size ; 2：day
if log_mode == "1":
    runlog = runlogapi()
    runlog.loginit(log_config_path, 30)
else:
    runlog = Logger(log_config_path)

# data-sync接口
api_url = cfg.get("SYNC-DATA", "api_url")

# 时间间隔
time_interval = cfg.get("TIME_INTERVAL", "time_interval")

# 上传上报数据
old_delete_switch = cfg.get("REPORT_SEARCH", "old_delete_switch")
old_delete_day = cfg.get("REPORT_SEARCH", "old_delete_day")
old_delete_day = int(old_delete_day)

# 最早修改时间
all_update_start_time = cfg.get("UPDATE_START_TIME", "all_update_start_time")

all_update_switch = cfg.get("UPDATE_START_TIME", "all_update_switch")

increment_update_start_time = cfg.get("UPDATE_START_TIME", "increment_update_start_time")

increment_update_time_interval = cfg.get("UPDATE_START_TIME", "increment_update_time_interval")

all_delete_start_time = cfg.get("DELETE_START_TIME", "all_delete_start_time")

all_delete_switch = cfg.get("DELETE_START_TIME", "all_delete_switch")

increment_delete_start_time = cfg.get("DELETE_START_TIME", "increment_delete_start_time")

increment_delete_time_interval = cfg.get("DELETE_START_TIME", "increment_delete_time_interval")

elaticsearch_ip = cfg.get("ELASTICSEARCH", "elaticsearch_ip")

elaticsearch_port = cfg.get("ELASTICSEARCH", "elaticsearch_port")

suggest_index_alias = cfg.get("SEARCH", "suggest_index_alias")

suggest_index_name = cfg.get("SEARCH", "suggest_index_name")[1:-1].split(",")

if suggest_index_name == ['']:
    suggest_index_name = None

suggest_index_type = cfg.get("SEARCH", "suggest_index_type")

cast_index_alias = cfg.get("SEARCH", "cast_index_alias")

cast_index_name = cfg.get("SEARCH", "cast_index_name")[1:-1].split(",")

if cast_index_name == ['']:
    cast_index_name = None

cast_index_type = cfg.get("SEARCH", "cast_index_type")

elaticsearch_sync_time_interval = cfg.get("ELASTICSEARCH", "elaticsearch_sync_time_interval")

elasticsearch_search_index_alias = cfg.get("SEARCH", "elasticsearch_search_index_alias")

elasticsearch_search_index_name = cfg.get("SEARCH", "elasticsearch_search_index_name")[1:-1].split(",")

if elasticsearch_search_index_name == ['']:
    elasticsearch_search_index_name = None

elasticsearch_search_type = cfg.get("SEARCH", "elasticsearch_search_type")
# 发送个数
Num = cfg.get("NUM", "num")
# 天数周期
Day_Num = cfg.get("NUM", "day")
# 同步ES个数
Sync_es_num = cfg.get("NUM", "sync_es_num")

if Sync_es_num:
    Sync_es_num = int(Sync_es_num)
else:
    Sync_es_num = 100
# 版本
VERSION = cfg.get("VERSION", "version")

# 打开数据库连接
db_config = {
    'host': cfg.get("MYSQL", "host"),
    'port': int(cfg.get("MYSQL", "port")),
    'user': cfg.get("MYSQL", "user"),
    'passwd': cfg.get("MYSQL", "passwd"),
    'charset': 'utf8mb4',
    'cursorclass': pymysql.cursors.DictCursor
}
db_name = cfg.get("MYSQL", "db_name")
LOCK = threading.Lock()
