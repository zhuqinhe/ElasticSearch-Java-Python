package com.hoob.search.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {

	private final static Logger log = LoggerFactory.getLogger(DateUtils.class);

	public static final String START_TIME = "1970-01-01";

	public enum Pattern {
		/**
		 * 日期格式：yyyy-MM-dd HH:mm:ss
		 */
		yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
		/**
		 * 日期格式：yyyy-MM-dd HH:mm:ss
		 */
		yyyy_MM_dd_T_HH_mm_ss("yyyy-MM-dd'T'HH:mm:ss"),
		/**
		 * 日期格式：yyyyMMddHHmmss
		 */
		yyyyMMddHHmmss("yyyyMMddHHmmss"),
		/**
		 * 日期格式：yyyy/MM/dd HH:mm:ss
		 */
		yyyy1MM1dd_HH_mm_ss("yyyy/MM/dd HH:mm:ss"),
		/**
		 * 日期格式：yyyyMMdd HHmmss
		 */
		yyyyMMdd_HHmmss("yyyyMMdd HHmmss"),
		/**
		 * 日期格式：yyyy-MM-dd
		 */
		yyyy_MM_dd("yyyy-MM-dd"),
		/**
		 * 日期格式：HHmmss
		 */
		HHmmss("HHmmss"),
		/**
		 * 日期格式：yyyyMMdd
		 */
		yyyyMMdd("yyyyMMdd");

		private Pattern(String value) {
			this.value = value;
		}

		private String value;

		public String getValue() {
			return value;
		}

	}

	/**
	 * 获取指定格式的日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, Pattern pattern) {
		return FastDateFormat.getInstance(pattern.getValue()).format(date);
	}
	/**
	 * 时间格式化
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return formatDate(date, Pattern.yyyy_MM_dd_HH_mm_ss);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return formatDate(new Date(), Pattern.yyyy_MM_dd_HH_mm_ss);
	}

	/**
	 * 指定时间转换成UTC时间
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String getUTCTime(String time, Pattern pattern) {
		Date timeDate = null;
		try {
			timeDate = FastDateFormat.getInstance(pattern.getValue()).parse(time);
			return DateFormatUtils.formatUTC(timeDate, pattern.getValue());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取本周第一天的日期(星期一)
	 * 
	 * @return
	 */
	public static Date getWeekFirstDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取本月第一天的日期
	 * 
	 * @return
	 */
	public static Date getMonthFirstDate() {
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取本月最后一天的日期
	 * 
	 * @return
	 */
	public static Date getMonthLastDate() {
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * 获取本年的一一天
	 * 
	 */
	public static Date getYearFirstDate() {
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	/**
	 * 将时间戳转换为时间
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Date transTimstampToDate(long timestamp) {
		Date date = new Date(timestamp);
		return date;
	}

	/**
	 * 将时间戳转换为时间
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Date transTimstampToDate(String timestamp) {
		try {
			long l = Long.parseLong(timestamp);
			return transTimstampToDate(l);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}

	}

	/**
	 * 将字符串格式日期转换为Date
	 * 
	 * @param dateTime
	 * @param pattern
	 * @return
	 */
	public static Date parse(String dateTime, Pattern pattern) {
		try {
			Date newDate = FastDateFormat.getInstance(pattern.getValue()).parse(dateTime);
			return newDate;
		} catch (ParseException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 格式日期
	 * @param dateTime
	 * @return 日期格式：yyyy-MM-dd HH:mm:ss
	 */
	public static Date parse(String dateTime){
		return parse(dateTime, Pattern.yyyy_MM_dd_HH_mm_ss);
	}

	/**
	 * 将字符串格式日期转换为Date
	 * 
	 * @param dateTime
	 * @param pattern
	 * @return
	 */
	public static Date parse(String dateTime, String pattern) {
		try {
			return FastDateFormat.getInstance(pattern).parse(dateTime);
		} catch (ParseException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 获取当前时间hour小时之前的时间
	 * 
	 * @param hour
	 * @return
	 */
	public static String getBeforeTime(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(hour + "小时前的时间：" + df.format(calendar.getTime()));
		System.out.println("当前的时间：" + df.format(new Date()));

		return df.format(calendar.getTime());
	}

	/**
	 * 获取当前时间minute分钟之后的时间
	 * 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static String getAfterTime(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(minute + "分钟后的时间：" + df.format(calendar.getTime()));
		System.out.println("当前的时间：" + df.format(new Date()));

		return df.format(calendar.getTime());
	}

	/**
	 * 获取当前时间minute分钟之后的时间
	 * 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getAfterDate(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (minute != 0) {
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(minute + "分钟后的时间：" + df.format(calendar.getTime()));
		System.out.println("当前的时间：" + df.format(new Date()));

		return calendar.getTime();
	}

	/**
	 * 由开始时间和结束时间，计算时长
	 *
	 * @param startTime
	 *            开始时间，如：02:21:00
	 * @param endTime
	 *            开始时间，如：02:26:19
	 * @return
	 * @throws ParseException
	 */
	public static String calLengthByStartEnd(String startTime, String endTime) throws ParseException {
		Date start = FastDateFormat.getInstance("HH:mm:ss").parse(startTime);
		Date end = FastDateFormat.getInstance("HH:mm:ss").parse(endTime);
		long length = (end.getTime() / 1000) - (start.getTime() / 1000);
		long hour = length / 3600;
		long minute = (length % 3600) / 60;
		long second = length % 60;
		return new StringBuilder().append(hour < 10 ? "0" + hour : hour).append(minute < 10 ? "0" + minute : minute)
				.append(second < 10 ? "0" + second : second).append("00").toString();
	}

	/**
	 * 日期+n天
	 * 
	 * @param date
	 * @return
	 */
	public static String addDate(String date, Pattern pattern,int n) {
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(parse(date, pattern));
			cl.add(Calendar.DATE, n);
			return formatDate(cl.getTime(), pattern);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}

	}
	
	/**
	 * 当前日期+n年
	 * 
	 * @param n
	 * @return
	 */
	public static Date getAfterYear(int n) {
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTime(new Date());
			cl.add(Calendar.YEAR, n);
			return cl.getTime();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}

	}
	/**
	 * 指定时间转换成UTC时间
	 *
	 * @param date
	 * @return
	 */
	public static String getUTCTime(String date) {
		try {
			return DateFormatUtils.formatUTC(parse(date, Pattern.yyyy_MM_dd_HH_mm_ss),
					Pattern.yyyy_MM_dd_HH_mm_ss.getValue());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }

	public static String getYYYYMMDD() {
		return formatDate(new Date(), Pattern.yyyyMMdd);
	}

	/**
     * 获取当前时间
     * @return
     */
    public static Date now() {
    	Calendar cl = Calendar.getInstance();
    	
    	return cl.getTime();
    }
    
}
