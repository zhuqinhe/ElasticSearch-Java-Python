package com.hoob.search.common;

import java.io.File;

public class Constants {
	public static final String PRO_FILE_PATH = System.getProperty("user.dir");


	/**
     * 成功 ResultCode
     */
    public static final Integer SUCCESS_CODE = 0;
    /**
     * 成功ResultMsg
     */
    public static final String SUCCESS_MSG = "业务处理成功";
 
    /**
     * 失败 ResultCode
     */
    public static final Integer FAILURE_CODE = -1;
    /**
     * 失败ResultMsg
     */
    public static final String FAILURE_MSG = "业务处理失败";
    
    /**
     * 服务不可用ResultCode
     */
    public static final Integer SERVER_CURRENTLY_UNAVAILABLE_CODE = 2000;
    /**
     * 服务不可用ResultMsg
     */
    public static final String SERVER_CURRENTLY_UNAVAILABLE_MSG = "Service Currently Unavailable";
    /**
     * 系统参数错误ResultCode
     */
    public static final Integer MISSING_REQUIRED_ARGUMENTS_CODE = 40001;
    /**
     * 系统参数错误ResultMsg
     */
    public static final String MISSING_REQUIRED_ARGUMENTS_MSG = "Missing Required Arguments";
    /**
     * 参数不合法ResultCode
     */
    public static final Integer INVALID_ARGUMENTS_CODE = 40002;
    /**
     * 参数不合法ResultMsg
     */
    public static final String INVALID_ARGUMENTS_MSG = "Invalid Arguments";
    
    /**
     * 无认证字符串(HMAC)ResultCode
     */
    public static final Integer INVALID_HMAC_SUB_CODE = 2205;
    /**
     * 无认证字符串(HMAC)ResultMsg
     */
    public static final String INVALID_HMAC_SUB_MSG = "无认证字符串(HMAC)";
    
    /**
     * 请求参数错误ResultCode
     */
    public static final Integer INVALID_ARGUMENTS_SUB_CODE = 5002;
    /**
     * 请求参数错误ResultMsg
     */
    public static final String INVALID_ARGUMENTS_SUB_MSG = "请求参数错误";
    
    /**
     * 无认证字符串(HMAC)ResultCode
     */
    public static final Integer INVALID_DATA_SUB_CODE = 5003;
    /**
     * 无认证字符串(HMAC)ResultMsg
     */
    public static final String INVALID_DATA_SUB_MSG = "无效数据";
    /**
     * 参数不能为空 ResultCode
     */
    public static final Integer PARAMA_IS_NULL_CODE = 2;
    /**
     * 参数不能为空 ResultMsg
     */
    public static final String PARAMA_IS_NULL_MSG = "参数不能为空";
    /**
     * 安卓终端Code
     */
    public static final String ANDROID_CODE = "1";
    /**
     * IOS终端Code
     */
    public static final String IOS_CODE = "2";
    
    
    /**
     * property文件路径
     */
    public static final String LIUNX_FILE_NAME = "/opt/hoob/NE/search-server/etc/search.properties";
    

    public static final String LIUNX_REDIS_FILE_NAME = "/opt/hoob/NE/search-server/etc/redis.properties";
    

    public static final String LIUNX_SYSTEM_FILE_NAME = "/opt/hoob/NE/search-server/etc/system.properties";
    

    
    
	public static final String LIUNX_ETC_PATH = "/opt/hoob/NE/search-server/etc/";
	

    /**
     * 
     */
    public static final String LOG4J2_MASTER = "/opt/hoob/NE/search-server/etc/log4j2.xml";


}
