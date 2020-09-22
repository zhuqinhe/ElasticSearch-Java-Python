package com.hoob.search.sys.utils;

import java.util.Set;
import java.util.TreeSet;

import com.hoob.search.sys.common.ConfigKey;

public class Utils {
	//
	public static String arrayToString(String[] strArr) {

		if (strArr == null || strArr.length == 0) {
			return "";
		}
		Set<String> set = new TreeSet<>();
		for (String str : strArr) {
			set.add(str);
		}
		StringBuffer sb = new StringBuffer("");
		for (String str : set) {
			sb.append(str).append(",");
		}
		return sb.toString();
	}

	//
	public static String checkLanguage(String language) {
		String defaultLanguage = "zh_CN";
		if (StringUtils.isEmpty(language)) {
			language = ConfigUtil.getProperties(ConfigKey.DEFAULT_LANGUAGE.name());
			;
		}
		if (StringUtils.isEmpty(language)) {
			language = defaultLanguage;
		}
		return language;
	}
}
