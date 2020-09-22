package com.hoob.search.sys.utils;

import java.util.HashMap;
import java.util.Map;

import com.hoob.search.sys.common.ErrorCode;


public class ResponseUtils {
//
	public static Map<String, Object> createRespMap(ErrorCode ec) {
		Map<String, Object> respMap = new HashMap<>();
		respMap.put("resultCode", ec.getCode());
		respMap.put("description", ec.getName());
		return respMap;
	}
	
}
