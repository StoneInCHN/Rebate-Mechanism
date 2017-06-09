package org.rebate.json.request;

import org.rebate.json.base.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

public class AuthRequest extends BaseRequest {

  /**
   * 真实姓名
   */
  private String realName;

  /**
   * 身份证号码
   */
  private String cardNo;

  /**
   * 用户身份证正面照
   */
  private MultipartFile cardFrontPic;

  /**
   * 用户身份证反面照
   */
  private MultipartFile cardBackPic;


  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public MultipartFile getCardFrontPic() {
    return cardFrontPic;
  }

  public void setCardFrontPic(MultipartFile cardFrontPic) {
    this.cardFrontPic = cardFrontPic;
  }

  public MultipartFile getCardBackPic() {
    return cardBackPic;
  }

  public void setCardBackPic(MultipartFile cardBackPic) {
    this.cardBackPic = cardBackPic;
  }


}
