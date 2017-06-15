package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.AccountStatus;

public class SellerRequest {
  /**
   * 商家名字
   */
  private String name;

  /**
   * 联系人
   */
  private String contactPerson;
  /**
   * 联系人手机
   */
  private String contactCellPhone;

  /**
   * 申请人手机号
   */
  private String cellPhoneNum;

  /** 地区 */
  private Long areaId;

  /**
   * 审核状态
   */
  private AccountStatus accountStatus;

  /**
   * 提出申请的用户
   */
  private Long endUserId;

  /**
   * 商家类别
   */
  private Long sellerCategoryId;

  /** 申请起始日期 */
  private Date applyFromDate;

  /** 申请结束日期 */
  private Date applyToDate;

  /**
   * 营业执照号
   */
  private String licenseNum;


  public String getLicenseNum() {
    return licenseNum;
  }

  public void setLicenseNum(String licenseNum) {
    this.licenseNum = licenseNum;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getContactCellPhone() {
    return contactCellPhone;
  }

  public void setContactCellPhone(String contactCellPhone) {
    this.contactCellPhone = contactCellPhone;
  }

  public Long getAreaId() {
    return areaId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public Long getEndUserId() {
    return endUserId;
  }

  public void setEndUserId(Long endUserId) {
    this.endUserId = endUserId;
  }

  public Long getSellerCategoryId() {
    return sellerCategoryId;
  }

  public void setSellerCategoryId(Long sellerCategoryId) {
    this.sellerCategoryId = sellerCategoryId;
  }

  public Date getApplyFromDate() {
    return applyFromDate;
  }

  public void setApplyFromDate(Date applyFromDate) {
    this.applyFromDate = applyFromDate;
  }

  public Date getApplyToDate() {
    return applyToDate;
  }

  public void setApplyToDate(Date applyToDate) {
    this.applyToDate = applyToDate;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

}
