package org.rebate.utils.jiupai.pojo;

import java.io.File;

import org.rebate.beans.Setting;
import org.rebate.utils.SettingUtils;

/**
 * 
 *九派参数配置
 */
public class JiuPaiConfig {
   
    private String requestUrl;
    private String merchantId;
    private String merchantName;
    private String signType;
    private String version;
    private String merchantCertPath;
    private String merchantCertPass;
    private String rootCertPath;
    private String charset;
    private String notifyUrl;
	  
	private JiuPaiConfig() {}
	private static JiuPaiConfig config;
	public static JiuPaiConfig getConfig() {
	     if(config == null) {
	          config = new JiuPaiConfig();//初始化九派基础数据
	          Setting setting = SettingUtils.get();
	      	  String path = config.getClass().getResource("/").getPath();
	    	  config.setRequestUrl(setting.getJiupaiRequestUrl());//九派支付网关URL
	    	  config.setMerchantId(setting.getJiupaiMerchantId());//九派商户号
	    	  config.setMerchantName(setting.getJiupaiMerchantName());//九派商户名
	    	  config.setSignType("RSA256");//加密算法
	    	  config.setVersion("1.0");//接口版本号
	    	  config.setMerchantCertPath(path + File.separator + setting.getJiupaiMerchantCertPath());//九派私钥证书
	    	  config.setMerchantCertPass(setting.getJiupaiMerchantCertPass());//九派通联私钥密码
	    	  config.setRootCertPath(path + File.separator + setting.getJiupaiRootCertPath());//九派公钥证书
	    	  config.setCharset("02");//请求字符集，字符集  00:GB18030  01:GB2312  02:UTF-8  other:GBK
	    	  config.setNotifyUrl(setting.getJiupaiNotifyUrl());//
	      }
	      return config;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMerchantCertPath() {
		return merchantCertPath;
	}
	public void setMerchantCertPath(String merchantCertPath) {
		this.merchantCertPath = merchantCertPath;
	}
	public String getMerchantCertPass() {
		return merchantCertPass;
	}
	public void setMerchantCertPass(String merchantCertPass) {
		this.merchantCertPass = merchantCertPass;
	}
	public String getRootCertPath() {
		return rootCertPath;
	}
	public void setRootCertPath(String rootCertPath) {
		this.rootCertPath = rootCertPath;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}



}
