package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Area;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.AreaDao;
@Repository("areaDaoImpl")
public class AreaDaoImpl extends  BaseDaoImpl<Area,Long> implements AreaDao {

}