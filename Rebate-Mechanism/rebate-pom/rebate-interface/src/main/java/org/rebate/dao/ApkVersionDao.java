package org.rebate.dao;

import org.rebate.entity.ApkVersion;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.dao.BaseDao;

public interface ApkVersionDao extends BaseDao<ApkVersion, Long> {
  /**
   * 获取最新版本
   * 
   * @param versionCode
   * @return
   */
  ApkVersion getNewVersion(Integer versionCode, AppPlatform appPlatform);
}
