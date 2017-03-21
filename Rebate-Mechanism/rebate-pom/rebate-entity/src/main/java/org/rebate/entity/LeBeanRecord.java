package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.LeBeanChangeType;

/**
 * 乐豆记录
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_le_bean_record")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_le_bean_record_sequence")
public class LeBeanRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 返利的用户
   */
  private EndUser endUser;

  /**
   * 乐豆变化类型
   */
  private LeBeanChangeType type;

  /**
   * 乐豆变化数量
   */
  private BigDecimal amount;


  /**
   * 用户当前乐豆
   */
  private BigDecimal userCurLeBean;


  /**
   * 备注
   */
  private String remark;



  public LeBeanChangeType getType() {
    return type;
  }

  public void setType(LeBeanChangeType type) {
    this.type = type;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getUserCurLeBean() {
    return userCurLeBean;
  }

  public void setUserCurLeBean(BigDecimal userCurLeBean) {
    this.userCurLeBean = userCurLeBean;
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
