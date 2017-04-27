<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.operationLog.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.operationLog")}</a> </li>
                <li class="active">${message("rebate.operationLog.list")}(${message("rebate.common.page.totalPages",page.total)})</li>
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
									${message("rebate.operationLog.operator")}:
								</th>
								<td>
									<input type="text" name="operator" class="text" value="${operator}"maxlength="20" />
								</td>
								<th>
									${message("rebate.common.beginDate")}:
								</th>
								<td>
									<input type="text" id="beginDate" name="beginDate" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" readonly [#if beginDate??]value="${beginDate?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th>
									${message("rebate.common.endDate")}:
								</th>
								<td>
									<input type="text" id="endDate" name="endDate" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" readonly [#if endDate??]value="${endDate?string("yyyy-MM-dd")}" [/#if] />
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
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>${message("rebate.common.refresh")}</span></a>
         </div>
         <div>
        <table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<th>
						<a href="javascript:;" class="sort" name="operation">${message("rebate.operationLog.operation")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="operator">${message("rebate.operationLog.operator")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="content">${message("rebate.operationLog.content")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="parameter">${message("rebate.operationLog.parameter")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="ip">${message("rebate.operationLog.ip")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.common.createDate")}</a>
					</th>
					<th>
						<span>${message("rebate.common.handle")}</span>
					</th>
				</tr>
			</thead>
			<tbody>
				[#list page.content as operationLog]
				<tr>
					<td>
						${message(operationLog.operation)}
					</td>
					<td>
						${message(operationLog.operator)}
					</td>
					<td>
						[#if  operationLog.content??]
							${operationLog.content}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  operationLog.parameter??]
							${operationLog.parameter}
						[#else]
							--
						[/#if]
						
					</td>
					<td>
						[#if  operationLog.ip??]
							${operationLog.ip}
						[#else]
							--
						[/#if]
						
					</td>
					<td>
						<span title="${operationLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${operationLog.createDate}</span>
					</td>
					<td>
						<a href="view.jhtml?id=${operationLog.id}" title="${message("csh.common.details")}"><i class="fa fa-eye"></i></a>
					</td>
				</tr>
				[/#list]
			</tbody>
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages total=page.total]
				[#include "/include/pagination.ftl"]
		[/@pagination]
	<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
</body>
</html>