package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 商家申请实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_seller_application")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_application_sequence")
public class SellerApplication extends BaseEntity{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  
  

}
