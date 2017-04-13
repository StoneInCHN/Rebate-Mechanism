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
	[@flash_message /]
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			pyName:"required",
			order: "digits"
		}
	});
});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.area")}</a> </li>
                <li><a href="#">${message("rebate.area.list")}</a></li>
                <li class="active">${message("rebate.area.edit")}</li>
          </ol>
		  <form id="inputForm" action="update.jhtml" method="post">
				<input type="hidden" name="id" value="${area.id}" />
				<table class="input">
					<tr>
								<th>
									${message("rebate.area.parent")}:
								</th>
								<td>
									[#if area.parent??]${area.parent.name}[#else]${message("rebate.area.root")}[/#if]
								</td>
					</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.area.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" value="${area.name}" maxlength="100" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.area.pyName")}:
								</th>
								<td>
									<input type="text" name="pyName" class="text" maxlength="200" value="${area.pyName}"/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.area.isCity")}:
								</th>
								<td>
									<input type="radio" value="true" [#if area.isCity] checked="checked" [/#if] name="isCity" />${message("rebate.common.true")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="false" [#if !area.isCity] checked="checked" [/#if] name="isCity" />${message("rebate.common.false")}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.common.order")}:
								</th>
								<td>
									<input type="text" name="order" class="text" value="${area.order}" maxlength="9" />
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("rebate.common.submit")}" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml[#if area.parent??]?parentId=${area.parent.id}[/#if]'" />
								</td>
							</tr>
						</table>
					</form>
     </div>
</body>
</html>