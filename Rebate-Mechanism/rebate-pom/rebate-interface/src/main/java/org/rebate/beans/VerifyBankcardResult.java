package org.rebate.beans;

import java.io.Serializable;


/**
 * 银行卡四元素检测结果
 * 
 *
 */
public class VerifyBankcardResult implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 本次查询流水号
   */
  private String jobid;
  /**
   * 姓名
   */
  private String realname;
  /**
   * 银行卡卡号
   */
  private String bankcard;
  /**
   * 身份证号码
   */
  private String idcard;
  /**
   * 手机号码
   */
  private String mobile;
  /**
   * 匹配结果，1:匹配 2:不匹配
   */
  private String res;
  /**
   * 匹配结果描述
   */
  private String message;
  
  public String getJobid() {
    return jobid;
  }
  public void setJobid(String jobid) {
    this.jobid = jobid;
  }
  public String getRealname() {
    return realname;
  }
  public void setRealname(String realname) {
    this.realname = realname;
  }
  public String getBankcard() {
    return bankcard;
  }
  public void setBankcard(String bankcard) {
    this.bankcard = bankcard;
  }
  public String getIdcard() {
    return idcard;
  }
  public void setIdcard(String idcard) {
    this.idcard = idcard;
  }
  public String getMobile() {
    return mobile;
  }
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  public String getRes() {
    return res;
  }
  public void setRes(String res) {
    this.res = res;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  





}
