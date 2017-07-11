package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.AgentDao;
import org.rebate.entity.Agent;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.AgentService;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("agentServiceImpl")
public class AgentServiceImpl extends BaseServiceImpl<Agent, Long> implements AgentService {

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  
  @Resource(name = "agentDaoImpl")
  public AgentDao agentDao;
  
  @Resource(name = "agentDaoImpl")
  public void setBaseDao(AgentDao agentDao) {
    super.setBaseDao(agentDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void updateAgent(Long areaId, Long endUserId, Agent agent) {
    //Area area = areaService.find(areaId);
    EndUser endUser = endUserService.find(endUserId);
    if (endUser != null && agent != null && agent.getId() != null) {
      Agent tempAgent = this.find(agent.getId());
      EndUser endUserTemp = tempAgent.getEndUser();
      endUserTemp.setAgent(null);
      endUserService.update(endUserTemp);
      endUser.setAgent(agent);
      endUserService.update(endUser);
      agent.setArea(tempAgent.getArea());
      agent.setAgencyLevel(tempAgent.getAgencyLevel());
      agent.setEndUser(endUser);
      this.update(agent);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void delete(Long... ids) {
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        Agent agent = this.find(id);
        EndUser endUser = agent.getEndUser();
        endUser.setAgent(null);
        endUserService.update(endUser);
        this.delete(agent);
      }
    }
  }

  @Override
  public boolean areaExists(Long areaId) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("area", areaId));
    List<Agent> agents = agentDao.findList(null, null, filters, null);
    if(agents!=null&& agents.size()>0){
      return true;
    }else{
      return false;
    }
  }



}
