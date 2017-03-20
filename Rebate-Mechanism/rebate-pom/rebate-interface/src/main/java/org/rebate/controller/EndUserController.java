package org.rebate.controller;

import javax.servlet.http.HttpServletRequest;

import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.json.base.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("endUserController")
@RequestMapping("/endUser")
public class EndUserController extends MobileBaseController {

  // @Resource(name = "endUserServiceImpl")
  // private EndUserService endUserService;
  //
  // @Resource(name = "smsTokenServiceImpl")
  // private SmsTokenService smsTokenService;
  //
  // @Resource(name = "fileServiceImpl")
  // private FileService fileService;
  //
  // @Resource(name = "reportUserRegStatisticsServiceImpl")
  // private ReportUserRegStatisticsService reportUserRegStatisticsService;


  // /**
  // * 测试
  // *
  // * @return
  // */
  // @RequestMapping(value = "/test", method = RequestMethod.POST)
  // public @ResponseBody BaseResponse test(@RequestBody BaseRequest req) {
  // BaseResponse response = new BaseResponse();
  // ReportUserRegStatistics aRegStatistics =
  // reportUserRegStatisticsService.getReportByDate(TimeUtils.formatDate2Day(new Date()));
  // return response;
  // }

  // /**
  // * 用户注销
  // *
  // * @return
  // */
  // @RequestMapping(value = "/logout", method = RequestMethod.POST)
  // public @ResponseBody BaseResponse logout(@RequestBody BaseRequest req) {
  // BaseResponse response = new BaseResponse();
  // Long userId = req.getUserId();
  // String token = req.getToken();
  //
  // String userToken = endUserService.getEndUserToken(userId);
  // // token验证
  // if (!TokenGenerator.isValiableToken(token, userToken)) {
  // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
  // response.setDesc(Message.error("csh.user.token.timeout").getContent());
  // return response;
  // }
  // EndUser endUser = endUserService.find(userId);
  // endUser.setjpushRegId(null);
  // endUserService.update(endUser);
  //
  // endUserService.deleteEndUserToken(userId);
  // response.setCode(CommonAttributes.SUCCESS);
  // return response;
  // }

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

  // /**
  // * 更新缓存信息
  // *
  // * @param req
  // * @return
  // */
  // @RequestMapping(value = "/updateLoginCacheInfo", method = RequestMethod.POST)
  // public @ResponseBody ResponseOne<Map<String, Object>> updateLoginCacheInfo(
  // @RequestBody UserLoginRequest userLoginReq) {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // Long userId = userLoginReq.getUserId();
  //
  // EndUser loginUser = endUserService.find(userId);
  //
  // response.setCode(CommonAttributes.SUCCESS);
  // response.setDesc(loginUser.getId().toString());
  //
  // String[] properties = {"id", "userName", "nickName", "photo", "signature"};
  // Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, loginUser);
  // map.put("defaultVehiclePlate", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getPlate() : null);
  // map.put("defaultVehicle", loginUser.getDefaultVehicle() != null ? loginUser.getDefaultVehicle()
  // .getVehicleFullBrand() : null);
  // map.put("defaultDeviceNo", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getDeviceNo() : null);
  // map.put("defaultVehicleIcon", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getBrandIcon() : null);
  // map.put("defaultVehicleId", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getId() : null);
  // response.setMsg(map);
  // String token = TokenGenerator.generateToken();
  // endUserService.createEndUserToken(token, loginUser.getId());
  // response.setToken(token);
  // return response;
  // }
  //
  // /**
  // * 用户登录
  // *
  // * @param req
  // * @return
  // */
  // @RequestMapping(value = "/login", method = RequestMethod.POST)
  // public @ResponseBody ResponseOne<Map<String, Object>> login(HttpServletRequest requesthttp,
  // @RequestBody UserLoginRequest userLoginReq) {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // String serverPrivateKey = setting.getServerPrivateKey();
  // String userName = userLoginReq.getUserName();
  // String password = userLoginReq.getPassword();
  // String imei = userLoginReq.getImei();
  //
  //
  // if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
  // response.setCode(CommonAttributes.FAIL_LOGIN);
  // response.setDesc(Message.error("csh.nameorpwd.invaliable").getContent());
  // return response;
  // }
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "login",
  // "Fetching User from database with UserName: %s", userName);
  // }
  // EndUser loginUser = endUserService.findByUserName(userName);
  // if (loginUser == null) {
  // response.setCode(CommonAttributes.FAIL_LOGIN);
  // response.setDesc(Message.error("csh.user.noexist").getContent());
  // return response;
  // }
  // try {
  // password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  //
  // if (!DigestUtils.md5Hex(password).equals(loginUser.getPassword())) {
  // response.setCode(CommonAttributes.FAIL_LOGIN);
  // response.setDesc(Message.error("csh.nameorpwd.error").getContent());
  // return response;
  // }
  //
  // if (AccountStatus.LOCKED.equals(loginUser.getAccountStatus())
  // || AccountStatus.DELETE.equals(loginUser.getAccountStatus())) {
  // response.setCode(CommonAttributes.FAIL_LOGIN);
  // response.setDesc(Message.error("csh.current.user.invalid").getContent());
  // return response;
  // }
  //
  // LoginStatistics loginStatistics = new LoginStatistics();
  // loginStatistics.setEndUser(loginUser);
  // loginStatistics.setLoginDate(new Date());
  // loginStatistics.setLoginIp(requesthttp.getRemoteAddr());
  // loginStatistics.setImei(imei);
  // loginUser.getLoginStatistics().add(loginStatistics);
  //
  // endUserService.update(loginUser);
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "update", "enduser login: %s", userName);
  // }
  //
  // if (imei != null && imei.contains("VG")) {
  // setting.setTokenTimeOut(0);
  // SettingUtils.set(setting);
  // }
  // if (imei != null && imei.contains("VG.R")) {
  // setting.setTokenTimeOut(30);
  // SettingUtils.set(setting);
  // }
  // response.setCode(CommonAttributes.SUCCESS);
  // response.setDesc(loginUser.getId().toString());
  //
  // String[] properties = {"id", "userName", "nickName", "photo", "signature"};
  // Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, loginUser);
  // map.put("defaultVehiclePlate", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getPlate() : null);
  // map.put("defaultVehicle", loginUser.getDefaultVehicle() != null ? loginUser.getDefaultVehicle()
  // .getVehicleFullBrand() : null);
  // map.put("defaultDeviceNo", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getDeviceNo() : null);
  // map.put("defaultVehicleIcon", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getBrandIcon() : null);
  // map.put("defaultVehicleId", loginUser.getDefaultVehicle() != null ? loginUser
  // .getDefaultVehicle().getId() : null);
  // response.setMsg(map);
  // String token = TokenGenerator.generateToken();
  // endUserService.createEndUserToken(token, loginUser.getId());
  // response.setToken(token);
  // return response;
  // }
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
  //
  // /**
  // * 获取短信验证码
  // *
  // * @param request
  // * @param smsTokenRequest
  // * @return
  // */
  // @RequestMapping(value = "/getSmsToken", method = RequestMethod.POST)
  // public @ResponseBody BaseResponse getSmsToken(HttpServletRequest request,
  // @RequestBody SmsTokenRequest smsTokenRequest) {
  // BaseResponse response = new BaseResponse();
  // String mobileNo = smsTokenRequest.getMobileNo();
  // SmsTokenType tokenType = smsTokenRequest.getTokenType();
  // TokenSendType sendType = smsTokenRequest.getSendType();
  // if (StringUtils.isEmpty(mobileNo) || !isMobileNumber(mobileNo)) {
  // response.setCode(CommonAttributes.FAIL_SMSTOKEN);
  // response.setDesc(Message.error("csh.mobile.invaliable").getContent());
  // } else {
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "getSmsToken",
  // "Fetching SmsToken from database with mobile no: %s", mobileNo);
  // }
  // EndUser endUser = endUserService.findByUserName(mobileNo);
  // if (tokenType == SmsTokenType.FINDPWD) {
  // if (endUser == null) {
  // response.setCode(CommonAttributes.FAIL_SMSTOKEN);
  // response.setDesc(Message.error("csh.user.noexist").getContent());
  // return response;
  // }
  // }
  // if (tokenType == SmsTokenType.REG) {
  // if (endUser != null) {
  // response.setCode(CommonAttributes.FAIL_SMSTOKEN);
  // response.setDesc(Message.error("csh.mobile.used").getContent());
  // return response;
  // }
  // }
  // SmsToken smsToken = smsTokenService.findByUserMobile(mobileNo, tokenType);
  // if (smsToken != null) {
  // smsTokenService.delete(smsToken);
  // }
  // Integer smsTokenNo = (int) ((Math.random() * 9 + 1) * 1000);
  // if (sendType == TokenSendType.SMS) {
  // UcpaasUtil.SendCodeBySms(mobileNo, smsTokenNo.toString());
  // } else if (sendType == TokenSendType.VOICE) {
  // UcpaasUtil.SendCodeByVoice(mobileNo, smsTokenNo.toString());
  // }
  //
  // SmsToken userSmsToken = new SmsToken();
  // userSmsToken.setCreateDate(new Date());
  // userSmsToken.setMobile(mobileNo);
  // userSmsToken.setSmsToken(smsTokenNo.toString());
  // userSmsToken.setTimeoutToken(TokenGenerator.generateToken());
  // userSmsToken.setSmsTokenType(tokenType);
  // userSmsToken.setSendType(sendType);
  // smsTokenService.save(userSmsToken);
  // response.setCode(CommonAttributes.SUCCESS);
  // response.setDesc(smsTokenNo.toString());
  // }
  // return response;
  // }
  //
  //
  // /**
  // * 导入终端用户
  // *
  // * @param request
  // * @param userReg
  // * @return
  // */
  // @RequestMapping(value = "/importUser", method = RequestMethod.GET)
  // public @ResponseBody ResponseOne<Map<String, Object>> importUser(String mobileNum) {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // String password = "123456";
  //
  // // 手机号码格式验证
  // if (StringUtils.isEmpty(mobileNum) || !isMobileNumber(mobileNum)) {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.mobile.invaliable").getContent());
  // return response;
  // }
  // // 账号重复验证
  // EndUser user = endUserService.findByUserName(mobileNum);
  // if (user != null) {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.mobile.used").getContent());
  // return response;
  // }
  //
  //
  // endUserService.userReg(mobileNum, password);
  // response.setCode(CommonAttributes.SUCCESS);
  // response.setDesc("用户导入成功！");
  // return response;
  //
  // }
  //
  // /**
  // * 终端用户注册
  // *
  // * @param request
  // * @param userReg
  // * @return
  // */
  // @RequestMapping(value = "/reg", method = RequestMethod.POST)
  // public @ResponseBody ResponseOne<Map<String, Object>> reg(@RequestBody UserRegRequest userReg)
  // {
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // String serverPrivateKey = setting.getServerPrivateKey();
  // String userName = userReg.getUserName();
  // String smsToken = userReg.getSmsToken();
  // String password = userReg.getPassword();
  // String password_confirm = userReg.getPassword_confirm();
  //
  // // 手机号码格式验证
  // if (StringUtils.isEmpty(userName) || !isMobileNumber(userName)) {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.mobile.invaliable").getContent());
  // return response;
  // }
  // // 账号重复验证
  // EndUser user = endUserService.findByUserName(userName);
  // if (user != null) {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.mobile.used").getContent());
  // return response;
  //
  // }
  // if (password != null || password_confirm != null) {
  // if (!password.equals(password_confirm)) {
  // response.setCode(CommonAttributes.FAIL_REG);
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
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.nameorpwd.invaliable").getContent());
  // return response;
  // }
  //
  // if (LogUtil.isDebugEnabled(EndUserController.class)) {
  // LogUtil.debug(EndUserController.class, "reg", "EndUser Reg. UserName: %s", userName);
  // }
  // EndUser regUser = endUserService.userReg(userName, password);
  // Map<String, Object> map = new HashMap<String, Object>();
  // map.put("isGetCoupon", regUser.getIsGetCoupon());
  // response.setMsg(map);
  // response.setCode(CommonAttributes.SUCCESS);
  // response.setDesc(regUser.getId().toString());
  // String token = TokenGenerator.generateToken();
  // endUserService.createEndUserToken(token, regUser.getId());
  // response.setToken(token);
  // return response;
  // } else {
  // // 短信验证码验证
  // SmsToken userSmsToken = smsTokenService.findByUserMobile(userName, SmsTokenType.REG);
  // if (userSmsToken == null) {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.mobile.invaliable").getContent());
  // return response;
  // } else {
  // String timeOutToken = userSmsToken.getTimeoutToken();
  // String smsCode = userSmsToken.getSmsToken();
  // if (timeOutToken != null
  // && !TokenGenerator.tokenTimeOut(timeOutToken, setting.getSmsCodeTimeOut())) {
  // if (!smsCode.equals(smsToken)) {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.sms.token.error").getContent());
  // return response;
  // } else {
  // smsTokenService.delete(userSmsToken);
  // response.setCode(CommonAttributes.SUCCESS);
  // return response;
  // }
  // } else {
  // response.setCode(CommonAttributes.FAIL_REG);
  // response.setDesc(Message.error("csh.sms.token.timeout").getContent());
  // return response;
  // }
  // }
  // }
  // }
  //
  //
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
