package org.rebate.utils.jiupai.pojo.capSingleTransfer;


/**
 * 单笔代付 请求参数
 *
 */
public class SingleTransferReq{
		
	private String mcSequenceNo;//商户交易流水
	private String mcTransDateTime;//商户交易时间
	private String orderNo;//订单号
	private String amount;//交易金额  单位：分
	private String cardNo;//银行卡号
	private String accName;//账户户名
	private String accType;//账户类型   0:卡   2:对公账号
	private String crdType;//银行卡类型   00:借记卡(只支持)  01:信用卡
	private String remark;//订单备注
	private String callBackUrl;//回调地址URL
	
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
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getCrdType() {
		return crdType;
	}
	public void setCrdType(String crdType) {
		this.crdType = crdType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCallBackUrl() {
		return callBackUrl;
	}
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
	
}

