<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.endUser.leMind.list")}</title>
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
	<form id="listForm" action="record.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.endUserManager")}</a> </li>
                <li class="active">${message("rebate.endUser.leMind.list")}(${message("rebate.common.page.totalPages", page.total)})</li>
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
								<!--<th>
									${message("rebate.endUser.leMind.status")}:
								</th>
								<td>
									<select  name="status">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if status == "ACITVE"] selected="selected" [/#if] value="ACITVE">${message("rebate.endUser.leMind.status.ACITVE")}</option>
										<option [#if status == "INACTIVE"] selected="selected" [/#if] value="INACTIVE">${message("rebate.endUser.leMind.status.INACTIVE")}</option>
									</select>
								</td>-->
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='record.jhtml'" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.record.time")}:
								</th>
								<td>
									<input type="text" id="recordTimeFrom" name="recordTimeFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'recordTimeTo\')}'});" readonly [#if recordTimeFrom??]value="${recordTimeFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="recordTimeTo" name="recordTimeTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'recordTimeFrom\')}'});" readonly [#if recordTimeTo??]value="${recordTimeTo?string("yyyy-MM-dd")}" [/#if]/>
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
						<a href="javascript:;" name="endUserCellPhoneNum">${message("rebate.endUser.mobile")}</a>
					</th>
					<th>
						<a href="javascript:;" name="endUserNickName">${message("rebate.endUser.nickName")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="amount">${message("rebate.endUser.leMind.amount")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="score">${message("rebate.endUser.leMind.score")}</a>
					</th>
					<!--<th>
						<a href="javascript:;" class="sort" name="totalBonus">${message("rebate.endUser.leMind.totalBonus")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="maxBonus">${message("rebate.endUser.leMind.maxBonus")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="status">${message("rebate.endUser.leMind.status")}</a>
					</th>-->
					<th>
						<a href="javascript:;" class="sort" name="userCurLeMind">${message("rebate.endUser.leMind.userCurLeMind")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="createDate">${message("rebate.endUser.record.time")}</a>
					</th>
					<th>
						<a href="javascript:;" name="createDate">${message("rebate.endUser.remark")}</a>
					</th>
					<!--<th>
						<span>${message("rebate.common.handle")}</span>
					</th>-->
				</tr>
			</thead>
			<tbody>
				[#list page.content as leMind]
				<tr>
					<!--<td>
						<input type="checkbox"  name="ids" value="${order.id}" />
					</td>-->
					<td>
						${leMind.endUser.cellPhoneNum}
					</td>
					<td>
						${leMind.endUser.nickName}
					</td>
					<td>
						${leMind.amount}
					</td>
					<td>
						${leMind.score}
					</td>
					<!--<td>
						${leMind.totalBonus}
					</td>
					<td>
						${leMind.maxBonus}
					</td>
					<td>
						${message("rebate.endUser.leMind.status.${leMind.status}")}
					</td>-->
					<td>
						${leMind.userCurLeMind}
					</td>
					<td>
						${leMind.createDate?string("yyyy-MM-dd HH:mm:ss")}
					</td>
					<td>
						<span title="${leMind.remark}">${abbreviate(leMind.remark,20, "...")}</span>
					</td>
					<!--
					<td>
						
						<a href="edit.jhtml?id=${order.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
						<a href="details.jhtml?id=${order.id}" title="${message("rebate.common.details")}"><i class="fa fa-eye"></i></a>
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