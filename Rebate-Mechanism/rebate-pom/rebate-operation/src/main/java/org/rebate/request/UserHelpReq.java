package org.rebate.request;

public class UserHelpReq {
  
  /**
   * 标题
   */
  private String title;
  
  /**
   * 是否启用
   */
  private Boolean isEnabled;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Boolean getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
  
  
}
