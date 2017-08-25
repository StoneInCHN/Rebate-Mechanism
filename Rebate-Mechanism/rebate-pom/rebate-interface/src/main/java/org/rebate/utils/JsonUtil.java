package org.rebate.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * 
 * 功能描述： 处理json的工具类，负责json数据转换成java对象和java对象转换成json<br>
 */

public class JsonUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

  /**
   * 从一个JSON 对象字符格式中得到一个java对象
   * 
   * @param jsonString
   * @param pojoCalss
   * @return
   */
  public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
    Object pojo;
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    pojo = JSONObject.toBean(jsonObject, pojoCalss);
    return pojo;
  }


  public static List<Map<String, Object>> getMapList4JsonStr(String json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      List<Map<String, Object>> map = objectMapper.readValue(json, List.class);
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 从json HASH表达式中获取一个map，改map支持嵌套功能
   * 
   * @param jsonString
   * @return
   */
  public static Map getMap4Json(String jsonString) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    Iterator keyIter = jsonObject.keys();
    String key;
    Object value;
    Map valueMap = new HashMap();
    while (keyIter.hasNext()) {
      key = (String) keyIter.next();
      value = jsonObject.get(key);
      valueMap.put(key, value);
    }
    return valueMap;
  }

  /**
   * 将一个java对象转换为object对象
   */
  public static Map getMap4Object(Object object) {
    String josn = getJsonString4JavaPOJO(object);
    return getMap4Json(josn);
  }

  /**
   * 从json数组中得到相应java数组
   * 
   * @param jsonString
   * @return
   */
  public static Object[] getObjectArray4Json(String jsonString) {
    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    return jsonArray.toArray();
  }

  /**
   * 从json对象集合表达式中得到一个java对象列表
   * 
   * @param jsonString
   * @param pojoClass
   * @return
   */
  public static List getList4Json(String jsonString, Class pojoClass) {
    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    JSONObject jsonObject;
    Object pojoValue;
    List list = new ArrayList();
    for (int i = 0; i < jsonArray.size(); i++) {
      jsonObject = jsonArray.getJSONObject(i);
      pojoValue = JSONObject.toBean(jsonObject, pojoClass);
      list.add(pojoValue);
    }
    return list;
  }

  /**
   * 从json数组中解析出java字符串数组
   * 
   * @param jsonString
   * @return
   */
  public static String[] getStringArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    String[] stringArray = new String[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      stringArray[i] = jsonArray.getString(i);

    }

    return stringArray;
  }

  /**
   * 从json数组中解析出javaLong型对象数组
   * 
   * @param jsonString
   * @return
   */
  public static Long[] getLongArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    Long[] longArray = new Long[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      longArray[i] = jsonArray.getLong(i);

    }
    return longArray;
  }

  /**
   * 从json数组中解析出java Integer型对象数组
   * 
   * @param jsonString
   * @return
   */
  public static Integer[] getIntegerArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    Integer[] integerArray = new Integer[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      integerArray[i] = jsonArray.getInt(i);

    }
    return integerArray;
  }

  /**
   * 从json数组中解析出java Integer型对象数组
   * 
   * @param jsonString
   * @return
   */
  public static Double[] getDoubleArray4Json(String jsonString) {

    JSONArray jsonArray = JSONArray.fromObject(jsonString);
    Double[] doubleArray = new Double[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      doubleArray[i] = jsonArray.getDouble(i);

    }
    return doubleArray;
  }

  /**
   * 将java对象转换成json字符串
   * 
   * @param javaObj
   * @return
   */
  public static String getJsonString4JavaPOJO(Object javaObj) {
    JSONObject json;
    json = JSONObject.fromObject(javaObj);
    return json.toString();

  }

  public static String getJsonString4JavaArray(Object[] objects) {
    JSONArray jsonArray;
    jsonArray = JSONArray.fromObject(objects);
    return jsonArray.toString();
  }

  /**
   * 向页面回去data字符串
   */
  public static void returnJsonStringData(HttpServletResponse response, String data) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (IOException e) {
      // LOGGER.error(LogUtil.debug("IOException", "500", "", "", e.getMessage()),e);
    }
    out.write(data);
    out.close();
  }

  /**
   * 将list转换为json字符串
   */
  public static String getJsonArray4JavaList(List list) {
    JSONArray jsonArray;
    jsonArray = JSONArray.fromObject(list);
    String jsonArrayStr = jsonArray.toString();
    jsonArrayStr = jsonArrayStr.substring(jsonArrayStr.indexOf("["));
    return jsonArray.toString();
  }

}
