<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.bonusParam.perDay")}</title>
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
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.bonusParam")}</a> </li>
                <li class="active">${message("rebate.bonusParam.perDay")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.bonusParam.time")}:
								</th>
								<td>
									<input type="text" id="reqStartTime" name="reqStartTime" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'reqEndTime\')}'});" readonly [#if reqStartTime??]value="${reqStartTime?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="reqEndTime" name="reqEndTime" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'reqStartTime\')}'});" readonly [#if reqEndTime??]value="${reqEndTime?string("yyyy-MM-dd")}" [/#if]/>
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <!--<a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>-->
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
        <table id="listTable" class="table table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<th>
						<a href="javascript:;" class="sort" name="bonusDate">${message("rebate.bonusParam.time")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="orderCount">${message("rebate.bonusParam.orderCount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="rebateTotalAmount">${message("rebate.bonusParam.rebateTotalAmount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="calValue">${message("rebate.bonusParam.calValue")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="avlLeMindCount">${message("rebate.bonusParam.avlLeMindCount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="bonusAmount">${message("rebate.bonusParam.bonusAmount")}</a>
					</th>
					<th>
						<a href="javascript:;" name="remark">${message("rebate.bonusParam.remark")}</a>
					</th>
					<!--<th>
						<span>${message("rebate.common.handle")}</span>
					</th>-->
				</tr>
			</thead>
			<tbody>
				[#list page.content as bonusParam]
				<tr>
					<td>
						${bonusParam.bonusDate}
					</td>
					<td>
						${bonusParam.orderCount}
					</td>
					<td>
						${bonusParam.rebateTotalAmount}
					</td>
					<td>
						${bonusParam.calValue}
					</td>
					<td>
						${bonusParam.avlLeMindCount}
					</td>
					<td>
						${bonusParam.bonusAmount}
					</td>
					<td>
						<span title="${bonusParam.remark}">${abbreviate(bonusParam.remark,50, "...")}</span>
					</td>
					<!--
					<td>
						<a href="edit.jhtml?id=${seller.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						<a href="details.jhtml?id=${endUser.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
					</td>
					-->
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