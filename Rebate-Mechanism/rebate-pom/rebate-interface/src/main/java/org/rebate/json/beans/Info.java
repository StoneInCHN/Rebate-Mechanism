package org.rebate.json.beans;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "TRX_CODE", "VERSION", "DATA_TYPE", "REQ_SN", "RET_CODE", "ERR_MSG", "SIGNED_MSG"})
public class Info {
		
	/** 交易代码 */
	private String TRX_CODE;
	/** 版本 */
	private String VERSION;
	/** 数据格式 */
	private String DATA_TYPE;
	/** 交易批次号 */
	private String REQ_SN;
	/** 返回代码 */
	private String RET_CODE;
	/** 错误信息 */
	private String ERR_MSG;
	/** 签名信息 */
	private String SIGNED_MSG;
	
	public String getTRX_CODE() {
		return TRX_CODE;
	}
	public void setTRX_CODE(String tRX_CODE) {
		TRX_CODE = tRX_CODE;
	}
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	public String getREQ_SN() {
		return REQ_SN;
	}
	public void setREQ_SN(String rEQ_SN) {
		REQ_SN = rEQ_SN;
	}
	public String getSIGNED_MSG() {
		return SIGNED_MSG;
	}
	public void setSIGNED_MSG(String sIGNED_MSG) {
		SIGNED_MSG = sIGNED_MSG;
	}
	public String getRET_CODE() {
		return RET_CODE;
	}
	public void setRET_CODE(String rET_CODE) {
		RET_CODE = rET_CODE;
	}
	public String getERR_MSG() {
		return ERR_MSG;
	}
	public void setERR_MSG(String eRR_MSG) {
		ERR_MSG = eRR_MSG;
	}
	
	public Info(String TRX_CODE, String VERSION, String DATA_TYPE, String REQ_SN,
			String RET_CODE, String ERR_MSG, String SIGNED_MSG){
		super();
		this.TRX_CODE = TRX_CODE;
		this.VERSION = VERSION;
		this.DATA_TYPE = DATA_TYPE;
		this.REQ_SN = REQ_SN;
		this.RET_CODE = RET_CODE;
		this.ERR_MSG = ERR_MSG;
		this.SIGNED_MSG = SIGNED_MSG;
	}
	
	public Info(){
		super();
	}
}
