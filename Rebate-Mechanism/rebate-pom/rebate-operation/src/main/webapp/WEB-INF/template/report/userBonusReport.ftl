<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.userBonusReport.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<ol class="breadcrumb">
        <li><a ><i class="fa fa-user"></i> ${message("rebate.main.reportManager")}</a> </li>
        <li class="active">${message("rebate.report.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
  </ol>
	<ul class="nav nav-tabs" role="tablist">
	    <li role="presentation" class="active"><a href="#user_table_list" aria-controls="setting" role="tab" data-toggle="tab">列表数据</a></li>
	    <li role="presentation"><a href="#user_charts" aria-controls="paymenttype" role="tab" data-toggle="tab">图表数据</a></li>
	  </ul>
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="user_table_list">
	<form id="listForm" action="userBonusReport.jhtml" method="get">
          
		  <div class="content-search accordion-group">
             <div class="accordion-heading" role="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  查询条件
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="accordion-inner">
						<table class="queryFiled">
							<tr>
								<th>
									${message("rebate.endUser.mobile")}
								</th>
								<td>
									<input type="text" name="mobile" class="text" value="${mobile}"maxlength="20"/>
								</td>
								<th>
									${message("rebate.endUser.nickName")}
								</th>
								<td>
									<input type="text" name="nickName" class="text" value="${nickName}"maxlength="20"/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.report.reportDate")}:
								</th>
								<td>
									<input type="text" id="reportDateFrom" name="reportDateFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'reportDateTo\')}'});" readonly [#if reportDateFrom??]value="${reportDateFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="reportDateTo" name="reportDateTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'reportDateFrom\')}'});" readonly [#if reportDateTo??]value="${reportDateTo?string("yyyy-MM-dd")}" [/#if]/>
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='userBonusReport.jhtml'" />
								</td>
							</tr>
						</table>
	                  </div>
	             </div>
	         </div>
	         
 		 <div class="button-group">
              <!--<a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="lockedButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>-->
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
        <table id="listTable" class="table table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<!--<th class="check">
						<input type="checkbox" id="selectAll" />
					</th>-->
					<th>
						<a href="javascript:;"  name="mobile">${message("rebate.endUser.mobile")}</a>
					</th>
					<th>
						<a href="javascript:;"  name="nickName">${message("rebate.endUser.nickName")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="bonusLeScore">${message("rebate.userBonusReport.bonusLeScore")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="consumeTotalAmount">${message("rebate.userBonusReport.consumeTotalAmount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="highBonusLeScore">${message("rebate.userBonusReport.highBonusLeScore")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="reportDate">${message("rebate.report.reportDate")}</a>
					</th>
				</tr>
			</thead>
			<tbody>
				[#list page.content as report]
				<tr>
				<td>
						${report.userId.cellPhoneNum}
					</td>
					<td>
						${report.userId.nickName}
					</td>
					<td>
						${report.bonusLeScore}
					</td>
					<td>
						${report.consumeTotalAmount}
					</td>
					<td>
						${report.highBonusLeScore}
					</td>
					<td>
						${report.reportDate}
					</td>
				</tr>
				[/#list]
			</tbody>
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/include/pagination.ftl"]
		[/@pagination]
    </form>
   </div>
   <div role="tabpanel" class="tab-pane" id="user_charts">
   	<form id="charts_report_search_form">
		  <div class="content-search accordion-group">
             <div class="accordion-heading" role="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
                      	  查询条件
                  </a>
             </div>
             <div id="collapseTwo" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingTwo">
                  <div class="accordion-inner">
						<table class="queryFiled">
							<tr>
								<th>
									${message("rebate.endUser.mobile")}
								</th>
								<td>
									<input type="text" name="mobile" class="text" value="${mobile}"maxlength="20"/>
								</td>
								<th>
									${message("rebate.endUser.nickName")}
								</th>
								<td>
									<input type="text" name="nickName" class="text" value="${nickName}"maxlength="20"/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.report.reportDate")}:
								</th>
								<td>
									<input type="text" id="reportDateFrom" name="reportDateFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'reportDateTo\')}'});" readonly [#if reportDateFrom??]value="${reportDateFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="reportDateTo" name="reportDateTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'reportDateFrom\')}'});" readonly [#if reportDateTo??]value="${reportDateTo?string("yyyy-MM-dd")}" [/#if]/>
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input id="chart_report_search" type="button" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='userBonusReport.jhtml'" />
								</td>
							</tr>
						</table>
	                  </div>
	             </div>
	         </div>
	         
 		 <div class="button-group">
              <!--<a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="lockedButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>-->
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
   		<div id="userBonusReportDivId" style="height:400px;width:99%;"></div>
   		
   </div>
   </form>
 </div>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${base}/resources/js/report.js"></script>
<script type="text/javascript">
$(function () {
	  $('[data-toggle="tooltip"]').tooltip();
	})
</script>
</body>
</html>