package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;

/**
 * 乐分记录
 * 
 *
 */
@Entity
@Table(name = "rm_le_score_record", indexes = {
    @Index(name = "createDateIndex", columnList = "createDate"),
    @Index(name = "leScoreTypeIndex", columnList = "leScoreType")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_le_score_record_sequence")
public class LeScoreRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 消费收益乐分的商家
   */
  private Seller seller;

  /**
   * 得到乐分的用户
   */
  private EndUser endUser;

  /**
   * 乐分数量
   */
  private BigDecimal amount;
  /**
   * 乐分类型
   */
  private LeScoreType leScoreType;

  /**
   * 用户当前乐分
   */
  private BigDecimal userCurLeScore;
  /**
   * 备注
   */
  private String remark;

  /**
   * 推荐的好友昵称
   */
  private String recommender;

  /**
   * 推荐的好友头像
   */
  private String recommenderPhoto;

  /**
   * 提现审核状态
   */
  private ApplyStatus withdrawStatus;

  /**
   * 提现的激励乐分(包括鼓励金乐分，推荐获得乐分)
   */
  private BigDecimal motivateLeScore;

  /**
   * 商家直接收益乐分
   */
  private BigDecimal incomeLeScore;

  /**
   * 代理商提成乐分
   */
  private BigDecimal agentLeScore;

  /**
   * 微信提现失败时返回的错误信息
   */
  private String wechatReturnMsg;

  /**
   * 是否已提现
   */
  private Boolean isWithdraw;


  @Column(scale = 2, precision = 10)
  public BigDecimal getAgentLeScore() {
    return agentLeScore;
  }

  public void setAgentLeScore(BigDecimal agentLeScore) {
    this.agentLeScore = agentLeScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getMotivateLeScore() {
    return motivateLeScore;
  }

  public void setMotivateLeScore(BigDecimal motivateLeScore) {
    this.motivateLeScore = motivateLeScore;
  }


  @Column(scale = 2, precision = 10)
  public BigDecimal getIncomeLeScore() {
    return incomeLeScore;
  }

  public void setIncomeLeScore(BigDecimal incomeLeScore) {
    this.incomeLeScore = incomeLeScore;
  }


  public ApplyStatus getWithdrawStatus() {
    return withdrawStatus;
  }

  public void setWithdrawStatus(ApplyStatus withdrawStatus) {
    this.withdrawStatus = withdrawStatus;
  }

  @Column(length = 200)
  public String getRecommenderPhoto() {
    return recommenderPhoto;
  }

  public void setRecommenderPhoto(String recommenderPhoto) {
    this.recommenderPhoto = recommenderPhoto;
  }

  @Column(length = 20)
  public String getRecommender() {
    return recommender;
  }

  public void setRecommender(String recommender) {
    this.recommender = recommender;
  }

  public LeScoreType getLeScoreType() {
    return leScoreType;
  }

  public void setLeScoreType(LeScoreType leScoreType) {
    this.leScoreType = leScoreType;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getUserCurLeScore() {
    return userCurLeScore;
  }

  public void setUserCurLeScore(BigDecimal userCurLeScore) {
    this.userCurLeScore = userCurLeScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Column(length = 100)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  @ManyToOne
  public EndUser getEndUser() {
    return endUser;
  }


  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @ManyToOne
  public Seller getSeller() {
    return seller;
  }


  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  @Column(length = 100)
  public String getWechatReturnMsg() {
    return wechatReturnMsg;
  }

  public void setWechatReturnMsg(String wechatReturnMsg) {
    this.wechatReturnMsg = wechatReturnMsg;
  }

  public Boolean getIsWithdraw() {
    return isWithdraw;
  }

  public void setIsWithdraw(Boolean isWithdraw) {
    this.isWithdraw = isWithdraw;
  }

}
