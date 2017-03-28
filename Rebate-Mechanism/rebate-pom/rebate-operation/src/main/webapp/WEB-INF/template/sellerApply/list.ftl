<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerApplication.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerApply")}</a> </li>
                <li class="active">${message("rebate.sellerApplication.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.sellerApplication.sellerName")}:
								</th>
								<td>
									<input type="text" name="sellerName" class="text" value="${sellerName}"maxlength="20" />
								</td>
								<th>
									${message("rebate.sellerApplication.contactPerson")}:
								</th>
								<td>
									<input type="text" name="contactPerson" class="text" value="${contactPerson}" maxlength="200" />
								</td>
								<th>
									${message("rebate.sellerApplication.contactCellPhone")}:
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
									${message("rebate.sellerApplication.createDate")}:
								</th>
								<td>
									<input type="text" id="applyFromDate" name="applyFromDate" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'applyToDate\')}'});" readonly [#if applyFromDate??]value="${applyFromDate?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									--
								</th>
								<td>
									<input type="text" id="applyToDate" name="applyToDate" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'applyFromDate\')}'});" readonly [#if applyToDate??]value="${applyToDate?string("yyyy-MM-dd")}" [/#if]/>
								</td>
								<th>
									${message("rebate.sellerApplication.applyStatus")}:
								</th>
								<td>
									<select  name="applyStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if applyStatus == "AUDIT_WAITING"] selected="selected" [/#if] value="AUDIT_WAITING">${message("rebate.common.auditStatus.AUDIT_WAITING")}</option>
										<option [#if applyStatus == "AUDIT_PASSED"] selected="selected" [/#if] value="AUDIT_PASSED">${message("rebate.common.auditStatus.AUDIT_PASSED")}</option>
										<option [#if applyStatus == "AUDIT_FAILED"] selected="selected" [/#if] value="AUDIT_FAILED">${message("rebate.common.auditStatus.AUDIT_FAILED")}</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.sellerCategory")}:
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
						<a href="javascript:;" class="sort" name="sellerName">${message("rebate.sellerApplication.sellerName")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="sellerCategory">${message("rebate.sellerApplication.sellerCategory")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="email">${message("rebate.sellerApplication.email")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="address">${message("rebate.sellerApplication.address")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="contactPerson">${message("rebate.sellerApplication.contactPerson")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="contactCellPhone">${message("rebate.sellerApplication.contactCellPhone")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="area">${message("rebate.sellerApplication.area")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="license">${message("rebate.sellerApplication.license")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="storePhoto">${message("rebate.sellerApplication.storePhoto")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="storePhone">${message("rebate.sellerApplication.storePhone")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="latitude">${message("rebate.sellerApplication.latitude")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="longitude">${message("rebate.sellerApplication.longitude")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.common.createDate")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="sellerApplicationStatus">${message("rebate.sellerApplication.applyStatus")}</a>
					</th>
					<th>
						<span>${message("rebate.common.handle")}</span>
					</th>
				</tr>
			</thead>
			<tbody>
				[#list page.content as sellerApplication]
				<tr>
					<td>
						<input type="checkbox"  name="ids" value="${sellerApplication.id}" />
					</td>
					<td>
						<span title="${sellerApplication.sellerName}">${sellerApplication.sellerName}</sapn>
					</td>
					<td>
						[#if  sellerApplication.sellerCategory??]
							${sellerApplication.sellerCategory.categoryName}
						[#else]
							--
						[/#if]
					</td>
					<td>
						${sellerApplication.email}
					</td>
					<td>
						<span title="${sellerApplication.address}">${sellerApplication.address}</sapn>
					</td>
					<td>
						${sellerApplication.contactPerson}
					</td>
					<td>
						${sellerApplication.contactCellPhone}
					</td>
					<td>
						${sellerApplication.area}
					</td>
					<td>
						[#if  sellerApplication.licenseImgUrl??]
							<ul  class="viewer-images clearfix">
								<li><img class="img-list img-rounded img-lazy" data-original="${sellerApplication.licenseImgUrl}" alt="${message("rebate.sellerApplication.license")}"></li>
						    </ul>
						[#else]
							--
						[/#if]			
					</td>
					<td>
						[#if  sellerApplication.storePhoto ??]
							<ul  class="viewer-images clearfix">
								<li><img class="img-list img-rounded img-lazy" data-original="${sellerApplication.storePhoto}" alt="${message("rebate.sellerApplication.storePhoto")}"></li>
						    </ul>
						[#else]
							--
						[/#if]
					</td>
					<td>
						${sellerApplication.storePhone}
					</td>
					<td>
						${sellerApplication.latitude}
					</td>
					<td>
						${sellerApplication.longitude}
					</td>
					<td>
						<span title="${sellerApplication.createDate?string("yyyy-MM-dd HH:mm:ss")}">${sellerApplication.createDate}</span>
					</td>
					<td>
						[#if  sellerApplication.applyStatus =="AUDIT_WAITING"]
							<span class="label label-info">${message("rebate.common.auditStatus.AUDIT_WAITING")}</span>
						[#elseif sellerApplication.applyStatus =="AUDIT_PASSED"]
							<span class="label label-success">${message("rebate.common.auditStatus.AUDIT_PASSED")}</span>
						[#elseif sellerApplication.applyStatus =="AUDIT_FAILED"]
							<span class="label label-warning">${message("rebate.common.auditStatus.AUDIT_FAILED")}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  sellerApplication.applyStatus =="AUDIT_WAITING"]
								<a href="edit.jhtml?id=${sellerApplication.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						[#else]
								<a href="details.jhtml?id=${sellerApplication.id}" title="${message("csh.common.details")}"><i class="fa fa-eye"></i></a>
						[/#if]
					</td>
				</tr>
				[/#list]
			</tbody>
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/include/pagination.ftl"]
		[/@pagination]
	<div> 
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