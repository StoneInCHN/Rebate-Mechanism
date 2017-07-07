package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.BonusForUserPerDay;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.BonusForUserPerDayDao;
@Repository("bonusForUserPerDayDaoImpl")
public class BonusForUserPerDayDaoImpl extends  BaseDaoImpl<BonusForUserPerDay,Long> implements BonusForUserPerDayDao {

}