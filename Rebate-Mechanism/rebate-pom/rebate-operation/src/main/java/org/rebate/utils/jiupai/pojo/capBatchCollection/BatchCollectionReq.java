package org.rebate.utils.jiupai.pojo.capBatchCollection;

import java.util.List;

public class BatchCollectionReq{

    /**
     * 批量代收数据列表
     */
    private List<BatchCollectionInfo> infoList;
    /**
     * 代收笔数
     */
    private int count;
    /**
     * 批次号
     */
    private String batchNo;

    public List<BatchCollectionInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<BatchCollectionInfo> infoList) {
        this.infoList = infoList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

}
