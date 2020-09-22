// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space
// Source File Name: RedisConfig.java

package com.hoob.search.redis.client;


public class RedisConfig {
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
	private static int timeout = 3000;

	/**
	 * 重试次数
	 */
	private static int retryNum = 3;

	/**
	 * 检查时间
	 */
	private static int checkTime = 300000;

	//
	public RedisConfig() {
	}

	public static int getMaxTotal() {
		return maxTotal;
	}

	public static void setMaxTotal(int maxTotal) {
		RedisConfig.maxTotal = maxTotal;
	}

	public static int getMaxIdle() {
		return maxIdle;
	}

	public static void setMaxIdle(int maxIdle) {
		RedisConfig.maxIdle = maxIdle;
	}

	public static int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public static void setMaxWaitMillis(int maxWaitMillis) {
		RedisConfig.maxWaitMillis = maxWaitMillis;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		RedisConfig.timeout = timeout;
	}

	public static int getRetryNum() {
		return retryNum;
	}

	public static void setRetryNum(int retryNum) {
		RedisConfig.retryNum = retryNum;
	}

	public static int getCheckTime() {
		return checkTime;
	}

	public static void setCheckTime(int checkTime) {
		RedisConfig.checkTime = checkTime;
	}

}
