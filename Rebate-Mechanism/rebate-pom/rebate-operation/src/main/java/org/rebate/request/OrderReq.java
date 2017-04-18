package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.OrderStatus;

public class OrderReq {
  /**
   * 用户手机号
   */
  private String cellPhoneNum;

  /**
   * 用户昵称
   */
  private String endUserNickName;

  /**
   * 商家名称
   */
  private String sellerName;

  // /**
  // * 商家手机号
  // */
  // private String sellerMobile;
  /**
   * 订单状态
   */
  private OrderStatus orderStatus;

  /**
   * 下单时间from
   */
  private Date orderDateFrom;

  /**
   * 下单时间to
   */
  private Date orderDateTo;

  /**
   * 订单编号
   */
  private String sn;

  /**
   * 支付方式
   */
  private String paymentType;



  public String getEndUserNickName() {
    return endUserNickName;
  }

  public void setEndUserNickName(String endUserNickName) {
    this.endUserNickName = endUserNickName;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Date getOrderDateFrom() {
    return orderDateFrom;
  }

  public void setOrderDateFrom(Date orderDateFrom) {
    this.orderDateFrom = orderDateFrom;
  }

  public Date getOrderDateTo() {
    return orderDateTo;
  }

  public void setOrderDateTo(Date orderDateTo) {
    this.orderDateTo = orderDateTo;
  }

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }


}
