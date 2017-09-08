package org.rebate.json.request;

import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.json.base.BaseRequest;


public class PushMsgRequest extends BaseRequest {

  /**
   * 推送regId
   */
  private String regId;

  /**
   * 推送内容
   */
  private String alert;

  /**
   * 手机平台
   */
  private AppPlatform appPlatform;

  /**
   * 推送消息类型 0：商家 1：推荐好友 2：业务员 3：代理商
   */
  private String type;

  /**
   * 消息开关
   */
  private Boolean msgSwitch;


  public Boolean getMsgSwitch() {
    return msgSwitch;
  }

  public void setMsgSwitch(Boolean msgSwitch) {
    this.msgSwitch = msgSwitch;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRegId() {
    return regId;
  }

  public void setRegId(String regId) {
    this.regId = regId;
  }

  public String getAlert() {
    return alert;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public AppPlatform getAppPlatform() {
    return appPlatform;
  }

  public void setAppPlatform(AppPlatform appPlatform) {
    this.appPlatform = appPlatform;
  }


}
