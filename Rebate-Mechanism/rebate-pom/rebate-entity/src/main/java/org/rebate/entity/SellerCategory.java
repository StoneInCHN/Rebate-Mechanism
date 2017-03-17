package org.rebate.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 商家类别实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_seller_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_category_sequence")
public class SellerCategory extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 商家类别名称
   */
  private String categoryName;

  /**
   * 排序
   */
  private Integer categorOrder;

  /**
   * 是否生效
   */
  private Boolean isActive;

  /**
   * 商家
   */
  private Set<Seller> sellers = new HashSet<Seller>();


  @OneToMany(mappedBy = "sellerCategory")
  public Set<Seller> getSellers() {
    return sellers;
  }

  public void setSellers(Set<Seller> sellers) {
    this.sellers = sellers;
  }

  @Column(length = 50)
  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public Integer getCategorOrder() {
    return categorOrder;
  }

  public void setCategorOrder(Integer categorOrder) {
    this.categorOrder = categorOrder;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

}
