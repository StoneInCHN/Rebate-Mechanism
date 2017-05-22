package org.rebate.json.request;

import java.util.List;

import org.rebate.entity.commonenum.CommonEnum.FeaturedService;
import org.rebate.entity.commonenum.CommonEnum.SortType;
import org.rebate.json.base.BaseRequest;
import org.springframework.web.multipart.MultipartFile;


public class SellerRequest extends BaseRequest {

  /**
   * 商家名字
   */
  private String sellerName;

  /**
   * 商家地址
   */
  private String address;
  /**
   * 用户所在地区ID
   */
  private Long areaId;

  /**
   * 用户所在地区ID集合字符串
   */
  private String areaIds;


  /**
   * 联系人手机
   */
  private String contactCellPhone;

  /**
   * 店铺电话
   */
  private String storePhone;
  /**
   * 店铺logo或图片
   */
  private MultipartFile storePicture;

  /**
   * 折扣
   */
  private String discount;

  /**
   * 类别ID
   */
  private Long categoryId;

  /**
   * 营业执照号
   */
  private String licenseNum;

  /**
   * 营业执照图片
   */
  private MultipartFile licenseImg;

  /**
   * 环境图片
   */
  private List<MultipartFile> envImgs;
  /**
   * 店铺简介
   */
  private String note;

  /**
   * 纬度
   */
  private String latitude;

  /**
   * 经度
   */
  private String longitude;

  /**
   * 商铺申请请求的ID
   */
  private Long applyId;

  /**
   * 商铺ID
   */
  private Long sellerId;

  /**
   * 人均消费
   */
  private String avgPrice;

  /**
   * 营业时间
   */
  private String businessTime;

  /**
   * 特色服务
   */
  private FeaturedService featuredService;

  /**
   * 商家回复评价
   */
  private String sellerReply;

  /**
   * 排序类型
   */
  private SortType sortType;

  /**
   * 搜索关键字
   */
  private String keyWord;

  /**
   * 是否为确定操作
   */
  private Boolean isConfirmOpr;


  public Boolean getIsConfirmOpr() {
    return isConfirmOpr;
  }

  public void setIsConfirmOpr(Boolean isConfirmOpr) {
    this.isConfirmOpr = isConfirmOpr;
  }

  public String getAreaIds() {
    return areaIds;
  }

  public void setAreaIds(String areaIds) {
    this.areaIds = areaIds;
  }

  public String getKeyWord() {
    return keyWord;
  }

  public void setKeyWord(String keyWord) {
    this.keyWord = keyWord;
  }

  public SortType getSortType() {
    return sortType;
  }

  public void setSortType(SortType sortType) {
    this.sortType = sortType;
  }

  public String getSellerReply() {
    return sellerReply;
  }

  public void setSellerReply(String sellerReply) {
    this.sellerReply = sellerReply;
  }

  public String getAvgPrice() {
    return avgPrice;
  }

  public void setAvgPrice(String avgPrice) {
    this.avgPrice = avgPrice;
  }

  public String getBusinessTime() {
    return businessTime;
  }

  public void setBusinessTime(String businessTime) {
    this.businessTime = businessTime;
  }

  public FeaturedService getFeaturedService() {
    return featuredService;
  }

  public void setFeaturedService(FeaturedService featuredService) {
    this.featuredService = featuredService;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  public Long getApplyId() {
    return applyId;
  }

  public void setApplyId(Long applyId) {
    this.applyId = applyId;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContactCellPhone() {
    return contactCellPhone;
  }

  public void setContactCellPhone(String contactCellPhone) {
    this.contactCellPhone = contactCellPhone;
  }

  public String getStorePhone() {
    return storePhone;
  }

  public void setStorePhone(String storePhone) {
    this.storePhone = storePhone;
  }

  public MultipartFile getStorePicture() {
    return storePicture;
  }

  public void setStorePicture(MultipartFile storePicture) {
    this.storePicture = storePicture;
  }

  public String getDiscount() {
    return discount;
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getLicenseNum() {
    return licenseNum;
  }

  public void setLicenseNum(String licenseNum) {
    this.licenseNum = licenseNum;
  }

  public MultipartFile getLicenseImg() {
    return licenseImg;
  }

  public void setLicenseImg(MultipartFile licenseImg) {
    this.licenseImg = licenseImg;
  }

  public List<MultipartFile> getEnvImgs() {
    return envImgs;
  }

  public void setEnvImgs(List<MultipartFile> envImgs) {
    this.envImgs = envImgs;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Long getAreaId() {
    return areaId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }



}
