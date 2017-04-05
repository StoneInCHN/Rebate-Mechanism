package org.rebate.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "rm_user_recommend_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_recommend_relation_sequence")
public class UserRecommendRelation extends BaseEntity {


  private static final long serialVersionUID = 1L;

  // /**
  // * 用户ID
  // */
  // private Long userId;

  /**
   * 用户
   */
  private EndUser endUser;

  /**
   * 上级推荐人
   */
  private UserRecommendRelation parent;

  /**
   * 下级推荐的用户
   */
  private Set<UserRecommendRelation> children = new HashSet<UserRecommendRelation>();

  // /**
  // * 推荐层级状态
  // */
  // private CommonStatus status;
  //
  //
  // public CommonStatus getStatus() {
  // return status;
  // }
  //
  // public void setStatus(CommonStatus status) {
  // this.status = status;
  // }

  @OneToOne
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
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
