<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.systemConfig.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript">
$(function() {

	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			configValue: "required",
			configOrder: "required"
		}
	});

});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.systemConfig")}</a> </li>
                <li><a href="#">${message("rebate.systemConfig.list")}</a></li>
                <li class="active">${message("rebate.systemConfig.edit")}</li>
          </ol>
		  <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${systemConfig.id}" />
						<table class="input">
							<tr>
								<th>
									${message("rebate.systemConfig.configKey")}:
								</th>
								<td>
									${systemConfig.configKey}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.systemConfig.configValue")}:
								</th>
								<td>
									<input type="text" name="configValue" value="${systemConfig.configValue}" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.systemConfig.configOrder")}:
								</th>
								<td>
									<input type="text" name="configOrder" value="${systemConfig.configOrder}" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.systemConfig.isEnabled")}:
								</th>
								<td>
									<select name="isEnabled">
										<option [#if systemConfig.isEnabled] selected="selected" [/#if] value="true">${message("rebate.systemConfig.isEnabled.true")}</option>
										<option [#if !systemConfig.isEnabled] selected="selected" [/#if] value="false">${message("rebate.systemConfig.isEnabled.false")}</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" id="submit" class="btn btn-info" value="${message("rebate.common.submit")}" />
									<input type="button" class="btn btn-danger" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>
					</form>
     </div>
</body>
</html>