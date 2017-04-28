package org.rebate.service;

import org.rebate.entity.ApkVersion;
import org.rebate.framework.service.BaseService;

public interface ApkVersionService extends BaseService<ApkVersion, Long> {

  Boolean versionExists(String versionName, Long id);

  /**
   * 获取当前最新版本
   * 
   * @return
   */
  ApkVersion getCurNewApkVersion();

}
