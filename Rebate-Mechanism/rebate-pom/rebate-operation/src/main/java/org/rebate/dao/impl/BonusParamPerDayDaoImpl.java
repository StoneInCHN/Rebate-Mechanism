package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.BonusParamPerDay;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.BonusParamPerDayDao;
@Repository("bonusParamPerDayDaoImpl")
public class BonusParamPerDayDaoImpl extends  BaseDaoImpl<BonusParamPerDay,Long> implements BonusParamPerDayDao {

}