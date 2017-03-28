<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.admin.edit")}</title>
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
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			email: {
				required: true,
				email: true
			},
			roleIds: "required",
			adminStatus: "required"
		}
	});

});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.admin")}</a> </li>
                <li><a href="#">${message("rebate.admin.list")}</a></li>
                <li class="active">${message("rebate.admin.edit")}</li>
          </ol>
		 <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${admin.id}" />
						<input type="hidden" name="username" value="${admin.username}" />
						<table class="input tabContent">
							<tr>
								<th>
									${message("rebate.admin.username")}:
								</th>
								<td>
									${admin.username}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" value="${admin.name}" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.email")}:
								</th>
								<td>
									<input type="text" name="email" class="text" maxlength="200" value="${admin.email}" />
								</td>
							</tr>
							<tr class="roles">
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.roles")}:
								</th>
								<td>
									<span class="fieldSet">
										[#list roles as role]
											<label>
												<input type="checkbox" name="roleIds" value="${role.id}"[#if admin.roles?seq_contains(role)] checked="checked"[/#if] /><span>${role.name}</span>
											</label>
										[/#list]
									</span>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.admin.adminStatus")}:
								</th>
								<td>
									<input type="radio" value="actived" name="adminStatus" [#if admin.adminStatus== "actived" ]checked="checked"[/#if] />${message("rebate.admin.adminStatus.actived")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="locked" name="adminStatus" [#if admin.adminStatus== "locked" ]checked="checked"[/#if] />${message("rebate.admin.adminStatus.locked")}
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