package com.hoob.search.utils;

/**
 * 消息信息类
 * 
 * @see CodeMessage
 * @since
 */
public class CodeMessage{
    /**
     * 成功0
     */
    public static final String IS_SUCCESS = "成功";

    /**
     * 网络异常-1
     */
    public static final String NETWORK_ANOMALIES = "网络异常";

    /**
     * 参数异常1
     */
    public static final String PARAM_ANOMALIES = "请求参数异常！";

    /**
     * 系统内部错误（未知错误）500
     */
    public static final String SYSTEM_ERROR = "系统内部错误（未知错误）";

    /**
     * 系统过载保护800
     */
    public static final String OVERLOAD_PROTECTION = "系统繁忙(5000)-系统过载保护";

    /**
     * 数据库错误1000
     */
    public static final String DATABASE_ERROR = "数据库错误";

    /**
     * 用户名已存在aaa100000
     */
    public static final String USER_ALREADY_EXISTS = "用户名已存在";

    /**
     * 用户注册成功，终端绑定失败aaa100001
     */
    public static final String RS_TF = "用户注册成功，终端绑定失败";

    /**
     * 鉴权失败，返回批价信息aaa100002
     */
    public static final String AF_RP_INFO = "鉴权失败，返回批价信息";

    /**
     * 用户不存在aaa100003
     */
    public static final String USER_NOT_EXIST = "用户不存在";

    /**
     * 用户已禁用aaa100004
     */
    public static final String USER_DISABLED = "用户已禁用";

    /**
     * 订购产品不存在aaa100005
     */
    public static final String ORDER_PRODUCTS_NOT_EXIST = "订购产品不存在";

    /**
     * 订购产品已禁用或已过期aaa100006
     */
    public static final String ORDER_PRODUCTS_DISABLED = "订购产品已禁用或已过期";

    /**
     * 授权信息不存在aaa100007
     */
    public static final String AUTO_INFO_NOT_EXIST = "授权信息不存在";

    /**
     * 支付状态传值错误aaa100008
     */
    public static final String PAY_STATUS_ERROR = "支付状态传值错误";

    /**
     * 非法请求，userToken校验失败aaa100009
     */
    public static final String ILL_REQUEST_USERTOKEN_FAIL = "非法请求，userToken校验失败";

    /**
     * 激活失败，SN校验失败aaa100010
     */
    public static final String ACTIVATION_FAILE_SN_FAIL = "激活失败，SN校验失败";

    /**
     * 激活失败，MAC地址不匹配aaa100011
     */
    public static final String ACTIVATION_FAILE_MAC_FAIL = "激活失败，MAC地址不匹配";

    /**
     * 认证失败，用户名或密码错误aaa100012
     */
    public static final String AUTO_FAIL_NAME_PD = "认证失败，用户名或密码错误";

    /**
     * 兑换码不正确aaa100013
     */
    public static final String CONVERSION_NOT_CORRECT = "兑换码不正确";

    /**
     * 用户注册请求参数异常aaa100014
     */
    public static final String USER_REGISTER_PARAM_ERROR = "用户注册请求参数异常";

    /**
     * 用户激活中终端参数校验异常aaa100015
     */
    public static final String TERMINAL_CHECK_ERROR = "用户激活中终端参数校验异常";

    /**
     * 用户激活请求参数异常aaa100016
     */
    public static final String USER_ACTIVE_PARAM_ERROR = "用户激活请求参数异常";

    /**
     * 用户所在区域为黑名单，激活失败aaa100017
     */
    public static final String BLACK_AREA_FAIL = "用户所在区域为黑名单，激活失败";

    /**
     * 用户所在区域不在白名单中，激活失败aaa100018
     */
    public static final String WHITE_AREA_FAIL = "用户所在区域不在白名单，激活失败";

    /**
     * 终端为禁用状态，激活失败aaa100019
     */
    public static final String CLOST_STATUS_TERMINAL = "终端为禁用状态，激活失败";

    /**
     * 用户认证请求参数异常aaa100020
     */
    public static final String USER_LOGIN_PARAM_ERROR = "用户认证请求参数异常";

    /**
     * 用户认证已达上限-3aaa100021
     */
    public static final String USER_LOGIN_LIMIT_THREE = "用户认证已达上限-3";

    /**
     * 用户名密码校验失败aaa100022
     */
    public static final String USER_NAME_PW_ERROR = "用户名密码校验失败";

    /**
     * 用户信息查询失败，token与用户名校验失败aaa100023
     */
    public static final String QUERY_TOKEN_USERNAME_ERROR = "用户信息查询失败，token与用户名校验失败";

    /**
     * 用户信息修改失败，token与用户名校验失败aaa100024
     */
    public static final String MODIFY_TOKEN_USERNAME_ERROR = "用户信息修改失败，token与用户名校验失败";

    /**
     * 用户信息修改失败，原密码错误aaa100025
     */
    public static final String MODIFY_PW_CHECK_ERROR = "用户信息修改失败，原密码错误";

    /**
     * token没有找到aaa100026
     */
    public static final String TOKEN_NOT_FOUND = "token没有找到";

    /**
     * token已失效aaa100027
     */
    public static final String HEART_BEAT_LOGIN_INVALID = "登录已过期，请重新登录！";

    /**
     * 心跳检测，未登录（没有找到登录信息）aaa100028
     */
    public static final String HEART_BEAT_LOGIN_NOT_EXIST = "未登录！";

    /**
     * 心跳检测，MAC地址不匹配aaa100029
     */
    public static final String HEART_BEAT_MAC_FAIL = "MAC地址不匹配！";

    /**
     * 心跳检测，用户名不匹配aaa100030
     */
    public static final String HEART_BEAT_USER_NAME_FAIL = "用户名不匹配！";

    /**
     * 过载流控邮件标题
     */
    public static final String OVERLOAD_EMALL_TITEL_FLOW = "AAA过载流控！！！";

    /**
     * 过载告警邮件标题
     */
    public static final String OVERLOAD_EMALL_TITEL = "AAA过载告警！";

    /**
     * email格式不正确aaa100031
     */
    public static final String EMAIL_FORMAT_FAIL = "email格式不正确";

    /**
     * token与用户名校验失败aaa100032
     */
    public static final String USERNAME_TOKEN_ERROR = "token与用户名校验失败";

    /**
     * 订购套餐不存在aaa100033
     */
    public static final String ORDER_PACKAGE_NOT_EXIST = "订购套餐不存在";

    /**
     * 订购套餐已禁用或已过期aaa100034
     */
    public static final String ORDER_PACKAGE_DISABLED = "订购套餐已禁用或已过期";

    /**
     * 更新订购支付状态错误aaa100035
     */
    public static final String ORDER_UPDATE_PLAY_STATUS_ERROR = "支付状态码错误";

    /**
     * 订购信息不存在aaa100036
     */
    public static final String ORDER_INFO_NOT_EXIST = "订购信息不存在";

    /**
     * 产品码或套餐码，必须有一个存在aaa100037
     */
    public static final String PACKAGE_OR_PRODUCT_MUST_EXIST = "产品码和套餐码必须只有一个存在！";

    /**
     * 首次激活策略不存在aaa100038
     */
    public static final String FIRST_ACTIVE_AUTH_NOT_EXIST = "首次激活策略不存在";

    /**
     * 生成授权信息失败aaa100039
     */
    public static final String CREATE_AUTH_INFO_FAIL = "生成授权信息失败";

    /**
     * 终端数据不存在aaa100040
     */
    public static final String TERMINAL_DATA_NOT_EXIST = "终端数据不存在";

    /**
     * 用户信息插入数据库失败aaa100041
     */
    public static final String USER_INSERT_DATA_FAIL = "用户信息插入数据库失败";

    /**
     * 更新终端信息失败aaa100042
     */
    public static final String UPDATE_TERMINAL_INFO_FAIL = "更新终端信息失败";

    /**
     * 数据库找不到user对象aaa100043
     */
    public static final String USER_DATA_NOT_FOUNT = "数据库找不到用户对象";

    /**
     * 更新用户信息失败aaa100044
     */
    public static final String UPDATE_USER_INFO_FAIL = "更新用户信息失败";

    /**
     * 更新token信息失败aaa100045
     */
    public static final String UPDATE_TOKEN_INFO_FAIL = "更新token信息失败";

    /**
     * token对象插入数据库失败aaa100046
     */
    public static final String TOEKN_INSERT_DATA_FAIL = "token对象插入数据库失败";

    /**
     * macAddr参数没有值aaa100047
     */
    public static final String MAC_PARAM_NOT_VAL = "macAddr参数没有值";

    /**
     * 参数异常
     */
    public static final String PARAM_EXCEPTION = "请求参数[{}]异常！";

    /**
     * 授权信息不存在aaa100048
     */
    public static final String AUTH_INFO_NOT_EXIST = "授权信息不存在";

    /**
     * 兑换内容为空aaa100049
     */
    public static final String EXCHANGE_CONTENT_INFO_IS_EMPTY = "兑换内容为空";

    /**
     * 授权策略为禁用状态aaa100050
     */
    public static final String AUTH_POLICY_DISABLE = "授权策略为禁用状态";

    /**
     * 报文解析异常
     */
    public static final String EXCHANGE_BODY_ERROR = "报文异常";

    /**
     * 连接异常
     */
    public static final String EXCHANGE_CONNECT_ERROR = "连接异常";

    /**
     * 订单已支付，请勿重复操作
     */
    public static final String ORDER_PAID_NOT_REPEAT = "订单已支付，请勿重复操作";

    /**
     * 订购的产品是剧集或节目，必须指定内容ID
     */
    public static final String ORDER_PRODUCT_CONTENT_ID_NOT_NOTHING = "订购剧集或节目，必须指定contentId参数";

    /**
     * 终端不是首次激活、但并没有绑定用户
     */
    public static final String TERMINAL_NOT_FIRST_ACTIVE = "终端不是首次激活、但并没有绑定用户";

    /**
     * 核对订单失败
     */
    public static final String CHECK_ORDER_FAIL = "核对订单失败";

    /**
     * 兑换太频繁，限制访问
     */
    public static final String EXCHANGE_LIMIT = "兑换太频繁，限制访问";

    /**
     * aaa100057 增加授权天数超过最大限制天数
     */
    public static final String MMS_AUTH_DAYS_LIMIT = "增加授权天数超过最大限制天数";

    /**
     * aaa100058 授权太频繁，限制访问
     */
    public static final String MMS_AUTH_LIMIT = "授权太频繁，限制访问";
}
