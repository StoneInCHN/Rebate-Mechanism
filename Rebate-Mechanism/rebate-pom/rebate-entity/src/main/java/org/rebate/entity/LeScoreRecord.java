package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;

/**
 * 乐分记录
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_le_score_record")
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
   * 提现状态
   */
  private ApplyStatus withdrawStatus;

  /**
   * 提现的激励乐分(包括乐心分红乐分，推荐获得乐分)
   */
  private BigDecimal motivateLeScore;

  /**
   * 提现的直接收入乐分(包括商家收益乐分，代理商提成乐分)
   */
  private BigDecimal incomeLeScore;


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



}
