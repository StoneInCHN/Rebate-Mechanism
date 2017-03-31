package org.rebate.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;
import org.rebate.entity.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 推送消息
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_message_info")
@Indexed(index = "messageInfo")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_message_info_sequence")
public class MessageInfo extends BaseEntity {

  private static final long serialVersionUID = 1170442128165498366L;

  /** 消息标题 */
  private String messageTitle;

  /** 消息内容 */
  private String messageContent;

  /** 消息、会员对应关系实体 */
  private Set<MsgEndUser> msgUser = new HashSet<MsgEndUser>();


  @JsonProperty
  public String getMessageTitle() {
    return messageTitle;
  }

  public void setMessageTitle(String messageTitle) {
    this.messageTitle = messageTitle;
  }

  @JsonProperty
  public String getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
  }

  @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  public Set<MsgEndUser> getMsgUser() {
    return msgUser;
  }

  public void setMsgUser(Set<MsgEndUser> msgUser) {
    this.msgUser = msgUser;
  }

}
