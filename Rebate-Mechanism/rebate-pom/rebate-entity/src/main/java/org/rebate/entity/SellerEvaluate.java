package org.rebate.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;

/**
 * Entity - 用户对商家评价
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_seller_evaluate")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_evaluate_sequence")
public class SellerEvaluate extends BaseEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 4509383295262124028L;



  /** 评价用户 */
  private EndUser endUser;


  /** 得分 */
  private Integer score;

  /**
   * 评价内容
   */
  private String content;

  /**
   * 商家
   */
  private Seller seller;

  /**
   * 商家回复
   */
  private String sellerReply;

  /** 评价的图片 */
  private List<SellerEvaluateImage> evaluateImages = new ArrayList<SellerEvaluateImage>();

  /**
   * 评价对应的订单
   * 
   */
  private Order order;

  /**
   * 是否显示，被屏蔽的评价评分也要去掉
   */
  private CommonStatus status;


  public CommonStatus getStatus() {
    return status;
  }

  public void setStatus(CommonStatus status) {
    this.status = status;
  }

  @OneToOne(mappedBy = "evaluate")
  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  @Valid
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  @CollectionTable(name = "rm_seller_evaluate_image")
  public List<SellerEvaluateImage> getEvaluateImages() {
    return evaluateImages;
  }

  public void setEvaluateImages(List<SellerEvaluateImage> evaluateImages) {
    this.evaluateImages = evaluateImages;
  }

  @ManyToOne
  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  @Column(length = 1000)
  public String getSellerReply() {
    return sellerReply;
  }

  public void setSellerReply(String sellerReply) {
    this.sellerReply = sellerReply;
  }

  @ManyToOne
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  @Column(length = 1000)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }



}
