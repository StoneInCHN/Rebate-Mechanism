<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.seller.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerManager")}</a> </li>
                <li class="active">${message("rebate.seller.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.seller.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" value="${name}"maxlength="20" />
								</td>
								<th>
									${message("rebate.seller.contactPerson")}:
								</th>
								<td>
									<input type="text" name="contactPerson" class="text" value="${contactPerson}" maxlength="200" />
								</td>
								<th>
									${message("rebate.seller.contactCellPhone")}:
								</th>
								<td>
									<input type="text" name="contactCellPhone" class="text" value="${contactCellPhone}" maxlength="200" />
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
									${message("rebate.seller.accountStatus")}:
								</th>
								<td>
									<select  name="accountStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if accountStatus == "ACTIVED"] selected="selected" [/#if] value="ACTIVED">${message("rebate.common.accountStatus.ACTIVED")}</option>
										<option [#if accountStatus == "LOCKED"] selected="selected" [/#if] value="LOCKED">${message("rebate.common.accountStatus.LOCKED")}</option>
									</select>
								</td>
								<th>
									${message("rebate.seller.sellerCategory")}:
								</th>
								<td>
									<select  name="sellerCategoryId">
										<option value="">${message("rebate.common.All")}</option>
										[#list sellerCategorys as sellerCategory]	
											<option [#if sellerCategory.id == sellerCategoryId] selected="selected" [/#if] value="${sellerCategory.id}">${sellerCategory.categoryName}</option>
										[/#list]
									</select>
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="lockedButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
        <table id="listTable" class="table table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<th class="check">
						<input type="checkbox" id="selectAll" />
					</th>
					<th>
						<a href="javascript:;" class="sort" name="name">${message("rebate.seller.name")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="sellerCategory">${message("rebate.seller.sellerCategory")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="address">${message("rebate.seller.address")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="contactPerson">${message("rebate.seller.contactPerson")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="contactCellPhone">${message("rebate.seller.contactCellPhone")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="area">${message("rebate.seller.area")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="storePhoto">${message("rebate.seller.storePhoto")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="discount">${message("rebate.seller.discount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.common.createDate")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="sellerStatus">${message("rebate.seller.applyStatus")}</a>
					</th>
					<th>
						<span>${message("rebate.common.handle")}</span>
					</th>
				</tr>
			</thead>
			<tbody>
				[#list page.content as seller]
				<tr>
					<td>
						<input type="checkbox"  name="ids" value="${seller.id}" />
					</td>
					<td>
						<span title="${seller.name}">${seller.name}</sapn>
					</td>
					<td>
						[#if  seller.sellerCategory??]
							${seller.sellerCategory.categoryName}
						[#else]
							--
						[/#if]
					</td>
					<td>
						<span title="${seller.address}">${seller.address}</sapn>
					</td>
					<td>
						${seller.contactPerson}
					</td>
					<td>
						${seller.contactCellPhone}
					</td>
					<td>
						${seller.area}
					</td>
					<td>
						[#if  seller.storePictureUrl ??]
							<ul  class="viewer-images clearfix">
								<li><img class="img-list img-rounded img-lazy" data-original="${seller.storePictureUrl}" alt="${message("rebate.seller.storePhoto")}"></li>
						    </ul>
						[#else]
							--
						[/#if]
					</td>
					<td>
						${seller.discount}
					</td>
					<td>
						<span title="${seller.createDate?string("yyyy-MM-dd HH:mm:ss")}">${seller.createDate}</span>
					</td>
					<td>
						[#if  seller.accountStatus =="ACTIVED"]
							<span class="label label-success">${message("rebate.common.accountStatus.ACTIVED")}</span>
						[#elseif seller.accountStatus =="LOCKED"]
							<span class="label label-danger">${message("rebate.common.accountStatus.LOCKED")}</span>
						[#elseif seller.accountStatus =="DELETE"]
							<span class="label label-warning">${message("rebate.common.accountStatus.DELETE")}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						<!--
						<a href="edit.jhtml?id=${seller.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						-->
						<a href="details.jhtml?id=${seller.id}" title="${message("csh.common.details")}"><i class="fa fa-eye"></i></a>
					</td>
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
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(function(){
	
	$('.viewer-images').viewer();
	//图片懒加载
	$('.img-lazy').lazyload();
	
})
</script>
</body>
</html>