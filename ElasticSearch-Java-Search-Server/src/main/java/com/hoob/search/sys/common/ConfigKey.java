package com.hoob.search.sys.common;


public enum ConfigKey {
	DEFAULT_LANGUAGE,//默认语言配置

	TOMCATTEMURL,//tomcat 的零时根目录
	ACCESSLOGPATTERN,//访问日志格式配置
	ACCESSLOGENABLED,//是否开启访问日志
	TOMCATCONNECTIONS,//tomcat最大连接数配置
	TOMCATMAXTHREADS,//tomcat最大线程数配置
	TOMCATMCONNECTIONTIMEOUT,//tomcat最大线程数配置
	TOMCATMINSPARETHREADS,//最小线程数
	TOMCATACCEPTORTHREADCOUNT,//可用的  参考值 CPU 8个以下逻辑核心  acceptorThreadCount =2   ，CPU 8个以上逻辑核心  acceptorThreadCount = 4
	TOMCATACCEPTCOUNT,//
	MAXKEEPALIVEREQUESTS,//
	KEEPALIVETIMEOUT,//

}
