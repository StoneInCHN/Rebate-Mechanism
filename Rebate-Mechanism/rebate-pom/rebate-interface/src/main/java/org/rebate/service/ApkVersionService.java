package org.rebate.service;

import org.rebate.entity.ApkVersion;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.service.BaseService;

public interface ApkVersionService extends BaseService<ApkVersion, Long> {

  /**
   * 获取最新版本
   * 
   * @param versionCode
   * @return
   */
  ApkVersion getNewVersion(Integer versionCode, AppPlatform appPlatform);
}
