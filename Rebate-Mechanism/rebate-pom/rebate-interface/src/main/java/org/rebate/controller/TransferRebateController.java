package org.rebate.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.RebateType;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.request.UserRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.LeBeanRecordService;
import org.rebate.service.LeMindRecordService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("transferRebateController")
@RequestMapping("/transferRebate")
public class TransferRebateController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "leMindRecordServiceImpl")
  private LeMindRecordService leMindRecordService;

  @Resource(name = "leScoreRecordServiceImpl")
  private LeScoreRecordService leScoreRecordService;

  @Resource(name = "leBeanRecordServiceImpl")
  private LeBeanRecordService leBeanRecordService;



  /**
   * 用户转账(乐心,乐豆,乐分)
   *
   * @param request
   * @param resetPwdReq
   * @return
   */
  @RequestMapping(value = "/doTransfer", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse updatePwd(@RequestBody UserRequest req) {
    BaseResponse response = new BaseResponse();
    String smsCode = req.getSmsCode();
    String cellPhoneNum = req.getCellPhoneNum();
    RebateType transType = req.getTransType();
    BigDecimal amount = req.getAmount();
    String token = req.getToken();
    Long userId = req.getUserId();


    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }

    if (amount == null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.amount.null").getContent());
      return response;
    }

    EndUser transferUser = endUserService.find(userId);
    if (RebateType.LE_MIND.equals(transType)) {
      if (!isInteger(amount.toString())) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.transfer.leMind.invalid").getContent());
        return response;
      }
      if (amount.compareTo(transferUser.getCurLeMind()) > 0) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.transfer.leMind.insufficient").getContent());
        return response;
      }
    }

    if (RebateType.LE_BEAN.equals(transType)) {
      if (!isNumber(amount.toString())) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.transfer.leBean.invalid").getContent());
        return response;
      }
      if (amount.compareTo(transferUser.getCurLeBean()) > 0) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.transfer.leBean.insufficient").getContent());
        return response;
      }
    }

    if (RebateType.LE_SCORE.equals(transType)) {
      if (!isNumber(amount.toString())) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.transfer.leScore.invalid").getContent());
        return response;
      }
      if (amount.compareTo(transferUser.getCurLeScore()) > 0) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.transfer.leScore.insufficient").getContent());
        return response;
      }
    }

    EndUser user = endUserService.findByUserMobile(cellPhoneNum);
    if (user == null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.receiver.noexist").getContent());
      return response;
    }
    if (user != null && !AccountStatus.ACTIVED.equals(user.getAccountStatus())) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.receiver.invalid").getContent());
      return response;
    }
    if (user != null && user.getCellPhoneNum().equals(transferUser.getCellPhoneNum())) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.mobile.self").getContent());
      return response;
    }

    SMSVerificationCode smsVerficationCode =
        endUserService.getSmsCode(transferUser.getCellPhoneNum());
    if (smsVerficationCode == null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.sms.invaliable").getContent());
      return response;
    } else {
      String code = smsVerficationCode.getSmsCode();
      String timeoutToken = smsVerficationCode.getTimeoutToken();
      if (timeoutToken != null
          && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
        if (!smsCode.equals(code)) {
          response.setCode(CommonAttributes.FAIL_COMMON);
          response.setDesc(Message.error("rebate.sms.token.error").getContent());
          return response;
        } else {
          endUserService.deleteSmsCode(cellPhoneNum);
        }
      } else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
        return response;
      }
    }

    endUserService.transferRebate(transferUser, user, transType, amount);
    if (LogUtil.isDebugEnabled(TransferRebateController.class)) {
      LogUtil.debug(TransferRebateController.class, "doTransfer",
          "User Transfer. TransferId: %s,ReceiverCellPhone: %s,TransferType: %s,transAmount: %s",
          userId, cellPhoneNum, transType.toString(), amount);
    }

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;

  }


  /**
   * 验证转账接收人手机号
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/verifyReceiver", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse verifyReceiver(@RequestBody UserRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();
    String receiverMobile = req.getReceiverMobile();

    if (LogUtil.isDebugEnabled(TransferRebateController.class)) {
      LogUtil.debug(TransferRebateController.class, "verifyReceiver",
          "verify receiver Mobile for transfer rebate. userId: %s,receiverMobile: %s", userId,
          receiverMobile);
    }

    // 手机号码格式验证
    if (StringUtils.isEmpty(receiverMobile) || !isMobileNumber(receiverMobile)) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }

    EndUser user = endUserService.findByUserMobile(receiverMobile);
    if (user == null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.receiver.noexist").getContent());
      return response;
    }
    if (user != null && !AccountStatus.ACTIVED.equals(user.getAccountStatus())) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.receiver.invalid").getContent());
      return response;
    }
    EndUser transferUser = endUserService.find(userId);
    if (user != null && user.getCellPhoneNum().equals(transferUser.getCellPhoneNum())) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.transfer.mobile.self").getContent());
      return response;
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }
}
