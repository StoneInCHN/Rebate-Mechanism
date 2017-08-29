package org.rebate.utils.jiupai.pojo.capBatchTransfer;


public class BatchTransferInfo{

    /**
     * 交易流水
     */
    private String jrnNo;

    /**
     * 商户订单号
     */
    private String mercOrdNo;

    /**
     * 业务类型 PC：代发工资（payroll credit）
     */
    private String busTyp;

    /**
     * 交易金额 单位：分
     */
    private String txnAmt;

    /**
     * 币种
     */
    private String ccy;

    /**
     * 收款行
     */
    private String bankCode;

    /**
     * 收款银行卡号
     */
    private String cardNo;

    /**
     * 对公/对私标识 TB：对公；TC：对私
     */
    private String cardFlag;

    /**
     * 联行号
     */
    private String bankKey;

    /**
     * 用户名
     */
    private String usrNm;

    /**
     * 商户交易时间 yyyyMMddHHmmss
     */
    private String reqTm;

    /**
     * 备注，用途等
     */
    private String rmk;

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

    public String getBusTyp() {
        return busTyp;
    }

    public void setBusTyp(String busTyp) {
        this.busTyp = busTyp;
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

    public String getCardFlag() {
        return cardFlag;
    }

    public void setCardFlag(String cardFlag) {
        this.cardFlag = cardFlag;
    }

    public String getBankKey() {
        return bankKey;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    public String getUsrNm() {
        return usrNm;
    }

    public void setUsrNm(String usrNm) {
        this.usrNm = usrNm;
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

