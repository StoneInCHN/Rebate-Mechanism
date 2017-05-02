package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.json.base.ResponseOne;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.PayUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("leScoreRecordServiceImpl")
public class LeScoreRecordServiceImpl extends BaseServiceImpl<LeScoreRecord, Long> implements
    LeScoreRecordService {

  @Resource(name = "leScoreRecordDaoImpl")
  private LeScoreRecordDao leScoreRecordDao;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "leScoreRecordDaoImpl")
  public void setBaseDao(LeScoreRecordDao leScoreRecordDao) {
    super.setBaseDao(leScoreRecordDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Message auditWithdraw(LeScoreRecord leScoreRecord) {
    Message message = new Message();
    try {
      LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "leScoreRecord = %s",
          leScoreRecord);
      LeScoreRecord temp = this.find(leScoreRecord.getId());
      EndUser endUser = temp.getEndUser();
      if (endUser == null) {
        LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "endUser不能为空");
        return Message.error("参数不全,用户不能为空");
      } else if (leScoreRecord.getWithdrawStatus() == null || temp.getWithdrawStatus() == null) {
        LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "auditWithdraw不能为空");
        return Message.error("审核状态不能为空");
      } else if (ApplyStatus.AUDIT_WAITING.equals(temp.getWithdrawStatus())) {
        LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw",
            "auditWithdraw 不是为待审核状态，不需要审核");
        return Message.error("审核状态错误");
      } else if (ApplyStatus.AUDIT_PASSED.equals(leScoreRecord.getWithdrawStatus())) {
        String partner_trade_no =
            String.valueOf((int) (Math.random() * 10000000) + System.currentTimeMillis()
                + (int) (Math.random() * 1000000));
        Integer amount = temp.getAmount().multiply(new BigDecimal(100)).intValue();
        ResponseOne<Map<String, Object>> response =
            PayUtil.wechatTransfers(endUser.getWechatOpenid(), amount, "用户提现", partner_trade_no);
        String code = response.getCode();
        if (CommonAttributes.SUCCESS.equals(code)) {
          // 提现成功
          Map<String, Object> map = response.getMsg();
          System.out.println(map);
          temp.setIsWithdraw(true);

        } else {
          // 提现失败
          temp.setIsWithdraw(false);
          temp.setWechatReturnMsg(response.getDesc());
        }

      } else if (ApplyStatus.AUDIT_FAILED.equals(leScoreRecord.getWithdrawStatus())) {
        // 审核失败 把 乐分还回到用户手中
        // 当前乐分
        BigDecimal curLeScore = temp.getAmount();
        // 激励乐分(包括乐心分红乐分，推荐获得乐分)
        BigDecimal motivateLeScore = temp.getMotivateLeScore();
        // 商家直接收益乐分
        BigDecimal incomeLeScore = temp.getIncomeLeScore();
        // 代理商提成乐分
        BigDecimal agentLeScore = temp.getAgentLeScore();
        endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore));
        endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore));
        endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore));
        endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore));
        endUserService.update(endUser);
      }
      temp.setWithdrawStatus(leScoreRecord.getWithdrawStatus());
      temp.setRemark(leScoreRecord.getRemark());
      this.update(temp);
      message = Message.success("操作成功!");
    } catch (Exception e) {
      LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "提现审核执行失败 ，异常信息： %s",
          e.getMessage());
      return Message.error("提现审核执行失败 ，异常信息：", e.getMessage());
    }
    return message;
  }
}
