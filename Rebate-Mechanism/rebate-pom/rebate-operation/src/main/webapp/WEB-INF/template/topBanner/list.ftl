<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.topBanner.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.topBanner")}</a> </li>
                <li class="active">${message("rebate.topBanner.list")}</li>
          </ol>
		  <div class="content-search accordion-group">
             <div class="accordion-heading" topBanner="tab" id="headingOne">
                  <a class="accordion-toggle" topBanner="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  查询条件
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" topBanner="tabpanel" aria-labelledby="headingOne">
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
							<a href="javascript:;" class="sort" name="bannerName">${message("rebate.topBanner.bannerName")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="linkUrl">${message("rebate.topBanner.linkUrl")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="bannerOrder">${message("rebate.topBanner.bannerOrder")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="title">${message("rebate.topBanner.title")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="isActive">${message("rebate.topBanner.isActive")}</a>
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
					[#list page.content as topBanner]
						<tr>
							<td>
								<input type="checkbox" name="ids" value="${topBanner.id}"/>
							</td>
							<td>
								${topBanner.bannerName}
							</td>
							<td>
								${topBanner.linkUrl}
							</td>
							<td>
								${topBanner.bannerOrder}
							</td>
							<td>
								${topBanner.title}
							</td>
							<td>
								${message(topBanner.isActive?string('rebate.common.true', 'rebate.common.false'))}
							</td>
							<td>
								<span title="${topBanner.createDate?string("yyyy-MM-dd HH:mm:ss")}">${topBanner.createDate}</span>
							</td>
							<td>
								<a href="edit.jhtml?id=${topBanner.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
							</td>
						</tr>
					[/#list]
				</tbody>
			</table>
		<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
</body>
</html>