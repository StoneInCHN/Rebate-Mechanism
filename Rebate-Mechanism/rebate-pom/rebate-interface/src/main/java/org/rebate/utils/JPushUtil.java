package org.rebate.utils;

import java.util.HashMap;
import java.util.Map;

import org.rebate.beans.Setting;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;


public class JPushUtil {

  public static Setting setting = SettingUtils.get();

  public static PushResult sendPush(PushPayload payload, String masterSecret, String appKey)
      throws Exception {
    JPushClient client;
    try {
      if (masterSecret == null && appKey == null) {
        masterSecret = setting.getMasterSecret();
        appKey = setting.getAppKey();
      }
      client = new JPushClient(masterSecret, appKey);
      return client.sendPush(payload);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      client = null;
    }
  }

  /**
   * 推送到所有设备
   * 
   * @param alert 内容（72个字符）
   * @return
   * @throws Exception
   */
  public static PushPayload buildPushObject(String alert) throws Exception {
    return PushPayload.alertAll(alert);
  }

  /**
   * 推送到IOS设备
   * 
   * @param alert 内容
   * @return
   */
  public static PushPayload buildPushObject_ios(String alert) {
    return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.all())
        .setNotification(Notification.ios(alert, null)).build();
  }

  /**
   * 推送到Android设备
   * 
   * @param alert 内容
   * @return
   */
  public static PushPayload buildPushObject_android(String alert) {
    return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.all())
        .setNotification(Notification.android(alert, null, null)).build();
  }

  /**
   * 推送到设备标签为tag的IOS设备
   * 
   * @param alert 内容
   * @param tag 标签
   * @return
   */
  public static PushPayload buildPushObject_ios_tag(String alert, String... tag) {
    return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.tag(tag))
        .setNotification(Notification.ios(alert, null)).build();
  }

  /**
   * 推送到设备标签为tag的Android设备
   * 
   * @param alert 内容
   * @param tag 标签
   * @return
   */
  public static PushPayload buildPushObject_android_tag(String alert, String... tag) {
    return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.tag(tag))
        .setNotification(Notification.android(alert, null, null)).build();
  }

  /**
   * 推送到别名为alias的IOS设备
   * 
   * @param alert 内容
   * @param alias 别名
   * @return
   */
  public static PushPayload buildPushObject_ios_alias(String alert, String... alias) {
    return buildPushObject_ios_alias(alert, null, alias);
  }

  /**
   * 推送到别名为alias的Android设备
   * 
   * @param alert 内容
   * @param alias 别名
   * @return
   */
  public static PushPayload buildPushObject_android_alias(String alert, String... alias) {
    return buildPushObject_android_alias(alert, null, alias);
  }

  /**
   * 推送到别名为alias的IOS设备，附带数据为extras
   * 
   * @param alert 内容
   * @param extras Map对象
   * @param alias 别名
   * @return
   */
  public static PushPayload buildPushObject_ios_alias(String alert, Map<String, String> extras,
      String... alias) {
    return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.alias(alias))
        .setNotification(Notification.ios(alert, extras)).build();
  }

  /**
   * 推送到注册ID为registerId的IOS设备，附带数据为extras
   * 
   * @param alert 内容
   * @param extras Map对象
   * @param alias 别名
   * @return
   */
  public static PushPayload buildPushObject_ios_registerId(String alert,
      Map<String, String> extras, String... registrationIds) {
    // ios推送生产环境setApnsProduction(true)
    // return PushPayload.newBuilder().setPlatform(Platform.ios())
    // .setAudience(Audience.registrationId(registrationIds))
    // .setNotification(Notification.ios(alert, extras))
    // .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
    // ios推送开发环境
    return PushPayload.newBuilder().setPlatform(Platform.ios())
        .setAudience(Audience.registrationId(registrationIds))
        .setNotification(Notification.ios(alert, extras)).build();
  }

  /**
   * 推送到别名为alias的Android设备，附带数据为extras
   * 
   * @param alert 内容
   * @param extras Map对象
   * @param alias 别名
   * @return
   */
  public static PushPayload buildPushObject_android_alias(String alert, Map<String, String> extras,
      String... alias) {
    return PushPayload.newBuilder().setPlatform(Platform.android())
        .setAudience(Audience.alias(alias))
        .setNotification(Notification.android(alert, null, extras)).build();
  }

  /**
   * 推送到注册ID为registerId的Android设备，附带数据为extras
   * 
   * @param alert 内容
   * @param extras Map对象
   * @param alias 别名
   * @return
   */
  public static PushPayload buildPushObject_android_registerId(String alert,
      Map<String, String> extras, String... registrationIds) {
    return PushPayload.newBuilder().setPlatform(Platform.android())
        .setAudience(Audience.registrationId(registrationIds))
        .setNotification(Notification.android(alert, null, extras)).build();
  }

  public static void main(String[] args) throws Exception {
    // PushPayload payload = JPushUtil.buildPushObject("推送广播，推送到所有客户端");
    // PushPayload payload = JPushUtil.buildPushObject_android("推送广播，推送到Android客户端");
    // // PushPayload payload = JPushUtil.buildPushObject_android_tag("推送广播，推送到指定Tag客户端", "重庆");
    Map<String, String> map = new HashMap<String, String>();
    map.put("id", "我是ID");
    map.put("title", "我是title");
    String[] regIds = {"18071adc0337926978c"};
    PushPayload payload =
        JPushUtil.buildPushObject_android_registerId("推送广播，推送到指定regId设备", map, regIds);// 100d85590944b10139b
    // PushPayload payload =
    // JPushUtil.buildPushObject_ios_registerId("推送广播，推送到指定regId设备", map, "13165ffa4e320612bdc");//
    JPushUtil.sendPush(payload, setting.getMasterSecret(), setting.getAppKey());

  }
}
