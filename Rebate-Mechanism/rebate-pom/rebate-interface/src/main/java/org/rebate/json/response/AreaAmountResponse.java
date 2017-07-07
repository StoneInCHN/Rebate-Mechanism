package org.rebate.json.response;

import java.math.BigDecimal;
import java.util.List;


public class AreaAmountResponse {
  /**
   * 地区ID
   */
  private Long areaId;
  /**
   * 日期 yyyy-MM-dd
   */
  private String date;
  /**
   * 当天总金额
   */
  private BigDecimal totalAmount;
  /**
   * 交易额折扣
   */
  private List<DiscountAmount> discountAmounts;
  
  
  public Long getAreaId() {
	return areaId;
  }
  public void setAreaId(Long areaId) {
	this.areaId = areaId;
  }

  public String getDate() {
	return date;
  }
  public void setDate(String date) {
	this.date = date;
  }
  public List<DiscountAmount> getDiscountAmounts() {
	return discountAmounts;
  }
  public void setDiscountAmounts(List<DiscountAmount> discountAmounts) {
	this.discountAmounts = discountAmounts;
  }
  public BigDecimal getTotalAmount() {
	return totalAmount;
  }
  public void setTotalAmount(BigDecimal totalAmount) {
	this.totalAmount = totalAmount;
  }

  
}
