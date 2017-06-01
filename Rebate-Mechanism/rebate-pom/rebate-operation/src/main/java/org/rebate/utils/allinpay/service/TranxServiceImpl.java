package org.rebate.utils.allinpay.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.beans.Setting;
import org.rebate.entity.BankCard;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.service.BankCardService;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.TimeUtils;
import org.rebate.utils.allinpay.pojo.TranxCon;
import org.rebate.utils.allinpay.tools.FileUtil;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.payreq.Body;
import com.aipg.payreq.Trans_Detail;
import com.aipg.payreq.Trans_Sum;
import com.aipg.rtreq.Trans;
import com.aipg.signquery.NSignReq;
import com.aipg.signquery.QSignDetail;
import com.aipg.transquery.TransQueryReq;
import com.allinpay.XmlTools;

public class TranxServiceImpl {
	
  TranxCon tranxContants = null;
  SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
  Setting setting = SettingUtils.get();
	
  public void init(){//初始化通联基础数据
	  tranxContants = new TranxCon();
	  String path = tranxContants.getClass().getResource("/").getPath();
	  String pfxPath = path + File.separator + setting.getPfxPath();
	  String tltcerPath = path + File.separator + setting.getTltcerPath(); 
	  tranxContants.setPfxPath(pfxPath);  
	  tranxContants.setTltcerPath(tltcerPath);
	  tranxContants.setUrl(setting.getAllinpayUrl());
	  tranxContants.setPfxPassword(setting.getPfxPassword());
	  tranxContants.setUserName(setting.getAllinpayUserName());
	  tranxContants.setPassword(setting.getAllinpayPassword());
	  tranxContants.setMerchantId(setting.getAllinpayMerchantId());
	  tranxContants.setBusinessCode(setting.getAllinpayBusinessCode());
  }
  


  /**
   * 批量代付
   * 
   * @throws Exception
   */
  public List<SellerClearingRecord> batchDaiFu(boolean isTLTFront, String totalItem, String totalSum,
      List<SellerClearingRecord> records, BankCardService bankCardService) throws Exception {
    
    String xmlRequest = "";
    AipgReq aipg = new AipgReq();
    InfoReq info = makeReq("100002");//批量代付的交易代码：100002
    aipg.setINFO(info);
    
    Body body = new Body();
    Trans_Sum trans_sum = new Trans_Sum();
    trans_sum.setBUSINESS_CODE(tranxContants.getBusinessCode()); 
    trans_sum.setMERCHANT_ID(tranxContants.getMerchantId());
    trans_sum.setSUBMIT_TIME(TimeUtils.format("yyyyMMddHHmmss", new Date().getTime()));
    trans_sum.setTOTAL_ITEM(totalItem);
    trans_sum.setTOTAL_SUM(totalSum);
    body.setTRANS_SUM(trans_sum);
    
    List<Trans_Detail> transList = new ArrayList<Trans_Detail>();
    for (int i = 0; i < records.size(); i++) {
      SellerClearingRecord record = records.get(i);
      record.setReqSn(info.getREQ_SN());
      BankCard bankCard = bankCardService.find(record.getBankCardId());
      if (bankCard == null) {//批量代付的话，是整批次成功或整批次失败，不允许某一单银行卡为空
		return null;
	  }
      Trans_Detail trans_detail = new Trans_Detail();
      String sn = genSn(i);
      trans_detail.setSN(sn);//记录序号，同一个请求内必须唯一，从0001,0002..开始递增
      record.setSn(sn);
      trans_detail.setACCOUNT_NAME(bankCard.getOwnerName()); // 银行卡姓名
      trans_detail.setACCOUNT_PROP("0"); // 0私人，1公司。不填时，默认为私人0。
      trans_detail.setACCOUNT_NO(bankCard.getCardNum()); // 银行卡账号
      BigDecimal payAmount = record.getAmount().subtract(record.getHandlingCharge());
      trans_detail.setAMOUNT(payAmount.multiply(new BigDecimal(100)).setScale(0).toString());//金额单位：分
      //trans_detail.setBANK_CODE("0105");//银行代码："0"+"105"对应中国建设银行，不传值的情况下，通联可以通过银行卡账号自动识别所属银行
      trans_detail.setCURRENCY("CNY");//人民币：CNY, 港元：HKD，美元：USD。不填时，默认为人民币

        transList.add(trans_detail);
    }
    body.setDetails(transList);
    aipg.addTrx(body);

    xmlRequest = XmlTools.buildXml(aipg, true);
    
    String xmlResponse = isFront(xmlRequest, isTLTFront, tranxContants.getUrl());
    
    if (xmlResponse != null) {
        Document doc = DocumentHelper.parseText(xmlResponse);
        Element root = doc.getRootElement();// AIPG
        Element infoElement = root.element("INFO");
        String ret_code = infoElement.element("RET_CODE").getText();
        String err_msg = infoElement.element("ERR_MSG").getText();
        if ("0000".equals(ret_code)) {//0000表示通联接受请求并处理成功，RET_CODE是一个中间状态
        	System.out.println("批量代付成功！RET_CODE="+ret_code+",ERR_MSG="+err_msg);
        	return records;
		}else {
			System.out.println("批量代付失败！RET_CODE="+ret_code+",ERR_MSG="+err_msg);
		}
	}
    return null;
    
  }

  /**
   * 日期：Sep 4, 2012 功能：
   * 
   * @throws Exception
   */
  public void batchDaiShou(String url, boolean isTLTFront) throws Exception {

    String xml = "";
    AipgReq aipg = new AipgReq();
    InfoReq info = makeReq("100001");
    aipg.setINFO(info);
    Body body = new Body();
    Trans_Sum trans_sum = new Trans_Sum();
    trans_sum.setBUSINESS_CODE("10600");
    trans_sum.setMERCHANT_ID(tranxContants.getMerchantId());
    trans_sum.setTOTAL_ITEM("3");
    trans_sum.setTOTAL_SUM("100002");
    body.setTRANS_SUM(trans_sum);
    List<Trans_Detail> transList = new ArrayList<Trans_Detail>();
    Trans_Detail trans_detail = new Trans_Detail();
    Trans_Detail trans_detail2 = new Trans_Detail();
    Trans_Detail trans_detail3 = new Trans_Detail();
    Trans_Detail trans_detail4 = new Trans_Detail();
    trans_detail.setSN("0001");
    trans_detail.setACCOUNT_NAME("许燕");
    trans_detail.setACCOUNT_PROP("0");
    trans_detail.setACCOUNT_NO("603023061216191772");
    trans_detail.setBANK_CODE("403");
    trans_detail.setAMOUNT("100000");
    trans_detail.setCURRENCY("NBK");
    // trans_detail.setSETTGROUPFLAG("xCHM");
    // trans_detail.setSUMMARY("分组清算");
    // trans_detail.setUNION_BANK("234234523523");
    transList.add(trans_detail);

    trans_detail2.setSN("0002");
    trans_detail2.setACCOUNT_NAME("系统对接测试02");
    // trans_detail.setACCOUNT_PROP("1") ;
    trans_detail2.setACCOUNT_NO("622682-0013800763464");
    trans_detail2.setBANK_CODE("403");
    trans_detail2.setAMOUNT("1");
    trans_detail2.setCURRENCY("CNY");
    trans_detail2.setUNION_BANK("234234523523");
    // trans_detail2.setSETTGROUPFLAG("CHM");
    // trans_detail2.setSUMMARY("分组清算");
    transList.add(trans_detail2);

    trans_detail3.setSN("0003");
    trans_detail3.setACCOUNT_NAME("系统对接测试03");
    // trans_detail.setACCOUNT_PROP("1") ;
    trans_detail3.setACCOUNT_NO("621034-32645-1271");
    trans_detail3.setBANK_CODE("403");
    trans_detail3.setAMOUNT("1");
    trans_detail3.setUNION_BANK("234234523523");
    // trans_detail3.setSETTGROUPFLAG("CHM");
    // trans_detail3.setSUMMARY("分组清算");
    transList.add(trans_detail3);
    body.setDetails(transList);
    aipg.addTrx(body);

    xml = XmlTools.buildXml(aipg, true);// .replaceAll("</INFO>",
                                        // "</INFO><BODY>").replaceAll("</AIPG>", "</BODY></AIPG>");
    isFront(xml, isTLTFront, url);
  }

  /**
   * 组装报文头部
   * 
   * @param trxcod
   * @return 日期：Sep 9, 2012
   */
  private InfoReq makeReq(String trxcod) {

    InfoReq info = new InfoReq();
    info.setTRX_CODE(trxcod);
    info.setREQ_SN(tranxContants.getMerchantId() + String.valueOf(System.currentTimeMillis()));
    info.setUSER_NAME(tranxContants.getUserName());
    info.setUSER_PASS(tranxContants.getPassword());
    info.setMERCHANT_ID(tranxContants.getMerchantId());
    info.setLEVEL("5");
    info.setDATA_TYPE("2");
    info.setVERSION("03");
    return info;
  }

  public String sendXml(String xml, String url, boolean isFront)
      throws UnsupportedEncodingException, Exception {
    System.out.println("======================发送报文======================：\n" + xml);
    String resp = XmlTools.send(url, new String(xml.getBytes(), "UTF-8"));
    System.out.println("======================响应内容======================");
    // System.out.println(new String(resp.getBytes(),"GBK")) ;
    boolean flag = this.verifyMsg(resp, tranxContants.getTltcerPath(), isFront);
    if (flag) {
      System.out.println("响应内容验证通过");
    } else {
      System.out.println("响应内容验证不通过");
    }
    return resp;
  }

  public String isFront(String xml, boolean flag, String url) {
    try {
      if (!flag) {
        xml = this.signMsg(xml);
      } else {
        xml = xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
      }
      return sendXml(xml, url, flag);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 报文签名
   * 
   * @param msg
   * @return 日期：Sep 9, 2012
   * @throws Exception
   */
  public String signMsg(String xml) throws Exception {
    xml = XmlTools.signMsg(xml, tranxContants.getPfxPath(), tranxContants.getPfxPassword(), false);
    return xml;
  }

  /**
   * 验证签名
   * 
   * @param msg
   * @return 日期：Sep 9, 2012
   * @throws Exception
   */
  public boolean verifyMsg(String msg, String cer, boolean isFront) throws Exception {
    boolean flag = XmlTools.verifySign(msg, cer, false, isFront);
    System.out.println("验签结果[" + flag + "]");
    return flag;
  }

  /**
   * 下载对账文件
   * 
   * @see com.aipg.core.TranxOperationInf#downBills(java.lang.String, boolean)
   */
  public void downBills(String url, boolean isTLTFront) throws Exception {
    String xml = "";
    AipgReq aipgReq = new AipgReq();
    InfoReq info = makeReq("200002");
    TransQueryReq dqr = new TransQueryReq();
    aipgReq.setINFO(info);
    aipgReq.addTrx(dqr);
    dqr.setSTATUS(2);
    dqr.setMERCHANT_ID(tranxContants.getMerchantId());
    dqr.setTYPE(1);
    dqr.setSTART_DAY("20121217");
    dqr.setEND_DAY("20121218");
    dqr.setCONTFEE("1");

    xml = XmlTools.buildXml(aipgReq, true);// .replaceAll("</INFO>",
                                           // "</INFO><BODY>").replaceAll("</AIPG>",
                                           // "</BODY></AIPG>");
    isFront(xml, isTLTFront, url);
  }

  /**
   * 签约通知
   * 
   */
  public void signNotice(String url, boolean isTLTFront) throws Exception {
    String xml = "";
    AipgReq aipgReq = new AipgReq();
    InfoReq info = makeReq("210003");// 交易类型
    aipgReq.setINFO(info);
    NSignReq nsign = new NSignReq();
    QSignDetail notify = new QSignDetail();
    notify.setACCTNAME("testt2");// 账户名
    notify.setAGREEMENTNO("0002000012");// 签约协议号
    notify.setACCT("6222023700014285995");// 银行账号
    notify.setSIGNTYPE("2");
    notify.setSTATUS("2");
    nsign.addDtl(notify);
    aipgReq.addTrx(nsign);
    xml = XmlTools.buildXml(aipgReq, true);// .replaceAll("</INFO>",
                                           // "</INFO><BODY>").replaceAll("</AIPG>",
                                           // "</BODY></AIPG>");
    isFront(xml, isTLTFront, url);
  }

  /**
   * 下载简单对账文件
   */
  public void downSimpleBills(String uRL11, boolean isTLTFront) throws Exception {
    String MERID = tranxContants.getMerchantId();
    String SETTDAY = "20130527";
    String REQTIME = "20130527121212";// df.format(new Date());
    String CONTFEE = "";
    String SIGN = "";
    CONTFEE = SETTDAY + "|" + REQTIME + "|" + MERID;
    System.out.println(CONTFEE);
    SIGN =
        XmlTools.signPlain(CONTFEE, tranxContants.getPfxPath(), tranxContants.getPfxPassword(),
            false);
    uRL11 =
        uRL11.replaceAll("@xxx", SETTDAY).replaceAll("@yyy", REQTIME).replaceAll("@zzz", MERID)
            .replaceAll("@sss", SIGN);
    System.out.println(uRL11);
    CONTFEE = sendXml("", uRL11, true);
    FileUtil.saveToFile(CONTFEE, "bill.txt", "UTF-8");
  }

  /**
   * 日期：Sep 4, 2012 功能：实时单笔代收付，100011是实时代笔代收，100014是实时单笔代付
   * 
   * @throws Exception
   */

  public void singleDaiFushi(String url, boolean isTLTFront) throws Exception {
    String xml = "";
    AipgReq aipg = new AipgReq();
    InfoReq info = makeReq("100014");
    aipg.setINFO(info);
    Trans trans = new Trans();
    trans.setBUSINESS_CODE("00600");
    trans.setMERCHANT_ID(tranxContants.getMerchantId());
    trans.setSUBMIT_TIME(df.format(new Date()));
    trans.setACCOUNT_NAME("测试试");
    trans.setACCOUNT_NO("622588121251757643");
    trans.setACCOUNT_PROP("0");
    // trans.setACCOUNT_TYPE("01");
    trans.setAMOUNT("2222");
    trans.setBANK_CODE("0105");
    trans.setCURRENCY("CNY");
    trans.setCUST_USERID("252523524253xx");
    trans.setTEL("13434245846");
    aipg.addTrx(trans);

    xml = XmlTools.buildXml(aipg, true);// .replaceAll("</INFO>",
                                        // "</INFO><BODY>").replaceAll("</AIPG>", "</BODY></AIPG>");
    isFront(xml, isTLTFront, url);

  }

  /**
   * @param reqsn 交易流水号
   * @param url 通联地址
   * @param isTLTFront 是否通过前置机 日期：Sep 4, 2012 功能：交易结果查询
   * @throws Exception
   */

  public void QueryTradeNew(String url, String reqsn, boolean isTLTFront) throws Exception {

    String xml = "";
    AipgReq aipgReq = new AipgReq();
    InfoReq info = makeReq("200004");
    aipgReq.setINFO(info);
    TransQueryReq dr = new TransQueryReq();
    aipgReq.addTrx(dr);
    dr.setMERCHANT_ID(tranxContants.getMerchantId());
    dr.setQUERY_SN(reqsn);
    dr.setSTATUS(2);
    dr.setTYPE(1);
    // dr.setSTATUS(2);
    dr.setSTART_DAY("");
    dr.setEND_DAY("");

    xml = XmlTools.buildXml(aipgReq, true);// .replaceAll("</INFO>",
                                           // "</INFO><BODY>").replaceAll("</AIPG>",
                                           // "</BODY></AIPG>");
    isFront(xml, isTLTFront, url);
  }


  public String genSn(int i) {
    String iStr = ((Integer) i).toString();
    int len = iStr.length();
    String sn = "";
    if (len == 1) {
      sn = "000" + iStr;
    } else if (len == 2) {
      sn = "00" + iStr;
    } else if (len == 3) {
      sn = "0" + iStr;
    } else if (len == 4) {
      sn = iStr;
    } else {
      sn = "0000";
    }
    return sn;
  }
}
