$().ready(
function() {
	$("#chart_report_search").click(function(){
		$.ajax({
			url : "../report/userBonusReportData.jhtml",
			type : "get",
			data:$("#charts_report_search_form").serialize(),
			success : function(result, response, status) {
				var _categories=new Array();
				var _series=new Array();
				var bonusLeScoreData=new Array();
				var consumeTotalAmountData=new Array();
				var highBonusLeScoreData=new Array();
				for(var i=0;i<result.length;i++){
					_categories.push(result[i].reportDate);
					
					bonusLeScoreData.push(result[i].bonusLeScore);
					consumeTotalAmountData.push(result[i].consumeTotalAmount);
					highBonusLeScoreData.push(result[i].highBonusLeScore);
				}
				$('#userBonusReportDivId')
				.highcharts(
						{
							chart : {
								type : 'column'
							},
							title : {
								text : '分红统计'
							},
							xAxis : {
								categories : _categories,
								crosshair : true
							},
							yAxis : {
								min : 0,
								title : {
									text : '金额'
								}
							},
							tooltip : {
								headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
								pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
										+ '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
								footerFormat : '</table>',
								shared : true,
								useHTML : true
							},
							plotOptions : {
								column : {
									pointPadding : 0.2,
									borderWidth : 0
								}
							},
							series : [
									{
										name : '分红金额',
										data : bonusLeScoreData
									},
									{
										name : '总消费金额',
										data : consumeTotalAmountData
									},
									{
										name : '最高分红',
										data : highBonusLeScoreData
									} ]
						});
			}
		});
	});
	
	var isNationChartsShown = false;
	var isRegChartsShown = false;
	$("a[href='#nation_charts']").click(function (e) {
		if(!isNationChartsShown){
			isNationChartsShown=true;
			nationChartDate('listForm');
		}
	});
	$("#nationBonusReport_search").click(function(){
		nationChartDate('chartsForm');
	});
	//用户注册统计
	$("#user_reg_report_search").click(function(){
		userRegChartDate("charts_report_search_form")
	});
	$("a[href='#uerReg_charts']").click(function (e) {
		if(!isRegChartsShown){
			isRegChartsShown=true;
			userRegChartDate('listForm');
		}
	});
})
function userRegChartDate(formId){
	$.ajax({
		url : "../report/userRegReportData.jhtml",
		type : "get",
		data:$("#"+formId).serialize(),
		success : function(result, response, status) {
			var _categories=new Array();
			var userRegData=new Array();
			for(var i=0;i<result.length;i++){
				_categories.push(result[i].statisticsDate);
				
				userRegData.push(result[i].regNum);
			}
			$('#userRegReportDivId')
			.highcharts(
					{
						chart : {
							type : 'column'
						},
						title : {
							text : '注册统计'
						},
						xAxis : {
							categories : _categories,
							crosshair : true
						},
						yAxis : {
							min : 0,
							title : {
								text : '人数'
							}
						},
						tooltip : {
							headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
							pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
									+ '<td style="padding:0"><b>{point.y} 个</b></td></tr>',
							footerFormat : '</table>',
							shared : true,
							useHTML : true
						},
						plotOptions : {
							column : {
								pointPadding : 0.2,
								borderWidth : 0
							}
						},
						series : [
								{
									name : '注册人数',
									data : userRegData
								}
								]
					});
		}
	});

}

//全国统计数据方法
function nationChartDate(formId){
	$.ajax({
		url : "../report/nationBonusReportData.jhtml",
		type : "get",
		data:$("#"+formId).serialize(),
		success : function(result, response, status) {
			if(result.length==0){
				alert('对不起，统计数据缺失')
			}
			debugger;
			var _categories=new Array();
			var consumeTotalAmountData=new Array();
			var consumePeopleNumData=new Array();
			var sellerNumData=new Array();
			//var publicTotalAmountData=new Array();
			var totalBonusData=new Array();
			var leMindByDayData=new Array();
			var consumeByDayData=new Array();
			var bonusLeScoreByDayData=new Array();
			//var publicAmountByDayData=new Array();
			var platformIncomeData=new Array();
			//var awardData=new Array();
			var ventureFundData = new Array();
			var agentCommissionData = new Array();
			var userRecommendCommissionData = new Array();
			var sellerRecommendCommissionData = new Array();
			for(var i=0;i<result.length;i++){
				_categories.push(result[i].reportDate);
				consumeTotalAmountData.push(result[i].consumeTotalAmount/1000);
				consumePeopleNumData.push(result[i].consumePeopleNum);
				sellerNumData.push(result[i].sellerNum);
				//publicTotalAmountData.push(result[i].publicTotalAmount/1000);
				leMindByDayData.push(result[i].leMindByDay);
				consumeByDayData.push(result[i].consumeByDay/1000);
				bonusLeScoreByDayData.push(result[i].bonusLeScoreByDay/1000);
				totalBonusData.push(result[i].totalBonus/1000);
				//publicAmountByDayData.push(result[i].publicAmountByDay);
				platformIncomeData.push(result[i].platformIncome);
				//awardData.push(result[i].award)
				ventureFundData.push(result[i].ventureFund)
				agentCommissionData.push(result[i].agentCommission)
				userRecommendCommissionData.push(result[i].userRecommendCommission)
				sellerRecommendCommissionData.push(result[i].sellerRecommendCommission)
			}
			$('#nationBonusReportDivId')
			.highcharts(
					{
						chart : {
							type : 'column'
						},
						title : {
							text : '分红统计'
						},
						xAxis : {
							categories : _categories,
							crosshair : true
						},
						yAxis : {
							min : 0,
							title : {
								text : '金额'
							}
						},
						tooltip : {
							headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
							pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
									+ '<td style="padding:0"><b>{point.y:.2f} </b></td></tr>',
							footerFormat : '</table>',
							shared : true,
							useHTML : true
						},
						plotOptions : {
							column : {
								pointPadding : 0.2,
								borderWidth : 0
							}
						},
						series : [
								{
									name : '全国消费总额(k)',
									data : consumeTotalAmountData
								},
								{
									name : '全国消费人数',
									data : consumePeopleNumData
								},
								{
									name : '全国公益商家',
									data : sellerNumData
								} ,
//								{
//									name : '公益总金额(k)',
//									data : publicTotalAmountData
//								},
								{
									name : '当日累计分红乐心',
									data : leMindByDayData
								},
								{
									name : '当日累计消费(k)',
									data : consumeByDayData
								},
								{
									name : '当日分红金额(k)',
									data : bonusLeScoreByDayData
								},
								{
									name : '累计分红(k)',
									data : totalBonusData
								},
//								{
//									name : '公益金额',
//									data : publicAmountByDayData
//								},
								{
									name : '平台收益',
									data : platformIncomeData
								},
//								{
//									name : '奖池',
//									data : awardData
//								},
								{
									name : '创业基金',
									data : ventureFundData
								},
								{
									name : '代理商提成金',
									data : agentCommissionData
								},
								{
									name : '用户推荐提成金',
									data : userRecommendCommissionData
								},
								{
									name : '商家推荐提成金',
									data : sellerRecommendCommissionData
								}
								]
					});
		}
	});
}
