<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.role.add")}</title>
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
                <li class="active">${message("rebate.role.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" class="form-horizontal" role="form">
                     	<table class="input">
								<tr>
									<th>
										<span class="requiredField">*</span>${message("rebate.role.name")}:
									</th>
									<td>
										<input type="text" name="name" class="text" maxlength="200" />
									</td>
								</tr>
								<tr>
									<th>
										${message("rebate.role.description")}:
									</th>
									<td>
										<textarea  name="description" class="text" maxlength="200"></textarea>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>
								</tr>
								<tr class="authorities">
									<th>
										<a href="javascript:;" class="selectAll" title="${message("rebate.role.selectAll")}">${message("rebate.main.systemNav")}</a>
									</th>
									<td>
										<span class="fieldSet">
											<label>
												<input type="checkbox" name="authorities" value="rebate:admin" /><span>${message("rebate.role.admin")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:role" /><span>${message("rebate.role.role")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:account" /><span>${message("rebate.account.settingGroup")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:sellerApply" /><span>${message("rebate.main.sellerApply")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:area" /><span>${message("rebate.main.area")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="rebate:operationLog" /><span>${message("rebate.main.operation.log")}</span>
											</label>
										</span>
									</td>
								</tr>
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