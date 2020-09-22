package com.hoob.search.redis.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class JsonUtil {

	private static ObjectMapper objectMapper;
//
	public JsonUtil() {
	}

	/**
	 * 将对象转成字符串Json
	 * 
	 * @param obj
	 *            对象
	 * @return Json串
	 * @throws JsonProcessingException
	 * @see
	 */
	public static String writeObject2JSON(Object obj) throws JsonProcessingException {

		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * 将Json串转成对象
	 * 
	 * @param json
	 *            json串
	 * @param obj
	 *            对象class
	 * @return 对象
	 * @throws IOException
	 * @see
	 */
	public static Object writeJSON2Object(String json, Class obj) throws IOException {
		return objectMapper.readValue(json, obj);
	}

	/**
	 * 将json串转成对象
	 * 
	 * @param json
	 *            json串
	 * @param collectionClas
	 *            集合对象
	 * @param elementClasses
	 *            对象数组
	 * @return
	 * @throws IOException
	 * @see
	 */
	public static Object writeJSON2List(String json, Class collectionClass, Class elementClasses[]) throws IOException {
		com.fasterxml.jackson.databind.JavaType jt = objectMapper.getTypeFactory()
				.constructParametricType(collectionClass, elementClasses);
		return objectMapper.readValue(json, jt);
	}

	static {
		// 初始化
		objectMapper = new ObjectMapper();
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
}