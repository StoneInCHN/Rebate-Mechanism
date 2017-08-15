package org.rebate.json.request;

import java.math.BigDecimal;

import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.RebateType;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.entity.commonenum.CommonEnum.SmsCodeType;
import org.rebate.json.base.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest extends BaseRequest {

  /**
   * 提现金额
   */
  private BigDecimal withdrawAmount;
  /**
   * 密码
   */
  private String password;

  /**
   * 确认密码
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

  private Boolean isFavoriteAdd;
  /**
   * 用户头像
   */
  private MultipartFile photo;

  /**
   * 设置KEY
   */
  private SettingConfigKey configKey;

  /**
   * 备注
   */
  private String remark;

  /**
   * 微信授权openid
   */
  private String openId;

  /**
   * 微信账号昵称
   */
  private String wxNickName;

  /**
   * 乐分记录类型
   */
  private LeScoreType leScoreType;

  /**
   * 转账类型
   */
  private RebateType transType;

  /**
   * 额度
   */
  private BigDecimal amount;

  /**
   * 转账接收人手机号
   */
  private String receiverMobile;


  public String getReceiverMobile() {
    return receiverMobile;
  }

  public void setReceiverMobile(String receiverMobile) {
    this.receiverMobile = receiverMobile;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public RebateType getTransType() {
    return transType;
  }

  public void setTransType(RebateType transType) {
    this.transType = transType;
  }

  public BigDecimal getWithdrawAmount() {
    return withdrawAmount;
  }

  public void setWithdrawAmount(BigDecimal withdrawAmount) {
    this.withdrawAmount = withdrawAmount;
  }

  public LeScoreType getLeScoreType() {
    return leScoreType;
  }

  public void setLeScoreType(LeScoreType leScoreType) {
    this.leScoreType = leScoreType;
  }

  public String getWxNickName() {
    return wxNickName;
  }

  public void setWxNickName(String wxNickName) {
    this.wxNickName = wxNickName;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public SettingConfigKey getConfigKey() {
    return configKey;
  }

  public void setConfigKey(SettingConfigKey configKey) {
    this.configKey = configKey;
  }

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

  public Boolean getIsFavoriteAdd() {
    return isFavoriteAdd;
  }

  public void setIsFavoriteAdd(Boolean isFavoriteAdd) {
    this.isFavoriteAdd = isFavoriteAdd;
  }

}
