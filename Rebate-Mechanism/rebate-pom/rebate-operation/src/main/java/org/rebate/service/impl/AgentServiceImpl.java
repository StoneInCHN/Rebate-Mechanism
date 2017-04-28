package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.rebate.entity.Agent;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.dao.AgentDao;
import org.rebate.service.AgentService;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("agentServiceImpl")
public class AgentServiceImpl extends BaseServiceImpl<Agent,Long> implements AgentService {

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
  
    @Resource(name = "endUserServiceImpl")
    private EndUserService endUserService;
    
        @Resource(name="agentDaoImpl")
        public void setBaseDao(AgentDao agentDao) {
           super.setBaseDao(agentDao);
    }

      @Override
      @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
      public void updateAgent(Long areaId, Long endUserId, Agent agent) {
        Area area = areaService.find(areaId);
        EndUser endUser = endUserService.find(endUserId);
        if(area!=null&&endUser!=null &&agent!=null&& agent.getId()!=null){
          Agent tempAgent = this.find(agent.getId());
          EndUser endUserTemp = tempAgent.getEndUser();
          endUserTemp.setAgent(null);
          endUserService.update(endUserTemp);
          endUser.setAgent(agent);
          endUserService.update(endUser);
          agent.setArea(area);
          agent.setEndUser(endUser);
          this.update(agent);
        }
      }

      @Override
      @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
      public void delete(Long... ids) {
        if(ids!=null && ids.length >0){
          for (Long id : ids) {
            Agent agent =  this.find(id);
            EndUser endUser = agent.getEndUser();
            endUser.setAgent(null);
            endUserService.update(endUser);
            this.delete(agent);
          }
        }
      }
      
      
      
}