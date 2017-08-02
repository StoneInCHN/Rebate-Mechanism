<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerEvaluate.list")}</title>
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
            <li><a href="#">${message("rebate.main.sellerEvaluate")}</a></li>
            <li class="active">${message("rebate.sellerEvaluate.list")}(${message("rebate.common.page.totalPages",page.total)})</li>
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
									${message("rebate.sellerEvaluate.endUser")}:
								</th>
								<td>
									<input type="text" name="userName" class="text" value="${userName}"maxlength="20" />
								</td>
								<th>
									${message("rebate.sellerEvaluate.seller")}:
								</th>
								<td>
									<input type="text" name="sellerName" class="text" value="${sellerName}"maxlength="20" />
								</td>
								<th>
									${message("rebate.sellerEvaluate.score")}:
								</th>
								<td>
									<input type="number" name="score" class="text" value="${score}" min="1" max="5" />
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
              <a  id="activeButton" class="btn btn-default disabled"><i class="fa fa-plus"></i><span>启用</span></a>
              <a  id="inActiveButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <table id="listTable" class="table table-striped table-bordered">
		 		<thead>
					<tr>
						<th class="check">
							<input type="checkbox" id="selectAll" />
						</th>
						<th>
							${message("rebate.sellerEvaluate.endUser")}
						</th>
						<th>
							${message("rebate.sellerEvaluate.seller")}
						</th>
						<th>
							${message("rebate.sellerEvaluate.score")}
						</th>
						<th>
							${message("rebate.sellerEvaluate.content")}
						</th>
						<th>
							${message("rebate.sellerEvaluate.sellerReply")}
						</th>
						<th>
							${message("rebate.sellerEvaluate.status")}
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
					[#list page.content as sellerEvaluate]
					<tr>
						<td>
							<input type="checkbox"  name="ids" value="${sellerEvaluate.id}" />
						</td>
						<td>
							[#if  sellerEvaluate.endUser??]
								${sellerEvaluate.endUser.nickName}
							[#else]
								--
							[/#if]
						</td>
						<td>
							[#if  sellerEvaluate.seller??]
								${sellerEvaluate.seller.name}
							[#else]
								--
							[/#if]
						</td>
						<td>
							${sellerEvaluate.score}
						</td>
						<td>
							${sellerEvaluate.content}
						</td>
						<td>
							${sellerEvaluate.sellerReply}
						</td>
						<td>
							[#if  sellerEvaluate.status =="ACITVE"]
								${message("rebate.sellerEvaluate.status.ACITVE")}
							[#elseif sellerEvaluate.status =="INACTIVE"]
								${message("rebate.sellerEvaluate.status.INACTIVE")}
							[#else]
								--
							[/#if]
							</td>
						<td>
							<span title="${sellerEvaluate.createDate?string("yyyy-MM-dd HH:mm:ss")}">${sellerEvaluate.createDate}</span>
						</td>
						<td>
							<a href="details.jhtml?id=${sellerEvaluate.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
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