<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.holidayConfig.add")}</title>
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
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">

$().ready(function() {
	var $holidayName = $("#holidayName");
	var $startDate = $("#startDate");
	var $endDate = $("#endDate");
	
	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			holidayName: {
				required: true
			},
			startDate: {
				required: true
			},
			endDate: {
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.holidayConfig")}</a> </li>
                <li><a href="#">${message("rebate.holidayConfig.list")}</a></li>
                <li class="active">${message("rebate.holidayConfig.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.holidayConfig.name")}:
								</th>
								<td>
									<input type="text" id="holidayName"  name="holidayName" maxlength="30"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.holidayConfig.time")}:
								</th>
								<td>
									<input type="text" id="startDate" name="startDate" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" readonly [#if startDate??]value="${startDate?string("yyyy-MM-dd")}"[/#if] />
									--
									<input type="text" id="endDate" name="endDate" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'startDate\')}'});" readonly [#if endDate??]value="${endDate?string("yyyy-MM-dd")}" [/#if]/>
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