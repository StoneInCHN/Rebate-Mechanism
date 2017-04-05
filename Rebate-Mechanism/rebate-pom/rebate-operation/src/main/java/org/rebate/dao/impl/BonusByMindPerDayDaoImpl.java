package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.BonusByMindPerDay;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.BonusByMindPerDayDao;
@Repository("bonusByMindPerDayDaoImpl")
public class BonusByMindPerDayDaoImpl extends  BaseDaoImpl<BonusByMindPerDay,Long> implements BonusByMindPerDayDao {

}