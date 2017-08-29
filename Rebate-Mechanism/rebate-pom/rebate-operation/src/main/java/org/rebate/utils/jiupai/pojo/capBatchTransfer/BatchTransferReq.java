package org.rebate.utils.jiupai.pojo.capBatchTransfer;

import java.util.List;

public class BatchTransferReq {
	
    /**
     * 代付数据详细列表
     */
    private List<BatchTransferInfo> infoList;

    /**
     * 此次请求数据条数
     */
    private int count;

    /**
     * 批次号
     */
    private String batchNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public List<BatchTransferInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<BatchTransferInfo> infoList) {
        this.infoList = infoList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
