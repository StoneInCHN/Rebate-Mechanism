package org.rebate.json.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import org.rebate.entity.Area;
import org.rebate.entity.Order;
import org.rebate.entity.SellerClearingRecord;


/**
 *  商家已结算货款
 * 
 *
 */
public class SellerClearingOrders implements Serializable {

  private static final long serialVersionUID = 1L;

	/** 
	 * 货款记录 
	 */
	private SellerClearingRecord record;
	/**
	 * 货款记录对应的订单
 	*/
	private Set<Order> orderSet;
	
	public SellerClearingRecord getRecord() {
		return record;
	}
	public void setRecord(SellerClearingRecord record) {
		this.record = record;
	}
	public Set<Order> getOrderSet() {
		return orderSet;
	}
	public void setOrderSet(Set<Order> orderSet) {
		this.orderSet = orderSet;
	} 

}
