
package org.rebate.beans;

/**
 * 公共参数
 * 
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** common-config.xml文件路径 */
	public static final String COMMON_CONFIG_XML_PATH = "/common-config.xml";

	/** common-config.properties文件路径 */
	public static final String COMMON_CONFIG_PROPERTIES_PATH = "/common-config.properties";
	
    /** 请求成功 */
	public static final String SUCCESS = "0000";// 请求成功
	
	/** 操作失败 */
	public static final String FAIL_COMMON = "1000"; // 操作失败
	
	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}