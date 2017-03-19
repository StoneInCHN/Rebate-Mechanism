package org.rebate.utils.wechat;


/**
 * 
 * 接口凭证
 * @author jack
 * @version 1.0
 *
 */
public class Token 
{
	//接口访问凭证
	private String access_token;
	
	//接口访问凭证的有效时间
	private String expires_in;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
}
