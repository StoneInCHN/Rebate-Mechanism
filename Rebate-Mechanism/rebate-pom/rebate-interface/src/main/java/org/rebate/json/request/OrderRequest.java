package org.rebate.json.request;

import java.math.BigDecimal;

import org.rebate.json.base.BaseRequest;



public class OrderRequest extends BaseRequest {

  /**
   * 支付方式
   */
  private String payType;

  /**
   * 支付金额
   */
  private BigDecimal amount;

  /**
   * 商铺ID
   */
  private Long sellerId;

  /**
   * 备注
   */
  private String remark;

  /**
   * 是否为乐豆支付
   */
  private Boolean isBeanPay;


  public Boolean getIsBeanPay() {
    return isBeanPay;
  }

  public void setIsBeanPay(Boolean isBeanPay) {
    this.isBeanPay = isBeanPay;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  public String getPayType() {
    return payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
