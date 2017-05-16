package org.rebate.json.request;

import java.math.BigDecimal;

import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.json.base.BaseRequest;

public class OrderRequest extends BaseRequest {

  /**
   * 支付方式
   */
  private String payType;

  /**
   * 支付方式ID
   */
  private String payTypeId;

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

  /**
   * 根据回复状态查看订单
   */
  private OrderStatus orderStatus;

  /**
   * 配置项key
   */
  private SystemConfigKey configKey;

  /**
   * 支付密码
   */
  private String payPwd;

  /**
   * 商家录单
   */
  private Boolean isSallerOrder;

  public String getPayPwd() {
    return payPwd;
  }

  public void setPayPwd(String payPwd) {
    this.payPwd = payPwd;
  }

  public String getPayTypeId() {
    return payTypeId;
  }

  public void setPayTypeId(String payTypeId) {
    this.payTypeId = payTypeId;
  }

  public SystemConfigKey getConfigKey() {
    return configKey;
  }

  public void setConfigKey(SystemConfigKey configKey) {
    this.configKey = configKey;
  }

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

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Boolean getIsSallerOrder() {
    return isSallerOrder;
  }

  public void setIsSallerOrder(Boolean isSallerOrder) {
    this.isSallerOrder = isSallerOrder;
  }


}
