package org.rebate.request;

public class SellerCategoryReq {
  /**
   * 商家类别名称
   */
  private String categoryName;
  
  /**
   * 是否生效
   */
  private Boolean isActive;

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }
  
}
