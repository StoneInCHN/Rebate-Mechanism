package org.rebate.utils;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import org.rebate.beans.Setting;

public class TelematicsUtils {

  public static Setting setting = SettingUtils.get();

  public static List<Map<String, Object>> baiduTelematics(String lon, String lat, String keyWord) {
    String location = lon + "," + lat;
    String url =
        setting.getBdCarMapUrl() + "?location=" + location + "&keyWord=" + keyWord + "&number="
            + setting.getBdNumber() + "page=" + setting.getBdPage() + "&output=json&ak="
            + setting.getBdCarMapAk() + "&mcode=" + setting.getBdCarMcode();
    String res = ApiUtils.get(url);

    try {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);
      if (resMap.get("pointList") == null || resMap.get("pointList").equals("")) {
        return null;
      }
      List<Map<String, Object>> listMap = (List<Map<String, Object>>) resMap.get("pointList");
      for (Map<String, Object> map : listMap) {
        Map<String, Object> locationMap = (Map<String, Object>) map.get("location");
        locationMap =
            LatLonUtil.convertCoordinateForBaiDuLbs(locationMap.get("lng").toString(), locationMap
                .get("lat").toString());
        map.put("location", locationMap);
      }
      return listMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
