package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.ParamConfig;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.ParamConfigDao;
@Repository("paramConfigDaoImpl")
public class ParamConfigDaoImpl extends  BaseDaoImpl<ParamConfig,Long> implements ParamConfigDao {

}