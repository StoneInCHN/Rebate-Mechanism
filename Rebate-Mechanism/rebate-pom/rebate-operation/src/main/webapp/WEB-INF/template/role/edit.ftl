<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.role.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<style type="text/css">
.authorities label {
	min-width: 120px;
	_width: 120px;
	display: block;
	float: left;
	padding-right: 4px;
	_white-space: nowrap;
}
.authorities th a{
	color:#666;
	font-weight:bold;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $selectAll = $("#inputForm .selectAll");
	
	
	$selectAll.click(function() {
		var $this = $(this);
		var $thisCheckbox = $this.closest("tr").find(":checkbox");
		if ($thisCheckbox.filter(":checked").size() > 0) {
			$thisCheckbox.prop("checked", false);
		} else {
			$thisCheckbox.prop("checked", true);
		}
		return false;
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name:{
				required: true
			
			},
			authorities: "required",
			description:{
				maxlength:200
			}
		}
	});
	
});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.role")}</a> </li>
                <li><a href="#">${message("rebate.role.list")}</a></li>
                <li class="active">${message("rebate.role.edit")}</li>
          </ol>
		 <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${role.id}" />
						<input type="hidden" id="isSystem" value="${role.isSystem}" />
						<table class="input">
								<tr>
									<th>
										<span class="requiredField">*</span>${message("rebate.role.name")}:
									</th>
									<td>
										<input type="text" name="name" class="text" value="${role.name}" maxlength="200" />
									</td>
								</tr>
								<tr>
									<th>
										${message("rebate.role.description")}:
									</th>
									<td>
										<textarea  name="description" class="text" maxlength="200">${role.description}</textarea>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>
								</tr>
								<tr class="authorities">
									<th>
										<a href="javascript:;" class="selectAll" title="${message("rebate.role.selectAll")}">${message("rebate.role.systemGroup")}</a>
									</th>
									<td>
										<span class="fieldSet">
											<label>
												<input type="checkbox" name="authorities" value="rebate:account" [#if role.authorities?seq_contains("rebate:account")] checked="checked"[/#if] /><span>${message("rebate.account.settingGroup")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:admin" [#if role.authorities?seq_contains("rebate:admin")] checked="checked"[/#if] /><span>${message("rebate.main.admin")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:role" [#if role.authorities?seq_contains("rebate:role")] checked="checked"[/#if] /><span>${message("rebate.main.role")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:systemConfig" [#if role.authorities?seq_contains("rebate:systemConfig")] checked="checked"[/#if] /><span>${message("rebate.main.systemConfig")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:area" [#if role.authorities?seq_contains("rebate:area")] checked="checked"[/#if] /><span>${message("rebate.main.area")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:operationLog" [#if role.authorities?seq_contains("rebate:operationLog")] checked="checked"[/#if] /><span>${message("rebate.main.operation.log")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:topBanner" [#if role.authorities?seq_contains("rebate:topBanner")] checked="checked"[/#if] /><span>${message("rebate.main.topBanner")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:userHelp" [#if role.authorities?seq_contains("rebate:userHelp")] checked="checked"[/#if] /><span>${message("rebate.main.userHelp")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:holidayConfig" [#if role.authorities?seq_contains("rebate:holidayConfig")] checked="checked"[/#if] /><span>${message("rebate.main.holidayConfig")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:messageInfo" [#if role.authorities?seq_contains("rebate:messageInfo")] checked="checked"[/#if] /><span>${message("rebate.main.messageInfo")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:agentCommission" [#if role.authorities?seq_contains("rebate:agentCommission")] checked="checked"[/#if] /><span>${message("rebate.main.agentCommission")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:agent" [#if role.authorities?seq_contains("rebate:agent")] checked="checked"[/#if] /><span>${message("rebate.main.agent")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:sellerCategory" [#if role.authorities?seq_contains("rebate:sellerCategory")] checked="checked"[/#if] /><span>${message("rebate.main.sellerCategory")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:sellerApply" [#if role.authorities?seq_contains("rebate:sellerApply")] checked="checked"[/#if] /><span>${message("rebate.main.sellerApply")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:seller" [#if role.authorities?seq_contains("rebate:seller")] checked="checked"[/#if] /><span>${message("rebate.main.seller")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:sellerEvaluate" [#if role.authorities?seq_contains("rebate:sellerEvaluate")] checked="checked"[/#if] /><span>${message("rebate.main.sellerEvaluate")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:withdraw" [#if role.authorities?seq_contains("rebate:withdraw")] checked="checked"[/#if] /><span>${message("rebate.main.leScoreRecord.withdraw")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:endUser" [#if role.authorities?seq_contains("rebate:endUser")] checked="checked"[/#if] /><span>${message("rebate.endUser.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:scoreRecord" [#if role.authorities?seq_contains("rebate:scoreRecord")] checked="checked"[/#if] /><span>${message("rebate.endUser.score.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:leMindRecord" [#if role.authorities?seq_contains("rebate:leMindRecord")] checked="checked"[/#if] /><span>${message("rebate.endUser.leMind.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:leScoreRecord" [#if role.authorities?seq_contains("rebate:leScoreRecord")] checked="checked"[/#if] /><span>${message("rebate.endUser.leScore.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:leBeanRecord" [#if role.authorities?seq_contains("rebate:leBeanRecord")] checked="checked"[/#if] /><span>${message("rebate.endUser.leBean.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:order" [#if role.authorities?seq_contains("rebate:order")] checked="checked"[/#if] /><span>${message("rebate.order.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:sallerOrder" [#if role.authorities?seq_contains("rebate:sallerOrder")] checked="checked"[/#if] /><span>${message("rebate.sallerOrder.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:version" [#if role.authorities?seq_contains("rebate:version")] checked="checked"[/#if] /><span>${message("rebate.apkVersion.info")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:report" [#if role.authorities?seq_contains("rebate:report")] checked="checked"[/#if] /><span>${message("rebate.main.reportManager")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:bonusParam" [#if role.authorities?seq_contains("rebate:bonusParam")] checked="checked"[/#if] /><span>${message("rebate.main.bonusParam")}</span>
											</label>
										</span>
									</td>
								</tr>
								[#if role.isSystem]
									<tr>
										<th>
											&nbsp;
										</th>
										<td>
											<span class="tips">${message("rebate.role.editSystemNotAllowed")}</span>
										</td>
									</tr>
								[/#if]
								<tr>
									<th>
										&nbsp;
									</th>
									<td>
										<input type="submit" class="button" value="${message("rebate.common.submit")}"[#if role.isSystem] disabled="disabled"[/#if] />
										<input type="button" id="backBtn" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
									</td>
								</tr>
							</table>
					</form>
     </div>
</body>
</html>