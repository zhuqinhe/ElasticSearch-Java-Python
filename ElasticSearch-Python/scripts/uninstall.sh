#!/bin/bash

APP_NAME="search_Sync_Data"
APP_HOME="/opt/hoob/NE/search_sync_data"
TAR_DIR="/ElasticSearch-Python-*-GEN"

ps_out=`ps -ef | grep -v 'grep'`
result=$(echo $ps_out | grep $APP_NAME)
if [[ "$result" != "" ]];then
    echo "$APP_NAME is running and will be shutting down. If you want to uninstall, please execute this shell again"
    killall search_Sync_Data
else
    rm -rf $APP_HOME
    rm -rf /etc/init.d/ElasticSearch-Python
    rm -rf `pwd`/..$TAR_DIR
    echo "uninstall $APP_NAME -----------OK"
    exit 0
fi