package org.rebate.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.Gender;

/**
 * 终端用户实体
 * 
 * @author shijun
 *
 */

@Entity
@Table(name = "rm_end_user", indexes = {@Index(name = "cellPhoneNumIndex",
    columnList = "cellPhoneNum")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_end_user_sequence")
public class EndUser extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 用户头像
   */
  private String userPhoto;

  /**
   * 手机号
   */
  private String cellPhoneNum;

  /**
   * 登录密码
   */
  private String loginPwd;

  /**
   * 支付密码
   */
  private String paymentPwd;

  /**
   * 推荐人
   */
  private String recommender;

  /**
   * 账号状态
   */
  private AccountStatus accountStatus;
  /**
   * 昵称
   */
  private String nickName;
  /**
   * 年龄
   */
  private Integer age;
  /**
   * 性别
   */
  private Gender gender;

  /**
   * 地址
   */
  private String address;

  /**
   * 极光push注册ID
   */
  private String jpushRegId;

  /**
   * 用户二维码
   */
  private byte[] qrImage;

  /**
   * 用户收藏的商家
   */
  private Set<Seller> favoriteSellers = new HashSet<Seller>();


  /**
   * 用户对商家的评价
   */
  private Set<SellerEvaluate> sellerEvalutes = new HashSet<SellerEvaluate>();

  /**
   * 用户拥有的商户店铺
   */
  private Seller seller;

  /**
   * 积分
   */
  private BigDecimal score;

  /**
   * 乐心
   */
  private BigDecimal leMind;

  /**
   * 乐分
   */
  private BigDecimal leScore;



  @Column(scale = 2, precision = 10)
  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getLeMind() {
    return leMind;
  }

  public void setLeMind(BigDecimal leMind) {
    this.leMind = leMind;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getLeScore() {
    return leScore;
  }

  public void setLeScore(BigDecimal leScore) {
    this.leScore = leScore;
  }

  @OneToOne(mappedBy = "endUser")
  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  @Column(length = 200)
  public String getUserPhoto() {
    return userPhoto;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  @OneToMany(mappedBy = "endUser")
  public Set<SellerEvaluate> getSellerEvalutes() {
    return sellerEvalutes;
  }

  public void setSellerEvalutes(Set<SellerEvaluate> sellerEvalutes) {
    this.sellerEvalutes = sellerEvalutes;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "rm_endUser_favorite_seller")
  @OrderBy("createDate desc")
  public Set<Seller> getFavoriteSellers() {
    return favoriteSellers;
  }

  public void setFavoriteSellers(Set<Seller> favoriteSellers) {
    this.favoriteSellers = favoriteSellers;
  }

  @Lob
  @Column(length = 100000, columnDefinition = "blob")
  public byte[] getQrImage() {
    return qrImage;
  }

  public void setQrImage(byte[] qrImage) {
    this.qrImage = qrImage;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  @Column(length = 100)
  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @Column(length = 200)
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(length = 100)
  public String getJpushRegId() {
    return jpushRegId;
  }

  public void setJpushRegId(String jpushRegId) {
    this.jpushRegId = jpushRegId;
  }

  @Column(length = 50)
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Column(length = 20, nullable = false)
  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  @Column(length = 50, nullable = false)
  public String getLoginPwd() {
    return loginPwd;
  }

  public void setLoginPwd(String loginPwd) {
    this.loginPwd = loginPwd;
  }

  @Column(length = 50)
  public String getPaymentPwd() {
    return paymentPwd;
  }

  public void setPaymentPwd(String paymentPwd) {
    this.paymentPwd = paymentPwd;
  }

  @Column(length = 20)
  public String getRecommender() {
    return recommender;
  }

  public void setRecommender(String recommender) {
    this.recommender = recommender;
  }
}
