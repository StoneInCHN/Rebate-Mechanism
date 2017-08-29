package org.rebate.utils.jiupai.pojo;

import java.io.Serializable;

public class BaseReq implements Serializable {

	private static final long serialVersionUID = 6956849943470769962L;
	
	private String version = "1.0";
    private String requestId = "NUL";
    private String merchantId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
