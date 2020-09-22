// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AlgorithmUtil.java

package com.hoob.search.redis.route;

import com.hoob.search.redis.utils.DateUtil;


public class AlgorithmUtil {
//
	public AlgorithmUtil() {
	}

	/**
	 * 根据key算出id
	 * 
	 * @param key
	 *            key
	 * @return 计算结果
	 * @see
	 */
	public static int getFNVHashCode(String key) {
		int hash = 0;
		int x = 0;
		for (int i = 0; i < key.length(); i++) {
			hash = (hash << 4) + key.charAt(i);
			if ((x = hash & 0xf0000000) != 0) {
				hash ^= x >> 24;
				hash &= ~x;
			}
		}

		return hash & 0x7fffffff;
	}
//
	public static void main(String args[]) {
		int i = getFNVHashCode(DateUtil.getSysDate("yyyyMMdd"));
		System.out.println(i);
	}
}
