package org.rebate.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 用户推荐关系表
 * 
 * 一个用户可推荐其他多个用户，并只能被一个非自己的用户推荐
 * 
 * @author Andrea
 *
 */

@Entity
@Table(name = "rm_user_recommend_relation", indexes = {@Index(name = "cellPhoneNumIndex",
    columnList = "cellPhoneNum")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_recommend_relation_sequence")
public class UserRecommendRelation extends BaseEntity {


  private static final long serialVersionUID = 1L;

  // /**
  // * 用户ID
  // */
  // private Long userId;

  /**
   * 手机号
   */
  private String cellPhoneNum;

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 上级推荐人
   */
  private UserRecommendRelation parent;

  /**
   * 下级推荐的用户
   */
  private Set<UserRecommendRelation> children = new HashSet<UserRecommendRelation>();


  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public UserRecommendRelation getParent() {
    return parent;
  }

  public void setParent(UserRecommendRelation parent) {
    this.parent = parent;
  }

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  public Set<UserRecommendRelation> getChildren() {
    return children;
  }

  public void setChildren(Set<UserRecommendRelation> children) {
    this.children = children;
  }

}
