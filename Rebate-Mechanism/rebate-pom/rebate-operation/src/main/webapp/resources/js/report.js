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
										+ '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
										name : '乐豆',
										data : bonusLeScoreData
									},
									{
										name : '总消费金额',
										data : consumeTotalAmountData
									},
									{
										name : '最高分红乐豆',
										data : highBonusLeScoreData
									} ]
						});
			}
		});
	});
});