package org.rebate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.ApkVersion;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.SettingConfigRequest;
import org.rebate.service.ApkVersionService;
import org.rebate.service.EndUserService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("commonController")
@RequestMapping("/common")
public class CommonController extends MobileBaseController {


  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "apkVersionServiceImpl")
  private ApkVersionService apkVersionService;


  /**
   * 初始化jpush reg id
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/setJpushId", method = RequestMethod.POST)
  @UserValidCheck
  public @ResponseBody ResponseOne<Map<String, Object>> setRegId(
      @RequestBody SettingConfigRequest req) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = req.getUserId();
    String token = req.getToken();
    String jPushRegId = req.getJpushRegId();
    AppPlatform appPlatform = req.getAppPlatform();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter("jpushRegId", Operator.eq, jPushRegId);
    filters.add(filter);
    List<EndUser> updateUsers = new ArrayList<EndUser>();
    List<EndUser> users = endUserService.findList(null, filters, null);
    if (!CollectionUtils.isEmpty(users)) {
      for (EndUser user : users) {
        if (!user.getId().equals(endUser.getId()) && jPushRegId.equals(user.getJpushRegId())) {
          user.setJpushRegId(null);
          updateUsers.add(user);
        }
      }
    }

    endUser.setJpushRegId(jPushRegId);
    updateUsers.add(endUser);
    endUserService.update(updateUsers);

    endUserService.createEndUserAppPlatform(appPlatform, userId);
    if (LogUtil.isDebugEnabled(CommonController.class)) {
      LogUtil.debug(CommonController.class, "setJpushId",
          "init User Jpush reg ID.userId: %s, jpushRegId: %s, appPlatform: %s", userId, jPushRegId,
          appPlatform);
    }

    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 获取app版本更新信息
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/getAppVersion", method = RequestMethod.POST)
  @UserValidCheck
  public @ResponseBody ResponseOne<Map<String, Object>> getAppVersion(
      @RequestBody SettingConfigRequest req) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    AppPlatform appPlatform = AppPlatform.ANDROID;
    Integer versionCode = req.getVersionCode();


    if (LogUtil.isDebugEnabled(CommonController.class)) {
      LogUtil.debug(CommonController.class, "getAppVersion",
          "get app version param.versionCode: %s, appPlatform: %s", versionCode, appPlatform);
    }

    Map<String, Object> map = new HashMap<String, Object>();
    ApkVersion version = apkVersionService.getNewVersion(versionCode, appPlatform);
    String[] properties =
        {"id", "versionName", "versionCode", "apkPath", "updateContent", "isForced"};
    map = FieldFilterUtils.filterEntityMap(properties, version);

    if (LogUtil.isDebugEnabled(CommonController.class)) {
      LogUtil.debug(CommonController.class, "getAppVersion",
          "get app version result.versionCode: %s, appPlatform: %s",
          version != null ? version.getVersionCode() : null,
          version != null ? version.getAppPlatform() : null);
    }

    response.setMsg(map);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
}