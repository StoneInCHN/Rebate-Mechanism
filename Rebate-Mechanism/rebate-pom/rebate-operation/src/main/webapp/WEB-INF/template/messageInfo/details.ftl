<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.userHelp.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.userHelp")}</a> </li>
                <li><a href="list.jhtml">${message("rebate.userHelp.list")}</a></li>
                <li class="active">${message("rebate.userHelp.edit")}</li>
          </ol>
			<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.userHelp.title")}:
								</th>
								<td>
									${userHelp.title}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.userHelp.configOrder")}:
								</th>
								<td>
									${userHelp.configOrder}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.userHelp.isEnabled")}:
								</th>
								<td>
									[#if  userHelp.isEnabled]
										<span class="label label-info">${message("rebate.common.true")}</span>
									[#elseif !userHelp.isEnabled]
										<span class="label label-success">${message("rebate.common.false")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.userHelp.content")}:
								</th>
								<td>
									${userHelp.content}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.userHelp.remark")}:
								</th>
								<td>
									${userHelp.remark}
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>  
     </div>
</body>
</html>