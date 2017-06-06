package org.rebate.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.Gender;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 终端用户实体
 * 
 * @author shijun
 *
 */

@Entity
@Table(name = "rm_end_user", indexes = {
    @Index(name = "cellPhoneNumIndex", columnList = "cellPhoneNum"),
    @Index(name = "recommenderIdIndex", columnList = "recommenderId"),
    @Index(name = "nickNameIndex", columnList = "nickName")})
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
   * 推荐人ID
   */
  private Long recommenderId;

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
   * 用户所在地区
   */
  private Area area;

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
  private Set<Seller> sellers = new HashSet<Seller>();

  /**
   * 用户发起的商户申请
   */
  private Set<SellerApplication> sellerApplications = new HashSet<SellerApplication>();

  /**
   * 用户积分记录
   */
  private Set<RebateRecord> rebateRecords = new HashSet<RebateRecord>();

  /**
   * 用户乐分记录
   */
  private Set<LeScoreRecord> leScoreRecords = new HashSet<LeScoreRecord>();
  /**
   * 用户乐心记录
   */
  private Set<LeMindRecord> leMindRecords = new HashSet<LeMindRecord>();
  /**
   * 用户乐豆记录
   */
  private Set<LeBeanRecord> leBeanRecords = new HashSet<LeBeanRecord>();

  /**
   * 用户是否为代理商
   */
  private Agent agent;

  /**
   * 当前积分
   */
  private BigDecimal curScore = new BigDecimal("0");

  /**
   * 当前乐心
   */
  private BigDecimal curLeMind = new BigDecimal("0");

  /**
   * 当前乐分
   */
  private BigDecimal curLeScore = new BigDecimal("0");

  /**
   * 激励乐分(包括消费鼓励金乐分，推荐获得乐分)
   */
  private BigDecimal motivateLeScore = new BigDecimal("0");

  /**
   * 商家直接收入乐分
   */
  private BigDecimal incomeLeScore = new BigDecimal("0");


  // /**
  // * 推荐乐分
  // */
  // private BigDecimal recommendLeScore = new BigDecimal("0");
  //
  /**
   * 代理商提成乐分
   */
  private BigDecimal agentLeScore = new BigDecimal("0");

  /**
   * 累计积分
   */
  private BigDecimal totalScore = new BigDecimal("0");

  /**
   * 累计乐心
   */
  private BigDecimal totalLeMind = new BigDecimal("0");

  /**
   * 累计乐分
   */
  private BigDecimal totalLeScore = new BigDecimal("0");

  /**
   * 当前乐豆
   */
  private BigDecimal curLeBean = new BigDecimal("0");

  /**
   * 累计乐豆（无法提取）
   */
  private BigDecimal totalLeBean = new BigDecimal("0");

  /**
   * 用户订单
   */
  private Set<Order> userOrders = new HashSet<Order>();

  /**
   * 是否绑定微信
   */
  private Boolean isBindWeChat;

  /**
   * 微信openid
   */
  private String wechatOpenid;

  /**
   * 微信账号昵称
   */
  private String wechatNickName;

  /**
   * 消息
   */
  private Set<MsgEndUser> msgEndUsers = new HashSet<MsgEndUser>();

  /**
   * 是否被限制达到推荐层级而无法被推荐
   */
  private Boolean isLimitRecommend;

  /**
   * 商户
   */
  private Seller seller;

  // /**
  // * 商户图片
  // */
  // private String sellerPicUrl;


  @Column(scale = 4, precision = 12)
  public BigDecimal getAgentLeScore() {
    return agentLeScore;
  }

  public void setAgentLeScore(BigDecimal agentLeScore) {
    this.agentLeScore = agentLeScore;
  }

  @Column(length = 50)
  public String getWechatNickName() {
    return wechatNickName;
  }

  public void setWechatNickName(String wechatNickName) {
    this.wechatNickName = wechatNickName;
  }

  @Column(length = 50)
  public String getWechatOpenid() {
    return wechatOpenid;
  }

  public void setWechatOpenid(String wechatOpenid) {
    this.wechatOpenid = wechatOpenid;
  }

  @Transient
  public Seller getSeller() {
    if (this.getSellers() != null) {
      if (this.getSellers() != null) {
        for (Seller seller : this.getSellers()) {
          if (!AccountStatus.DELETE.equals(seller.getAccountStatus())) {
            return seller;
          }
        }
      }
    }
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }


  @Transient
  public Boolean getIsLimitRecommend() {
    return isLimitRecommend;
  }

  public void setIsLimitRecommend(Boolean isLimitRecommend) {
    this.isLimitRecommend = isLimitRecommend;
  }

  @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL)
  public Set<LeMindRecord> getLeMindRecords() {
    return leMindRecords;
  }

  public void setLeMindRecords(Set<LeMindRecord> leMindRecords) {
    this.leMindRecords = leMindRecords;
  }

  @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL)
  public Set<LeBeanRecord> getLeBeanRecords() {
    return leBeanRecords;
  }

  public void setLeBeanRecords(Set<LeBeanRecord> leBeanRecords) {
    this.leBeanRecords = leBeanRecords;
  }

  @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL)
  public Set<LeScoreRecord> getLeScoreRecords() {
    return leScoreRecords;
  }

  public void setLeScoreRecords(Set<LeScoreRecord> leScoreRecords) {
    this.leScoreRecords = leScoreRecords;
  }

  @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL)
  public Set<RebateRecord> getRebateRecords() {
    return rebateRecords;
  }

  public void setRebateRecords(Set<RebateRecord> rebateRecords) {
    this.rebateRecords = rebateRecords;
  }

  @Transient
  public Boolean getIsBindWeChat() {
    isBindWeChat = false;
    if (getWechatOpenid() != null) {
      isBindWeChat = true;
    }
    return isBindWeChat;
  }

  public void setIsBindWeChat(Boolean isBindWeChat) {
    this.isBindWeChat = isBindWeChat;
  }

  @ManyToOne
  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
  }

  @OneToOne
  public Agent getAgent() {
    return agent;
  }

  public void setAgent(Agent agent) {
    this.agent = agent;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getMotivateLeScore() {
    return motivateLeScore;
  }

  public void setMotivateLeScore(BigDecimal motivateLeScore) {
    this.motivateLeScore = motivateLeScore;
  }


  @Column(scale = 4, precision = 12)
  public BigDecimal getIncomeLeScore() {
    return incomeLeScore;
  }

  public void setIncomeLeScore(BigDecimal incomeLeScore) {
    this.incomeLeScore = incomeLeScore;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getCurLeBean() {
    return curLeBean;
  }

  public void setCurLeBean(BigDecimal curLeBean) {
    this.curLeBean = curLeBean;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalLeBean() {
    return totalLeBean;
  }

  public void setTotalLeBean(BigDecimal totalLeBean) {
    this.totalLeBean = totalLeBean;
  }

  @OneToMany(mappedBy = "endUser")
  public Set<Order> getUserOrders() {
    return userOrders;
  }

  public void setUserOrders(Set<Order> userOrders) {
    this.userOrders = userOrders;
  }

  @OneToMany(mappedBy = "endUser")
  public Set<SellerApplication> getSellerApplications() {
    return sellerApplications;
  }

  public void setSellerApplications(Set<SellerApplication> sellerApplications) {
    this.sellerApplications = sellerApplications;
  }

  @OneToMany(mappedBy = "endUser")
  public Set<Seller> getSellers() {
    return sellers;
  }

  public void setSellers(Set<Seller> sellers) {
    this.sellers = sellers;
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

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
  @JsonProperty
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
  @JsonProperty
  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  @Column(length = 50)
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

  public Long getRecommenderId() {
    return recommenderId;
  }

  public void setRecommenderId(Long recommenderId) {
    this.recommenderId = recommenderId;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getCurScore() {
    return curScore;
  }

  public void setCurScore(BigDecimal curScore) {
    this.curScore = curScore;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getCurLeMind() {
    return curLeMind;
  }

  public void setCurLeMind(BigDecimal curLeMind) {
    this.curLeMind = curLeMind;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getCurLeScore() {
    return curLeScore;
  }

  public void setCurLeScore(BigDecimal curLeScore) {
    this.curLeScore = curLeScore;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(BigDecimal totalScore) {
    this.totalScore = totalScore;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalLeMind() {
    return totalLeMind;
  }

  public void setTotalLeMind(BigDecimal totalLeMind) {
    this.totalLeMind = totalLeMind;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalLeScore() {
    return totalLeScore;
  }

  public void setTotalLeScore(BigDecimal totalLeScore) {
    this.totalLeScore = totalLeScore;
  }

  @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL)
  public Set<MsgEndUser> getMsgEndUsers() {
    return msgEndUsers;
  }

  public void setMsgEndUsers(Set<MsgEndUser> msgEndUsers) {
    this.msgEndUsers = msgEndUsers;
  }
}
