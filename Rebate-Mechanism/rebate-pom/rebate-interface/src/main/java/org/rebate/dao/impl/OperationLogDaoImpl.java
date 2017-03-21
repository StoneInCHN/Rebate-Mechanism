package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.OperationLog;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.OperationLogDao;
@Repository("operationLogDaoImpl")
public class OperationLogDaoImpl extends  BaseDaoImpl<OperationLog,Long> implements OperationLogDao {

}