package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 银行卡
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_bank_card", indexes = {@Index(name = "isDefault", columnList = "isDefault")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_bank_card_sequence")
public class BankCard extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 持卡人姓名
   */
  private String ownerName;

  /**
   * 银行卡号
   */
  private String cardNum;

  /**
   * 银行类别
   */
  private String bankName;

  /**
   * 银行卡类型
   */
  private String cardType;

  /**
   * 用户
   */
  private EndUser endUser;

  /**
   * 是否为默认银行卡
   */
  private Boolean isDefault;

  /**
   * 预留手机号
   */
  private String reservedMobile;

  /**
   * 银行卡图标
   */
  private String bankLogo;


  @Column(length = 200)
  public String getBankLogo() {
    return bankLogo;
  }

  public void setBankLogo(String bankLogo) {
    this.bankLogo = bankLogo;
  }

  @Column(length = 20, nullable = false)
  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  @Column(length = 20)
  public String getReservedMobile() {
    return reservedMobile;
  }

  public void setReservedMobile(String reservedMobile) {
    this.reservedMobile = reservedMobile;
  }

  @Column(length = 50, nullable = false)
  public String getCardNum() {
    return cardNum;
  }

  public void setCardNum(String cardNum) {
    this.cardNum = cardNum;
  }

  @Column(length = 50, nullable = false)
  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }


  @Column(length = 20)
  public String getCardType() {
    return cardType;
  }

  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }
  @Column(nullable = false)
  public Boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }


}
