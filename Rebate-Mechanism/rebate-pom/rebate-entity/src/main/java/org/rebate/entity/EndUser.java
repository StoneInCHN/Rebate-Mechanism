package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 终端用户实体
 * 
 * @author shijun
 *
 */

@Entity
@Table(name = "rm_end_user")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_end_user_sequence")
public class EndUser extends BaseEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 手机号
   */
  private String cellPhoneNum;

  /**
   * 登录密码
   */
  private String loginPwd;

  /**
   * 支付密码
   */
  private String paymentPwd;

  /**
   * 推荐人
   */
  private String recommender;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public String getLoginPwd() {
    return loginPwd;
  }

  public void setLoginPwd(String loginPwd) {
    this.loginPwd = loginPwd;
  }

  public String getPaymentPwd() {
    return paymentPwd;
  }

  public void setPaymentPwd(String paymentPwd) {
    this.paymentPwd = paymentPwd;
  }

  public String getRecommender() {
    return recommender;
  }

  public void setRecommender(String recommender) {
    this.recommender = recommender;
  }
}
