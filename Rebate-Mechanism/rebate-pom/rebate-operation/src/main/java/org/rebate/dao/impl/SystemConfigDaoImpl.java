package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SystemConfig;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SystemConfigDao;
@Repository("systemConfigDaoImpl")
public class SystemConfigDaoImpl extends  BaseDaoImpl<SystemConfig,Long> implements SystemConfigDao {

}