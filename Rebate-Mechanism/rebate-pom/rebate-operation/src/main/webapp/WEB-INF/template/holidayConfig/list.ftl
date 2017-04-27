<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.holidayConfig.list")}</title>
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
            <li><a href="#">${message("rebate.main.holidayConfig")}</a></li>
            <li class="active">${message("rebate.holidayConfig.list")}(${message("rebate.common.page.totalPages",page.total)})</li>
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
									${message("rebate.holidayConfig.name")}:
								</th>
								<td>
									<input type="text" name="holidayName" class="text" value="${holidayName}" maxlength="30" />
								</td>
								<th>
									${message("rebate.common.status")}:
								</th>
								<td>
									<select  name="status">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if status == "ACITVE"] selected="selected" [/#if] value="ACITVE">${message("rebate.holidayConfig.status.ACITVE")}</option>
										<option [#if status == "INACTIVE"] selected="selected" [/#if] value="INACTIVE">${message("rebate.holidayConfig.status.INACTIVE")}</option>
									</select>
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.holidayConfig.time")}:
								</th>
								<td>
									<input type="text" id="startDate" name="startDate" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" readonly [#if startDate??]value="${startDate?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="endDate" name="endDate" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'startDate\')}'});" readonly [#if endDate??]value="${endDate?string("yyyy-MM-dd")}" [/#if]/>
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <a  id="addButton" class="btn btn-default"><i class="fa fa-plus"></i><span>添加</span></a>
              <a  id="lockedButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>
              <a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <table id="listTable" class="table table-striped table-bordered">
		 		<thead>
					<tr>
						<th class="check">
							<input type="checkbox" id="selectAll" />
						</th>
						<th>
							<a href="javascript:;" class="sort" name="holidayName">${message("rebate.holidayConfig.name")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="status">${message("rebate.common.status")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="startDate">${message("rebate.holidayConfig.startDate")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="endDate">${message("rebate.holidayConfig.endDate")}</a>
						</th>
						<th>
							<span>${message("rebate.common.handle")}</span>
						</th>
					</tr>
				</thead>
				<tbody>
					[#list page.content as holidayConfig]
					<tr>
						<td>
							<input type="checkbox"  name="ids" value="${holidayConfig.id}" />
						</td>
						<td>
							${holidayConfig.holidayName}
						</td>
						<td>
							[#if  holidayConfig.status =="ACITVE"]
							<span class="label label-success">${message("rebate.holidayConfig.status.ACITVE")}</span>
							[#elseif holidayConfig.status =="INACTIVE"]
								<span class="label label-danger">${message("rebate.holidayConfig.status.INACTIVE")}</span>
							[#else]
								--
							[/#if]
						</td>
						<td>
							${holidayConfig.startDate}
						</td>
						<td>
							${holidayConfig.endDate}
						</td>
						<td>
							<a href="edit.jhtml?id=${holidayConfig.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						</td>
					</tr>
					[/#list]
				</tbody>
				</table>
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages total=page.total]
					[#include "/include/pagination.ftl"]
				[/@pagination]
	</form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>

</body>
</html>