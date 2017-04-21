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
                <li class="active">${message("rebate.leScoreRecord.withdraw.list")}</li>
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
									${message("rebate.leScoreRecord.seller")}:
								</th>
								<td>
									<input type="text" name="sellerName" class="text" value="${sellerName}"maxlength="20" />
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
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <div>
        <table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>

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
						<a href="javascript:;" class="sort" name="withdrawStatus">${message("rebate.leScoreRecord.withdrawStatus")}</a>
					</th>
					<th>
						<span>${message("rebate.leScoreRecord.remark")}</span>
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
						[#if  leScoreRecord.endUser??]
							${leScoreRecord.endUser.userName}
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
							${leScoreRecord.amount}
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
						[#if  leScoreRecord.remark??]
						<span data-toggle="tooltip" data-placement="left" title="${leScoreRecord.remark}">${leScoreRecord.remark}</span>
						[#else]
							--
						[/#if]
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
	<div> 
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