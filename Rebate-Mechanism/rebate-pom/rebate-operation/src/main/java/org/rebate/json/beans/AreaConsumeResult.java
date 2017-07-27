package org.rebate.json.beans;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 *  每日地区消费/交易额结果
 * 
 *
 */
public class AreaConsumeResult implements Serializable {

  private static final long serialVersionUID = 1L;

	/** 地区ID */
	private Long areaID;
	/**
	 * 消费金额
 	*/
	private BigDecimal amount; 
	/**
 	* 消费折扣
 	*/
	private BigDecimal sellerDiscount;
	
	public Long getAreaID() {
		return areaID;
	}
	public void setAreaID(Long areaID) {
		this.areaID = areaID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getSellerDiscount() {
		return sellerDiscount;
	}
	public void setSellerDiscount(BigDecimal sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}

}
