package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;

public class SellerClearingRecordReq {
  
  private Boolean isClearing;

  private String sn;
  
  private String clearingSn;
  
  private String reqSn;
  
  private String sellerName;
  
  private String endUserNickName;
  
  /**
   * 货款结算状态
   */
  private ClearingStatus clearingStatus;

  /**
   * 开始时间
   */
  private Date dateFrom;

  /**
   * 结束时间
   */
  private Date dateTo;

  public Boolean getIsClearing() {
    return isClearing;
  }

  public void setIsClearing(Boolean isClearing) {
    this.isClearing = isClearing;
  }

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public String getClearingSn() {
    return clearingSn;
  }

  public void setClearingSn(String clearingSn) {
    this.clearingSn = clearingSn;
  }

  public String getReqSn() {
    return reqSn;
  }

  public void setReqSn(String reqSn) {
    this.reqSn = reqSn;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public String getEndUserNickName() {
    return endUserNickName;
  }

  public void setEndUserNickName(String endUserNickName) {
    this.endUserNickName = endUserNickName;
  }

  public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

  public ClearingStatus getClearingStatus() {
    return clearingStatus;
  }

  public void setClearingStatus(ClearingStatus clearingStatus) {
    this.clearingStatus = clearingStatus;
  }
  
}
