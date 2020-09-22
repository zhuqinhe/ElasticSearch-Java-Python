package com.hoob.search.utils;

/**
 * 消息碼
 * 
 * @see CodeNum
 * @since
 */
public class CodeNum{
    /**
     * 成功
     */
    public static final String IS_SUCCESS = "0";

    /**
     * 网络异常
     */
    public static final String NETWORK_ANOMALIES = "-1";

    /**
     * 参数异常
     */
    public static final String PARAM_ANOMALIES = "1";

    /**
     * 系统内部错误（未知错误）
     */
    public static final String SYSTEM_ERROR = "500";
    
    /**
     * 系统过载保护
     */
    public static final Integer OVERLOAD_PROTECTION_NUB = 20000;

    /**
     * 系统过载保护
     */
    public static final String OVERLOAD_PROTECTION = "20000";

    /**
     * 数据库错误
     */
    public static final String DATABASE_ERROR = "1000";

    /**
     * 用户名已存在
     */
    public static final String USER_ALREADY_EXISTS = "aaa100000";

    /**
     * 用户注册成功，终端绑定失败
     */
    public static final String RS_TF = "aaa100001";

    /**
     * 鉴权失败，返回批价信息
     */
    public static final String AF_RP_INFO = "aaa100002";

    /**
     * 用户不存在
     */
    public static final String USER_NOT_EXIST = "aaa100003";

    /**
     * 用户已禁用
     */
    public static final String USER_DISABLED = "aaa100004";

    /**
     * 订购产品不存在
     */
    public static final String ORDER_PRODUCTS_NOT_EXIST = "aaa100005";

    /**
     * 订购产品已禁用
     */
    public static final String ORDER_PRODUCTS_DISABLED = "aaa100006";

    /**
     * 授权信息不存在
     */
    public static final String AUTO_INFO_NOT_EXIST = "aaa100007";

    /**
     * 支付状态传值错误
     */
    public static final String PAY_STATUS_ERROR = "aaa100008";

    /**
     * 非法请求，userToken校验失败
     */
    public static final String ILL_REQUEST_USERTOKEN_FAIL = "aaa100009";

    /**
     * 激活失败，SN校验失败
     */
    public static final String ACTIVATION_FAILE_SN_FAIL = "aaa100010";

    /**
     * 激活失败，MAC地址不匹配
     */
    public static final String ACTIVATION_FAILE_MAC_FAIL = "aaa100011";

    /**
     * 认证失败，用户名或密码错误
     */
    public static final String AUTO_FAIL_NAME_PD = "aaa100012";

    /**
     * 兑换码不正确
     */
    public static final String CONVERSION_NOT_CORRECT = "aaa100013";

    /**
     * 用户注册请求参数异常
     */
    public static final String USER_REGISTER_PARAM_ERROR = "aaa100014";

    /**
     * 用户激活中终端参数校验异常
     */
    public static final String TERMINAL_CHECK_ERROR = "aaa100015";

    /**
     * 用户激活请求参数异常
     */
    public static final String USER_ACTIVE_PARAM_ERROR = "aaa100016";

    /**
     * 用户所在区域为黑名单，激活失败
     */
    public static final String BLACK_AREA_FAIL = "aaa100017";

    /**
     * 用户所在区域不在白名单中，激活失败
     */
    public static final String WHITE_AREA_FAIL = "aaa100018";

    /**
     * 终端为禁用状态，激活失败
     */
    public static final String CLOST_STATUS_TERMINAL = "aaa100019";

    /**
     * 用户认证请求参数异常
     */
    public static final String USER_LOGIN_PARAM_ERROR = "aaa100020";

    /**
     * 用户认证已达上限-3
     */
    public static final String USER_LOGIN_LIMIT_THREE = "aaa100021";

    /**
     * 用户名密码校验失败
     */
    public static final String USER_NAME_PW_ERROR = "aaa100022";

    /**
     * 用户信息查询失败，token与用户名校验失败
     */
    public static final String QUERY_TOKEN_USERNAME_ERROR = "aaa100023";

    /**
     * 用户信息修改失败，token与用户名校验失败
     */
    public static final String MODIFY_TOKEN_USERNAME_ERROR = "aaa100024";

    /**
     * 用户信息修改失败，原密码错误
     */
    public static final String MODIFY_PW_CHECK_ERROR = "aaa100025";

    /**
     * token没有找到
     */
    public static final String TOKEN_NOT_FOUND = "aaa100026";

    /**
     * 心跳检测，登录失效
     */
    public static final String HEART_BEAT_LOGIN_INVALID = "aaa100027";

    /**
     * 心跳检测，未登录（没有找到登录信息）
     */
    public static final String HEART_BEAT_LOGIN_NOT_EXIST = "aaa100028";

    /**
     * 心跳检测，MAC地址不匹配
     */
    public static final String HEART_BEAT_MAC_FAIL = "aaa100029";

    /**
     * 心跳检测，用户名不匹配
     */
    public static final String HEART_BEAT_USER_NAME_FAIL = "aaa100030";

    /**
     * email格式不正确
     */
    public static final String EMAIL_FORMAT_FAIL = "aaa100031";

    /**
     * token与用户名校验失败
     */
    public static final String USERNAME_TOKEN_ERROR = "aaa100032";

    /**
     * 订购套餐不存在
     */
    public static final String ORDER_PACKAGE_NOT_EXIST = "aaa100033";

    /**
     * 订购套餐已禁用
     */
    public static final String ORDER_PACKAGE_DISABLED = "aaa100034";

    /**
     * 更新订购支付状态错误
     */
    public static final String ORDER_UPDATE_PLAY_STATUS_ERROR = "aaa100035";

    /**
     * 订购信息不存在
     */
    public static final String ORDER_INFO_NOT_EXIST = "aaa100036";

    /**
     * 产品码或套餐码，必须有一个存在
     */
    public static final String PACKAGE_OR_PRODUCT_MUST_EXIST = "aaa100037";

    /**
     * 首次激活策略不存在
     */
    public static final String FIRST_ACTIVE_AUTH_NOT_EXIST = "aaa100038";

    /**
     * 生成授权信息失败
     */
    public static final String CREATE_AUTH_INFO_FAIL = "aaa100039";

    /**
     * 终端数据不存在
     */
    public static final String TERMINAL_DATA_NOT_EXIST = "aaa100040";

    /**
     * 用户信息插入数据库失败
     */
    public static final String USER_INSERT_DATA_FAIL = "aaa100041";

    /**
     * 更新终端信息失败
     */
    public static final String UPDATE_TERMINAL_INFO_FAIL = "aaa100042";

    /**
     * 数据库找不到user对象
     */
    public static final String USER_DATA_NOT_FOUNT = "aaa100043";

    /**
     * 更新用户信息失败
     */
    public static final String UPDATE_USER_INFO_FAIL = "aaa100044";

    /**
     * 更新token信息失败
     */
    public static final String UPDATE_TOKEN_INFO_FAIL = "aaa100045";

    /**
     * token对象插入数据库失败
     */
    public static final String TOEKN_INSERT_DATA_FAIL = "aaa100046";

    /**
     * macAddr参数没有值
     */
    public static final String MAC_PARAM_NOT_VAL = "aaa100047";

    /**
     * 授权信息不存在
     */
    public static final String AUTH_INFO_NOT_EXIST = "aaa100048";

    /**
     * 兑换内容为空
     */
    public static final String EXCHANGE_CONTENT_INFO_IS_EMPTY = "aaa100049";

    /**
     * 授权策略为禁用状态
     */
    public static final String AUTH_POLICY_DISABLE = "aaa100050";

    /**
     * 报文解析异常
     */
    public static final String EXCHANGE_BODY_ERROR = "aaa100051";

    /**
     * 连接异常
     */
    public static final String EXCHANGE_CONNECT_ERROR = "aaa100052";

    /**
     * 订单已支付，请勿重复操作
     */
    public static final String ORDER_PAID_NOT_REPEAT = "aaa100053";

    /**
     * 订购的产品是剧集或节目，必须指定内容ID
     */
    public static final String ORDER_PRODUCT_CONTENT_ID_NOT_NOTHING = "aaa100054";

    /**
     * 终端不是首次激活、但并没有绑定用户
     */
    public static final String TERMINAL_NOT_FIRST_ACTIVE = "aaa100055";

    /**
     * 核对订单失败
     */
    public static final String CHECK_ORDER_FAIL = "aaa100056";

    /**
     * 兑换太频繁，限制访问
     */
    public static final String EXCHANGE_LIMIT = "diamond100009";

    /**
     * aaa100057 增加授权天数超过最大限制天数
     */
    public static final String MMS_AUTH_DAYS_LIMIT = "aaa100057";

    /**
     * aaa100058 授权太频繁，限制访问
     */
    public static final String MMS_AUTH_LIMIT = "aaa100058";

}
