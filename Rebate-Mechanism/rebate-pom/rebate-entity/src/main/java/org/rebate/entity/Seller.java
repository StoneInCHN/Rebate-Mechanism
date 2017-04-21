package org.rebate.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.FeaturedService;

/**
 * 商家实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_seller", indexes = {@Index(name = "nameIndex", columnList = "name"),
    @Index(name = "contactPersonIndex", columnList = "contactPerson"),
    @Index(name = "contactCellPhoneIndex", columnList = "contactCellPhone"),
    @Index(name = "accountStatusIndex", columnList = "accountStatus")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_sequence")
public class Seller extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 商家名字
   */
  private String name;
  // /**
  // * 门店照片
  // */
  // private MultipartFile storePicture;

  /**
   * 门店照片URL
   */
  private String storePictureUrl;


  /**
   * 商家类别
   */
  private SellerCategory sellerCategory;

  /**
   * 商家地址
   */
  private String address;

  /**
   * 商家联系人
   */
  private String contactPerson;
  /**
   * 商家联系人手机
   */
  private String contactCellPhone;

  /**
   * 店铺电话
   */
  private String storePhone;

  /**
   * 营业时间
   */
  private String businessTime;
  /**
   * 商家状态
   */
  private AccountStatus accountStatus;

  /** 地区 */
  private Area area;

  /**
   * 评分
   */
  private BigDecimal rateScore = new BigDecimal("5");

  /**
   * 评价次数
   */
  private Integer rateCounts = 0;

  /**
   * 均价
   */
  private BigDecimal avgPrice;


  /**
   * 纬度
   */
  private BigDecimal latitude;

  /**
   * 经度
   */
  private BigDecimal longitude;

  /**
   * 营业执照号
   */
  private String licenseNum;

  /** 营业执照图片 */
  private String licenseImgUrl;

  /**
   * 店铺介绍
   */
  private String description;

  /**
   * 折扣
   */
  private BigDecimal discount;

  /**
   * 商家被收藏数
   */
  private Integer favoriteNum = 0;

  /**
   * 收藏该商家的用户
   */
  private Set<EndUser> favoriteEndUsers = new HashSet<EndUser>();

  /**
   * 商家的评价
   */
  private Set<SellerEvaluate> sellerEvaluates = new HashSet<SellerEvaluate>();

  /**
   * 特色服务
   */
  private FeaturedService featuredService;

  /**
   * 商家所属用户
   */
  private EndUser endUser;

  /**
   * 商家订单
   */
  private Set<Order> sellerOrders = new HashSet<Order>();

  /**
   * 商户收款二维码
   */
  private byte[] qrImage;

  /**
   * 商家环境图片
   */
  private List<SellerEnvImage> envImages = new ArrayList<SellerEnvImage>();

  // /**
  // * 推荐人
  // */
  // private String recommender;
  //
  // /**
  // * 推荐人ID
  // */
  // private Long recommenderId;

  /**
   * 商家累计订单数
   */
  private Integer totalOrderNum = 0;

  /**
   * 累计订单金额
   */
  private BigDecimal totalOrderAmount = new BigDecimal("0");

  /**
   * 所有订单未结算(提取)的金额
   */
  private BigDecimal unClearingAmount = new BigDecimal("0");


  public Integer getRateCounts() {
    return rateCounts;
  }

  public void setRateCounts(Integer rateCounts) {
    this.rateCounts = rateCounts;
  }

  public Integer getTotalOrderNum() {
    return totalOrderNum;
  }

  public void setTotalOrderNum(Integer totalOrderNum) {
    this.totalOrderNum = totalOrderNum;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getTotalOrderAmount() {
    return totalOrderAmount;
  }

  public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
    this.totalOrderAmount = totalOrderAmount;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getUnClearingAmount() {
    return unClearingAmount;
  }

  public void setUnClearingAmount(BigDecimal unClearingAmount) {
    this.unClearingAmount = unClearingAmount;
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


  @Valid
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  @CollectionTable(name = "rm_seller_env_image")
  public List<SellerEnvImage> getEnvImages() {
    return envImages;
  }

  public void setEnvImages(List<SellerEnvImage> envImages) {
    this.envImages = envImages;
  }

  @Lob
  @Column(length = 100000, columnDefinition = "blob")
  public byte[] getQrImage() {
    return qrImage;
  }

  public void setQrImage(byte[] qrImage) {
    this.qrImage = qrImage;
  }

  @OneToMany(mappedBy = "seller")
  public Set<Order> getSellerOrders() {
    return sellerOrders;
  }

  public void setSellerOrders(Set<Order> sellerOrders) {
    this.sellerOrders = sellerOrders;
  }

  @ManyToOne
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @Column(length = 100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FeaturedService getFeaturedService() {
    return featuredService;
  }

  public void setFeaturedService(FeaturedService featuredService) {
    this.featuredService = featuredService;
  }

  @OneToMany(mappedBy = "seller")
  public Set<SellerEvaluate> getSellerEvaluates() {
    return sellerEvaluates;
  }

  public void setSellerEvaluates(Set<SellerEvaluate> sellerEvaluates) {
    this.sellerEvaluates = sellerEvaluates;
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

  @Column(length = 50)
  public String getBusinessTime() {
    return businessTime;
  }

  public void setBusinessTime(String businessTime) {
    this.businessTime = businessTime;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  @ManyToOne
  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
  }

  @Column(scale = 1, precision = 2)
  public BigDecimal getRateScore() {
    return rateScore;
  }

  public void setRateScore(BigDecimal rateScore) {
    this.rateScore = rateScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getAvgPrice() {
    return avgPrice;
  }

  public void setAvgPrice(BigDecimal avgPrice) {
    this.avgPrice = avgPrice;
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


  @Column(length = 1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(scale = 2, precision = 3)
  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public Integer getFavoriteNum() {
    return favoriteNum;
  }

  public void setFavoriteNum(Integer favoriteNum) {
    this.favoriteNum = favoriteNum;
  }

  @ManyToMany(mappedBy = "favoriteSellers", fetch = FetchType.LAZY)
  public Set<EndUser> getFavoriteEndUsers() {
    return favoriteEndUsers;
  }

  public void setFavoriteEndUsers(Set<EndUser> favoriteEndUsers) {
    this.favoriteEndUsers = favoriteEndUsers;
  }

  @Column(length = 200)
  public String getStorePictureUrl() {
    return storePictureUrl;
  }

  public void setStorePictureUrl(String storePictureUrl) {
    this.storePictureUrl = storePictureUrl;
  }


  @ManyToOne
  public SellerCategory getSellerCategory() {
    return sellerCategory;
  }

  public void setSellerCategory(SellerCategory sellerCategory) {
    this.sellerCategory = sellerCategory;
  }



}
