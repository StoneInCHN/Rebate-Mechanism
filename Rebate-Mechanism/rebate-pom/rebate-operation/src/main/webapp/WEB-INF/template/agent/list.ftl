<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.agent.list")}</title>
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
            <li><a href="#">${message("rebate.main.agent")}</a></li>
            <li class="active">${message("rebate.agent.list")}(${message("rebate.common.page.totalPages",page.total)})</li>
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
									${message("rebate.agent.area")}:
								</th>
								<td>
									<input type="text" name="areaName" class="text" value="${areaName}"maxlength="20" />
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
              <a  id="addButton" class="btn btn-default"><i class="fa fa-plus"></i><span>添加</span></a>
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
							${message("rebate.agent.area")}
						</th>
						<th>
							${message("rebate.agent.endUser")}
						</th>
						<th>
							${message("rebate.endUser.mobile")}
						</th>
						<th>
							<a href="javascript:;" class="sort" name="agencyLevel">${message("rebate.agent.agencyLevel")}</a>
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
					[#list page.content as agent]
					<tr>
						<td>
							<input type="checkbox"  name="ids" value="${agent.id}" />
						</td>
						<td>
							[#if  agent.area??]
								${agent.area.fullName}
							[#else]
								--
							[/#if]	
						</td>
						<td>
							[#if  agent.endUser??]
								${agent.endUser.nickName}
							[#else]
								--
							[/#if]	
						</td>
						<td>
							[#if  agent.endUser??]
								${agent.endUser.cellPhoneNum}
							[#else]
								--
							[/#if]	
						</td>
						<td>
							${message("rebate.agent.agencyLevel." + agent.agencyLevel)}
						</td>
						<td>
							<span title="${agent.createDate?string("yyyy-MM-dd HH:mm:ss")}">${agent.createDate}</span>
						</td>
						<td>
							<a href="edit.jhtml?id=${agent.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
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

</body>
</html>