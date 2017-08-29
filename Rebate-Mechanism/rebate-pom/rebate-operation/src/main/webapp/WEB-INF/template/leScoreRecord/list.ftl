<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.leScoreRecord.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.leScoreRecord")}</a> </li>
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.leScoreRecord.withdraw")}</a> </li>
                <li class="active">${message("rebate.leScoreRecord.withdraw.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.leScoreRecord.endUser.userName")}
								</th>
								<td>
									<input type="text" name="userName" class="text" value="${userName}"maxlength="20" />
								</td>
								<th>
									${message("rebate.leScoreRecord.endUser.cellPhoneNum")}
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${cellPhoneNum}"maxlength="20" />
								</td>
								<th>
									${message("rebate.leScoreRecord.withdrawStatus")}:
								</th>
								<td>
									<select  name="withdrawStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if withdrawStatus == "AUDIT_WAITING"] selected="selected" [/#if] value="AUDIT_WAITING">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_WAITING")}</option>
										<option [#if withdrawStatus == "AUDIT_PASSED"] selected="selected" [/#if] value="AUDIT_PASSED">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_PASSED")}</option>
										<option [#if withdrawStatus == "AUDIT_FAILED"] selected="selected" [/#if] value="AUDIT_FAILED">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_FAILED")}</option>
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
							<tr>
								<th>
									${message("rebate.common.beginDate")}:
								</th>
								<td>
									<input type="text" id="beginDate" name="beginDate" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" readonly [#if beginDate??]value="${beginDate?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th>
									${message("rebate.common.endDate")}:
								</th>
								<td>
									<input type="text" id="endDate" name="endDate" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" readonly [#if endDate??]value="${endDate?string("yyyy-MM-dd")}" [/#if] />
								</td>
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
              <a  id="withdrawButton" class="btn btn-default disabled" > <i class="fa fa-money"></i><span>提现</span></a>
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
						<span>${message("rebate.leScoreRecord.endUser.userName")}</span>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.endUser.cellPhoneNum")}</span>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="amount">${message("rebate.leScoreRecord.amount")}</a>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.endUser.role")}</span>
					</th>					
					<th>
						<a href="javascript:;" class="sort" name="withdrawStatus">${message("rebate.leScoreRecord.withdrawStatus")}</a>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.isWithdraw")}</span>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.status")}</span>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.handlingChange")}</span>
					</th>	
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.withdrawMsg")}</span>
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
				[#list page.content as leScoreRecord]
				<tr>
						<td>
							<input type="checkbox"  name="ids" value="${leScoreRecord.id}" [#if  leScoreRecord.withdrawStatus != "AUDIT_PASSED" || leScoreRecord.isWithdraw || leScoreRecord.status != null ] disabled="disabled" [/#if]/>
						</td>
					<td>
						[#if  leScoreRecord.endUser??]
							${leScoreRecord.endUser.nickName}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  leScoreRecord.endUser??]
							${leScoreRecord.endUser.cellPhoneNum}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  leScoreRecord.amount??]
							${leScoreRecord.amount * -1}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if leScoreRecord.endUser??]
					    	[#if leScoreRecord.endUser.seller != null]
					    		${message("rebate.endUser.seller")}&nbsp;
					    	[/#if]	
					    	[#if leScoreRecord.endUser.agent != null]
					    		${message("rebate.endUser.agent")}&nbsp;
					    	[/#if]	
					    	[#if leScoreRecord.endUser.isSalesman == true]	
					    		${message("rebate.endUser.salesman")}&nbsp;
					    	[/#if]	
					    	[#if leScoreRecord.endUser.seller == null && leScoreRecord.endUser.agent == null && leScoreRecord.endUser.isSalesman == false]
					    		${message("rebate.endUser.normal")}&nbsp;
					    	[/#if]
						[#else]
							--
						[/#if]
					</td>	
					<td>
						[#if  leScoreRecord.withdrawStatus =="AUDIT_WAITING"]
							<span class="label label-info">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_WAITING")}</span>
						[#elseif leScoreRecord.withdrawStatus =="AUDIT_PASSED"]
							<span class="label label-success">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_PASSED")}</span>
						[#elseif leScoreRecord.withdrawStatus =="AUDIT_FAILED"]
							<span class="label label-warning">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_FAILED")}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  leScoreRecord.isWithdraw ?? ]
							[#if  leScoreRecord.isWithdraw ]
								<span class="label label-success">${message("rebate.common.true")}</span>
							[#else]
								<span class="label label-danger">${message("rebate.common.false")}</span>
							[/#if]
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  leScoreRecord.status =="PROCESSING"]
							<span class="label label-info">${message("rebate.leScoreRecord.withdraw.status.PROCESSING")}</span>
						[#elseif leScoreRecord.status =="SUCCESS"]
							<span class="label label-success">${message("rebate.leScoreRecord.withdraw.status.SUCCESS")}</span>
						[#elseif leScoreRecord.status =="FAILED"]
							<span class="label label-danger">${message("rebate.leScoreRecord.withdraw.status.FAILED")}</span>
						[#else]
							--
						[/#if]
					</td>					
					<td>
						[#if  leScoreRecord.handlingCharge??]
							${leScoreRecord.handlingCharge}
						[#else]
							--
						[/#if]
					</td>	
					<td>
						[#if  leScoreRecord.withdrawMsg??]
						<span data-toggle="tooltip" data-placement="left" title="${leScoreRecord.withdrawMsg}">${leScoreRecord.withdrawMsg}</span>
						[#else]
							--
						[/#if]
					</td>
					<!--
					<td>
						[#if  leScoreRecord.remark??]
						<span data-toggle="tooltip" data-placement="left" title="${leScoreRecord.remark}">${leScoreRecord.remark}</span>
						[#else]
							--
						[/#if]
					</td>
					-->
					<td>
						[#if leScoreRecord.paymentChannel??]
							[#if  leScoreRecord.paymentChannel =="ALLINPAY"]
								<span class="label label-info">${message("rebate.sellerClearingRecord.paymentChannel.ALLINPAY")}</span>
							[#elseif leScoreRecord.paymentChannel =="JIUPAI"]
								<span class="label label-primary">${message("rebate.sellerClearingRecord.paymentChannel.JIUPAI")}</span>
							[/#if]
						[#else]
							--
						[/#if]
					</td>
					<td>
						<span title="${leScoreRecord.createDate?string("yyyy-MM-dd HH:mm:ss")}">${leScoreRecord.createDate}</span>
					</td>
					<td>
						[#if  leScoreRecord.withdrawStatus =="AUDIT_WAITING"]
								<a href="edit.jhtml?id=${leScoreRecord.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						[#else]
								<a href="details.jhtml?id=${leScoreRecord.id}" title="${message("csh.common.details")}"><i class="fa fa-eye"></i></a>
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