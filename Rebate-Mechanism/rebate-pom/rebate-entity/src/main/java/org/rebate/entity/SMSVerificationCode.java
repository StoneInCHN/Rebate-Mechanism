package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 短信验证码实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_sms_verification_code", indexes = {@Index(name = "cellPhoneNumIndex",
    columnList = "cellPhoneNum")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_sms_verification_code_sequence")
public class SMSVerificationCode extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 手机号
   */
  private String cellPhoneNum;

  /**
   * 验证码
   */
  private String smsCode;

  /** 短信验证码过期时间token */
  private String timeoutToken;



  @Column(length = 20, nullable = false)
  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  @Column(length = 6)
  public String getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(String smsCode) {
    this.smsCode = smsCode;
  }

  @Column(length = 100)
  public String getTimeoutToken() {
    return timeoutToken;
  }

  public void setTimeoutToken(String timeoutToken) {
    this.timeoutToken = timeoutToken;
  }


}
