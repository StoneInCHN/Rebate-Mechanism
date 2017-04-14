<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.settingConfig.edit")}</title>
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
			configValue:{
				required:true
			}
		}
	});

});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.settingConfig")}</a> </li>
                <li><a href="#">${message("rebate.settingConfig.list")}</a></li>
                <li class="active">${message("rebate.settingConfig.edit")}</li>
          </ol>
		  <form id="inputForm" action="updateOther.jhtml" method="post">
						<input type="hidden" name="id" value="${settingConfig.id}" />
						<table class="input">
							<tr>
								<th>
									${message("rebate.settingConfig.configKey")}:
								</th>
								<td>
									${message("rebate.settingConfig.configKey."+settingConfig.configKey)}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.settingConfig.configValue")}:
								</th>
								<td>
									<input type="text" name="configValue" value="${settingConfig.configValue}" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.settingConfig.isEnabled")}:
								</th>
								<td>
									<select name="isEnabled">
										<option [#if settingConfig.isEnabled] selected="selected" [/#if] value="true">${message("rebate.settingConfig.isEnabled.true")}</option>
										<option [#if !settingConfig.isEnabled] selected="selected" [/#if] value="false">${message("rebate.settingConfig.isEnabled.false")}</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.settingConfig.remark")}:
								</th>
								<td>
									<textarea name="remark" cols="50" rows="5">${settingConfig.remark}</textarea>
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