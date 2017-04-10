package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.UserHelp;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.UserHelpDao;
@Repository("userHelpDaoImpl")
public class UserHelpDaoImpl extends  BaseDaoImpl<UserHelp,Long> implements UserHelpDao {

}