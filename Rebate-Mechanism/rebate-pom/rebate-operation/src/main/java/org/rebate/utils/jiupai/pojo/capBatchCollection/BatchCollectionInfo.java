package org.rebate.utils.jiupai.pojo.capBatchCollection;

import java.io.Serializable;

public class BatchCollectionInfo implements Serializable {

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 交易流水
     */
    private String jrnNo;

    /**
     * 商户订单号
     */
    private String mercOrdNo;

    /**
     * 交易金额 单位：分
     */
    private String txnAmt;

    /**
     * 币种
     */
    private String ccy;

    /**
     * 银行
     */
    private String bankCode;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 用户名
     */
    private String usrNm;

    /**
     * 证件类型
     */
    private String idTyp;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 银行预留手机号
     */
    private String phoneNo;

    /**
     * 商户交易时间 yyyyMMddHHmmss
     */
    private String reqTm;

    /**
     * 备注，用途等
     */
    private String rmk;


    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getJrnNo() {
        return jrnNo;
    }

    public void setJrnNo(String jrnNo) {
        this.jrnNo = jrnNo;
    }

    public String getMercOrdNo() {
        return mercOrdNo;
    }

    public void setMercOrdNo(String mercOrdNo) {
        this.mercOrdNo = mercOrdNo;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUsrNm() {
        return usrNm;
    }

    public void setUsrNm(String usrNm) {
        this.usrNm = usrNm;
    }

    public String getIdTyp() {
        return idTyp;
    }

    public void setIdTyp(String idTyp) {
        this.idTyp = idTyp;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getReqTm() {
        return reqTm;
    }

    public void setReqTm(String reqTm) {
        this.reqTm = reqTm;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}

