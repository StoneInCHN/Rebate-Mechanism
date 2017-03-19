package org.rebate.json.base;

public class BaseResponse {
  
    /** response code*/
    private String code;
    
    /** response desc*/
    private String desc;
    
    /** response token*/
    private String token;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
    
    
}
