package org.rebate.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import org.rebate.beans.Setting;


public class LatLonUtil {

  private static final double PI = 3.14159265;
  private static final double EARTH_RADIUS = 6378137;
  private static final double RAD = Math.PI / 180.0;

  /**
   * @param raidus 单位米 return minLat,minLng,maxLat,maxLng
   */
  public static double[] getAround(double lat, double lon, int raidus) {

    Double latitude = lat;
    Double longitude = lon;

    Double degree = (24901 * 1609) / 360.0;
    double raidusMile = raidus;

    Double dpmLat = 1 / degree;
    Double radiusLat = dpmLat * raidusMile;
    Double minLat = latitude - radiusLat;
    Double maxLat = latitude + radiusLat;

    Double mpdLng = degree * Math.cos(latitude * (PI / 180));
    Double dpmLng = 1 / mpdLng;
    Double radiusLng = dpmLng * raidusMile;
    Double minLng = longitude - radiusLng;
    Double maxLng = longitude + radiusLng;
    // System.out.println(&quot;[&quot;+minLat+&quot;,&quot;+minLng+&quot;,&quot;+maxLat+&quot;,&quot;+maxLng+&quot;]&quot;);
    return new double[] {minLat, minLng, maxLat, maxLng};
  }

  /**
   * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
   *
   * @param lng1
   * @param lat1
   * @param lng2
   * @param lat2
   * @return
   */
  public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
    double radLat1 = lat1 * RAD;
    double radLat2 = lat2 * RAD;
    double a = radLat1 - radLat2;
    double b = (lng1 - lng2) * RAD;
    double s =
        2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
            * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    s = s * EARTH_RADIUS;
    s = Math.round(s * 10000) / 10000;
    return s;
  }

  // lat是已知点的纬度，lon是已知点的经度
  // distance就是那个X公里范围,radius是地球半径，一般取平均半径6371km
  /**
   * @param distance 查找半径 单位米
   * @param lat 中心点纬度
   * @param lon 中心点经度 return minLat,minLng,maxLat,maxLng
   */
  public static double[] boundingCoordinates(double lat, double lon, double distance) {
    lat = lat * PI / 180;
    lon = lon * PI / 180; // 先换算成弧度
    double rad_dist = distance / EARTH_RADIUS; // 计算X公里在地球圆周上的弧度
    double lat_min = lat - rad_dist;
    double lat_max = lat + rad_dist; // 计算纬度范围

    double lon_min, lon_max;
    // 因为纬度在-90度到90度之间，如果超过这个范围，按情况进行赋值
    if (lat_min > -PI / 2 && lat_max < PI / 2) {
      // 开始计算经度范围
      double lon_t = Math.asin(Math.sin(rad_dist) / Math.cos(lat));
      lon_min = lon - lon_t;
      // 同理，经度的范围在-180度到180度之间
      if (lon_min < -PI)
        lon_min += 2 * PI;
      lon_max = lon + lon_t;
      if (lon_max > PI)
        lon_max -= 2 * PI;
    } else {
      lat_min = Math.max(lat_min, -PI / 2);
      lat_max = Math.min(lat_max, PI / 2);
      lon_min = -PI;
      lon_max = PI;
    }
    // 最后置换成角度进行输出
    lat_min = lat_min * 180 / PI;
    lat_max = lat_max * 180 / PI;
    lon_min = lon_min * 180 / PI;
    lon_max = lon_max * 180 / PI;
    double[] result = new double[] {lat_min, lon_min, lat_max, lon_max};
    return result;
  }

  /*
   * public static void main(String[] args) { Double lat1 = 30.555018; Double lon1 = 104.069338; int
   * radius = 5000; double[] result = getAround(lat1, lon1, radius); double[] result2 =
   * boundingCoordinates(lat1, lon1,radius) ; double dis = getDistance(104.55, 30.144, 105.44,
   * 31.144); for (double a :result) { System.out.print(a +" , "); } System.out.println(); for
   * (double a :result2) { System.out.print(a +" , "); }
   *//*
      * round( 6378.138*2*asin( sqrt( pow(sin( (30.144*pi()/180-31.144*pi()/180)/2),2) +
      * cos(30.144*pi()/180)*cos(31.144*pi()/180)*pow(sin( (104.55*pi()/180-105.44*pi()/180)/2),2) )
      * )*1000 )
      *//*
         * }
         */

  public static Map<String, Object> convertCoordinateForBaiDuLbs(String lon, String lat) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      Setting setting = SettingUtils.get();
      String url =
          setting.getBdCarConvert() + "coords=" + lon + "," + lat + "&from=3&to=5&ak="
              + setting.getBdCarMapAk();
      String res = ApiUtils.get(url);
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);
      if (resMap != null && (int) resMap.get("status") == 0) {
        List<Map<String, Object>> coords = (List<Map<String, Object>>) resMap.get("result");
        for (Map<String, Object> coord : coords) {
          map.put("lng", coord.get("x").toString());
          map.put("lat", coord.get("y").toString());
          return map;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Map<String, Object> convertCoordinate(String lon, String lat) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      Setting setting = SettingUtils.get();
      String url = setting.getConvertMapUrl() + "?from=0&to=4&x=" + lon + "&y=" + lat;
      String res = ApiUtils.get(url);
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);
      String x = resMap.get("x").toString();
      String y = resMap.get("y").toString();
      map.put("lng", new String(Base64.decodeBase64(x), "UTF-8"));
      map.put("lat", new String(Base64.decodeBase64(y), "UTF-8"));
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * 百度地址转坐标
   * 
   * @param address
   * @return
   */
  public static Map<String, Object> convertAddress(String address) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      Setting setting = SettingUtils.get();
      String url =
          setting.getConvertAddressUrl() + "?address=" + address + "&output=json&ak="
              + setting.getMapAk();
      String res = ApiUtils.get(url);
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);
      Map<String, Object> resultMap = (Map<String, Object>) resMap.get("result");
      Map<String, Object> locMap = (Map<String, Object>) resultMap.get("location");
      return locMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * 百度坐标转地址
   * 
   * @param lat
   * @param lng
   * @return
   */
  public static String convertCoorForAddr(String lat, String lng) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      Setting setting = SettingUtils.get();
      String url =
          setting.getConvertAddressUrl() + "?location=" + lat + "," + lng + "&output=json&ak="
              + setting.getMapAk();
      String res = ApiUtils.get(url);
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);
      Map<String, Object> resultMap = (Map<String, Object>) resMap.get("result");
      String addr = (String) resultMap.get("formatted_address");
      return addr;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
