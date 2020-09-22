package com.hoob.search.utils;


import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 字符串处理通用类
 * 
 * @see StringUtil
 * @since
 */
public final class StringUtil{
    /**
     * 日志
     */
    private static Logger logger = LogManager.getLogger(StringUtil.class);

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
     * key THREE_DES_KEY
     */
    public static final String THREE_DES_KEY = "2b494e53756c664c2f44465245733572";

    /**
     * HUA_WEI_PASS_WORD_DES_KEY
     */
    public static final String HUA_WEI_PASS_WORD_DES_KEY = "2b494e53756c664c2f44465245733572";

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
    public StringUtil(){
    	
    }

    /**
     * 校验是否数字或字母
     * 
     * @param str
     *            需要校验的字符串
     * @return 校验结果
     * @see
     */
    public static boolean isNumAndChar(String str){
        if (null == str || "".equals(str)){
            return false;
        }
        final String repx = "^[A-Za-z0-9]+$";
        Matcher match = Pattern.compile(repx).matcher(str);
        return match.matches();
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
    public static String getIndexStr(int index, int maxLen){
        String indexStr = String.valueOf(index);
        for (int len = indexStr.length(); len < maxLen; len++ ){
            indexStr = (new StringBuilder()).append("0").append(indexStr).toString();
        }

        return indexStr;
    }

   
    /**
     * 校验字符串是否equals
     * 
     * @param str
     * @param str1
     * @return
     * @see
     */
    public static boolean equals(String str, String str1){
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
    public static String getString(String str){
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
    public static String getString(Double str){
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
    public static String getString(Long str){
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
    public static String getString(Integer str){
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
    public static String convert2Html(String origine){
        String outStr;
        if (null != origine){
            String tmp = replace(origine, ">", "&gt;");
            String tmp2 = replace(tmp, "<", "&lt;");
            String tmp3 = replace(tmp2, " ", "&nbsp;");
            String tmp4 = replace(tmp3, "\r\n", "<br>");
            outStr = tmp4;
        }else{
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
    public static String replace(String str, String old, String rep){
        if (null == str || null == old || null == rep){
            return "";
        }

        int index = str.indexOf(old);
        if (index < 0 || "".equals(old)){
            return str;
        }

        StringBuilder strBuf = new StringBuilder(str);

        for (; index >= 0; index = strBuf.toString().indexOf(old)){
            strBuf.delete(index, index + old.length());
            strBuf.insert(index, rep);
        }

        return strBuf.toString();
    }

    /**
     * 获取随机数
     * 
     * @param index
     *            次数
     * @return 结果
     * @see
     */
    public static String getRandom(int index){
        int randomIndex;
        StringBuilder randomID = new StringBuilder("");
        Double medianRandom;
        char[] randomElement = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i < index; i++ ){
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
    public static String getRandomZeroAndOne(int index){
        int randomIndex;
        StringBuilder randomID = new StringBuilder("");
        Double medianRandom;
        char[] randomElement = {'0', '1', '0', '1', '1', '0', '1', '0', '1', '1'};
        for (int i = 0; i < index; i++ ){
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
    public static boolean isNil(String string){
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
    public static boolean checkValidate(String regex, String value, boolean isNull){
        if (!isNull && isNil(value)){
            return true;
        }

        if (isNil(value)){
            return false;
        }else{
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
    public static boolean checkValidate(String regex, String value){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.matches();
    }

   
    

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
    public static String dataFormat(double value, String dataFormatStyle){
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
    public static int getJava2OracleLength(String value){
        if (null == value){
            return 0;
        }

        if (value.length() == value.getBytes().length){
            return value.length();
        }

        int len = 0;
        char[] charArr = value.toCharArray();

        for (int i = 0; i < charArr.length; i++ ){
            if (String.valueOf(charArr[i]).matches("[^x00-xff]")){
                len += 3;
            }else{
                len++ ;
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
    public static int getLengthInOracle(String value){
        int length = 0;
        try{
            if (!isNil(value)){
                length = value.getBytes("UTF-8").length;
            }
        }catch (UnsupportedEncodingException e){
            logger.error(e.getMessage(), e);
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
    public static String dataFormat(float value, String dataFormatStyle){
        DecimalFormat df = new DecimalFormat(dataFormatStyle);
        return df.format(value);
    }

    

    /**
     * 判断字符串不为null或空字符串
     * 
     * @param foo
     * @return
     * @see
     */
    public static final boolean isValid(String foo){
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
    public static boolean checkRiskSql(String sql){
        sql = sql.trim().toLowerCase();
        if ((sql.startsWith("delete") || sql.startsWith("update"))
            && (!sql.contains("where") || sql.indexOf("=") == -1)){
            logger.error("checkRiskSql Error:" + sql);
            return false;
        }else{
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
    public static boolean isEmpty(String string){
        return (null == string) || (string.trim().length() == 0);
    }

    /**
     * 对比double值
     * 
     * @param frist
     *            第一个值
     * @param second
     *            第二个值
     * @return first > second结果
     * @see
     */
    public static boolean compareDouble(double first, double second){
        return first <= second;
    }

    /**
     * 判断定长数组是否为空
     * 
     * @param string
     *            数组
     * @param len
     *            数组长度
     * @return 是否为空
     * @see
     */
    public static boolean isArrayEmpty(String[] string, int len){
        if (string.length == len){
            for (int i = 0; i < string.length; i++ ){
                if (null == string[i] || string[i].trim().length() == 0){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断数组不为空
     * 
     * @param arry
     *            数组
     * @return 校验结果
     * @see
     */
    public static boolean isNotArrayEntity(String[] arry){
        return null != arry && 0 != arry.length;
    }

    /**
     * 判断传入参数是否为空
     * 
     * @param params
     * @return
     * @see
     */
    public static boolean paramValid(String... params){
        if (null == params){
            return true;
        }

        for (String param : params){
            if (StringUtil.isEmpty(param)){
                return true;
            }
        }

        return false;
    }

}
