package org.rebate.utils.allinpay.pojo;


/**
 */
public class TranxCon {
	  /**
	   * XML参数
	   */    
	  private String url;
	  private String pfxPassword;
	  private String userName;
	  private String merchantId;
	  private String password;
	  private String businessCode;
	  private String pfxPath;
	  private String tltcerPath;

	  public String getMerchantId() {
	    return merchantId;
	  }

	  public String getPassword() {
	    return password;
	  }

	  public String getPfxPassword() {
	    return pfxPassword;
	  }

	  public String getPfxPath() {
	    return pfxPath;
	  }


	  public String getTltcerPath() {
	    return tltcerPath;
	  }

	  public String getUserName() {
	    return userName;
	  }

	  public void setMerchantId(String merchantId) {
	    this.merchantId = merchantId;
	  }

	  public void setPassword(String password) {
	    this.password = password;
	  }

	  public void setPfxPassword(String pfxPassword) {
	    this.pfxPassword = pfxPassword;
	  }

	  public void setPfxPath(String pfxPath) {
	    this.pfxPath = pfxPath;
	  }

	  public void setTltcerPath(String tltcerPath) {
	    this.tltcerPath = tltcerPath;
	  }

	  public void setUserName(String userName) {
	    this.userName = userName;
	  }

		public String getUrl() {
			return url;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}

		public String getBusinessCode() {
			return businessCode;
		}

		public void setBusinessCode(String businessCode) {
			this.businessCode = businessCode;
		}

}
