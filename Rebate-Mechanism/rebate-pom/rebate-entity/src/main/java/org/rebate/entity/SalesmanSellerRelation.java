package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 业务员推荐商家关系表
 * 
 * 一个业务员可以推荐其他多个商家
 * 
 * @author Andrea
 *
 */

@Entity
@Table(name = "rm_salesman_seller_relation")
@SequenceGenerator(name = "sequenceGenerator",
    sequenceName = "rm_salesman_seller_relation_sequence")
public class SalesmanSellerRelation extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 业务员用户
   */
  private EndUser endUser;

  /**
   * 业务员推荐的商家
   */
  private Seller seller;

  /**
   * 业务员推荐的商家审核记录
   */
  private SellerApplication sellerApplication;

  /**
   * 该商家给业务员带来的累计收益
   */
  private BigDecimal totalRecommendLeScore = new BigDecimal("0");

  /**
   * 是否审核通过
   */
  private Boolean applyStatus;



  public Boolean getApplyStatus() {
    return applyStatus;
  }

  public void setApplyStatus(Boolean applyStatus) {
    this.applyStatus = applyStatus;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalRecommendLeScore() {
    return totalRecommendLeScore;
  }

  public void setTotalRecommendLeScore(BigDecimal totalRecommendLeScore) {
    this.totalRecommendLeScore = totalRecommendLeScore;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @OneToOne
  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  @OneToOne
  public SellerApplication getSellerApplication() {
    return sellerApplication;
  }

  public void setSellerApplication(SellerApplication sellerApplication) {
    this.sellerApplication = sellerApplication;
  }


}
