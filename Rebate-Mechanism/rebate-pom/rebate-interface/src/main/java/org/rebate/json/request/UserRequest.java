package org.rebate.json.request;

import org.rebate.entity.commonenum.CommonEnum.SmsCodeType;
import org.rebate.json.base.BaseRequest;
import org.springframework.web.multipart.MultipartFile;


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

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 用户所在地区ID
   */
  private Long areaId;

  /**
   * 验证码类型
   */
  private SmsCodeType smsCodeType;

  /**
   * 用户头像
   */
  private MultipartFile photo;

  public MultipartFile getPhoto() {
    return photo;
  }

  public void setPhoto(MultipartFile photo) {
    this.photo = photo;
  }

  public SmsCodeType getSmsCodeType() {
    return smsCodeType;
  }

  public void setSmsCodeType(SmsCodeType smsCodeType) {
    this.smsCodeType = smsCodeType;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public Long getAreaId() {
    return areaId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }

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
