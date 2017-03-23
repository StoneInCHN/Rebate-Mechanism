package org.rebate.dao;

import org.rebate.beans.SMSVerificationCode;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.dao.BaseDao;

public interface EndUserDao extends BaseDao<EndUser, Long> {

  /**
   * 根据用户名查找终端用户
   * 
   * @param username 用户名
   * @return 终端用户， 若不存在则返回null
   */
  EndUser findByUserName(String username);

  /**
   * 根据手机号码查找终端用户
   * 
   * @param mobileNo 手机号
   * @return 终端用户， 若不存在则返回null
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
   * 删除终端用户登录token
   */
  void deleteEndUserToken(Long id);

  /**
   * 获取终端用户登录手机平台信息
   * 
   */
  AppPlatform getEndUserAppPlatform(Long id);

  /**
   * 创建终端用户登录手机平台信息
   */
  AppPlatform createEndUserAppPlatform(AppPlatform appPlatform, Long id);

  /**
   * 将短信验证码实体对象存入缓存
   * 
   * @param smsCode
   * @return
   */
  SMSVerificationCode createSmsCode(String cellPhoneNum, SMSVerificationCode smsCode);

  /**
   * 根据手机号获取缓存中的短信验证码对象
   * 
   * @param smsCode
   * @return
   */
  SMSVerificationCode getSmsCode(String cellPhone);

  /**
   * 根据手机号删除缓存中的短信验证码对象
   * 
   * @param smsCode
   * @return
   */
  void deleteSmsCode(String cellPhone);
}
