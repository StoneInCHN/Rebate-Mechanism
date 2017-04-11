<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.userHelp.list")}</title>
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
            <li><a href="#">${message("rebate.main.userHelp")}</a></li>
            <li class="active">${message("rebate.userHelp.list")}(${message("rebate.common.page.totalPages",page.total)})</li>
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
									${message("rebate.userHelp.title")}:
								</th>
								<td>
									<input type="text" name="title" class="text" value="${title}"maxlength="20" />
								</td>
								<th>
									${message("rebate.userHelp.isEnabled")}:
								</th>
								<td>
									<select  name="isEnabled">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if isEnabled] checked="checked" [/#if] value="true">${message("rebate.common.true")}</option>
										<option [#if !isEnabled] checked="checked" [/#if] value="false">${message("rebate.common.false")}</option>
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
							<a href="javascript:;" class="sort" name="title">${message("rebate.userHelp.title")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="remark">${message("rebate.userHelp.remark")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="configOrder">${message("rebate.userHelp.configOrder")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="createDate">${message("rebate.common.createDate")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="isEnabled">${message("rebate.userHelp.isEnabled")}</a>
						</th>
						<th>
							<span>${message("rebate.common.handle")}</span>
						</th>
					</tr>
				</thead>
				<tbody>
					[#list page.content as userHelp]
					<tr>
						<td>
							<input type="checkbox"  name="ids" value="${userHelp.id}" />
						</td>
						<td>
							${userHelp.title}
						</td>
						<td>
							[#if userHelp.remark??]
								<span data-toggle="tooltip" data-placement="top" >${abbreviate(userHelp.remark, 50, "...")}</span>
							[#else]
								---	
							[/#if]
						</td>
						<td>
							${userHelp.configOrder}
						</td>
						<td>
							<span title="${userHelp.createDate?string("yyyy-MM-dd HH:mm:ss")}">${userHelp.createDate}</span>
						</td>
						<td>
							[#if  userHelp.isEnabled]
								<span class="label label-info">${message("rebate.common.true")}</span>
							[#elseif !userHelp.isEnabled]
								<span class="label label-success">${message("rebate.common.false")}</span>
							[#else]
								--
							[/#if]
						</td>
						<td>
							<a href="edit.jhtml?id=${userHelp.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
							<a href="details.jhtml?id=${userHelp.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
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
<script>
	$(function () {
  $('[data-toggle="tooltip"]').tooltip();
})
</script>
</body>
</html>