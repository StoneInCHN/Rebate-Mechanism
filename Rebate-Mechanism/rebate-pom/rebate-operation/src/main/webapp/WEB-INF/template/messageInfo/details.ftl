<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.messageInfo.edit")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.messageInfo")}</a> </li>
                <li><a href="list.jhtml">${message("rebate.messageInfo.list")}</a></li>
                <li class="active">${message("rebate.messageInfo.details")}</li>
          </ol>
			<table class="input tabContent">
							<tr>
								<th>
									${message("rebate.messageInfo.messageTitle")}:
								</th>
								<td>
									${messageInfo.messageTitle}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.messageInfo.messageContent")}:
								</th>
								<td>
									${messageInfo.messageContent}
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