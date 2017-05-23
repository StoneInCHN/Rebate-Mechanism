package org.rebate.beans;

import java.io.Serializable;


/**
 * 银行卡四元素检测结果
 * 
 *
 */
public class VerifyBankcardBean implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 返回码
   */
  private int error_code;
  /**
   * 返回码描述
   */
  private String reason;
  /**
   * 验证结果码
   */
  private String resultcode;
  
  /**
   * 验证结果
   */
  private VerifyBankcardResult result;

  public int getError_code() {
    return error_code;
  }

  public void setError_code(int error_code) {
    this.error_code = error_code;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public VerifyBankcardResult getResult() {
    return result;
  }

  public void setResult(VerifyBankcardResult result) {
    this.result = result;
  }

  public String getResultcode() {
    return resultcode;
  }

  public void setResultcode(String resultcode) {
    this.resultcode = resultcode;
  }

}
