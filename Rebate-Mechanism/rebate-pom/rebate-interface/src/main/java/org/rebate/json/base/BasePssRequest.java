package org.rebate.json.base;

import java.math.BigDecimal;

public class BasePssRequest {

  /**
   * 用户token
   */
  private String token;
  /**
   * 用户ID
   */
  private Long userId;

  private Integer type;//pss类型

  private BigDecimal amount;//金额

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
