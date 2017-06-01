package org.rebate.utils.allinpay.demo;


/**
 */
public class TranxConTest {

  /**
   * XML交易参数
   */    
    
  //测试环境
//  public String url = "https://113.108.182.3/aipg/ProcessServlet";
//  public String pfxPassword = "111111";
//  public String userName = "20060400000044502";
//  public String merchantId = "200604000000445";
//  public String password = "`12qwe";
//	public String businessCode = "09400";//测试环境：虚拟账户取现
//  public String pfxPath = "src\\main\\resources\\allinpayConfig\\test\\20060400000044502.p12";
//  public String tltcerPath = "src\\main\\resources\\allinpayConfig\\test\\allinpay-pds.cer";
  
  //生产环境
  public String url = "https://tlt.allinpay.com/aipg/ProcessServlet";
  public String pfxPassword = "111111";
  public String userName = "20070100000440901";
  public String merchantId = "200701000004409";
  public String password = "allinpay_123";
  public String businessCode = "09900";//生产环境:代付
  public String pfxPath = "src\\main\\resources\\allinpayConfig\\product\\20070100000440901.p12";
  public String tltcerPath = "src\\main\\resources\\allinpayConfig\\product\\allinpay-pds.cer";
  

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
