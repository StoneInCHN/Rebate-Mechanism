<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerCategory.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.viewer-images li {
    float: inherit;
}
</style>
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerCategory")}</a> </li>
                <li class="active">${message("rebate.sellerCategory.list")}</li>
          </ol>
		  <div class="content-search accordion-group">
             <div class="accordion-heading" sellerCategory="tab" id="headingOne">
                  <a class="accordion-toggle" sellerCategory="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  查询条件
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" sellerCategory="tabpanel" aria-labelledby="headingOne">
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
							${message("rebate.sellerCategory.categoryName")}
						</th>
						<th>
							${message("rebate.sellerCategory.categoryPic")}
						</th>
						<th>
							${message("rebate.sellerCategory.categorOrder")}
						</th>
						<th>
							${message("rebate.sellerCategory.isActive")}
						</th>
						<th>
							${message("rebate.common.createDate")}
						</th>
						<th>
							${message("rebate.common.handle")}
						</th>
					</tr>
				</thead>
				<tbody>
					[#list page.content as sellerCategory]
						<tr>
							<td>
								<input type="checkbox" name="ids" value="${sellerCategory.id}"/>
							</td>
							<td>
								${sellerCategory.categoryName}
							</td>
							<td>
								[#if  sellerCategory.categoryPicUrl ??]
									<ul  class="viewer-images clearfix">
										<li><img class="img-list img-rounded img-lazy" data-original="${sellerCategory.categoryPicUrl}" alt="${message("rebate.sellerCategory.categoryPic")}"></li>
								    </ul>
								[#else]
									--
								[/#if]	
							</td>
							<td>
								${sellerCategory.categorOrder}
							</td>
							<td>
								${message(sellerCategory.isActive?string('rebate.common.true', 'rebate.common.false'))}
							</td>
							<td>
								<span title="${sellerCategory.createDate?string("yyyy-MM-dd HH:mm:ss")}">${sellerCategory.createDate}</span>
							</td>
							<td>
								<a href="edit.jhtml?id=${sellerCategory.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
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
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript">
$(function(){
	
	$('.viewer-images').viewer();
	//图片懒加载
	$('.img-lazy').lazyload();
	
})
</script>
</body>
</html>