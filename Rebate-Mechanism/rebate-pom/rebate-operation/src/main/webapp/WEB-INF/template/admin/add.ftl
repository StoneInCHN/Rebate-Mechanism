<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.admin.add")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>

<script type="text/javascript">
$().ready(function() {
	var $saveBtn = $("#saveBtn");
	$saveBtn.click(function(){
		var roles = $("input[name='roleIds']");		
		if(roles.length == 0){
			var error = dialog({
					title:'添加管理员账户',
					content:  '请先在角色菜单添加至少一个自定义角色',
					okValue: '确定',
					ok: function () {}
				});
			error.showModal();			
			return false;
		}
	});
	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			username: {
				required: true,
				minlength: 2,
				maxlength: 20,
				remote: {
					url: "check_username.jhtml",
					cache: false
				}
			},
			password: {
				required: true,
				pattern: /^[^\s&\"<>]+$/,
				minlength: 4,
				maxlength: 20
			},
			rePassword: {
				required: true,
				equalTo: "#password"
			},
			email: {
				required: true,
				email: true
			},
			name: "required",
			roleIds: "required",
			adminStatus: "required"
		},
		messages: {
			username: {
				remote: "${message("rebate.admin.validate.exist")}"
			},
			password: {
				pattern: "${message("rebate.admin.validate.illegal")}"
			}
		}
	});

});
</script>
</head>
<body>
 <div>
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.admin")}</a> </li>
                <li><a href="#">${message("rebate.admin.list")}</a></li>
                <li class="active">${message("rebate.admin.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.username")}:
								</th>
								<td>
									<input type="text" name="username" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.password")}:
								</th>
								<td>
									<input type="password" id="password" name="password" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.rePassword")}:
								</th>
								<td>
									<input type="password" name="rePassword" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.email")}:
								</th>
								<td>
									<input type="text" name="email" class="text" maxlength="200" />
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
												<input type="checkbox" name="roleIds" value="${role.id}" /><span>${role.name}</span>
											</label>
										[/#list]
									</span>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.admin.adminStatus")}:
								</th>
								<td>
									<input type="radio" value="actived" name="adminStatus" checked="checked" />${message("rebate.admin.adminStatus.actived")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="locked" name="adminStatus" />${message("rebate.admin.adminStatus.locked")}
								</td>
							</tr>
						</table>
						<table class="input">
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" id="saveBtn" class="button" value="${message("rebate.common.submit")}" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>                                     
                     </form>
     </div>
</body>
</html>