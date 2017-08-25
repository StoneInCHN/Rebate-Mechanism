package org.rebate.json.request;

import java.math.BigDecimal;

import org.rebate.json.base.BaseRequest;

public class JiupaiPayRequest extends BaseRequest {

  /**
   * 订单号
   */
  private String orderSn;

  /**
   * 身份证号码
   */
  private String idNo;

  /**
   * 银行卡号码
   */
  private String cardNo;

  /**
   * 绑卡协议号
   */
  private String contractId;

  /**
   * 支付金额
   */
  private BigDecimal amount;

  /**
   * 客户端IP地址
   */
  private String clientIP;

  /**
   * 商品名称(商家名称)
   */
  private String goodsName;

  /**
   * 支付验证码
   */
  private String checkCode;

  public String getCheckCode() {
    return checkCode;
  }

  public void setCheckCode(String checkCode) {
    this.checkCode = checkCode;
  }

  public String getGoodsName() {
    return goodsName;
  }

  public void setGoodsName(String goodsName) {
    this.goodsName = goodsName;
  }

  public String getClientIP() {
    return clientIP;
  }

  public void setClientIP(String clientIP) {
    this.clientIP = clientIP;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getContractId() {
    return contractId;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public String getIdNo() {
    return idNo;
  }

  public void setIdNo(String idNo) {
    this.idNo = idNo;
  }

  public String getOrderSn() {
    return orderSn;
  }

  public void setOrderSn(String orderSn) {
    this.orderSn = orderSn;
  }

}
