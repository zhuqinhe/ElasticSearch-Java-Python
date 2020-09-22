/*
 * 文件名：OverloadProtection.java 版权：©Copyright by www.sowell-tech.cn 描述： 修改人：xiaohui 修改时间：2016年11月8日
 * 修改内容：
 */

package com.hoob.search.server.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.hoob.search.model.MessageReturn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoob.search.redis.utils.JsonUtil;
import com.hoob.search.utils.CodeMessage;
import com.hoob.search.utils.CodeNum;


@Aspect
@Component("overloadProtection")
public class OverloadProtection {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LogManager.getLogger(OverloadProtection.class);

	/**
	 * 原子整数
	 */
	private static AtomicInteger ai = new AtomicInteger(0);

	/**
	 * 是否过载
	 */
	private static boolean isOverLoad;

	/**
	 * 统计Service中方法调用的时间
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("execution(* com.fonsview.search.controller.*Controller.*(..))")
	public Object overloadAspect(ProceedingJoinPoint joinPoint) throws Throwable {
		// 进入请求就 +1
		ai.getAndIncrement();

		Object object = null;

		// 判断当前是否已经过载
		if (!isOverLoad()) {
			object = joinPoint.proceed();
		} else {
			try {
				ai.getAndDecrement();
				LOGGER.warn(CodeMessage.OVERLOAD_PROTECTION);
				return JsonUtil.writeObject2JSON(
						new MessageReturn(CodeNum.OVERLOAD_PROTECTION_NUB, CodeMessage.OVERLOAD_PROTECTION));
			} catch (JsonProcessingException e) {
				LOGGER.error(e);
			}
		}

		// 请求完成就--
		ai.getAndDecrement();

		return object;
	}

	public static AtomicInteger getProConcurrency() {
		return ai;
	}

	public static synchronized boolean isOverLoad() {
		return isOverLoad;
	}

	public static synchronized void setOverLoad(boolean isOverLoad) {
		OverloadProtection.isOverLoad = isOverLoad;
	}
}
