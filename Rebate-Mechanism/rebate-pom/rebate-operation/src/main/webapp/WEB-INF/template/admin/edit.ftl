<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("admin.admin.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/style.css" rel="stylesheet" type="text/css" />
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
	<div class="mainbar">
		<div class="page-head">
			<div class="bread-crumb">
				<a ><i class="fa fa-user"></i> ${message("csh.main.admin")}</a> 
				<span class="divider">/</span> 
				<a href="list.jhtml" ><i class="fa fa-list"></i>${message("csh.admin.list")}</a>
				<span class="divider">/</span>
				<a  class="bread-current"><i class="fa fa-pencil-square-o"></i>${message("admin.admin.edit")}</a>
			</div>
			<div class="clearfix"></div>
		</div>
		<div class="matter">
        <div class="container">
          <div class="row">
            <div class="col-md-12">
              <div class="widget wgreen">
                <div class="widget-head">
                  <div class="pull-left">${message("admin.admin.base")}</div>
                  <div class="clearfix"></div>
                </div>
                <div class="widget-content">
                  <div class="padd">
                    <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${admin.id}" />
						<input type="hidden" name="username" value="${admin.username}" />
						<table class="input tabContent">
							<tr>
								<th>
									${message("csh.admin.username")}:
								</th>
								<td>
									${admin.username}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("csh.admin.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" value="${admin.name}" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("csh.admin.email")}:
								</th>
								<td>
									<input type="text" name="email" class="text" maxlength="200" value="${admin.email}" />
								</td>
							</tr>
							<tr class="roles">
								<th>
									<span class="requiredField">*</span>${message("csh.admin.roles")}:
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
									${message("csh.admin.adminStatus")}:
								</th>
								<td>
									<input type="radio" value="actived" name="adminStatus" [#if admin.adminStatus== "actived" ]checked="checked"[/#if] />${message("csh.admin.adminStatus.actived")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="locked" name="adminStatus" [#if admin.adminStatus== "locked" ]checked="checked"[/#if] />${message("csh.admin.adminStatus.locked")}
								</td>
							</tr>
						</table>
						<table class="input">
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("csh.common.submit")}" />
									<input type="button" class="button" value="${message("csh.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>
					</form>
                  </div>
                </div>
              </div>  
            </div>
          </div>
        </div>
	   </div>
	</div>
	<script type="text/javascript" src="${base}/resources/js/custom.js"></script>
</body>
</html>