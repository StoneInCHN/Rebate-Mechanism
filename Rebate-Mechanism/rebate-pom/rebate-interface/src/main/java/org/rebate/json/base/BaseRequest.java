package org.rebate.json.base;

import java.util.List;

public class BaseRequest {

  /**
   * 用户token
   */
  private String token;
  /**
   * 用户名
   */
  private String userName;
  /**
   * 手机号
   */
  private String cellPhoneNum;
  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 所需entity的ID
   */
  private Long entityId;

  private List<Long> entityIds;

  /**
   * 分页-页面大小
   */
  private Integer pageSize = 10;
  /**
   * 分页-当前页码
   */
  private Integer pageNumber = 1;



  public List<Long> getEntityIds() {
    return entityIds;
  }

  public void setEntityIds(List<Long> entityIds) {
    this.entityIds = entityIds;
  }

  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  // public String getMobileNum() {
  // return mobileNum;
  // }
  //
  // public void setMobileNum(String mobileNum) {
  // this.mobileNum = mobileNum;
  // }
}
