<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.admin.list")}</title>
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
            <li><a href="#">${message("rebate.main.admin")}</a></li>
            <li class="active">${message("rebate.admin.list")}(${message("rebate.common.page.totalPages",page.total)})</li>
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
									${message("rebate.admin.username")}:
								</th>
								<td>
									<input type="text" name="username" class="text" value="${username}"maxlength="20" />
								</td>
								<th>
									${message("rebate.admin.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" value="${name}" maxlength="200" />
								</td>
								<th>
									${message("rebate.admin.email")}:
								</th>
								<td>
									<input type="text" name="email" class="text" value="${email}" maxlength="200" />
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
									${message("rebate.admin.loginDate")}:
								</th>
								<td>
									<input type="text" name="loginDate" class="text Wdate" onfocus="WdatePicker();" readonly value="[#if loginDate??]${loginDate?string("yyyy-MM-dd")}[/#if]" />
								</td>
								<th>
									${message("rebate.admin.adminStatus")}:
								</th>
								<td>
									<select  name="adminStatus">
										<option value="">${message("rebate.admin.adminStatus")}</option>
										<option value="actived">${message("rebate.admin.adminStatus.actived")}</option>
										<option value="locked">${message("rebate.admin.adminStatus.locked")}</option>
									</select>
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
							<a href="javascript:;" class="sort" name="username">${message("rebate.admin.username")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="email">${message("rebate.admin.email")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="name">${message("rebate.admin.name")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="loginDate">${message("rebate.admin.loginDate")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="loginIp">${message("rebate.admin.loginIp")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="createDate">${message("rebate.common.createDate")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="adminStatus">${message("rebate.admin.adminStatus")}</a>
						</th>
						<th>
							<span>${message("rebate.common.handle")}</span>
						</th>
					</tr>
				</thead>
				<tbody>
					[#list page.content as admin]
					<tr>
						<td>
							<input type="checkbox"  name="ids" value="${admin.id}" />
						</td>
						<td>
							${admin.username}
						</td>
						<td>
							${admin.email}
						</td>
						<td>
							${admin.name}
						</td>
						<td>
							[#if admin.loginDate??]
								<span title="${admin.loginDate?string("yyyy-MM-dd HH:mm:ss")}">${admin.loginDate}</span>
							[#else]
								-
							[/#if]
						</td>
						<td>
							${(admin.loginIp)!"-"}
						</td>
						<td>
							<span title="${admin.createDate?string("yyyy-MM-dd HH:mm:ss")}">${admin.createDate}</span>
						</td>
						<td>
							${message("rebate.admin.adminStatus."+admin.adminStatus)}
						</td>
						<td>
							<a href="edit.jhtml?id=${admin.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
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
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
</body>
</html>