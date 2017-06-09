package org.rebate.dao;

import org.rebate.entity.UserAuth;
import org.rebate.framework.dao.BaseDao;

public interface UserAuthDao extends BaseDao<UserAuth, Long> {
  /**
   * 获取用户实名认证信息
   * 
   * @param userId
   * @return
   */
  UserAuth getUserAuth(Long userId, Boolean isAuth);

  /**
   * 根据身份证号码获取用户实名认证信息
   * 
   * @param userId
   * @return
   */
  UserAuth getUserAuthByIdCard(String cardNo, Boolean isAuth);
}
