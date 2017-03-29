package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SnDao;
import org.rebate.entity.Sn.Type;
import org.rebate.service.SnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("snServiceImpl")
public class SnServiceImpl implements SnService {

  @Resource(name = "snDaoImpl")
  private SnDao snDao;

  @Transactional
  public String generate(Type type) {
    return snDao.generate(type);
  }
}
