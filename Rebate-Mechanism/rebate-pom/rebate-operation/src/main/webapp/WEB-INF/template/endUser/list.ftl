<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.endUser.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.endUserManager")}</a> </li>
                <li class="active">${message("rebate.endUser.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
									${message("rebate.endUser.mobile")}:
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${cellPhoneNum}" maxlength="20" />
								</td>
								<th>
									${message("rebate.endUser.nickName")}:
								</th>
								<td>
									<input type="text" name="nickName" class="text" value="${nickName}"  maxlength="50" />
								</td>
								<th>
									${message("rebate.endUser.status")}:
								</th>
								<td>
									<select  name="accountStatus">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if accountStatus == "ACTIVED"] selected="selected" [/#if] value="ACTIVED">${message("rebate.common.accountStatus.ACTIVED")}</option>
										<option [#if accountStatus == "LOCKED"] selected="selected" [/#if] value="LOCKED">${message("rebate.common.accountStatus.LOCKED")}</option>
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
									${message("rebate.endUser.regDate")}:
								</th>
								<td>
									<input type="text" id="regDateFrom" name="regDateFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'regDateTo\')}'});" readonly [#if regDateFrom??]value="${regDateFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="regDateTo" name="regDateTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'regDateFrom\')}'});" readonly [#if regDateTo??]value="${regDateTo?string("yyyy-MM-dd")}" [/#if]/>
								</td>
								<th>
									${message("rebate.endUser.isSalesman")}:
								</th>
								<td>
									<select  name="isSalesman">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if isSalesman?? && isSalesman] selected="selected" [/#if] value="true">${message("rebate.common.true")}</option>
										<option [#if isSalesman?? && !isSalesman] selected="selected" [/#if] value="false">${message("rebate.common.false")}</option>
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
						<a href="javascript:;" class="sort" name="nickName">${message("rebate.endUser.nickName")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="cellPhoneNum">${message("rebate.endUser.mobile")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="area">${message("rebate.endUser.area")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="address">${message("rebate.endUser.address")}</a>
					</th>
					<th>
						<a href="javascript:;" name="agent">${message("rebate.endUser.agent")}</a>
					</th>
					<th>
						<a href="javascript:;" name="isBindWeChat">${message("rebate.endUser.isBindWeChat")}</a>
					</th>
					<th>
						<a href="javascript:;" name="isSalesman">${message("rebate.endUser.isSalesman")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.endUser.regDate")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="accountStatus">${message("rebate.endUser.status")}</a>
					</th>
					<th>
						<span>${message("rebate.common.handle")}</span>
					</th>
				</tr>
			</thead>
			<tbody>
				[#list page.content as endUser]
				<tr>
					<td>
						<input type="checkbox"  name="ids" value="${endUser.id}" />
					</td>
					<td>
						<span title="${endUser.nickName}">${endUser.nickName}</sapn>
					</td>
					<td>
						<span title="${endUser.cellPhoneNum}">${endUser.cellPhoneNum}</sapn>
					</td>
					<td>
						<span title="${endUser.area}">${endUser.area}</sapn>
					</td>
					<td>
						<span title="${endUser.address}">${endUser.address}</sapn>
					</td>
					
					<td>
						[#if endUser.agent??]
							[#if  endUser.agent.agencyLevel =="PROVINCE"]
							<span class="label label-success">${message("rebate.endUser.agent.agencyLevel.PROVINCE")}</span>
							[#elseif endUser.agent.agencyLevel =="CITY"]
								<span class="label label-danger">${message("rebate.endUser.agent.agencyLevel.CITY")}</span>
							[#elseif endUser.agent.agencyLevel =="COUNTY"]
								<span class="label label-warning">${message("rebate.endUser.agent.agencyLevel.COUNTY")}</span>
							[#else]
								--
							[/#if]
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if endUser.isBindWeChat == true]
							${message("rebate.common.true")}
						[#else]
							${message("rebate.common.false")}
						[/#if]
					</td>
					<td>
						[#if endUser.isSalesman == true]
							${message("rebate.common.true")}
						[#else]
							${message("rebate.common.false")}
						[/#if]
					</td>
					<td>
						<span title="${endUser.createDate?string("yyyy-MM-dd HH:mm:ss")}">${endUser.createDate}</span>
					</td>
					<td>
						[#if  endUser.accountStatus =="ACTIVED"]
							<span class="label label-success">${message("rebate.common.accountStatus.ACTIVED")}</span>
						[#elseif endUser.accountStatus =="LOCKED"]
							<span class="label label-danger">${message("rebate.common.accountStatus.LOCKED")}</span>
						[#elseif endUser.accountStatus =="DELETE"]
							<span class="label label-warning">${message("rebate.common.accountStatus.DELETE")}</span>
						[#else]
							--
						[/#if]
					</td>
					<td>
						<!--
						<a href="edit.jhtml?id=${seller.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						-->
						<a href="details.jhtml?id=${endUser.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
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