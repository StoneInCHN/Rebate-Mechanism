package org.rebate.aspect;

import java.lang.reflect.Type;

public class UserParam {

  private Long userId;

  private Type returnType;


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
