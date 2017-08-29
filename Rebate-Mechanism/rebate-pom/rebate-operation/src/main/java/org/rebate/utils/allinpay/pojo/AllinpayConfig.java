package org.rebate.utils.allinpay.pojo;

import java.io.File;

import org.rebate.beans.Setting;
import org.rebate.utils.SettingUtils;

/**
 * 
 *通联参数配置
 */
public class AllinpayConfig {
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
	  
	  public AllinpayConfig() {}
	  private static AllinpayConfig config;
	  public static AllinpayConfig getConfig() {
	     if(config == null) {
	          config = new AllinpayConfig();//初始化通联基础数据
	          Setting setting = SettingUtils.get();
	      	  String path = config.getClass().getResource("/").getPath();
	    	  String pfxPath = path + File.separator + setting.getPfxPath();
	    	  String tltcerPath = path + File.separator + setting.getTltcerPath(); 
	    	  config.setPfxPath(pfxPath);  
	    	  config.setTltcerPath(tltcerPath);
	    	  config.setUrl(setting.getAllinpayUrl());
	    	  config.setPfxPassword(setting.getPfxPassword());
	    	  config.setUserName(setting.getAllinpayUserName());
	    	  config.setPassword(setting.getAllinpayPassword());
	    	  config.setMerchantId(setting.getAllinpayMerchantId());
	    	  config.setBusinessCode(setting.getAllinpayBusinessCode());
	      }
	      return config;
	  }

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
