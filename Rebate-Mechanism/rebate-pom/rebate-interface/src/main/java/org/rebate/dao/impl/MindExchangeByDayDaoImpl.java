package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.MindExchangeByDay;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.MindExchangeByDayDao;
@Repository("mindExchangeByDayDaoImpl")
public class MindExchangeByDayDaoImpl extends  BaseDaoImpl<MindExchangeByDay,Long> implements MindExchangeByDayDao {

}