package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.HotCity;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.HotCityDao;
@Repository("hotCityDaoImpl")
public class HotCityDaoImpl extends  BaseDaoImpl<HotCity,Long> implements HotCityDao {

}