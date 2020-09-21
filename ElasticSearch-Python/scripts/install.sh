#!/bin/sh

Force=$1
BASE_PATH="/opt/hoob/NE/search_sync_data"
LOG_PATH=$BASE_PATH/log
TEMPLATES_PATH=$BASE_PATH/templates



##########INIT Directory##########
function intDirectory()
{	
	if [ ! -d "$1" ];then
		mkdir "$1" -p
		echo "directory $1 not exist,now create it"
	fi		
}

intDirectory $BASE_PATH
intDirectory $LOG_PATH
intDirectory $TEMPLATES_PATH

##########Copy file from install package to target directory##########

if [ "$Force" == "-f" ]; then
		yes|cp -rf ElasticSearch-Python/* $BASE_PATH
		echo "Copy etc file to $BASE_PATH"
else
    mv $BASE_PATH/etc/config.ini $BASE_PATH/etc/config.ini_bak
    yes|cp -rf ElasticSearch-Python/* $BASE_PATH
    mv $BASE_PATH/etc/config.ini_bak $BASE_PATH/etc/config.ini
fi


if [ "$Force" == "-first" ]; then
		
		yes|cp -rf ElasticSearch-Python/* $BASE_PATH
		echo "Copy etc file to $BASE_PATH"	
		#chmod +x $BIN_PATH/*.sh
else
    mv $BASE_PATH/etc/config.ini $BASE_PATH/etc/config.ini_bak
    yes|cp -rf ElasticSearch-Python/* $BASE_PATH
    mv $BASE_PATH/etc/config.ini_bak $BASE_PATH/etc/config.ini
fi

chmod 755 ElasticSearch-Python/*/*

cp ElasticSearch-Python/scripts/commend.sh /etc/init.d/
mv /etc/init.d/commend.sh /etc/init.d/ElasticSearch-Python
chmod 755 $BASE_PATH/*
#chmod 755 $TEMPLATES_PATH/*
echo "current ElasticSearch-Python install in $BASE_PATH"
echo "install success..."
exit 0
