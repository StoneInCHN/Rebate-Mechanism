<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.agent.edit")}</title>
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
<script type="text/javascript" src="${base}/resources/js/jquery.lSelect.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $areaId = $("#areaId");
	// 地区选择
	$areaId.lSelect({
		url: "${base}/console/common/area.jhtml"
	});
	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			areaId: {
				required: true
			},
			agencyLevel: {
				required: true
			}
		}
	});

});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.agent")}</a> </li>
                <li><a href="#">${message("rebate.agent.list")}</a></li>
                <li class="active">${message("rebate.agent.edit")}</li>
          </ol>
		 <form id="inputForm" action="update.jhtml" method="post">
			<input type="hidden" name="id" value="${agent.id}" />
			<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.agent.area")}:
								</th>
								<td>
									<input type="hidden" id="areaId"  name="areaId" value="${(agent.area.id)!}" treePath="${(agent.area.treePath)!}"/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.agent.endUser")}:
								</th>
								<td>
									<input type="text" id="endUser" name="endUser" value="${agent.endUser}" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.agent.agencyLevel")}:
								</th>
								<td>
									<select name="agencyLevel">
										<option [#if agent.agencyLevel == 'PROVINCE' ] selected="selected" [/#if] value="PROVINCE">${message("rebate.agent.agencyLevel.PROVINCE")}</option>
										<option [#if agent.agencyLevel == 'CITY' ] selected="selected" [/#if] value="CITY">${message("rebate.agent.agencyLevel.CITY")}</option>
										<option [#if agent.agencyLevel == 'COUNTY' ] selected="selected" [/#if] value="COUNTY">${message("rebate.agent.agencyLevel.COUNTY")}</option>
									</select>
								</td>
							</tr>
						</table>
						<table class="input">
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("rebate.common.submit")}" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>           
						
		</form>
     </div>
</body>
</html>