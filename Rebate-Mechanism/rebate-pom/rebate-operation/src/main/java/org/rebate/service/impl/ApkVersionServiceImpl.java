package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.ApkVersion;
import org.rebate.dao.ApkVersionDao;
import org.rebate.service.ApkVersionService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("apkVersionServiceImpl")
public class ApkVersionServiceImpl extends BaseServiceImpl<ApkVersion,Long> implements ApkVersionService {

      @Resource(name="apkVersionDaoImpl")
      public void setBaseDao(ApkVersionDao apkVersionDao) {
         super.setBaseDao(apkVersionDao);
  }
}