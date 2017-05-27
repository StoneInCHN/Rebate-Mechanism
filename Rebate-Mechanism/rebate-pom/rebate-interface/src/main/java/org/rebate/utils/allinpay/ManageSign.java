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


}
