package org.rebate.utils.allinpay;

import com.allinpay.XmlTools;

/**
 */
public class ManageSign {
  
  private static TranxCon tranxContants = new TranxCon();

  /**
   * 报文签名
   * 
   * @param msg
   * @return 日期：Sep 9, 2012
   * @throws Exception
   */
  public static String signMsg(String xml) throws Exception {
    xml = XmlTools.signMsg(xml, tranxContants.pfxPath, tranxContants.pfxPassword, false);
    return xml;
  }

  /**
   * 验证签名
   * 
   * @param msg
   * @return 日期：Sep 9, 2012
   * @throws Exception
   */
  public static boolean verifyMsg(String msg, boolean isFront) throws Exception {
    boolean flag = XmlTools.verifySign(msg, tranxContants.tltcerPath, false, isFront);
    System.out.println("验签结果[" + flag + "]");
    return flag;
  }

  public static void main(String[] args) throws Exception{
	  String xmlString = "<?xml version=\"1.0\" encoding=\"GBK\"?><AIPG>  <INFO>    <TRX_CODE>200003</TRX_CODE>    <VERSION>03</VERSION>    <DATA_TYPE>2</DATA_TYPE>    <REQ_SN>2006040000004451495764988336</REQ_SN>       <SIGNED_MSG></SIGNED_MSG>  </INFO></AIPG>  ";
	  String newXml = signMsg(xmlString);
	  System.out.println(newXml);
  }
}
