package org.rebate.utils.allinpay.demo;

import java.util.ArrayList;
import java.util.List;

import org.rebate.entity.SellerClearingRecord;



/**
 */
public class TranxMain {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
       
	TranxConTest tranxContants = new TranxConTest();

    boolean isfront = false;// 是否发送至前置机（由前置机进行签名）
    TranxServiceImplTest tranxService = new TranxServiceImplTest();

    List<SellerClearingRecord> records = new ArrayList<SellerClearingRecord>();
    SellerClearingRecord record = new SellerClearingRecord();
    record.setRemark("1");
    records.add(record);
    //批量代付
    //tranxService.batchDaiFu(tranxContants.getUrl(), isfront, "1", "8", records);
    
    
    // 单笔实时代付
    //tranxService.singleDaiFushi(tranxContants.getUrl(), isfront);
    // tranxService.batchDaiShou(URL11https, isfront);
    
    // tranxService.downBills(URL11, isfront);
    // 签约结果通知
    // tranxService.signNotice(NOTICEURL, isfront);
    // 简单对账文件下载
    // tranxService.downSimpleBills(URLbill, true);
    // 交易查询
     tranxService.queryTradeNew(tranxContants.getUrl(), "2007010000044091501227303196", isfront);

  }
}
