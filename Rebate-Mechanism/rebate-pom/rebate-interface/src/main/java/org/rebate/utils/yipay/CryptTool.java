package org.rebate.utils.yipay;

import java.util.Calendar;

/**
 * CryptTool 封装了一些加密工具方法, 包括 3DES, MD5 等.
 * 
 * @author hxq
 * @version 1.0 2006-01-10
 */
public class CryptTool {

  public CryptTool() {}

  private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
      "B", "C", "D", "E", "F"};

  /**
   * 转换字节数组为16进制字串
   * 
   * @param b 字节数组
   * @return 16进制字串
   */

  public static String byteArrayToHexString(byte[] b) {
    StringBuffer resultSb = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      resultSb.append(byteToHexString(b[i]));
    }
    return resultSb.toString().toUpperCase();
  }

  private static String byteToHexString(byte b) {
    int n = b;
    if (n < 0)
      n = 256 + n;
    int d1 = n / 16;
    int d2 = n % 16;
    return hexDigits[d1] + hexDigits[d2];
  }


  /**
   * MD5 摘要计算(byte[]).
   * 
   * @param src byte[]
   * @throws Exception
   * @return byte[] 16 bit digest
   */
  public static byte[] md5Digest(byte[] src) throws Exception {
    java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5"); // MD5 is 16
                                                                                      // bit message
                                                                                      // digest

    return alg.digest(src);
  }

  /**
   * MD5 摘要计算(String).
   * 
   * @param src String
   * @throws Exception
   * @return String
   */
  public static String md5Digest(String src) throws Exception {
    return byteArrayToHexString(md5Digest(src.getBytes()));
  }

  // 取得现在的日期，格式yyyymmddhhmmss
  public static String getCurrentDate() {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(cal.YEAR);
    int month = cal.get(cal.MONTH) + 1;
    int day = cal.get(cal.DAY_OF_MONTH);
    int hour = cal.get(cal.HOUR_OF_DAY);
    int minute = cal.get(cal.MINUTE);
    int second = cal.get(cal.SECOND);
    String cDate = Integer.toString(year);
    if (month < 10) {
      cDate = cDate + "0" + Integer.toString(month);
    } else {
      cDate = cDate + Integer.toString(month);
    }
    if (day < 10) {
      cDate = cDate + "0" + Integer.toString(day);
    } else {
      cDate = cDate + Integer.toString(day);
    }
    if (hour < 10) {
      cDate = cDate + "0" + Integer.toString(hour);
    } else {
      cDate = cDate + Integer.toString(hour);
    }
    if (minute < 10) {
      cDate = cDate + "0" + Integer.toString(minute);
    } else {
      cDate = cDate + Integer.toString(minute);
    }
    if (second < 10) {
      cDate = cDate + "0" + Integer.toString(second);
    } else {
      cDate = cDate + Integer.toString(second);
    }
    return cDate.trim();
  }

  // 取得当前日期,格式yyyymmdd
  public static String getTodayDate2() {
    // 初始化时间
    Calendar RightNow = Calendar.getInstance();
    return changeDatetoString2(RightNow);
  }

  // 将日期转换成字符串,格式yyyymmdd
  public static String changeDatetoString2(Calendar cDate) {
    int Year;
    int Month;
    int Day;
    String sDate = "";

    // 初始化时间
    Year = cDate.get(Calendar.YEAR);
    Month = cDate.get(Calendar.MONTH) + 1;
    Day = cDate.get(Calendar.DAY_OF_MONTH);

    sDate = Integer.toString(Year);
    if (Month >= 10) {
      sDate = sDate + Integer.toString(Month);
    } else {
      sDate = sDate + "0" + Integer.toString(Month);
    }
    if (Day >= 10) {
      sDate = sDate + Integer.toString(Day);
    } else {
      sDate = sDate + "0" + Integer.toString(Day);
    }
    return sDate;
  }

}
