package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * Entity - 推送消息会员关系
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_enduser_message")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_enduser_message_sequence")
public class MsgEndUser extends BaseEntity {

  private static final long serialVersionUID = 8059526848027012082L;

  /** 接受消息的用户实体 */
  private EndUser endUser;

  /** 消息实体 */
  private MessageInfo message;

  /** 是否已经推送 */
  private Boolean isPush;

  /** 是否已读 */
  private Boolean isRead;

  @ManyToOne
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @ManyToOne
  public MessageInfo getMessage() {
    return message;
  }

  public void setMessage(MessageInfo message) {
    this.message = message;
  }

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public Boolean getIsPush() {
    return isPush;
  }

  public void setIsPush(Boolean isPush) {
    this.isPush = isPush;
  }


}
