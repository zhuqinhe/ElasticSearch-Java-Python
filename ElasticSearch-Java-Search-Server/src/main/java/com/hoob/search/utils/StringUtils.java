package com.hoob.search.utils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hoob.search.redis.utils.CommonConsts;
import com.hoob.search.redis.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class StringUtils {

	public static final long HOURS_ONE_DAY = 24 * 60 * 60 * 1000;
	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 数字常量
	 */
	public static final int NUMBER = 1;

	public static final int TEXT = 2;

	public static final int CHAR = 3;

	public static final int REGEXP = 4;

	public static final int DATE = 5;

	public static final int DOUBLE = 6;

	public static final int NUM_AND_CHAR = 7;

	public static final int CHAR_TWO = 8;

	public static final int NOT_CHECK_LEN = -1;

	/**
	 * 小数占位格式
	 */
	public static final String FORMAT_STYLE = "0.###";

	public static final String FORMAT_STYLE_LEN_TWO = "0.##";

	/**
	 * 查询限制
	 */
	public static final String LIMITQUERY = "20";

	/**
	 * 数字格式校验
	 */
	public static final String RATIONAL_NUMBERS_REGEXP = "^(-?\\d+)(\\.\\d+)?$";

	/**
	 * key
	 */
	public static final String THREEDESKEY = "2b494e53756c664c2f44465245733572";

	public static final String HUAWEIPASSWORDDESKEY = "2b494e53756c664c2f44465245733572";

	/**
	 * 180000
	 */
	public static final long TOKENTIME = 0x1b7740L;

	public static final long HEARTIME = 60000L;

	public static final String SOURCE_SYSTEM = "HQ";

	/**
	 * 字母+数字校验
	 */
	public static final String NUMBERS_AND_CHAR_REGEXP = "^[A-Za-z0-9]+$";

	/**
	 * 字母+数字校验
	 */
	public static final String LEGALCHAR_REGEXP = "^\\w+$";
	//
	public void stringUtil() {
	}

	/**
	 * 校验是否数字或字母
	 *
	 * @param str
	 *            需要校验的字符串
	 * @return 校验结果
	 * @see
	 */
	private final static Pattern IS_NUM_AND_CHAR = Pattern.compile("^[A-Za-z0-9]+$");

	public static boolean isNumAndChar(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}

		return IS_NUM_AND_CHAR.matcher(str).matches();
	}

	/**
	 * 根据指定数字拼接加0到指定位数
	 *
	 * @param index
	 *            数字
	 * @param maxLen
	 *            最大长度
	 * @return 处理结果
	 * @see
	 */
	public static String getIndexStr(int index, int maxLen) {
		String indexStr = String.valueOf(index);
		for (int len = indexStr.length(); len < maxLen; len++) {
			indexStr = (new StringBuilder()).append("0").append(indexStr).toString();
		}

		return indexStr;
	}

	/**
	 * 判断字符串是否小数
	 *
	 * @param str
	 *            字符串
	 * @return 检验结果
	 * @see
	 */
	private final static Pattern IS_DOUBLE = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");

	public static boolean isDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}

		return IS_DOUBLE.matcher(str).matches();
	}

	/**
	 * 判断字符串是否数字
	 *
	 * @param str
	 *            字符串
	 * @return 校验结果
	 * @see
	 */
	private final static Pattern IS_INTEGER = Pattern.compile("(^(0|[1-9]\\d*)$|^(-([1-9]\\d*))$)");

	public static boolean isInteger(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}

		return IS_INTEGER.matcher(str).matches();
	}

	/**
	 * 校验字符串是否是由字母和数字组成
	 *
	 * @param str
	 *            字符串
	 * @return 校验结果
	 * @see
	 */
	private final static Pattern IS_LEGAL_CHAR = Pattern.compile("^\\w+$");

	public static boolean isLegalChar(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}

		return IS_LEGAL_CHAR.matcher(str).matches();
	}

	/**
	 * 校验是否中文
	 *
	 * @param str
	 *            字符串
	 * @return 校验结果
	 * @see
	 */
	private final static Pattern IS_CHINA = Pattern.compile("[\\u4e00-\\u9fa5]+");

	public static boolean isChina(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}

		return IS_CHINA.matcher(str).matches();
	}

	/**
	 * 校验字符串是否equals
	 *
	 * @param str
	 * @param str1
	 * @return
	 * @see
	 */
	public static boolean equals(String str, String str1) {
		return getString(str).equals(getString(str1));
	}

	/**
	 * 获取一个null 转换成空字符串
	 *
	 * @param str
	 *            字符串
	 * @return 转换结果
	 * @see
	 */
	public static String getString(String str) {
		return null != str ? str : "";
	}

	/**
	 * 将double转换成字符串
	 *
	 * @param str
	 *            字符串
	 * @return 转换结果
	 * @see
	 */
	public static String getString(Double str) {
		return null != str ? String.valueOf(str) : "";
	}

	/**
	 * 将long转换成字符串
	 *
	 * @param str
	 *            字符串
	 * @return 转换结果
	 * @see
	 */
	public static String getString(Long str) {
		return null != str ? String.valueOf(str) : "";
	}

	/**
	 * 将integer转换成数字
	 *
	 * @param str
	 *            字符串
	 * @return 转换结果
	 * @see
	 */
	public static String getString(Integer str) {
		return null != str ? String.valueOf(str) : "";
	}

	/**
	 * 将字符串中的标签转成 转义字符
	 *
	 * @param origine
	 *            字符串
	 * @return 转义后的字符串
	 * @see
	 */
	public static String convert2Html(String origine) {
		String outStr = null;
		if (null != origine) {
			String tmp = replace(origine, ">", "&gt;");
			String tmp2 = replace(tmp, "<", "&lt;");
			String tmp3 = replace(tmp2, " ", "&nbsp;");
			String tmp4 = replace(tmp3, "\r\n", "<br>");
			outStr = tmp4;
		} else {
			outStr = "";
		}
		return outStr;
	}

	/**
	 * 将指定字符串中的字符进行替换
	 *
	 * @param str
	 *            字符串
	 * @param old
	 *            需要替换的字符
	 * @param rep
	 *            替换后的字符
	 * @return 处理结果
	 * @see
	 */
	public static String replace(String str, String old, String rep) {
		if (null == str || null == old || null == rep) {
			return "";
		}

		int index = str.indexOf(old);
		if (index < 0 || "".equals(old)) {
			return str;
		}

		StringBuffer strBuf = new StringBuffer(str);

		for (; index >= 0; index = strBuf.toString().indexOf(old)) {
			strBuf.delete(index, index + old.length());
			strBuf.insert(index, rep);
		}

		return strBuf.toString();
	}

	/**
	 * 该方法主要使用正则表达式来判断字符串中是否包含字母
	 * 
	 * @param cardNum
	 *            待检验的原始卡号
	 * @return 返回是否包含
	 */
	public static boolean judgeContainsStr(String cardNum) {
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(cardNum);
		return m.matches();
	}

	/**
	 * 获取随机数
	 *
	 * @param index
	 *            次数
	 * @return 结果
	 * @see
	 */
	public static String getRandom(int index) {
		int randomIndex = -1;
		StringBuffer randomID = new StringBuffer("");
		Double medianRandom = null;
		char randomElement[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		for (int i = 0; i < index; i++) {
			medianRandom = Double.valueOf(Math.random() * 998D);
			randomIndex = medianRandom.intValue() % 10;
			randomID.append(String.valueOf(randomElement[randomIndex]));
		}

		return randomID.toString();
	}

	/**
	 * 获取随机数
	 *
	 * @param index
	 *            次数
	 * @return 结果
	 * @see
	 */
	public static String getRandomZeroAndOne(int index) {
		int randomIndex = -1;
		StringBuffer randomID = new StringBuffer("");
		Double medianRandom = null;
		char randomElement[] = { '0', '1', '0', '1', '1', '0', '1', '0', '1', '1' };
		for (int i = 0; i < index; i++) {
			medianRandom = Double.valueOf(Math.random() * 998D);
			randomIndex = medianRandom.intValue() % 10;
			randomID.append(String.valueOf(randomElement[randomIndex]));
		}

		return randomID.toString();
	}

	/**
	 * 判断字符串是null、""、"null"
	 *
	 * @param string
	 *            字符串
	 * @return 判断结果
	 * @see
	 */
	public static boolean isNil(String string) {
		return null == string || 0 == string.trim().length() || "null".equals(string);
	}

	/**
	 * 检查校验
	 *
	 * @param regex
	 *            校验格式
	 * @param value
	 *            值
	 * @param isNull
	 *            是否为空
	 * @return 处理结果
	 * @see
	 */
	public static boolean checkValidate(String regex, String value, boolean isNull) {
		if (!isNull && isNil(value)) {
			return true;
		}

		if (isNil(value)) {
			return false;
		} else {
			return checkValidate(regex, value);
		}
	}

	/**
	 * 指定格式校验字符串
	 *
	 * @param regex
	 *            校验格式
	 * @param value
	 *            字符串
	 * @return 处理结果
	 * @see
	 */
	public static boolean checkValidate(String regex, String value) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 * 检查字符串日期格式
	 *
	 * @param str
	 *            字符串
	 * @param formatStr
	 *            日期格式
	 * @return
	 * @see
	 */
	// public static boolean checkDate(String str, String formatStr)
	// {
	// if (str == null || formatStr == null) return false;
	// return DateUtil.format(str, formatStr) != null;
	// }

	/**
	 * 数据格式化
	 *
	 * @param value
	 *            double值
	 * @param dataFormatStyle
	 *            格式
	 * @return 处理结果
	 * @see
	 */
	public static String dataFormat(double value, String dataFormatStyle) {
		DecimalFormat df = new DecimalFormat(dataFormatStyle);
		return df.format(value);
	}

	/**
	 * 获取字符串Java Oracle Length
	 *
	 * @param value
	 *            字符串
	 * @return 长度
	 * @see
	 */
	public static int getJava2OracleLength(String value) {
		if (null == value) {
			return 0;
		}

		if (value.length() == value.getBytes().length) {
			return value.length();
		}

		int len = 0;
		char charArr[] = value.toCharArray();

		for (int i = 0; i < charArr.length; i++) {
			if (String.valueOf(charArr[i]).matches("[^x00-xff]")) {
				len += 3;
			} else {
				len++;
			}
		}

		return len;
	}

	/**
	 * 获取字符串Java Oracle Length
	 *
	 * @param value
	 *            字符串
	 * @return 长度
	 * @see
	 */
	public static int getLengthInOracle(String value) {
		int length = 0;
		try {
			if (!isNil(value)) {
				length = value.getBytes("UTF-8").length;
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return length;
	}

	/**
	 * float数据格式化
	 *
	 * @param value
	 *            float值
	 * @param dataFormatStyle
	 *            格式
	 * @return 处理结果
	 * @see
	 */
	public static String dataFormat(float value, String dataFormatStyle) {
		DecimalFormat df = new DecimalFormat(dataFormatStyle);
		return df.format(value);
	}

	/**
	 * 获取序列号
	 *
	 * @return 序列号
	 * @see
	 */
	public static String getSerialNumer() {
		int randomInt = (new Random()).nextInt(0xf423f);
		String serialNumer = (new StringBuilder())
				.append(DateUtil.getSysDate(DateUtil.YYYYMMDDHHMMSS_SHORT, new Date())).append(randomInt).toString();
		return serialNumer;
	}

	/**
	 * 判断字符串不为null或空字符串
	 *
	 * @param foo
	 * @return
	 * @see
	 */
	public static final boolean isValid(String foo) {
		return foo != null && foo.length() > 0;
	}

	/**
	 * 检查sql格式
	 *
	 * @param sql
	 *            sql字符串
	 * @return 检查结果
	 * @see
	 */
	public static boolean checkRiskSql(String sql) {
		sql = sql.trim().toLowerCase();
		if ((sql.startsWith("delete") || sql.startsWith("update"))
				&& (!sql.contains("where") || sql.indexOf("=") == -1)) {
			LOGGER.error("checkRiskSql Error:" + sql);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断字符串是否为null或者空字符串
	 *
	 * @param string
	 * @return
	 * @see
	 */
	public static boolean isEmpty(String string) {
		return (null == string) || (string.trim().length() == 0);
	}

	/**
	 * 判断传入参数是否为空
	 *
	 * @param params
	 * @return
	 * @see
	 */
	public static boolean paramValid(String... params) {
		if (null == params) {
			return true;
		}

		for (String param : params) {
			if (isEmpty(param)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 用下划线拼接传入参数
	 *
	 * @param params
	 * @return
	 * @see
	 */
	public static String printVal(String... params) {
		StringBuilder str = new StringBuilder("");
		if (null == params) {
			return str.toString();
		}

		for (String param : params) {
			str.append(isEmpty(param) ? "NULL" : param).append(CommonConsts.DOWN_LINE);
		}

		return str.toString();
	}
	/**
	 * 用逗号拼接sql in 传入参数
	 *
	 * @param list
	 * @return
	 * @see
	 */
	public static String printValComma(List<String> list) {
		StringBuilder str = new StringBuilder("");
		if (null == list) {
			return str.toString();
		}
		for (String param : list) {
			if(list.size() - 1 == list.indexOf(param)){
				str.append(isEmpty(param) ? "NULL" : param);
			}else{
				str.append(isEmpty(param) ? "NULL" : param).append("'"+CommonConsts.COMMA+"'");
			}
		}

		return str.toString();
	}
	/**
	 * 文件的作路径中
	 * 
	 * @param path
	 * @return
	 */
	public static String getDirFromPath(String path) {
		if (path == null || "".equals(path.trim())) {
			return null;
		}
		int index = path.lastIndexOf("/");
		if (index >= 0) {
			return path.substring(0, index + 1);
		}
		return null;
	}

	/**
	 * 从文件名全路径中获取文件名称
	 * 
	 * @param path
	 * @return 说明：路径分隔符必须为/
	 */
	public static String getFileNameFromPath(String path) {
		if (path == null || "".equals(path.trim())) {
			return null;
		}
		int index = path.lastIndexOf("/");
		if (index >= 0) {
			return path.substring(index + 1);
		}
		return null;
	}

	/**
	 * 判断参数是否为空：此处为空的条件是参数等于null或空串或null字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean paramIsNull(String param) {
		if (null == param || "".equals(param) || "null".equalsIgnoreCase(param.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断参数是否不为空：此处为空的条件是参数等于null或空串或null字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean paramIsNotNull(String param) {
		return !paramIsNull(param);
	}
	//
	public static String handleStrParam(String p) {
		if (p == null || p.trim().length() <= 0 || "null".equalsIgnoreCase(p.trim())
				|| "undefined".equalsIgnoreCase(p.trim())) {
			return null;
		}
		return p.trim();
	}
	//
	public static Integer handleIntParam(Object o, int defaultValue) {
		if (o == null) {
			return defaultValue;
		}

		if (String.valueOf(o).matches("\\d+")) {
			return Integer.parseInt(o.toString());
		}
		return defaultValue;
	}
	//
	public static Integer handleIntParam(String p) {
		Integer i = null;
		if (p == null || p.trim().length() <= 0 || "null".equalsIgnoreCase(p.trim())
				|| "undefined".equalsIgnoreCase(p.trim())) {
			return null;
		}
		try {
			i = Integer.parseInt(p);
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error("分页数字转换错误");
		}
		return i;
	}
	//
	public static Long handleLongParam(String p) {
		Long pl = null;
		if (p == null || p.trim().length() <= 0 || "null".equalsIgnoreCase(p.trim())
				|| "undefined".equalsIgnoreCase(p.trim())) {
			pl = null;
		} else {
			try {
				pl = Long.parseLong(p);
			} catch (NumberFormatException e) {
				LOGGER.error("id is not a number");
				LOGGER.error(e.getMessage(), e);
			}
		}
		return pl;
	}
	/**
	 * 
	 * @param set
	 * @param separator
	 * @return
	 */
	public static String setTowStr(Set<String> set, String separator) {
		if (set == null || set.size() < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer("");
		for (String s : set) {
			if ("".equals(sb.toString())) {
				sb.append(s);
			} else {
				sb.append(separator);
				sb.append(s);
			}
		}
		return sb.toString();
	}

	/**
	 * 用指定的分隔符将list转换成String
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String list2Str(List list, final String separator) {
		if (list == null || list.size() < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer("");
		for (Object obj : list) {
			if ("".equals(sb.toString())) {
				sb.append(obj.toString());
			} else {
				sb.append(separator);
				sb.append(obj.toString());
			}
		}
		return sb.toString();
	}

	/**
	 * 用指定的分隔符将string转换成list
	 * 
	 * @param str
	 * @param separator
	 * @return
	 */
	public static List<String> str2List(String str, final String separator) {
		List<String> list = new ArrayList<String>();
		if (isNotEmpty(str)) {
			String[] strArray = str.split(separator);
			if (strArray != null && strArray.length > 0) {
				for (int i = 0; i < strArray.length; i++) {
					list.add(strArray[i]);
				}
			}
		}
		return list;
	}

	/**
	 * 判断字符串是否为大于0的数字
	 * 
	 * @param s
	 * @return 字符大于0返回true；否则返回false
	 */
	public static boolean isNumberAndMoreThanZeo(String s) {
		if (org.apache.commons.lang3.StringUtils.isNumeric(s) && Long.parseLong(s) > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNumber(CharSequence s) {
		return org.apache.commons.lang3.StringUtils.isNumeric(s);
	}

	public static boolean isEmpty(CharSequence str) {
		return org.apache.commons.lang3.StringUtils.isBlank(str);
	}

	public static boolean isNotEmpty(CharSequence str) {
		return org.apache.commons.lang3.StringUtils.isNotBlank(str);
	}

	/**
	 * 如果s不为空（去除前后空白后），则返回前后加“%”的串，否则，返回null。 用于在数据中匹配字符串字段。
	 * 
	 * @param s
	 *            源字符串。
	 * @return 如果s不为空（去除前后空白后），则返回前后加“%”的串，否则，返回null。
	 */
	public static String likeStr(String s) {
		s = emptyToNull(s);
		if (s != null) {
			s = s.replace("%", "\\%");
			s = s.replace("_", "\\_");
			return '%' + s + '%';
		} else {
			return null;
		}
	}

	/**
	 * 用于匹配s开头的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String likeSearchStr(String s) {
		s = emptyToNull(s);
		if (s != null) {
			s = s.replace("%", "\\%");
			s = s.replace("_", "\\_");
			return s + '%';
		} else {
			return null;
		}
	}

	/**
	 * 如果s去除前后空白后为空，则返回null；否则返回s去除前后空白后的串。
	 * 
	 * @param s
	 *            源字符串。
	 * @return 如果s去除前后空白后为空，则返回null；否则返回s去除前后空白后的串。
	 */
	public static String emptyToNull(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		} else {
			if (StringUtils.isEmpty(s = s.trim())) {
				return null;
			} else {
				return s;
			}
		}
	}

	// set转string
	public static String setTowString(Set<Long> cpIds) {
		if (cpIds != null && cpIds.size() >= 1) {
			StringBuilder sb = new StringBuilder();
			for (Long id : cpIds) {
				if (sb.toString().length() > 0) {
					sb.append("," + id);
				} else {
					sb.append(id);
				}
			}
			return sb.toString();
		} else {
			return null;
		}

	}

	public static List<String> getStrListBySeperator(String str, String seperator) {
		List<String> strList = new ArrayList<String>();
		if (str == null || "".equals(str)) {
			return strList;
		}
		strList = Arrays.asList(str.split(seperator));
		return strList;
	}

	/**
	 * 判断输入字符串是否为null，为null则返回""
	 */
	public static String formatNullStr(Object str) {
		return str == null ? "" : str.toString();
	}
	/**
	 * 
	 * @param o
	 * @param def
	 * @return
	 */
	public static Integer toInteger(Object o, Integer def) {
		if (o == null) {
			return def;
		} else if (o instanceof Integer) {
			return (Integer) o;
		} else if (o instanceof String && ((String) o).trim().matches("^-?\\d+$")) {
			return Integer.valueOf((String) o);
		} else {
			return def;
		}
	}

	public static String isRightfulDateRegex = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9]"
			+ "[0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|"
			+ "[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|"
			+ "((0[48]|[2468][048]|[3579][26])00))-02-29)$";

	public static boolean isRightfulDate(String date) {
		return date.matches(isRightfulDateRegex);
	}

	public static String isRightfulTime_regex = "^([0-1]{1}[0-9]{1}|2[0-3]{1}):[0-5]{1}[0-9]{1}$";

	public static boolean isRightfulTime(String date) {
		return date.matches(isRightfulTime_regex);
	}

	/**
	 * 处理语言字符串，方便查询。
	 * 
	 * @param lang
	 *            必须是如"zh-cn"、"en-us"格式。
	 * @return 返回"zh_CN"、"en_US"格式的字符串。
	 */
	public static String handleLangStr(String lang) {
		return new StringBuilder(lang.substring(0, lang.indexOf("-"))).append("_")
				.append(lang.substring(lang.indexOf("-") + 1).toUpperCase()).toString();
	}

	public static String getCorrelateID() {
		String correlateID = UUID.randomUUID().toString().replace("-", "");
		if (correlateID.length() != 32) {
			LOGGER.info(">>>>>correlateID is not 32bit");
		}
		return correlateID;
	}

	/**
	 * 转换url里的'特殊字符
	 * 
	 * @param url
	 * @return
	 */
	public static String formatUrl(String url) {
		return url == null ? "" : url.replace("'", "\"");
	}

	/**
	 * 转义内容中带有'特殊字符
	 * 
	 * @param value
	 * @return
	 */
	public static String formatSpecialChar(String value) {
		return value == null ? "" : value.replace("\'", "\\'");
	}

	public static String getProtocolType(String domain) {
		if (StringUtils.isNotEmpty(domain)) {
			return domain.substring(domain.length() - 1);
		}
		return null;
	}

	public static String getCorrelateID4UNICOM() {
		return getCorrelateID().substring(0, 20);
	}

	public static String getUUID32() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 字符串补0
	 * 
	 * @author ziven
	 * @param str
	 *            需要补0的字符串
	 * @param strLength
	 *            字符串长度
	 * @return
	 */
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = new StringBuffer();
		while (strLen < strLength) {
			// 左补0
			sb.append("0");
			strLen++;
		}
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 字符串长度是否过长
	 * 
	 * @author ziven
	 * @param str
	 * 
	 * @param strLength
	 *            字符串长度
	 * @return
	 */
	public static boolean isGteLength(String str, int strLength) {
		int strLen = str.length();
		if (strLen <= strLength) {
			return true;
		}
		return false;
	}
}
