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
	<form id="listForm" action="userBonusReport.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.reportManager")}</a> </li>
                <li class="active">${message("rebate.report.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
          </ol>
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
						<a href="javascript:;" class="sort" name="userName">${message("rebate.endUser.userName")}</a>
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
						${report.userId.userName}
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
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
</script>
</body>
</html>