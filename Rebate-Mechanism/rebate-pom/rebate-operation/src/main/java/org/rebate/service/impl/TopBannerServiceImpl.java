package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.TopBanner;
import org.rebate.dao.TopBannerDao;
import org.rebate.service.TopBannerService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("topBannerServiceImpl")
public class TopBannerServiceImpl extends BaseServiceImpl<TopBanner,Long> implements TopBannerService {

      @Resource(name="topBannerDaoImpl")
      public void setBaseDao(TopBannerDao topBannerDao) {
         super.setBaseDao(topBannerDao);
  }
}