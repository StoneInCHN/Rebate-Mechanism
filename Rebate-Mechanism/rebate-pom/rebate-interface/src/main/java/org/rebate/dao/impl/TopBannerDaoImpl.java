package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.TopBanner;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.TopBannerDao;
@Repository("topBannerDaoImpl")
public class TopBannerDaoImpl extends  BaseDaoImpl<TopBanner,Long> implements TopBannerDao {

}