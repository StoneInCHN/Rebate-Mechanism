package org.rebate.service;

import java.util.Date;
import java.util.List;

import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.service.BaseService;

public interface EndUserService extends BaseService<EndUser, Long> {

  /**
   * 根据用户名查找终端用户
   * 
   * @param username 用户名
   * @return 终端用户，若不存在则返回null
   */
  EndUser findByUserName(String userName);

  /**
   * 根据手机号码查找终端用户
   * 
   * @param mobileNo 用户名
   * @return 终端用户，若不存在则返回null
   */
  EndUser findByUserMobile(String mobileNo);

  /**
   * 获取终端用户登录token
   * 
   * @return
   */
  String getEndUserToken(Long id);

  /**
   * 创建终端用户登录token
   */
  String createEndUserToken(String token, Long id);

  /**
   * 获取终端用户登录手机平台信息
   * 
   * @return
   */
  AppPlatform getEndUserAppPlatform(Long id);

  /**
   * 创建终端用户登录手机平台信息
   */
  AppPlatform createEndUserAppPlatform(AppPlatform appPlatform, Long id);

  /**
   * 删除终端用户登录token
   */
  void deleteEndUserToken(Long id);

  /**
   * 用户注册
   * 
   * @param userName
   * @param password
   * @return
   */
  EndUser userReg(String userName, String password);


  /**
   * 用户每日乐分分红计算
   */
  void dailyBonusCalJob(Date startTime, Date endTime);

  /**
   * 根据时间获取每日产生大于等于1个乐心的用户
   * 
   * @param startTime
   * @param endTime
   * @return
   */
  List<EndUser> getMindUsersByDay(Date startTime, Date endTime);

  /**
   * 根据区域获取代理商
   * 
   * @return
   */
  EndUser getAgentByArea(Area area);
}
