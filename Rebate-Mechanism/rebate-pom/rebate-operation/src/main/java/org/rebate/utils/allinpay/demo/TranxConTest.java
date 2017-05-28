package org.rebate.utils.allinpay.demo;


/**
 */
public class TranxConTest {

  /**
   * XML交易参数
   */
  
  // 商户证书信息
  public String pfxPassword = "111111";
  //测试环境
  //public String userName = "20060400000044502";
  //public String merchantId = "200604000000445";
  //public String password = "`12qwe";
  //public String cerPath = "src\\main\\resources\\allinpayConfig\\test\\20060400000044502.cer";
  //public String pfxPath = "src\\main\\resources\\allinpayConfig\\test\\20060400000044502.p12";
  //public String tltcerPath = "src\\main\\resources\\allinpayConfig\\test\\allinpay-pds.cer";
  
  //生产环境
  public String userName = "20070100000440901";
  public String merchantId = "200701000004409";
  public String password = "allinpay_123";
  public String pfxPath = "src\\main\\resources\\allinpayConfig\\product\\20070100000440904.p12";
  public String tltcerPath = "src\\main\\resources\\allinpayConfig\\product\\allinpay-pds.cer";
  
  public String sum = "200000";// 交易总金额
  public String tel = "13434245847";
    

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

  public String getSum() {
    return sum;
  }

  public String getTel() {
    return tel;
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

  public void setSum(String sum) {
    this.sum = sum;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public void setTltcerPath(String tltcerPath) {
    this.tltcerPath = tltcerPath;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }



}
