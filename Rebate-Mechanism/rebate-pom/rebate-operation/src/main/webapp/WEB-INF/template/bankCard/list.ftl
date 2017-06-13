<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.bankCard.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.bankCard")}</a> </li>
                <li class="active">${message("rebate.bankCard.list")}(银行卡列表(共<strong style="color:red">${page.total}</strong>条))</li>
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
									${message("rebate.bankCard.ownerName")}:
								</th>
								<td>
									<input type="text" name="ownerName" class="text" value="${ownerName}" maxlength="20" />
								</td>
								<th>
									${message("rebate.bankCard.cardNum")}:
								</th>
								<td>
									<input type="text" name="cardNum" class="text" value="${cardNum}" maxlength="20" />
								</td>
								<th>
									${message("rebate.bankCard.reservedMobile")}:
								</th>
								<td>
									<input type="text" name="reservedMobile" class="text" value="${reservedMobile}"  maxlength="50" />
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
									${message("rebate.bankCard.delStatus")}:
								</th>
								<td>
									<select  name="delStatus">
											[#if delStatus ??]
												<option value="">${message("rebate.common.All")}</option>
												<option [#if delStatus ] selected="selected" [/#if] value="true">${message("rebate.common.true")}</option>
												<option [#if !delStatus ] selected="selected" [/#if] value="false">${message("rebate.common.false")}</option>
											[#else] 
												<option  selected="selected" value="">${message("rebate.common.All")}</option>
												<option  value="true">${message("rebate.common.true")}</option>
												<option  value="false">${message("rebate.common.false")}</option>
											[/#if]
											
									</select>
								</td>
								<th>
									${message("rebate.bankCard.idCard")}:
								</th>
								<td>
									<input type="text" name="idCard" class="text" value="${idCard}"  maxlength="100" />
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
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.isDefault")}:
								</th>
								<td>
									<select  name="isDefault">
											[#if isDefault ??]
												<option value="">${message("rebate.common.All")}</option>
												<option [#if isDefault ] selected="selected" [/#if] value="true">${message("rebate.common.true")}</option>
												<option [#if !isDefault ] selected="selected" [/#if] value="false">${message("rebate.common.false")}</option>
											[#else] 
												<option  selected="selected" value="">${message("rebate.common.All")}</option>
												<option  value="true">${message("rebate.common.true")}</option>
												<option  value="false">${message("rebate.common.false")}</option>
											[/#if]
											
									</select>
								</td>
								<th>
									${message("rebate.bankCard.endUser")}:
								</th>
								<td>
									<input type="text" name="nickName" class="text" value="${nickName}"  maxlength="100" />
								</td>
								<th>
									${message("rebate.bankCard.cellPhoneNum")}:
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${cellPhoneNum}"  maxlength="100" />
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
						<a href="javascript:;" class="sort" name="ownerName">${message("rebate.bankCard.ownerName")}</a>
					</th>
					<th>
						<a href="javascript:;" name="cardNum">${message("rebate.bankCard.cardNum")}</a>
					</th>
					<th>
						<a href="javascript:;" name="bankName">${message("rebate.bankCard.bankName")}</a>
					</th>
					<th>
						<a href="javascript:;" name="cardType">${message("rebate.bankCard.cardType")}</a>
					</th>
					<th>
						${message("rebate.bankCard.endUser")}
					</th>
					<th>
						${message("rebate.bankCard.cellPhoneNum")}
					</th>
					<th>
						<a href="javascript:;" class="sort" name="idCard">${message("rebate.bankCard.idCard")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="isDefault">${message("rebate.bankCard.isDefault")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="delStatus">${message("rebate.bankCard.delStatus")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="reservedMobile">${message("rebate.bankCard.reservedMobile")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.common.createDate")}</a>
					</th>
					<!--
					<th>
						<span>${message("rebate.common.handle")}</span>
					</th>
					-->
				</tr>
			</thead>
			<tbody>
				[#list page.content as bankCard]
				<tr>
					<!--<td>
						<input type="checkbox"  name="ids" value="${bankCard.id}" />
					</td>-->
					<td>
						<span title="${bankCard.ownerName}">${bankCard.ownerName}</sapn>
					</td>
					<td>
						${bankCard.cardNum}
					</td>
					<td>
						${bankCard.bankName}
					</td>
					<td>
						${bankCard.cardType}
					</td>
					<td>
						[#if  bankCard.endUser]
							${bankCard.endUser.nickName}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  bankCard.endUser]
							${bankCard.endUser.cellPhoneNum}
						[#else]
							--
						[/#if]
					</td>
					<td>
						${bankCard.idCard}
					</td>
					<td>
						[#if  bankCard.isDefault]
							<span class="label label-success">${message("rebate.common.true")}</span>
						[#else]
							<span class="label label-danger">${message("rebate.common.false")}</span>
						[/#if]
					</td>
					<td>
						[#if  bankCard.delStatus]
							<span class="label label-success">${message("rebate.common.true")}</span>
						[#else]
							<span class="label label-info">${message("rebate.common.false")}</span>
						[/#if]
					</td>
					<td>
						${bankCard.reservedMobile}
					</td>
					<td>
						${bankCard.createDate?string("yyyy-MM-dd HH:mm:ss")}
					</td>
					<!--
					<td>
						<a href="details.jhtml?id=${bankCard.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
					</td>
					-->
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