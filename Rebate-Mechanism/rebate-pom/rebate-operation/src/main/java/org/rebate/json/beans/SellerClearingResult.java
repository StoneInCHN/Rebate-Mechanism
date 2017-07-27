package org.rebate.json.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.rebate.entity.Area;


/**
 *  商家已结算货款
 * 
 *
 */
public class SellerClearingResult implements Serializable {

  private static final long serialVersionUID = 1L;

	/** 交易日期 */
	private String paymentDate;
	/**
	 * 商户名称
 	*/
	private String sellerName; 
	/**
 	* 交易额
 	*/
	private BigDecimal amount;
	/**
 	* 商家结算金额
 	*/
	private BigDecimal sellerIncome;
	/**
 	* 毛利润
 	*/
	private BigDecimal profit;
	/**
 	* 交易笔数
 	*/
	private int count;
	
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getSellerIncome() {
		return sellerIncome;
	}
	public void setSellerIncome(BigDecimal sellerIncome) {
		this.sellerIncome = sellerIncome;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	

}
