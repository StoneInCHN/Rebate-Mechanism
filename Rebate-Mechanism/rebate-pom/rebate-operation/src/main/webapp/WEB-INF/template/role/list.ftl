<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.role.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.role")}</a> </li>
                <li class="active">${message("rebate.role.list")}</li>
          </ol>
		  <div class="content-search accordion-group">
             <div class="accordion-heading" role="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  查询条件
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="accordion-inner">
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <a  id="addButton" class="btn btn-default"><i class="fa fa-plus"></i><span>添加</span></a>
              <a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <div>
	        <table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
				<thead>
					<tr>
						<th class="check">
							<input type="checkbox" id="selectAll" />
						</th>
						<th>
							<a href="javascript:;" class="sort" name="name">${message("rebate.role.name")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="isSystem">${message("rebate.role.isSystem")}</a>
						</th>
						<th>
							<span>${message("rebate.role.description")}</span>
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
					[#list page.content as role]
						<tr>
							<td>
								<input type="checkbox" name="ids"[#if role.isSystem] title="${message("rebate.role.deleteSystemNotAllowed")}" disabled="disabled"[#else] value="${role.id}"[/#if] />
							</td>
							<td>
								${role.name}
							</td>
							<td>
								${message(role.isSystem?string('rebate.common.true', 'rebate.common.false'))}
							</td>
							<td>
								[#if role.description??]
									<span title="${role.description}">${abbreviate(role.description, 50, "...")}</span>
								[/#if]
							</td>
							<td>
								<span title="${role.createDate?string("yyyy-MM-dd HH:mm:ss")}">${role.createDate}</span>
							</td>
							<td>
								<a href="edit.jhtml?id=${role.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
							</td>
						</tr>
					[/#list]
				</tbody>
			</table>
		<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
</body>
</html>