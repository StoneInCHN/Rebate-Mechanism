package org.rebate.aspect;

import java.lang.reflect.Type;

public class UserParam {



  public enum CheckUserType {
    /**
     * 终端用户
     */
    ENDUSER,
    /**
     * 商家
     */
    SELLER


  };

  private Long userId;

  private CheckUserType userType;

  private Type returnType;


  public CheckUserType getUserType() {
    return userType;
  }

  public void setUserType(CheckUserType userType) {
    this.userType = userType;
  }

  public Type getReturnType() {
    return returnType;
  }

  public void setReturnType(Type returnType) {
    this.returnType = returnType;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }


}
