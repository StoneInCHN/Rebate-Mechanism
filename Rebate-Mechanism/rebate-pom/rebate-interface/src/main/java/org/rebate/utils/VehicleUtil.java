package org.rebate.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import org.rebate.beans.Setting;


public class VehicleUtil {

  private static double avgJS = 0.110198205D;
  private static double avgSC = 0.307525734D;
  private static double avgZW = 0.194117877D;

  public static List<List<Map<String, Object>>> getVehicleLineByCode(
      List<Map<String, Object>> vehicleLineMap) {
    String[] codes =
        {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};
    List<List<Map<String, Object>>> lineMaps = new ArrayList<List<Map<String, Object>>>();
    for (String code : codes) {
      List<Map<String, Object>> lineMap = new ArrayList<Map<String, Object>>();
      for (Map<String, Object> map : vehicleLineMap) {
        if (code.equals(map.get("code"))) {
          lineMap.add(map);
        }
      }
      lineMaps.add(lineMap);
    }
    return lineMaps;
  }

  /**
   * 计算驾驶得分
   * 
   * @param js 急加速次数
   * @param sc 急刹车次数
   * @param zw 急转弯次数
   * @param pl 疲劳驾驶次数
   * @param mile 里程
   * @return
   */
  public static Integer getScore(Integer js, Integer sc, Integer zw, Integer pl, Double mile) {
    if (mile <= 0) {
      return -1;
    }
    int score = 100;
    if (pl > 0) {
      score -= 25;
    }
    if (js / mile > avgJS) {
      double out = js / mile - avgJS;
      int percent = (int) (out * 100.0D / avgJS);
      if (percent >= 25)
        score -= 25;
      else {
        score -= percent;
      }
    }
    if (sc / mile > avgSC) {
      double out = sc / mile - avgSC;
      int percent = (int) (out * 100.0D / avgSC);
      if (percent >= 25)
        score -= 25;
      else {
        score -= percent;
      }
    }
    if (zw / mile > avgZW) {
      double out = zw / mile - avgZW;
      int percent = (int) (out * 100.0D / avgZW);
      if (percent >= 25)
        score -= 25;
      else {
        score -= percent;
      }
    }
    if (score == 100) {
      score = 99;
    }
    return score;
  }

  public static String getWeather4CashCar(String location) {
    try {
      Setting setting = SettingUtils.get();
      String pyUrl =
          setting.getBdApiStoreHz2PyUrl() + "?words=" + location
              + "&accent=0&traditional=0&letter=0&oc=0&type=json";

      String res = ApiUtils.get(pyUrl, setting.getBdApiStoreKey());
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);
      if (resMap.get("py") != null) {
        String pyLocation = resMap.get("py").toString().replace(" ", "");
        String cashUrl =
            setting.getBdApiStoreWeatherUrl() + "?location=" + pyLocation + "&language=zh-Hans";

        String cashRes = ApiUtils.get(cashUrl, setting.getBdApiStoreKey());
        ObjectMapper cashMapper = new ObjectMapper();
        Map<String, Object> cashMap =
            (Map<String, Object>) cashMapper.readValue(cashRes, Map.class);
        if (cashMap != null) {
          List<Map<String, Object>> resultMap = (List<Map<String, Object>>) cashMap.get("results");
          if (resultMap != null && resultMap.size() > 0) {
            Map<String, Object> suggestionMap =
                (Map<String, Object>) resultMap.get(0).get("suggestion");
            Map<String, Object> washingMap = (Map<String, Object>) suggestionMap.get("car_washing");
            String brief = washingMap.get("brief").toString();
            return brief + "洗车";
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;

  }
}
