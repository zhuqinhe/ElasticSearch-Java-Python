package com.hoob.search.redis.utils;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SerializeUtil {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtil.class);
//
	public SerializeUtil() {
	}

	/**
	 * 序列化对象
	 * 
	 * @param object
	 *            对象
	 * @return 序列化值
	 * @see
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		byte bytes[];

		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			LOGGER.error((new StringBuilder()).append("serialize failed, the obj is:").append(object).toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 *            序列化值
	 * @return 对象
	 * @see
	 */
	public static Object unserialize(byte bytes[]) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			LOGGER.error((new StringBuilder()).append("unserialize failed, the string is:").append(new String(bytes))
					.toString());
			e.printStackTrace();
		}

		return null;
	}

}
