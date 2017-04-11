package org.rebate.json.request;

import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.json.base.BaseRequest;

public class SettingConfigRequest extends BaseRequest {

  /**
   * 版本序列号
   */
  private Integer versionCode;

  /**
   * jpush推送用户注册id
   */
  private String jpushRegId;

  /**
   * 用户手机平台
   */
  private AppPlatform appPlatform;


  public AppPlatform getAppPlatform() {
    return appPlatform;
  }

  public void setAppPlatform(AppPlatform appPlatform) {
    this.appPlatform = appPlatform;
  }

  public Integer getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(Integer versionCode) {
    this.versionCode = versionCode;
  }

  public String getJpushRegId() {
    return jpushRegId;
  }

  public void setJpushRegId(String jpushRegId) {
    this.jpushRegId = jpushRegId;
  }

}
