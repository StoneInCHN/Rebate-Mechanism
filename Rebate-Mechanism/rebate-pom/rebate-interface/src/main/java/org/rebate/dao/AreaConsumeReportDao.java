package org.rebate.dao; 
import org.rebate.entity.AreaConsumeReport;
import org.rebate.framework.dao.BaseDao;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.AgentAreaRequest;
import org.rebate.json.response.AreaAmountResponse;
import org.rebate.json.response.AreaCountResponse;
import org.rebate.json.response.SalesManReport;

public interface AreaConsumeReportDao extends  BaseDao<AreaConsumeReport,Long>{

	ResponseMultiple<AreaAmountResponse> getConsumeAmountReport(AgentAreaRequest request,
			ResponseMultiple<AreaAmountResponse> response);

	ResponseMultiple<AreaCountResponse> getSellerCountReport(AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse> response);

	ResponseMultiple<AreaCountResponse> getEndUserCountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse> response);

	ResponseMultiple<AreaCountResponse<SalesManReport>> getSalesmanCountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse<SalesManReport>> response);


}