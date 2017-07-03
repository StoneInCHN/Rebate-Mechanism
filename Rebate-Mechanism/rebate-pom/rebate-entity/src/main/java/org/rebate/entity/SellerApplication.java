package org.rebate.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;


/**
 * 商家申请实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_seller_application", indexes = {
    @Index(name = "sellerNameIndex", columnList = "sellerName"),
    @Index(name = "licenseNumIndex", columnList = "licenseNum"),
    @Index(name = "contactPersonIndex", columnList = "contactPerson"),
    @Index(name = "contactCellPhoneIndex", columnList = "contactCellPhone"),
    @Index(name = "createDateIndex", columnList = "createDate"),
    @Index(name = "applyStatusIndex", columnList = "applyStatus")})
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
   * 营业执照号
   */
  private String licenseNum;

  /**
   * 营业执照图片
   * */
  private String licenseImgUrl;

  /**
   * 门店照片
   */
  private String storePhoto;

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
   * 店铺介绍
   */
  private String description;

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

  /**
   * 提出申请的用户
   */
  private EndUser endUser;

  /**
   * 折扣
   */
  private BigDecimal discount;

  /**
   * 每日营业额上限
   */
  private BigDecimal limitAmountByDay;

  /**
   * 商家环境图片
   */
  private List<SellerEnvImage> envImages = new ArrayList<SellerEnvImage>();

  /**
   * 商家承诺书图片
   */
  private List<SellerCommitmentImage> commitmentImages = new ArrayList<SellerCommitmentImage>();


  @Column(length = 1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Valid
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  @CollectionTable(name = "rm_seller_apply_commitment_image")
  public List<SellerCommitmentImage> getCommitmentImages() {
    return commitmentImages;
  }

  public void setCommitmentImages(List<SellerCommitmentImage> commitmentImages) {
    this.commitmentImages = commitmentImages;
  }

  @Valid
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  @CollectionTable(name = "rm_seller_apply_env_image")
  public List<SellerEnvImage> getEnvImages() {
    return envImages;
  }

  public void setEnvImages(List<SellerEnvImage> envImages) {
    this.envImages = envImages;
  }

  @Column(length = 100)
  public String getLicenseNum() {
    return licenseNum;
  }

  public void setLicenseNum(String licenseNum) {
    this.licenseNum = licenseNum;
  }

  @Column(length = 200)
  public String getLicenseImgUrl() {
    return licenseImgUrl;
  }

  public void setLicenseImgUrl(String licenseImgUrl) {
    this.licenseImgUrl = licenseImgUrl;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

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

  @ManyToOne(fetch = FetchType.LAZY)
  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
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

  @ManyToOne(fetch = FetchType.LAZY)
  public SellerCategory getSellerCategory() {
    return sellerCategory;
  }

  public void setSellerCategory(SellerCategory sellerCategory) {
    this.sellerCategory = sellerCategory;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getLimitAmountByDay() {
    return limitAmountByDay;
  }

  public void setLimitAmountByDay(BigDecimal limitAmountByDay) {
    this.limitAmountByDay = limitAmountByDay;
  }

}
