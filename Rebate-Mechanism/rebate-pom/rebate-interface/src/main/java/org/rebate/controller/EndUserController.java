package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeBeanRecord;
import org.rebate.entity.LeMindRecord;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.RebateRecord;
import org.rebate.entity.Seller;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.entity.commonenum.CommonEnum.SmsCodeType;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.SmsCodeRequest;
import org.rebate.json.request.UserRequest;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.rebate.service.FileService;
import org.rebate.service.LeBeanRecordService;
import org.rebate.service.LeMindRecordService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.RebateRecordService;
import org.rebate.service.SellerService;
import org.rebate.service.SystemConfigService;
import org.rebate.service.UserRecommendRelationService;
import org.rebate.service.mapper.SellerRowMapper;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.KeyGenerator;
import org.rebate.utils.QRCodeGenerator;
import org.rebate.utils.RSAHelper;
import org.rebate.utils.TokenGenerator;
import org.rebate.utils.ToolsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller("endUserController")
@RequestMapping("/endUser")
public class EndUserController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;

  @Resource(name = "fileServiceImpl")
  private FileService fileService;

  @Resource(name = "userRecommendRelationServiceImpl")
  private UserRecommendRelationService userRecommendRelationService;

  @Resource(name = "rebateRecordServiceImpl")
  private RebateRecordService rebateRecordService;

  @Resource(name = "leMindRecordServiceImpl")
  private LeMindRecordService leMindRecordService;

  @Resource(name = "leScoreRecordServiceImpl")
  private LeScoreRecordService leScoreRecordService;

  @Resource(name = "leBeanRecordServiceImpl")
  private LeBeanRecordService leBeanRecordService;

  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;

  @Resource(name = "systemConfigServiceImpl")
  private SystemConfigService systemConfigService;

  /**
   * 测试
   *
   * @return
   */
  @RequestMapping(value = "/test", method = RequestMethod.POST)
  public @ResponseBody BaseResponse test(@RequestBody BaseRequest req) {
    BaseResponse response = new BaseResponse();
    // SMSVerificationCode smsCode = new SMSVerificationCode();
    // smsCode.setCellPhoneNum("13778999879");
    // smsCode.setSmsCode("0987");
    // smsCode.setTimeoutToken("1461223190968");
    // endUserService.createSmsCode(smsCode.getCellPhoneNum(), smsCode);
    // SMSVerificationCode code = endUserService.getSmsCode("13778999879");
    // response.setCode(code.getCellPhoneNum());
    // response.setDesc(code.getSmsCode());
    // response.setToken(code.getTimeoutToken());
    // endUserService.deleteSmsCode("13778999879");

    // Area area = areaService.find(new Long(1));
    // EndUser endUser = endUserService.getAgentByArea(area);
    // response.setDesc(endUser.getCellPhoneNum());
    // List<Area> list = areaService.findAll();
    // for (Area area : list) {
    // String name = area.getName();
    // if (name.contains("自治县")) {
    // name = name.replace("自治县", "");
    // }
    // if (name.contains("县")) {
    // name = name.replace("县", "");
    // }
    // if (name.contains("自治州")) {
    // name = name.replace("自治州", "");
    // }
    // if (name.contains("市")) {
    // name = name.replace("市", "");
    // }

    // String pyName = Pinyin4jUtil.converterToSpell(name);
    // String str[] = pyName.split(",");
    // if (str.length > 1) {
    // pyName = str[1];
    // }
    // String pyName = area.getPyName();
    // if (pyName.endsWith("ou") && name.endsWith("区")) {
    // pyName = pyName.substring(0, pyName.length() - 2);
    // pyName += "qu";
    // }
    // area.setPyName(pyName);
    // System.out.println(area.getName() + "--" + pyName);
    // }
    // areaService.update(list);
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
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
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

    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_LOGIN);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }
    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "login",
          "Fetching User from database with CellPhoneNum: %s", cellPhoneNum);
    }
    EndUser loginUser = endUserService.findByUserMobile(cellPhoneNum);
    /**
     * 密码登录
     */
    if (!StringUtils.isEmpty(password)) {
      if (loginUser == null) {
        response.setCode(CommonAttributes.FAIL_LOGIN);
        response.setDesc(Message.error("rebate.user.noexist").getContent());
        return response;
      }
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
     * 短信验证码登录 1.手机号已注册，直接登录 2.手机号未注册，注册该手机号并登录
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
            if (loginUser == null) {// 用户不存在，登录成功的同时注册该手机号
              loginUser = endUserService.userReg(cellPhoneNum, null, null);
              if (LogUtil.isDebugEnabled(EndUserController.class)) {
                LogUtil.debug(EndUserController.class, "login",
                    "enduser smsCode login with cellPhoneNum no exist.Reg the cellPhoneNum: %s",
                    cellPhoneNum);
              }
            }
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

    String[] properties =
        {"id", "cellPhoneNum", "nickName", "userPhoto", "recommender", "agent.agencyLevel",
            "area.id", "area.name", "curScore", "curLeMind", "curLeScore", "totalScore",
            "totalLeMind", "totalLeScore", "curLeBean", "totalLeBean", "isBindWeChat"};
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, loginUser);
    map.putAll(endUserService.isUserHasSeller(loginUser));
    map.put("isSetLoginPwd", false);
    map.put("isSetPayPwd", false);
    if (loginUser.getLoginPwd() != null) {
      map.put("isSetLoginPwd", true);
    }
    if (loginUser.getPaymentPwd() != null) {
      map.put("isSetPayPwd", true);
    }
    response.setMsg(map);
    String token = TokenGenerator.generateToken();
    endUserService.createEndUserToken(token, loginUser.getId());
    response.setToken(token);
    response.setCode(CommonAttributes.SUCCESS);
    response.setDesc(loginUser.getId().toString());
    return response;
  }

  /**
   * 重置密码
   *
   * @param request
   * @param resetPwdReq
   * @return
   */
  @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
  public @ResponseBody BaseResponse resetPwd(HttpServletRequest request,
      @RequestBody UserRequest resetPwdReq) {
    BaseResponse response = new BaseResponse();
    String serverPrivateKey = setting.getServerPrivateKey();
    String password_confirm = resetPwdReq.getPassword_confirm();
    String password = resetPwdReq.getPassword();
    String smsCode = resetPwdReq.getSmsCode();
    String cellPhoneNum = resetPwdReq.getCellPhoneNum();

    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }

    EndUser user = endUserService.findByUserMobile(cellPhoneNum);
    if (user == null) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.user.noexist").getContent());
      return response;

    }
    // 密码非空验证
    if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.pwd.null.error").getContent());
      return response;
    }

    try {
      password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
      password_confirm =
          KeyGenerator.decrypt(password_confirm, RSAHelper.getPrivateKey(serverPrivateKey));
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!password.equals(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.pwd.no.same").getContent());
      return response;
    }

    if (password.length() < setting.getPasswordMinlength()) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.pwd.length.error").getContent());
      return response;
    }

    SMSVerificationCode smsVerficationCode = endUserService.getSmsCode(cellPhoneNum);
    if (smsVerficationCode == null) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.sms.invaliable").getContent());
      return response;
    } else {
      String code = smsVerficationCode.getSmsCode();
      String timeoutToken = smsVerficationCode.getTimeoutToken();
      if (timeoutToken != null
          && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
        if (!smsCode.equals(code)) {
          response.setCode(CommonAttributes.FAIL_RESET_PWD);
          response.setDesc(Message.error("rebate.sms.token.error").getContent());
          return response;
        } else {
          endUserService.deleteSmsCode(cellPhoneNum);
        }
      } else {
        response.setCode(CommonAttributes.FAIL_RESET_PWD);
        response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
        return response;
      }
    }
    user.setLoginPwd(DigestUtils.md5Hex(password));
    endUserService.update(user);

    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "resetPassword",
          "EndUser Reset Password. CellPhone: %s", cellPhoneNum);
    }
    response.setCode(CommonAttributes.SUCCESS);
    return response;

  }

  /**
   * 获取短信验证码
   *
   * @param request
   * @param smsTokenRequest
   * @return
   */
  @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
  public @ResponseBody BaseResponse getSmsCode(@RequestBody SmsCodeRequest smsCodeRequest) {
    BaseResponse response = new BaseResponse();
    String cellPhoneNum = smsCodeRequest.getCellPhoneNum();
    SmsCodeType smsCodeType = smsCodeRequest.getSmsCodeType();
    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_SMSTOKEN);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
    } else {
      EndUser endUser = endUserService.findByUserMobile(cellPhoneNum);
      if (smsCodeType.equals(SmsCodeType.REG)) {// 注册（user应不存在）
        if (endUser != null) {
          response.setCode(CommonAttributes.FAIL_SMSTOKEN);
          response.setDesc(Message.error("rebate.mobile.used").getContent());
          return response;
        }
      } else if (smsCodeType.equals(SmsCodeType.UPDATELOGINPWD)
          || smsCodeType.equals(SmsCodeType.RESETPWD)
          || smsCodeType.equals(SmsCodeType.UPDATEPAYPWD)) {// 找回登录密码，修改登录密码，修改支付密码（user应存在）
        if (endUser == null) {
          response.setCode(CommonAttributes.FAIL_SMSTOKEN);
          response.setDesc(Message.error("rebate.user.noexist").getContent());
          return response;
        }
      } else {// 登录
        // do nothing
      }

      SMSVerificationCode smsVerificationCode = endUserService.getSmsCode(cellPhoneNum);
      if (smsVerificationCode != null) {
        endUserService.deleteSmsCode(cellPhoneNum);
      }

      Integer smsCode = (int) ((Math.random() * 9 + 1) * 1000);
      ToolsUtils.sendSmsMsg(cellPhoneNum, setting.getSmsContentTemp() + smsCode.toString()
          + setting.getSmsPostfix());// 发送短信验证码
      SMSVerificationCode newSmsCode = new SMSVerificationCode();
      newSmsCode.setCellPhoneNum(cellPhoneNum);
      newSmsCode.setSmsCode(smsCode.toString());
      newSmsCode.setTimeoutToken(new Long(new Date().getTime()).toString());
      endUserService.createSmsCode(cellPhoneNum, newSmsCode);
      if (LogUtil.isDebugEnabled(EndUserController.class)) {
        LogUtil.debug(EndUserController.class, "getSmsCode",
            "send SmsCode for cellPhone number: %s,smsCode: %s,type: %s", cellPhoneNum,
            smsCode.toString(), smsCodeType.toString());
      }
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

    try {
      password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
      password_confirm =
          KeyGenerator.decrypt(password_confirm, RSAHelper.getPrivateKey(serverPrivateKey));
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!password.equals(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_REG);
      response.setDesc(Message.error("rebate.pwd.no.same").getContent());
      return response;
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
        } else {// 短信验证码匹配成功
          if (BooleanUtils.isTrue(endUserService.isRecommendLimited(recommenderMobile))) {// 推荐层级关系超过配置的最大层级
            response.setCode(CommonAttributes.FAIL_REG);
            response.setDesc(Message.error("rebate.recommend.level.limit", recommenderMobile)
                .getContent());
            return response;
          }
          endUserService.deleteSmsCode(cellPhoneNum);
        }
      } else {
        response.setCode(CommonAttributes.FAIL_REG);
        response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
        return response;
      }
    }

    EndUser regUser = endUserService.userReg(cellPhoneNum, password, recommenderMobile);
    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "Reg",
          "EndUser Reg. User CellPhoneNum: %s, Recommender Mobile: %s", cellPhoneNum,
          recommenderMobile);
    }

    String[] properties =
        {"id", "cellPhoneNum", "nickName", "userPhoto", "recommender", "agent.agencyLevel",
            "area.id", "area.name", "curScore", "curLeMind", "curLeScore", "totalScore",
            "totalLeMind", "totalLeScore", "curLeBean", "totalLeBean", "isBindWeChat"};
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, regUser);
    map.putAll(endUserService.isUserHasSeller(regUser));
    map.put("isSetLoginPwd", false);
    map.put("isSetPayPwd", false);
    if (regUser.getLoginPwd() != null) {
      map.put("isSetLoginPwd", true);
    }
    if (regUser.getPaymentPwd() != null) {
      map.put("isSetPayPwd", true);
    }
    response.setMsg(map);
    response.setCode(CommonAttributes.SUCCESS);
    response.setDesc(regUser.getId().toString());
    String token = TokenGenerator.generateToken();
    endUserService.createEndUserToken(token, regUser.getId());
    response.setToken(token);
    return response;

  }

  /**
   * 修改终端用户信息(不包括头像,密码)
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/editUserInfo", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> editUserInfo(@RequestBody UserRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = req.getUserId();
    String token = req.getToken();
    String nickName = req.getNickName();
    Long areaId = req.getAreaId();
    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);

    if (nickName != null) {// 修改昵称
      endUser.setNickName(nickName);
    }
    if (areaId != null) {// 修改所在地区
      Area area = areaService.find(areaId);
      endUser.setArea(area);
    }
    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "edit user info",
          "Edit EndUser Info. NickName: %s, area: %s", endUser.getNickName(),
          endUser.getArea() != null ? endUser.getArea().getName() : null);
    }
    endUserService.update(endUser);

    response.setCode(CommonAttributes.SUCCESS);
    String[] properties =
        {"id", "cellPhoneNum", "nickName", "userPhoto", "recommender", "agent.agencyLevel",
            "area.id", "area.name", "curScore", "curLeMind", "curLeScore", "totalScore",
            "totalLeMind", "totalLeScore", "curLeBean", "totalLeBean"};
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, endUser);
    response.setMsg(map);

    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 修改密码（支付密码和登录密码）
   *
   * @param request
   * @param resetPwdReq
   * @return
   */
  @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
  public @ResponseBody BaseResponse updatePwd(HttpServletRequest request,
      @RequestBody UserRequest req) {
    BaseResponse response = new BaseResponse();
    String serverPrivateKey = setting.getServerPrivateKey();
    String password_confirm = req.getPassword_confirm();
    String password = req.getPassword();
    String smsCode = req.getSmsCode();
    String cellPhoneNum = req.getCellPhoneNum();
    SmsCodeType smsCodeType = req.getSmsCodeType();
    String token = req.getToken();
    Long userId = req.getUserId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    if (StringUtils.isEmpty(cellPhoneNum) || !isMobileNumber(cellPhoneNum)) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.mobile.invaliable").getContent());
      return response;
    }

    EndUser user = endUserService.findByUserMobile(cellPhoneNum);
    if (user == null) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.user.noexist").getContent());
      return response;

    }
    // 密码非空验证
    if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.pwd.null.error").getContent());
      return response;
    }

    try {
      password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
      password_confirm =
          KeyGenerator.decrypt(password_confirm, RSAHelper.getPrivateKey(serverPrivateKey));
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!password.equals(password_confirm)) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.pwd.no.same").getContent());
      return response;
    }

    if (password.length() < setting.getPasswordMinlength()) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.pwd.length.error").getContent());
      return response;
    }

    SMSVerificationCode smsVerficationCode = endUserService.getSmsCode(cellPhoneNum);
    if (smsVerficationCode == null) {
      response.setCode(CommonAttributes.FAIL_RESET_PWD);
      response.setDesc(Message.error("rebate.sms.invaliable").getContent());
      return response;
    } else {
      String code = smsVerficationCode.getSmsCode();
      String timeoutToken = smsVerficationCode.getTimeoutToken();
      if (timeoutToken != null
          && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
        if (!smsCode.equals(code)) {
          response.setCode(CommonAttributes.FAIL_RESET_PWD);
          response.setDesc(Message.error("rebate.sms.token.error").getContent());
          return response;
        } else {
          endUserService.deleteSmsCode(cellPhoneNum);
        }
      } else {
        response.setCode(CommonAttributes.FAIL_RESET_PWD);
        response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
        return response;
      }
    }

    if (smsCodeType.equals(SmsCodeType.UPDATELOGINPWD)) {
      user.setLoginPwd(DigestUtils.md5Hex(password));
    } else if (smsCodeType.equals(SmsCodeType.UPDATEPAYPWD)) {
      user.setPaymentPwd(DigestUtils.md5Hex(password));
    }

    endUserService.update(user);
    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "Update Password",
          "EndUser Update Password. CellPhone: %s,Type: %s", cellPhoneNum, smsCodeType.toString());
    }

    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;

  }

  /**
   * 修改用户头像
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/editUserPhoto", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> editUserPhoto(UserRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = req.getUserId();
    String token = req.getToken();
    MultipartFile photo = req.getPhoto();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);

    endUser.setUserPhoto(fileService.saveImage(photo, ImageType.PHOTO));
    endUserService.update(endUser);
    if (LogUtil.isDebugEnabled(EndUserController.class)) {
      LogUtil.debug(EndUserController.class, "editUserPhoto",
          "Edit photo for EndUser. CellPhoneNum: %s, PhotoUrl: %s", endUser.getCellPhoneNum(),
          endUser.getUserPhoto());
    }

    String[] properties =
        {"id", "cellPhoneNum", "nickName", "userPhoto", "recommender", "agent.agencyLevel",
            "area.id", "area.name", "curScore", "curLeMind", "curLeScore", "totalScore",
            "totalLeMind", "totalLeScore", "curLeBean", "totalLeBean"};
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, endUser);
    map.putAll(endUserService.isUserHasSeller(endUser));

    response.setMsg(map);
    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 获取用户信息
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
  @UserValidCheck
  public @ResponseBody ResponseOne<Map<String, Object>> getUserInfo(@RequestBody BaseRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = req.getUserId();
    String token = req.getToken();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);

    String[] properties =
        {"id", "cellPhoneNum", "nickName", "userPhoto", "recommender", "agent.agencyLevel",
            "area.id", "area.name", "curScore", "curLeMind", "curLeScore", "totalScore",
            "totalLeMind", "totalLeScore", "curLeBean", "totalLeBean", "isBindWeChat"};
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, endUser);
    map.putAll(endUserService.isUserHasSeller(endUser));
    map.put("isSetLoginPwd", false);
    map.put("isSetPayPwd", false);
    if (endUser.getLoginPwd() != null) {
      map.put("isSetLoginPwd", true);
    }
    if (endUser.getPaymentPwd() != null) {
      map.put("isSetPayPwd", true);
    }
    response.setMsg(map);

    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户二维码
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/getQrCode", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> getQrCode(@RequestBody BaseRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = req.getUserId();
    String token = req.getToken();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    Map<String, Object> map = new HashMap<String, Object>();
    if (endUser.getQrImage() == null) {
      String content =
          "{\"flag\":\"" + DigestUtils.md5Hex("翼享生活") + "\",\"cellPhoneNum\":\""
              + endUser.getCellPhoneNum() + "\"}";
      byte[] bytes = QRCodeGenerator.generateQrImage(content);
      endUser.setQrImage(bytes);
      endUserService.update(endUser);
    }

    String[] properties = {"qrImage"};
    map = FieldFilterUtils.filterEntityMap(properties, endUser);
    response.setMsg(map);

    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 获取用户推荐记录
   * 
   * @return
   */
  @RequestMapping(value = "/getRecommendRec", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> getRecommendRec(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);

    Page<UserRecommendRelation> page =
        userRecommendRelationService.getRelationsByRecommender(userId, pageable);
    String[] propertys =
        {"id", "endUser.nickName", "endUser.totalLeScore", "endUser.userPhoto",
            "endUser.sellerName", "endUser.sellerPicUrl"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户积分记录列表
   * 
   * @return
   */
  @RequestMapping(value = "/scoreRec", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> scoreRec(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("endUser", Operator.eq, endUser);
    filters.add(userFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<RebateRecord> page = rebateRecordService.findPage(pageable);
    String[] propertys =
        {"id", "seller.name", "createDate", "seller.storePictureUrl", "rebateScore", "paymentType",
            "userCurScore"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户乐心记录列表
   * 
   * @return
   */
  @RequestMapping(value = "/leMindRec", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> leMindRec(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("endUser", Operator.eq, endUser);
    filters.add(userFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<LeMindRecord> page = leMindRecordService.findPage(pageable);
    String[] propertys = {"id", "amount", "createDate", "score", "userCurLeMind"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户乐分记录列表
   * 
   * @return
   */
  @RequestMapping(value = "/leScoreRec", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> leScoreRec(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("endUser", Operator.eq, endUser);
    filters.add(userFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<LeScoreRecord> page = leScoreRecordService.findPage(pageable);
    String[] propertys =
        {"id", "endUser.userPhoto", "createDate", "endUser.nickName", "amount", "leScoreType",
            "userCurLeScore", "recommender", "recommenderPhoto", "withdrawStatus", "remark"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户乐豆记录列表
   * 
   * @return
   */
  @RequestMapping(value = "/leBeanRec", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> leBeanRec(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("endUser", Operator.eq, endUser);
    filters.add(userFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<LeBeanRecord> page = leBeanRecordService.findPage(pageable);
    String[] propertys =
        {"id", "amount", "createDate", "userCurLeBean", "type", "recommender", "recommenderPhoto"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 添加收藏
   * 
   * @return
   */
  @RequestMapping(value = "/favoriteSeller", method = RequestMethod.POST)
  public @ResponseBody BaseResponse favoriteSeller(@RequestBody UserRequest request) {

    BaseResponse response = new BaseResponse();
    Long userId = request.getUserId();
    String token = request.getToken();
    Long sellerId = request.getEntityId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    Seller seller = sellerService.find(sellerId);
    if (endUser == null || seller == null) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(Message.error("rebate.request.param.invalid").getContent());
      return response;
    }
    Set<Seller> sellers = endUser.getFavoriteSellers();
    if (sellers == null) {
      sellers = new HashSet<Seller>();
    }
    if (request.getIsFavoriteAdd()) {
      if (!sellers.contains(seller)) {
        sellers.add(seller);
        endUser.setFavoriteSellers(sellers);
        int favoriteNum = seller.getFavoriteNum() == null ? 0 : seller.getFavoriteNum();
        seller.setFavoriteNum(++favoriteNum);
      }
    } else {
      if (sellers.contains(seller)) {
        sellers.remove(seller);
        endUser.setFavoriteSellers(sellers);
        int favoriteNum = seller.getFavoriteNum() == null ? 0 : seller.getFavoriteNum();
        seller.setFavoriteNum(--favoriteNum);
      }
    }
    endUserService.update(endUser);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setCode(CommonAttributes.SUCCESS);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 用户收藏列表
   * 
   * @return
   */
  @RequestMapping(value = "/favoriteSellerList", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> favoriteSellerList(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);

    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("favoriteEndUsers", Operator.eq, endUser);
    filters.add(userFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<Seller> page = sellerService.findFavoriteSellers(pageable, userId);
    String[] propertys =
        {"id", "name", "sellerCategory.categoryName", "latitude", "longitude", "rateScore",
            "avgPrice", "storePictureUrl", "address", "discount"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    SystemConfig rebateScore = systemConfigService.getConfigByKey(SystemConfigKey.REBATESCORE_USER);
    SystemConfig unitConsume = systemConfigService.getConfigByKey(SystemConfigKey.UNIT_CONSUME);
    for (Map<String, Object> map : result) {

      BigDecimal unit = new BigDecimal(unitConsume.getConfigValue());
      map.put("unitConsume", unit);
      BigDecimal rebateUserScore =
          unit.subtract(
              ((BigDecimal) map.get("discount")).divide(new BigDecimal("10")).multiply(unit))
              .multiply(new BigDecimal(rebateScore.getConfigValue()));
      map.put("rebateScore", rebateUserScore);
    }
    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 用户获取乐分提现信息
   * 
   * @return
   */
  @RequestMapping(value = "/getWithdrawInfo", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> withdrawReq(@RequestBody UserRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = request.getUserId();
    String token = request.getToken();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("wxUserPhoto", "wechatUserPhoto");
    map.put("wxNickName", "wxNickName");
    map.put("curLeScore", endUser.getCurLeScore());
    map.putAll(endUserService.getAvlLeScore(endUser));
    response.setMsg(map);

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setCode(CommonAttributes.SUCCESS);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 用户确认乐分提现
   * 
   * @return
   */
  @RequestMapping(value = "/withdrawConfirm", method = RequestMethod.POST)
  public @ResponseBody BaseResponse withdrawConfirm(@RequestBody UserRequest request) {
    String serverPrivateKey = setting.getServerPrivateKey();
    BaseResponse response = new BaseResponse();
    Long userId = request.getUserId();
    String token = request.getToken();
    String password = request.getPassword();
    String remark = request.getRemark();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    // 密码非空验证
    if (StringUtils.isEmpty(password)) {
      response.setCode(CommonAttributes.FAIL_USER_WITHDRAW);
      response.setDesc(Message.error("rebate.pwd.null.error").getContent());
      return response;
    }
    // 支付密码未设置
    if (StringUtils.isEmpty(endUser.getPaymentPwd())) {
      response.setCode(CommonAttributes.FAIL_USER_WITHDRAW);
      response.setDesc(Message.error("rebate.payPwd.not.set").getContent());
      return response;
    }
    try {
      password = KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 密码长度验证
    if (password.length() < setting.getPasswordMinlength()) {
      response.setCode(CommonAttributes.FAIL_USER_WITHDRAW);
      response.setDesc(Message.error("rebate.pwd.length.error", setting.getPasswordMinlength())
          .getContent());
      return response;
    }

    if (!DigestUtils.md5Hex(password).equals(endUser.getPaymentPwd())) {
      response.setCode(CommonAttributes.FAIL_USER_WITHDRAW);
      response.setDesc(Message.error("rebate.payPwd.error").getContent());
      return response;
    }

    endUserService.userWithdraw(userId, remark);

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setCode(CommonAttributes.SUCCESS);
    response.setToken(newtoken);
    return response;
  }
}
