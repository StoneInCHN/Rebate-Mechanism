package org.rebate.json.response;



public class SalesManReport {
	  /**
	   * Id
	   */
	  private Long id;
	  /**
	   * 用户名
	   */
	  private String userName;
	  
	  /**
	   * 昵称
	   */
	  private String nickName;

	  /**
	   * 用户头像
	   */
	  private String userPhoto;

	  /**
	   * 手机号
	   */
	  private String cellPhoneNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getCellPhoneNum() {
		return cellPhoneNum;
	}

	public void setCellPhoneNum(String cellPhoneNum) {
		this.cellPhoneNum = cellPhoneNum;
	}
  


  
}
