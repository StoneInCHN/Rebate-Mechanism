package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 商家货款结算与订单记录关系表
 * 
 * 一条货款结算记录对应1个或多个订单
 * 
 * @author Andrea
 *
 */

@Entity
@Table(name = "rm_clearing_order_relation", indexes = {@Index(name = "clearingRecIdIndex",
    columnList = "clearingRecId")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_clearing_order_relation_sequence")
public class ClearingOrderRelation extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 结算记录ID
   */
  private Long clearingRecId;

  /**
   * 关联的订单
   */
  private Order order;



  public Long getClearingRecId() {
    return clearingRecId;
  }

  public void setClearingRecId(Long clearingRecId) {
    this.clearingRecId = clearingRecId;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orderInfo")
  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

}
