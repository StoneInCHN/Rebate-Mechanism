package org.rebate.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author sujinxuan
 *
 */
public class TimeUtils {

  private static final SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

  /**
   * 格式化时间
   * 
   * @param sdf 格式化时间格式:yyyyMMdd
   * @param time 时间戳
   * @return
   */
  public static String format(String format, long time) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date d = new Date(time);
    return sdf.format(d);
  }


  public static Date convertStr2Date(String dateStr) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      return sdf.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getLongDateStr(Date date) {
    return longDateFormat.format(date);
  }

  /**
   * 格式化日期 yyyy-MM-dd 00:00:00
   * 
   * @param date
   * @return
   */
  public static Date formatDate2Day(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * 计算两个日期之间相差的天数
   * 
   * @param smdate 较小的时间
   * @param bdate 较大的时间
   * @return 相差天数
   * @throws ParseException
   */
  public static int daysBetween(Date smdate, Date bdate) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try {
      smdate = sdf.parse(sdf.format(smdate));
      bdate = sdf.parse(sdf.format(bdate));
    } catch (ParseException e) {
      return 0;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(smdate);
    long time1 = cal.getTimeInMillis();
    cal.setTime(bdate);
    long time2 = cal.getTimeInMillis();
    long between_days = (time2 - time1) / (1000 * 3600 * 24);

    return Integer.parseInt(String.valueOf(between_days));
  }

  /**
   * 加天数
   * 
   * @return
   */
  public static Date addDays(Integer days, Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, days);

    return cal.getTime();
  }

}
