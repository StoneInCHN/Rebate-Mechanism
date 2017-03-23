package org.rebate.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.SmsCodeRequest;
import org.rebate.json.request.UserRequest;
import org.rebate.service.EndUserService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.KeyGenerator;
import org.rebate.utils.RSAHelper;
import org.rebate.utils.TokenGenerator;
import org.rebate.utils.ToolsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("endUserController")
@RequestMapping("/endUser")
public class EndUserController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  //
  // @Resource(name = "smsTokenServiceImpl")
  // private SmsTokenService smsTokenService;
  //
  // @Resource(name = "fileServiceImpl")
  // private FileService fileService;
  //
  // @Resource(name = "reportUserRegStatisticsServiceImpl")
  // private ReportUserRegStatisticsService reportUserRegStatisticsService;


  /**
   * 测试
   *
   * @return
   */
  @RequestMapping(value = "/test", method = RequestMethod.POST)
  public @ResponseBody BaseResponse test(@RequestBody BaseRequest req) {
    BaseResponse response = new BaseResponse();
    SMSVerificationCode smsCode = new SMSVerificationCode();
    smsCode.setCellPhoneNum("13778999879");
    smsCode.setSmsCode("0987");
    smsCode.setTimeoutToken("1461223190968");
    endUserService.createSmsCode(smsCode.getCellPhoneNum(), smsCode);
    SMSVerificationCode code = endUserService.getSmsCode("13778999879");
    response.setCode(code.getCellPhoneNum());
    response.setDesc(code.getSmsCode());
    response.setToken(code.getTimeoutToken());
    endUserService.deleteSmsCode("13778999879");
    return response;
  }

  /**
   * 用户注销
   *
   * @return
   */
  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public @ResponseBody BaseResponse logout(@RequestBody BaseRequest req) {
    BaseResponse response = new BaseResponse();
    Long userId = req.getUserId();
    String token = req.getToken();

    String userToken = endUserService.getEndUserToken(userId);
    // token验证
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("csh.user.token.timeout").getContent());
      return response;
    }
    EndUser endUser = endUserService.find(userId);
    endUser.setJpushRegId(null);
    endUserService.update(endUser);

    endUserService.deleteEndUserToken(userId);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 获取公匙
   * 
   * @param request
   * @return
   */
  @RequestMapping(value = "/rsa", method = RequestMethod.POST)
  public @ResponseBody BaseResponse rsa(HttpServletRequest request) {
    BaseResponse response = new BaseResponse();
    String key = setting.getServerPublicKey();
    response.setCode(CommonAttributes.SUCCESS);
    response.setDesc(key);
    return response;
  }


  /**
   * 用户登录 （分为密码登录和短信验证码登录）
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> login(HttpServletRequest requesthttp,
      @RequestBody UserRequest userLoginReq) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    String serverPrivateKey = setting.getServerPrivateKey();
    String cellPhoneNum = userLoginReq.getCellPhoneNum();
    String password = userLoginReq.getPassword();
    String smsCode = userLoginReq.getSmsCode();


    if (StringUtils.isEmpty(cellPhoneNum) || isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_LOGIN);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }
    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "login",
          "Fetching User from database with CellPhoneNum: %s", cellPhoneNum);
    }
    EndUser loginUser = endUserService.findByUserMobile(cellPhoneNum);
    if (loginUser == null) {
      response.setCode(CommonAttributes.FAIL_LOGIN);
      response.setDesc(Message.error("rebate.user.noexist").getContent());
      return response;
    }

    /**
     * 密码登录
     */
    if (!StringUtils.isEmpty(password)) {
      try {
        password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
      } catch (Exception e) {
        e.printStackTrace();
      }

      // 密码长度验证
      if (password.length() < setting.getPasswordMinlength()) {
        response.setCode(CommonAttributes.FAIL_LOGIN);
        response.setDesc(Message.error("rebate.pwd.length.error", setting.getPasswordMinlength())
            .getContent());
        return response;
      }

      if (!DigestUtils.md5Hex(password).equals(loginUser.getLoginPwd())) {
        response.setCode(CommonAttributes.FAIL_LOGIN);
        response.setDesc(Message.error("rebate.nameorpwd.error").getContent());
        return response;
      }
    }
    /**
     * 短信验证码登录
     */
    else {
      SMSVerificationCode smsVerficationCode = endUserService.getSmsCode(cellPhoneNum);
      if (smsVerficationCode == null) {
        response.setCode(CommonAttributes.FAIL_LOGIN);
        response.setDesc(Message.error("rebate.sms.invaliable").getContent());
        return response;
      } else {
        String code = smsVerficationCode.getSmsCode();
        String timeoutToken = smsVerficationCode.getTimeoutToken();
        if (timeoutToken != null
            && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
          if (!smsCode.equals(code)) {
            response.setCode(CommonAttributes.FAIL_LOGIN);
            response.setDesc(Message.error("rebate.sms.token.error").getContent());
            return response;
          } else {
            endUserService.deleteSmsCode(cellPhoneNum);
          }
        } else {
          response.setCode(CommonAttributes.FAIL_LOGIN);
          response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
          return response;
        }
      }
    }

    if (AccountStatus.LOCKED.equals(loginUser.getAccountStatus())
        || AccountStatus.DELETE.equals(loginUser.getAccountStatus())) {
      response.setCode(CommonAttributes.FAIL_LOGIN);
      response.setDesc(Message.error("rebate.current.user.invalid").getContent());
      return response;
    }

    // endUserService.update(loginUser);
    // if (LogUtil.isDebugEnabled(EndUserController.class)) {
    // LogUtil.debug(EndUserController.class, "update", "enduser login: %s", cellPhoneNum);
    // }

    String[] properties =
        {"id", "cellPhoneNum", "nickName", "userPhoto", "recommender", "agent.agencyLevel"};
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, loginUser);
    if (!CollectionUtils.isEmpty(loginUser.getSellers())) {
      map.put("isSeller", true);
    } else {
      map.put("isSeller", false);
    }
    response.setMsg(map);
    String token = TokenGenerator.generateToken();
    endUserService.createEndUserToken(token, loginUser.getId());
    response.setToken(token);
    response.setCode(CommonAttributes.SUCCESS);
    response.setDesc(loginUser.getId().toString());
    return response;
  }

  //
  // /**
  // * 重置密码
  // *
  // * @param request
  // * @param resetPwdReq
  // * @return
  // */
  // @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
  // public @ResponseBody BaseResponse resetPwd(HttpServletRequest request,
  // @RequestBody UserRegRequest resetPwdReq) {
  // BaseResponse response = new BaseResponse();
  // String serverPrivateKey = setting.getServerPrivateKey();
  // String password_confirm = resetPwdReq.getPassword_confirm();
  // String password = resetPwdReq.getPassword();
  // String smsToken = resetPwdReq.getSmsToken();
  // String mobileNo = resetPwdReq.getUserName();
  //
  // if (StringUtils.isEmpty(mobileNo) || !isMobileNumber(mobileNo)) {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.mobile.invaliable").getContent());
  // return response;
  // }
  //
  // EndUser user = endUserService.findByUserName(mobileNo);
  // if (user == null) {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.user.noexist").getContent());
  // return response;
  //
  // }
  // if (password != null || password_confirm != null) {
  // if (!password.equals(password_confirm)) {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.pwd.no.same").getContent());
  // return response;
  // }
  // try {
  // password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // if (password.length() < setting.getPasswordMinlength()
  // || password.length() > setting.getPasswordMaxlength()) {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.nameorpwd.invaliable").getContent());
  // return response;
  // }
  // user.setPassword(DigestUtils.md5Hex(password));
  // endUserService.update(user);
  //
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "update",
  // "EndUser Reset Password. UserName: %s,id: %s", mobileNo, user.getId());
  // }
  // response.setCode(CommonAttributes.SUCCESS);
  // return response;
  // } else {
  // // 短信验证码验证
  // SmsToken userSmsToken = smsTokenService.findByUserMobile(mobileNo, SmsTokenType.FINDPWD);
  // if (userSmsToken == null) {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.mobile.invaliable").getContent());
  // return response;
  // } else {
  // String timeOutToken = userSmsToken.getTimeoutToken();
  // String smsCode = userSmsToken.getSmsToken();
  // if (timeOutToken != null
  // && !TokenGenerator.tokenTimeOut(timeOutToken, setting.getSmsCodeTimeOut())) {
  // if (!smsCode.equals(smsToken)) {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.sms.token.error").getContent());
  // return response;
  // } else {
  // smsTokenService.delete(userSmsToken);
  // response.setCode(CommonAttributes.SUCCESS);
  // return response;
  // }
  // } else {
  // response.setCode(CommonAttributes.FAIL_RESET_PWD);
  // response.setDesc(Message.error("csh.sms.token.timeout").getContent());
  // return response;
  // }
  // }
  // }
  //
  // }

  /**
   * 获取短信验证码
   *
   * @param request
   * @param smsTokenRequest
   * @return
   */
  @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
  public @ResponseBody BaseResponse getSmsToken(HttpServletRequest request,
      @RequestBody SmsCodeRequest smsCodeRequest) {
    BaseResponse response = new BaseResponse();
    String cellPhoneNum = smsCodeRequest.getCellPhoneNum();
    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_SMSTOKEN);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
    } else {
      if (LogUtil.isDebugEnabled(EndUserController.class)) {
        LogUtil.debug(EndUserController.class, "getSmsCode",
            "Fetching SmsCode from cache with cellPhone number: %s", cellPhoneNum);
      }
      SMSVerificationCode smsVerificationCode = endUserService.getSmsCode(cellPhoneNum);
      if (smsVerificationCode != null) {
        endUserService.deleteSmsCode(cellPhoneNum);
      }
      Integer smsCode = (int) ((Math.random() * 9 + 1) * 1000);
      ToolsUtils.sendSmsMsg(cellPhoneNum, "");// 发送短信验证码
      SMSVerificationCode newSmsCode = new SMSVerificationCode();
      newSmsCode.setCellPhoneNum(cellPhoneNum);
      newSmsCode.setSmsCode(smsCode.toString());
      newSmsCode.setTimeoutToken(new Long(new Date().getTime()).toString());
      endUserService.createSmsCode(cellPhoneNum, newSmsCode);

      response.setCode(CommonAttributes.SUCCESS);
      response.setDesc(smsCode.toString());
    }
    return response;
  }



  /**
   * 终端用户注册
   *
   * @param request
   * @param userReg
   * @return
   */
  @RequestMapping(value = "/reg", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> reg(@RequestBody UserRequest userReg) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    String serverPrivateKey = setting.getServerPrivateKey();
    String cellPhoneNum = userReg.getCellPhoneNum();
    String smsCode = userReg.getSmsCode();
    String password = userReg.getPassword();
    String password_confirm = userReg.getPassword_confirm();
    String recommenderMobile = userReg.getRecommenderMobile();

    // 手机号码格式验证
    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }
    // 账号重复验证
    EndUser user = endUserService.findByUserMobile(cellPhoneNum);
    if (user != null) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.mobile.used").getContent());
      return response;
    }

    // 推荐人账号验证
    if (!StringUtils.isEmpty(recommenderMobile)) {
      EndUser recommender = endUserService.findByUserMobile(recommenderMobile);
      if (recommender == null) {
        response.setCode(CommonAttributes.FAIL_REG);
        response.setDesc(Message.error("rebate.recommender.no.exist").getContent());
        return response;
      }
    }


    // 密码非空验证
    if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.pwd.null.error").getContent());
      return response;
    }

    if (!password.equals(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.pwd.no.same").getContent());
      return response;
    }
    try {
      password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (password.length() < setting.getPasswordMinlength()) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.pwd.length.error").getContent());
      return response;
    }

    SMSVerificationCode smsVerficationCode = endUserService.getSmsCode(cellPhoneNum);
    if (smsVerficationCode == null) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.sms.invaliable").getContent());
      return response;
    } else {
      String code = smsVerficationCode.getSmsCode();
      String timeoutToken = smsVerficationCode.getTimeoutToken();
      if (timeoutToken != null
          && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
        if (!smsCode.equals(code)) {
          response.setCode(CommonAttributes.FAIL_REG);
          response.setDesc(Message.error("rebate.sms.token.error").getContent());
          return response;
        } else {
          endUserService.deleteSmsCode(cellPhoneNum);
        }
      } else {
        response.setCode(CommonAttributes.FAIL_REG);
        response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
        return response;
      }
    }

    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "Reg",
          "EndUser Reg. User CellPhoneNum: %s, Recommender Mobile: %s", cellPhoneNum,
          recommenderMobile);
    }

    EndUser regUser = endUserService.userReg(cellPhoneNum, password, recommenderMobile);
    Map<String, Object> map = new HashMap<String, Object>();
    response.setMsg(map);
    response.setCode(CommonAttributes.SUCCESS);
    response.setDesc(regUser.getId().toString());
    String token = TokenGenerator.generateToken();
    endUserService.createEndUserToken(token, regUser.getId());
    response.setToken(token);
    return response;

  }


  //
  // /**
  // * 修改终端用户信息(不包括头像)
  // *
  // * @param req
  // * @return
  // */
  // @RequestMapping(value = "/editUserInfo", method = RequestMethod.POST)
  // @UserValidCheck
  // public @ResponseBody ResponseOne<Map<String, Object>> editUserInfo(
  // @RequestBody EndUserInfoRequest req) {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // Long userId = req.getUserId();
  // String token = req.getToken();
  // String nickName = req.getNickName();
  // String sign = req.getSign();
  // // 验证登录token
  // String userToken = endUserService.getEndUserToken(userId);
  // if (!TokenGenerator.isValiableToken(token, userToken)) {
  // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
  // response.setDesc(Message.error("csh.user.token.timeout").getContent());
  // return response;
  // }
  //
  // EndUser endUser = endUserService.find(userId);
  //
  // if (nickName != null) {// 修改昵称
  // endUser.setNickName(nickName);
  // }
  // if (sign != null) {// 修改签名
  // endUser.setSignature(sign);
  // }
  // endUserService.update(endUser);
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "Update",
  // "Edit EndUser Info. NickName: %s, Signature: %s", endUser.getNickName(),
  // endUser.getSignature());
  // }
  //
  // response.setCode(CommonAttributes.SUCCESS);
  // String[] properties = {"id", "userName", "nickName", "photo", "signature"};
  // Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, endUser);
  // response.setMsg(map);
  //
  // String newtoken = TokenGenerator.generateToken(req.getToken());
  // endUserService.createEndUserToken(newtoken, userId);
  // response.setToken(newtoken);
  // return response;
  // }
  //
  //
  // /**
  // * 修改用户头像
  // *
  // * @param req
  // * @return
  // */
  // @RequestMapping(value = "/editUserPhoto", method = RequestMethod.POST)
  // // @UserValidCheck
  // public @ResponseBody ResponseOne<Map<String, Object>> editUserPhoto(EndUserInfoRequest req) {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  //
  // Long userId = req.getUserId();
  // String token = req.getToken();
  // MultipartFile photo = req.getPhoto();
  //
  // // 验证登录token
  // String userToken = endUserService.getEndUserToken(userId);
  // if (!TokenGenerator.isValiableToken(token, userToken)) {
  // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
  // response.setDesc(Message.error("csh.user.token.timeout").getContent());
  // return response;
  // }
  //
  // EndUser endUser = endUserService.find(userId);
  //
  // endUser.setPhoto(fileService.saveImage(photo, ImageType.PHOTO));
  //
  // endUserService.update(endUser);
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "Update",
  // "Updating photo for EndUser. UserName: %s, Photo: %s", endUser.getMobileNum(),
  // endUser.getPhoto());
  // }
  //
  // response.setCode(CommonAttributes.SUCCESS);
  // String[] properties = {"id", "userName", "nickName", "photo", "signature"};
  // Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, endUser);
  // response.setMsg(map);
  // String newtoken = TokenGenerator.generateToken(req.getToken());
  // endUserService.createEndUserToken(newtoken, userId);
  // response.setToken(newtoken);
  // return response;
  // }


  // /**
  // * 获取用户信息
  // *
  // * @param req
  // * @return
  // */
  // @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
  // @UserValidCheck
  // public @ResponseBody ResponseOne<Map<String, Object>> getUserInfo(@RequestBody BaseRequest req)
  // {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // Long userId = req.getUserId();
  // String token = req.getToken();
  //
  // // 验证登录token
  // String userToken = endUserService.getEndUserToken(userId);
  // if (!TokenGenerator.isValiableToken(token, userToken)) {
  // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
  // response.setDesc(Message.error("csh.user.token.timeout").getContent());
  // return response;
  // }
  //
  // EndUser endUser = endUserService.find(userId);
  //
  // String[] properties = {"id", "userName", "nickName", "photo",
  // "signature","defaultVehiclePlate", "defaultVehicle"};
  // Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, endUser);
  // response.setMsg(map);
  //
  // String newtoken = TokenGenerator.generateToken(req.getToken());
  // endUserService.createEndUserToken(newtoken, userId);
  // response.setToken(newtoken);
  // return response;
  // }

}
