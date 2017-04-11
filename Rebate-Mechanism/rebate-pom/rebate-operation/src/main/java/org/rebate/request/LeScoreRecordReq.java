package org.rebate.request;

import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;

public class LeScoreRecordReq {

  /**
   * 商家名字
   */
  private String sellerName;
  /**
   * 乐分类型
   */
  private LeScoreType leScoreType;
  
  /**
   * 提现状态
   */
  private ApplyStatus withdrawStatus;

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public LeScoreType getLeScoreType() {
    return leScoreType;
  }

  public void setLeScoreType(LeScoreType leScoreType) {
    this.leScoreType = leScoreType;
  }

  public ApplyStatus getWithdrawStatus() {
    return withdrawStatus;
  }

  public void setWithdrawStatus(ApplyStatus withdrawStatus) {
    this.withdrawStatus = withdrawStatus;
  }
  
}
