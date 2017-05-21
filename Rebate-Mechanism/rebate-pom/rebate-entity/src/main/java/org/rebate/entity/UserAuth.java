package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 用户实名认证
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_user_auth", indexes = {@Index(name = "userId", columnList = "userId"),
    @Index(name = "isAuth", columnList = "isAuth")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_auth_sequence")
public class UserAuth extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 真实姓名
   */
  private String realName;

  /**
   * 身份证号
   */
  private String idCardNo;

  /**
   * 身份证正面照
   */
  private String idCardFrontPic;
  /**
   * 身份证反面照
   */
  private String idCardBackPic;

  /**
   * 用户id
   */
  private Long userId;

  /**
   * 是否完成实名认证并绑定银行卡成功
   */
  private Boolean isAuth;


  @Column(length = 20)
  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  @Column(length = 30)
  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
  }

  @Column(length = 200)
  public String getIdCardFrontPic() {
    return idCardFrontPic;
  }

  public void setIdCardFrontPic(String idCardFrontPic) {
    this.idCardFrontPic = idCardFrontPic;
  }

  @Column(length = 200)
  public String getIdCardBackPic() {
    return idCardBackPic;
  }

  public void setIdCardBackPic(String idCardBackPic) {
    this.idCardBackPic = idCardBackPic;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Boolean getIsAuth() {
    return isAuth;
  }

  public void setIsAuth(Boolean isAuth) {
    this.isAuth = isAuth;
  }



}
