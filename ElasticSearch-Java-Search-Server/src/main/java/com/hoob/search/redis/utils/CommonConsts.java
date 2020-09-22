// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space
// Source File Name: CommonConsts.java

package com.hoob.search.redis.utils;


public final class CommonConsts {

	/**
	 * 有效授权信息String集合 redis key 前缀
	 */
	public static final String REDIS_AUTH_VALID_LIST = "Redis_Auth_List";

	/**
	 * 授权信息Obj redis key前缀
	 */
	public static final String REDIS_AUTH_OBJ = "Redis_Auth_Obj";

	/**
	 * Token list的前缀
	 */
	public static final String REDIS_TOKEN_LIST = "Redis_Token_List";

	/**
	 * 配置参数redis key前缀
	 */
	public static final String REDIS_PAEAM_CONFIG = "Redis_Param_Config";

	/**
	 * ott账户redis key前缀
	 */
	public static final String REDIS_USER_OTT = "Redis_User_OTT";

	/**
	 * 终端redis key前缀
	 */
	public static final String REDIS_TERMINAL = "Redis_Terminal";

	/**
	 * 授权信息 auth_code redis key 前缀
	 */
	public static final String REDIS_AUTH_CODE = "Redis_Auth_Code";

	/**
	 * 终端用户 redis key前缀
	 */
	public static final String REDIS_TERMINAL_USER = "Redis_Terminal_User";

	/**
	 * 终端redis key Str前缀
	 */
	public static final String REDIS_SN_MAC = "Redis_Sn_Mac";

	/**
	 * 令牌redis key前缀
	 */
	public static final String REDIS_USER_TOKEN = "Redis_User_Token";

	/**
	 * 产品redis key前缀-code
	 */
	public static final String REDIS_PRODUCT_CODE = "Redis_Product_Code";

	/**
	 * 产品redis key前缀-id
	 */
	public static final String REDIS_PRODUCT_ID = "Redis_Product_Id";

	/**
	 * 套餐redis key前缀-code
	 */
	public static final String REDIS_PACKAGE_CODE = "Redis_Package_Code";

	/**
	 * 套餐redis key前缀-id
	 */
	public static final String REDIS_PACKAGE_ID = "Redis_Package_Id";

	/**
	 * 套餐-产品关联redis key前缀
	 */
	public static final String REDIS_LINK_PACKAGE_PRODUCT = "Redis_Link_Package_Product";

	/**
	 * 套餐-产品关联redisPush key前缀
	 */
	public static final String REDIS_PUSH_LINK_PACKAGE_PRODUCT = "Redis_Push_Link_Package_Product";

	/**
	 * 订购信息redis key前缀
	 */
	public static final String REDIS_ORDER_INFO = "Redis_Order_Info";

	/**
	 * 订购信息redis name前缀
	 */
	public static final String REDIS_LIST_ORDER_INFO = "Redis_Name_Order_Info";

	/**
	 * 授权信息redis key前缀
	 */
	public static final String REDIS_AUTH_INFO = "Redis_Auth_Info";

	/**
	 * 授权信息redis key前缀，用户名+产品码
	 */
	public static final String REDIS_AUTH_UNAME_PCODE = "Redis_Auth_UserName_ProductCode";

	/**
	 * 数字 -1
	 */
	public static final int NUMBER_NEGATIVE_ONE = -1;

	public static final String NUMBER_NEGATIVE_ONE_STR = "-1";

	/**
	 * 数字0
	 */
	public static final int NUMBER_ZERO = 0;

	public static final String NUMBER_ZERO_STR = "0";

	/**
	 * 数字1
	 */
	public static final int NUMBER_ONE = 1;

	public static final String NUMBER_ONE_STR = "1";

	/**
	 * 数字2
	 */
	public static final int NUMBER_TWO = 2;

	public static final String NUMBER_TWO_STR = "2";

	/**
	 * 数字3
	 */
	public static final int NUMBER_THREE = 3;

	public static final String NUMBER_THREE_STR = "3";

	/**
	 * 数字4
	 */
	public static final int NUMBER_FOUR = 4;

	public static final String NUMBER_FOUR_STR = "4";

	/**
	 * 数字5
	 */
	public static final int NUMBER_FIVE = 5;

	public static final String NUMBER_FIVE_STR = "5";

	/**
	 * 数字6
	 */
	public static final int NUMBER_SIX = 6;

	public static final String NUMBER_SIX_STR = "6";

	/**
	 * 数字7
	 */
	public static final int NUMBER_SEVEN = 7;

	public static final String NUMBER_SEVEN_STR = "7";

	/**
	 * 数字8
	 */
	public static final int NUMBER_EIGTH = 8;

	public static final String NUMBER_EIGTH_STR = "8";

	/**
	 * 数字9
	 */
	public static final int NUMBER_NINE = 9;

	public static final String NUMBER_NINE_STR = "9";

	/**
	 * 数字10
	 */
	public static final int NUMBER_TEN = 10;

	public static final String NUMBER_TEM_STR = "10";

	/**
	 * 常量unknown
	 */
	public static final String UNKNOWN = "unknown";

	/**
	 * BIZ类型的日志
	 */
	public static final String BIZ = "BIZ";

	/**
	 * 数字5000
	 */
	public static final int NUMBER_FIVE_Q = 5000;

	/**
	 * 数字1分钟
	 */
	public static final int NUMBER_ONE_MINUTE = 1000 * 60;

	/**
	 * 逗号“,”字符串
	 */
	public static final String COMMA = ",";

	/**
	 * 下划线
	 */
	public static final String DOWN_LINE = "_";

	/**
	 * 数字1小时60分钟
	 */
	public static final int ONE_H_MINUTE = 60;

	/**
	 * 加密的KEY
	 */
	public static final String KEY = "%sowell_%";

	/**
	 * 字符串，AAA
	 */
	public static final String AAA = "2";

	/**
	 * 字符串 md5_key
	 */
	public static final String MD5_KEY = "md5_key";

	/**
	 * 字符串system
	 */
	public static final String STRING_SYSTEM = "system";

	/**
	 * 1分钟
	 */
	public static final int ONE_MINUTE = 1 * 60 * 1000;

	/**
	 * 终端自产类型 字符串
	 */
	public static final String TERMINAL_PRODUCTED = "1";

	/**
	 * 终端代产类型字符串
	 */
	public static final String TERMINAL_GENERATE_PRODUCTED = "2";

	/**
	 * 终端被禁用状态字符串
	 */
	public static final String TERMINAL_IS_CLOSE = "3";

	/**
	 * 终端已激活状态字符串
	 */
	public static final String TERMINAL_HAS_ACTIVE = "2";

	/**
	 * 产品ObjectType 字符串
	 */
	public static final String PRODUCT_TYPE = "0";

	/**
	 * 套餐ObjectType 字符串
	 */
	public static final String PACKAGE_TYPE = "1";

	/**
	 * 消息是成功 CodeNum 字符串
	 */
	public static final String CODE_NUM_IS_SUCCESS = "0";

	/**
	 * "heartBeat_OutTimeMinute"字符串
	 */
	public static final String HEART_BEAT_OUTTIME_MINUTE = "heartBeat_OutTimeMinute";

	/**
	 * 用户状态为启用状态
	 */
	public static final String USER_STATUS_START = "1";

	/**
	 * 终端可以跨区
	 */
	public static final Character IS_CROSS_ALLOW = '1';

	/**
	 * AuthCenter网元licenseclient.conf配置文件
	 */
	public static final String LICENSE_CONF = "license_conf";

	/**
	 * tempUserName 字符串
	 */
	public static final String TEMP_USER_NAME_STR = "tempUserName";

	/**
	 * 获取兑换码接口地址
	 */
	public static final String DIAMOND_GET_EXCHANGE_INFO_URL = "diamond_getExchangeInfo_Url";

	/**
	 * 通知兑换结果接口地址
	 */
	public static final String DIAMOND_EXCHANGE_URL = "diamond_exchange_Url";

	/**
	 * 兑换保护处理
	 */
	public static final String DIAMOND_EXCHANGE_AEGIS = "diamond_exchange_ageis";

	/**
	 * 其他地区
	 */
	public static final String OTHER_PART_STR = "其他地区";

	/**
	 * KC 字符串
	 */
	public static final String KOOKAN_STR = "KC";

	/**
	 * utf-8
	 */
	public static final String UTF8 = "UTF-8";

	// the log format to kafka
	public static final String LOG_TO_KAFKA_STYLE_STR = "\"ip\":\"{}\",\"executeTime\":\"{}\","
			+ "\"data\":{\"request\":{},\"response\":{}}}";

	// enter space
	public static final String COMMENT_ENTER_STR = "\n";

	// empty string
	public static final String EMPTY_STR = "";

	/**
	 * ott 约定的密码
	 */
	public static final String OTT_AGREED_PD = "ott_agreed_password";

	/**
	 * ott 约定的key
	 */
	public static final String OTT_AGREED_KEY = "ott_agreed_key";

	/**
	 * 会员约定的token
	 */
	public static final String MEMBER_AGREED_TOKEN = "member_agreed_token";

	/**
	 * 会员系统约定vip3的套餐
	 */
	public static final String MEMBER_AGREED_VIP_THREE = "vipthree";

	/**
	 * 会员系统约定1个月会员套餐的code
	 */
	public static final String MEMBER_AGEREED_VIP_ONE_CODE = "vipone";

	/**
	 * 兑换码兑换
	 */
	public static final String EXCHANGE_NAME = "激活码订单";

	/**
	 * 用户增加授权接口限制最大天数
	 */
	public static final String MMS_MAX_AUTH_DAYS = "mms_max_auth_days";

	/**
	 * 用户增加授权接口保护
	 */
	public static final String MMS_AUTH_SUCESS_LIMIT = "mms_auth_sucess_limit";

	/**
	 * 清理(userToken)数据是否打开
	 */
	public static final String CLEAN_TIME_IS_OPEN = "clean_time_is_open";

	/**
	 * 数字10000
	 */
	public static final int NUMBER_TEN_THOUSAND = 10000;

	/**
	 * 系统内部错误（未知错误）500 错误消息
	 */
	public static final String SYSTEM_ERROR_STRING = "{\"returnCode\":\"500\",\"errorMessage\":\"系统内部错误（未知错误）\"}";
}
