package org.rebate.json.beans;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "NOTIFY_SN"})
public class Notify {
		
	/** 原请求报文的交易流水号 */
	private String NOTIFY_SN;

	public String getNOTIFY_SN() {
		return NOTIFY_SN;
	}

	public void setNOTIFY_SN(String nOTIFY_SN) {
		NOTIFY_SN = nOTIFY_SN;
	}
	public Notify(String NOTIFY_SN){
		super();
		this.NOTIFY_SN = NOTIFY_SN;
	}
	
	public Notify(){
		super();
	}
}
