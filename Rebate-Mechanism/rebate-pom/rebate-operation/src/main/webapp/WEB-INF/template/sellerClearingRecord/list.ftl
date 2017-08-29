<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerClearingRecord.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerClearingRecord")}</a> </li>
                <li class="active">${message("rebate.sellerClearingRecord.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.sellerClearingRecord.sn")}:
								</th>
								<td>
									<input type="text" name="sn" class="text" value="${sn}" maxlength="20" />
								</td>
								<th>
									${message("rebate.sellerClearingRecord.clearingSn")}:
								</th>
								<td>
									<input type="text" name="clearingSn" class="text" value="${clearingSn}" maxlength="20" />
								</td>
								<th>
									${message("rebate.sellerClearingRecord.reqSn")}:
								</th>
								<td>
									<input type="text" name="reqSn" class="text" value="${reqSn}"  maxlength="50" />
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.clearingStatus")}:
								</th>
								<td>
									<select  name="clearingStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if clearingStatus == "PROCESSING"] selected="selected" [/#if] value="PROCESSING">${message("rebate.sellerClearingRecord.clearingStatus.PROCESSING")}</option>
										<option [#if clearingStatus == "SUCCESS"] selected="selected" [/#if] value="SUCCESS">${message("rebate.sellerClearingRecord.clearingStatus.SUCCESS")}</option>
										<option [#if clearingStatus == "FAILED"] selected="selected" [/#if] value="FAILED">${message("rebate.sellerClearingRecord.clearingStatus.FAILED")}</option>
									</select>
								</td>
								<th>
									${message("rebate.common.createDate")}:
								</th>
								<td>
									<input type="text" id="dateFrom" name="dateFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'dateTo\')}'});" readonly [#if dateFrom??]value="${dateFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="dateTo" name="dateTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'dateFrom\')}'});" readonly [#if dateTo??]value="${dateTo?string("yyyy-MM-dd")}" [/#if]/>
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.seller")}:
								</th>
								<td>
									<input type="text" name="sellerName" class="text" value="${sellerName}"  maxlength="100" />
								</td>
								<th>
									${message("rebate.sellerClearingRecord.endUser")}手机号:
								</th>
								<td>
									<input type="text" name="endUserCellPhone" class="text" value="${endUserCellPhone}"  maxlength="50" />
								</td>
								<th>
									是否有效:
								</th>
								<td>
									<select  name="valid">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if valid == 'true'] selected="selected" [/#if] value="true">有效</option>
										<option [#if valid == 'false'] selected="selected" [/#if] value="false">无效</option>
									</select>
								</td>								
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.paymentChannel")}:
								</th>
								<td>
									<select name="paymentChannel">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if paymentChannel == 'ALLINPAY'] selected="selected" [/#if] value="ALLINPAY">${message("rebate.sellerClearingRecord.paymentChannel.ALLINPAY")}</option>
										<option [#if paymentChannel == 'JIUPAI'] selected="selected" [/#if] value="JIUPAI">${message("rebate.sellerClearingRecord.paymentChannel.JIUPAI")}</option>
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
        <table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<th class="check">
						<input type="checkbox" id="selectAll" />
					</th>
					<th>
						${message("rebate.sellerClearingRecord.seller")}
					</th>
					<th>
						<a href="javascript:;" name="totalOrderAmount">${message("rebate.sellerClearingRecord.totalOrderAmount")}</a>
					</th>
					<th>
						<a href="javascript:;" name="amount">${message("rebate.sellerClearingRecord.amount")}</a>
					</th>

					<th>
						<a href="javascript:;" class="sort" name="handlingCharge">${message("rebate.sellerClearingRecord.handlingCharge")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="clearingStatus">${message("rebate.sellerClearingRecord.clearingStatus")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="bankCardId">${message("rebate.sellerClearingRecord.bankCardId")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="clearingSn">${message("rebate.sellerClearingRecord.clearingSn")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="sn">${message("rebate.sellerClearingRecord.sn")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="reqSn">${message("rebate.sellerClearingRecord.reqSn")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="valid">${message("rebate.sellerClearingRecord.valid")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="valid">${message("rebate.sellerClearingRecord.paymentChannel")}</a>
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
				[#list page.content as sellerClearingRecord]
				<tr [#if sellerClearingRecord.valid == null || sellerClearingRecord.valid == false ] class="greyColor" [/#if]/>
					<td>
						<input type="checkbox"  name="ids" value="${sellerClearingRecord.id}" [#if sellerClearingRecord.valid == null || sellerClearingRecord.valid == false ] disabled="disabled" [/#if]/>
					</td>
					<td>
						<span title="${sellerClearingRecord.seller.name}">${sellerClearingRecord.seller.name}</sapn>
					</td>
					<td>
						${sellerClearingRecord.totalOrderAmount}
					</td>					
					<td>
						${sellerClearingRecord.amount}
					</td>
					<td>
						${sellerClearingRecord.handlingCharge}
					</td>
					<td>
						[#if  sellerClearingRecord.clearingStatus =="PROCESSING"]
							<span class="label label-warning">${message("rebate.sellerClearingRecord.clearingStatus.PROCESSING")}</span>
						[#elseif sellerClearingRecord.clearingStatus =="SUCCESS"]
							<span class="label label-success">${message("rebate.sellerClearingRecord.clearingStatus.SUCCESS")}</span>
						[#elseif sellerClearingRecord.clearingStatus =="FAILED"]
							<span class="label label-danger">${message("rebate.sellerClearingRecord.clearingStatus.FAILED")}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						${sellerClearingRecord.bankCardId}
					</td>
					<td>
						<span title="${sellerClearingRecord.clearingSn}">${sellerClearingRecord.clearingSn}</sapn>
					</td>
					<td>
						<span title="${sellerClearingRecord.sn}">${sellerClearingRecord.sn}</sapn>
					</td>
					<td>
						<span data-toggle="tooltip" data-placement="left" title="${sellerClearingRecord.reqSn}">${sellerClearingRecord.reqSn}</sapn>
					</td>
					<td>
						[#if sellerClearingRecord.valid ??]
							[#if sellerClearingRecord.valid ] ${message("rebate.common.true")} [/#if]
							[#if !sellerClearingRecord.valid ] ${message("rebate.common.false")} [/#if] 
						[#else] 
							--		
						[/#if]
					</td>
					<td>
						[#if sellerClearingRecord.paymentChannel??]
							[#if  sellerClearingRecord.paymentChannel =="ALLINPAY"]
								<span class="label label-info">${message("rebate.sellerClearingRecord.paymentChannel.ALLINPAY")}</span>
							[#elseif sellerClearingRecord.paymentChannel =="JIUPAI"]
								<span class="label label-primary">${message("rebate.sellerClearingRecord.paymentChannel.JIUPAI")}</span>
							[/#if]
						[#else]
							--
						[/#if]
					</td>
					<td>
						${sellerClearingRecord.createDate?string("yyyy-MM-dd HH:mm:ss")}
					</td>
					<td>
						<a href="details.jhtml?id=${sellerClearingRecord.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
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
<script>
	$(function () {
  		$('[data-toggle="tooltip"]').tooltip();
	})
</script>
</body>
</html>