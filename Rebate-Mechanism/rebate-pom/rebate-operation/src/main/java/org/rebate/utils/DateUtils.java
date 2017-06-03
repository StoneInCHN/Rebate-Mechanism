package org.rebate.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

@Component("dateUtils")
@Lazy(false)
public final class DateUtils implements ApplicationContextAware, DisposableBean {

  private static ApplicationContext applicationContext;

  private static final SimpleDateFormat detailsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static final SimpleDateFormat dateAndMinuteFormat = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm");
  private static final SimpleDateFormat hourAndMinuteFormat = new SimpleDateFormat("hh:mm");
  private static final SimpleDateFormat dateToNumberFormat = new SimpleDateFormat("yyyyMMddHHmmss");

  private DateUtils() {}

  public void destroy() throws Exception {
    applicationContext = null;
  }

  public void setApplicationContext(ApplicationContext context) throws BeansException {
    DateUtils.applicationContext = context;

  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static <T> T getBean(String name, Class<T> type) {
    Assert.hasText(name);
    Assert.notNull(type);
    return applicationContext.getBean(name, type);
  }

  public static <T> T getBean(Class<T> type) {
    Assert.notNull(type);
    return applicationContext.getBean(type);
  }

  public static String getMessage(String code, Object... args) {
    LocaleResolver localeResolver = getBean("localeResolver", LocaleResolver.class);
    Locale locale = localeResolver.resolveLocale(null);
    return applicationContext.getMessage(code, args, locale);
  }


  public static String getUUID() {
    return UUID.randomUUID().toString();
  }

  public static String genConsumedListId() {
    return dateToNumberFormat.format(new Date());
  }

  /**
   * generate date by timeString: hh:mm:ss
   * 
   * @param timeString
   * @return
   * @throws ParseException
   */
  public static Date genDate(String timeString) throws ParseException {
    String str_date = simpleFormat.format(new Date());
    return detailsFormat.parse(str_date + " " + timeString);
  }

  public static String genDateString(Date date) {
    // Calendar中1-星期天，2-星期一，3-星期二，4-星期三，5-星期四，6-星期五，7-星期六
    String[] week = {"", "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    String str = simpleFormat.format(date);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    String weekday = week[calendar.get(Calendar.DAY_OF_WEEK)];
    return str + " " + weekday;

  }

  /**
   * format date to hh:mm
   * 
   * @param date
   * @return
   */
  public static String genTimeString(Date date) {
    return hourAndMinuteFormat.format(date);

  }

  /**
   * format date to yyyy-mm-dd
   * 
   * @param date
   * @return
   */
  public static String genDateStringForDate(Date date) {
    return simpleFormat.format(date);

  }

  /**
   * format date to yyyy-MM-dd hh:mm
   * 
   * @param date
   * @return
   */
  public static String genDateStringForMinutes(Date date) {
    return dateAndMinuteFormat.format(date);

  }

  /**
   * format yyyy-MM-dd to date object
   * 
   * @param dateStr
   * @return
   */
  public static Date formatDate(String dateStr) {
    if (StringUtils.isEmpty(dateStr)) {
      return null;
    }
    try {
      return simpleFormat.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
  /**
   * 获取一天的开始时间
   * day为整数，表示在date的基础上加day天，day为负数，表示在date的基础上减day天
   * @param now
   * @return
   */
  public static Date startOfDay(Date date, int day){
    if (date == null) {
    	date = new Date();
    }
    Calendar startTime = Calendar.getInstance();
    startTime.setTime(date);
    startTime.add(Calendar.DATE, day);
    startTime.set(Calendar.HOUR_OF_DAY, 0);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);
    return startTime.getTime();
  }
  /**
   * 获取一天的结束时间
   * day为整数，表示在date的基础上加day天，day为负数，表示在date的基础上减day天
   * @param now
   * @return
   */
  public static Date endOfDay(Date date, int day){
    if (date == null) {
    	date = new Date();
    }
    Calendar endTime = Calendar.getInstance();
    endTime.setTime(date);
    endTime.add(Calendar.DATE, day);
    endTime.set(Calendar.HOUR_OF_DAY, 23);
    endTime.set(Calendar.MINUTE, 59);
    endTime.set(Calendar.SECOND, 59);
    endTime.set(Calendar.MILLISECOND, 999);
    return endTime.getTime();
  }
}
