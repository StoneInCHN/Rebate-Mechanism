<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerApplication.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/list.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
<div class="mainbar">
				<div class="page-head">
					<div class="bread-crumb">
						<a ><i class="fa fa-user"></i> ${message("rebate.main.sellerApply")}</a> 
						<span class="divider">/</span> 
						<span  class="bread-current"><i class="fa fa-list"></i>${message("rebate.sellerApplication.list")}(${message("rebate.common.page.totalPages", page.total)})</span>
					</div>
					<div class="clearfix"></div>
				</div>
			
			<form id="listForm" action="list.jhtml" method="get">
				  <div class="container operation">
					<div class="row">
						  <div class="col-xs-9 col-md-9 col-lg-9">
						  		<ul class="nav">
									 <li class="pull-left">
										<div class="btn-group operationButton">
										  <button type="button" id="deleteButton" class="btn btn-default disabled"><i class="fa fa-trash-o"></i>&nbsp;&nbsp;${message("rebate.common.delete")}</button>
										</div>
										<div class="btn-group operationButton">
										  <button type="button" id="refreshButton" class="btn btn-default"><i class="fa fa-refresh"></i>&nbsp;&nbsp;${message("rebate.common.refresh")}</button>
										</div>
									</li>
									  <li role="presentation" class="dropdown pull-right">
										    <a id="pageSizeSelect" aria-expanded="false" role="button" aria-haspopup="true" data-toggle="dropdown" class="dropdown-toggle" href="#">
										      ${message("rebate.common.page.pageSize")} <span class="caret"></span>
										    </a>
										    <ul id="pageSizeOption" class="dropdown-menu" role="menu" aria-labelledby="pageSizeSelect">
										     	<li>
													<a href="javascript:;"[#if page.pageSize == 10] class="active"[/#if] val="10">10</a>
												</li>
												<li>
													<a href="javascript:;"[#if page.pageSize == 20] class="active"[/#if] val="20">20</a>
												</li>
												<li>
													<a href="javascript:;"[#if page.pageSize == 50] class="active"[/#if] val="50">50</a>
												</li>
												<li>
													<a href="javascript:;"[#if page.pageSize == 100] class="active"[/#if] val="100">100</a>
												</li>
										    </ul>
									  </li>
									</ul>
						  </div>
						  <div class="col-xs-3 col-md-3 col-lg-3">
						  		<div class="input-group">
								      <div class="input-group-btn">
								        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">${message("rebate.common.choose")} <span class="caret"></span></button>
								        <ul class="dropdown-menu menuWrap" id="searchPropertyOption" role="menu">
								          <li [#if page.searchProperty == "sellerName" || page.searchProperty ==null] selected="selected" class="active" [/#if] title="sellerName"><a href="#">${message("rebate.sellerApplication.sellerName")}</a></li>
								          <li [#if page.searchProperty == "email"] selected="selected" class="active" [/#if] title="email"><a href="#">${message("rebate.sellerApplication.email")}</a></li>
								          <li [#if page.searchProperty == "contactCellPhone"] selected="selected" class="active" [/#if] title="contactCellPhone"><a href="#">${message("rebate.sellerApplication.contactCellPhone")}</a></li>
								        </ul>
								      </div>
								      <input type="text" class="form-control" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
							    </div>
						  </div>
					</div>
				</div>
				
				<div class="matter">
					<div class="container">
						<div class="row">
			              <div class="col-md-12">
			                <div class="widget">
									 <div class="widget-head">
						                  <div class="pull-left"><i class="fa fa-list"></i>${message("rebate.main.sellerApply")}</div>
						                  <div class="clearfix"></div>
						              </div>
						              <div class="widget-content">
										<table id="listTable" class="table table-striped table-bordered table-hover">
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
														${sellerApplication.sellerName}
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
														${sellerApplication.address}
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
														[#if  sellerApplication.license??]
															<a href="${base}${sellerApplication.license}" target="1024"><img src="${base}${sellerApplication.license}"  style="max-width:100px;max-height:100px;padding:5px" alt="${message("rebate.sellerApplication.license")}"></a>
														[#else]
															--
														[/#if]			
													</td>
													<td>
														[#if  sellerApplication.storePhone ??]
															<a href="${base}${sellerApplication.storePhone}" target="1024"><img src="${base}${sellerApplication.storePhone}"  style="max-width:100px;max-height:100px;padding:5px" alt="${message("rebate.sellerApplication.storePhone")}"></a>
														[#else]
															--
														[/#if]
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
														[#if  tenantApply.applyStatus ??]
															${message("rebate.common.auditStatus."+sellerApplication.applyStatus)}
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
											</tbody>
											[/#list]
										</table>
										<div class="widget-foot">
					                       [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
												[#include "/include/pagination.ftl"]
										   [/@pagination]
				                   		 </div>
									</div>
								</div>
							</div>
						</div>
					 </div>
				</div>
			</form>
</div>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
</body>
</html>