package org.rebate.utils.jiupai.pojo.capBatchQuery;


public class BatchQueryReq {

    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 页面大小
     */
	private Integer pageSize; 
	/**
	 * 当前页数
	 */
	private Integer pageNum;
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}



}
