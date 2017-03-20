package org.rebate.beans;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 登录令牌
 * 
 */
public class AuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5898441540965086534L;

	/** 验证码ID */
	private String captchaId;

	/** 验证码 */
	private String captcha;
	
	/** 注册后自动登录 */
	private Boolean isAutoLogin;

	/**
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param captchaId
	 *            验证码ID
	 * @param captcha
	 *            验证码
	 * @param rememberMe
	 *            记住我
	 * @param host
	 *            IP
	 * @param isAutoLogin
	 *            注册后自动登录           
	 */
	public AuthenticationToken(String username, String password, String captchaId, String captcha, boolean rememberMe, String host,Boolean isAutoLogin) {
		super(username, password, rememberMe, host);
		this.captchaId = captchaId;
		this.captcha = captcha;
		this.isAutoLogin = isAutoLogin;
	}

	/**
	 * 获取验证码ID
	 * 
	 * @return 验证码ID
	 */
	public String getCaptchaId() {
		return captchaId;
	}

	/**
	 * 设置验证码ID
	 * 
	 * @param captchaId
	 *            验证码ID
	 */
	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	/**
	 * 获取验证码
	 * 
	 * @return 验证码
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * 设置验证码
	 * 
	 * @param captcha
	 *            验证码
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
	/**
	 * 获取注册后是否自动登录
	 * @return isAutoLogin
	 */
    public Boolean getIsAutoLogin() {
      return isAutoLogin;
    }
  
    /**
     * 设置注册后是否自动登录
     * @param isAutoLogin
     */
    public void setIsAutoLogin(Boolean isAutoLogin) {
      this.isAutoLogin = isAutoLogin;
    }

	
}