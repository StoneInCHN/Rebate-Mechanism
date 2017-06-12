package org.rebate.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.AdminStatus;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name = "rm_admin")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_admin_sequence")
public class Admin extends BaseEntity {

  private static final long serialVersionUID = -7519486823153844426L;


  /** 用户名 */
  private String username;

  /** 密码 */
  private String password;

  /** E-mail */
  private String email;

  /** 姓名 */
  private String name;


  /** 最后登录日期 */
  private Date loginDate;

  /** 最后登录IP */
  private String loginIp;


  /** 帐号状态 */
  private AdminStatus adminStatus;

  /** 角色 */
  private Set<Role> roles = new HashSet<Role>();

  /**
   * 是否为内置账户
   */
  private Boolean isSystem;

  /**
   * 预留手机号
   */
  private String cellPhoneNum;

  /**
   * 获取用户名
   * 
   * @return 用户名
   */
  @NotEmpty(groups = Save.class)
  @Length(min = 2, max = 20)
  @Column(nullable = false, updatable = false, unique = true, length = 100)
  public String getUsername() {
    return username;
  }

  /**
   * 设置用户名
   * 
   * @param username 用户名
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * 获取密码
   * 
   * @return 密码
   */
  @NotEmpty(groups = Save.class)
  @Pattern(regexp = "^[^\\s&\"<>]+$")
  @Length(min = 4, max = 20)
  @Column(nullable = false)
  public String getPassword() {
    return password;
  }

  /**
   * 设置密码
   * 
   * @param password 密码
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * 获取E-mail
   * 
   * @return E-mail
   */
  @NotEmpty
  @Email
  @Length(max = 200)
  @Column(nullable = false)
  public String getEmail() {
    return email;
  }

  /**
   * 设置E-mail
   * 
   * @param email E-mail
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * 获取姓名
   * 
   * @return 姓名
   */
  @Length(max = 200)
  public String getName() {
    return name;
  }

  /**
   * 设置姓名
   * 
   * @param name 姓名
   */
  public void setName(String name) {
    this.name = name;
  }



  /**
   * 获取最后登录日期
   * 
   * @return 最后登录日期
   */
  public Date getLoginDate() {
    return loginDate;
  }

  /**
   * 设置最后登录日期
   * 
   * @param loginDate 最后登录日期
   */
  public void setLoginDate(Date loginDate) {
    this.loginDate = loginDate;
  }

  /**
   * 获取最后登录IP
   * 
   * @return 最后登录IP
   */
  public String getLoginIp() {
    return loginIp;
  }

  /**
   * 设置最后登录IP
   * 
   * @param loginIp 最后登录IP
   */
  public void setLoginIp(String loginIp) {
    this.loginIp = loginIp;
  }


  /**
   * 获取分销商状态
   * 
   * @return 分销商状态
   */
  @Column(nullable = false)
  public AdminStatus getAdminStatus() {
    return adminStatus;
  }

  /**
   * 设置分销商状态
   * 
   * @param adminStatus 分销商状态
   */
  public void setAdminStatus(AdminStatus adminStatus) {
    this.adminStatus = adminStatus;
  }

  /**
   * 获取角色
   * 
   * @return 角色
   */
  @NotEmpty
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "rm_admin_role")
  public Set<Role> getRoles() {
    return roles;
  }

  /**
   * 设置角色
   * 
   * @param roles 角色
   */
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Column(length = 4)
  public Boolean getIsSystem() {
    return isSystem;
  }

  public void setIsSystem(Boolean isSystem) {
    this.isSystem = isSystem;
  }
  
  @Column(length = 20)
  @JsonProperty
  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  /*
   * @OneToOne(fetch = FetchType.LAZY) public Distributor getDistributor() { return distributor; }
   * 
   * public void setDistributor(Distributor distributor) { this.distributor = distributor; }
   */



}
