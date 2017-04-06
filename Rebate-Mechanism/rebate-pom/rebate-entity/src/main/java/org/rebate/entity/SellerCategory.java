package org.rebate.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.rebate.entity.base.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

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
   * 商家类别图片地址
   */
  private String categoryPicUrl;

  /**
   * 商家类别图片
   */
  private MultipartFile categoryPic;

  /**
   * 商家
   */
  private Set<Seller> sellers = new HashSet<Seller>();

  @Column(length = 200)
  public String getCategoryPicUrl() {
    return categoryPicUrl;
  }

  public void setCategoryPicUrl(String categoryPicUrl) {
    this.categoryPicUrl = categoryPicUrl;
  }

  @Transient
  public MultipartFile getCategoryPic() {
    return categoryPic;
  }

  public void setCategoryPic(MultipartFile categoryPic) {
    this.categoryPic = categoryPic;
  }

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
