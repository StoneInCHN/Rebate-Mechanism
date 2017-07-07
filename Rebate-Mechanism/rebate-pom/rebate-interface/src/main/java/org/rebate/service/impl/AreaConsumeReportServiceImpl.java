package org.rebate.service.impl; 

import javax.annotation.Resource;

import org.rebate.dao.AreaConsumeReportDao;
import org.rebate.entity.AreaConsumeReport;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.AgentAreaRequest;
import org.rebate.json.response.AreaAmountResponse;
import org.rebate.json.response.AreaCountResponse;
import org.rebate.json.response.SalesManReport;
import org.rebate.service.AreaConsumeReportService;
import org.springframework.stereotype.Service;

@Service("areaConsumeReportServiceImpl")
public class AreaConsumeReportServiceImpl extends BaseServiceImpl<AreaConsumeReport,Long> implements AreaConsumeReportService {
	
	@Resource(name="areaConsumeReportDaoImpl")
	private AreaConsumeReportDao areaConsumeReportDao;
	
//	@Resource(name="areaDaoImpl")
//	private AreaDao areaDao;
//	
//	@Resource(name="sellerDaoImpl")
//	private SellerDao sellerDao;
	
    @Resource(name="areaConsumeReportDaoImpl")
    public void setBaseDao(AreaConsumeReportDao areaConsumeReportDao) {
        super.setBaseDao(areaConsumeReportDao);
    }

	@Override
	public ResponseMultiple<AreaAmountResponse> getConsumeAmountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaAmountResponse> response) {
		return areaConsumeReportDao.getConsumeAmountReport(request, response);
	}

//	@Override
//	public ResponseOne<ProvinceCount> getSellerCountReport(
//			AgentAreaRequest request, ResponseOne<ProvinceCount> response) {
//		ProvinceCount provinceCount = new ProvinceCount();
//		if (request.getAgencyLevel() == AgencyLevel.PROVINCE) {//省
//			Area province = areaDao.find(request.getAreaId());
//			provinceCount.setProvince(province.getName());
//			long provinceSellerNum = 0;
//			Set<Area> citySet = province.getChildren();
//			if (citySet.size() > 0) {
//				List<CityCount> cityList = new ArrayList<CityCount>();
//				for (Area city : citySet) {
//					CityCount cityCount = new CityCount();
//					cityCount.setCity(city.getName());
//					long citySellerNum = 0;
//					Set<Area> countrySet = city.getChildren();
//					if (countrySet.size() > 0) {
//						List<CountryCount> countryList = new ArrayList<CountryCount>();
//						cityCount.setCountryList(countryList);
//						for (Area country : countrySet) {
//							long countrySellerNum = sellerDao.count(Filter.eq("area", country.getId()));
//							if (countrySellerNum > 0) {
//								CountryCount countryCount = new CountryCount();
//								countryCount.setCountry(country.getName());
//								countryCount.setCount(countrySellerNum);
//								countryList.add(countryCount);
//								citySellerNum += countryCount.getCount();
//							}
//						}
//					}
//					if (citySellerNum > 0) {
//						cityCount.setCount(citySellerNum);
//						cityList.add(cityCount);
//						provinceSellerNum += cityCount.getCount();
//					}
//
//				}
//				provinceCount.setCount(provinceSellerNum);
//				provinceCount.setCityList(cityList);
//			}
//		}
//		response.setMsg(provinceCount);
//		return response;
//	}
	
	@Override
	public ResponseMultiple<AreaCountResponse> getSellerCountReport(
			AgentAreaRequest request, ResponseMultiple<AreaCountResponse> response) {
		return areaConsumeReportDao.getSellerCountReport(request, response);
	}

	@Override
	public ResponseMultiple<AreaCountResponse> getEndUserCountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse> response) {
		return areaConsumeReportDao.getEndUserCountReport(request, response);
	}

	@Override
	public ResponseMultiple<AreaCountResponse<SalesManReport>> getSalesmanCountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse<SalesManReport>> response) {
		return areaConsumeReportDao.getSalesmanCountReport(request, response);
	}

}