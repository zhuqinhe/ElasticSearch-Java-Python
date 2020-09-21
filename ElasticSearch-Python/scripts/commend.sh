#!/usr/bin/env bash
VERSION=/opt/hoob/NE/ElasticSearch-Python/version
start(){
process=`ps -ef |grep "ElasticSearch-Python" |grep -v "grep" |wc -l`
if [ ${process} -ne 1 ];then        
        cd /opt/hoob/NE/ElasticSearch-Python/
        #rm -rf temp/*
        ./ElasticSearch-Python
        echo "...wait "
	sleep 1;
        process=`ps -ef |grep "ElasticSearch-Python" |grep -v "grep" |wc -l`
        if [ ${process} -lt 1 ];then
                echo -e "\033[31;49;1m [-----start  Failed Version:$(head -n2 ${VERSION} | tail -n1 | cut -d'=' -f2)-----] \033[39;49;0m"
        else
                echo -e "\033[32;49;1m [-----start  Success Version:$(head -n2 ${VERSION} | tail -n1 | cut -d'=' -f2)-----] \033[39;49;0m"
        fi
else
    echo -e "\033[32;49;1m [-----ProductVersion:$(head -n2 ${VERSION} | tail -n1 | cut -d'=' -f2)-----] \033[39;49;0m"
    echo -e "\033[32;49;1m [-----start  Success -----] \033[39;49;0m"
fi
exit 0
}

stop(){
killall ElasticSearch-Python
sleep 1;
process=`ps -ef |grep "ElasticSearch-Python" |grep -v "grep" |wc -l`
if [ ${process} -ne 0 ];then
    echo -e "\033[31;49;1m [-----stop  Failed Version:$(head -n2 ${VERSION} | tail -n1 | cut -d'=' -f2)-----] \033[39;49;0m"
else
    echo -e "\033[32;49;1m [-----stop  Success Version:$(head -n2 ${VERSION} | tail -n1 | cut -d'=' -f2)-----] \033[39;49;0m"
fi
exit 0

}

status(){
echo -e "\033[32;49;1m [-----ProductName:ElasticSearch-Python -----] \033[39;49;0m"
echo -e "\033[32;49;1m [-----ProductVersion:$(head -n2 ${VERSION} | tail -n1 | cut -d'=' -f2)-----] \033[39;49;0m"

process=`ps -ef |grep "ElasticSearch-Python" |grep -v "grep" |wc -l`
if [ ${process} -eq 1 ];then
    echo -e "\033[32;49;1m [----- status:started-----] \033[39;49;0m"
else
    echo -e "\033[31;49;1m [----- status:stopped-----] \033[39;49;0m"
fi
exit 0

}
restart(){
ElasticSearch-Python_processes=`ps -ef | grep ElasticSearch-Python | grep -v grep | awk '{print $2}'`
if [ ! -z "${ElasticSearch-Python_processes}" ]; then
        for PID in "$ElasticSearch-Python_processes";
        do
                if [ ! -z "$PID" ]; then
                        kill $PID
                fi
        done
fi

start
}
case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  status)
        status
        ;;
  restart)
        restart
        RETVAL=$?
        ;;
*)
        echo $"Usage: $0 {start|stop|status|restart}"
        exit 1
esac

exit $RETVAL

