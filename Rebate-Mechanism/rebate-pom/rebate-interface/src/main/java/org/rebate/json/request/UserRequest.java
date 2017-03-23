package org.rebate.json.request;

import org.rebate.json.base.BaseRequest;


public class UserRequest extends BaseRequest {

  /**
   * 登录密码
   */
  private String password;

  /**
   * 注册时的确认密码
   */
  private String password_confirm;

  /**
   * 验证码登录
   */
  private String smsCode;

  /**
   * app初始化SDK后生成的client id
   */
  private String appClientId;

  /**
   * 推荐人手机号
   */
  private String recommenderMobile;

  public String getRecommenderMobile() {
    return recommenderMobile;
  }

  public void setRecommenderMobile(String recommenderMobile) {
    this.recommenderMobile = recommenderMobile;
  }

  public String getPassword_confirm() {
    return password_confirm;
  }

  public void setPassword_confirm(String password_confirm) {
    this.password_confirm = password_confirm;
  }

  public String getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(String smsCode) {
    this.smsCode = smsCode;
  }

  public String getAppClientId() {
    return appClientId;
  }

  public void setAppClientId(String appClientId) {
    this.appClientId = appClientId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }



}
