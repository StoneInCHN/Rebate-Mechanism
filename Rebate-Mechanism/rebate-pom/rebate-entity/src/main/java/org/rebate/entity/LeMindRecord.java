package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 乐心记录
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_le_mind_record")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_le_mind_record_sequence")
public class LeMindRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 得到乐心的用户
   */
  private EndUser endUser;

  /**
   * 获得乐心数量
   */
  private BigDecimal amount;
  /**
   * 对应的积分数量
   */
  private BigDecimal score;

  /**
   * 用户当前乐心
   */
  private BigDecimal userCurLeMind;
  /**
   * 备注
   */
  private String remark;


  @Column(scale = 2, precision = 10)
  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getUserCurLeMind() {
    return userCurLeMind;
  }

  public void setUserCurLeMind(BigDecimal userCurLeMind) {
    this.userCurLeMind = userCurLeMind;
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


}
