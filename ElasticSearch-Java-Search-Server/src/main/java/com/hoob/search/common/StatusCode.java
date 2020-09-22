package com.hoob.search.common;


public class StatusCode {	
	
	
	public static class UI{
		
//		0	成功
//		-1	失败
//		20000	Service Currently Unavailable			
//		20001	Invalid Auth Token
//		20002	Auth Token Timeout
//		20003	Invalid Captcha
//		20004	Captcha Timeout
//		20005	Unauthorized
//		20006	Partially Authorized Content
//		20008	Invalid User
//		20009	Abnormal Operation
//		20010	Wrong Arguments
//		20011	IP Restrict
//		20012   Unable user
//		40001	Missing Required Arguments
//		40002	Invalid Arguments
		
		public static final int UI_0 = 0;	
		public static final int UI_1= -1; //未登录
		public static final int UI_20000 = 20000;
		public static final int UI_20001 = 20001;
		public static final int UI_20002 = 20002;
		public static final int UI_20003 = 20003;
		public static final int UI_20004 = 20004;
		public static final int UI_20005 = 20005;
		public static final int UI_20006 = 20006;
		public static final int UI_20008 = 20008;
		public static final int UI_20009 = 20009;
		public static final int UI_20010 = 20010;
		public static final int UI_20011 = 20011;
		public static final int UI_20012 = 20012;
		public static final int UI_40001 = 40001;
		public static final int UI_40002 = 40002;
		public static final int UI_OLD_PASWORD_ERROR = 400;
		public static final int UI_VOD_50001 = 50001;//垫片不能删除
		public static final int UI_PWD_60001 = 60001;//密码过期
		public static final int ERROR_SYS_CONFIG_CHECK = 102001;//"SysConfig check 异常";

		
		
	}
	
	
	public static class BMS{		
//		0	成功
//		-1	部分成功（仅用于通过SOAP协议表示的操作结果中）
//		-2	通用错误（仅用于通过SOAP协议表示的操作结果中）
//		-1000	登陆FTP服务器失败
//		-1001	下载XML文件失败
//		-1002	解析XML文件失败
//		-1003	下载媒体文件（Movie）失败
//		-2000	对象不存在或无效
//		-2001	Mapping失败（Parent对象不存在）
//		-2002	Mapping失败（Element对象不存在）
//		-3000	系统内部错误
//		-3001	数据库错误
//		-4000	COPID无效
//		-4001	SOPID无效
		
		public static final int SUCCESS = 0;		
		public static final int BMS_1 = -1;
		public static final int BMS_2 = -2;
		public static final int BMS_1000 = -1001;
		public static final int BMS_1001 = -1002;
		public static final int BMS_1002 = -1003;
		public static final int BMS_2000 = -2000;
		public static final int BMS_2001 = -2001;
		public static final int BMS_2002 = -2002;
		public static final int BMS_3000 = -3000;
		public static final int BMS_3001 = -3001;
		public static final int BMS_4000 = -4000;
		public static final int BMS_4001 = -4001;
		
		public static final int BUILD_MESSAGE_FAILED = 30000;
		
	}
	
	
	public static class CMS{		
//		0	成功
//		-1	部分成功（仅用于通过SOAP协议表示的操作结果中）
//		-2	通用错误（仅用于通过SOAP协议表示的操作结果中）
//		-1000	登陆FTP服务器失败
//		-1001	下载XML文件失败
//		-1002	解析XML文件失败
//		-1003	下载媒体文件（Movie）失败
//		-2000	对象不存在或无效
//		-2001	Mapping失败（Parent对象不存在）
//		-2002	Mapping失败（Element对象不存在）
//		-3000	系统内部错误
//		-3001	数据库错误
//		-4000	COPID无效
//		-4001	SOPID无效
		
		public static final int SUCCESS = 0;		
		public static final int CMS_1 = -1;
		public static final int CMS_2 = -2;
		public static final int CMS_1000 = -1001;
		public static final int CMS_1001 = -1002;
		public static final int CMS_1002 = -1003;
		public static final int CMS_2000 = -2000;
		public static final int CMS_2001 = -2001;
		public static final int CMS_2002 = -2002;
		public static final int CMS_3000 = -3000;
		public static final int CMS_3001 = -3001;
		public static final int CMS_4000 = -4000;
		public static final int CMS_4001 = -4001;
		
		public static final int BUILD_MESSAGE_FAILED = 30000;
		
	}
	
	public static class CDN{
		
//		0	成功
//		-1	部分成功（仅用于通过SOAP协议表示的操作结果中）
//		-2	通用错误（仅用于通过SOAP协议表示的操作结果中）
//		-1000	登陆FTP服务器失败
//		-1001	下载XML文件失败
//		-1002	解析XML文件失败
//		-1003	下载媒体文件（Movie）失败
//		-2000	对象不存在或无效
//		-2001	Mapping失败（Parent对象不存在）
//		-2002	Mapping失败（Element对象不存在）
//		-3000	系统内部错误
//		-3001	数据库错误
//		-4000	COPID无效
//		-4001	SOPID无效
		
		public static final int SUCCESS = 0;	
		public static final int CDN_1 = -1;
		public static final int CDN_2 = -2;
		public static final int CDN_1000 = -1001;
		public static final int CDN_1001 = -1002;
		public static final int CDN_1002 = -1003;
		public static final int CDN_2000 = -2000;
		public static final int CDN_2001 = -2001;
		public static final int CDN_2002 = -2002;
		public static final int CDN_3000 = -3000;
		public static final int CDN_3001 = -3001;
		public static final int CDN_4000 = -4000;
		public static final int CDN_4001 = -4001;
		
		public static final int BUILD_MESSAGE_FAILED = 30000;
		
		public static final String getDesc(int code){  // I18N
			switch(code){
				case SUCCESS: return "OK";
				case BUILD_MESSAGE_FAILED: return "创建XML工单失败";
			}
			return "";
		}
	}
	
		
	public static class TVGW{
		/********TVGW**********/	
		//		0	操作成功
		//		-100	未知消息
		//		-101	参数非法
		//		-102	内部错误
		//		-1001	内容数目超过限制
		//		-1002	内容不存在
		//		-1003	内容已存在
		//		-1004	内容地址冲突
		//		-1005	FTP上载失败
		//		-1006	磁盘空间不足
		//		-1007	码率不存在（码率分离功能）
		//		-10000	其他错误
		public static final int TVGW_0 = 0;
		public static final int TVGW_100 = -100;
		public static final int TVGW_101 = -101;
		public static final int TVGW_102 = -102;
		public static final int TVGW_1001 = -1001;
		public static final int TVGW_1002 = -1002;
		public static final int TVGW_1003 = -1003;
		public static final int TVGW_1004 = -1004;
		public static final int TVGW_1005 = -1005;
		public static final int TVGW_1006 = -1006;
		public static final int TVGW_1007 = -1007;
		public static final int TVGW_10000 = -10000;
		
		public static final int URL_IS_NULL = 40000;
				
		public static final String getDesc(int code){  // I18N
			switch(code){
				case TVGW_0:return "操作成功";
				case TVGW_100:return "未知消息";
				case TVGW_101:return "参数非法";
				case TVGW_102:return "内部错误";
				case TVGW_1001:return "内容数目超过限制";
				case TVGW_1002:return "内容不存在";
				case TVGW_1003:return "内容已存在";
				case TVGW_1004:return "内容地址冲突";
				case TVGW_1005:return "FTP上载失败";
				case TVGW_1006:return "磁盘空间不足";
				case TVGW_1007:return "码率不存在（码率分离功能）";
				case TVGW_10000:return "其他错误";			
			}	
			return "";
		}
	}
	
	
	public static class CSS{
		/********TVGW**********/	
		//		0	操作成功
		//		-1000	未知消息		
		//		-1001	参数非法
		//		-1002	内部错误
		//		-1003	内容已存在
		//		-1004	内容下载失败
		//		-1005	FTP上载失败
		//		-1006	磁盘空间不足		
		//		-10000	其他错误
		public static final int CSS_0 = 0;		
		public static final int CSS_1000 = -1000;
		public static final int CSS_1001 = -1001;
		public static final int CSS_1002 = -1002;
		public static final int CSS_1003 = -1003;
		public static final int CSS_1004 = -1004;
		public static final int CSS_1005 = -1005;
		public static final int CSS_1006 = -1006;	
		public static final int CSS_10000 = -10000;
		
		public static final int URL_IS_NULL = 40000;	
		public static final int CP_DOAMIN_IS_NULL = 40001;
		public static final int CP_PLAYBACKURLPREFIX_IS_NULL = 40001;
		
	}
	
		
	
	
	public static class TransCode{
		/********TransCode**********/	
		//	0	转码成功
		//	-10001	参数错误
		//	-10002	TaskId冲突
		//	-10003	TaskId不存在
		//	-20001	请求队列满
		//	-30001	获取文件失败
		//	-30002	转码失败
		//	-30003	上传文件失败
		//	-30004	任务忙
		//	-40001	其他错误	
		//	public static final int FAIL = -1;
		
		public static final int	TRANSCODE_0 = 0;
		public static final int TRANSCODE_1 = -1;
		public static final int TRANSCODE_10001 = -10001;
		public static final int TRANSCODE_10002 = -10002;
		public static final int TRANSCODE_10003 = -10003;
		public static final int TRANSCODE_20001 = -20001;
		public static final int TRANSCODE_30001 = -30001;
		public static final int TRANSCODE_30002 = -30002;
		public static final int TRANSCODE_30003 = -30003;
		public static final int TRANSCODE_30004 = -30004;
		public static final int TRANSCODE_40001 = -40001;
	
		
		public static final String getDesc(int code){
			switch(code){
				case TRANSCODE_0:return "操作成功";
				case TRANSCODE_1:return "Fail";
				case TRANSCODE_10001:return "参数错误";
				case TRANSCODE_10002:return "TaskId冲突";
				case TRANSCODE_10003:return "TaskId不存在";
				case TRANSCODE_20001:return "求队列满";
				case TRANSCODE_30001:return "获取文件失败";
				case TRANSCODE_30002:return "转码失败";
				case TRANSCODE_30003:return "上传文件失败";
				case TRANSCODE_30004:return "任务忙";
				case TRANSCODE_40001:return "其他错误";						
			}	
			return "";
		}
		
	}

	/**
	 * 0 成功过
	 * 
	 * -1 失败
	 * 
	 * @author Faker
	 *
	 */
	public static class C2{
		/**
		 * 成功
		 */
		public static final int C2_SUCCESS = 0;
		/**
		 * 失败
		 */
		public static final int C2_FAIL = -1;

	}

	/**
	 * 0 成功过
	 * 
	 * -1 失败
	 * 
	 * @author Faker
	 *
	 */
	public static class C1 {
		/**
		 * 成功
		 */
		public static final int C1_SUCCESS = 0;
		/**
		 * 失败
		 */
		public static final int C1_FAIL = -1;

		// -1 部分成功（仅用于通过SOAP协议表示的操作结果中）
		// -2 通用错误（仅用于通过SOAP协议表示的操作结果中）
		// -1000 登陆FTP服务器失败
		// -1001 下载XML文件失败
		// -1002 解析XML文件失败
		// -1003 下载媒体文件（Movie）失败
		// -2000 对象不存在或无效
		// -2001 Mapping失败（Parent对象不存在）
		// -2002 Mapping失败（Element对象不存在）
		// -3000 系统内部错误
		// -3001 数据库错误
		// -4000 COPID无效
		// -4001 SOPID无效
		// 30000 工单消息创建失败


		public static final int BUILD_MESSAGE_FAILED = 30000;

		public static final String getDesc(int code) {
			switch (code) {
			case BUILD_MESSAGE_FAILED:
				return "工单创建失败";
			}
			return "";
		}
	}

	/**
	 * 0 成功过
	 * 
	 * -1 失败
	 * 
	 * @author Faker
	 *
	 */
	public static class C1CE {
		/**
		 * 成功
		 */
		public static final int C1CE_SUCCESS = 0;
		/**
		 * 失败
		 */
		public static final int C1CE_FAIL = -1;

	}


}
