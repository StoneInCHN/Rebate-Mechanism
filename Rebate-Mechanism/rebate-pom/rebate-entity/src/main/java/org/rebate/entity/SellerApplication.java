package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;


/**
 * 商家申请实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_seller_application")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_application_sequence")
public class SellerApplication extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 商家名字
   */
  private String sellerName;

  /**
   * 商家地址
   */
  private String address;

  /**
   * 联系人
   */
  private String contactPerson;
  /**
   * 联系人手机
   */
  private String contactCellPhone;

  /**
   * 店铺电话
   */
  private String storePhone;

  /** 地区 */
  private Area area;

  /**
   * 营业执照
   */
  private String license;

  // /** 营业执照文件 */
  // private MultipartFile licenseFile;

  /**
   * 门店照片
   */
  private String storePhoto;

  // /** 门店照片文件 */
  // private MultipartFile storePhotoFile;

  /**
   * 纬度
   */
  private BigDecimal latitude;

  /**
   * 经度
   */
  private BigDecimal longitude;

  /**
   * 审核状态
   */
  private ApplyStatus applyStatus;

  /**
   * 备注
   */
  private String notes;

  /**
   * 邮件
   */
  private String email;

  /**
   * 商家类别
   */
  private SellerCategory sellerCategory;
  
  private Integer   endUserId;
  
  @Column(length = 100)
  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  @Column(length = 20)
  public String getContactCellPhone() {
    return contactCellPhone;
  }

  public void setContactCellPhone(String contactCellPhone) {
    this.contactCellPhone = contactCellPhone;
  }

  @Column(length = 50)
  public String getStorePhone() {
    return storePhone;
  }

  public void setStorePhone(String storePhone) {
    this.storePhone = storePhone;
  }

  @Column(length = 100)
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(length = 20)
  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
  }

  @Column(length = 200)
  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }


  @Column(length = 200)
  public String getStorePhoto() {
    return storePhoto;
  }

  public void setStorePhoto(String storePhoto) {
    this.storePhoto = storePhoto;
  }

  @Column(scale = 6, precision = 10)
  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  @Column(scale = 6, precision = 10)
  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public ApplyStatus getApplyStatus() {
    return applyStatus;
  }

  public void setApplyStatus(ApplyStatus applyStatus) {
    this.applyStatus = applyStatus;
  }

  @Column(length = 500)
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Column(length = 100)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @ManyToOne
  public SellerCategory getSellerCategory() {
    return sellerCategory;
  }

  public void setSellerCategory(SellerCategory sellerCategory) {
    this.sellerCategory = sellerCategory;
  }

  public Integer getEndUserId() {
    return endUserId;
  }

  public void setEndUserId(Integer endUserId) {
    this.endUserId = endUserId;
  }
  
}
