package com.xc.template.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author luochaoqun
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	private static String startTimes=" 00:00:00";
	private static String endTimes=" 23:59:59";
	public static String FULL_TIME = "yyyy-MM-dd HH:mm:ss";
	public static String FULL_DAY_TIME = "yyyy-MM-dd";
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (date != null) {
			if (pattern != null && pattern.length > 0) {
				formatDate = DateFormatUtils.format(date, pattern[0].toString());
			} else {
				formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
			}
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	public static long getTime(Date date){
		return date.getTime() ;
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate(String date, String pattern) {
		if (StringUtils.isEmpty(date) || StringUtils.isEmpty(pattern)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
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
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/** 
	   * 得到几天前的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	  public static String getDateBefore(String dateTime,int day){ 
		Calendar now = Calendar.getInstance();
		Date d = DateUtils.parseDate(dateTime, "yyyy-MM-dd HH:mm:ss");
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return DateUtils.formatDate(now.getTime(), "yyyy-MM-dd HH:mm:ss");
	}

	  /**
	   * 
	   * @param dateTime "yyyy-MM-dd HH:mm:ss"
	   */
	  public static void getDay(String dateTime,int day){
		    SimpleDateFormat sdf=new SimpleDateFormat(dateTime);  
	        Date date=new Date();  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.add(Calendar.DAY_OF_MONTH, -day);  
	        date = calendar.getTime();  
	        System.out.println(sdf.format(date));  
	  }
	  
	  /**
	   * 返回最近3天的时间
	   * @param dateTime 2017-09-12
	   * @param day 2 
	   * @return [2017-09-09,2017-09-10, 2017-09-11]
	   */
	  public static List<String> getDateBefores(String dateTime,int day){ 
		  List<String> dayList=Lists.newArrayList();
		  for(int i=day;i>=0;i--){
				Calendar now = Calendar.getInstance();
				Date d = DateUtils.parseDate(dateTime, "yyyy-MM-dd");
				now.setTime(d);
				now.set(Calendar.DATE, now.get(Calendar.DATE) - i);
				String date=DateUtils.formatDate(now.getTime(), "yyyy-MM-dd");
				dayList.add(date);
			}
			return dayList;
		}
	  
	  /**
	   * 返回最近3天的时间
	   * @param startTime 2017-09-12
	   * @return 2017-09-12 00:00:00
	   */
	  public static String getDayStart(String startTime){
		  String time=startTime.trim()+startTimes;
		  return time;
	  }
	  
	  /**
	   * 返回当天的结束时间 加上 " 23:59:59"
	   * @param endTime 2017-09-12
	   * @return 2017-09-12 23:59:59
	   */
	  public static String getDayEnd(String endTime){
		  String time=endTime.trim()+endTimes;
		  return time;
	  }

	/**
	 *
	 * @author xuchang
	 * @Description 返回开始时间,在开始时间后加上" 00:00:00"
	 * @param startTime
	 * @return
	 */
	public static Date getDayStart(Date startTime){
		String time = formatDate(startTime, "yyyy-MM-dd")+startTimes;
		return parseDate(time);
	}

	/**
	 *
	 * @author xuchang
	 * @Description 返回结束时间,在开始时间后加上" 23:59:59"
	 * @param endTime
	 * @return
	 */
	public static Date getDayEnd(Date endTime){
		String time = formatDate(endTime, "yyyy-MM-dd")+endTimes;
		return parseDate(time);
	}

	/**
	 *获取下个月的第一天或最后一天
	 * @param minOrMax 0:第一天 1:最后一天
	 * @return
	 */
	public static Date getPerFirstDayOfMonth(Integer minOrMax) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, minOrMax==0?calendar.getActualMinimum(Calendar.DAY_OF_MONTH):calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 获取 前/后 offset个月 的当前时间, 没有 30/31号 ,则自动转为最大日期
	 * @param offset		 偏移量, 正数为后, 负数为前
	 * @return
	 */
	public static Date getMonthOffset(int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, offset);
		return calendar.getTime();
	}

	/**
	 * 获取 前/后 offset个月 的当前时间
	 * @param offset		 偏移量, 正数为后, 负数为前
	 * @param maxDayOfMonth	 true:月最大日期; false: 月最小日期
	 * @return
	 */
	public static Date getMonthDateOffset(int offset, boolean maxDayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, offset);
		calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth ? calendar.getActualMaximum(Calendar.DAY_OF_MONTH) : calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 获取 前/后 offset个月, 最 大/小 日期的 最 早/晚 时间
	 * @param offset		 偏移量, 正数为后, 负数为前
	 * @param maxDayOfMonth	 true:月最大日期; false: 月最小日期
	 * @param maxTimeOfDay	 true:日最早时间; false: 日最晚时间
	 * @return
	 */
	public static Date getMonthDateTimeOffset(int offset, boolean maxDayOfMonth, boolean maxTimeOfDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, offset);
		calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth ? calendar.getActualMaximum(Calendar.DAY_OF_MONTH) : calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date date = calendar.getTime();
		return maxTimeOfDay ? getDayEnd(date) : getDayStart(date);
	}

	/**
	 * 获取指定日期 前/后 offset个月, 最 大/小 日期的 最 早/晚 时间
	 * @param date		 	 指定日期
	 * @param offset		 偏移量, 正数为后, 负数为前
	 * @param maxDayOfMonth	 true:月最大日期; false: 月最小日期
	 * @param maxTimeOfDay	 true:日最早时间; false: 日最晚时间
	 * @return
	 */
	public static Date getMonthDateTimeOffset(Date date, int offset, boolean maxDayOfMonth, boolean maxTimeOfDay) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, offset);
		calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth ? calendar.getActualMaximum(Calendar.DAY_OF_MONTH) : calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date dat = calendar.getTime();
		return maxTimeOfDay ? getDayEnd(dat) : getDayStart(dat);
	}


		public static void main(String[] args) {
	System.out.println(DateUtils.getDateBefores("2017-09-12", 2));
	}
}
