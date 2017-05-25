package org.rebate.utils.allinpay.demo;

import java.util.ArrayList;
import java.util.List;

import org.rebate.entity.LeScoreRecord;
import org.rebate.utils.allinpay.service.TranxServiceImpl;



/**
 */
public class TranxMain {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    String URL8083 = "http://113.108.182.4:8083/aipg/ProcessServlet"; //
    String URL11 = "http://172.16.1.11:8080/aipg/ProcessServlet"; //
    String URL11https = "https://172.16.1.11/aipg/ProcessServlet"; //
    String URL = "http://tlt.allinpay.com/aipg/ProcessServlet";
    String NOTICEURL = "http://116.52.144.99:11021"; //
    String URLbill =
        "http://172.16.1.11:8080/aipg/GetConFile.do?SETTDAY=@xxx&REQTIME=@yyy&MERID=@zzz&SIGN=@sss"; //
    boolean isfront = false;// 是否发送至前置机（由前置机进行签名）
    String reqsn = "122432353465235433";// 交易流水号
    TranxServiceImpl tranxService = new TranxServiceImpl();
    // tranxService.batchDaiShou(URL11https, isfront);
    // tranxService.downBills(URL11, isfront);
    // 签约结果通知
    // tranxService.signNotice(NOTICEURL, isfront);
    // 简单对账文件下载
    // tranxService.downSimpleBills(URLbill, true);
    // 交易查询
    // tranxService.QueryTradeNew(URL11, reqsn, isfront);
    // 单笔实时代付
    // tranxService.singleDaiFushi(URL11, isfront);
    List<LeScoreRecord> records = new ArrayList<LeScoreRecord>();
    LeScoreRecord record = new LeScoreRecord();
    record.setRemark("1");
    records.add(record);
    tranxService.batchDaiFu("https://113.108.182.3/aipg/ProcessServlet", isfront, "1", "10003",
        records);
    // tranxService.batchDaiShou("https://113.108.182.3/aipg/ProcessServlet", isfront);
    // tranxService.singleDaiFushi("https://113.108.182.3/aipg/ProcessServlet", isfront);

    // tranxService.batchDaiShou("https://113.108.182.3/aipg/ProcessServlet", isfront);

  }
}
