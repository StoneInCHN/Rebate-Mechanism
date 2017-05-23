package org.rebate.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.rebate.beans.Setting;

public class ToolsUtils {


  public static String sendReq(String smsUrl, String parameters) {

    StringBuffer response = new StringBuffer();
    try {
      if (LogUtil.isDebugEnabled(ApiUtils.class)) {
        LogUtil.debug(ApiUtils.class, "Request API URL is : %s", smsUrl + parameters);
      }

      URL url = new URL(smsUrl);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      // add reuqest header
      con.setRequestMethod("GET");
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.write(parameters.getBytes());
      // wr.writeBytes(parameters);
      wr.flush();
      wr.close();

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    if (LogUtil.isDebugEnabled(ApiUtils.class)) {
      LogUtil.debug(ApiUtils.class, "Response", "Response from API is : %s", response.toString());
    }
    return response.toString();
  }

  public static String sendSmsMsg(String mobile, String msg) {
    try {
      Setting setting = SettingUtils.get();
      String smsUrl = setting.getSmsUrl();
      String smsOrgId = setting.getSmsOrgId();
      String smsUserName = setting.getSmsUserName();
      String smsPwd = setting.getSmsPwd();
      String message = URLEncoder.encode(msg, "UTF-8");
      String url =
          "Id=" + smsOrgId + "&Name=" + smsUserName + "&Psw=" + smsPwd + "&Message=" + message
              + "&Phone=" + mobile + "&Timestamp=0";
      String rs = sendReq(smsUrl, url);
      return rs;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }



}
