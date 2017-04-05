<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.operationLog.details")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
</head>
<body>
	  <ol class="breadcrumb">
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerManager")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.operationLog.list")}</a></li>
          <li class="active">${message("rebate.operationLog.details")}</li>
      </ol>
	<table class="input">
						<tr>
							<th>
								${message("rebate.operationLog.operator")}:
							</th>
							<td>
								${log.operation}
							</td>
						</tr>
						<tr>
							<th>
								${message("rebate.operationLog.operator")}:
							</th>
							<td>
								${log.operator}
							</td>
						</tr>
						<tr>
							<th>
								${message("rebate.operationLog.content")}:
							</th>
							<td>
								${log.content!"-"}
							</td>
						</tr>
						<tr>
							<th>
								${message("rebate.operationLog.parameter")}:
							</th>
							<td>
								<div class="well">
									${log.parameter?html}
								</div>
							</td>
						</tr>
						<tr>
							<th>
								${message("rebate.operationLog.ip")}:
							</th>
							<td>
								${log.ip}
							</td>
						</tr>
						<tr>
							<th>
								${message("lb.common.createDate")}
							</th>
							<td>
								${log.createDate?string("yyyy-MM-dd HH:mm:ss")}
							</td>
						</tr>
						<tr>
							<th>
								&nbsp;
							</th>
							<td>
								<input type="button" class="btn btn-primary" value="${message("lb.common.back")}" onclick="location.href='list.jhtml'" />
							</td>
						</tr>
					</table>
</body>
</html>