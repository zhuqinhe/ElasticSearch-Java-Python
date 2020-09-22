package com.hoob.search.es.client;

public class EsConfig {
	/**
     * 最大数
     */
    private static int maxTotal = 200;

    /**
     * 最大空闲
     */
    private static int maxIdle = 20;

    /**
     * 最大等待毫秒
     */
    private static int maxWaitMillis = 1000;

    /**
     * 超时时间
     */
    private static int timeout = 30000;

    /**
     * 重试次数
     */
    private static int retryNum = 3;

    /**
     * 检查时间
     */
    private static int checkTime = 300000;
//
    public EsConfig(){
    	
    }

	public static int getMaxTotal() {
		return maxTotal;
	}

	public static void setMaxTotal(int maxTotal) {
		EsConfig.maxTotal = maxTotal;
	}

	public static int getMaxIdle() {
		return maxIdle;
	}

	public static void setMaxIdle(int maxIdle) {
		EsConfig.maxIdle = maxIdle;
	}

	public static int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public static void setMaxWaitMillis(int maxWaitMillis) {
		EsConfig.maxWaitMillis = maxWaitMillis;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		EsConfig.timeout = timeout;
	}

	public static int getRetryNum() {
		return retryNum;
	}

	public static void setRetryNum(int retryNum) {
		EsConfig.retryNum = retryNum;
	}

	public static int getCheckTime() {
		return checkTime;
	}

	public static void setCheckTime(int checkTime) {
		EsConfig.checkTime = checkTime;
	}

   
}
