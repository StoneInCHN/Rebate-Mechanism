package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商家实体
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_seller")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_sequence")
public class Seller extends BaseEntity{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * 门店照片
   */
  private MultipartFile storePicture;
  
  /**
   * 门店照片URL
   */
  private String storePictureUrl;
  
  /**
   * 商家手机号
   */
  private String sellerCellPhoneNum;
  
  /**
   * 商家类别
   */
  private SellerCategory sellerCategory;
  
  
  

}
