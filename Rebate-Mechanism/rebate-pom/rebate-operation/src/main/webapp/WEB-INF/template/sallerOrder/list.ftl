<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sallerOrder.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.sallerOrder.info")}</a> </li>
                <li class="active">${message("rebate.sallerOrder.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.order.sn")}:
								</th>
								<td>
									<input type="text" name="sn" class="text" value="${sn}" maxlength="20" />
								</td>
								<th>
									${message("rebate.order.endUser.cellPhoneNum")}:
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${cellPhoneNum}" maxlength="20" />
								</td>
								<th>
									${message("rebate.order.endUser.nickName")}:
								</th>
								<td>
									<input type="text" name="endUserNickName" class="text" value="${endUserNickName}"  maxlength="50" />
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
									${message("rebate.order.sellerName")}:
								</th>
								<td>
									<input type="text" name="sellerName" class="text" value="${sellerName}"  maxlength="100" />
								</td>
								<th>
									${message("rebate.order.orderTime")}:
								</th>
								<td>
									<input type="text" id="orderDateFrom" name="orderDateFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'orderDateTo\')}'});" readonly [#if orderDateFrom??]value="${orderDateFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="orderDateTo" name="orderDateTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'orderDateFrom\')}'});" readonly [#if orderDateTo??]value="${orderDateTo?string("yyyy-MM-dd")}" [/#if]/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.status")}:
								</th>
								<td>
									<select  name="orderStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if orderStatus == "UNPAID"] selected="selected" [/#if] value="UNPAID">${message("rebate.orderStatus.UNPAID")}</option>
										<option [#if orderStatus == "PAID"] selected="selected" [/#if] value="PAID">${message("rebate.orderStatus.PAID")}</option>
										<option [#if orderStatus == "FINISHED"] selected="selected" [/#if] value="FINISHED">${message("rebate.orderStatus.FINISHED")}</option>
									</select>
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <!--<a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="lockedButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>-->
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
        <table id="listTable" class="table table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<!--<th class="check">
						<input type="checkbox" id="selectAll" />
					</th>-->
					<th>
						<a href="javascript:;" class="sort" name="sn">${message("rebate.order.sn")}</a>
					</th>
					<th>
						<a href="javascript:;" name="endUserCellPhoneNum">${message("rebate.order.endUser.cellPhoneNum")}</a>
					</th>
					<th>
						<a href="javascript:;" name="endUserNickName">${message("rebate.order.endUser.nickName")}</a>
					</th>
					<th>
						<a href="javascript:;" name="sellerId">${message("rebate.order.sellerId")}</a>
					</th>
					<th>
						<a href="javascript:;" name="sellerName">${message("rebate.order.sellerName")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.order.orderTime")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="paymentType">${message("rebate.order.paymentType")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="amount">${message("rebate.order.amount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="status">${message("rebate.order.status")}</a>
					</th>
					<th>
						<span>${message("rebate.common.handle")}</span>
					</th>
				</tr>
			</thead>
			<tbody>
				[#list page.content as order]
				<tr>
					<!--<td>
						<input type="checkbox"  name="ids" value="${order.id}" />
					</td>-->
					<td>
						<span title="${order.sn}">${order.sn}</sapn>
					</td>
					<td>
						${order.endUser.cellPhoneNum}
					</td>
					<td>
						${order.endUser.nickName}
					</td>
					<td>
						${order.seller.id}
					</td>
					<td>
						${order.seller.name}
					</td>
					<td>
						${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
					</td>
					<td>
						${order.paymentType}
					</td>
					<td>
						${order.amount}
					</td>
					<td>
						[#if  order.status =="UNPAID"]
							<span class="label label-danger">${message("rebate.orderStatus.UNPAID")}</span>
						[#elseif order.status =="PAID"]
							<span class="label label-warning">${message("rebate.orderStatus.PAID")}</span>
						[#elseif order.status =="FINISHED"]
							<span class="label label-success">${message("rebate.orderStatus.FINISHED")}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						<!--
						<a href="edit.jhtml?id=${order.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						-->
						<a href="details.jhtml?id=${order.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
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
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
</script>
</body>
</html>