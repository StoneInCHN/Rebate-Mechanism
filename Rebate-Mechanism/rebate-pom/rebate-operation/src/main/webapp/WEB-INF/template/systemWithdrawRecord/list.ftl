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
                <li><a ><i class="fa fa-user"></i> 平台提现</a> </li>
                <li class="active">平台提现列表(${message("rebate.common.page.totalPages", page.total)})</li>
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
									提现人
								</th>
								<td>
									<input type="text" name="operator" class="text" value="${operator}" maxlength="20" />
								</td>
								<th>
									提现人手机号
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${cellPhoneNum}" maxlength="20" />
								</td>
								<th>
									提现状态
								</th>
								<td>
									<select  name="withdrawStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if status == "PROCESSING"] selected="selected" [/#if] value="PROCESSING">处理中</option>
										<option [#if status == "SUCCESS"] selected="selected" [/#if] value="SUCCESS">处理成功</option>
										<option [#if status == "FAILED"] selected="selected" [/#if] value="FAILED"> 处理失败</option>
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
									银行卡号
								</th>
								<td>
									<input type="text" name="cardNum" class="text" value="${cardNum}" maxlength="20" />
								</td>
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
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
 		 	  <a  id="addButton" class="btn btn-default" ><i class="fa fa-plus"></i><span>添加提现</span></a>
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <div>
        <table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<th>
						<span>提现人</span>
					</th>
					<th>
						<span>提现人手机号</span>
					</th>	
					<th>
						<span>提现金额</span>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="withdrawStatus">提现状态</a>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.isWithdraw")}</span>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.handlingChange")}</span>
					</th>	
					<th>
						<span>${message("rebate.leScoreRecord.withdraw.withdrawMsg")}</span>
					</th>		
					<th>
						<span>银行卡号</span>
					</th>	
					<th>
						<span>交易批次号</span>
					</th>	
					<th>
						<span>${message("rebate.leScoreRecord.remark")}</span>
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
				[#list page.content as record]
				<tr>
					<td>
						${record.operator}
					</td>
					<td>
						${record.cellPhoneNum}
					</td>
					<td>
						${record.amount }
					</td>
					<td>
						[#if  record.status =="PROCESSING"]
							<span class="label label-info">${message("rebate.leScoreRecord.withdraw.status.PROCESSING")}</span>
						[#elseif record.status =="SUCCESS"]
							<span class="label label-success">${message("rebate.leScoreRecord.withdraw.status.SUCCESS")}</span>
						[#elseif record.status =="FAILED"]
							<span class="label label-warning">${message("rebate.leScoreRecord.withdraw.status.FAILED")}</span>
						[#else]
							--
						[/#if]
					</td>	
					<td>
						[#if  record.isWithdraw ?? ]
							[#if  record.isWithdraw ]
								<span class="label label-success">${message("rebate.common.true")}</span>
							[#else]
								<span class="label label-danger">${message("rebate.common.false")}</span>
							[/#if]
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  record.handlingCharge??]
							${record.handlingCharge}
						[#else]
							--
						[/#if]
					</td>	
					<td>
						[#if  record.withdrawMsg??]
						<span data-toggle="tooltip" data-placement="left" title="${record.withdrawMsg}">${record.withdrawMsg}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  record.cardNum??]
							${record.cardNum}
						[#else]
							--
						[/#if]
					</td>	
					<td>
						[#if  record.reqSn??]
							${record.reqSn}
						[#else]
							--
						[/#if]
					</td>	
					<td>
						[#if  record.remark??]
						<span data-toggle="tooltip" data-placement="left" title="${leScoreRecord.remark}">${leScoreRecord.remark}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						<span title="${record.createDate?string("yyyy-MM-dd HH:mm:ss")}">${record.createDate}</span>
					</td>
					<td>
							<a href="details.jhtml?id=${record.id}" title="${message("csh.common.details")}"><i class="fa fa-eye"></i></a>
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