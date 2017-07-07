package org.rebate.json.response;

import java.math.BigDecimal;


public class DiscountAmount {
  /**
   * 折扣
   */
  private BigDecimal discount;
  /**
   * 交易额
   */
  private BigDecimal amount;
  
  public BigDecimal getDiscount() {
	return discount;
  }
  public void setDiscount(BigDecimal discount) {
	this.discount = discount;
  }
  public BigDecimal getAmount() {
	return amount;
  }
  public void setAmount(BigDecimal amount) {
	this.amount = amount;
  }
  

}
