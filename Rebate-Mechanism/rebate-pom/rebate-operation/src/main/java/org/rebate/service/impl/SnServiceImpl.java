package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import org.rebate.entity.Sn;
import org.rebate.entity.Sn.Type;
import org.rebate.dao.SnDao;
import org.rebate.service.SnService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("snServiceImpl")
public class SnServiceImpl extends BaseServiceImpl<Sn,Long> implements SnService {
	
    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    @Transactional
    public String generate(Type type) {
      return snDao.generate(type);
    }
}