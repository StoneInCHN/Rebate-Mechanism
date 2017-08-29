package org.rebate.utils.jiupai.pojo.capOrderQueryReq;

public class OrderQueryReq {
	
	private String  mcSequenceNo;//商户交易流水
	private String  mcTransDateTime;//商户交易时间
	private String  orderNo;//原交易订单号
	private String  amount;//原交易金额 (单位:分)
	
	public OrderQueryReq(String  mcSequenceNo, String  mcTransDateTime, String  orderNo, String  amount){
		this.mcSequenceNo = mcSequenceNo;
		this.mcTransDateTime = mcTransDateTime;
		this.orderNo = orderNo;
		this.amount = amount;
	}
	public String getMcSequenceNo() {
		return mcSequenceNo;
	}
	public void setMcSequenceNo(String mcSequenceNo) {
		this.mcSequenceNo = mcSequenceNo;
	}
	public String getMcTransDateTime() {
		return mcTransDateTime;
	}
	public void setMcTransDateTime(String mcTransDateTime) {
		this.mcTransDateTime = mcTransDateTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	

}
