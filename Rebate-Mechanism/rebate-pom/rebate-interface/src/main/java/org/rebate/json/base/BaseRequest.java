package org.rebate.json.base;

public class BaseRequest {

  /**
   * 用户token
   */
  private String token;
  /**
   * 用户名
   */
  private String userName;
  // /**
  // * 手机号
  // */
  // private String mobileNum;
  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 分页-页面大小
   */
  private Integer pageSize;
  /**
   * 分页-当前页码
   */
  private Integer pageNumber;


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
