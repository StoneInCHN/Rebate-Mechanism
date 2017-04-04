package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SettingConfig;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SettingConfigDao;
@Repository("settingConfigDaoImpl")
public class SettingConfigDaoImpl extends  BaseDaoImpl<SettingConfig,Long> implements SettingConfigDao {

}