package org.rebate.json.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="AIPG")
@XmlType(propOrder = { "INFO"})
public class TxnResultNotify {
		
	/** INFO */
	private Info INFO;	
	
	@XmlElement(name="INFO")
	public Info getINFO() {
		return INFO;
	}

	public void setINFO(Info iNFO) {
		INFO = iNFO;
	}
	
	
	public TxnResultNotify(Info INFO, Notify NOTIFY){
		super();
		this.INFO = INFO;
	}
	
	public TxnResultNotify(){
		super();
	}
}
