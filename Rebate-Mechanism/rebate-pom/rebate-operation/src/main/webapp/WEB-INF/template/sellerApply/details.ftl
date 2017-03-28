<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerApplication.details")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
</head>
<body>
	  <ol class="breadcrumb">
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerManager")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.sellerApplication.list")}</a></li>
          <li class="active">${message("rebate.sellerApplication.details")}</li>
      </ol>
      <table class="input">
							<tr>
								<th>
									${message("rebate.sellerApplication.sellerName")}:
								</th>
								<td>
									${sellerApply.sellerName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.sellerCategory")}:
								</th>
								<td>
									[#if  sellerApply.sellerCategory??]
										${sellerApply.sellerCategory.categoryName}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.contactPerson")}:
								</th>
								<td>
									${sellerApply.contactPerson}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.contactCellPhone")}:
								</th>
								<td>
									${sellerApply.contactCellPhone}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.email")}:
								</th>
								<td>
									${sellerApply.email}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.address")}:
								</th>
								<td>
									${sellerApply.address}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.license")}:
								</th>
								<td>
									<a href="${base}${sellerApply.license}" target="1024"><img src="${base}${sellerApply.license}"  style="max-width:100px;max-height:100px;padding:5px" alt="${message("rebate.sellerApplication.license")}"></a>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.storePhoto")}:
								</th>
								<td>
									<a href="${base}${sellerApply.storePhoto}" target="1024"><img src="${base}${sellerApply.storePhoto}"  style="max-width:100px;max-height:100px;padding:5px" alt="${message("rebate.sellerApplication.storePhoto")}"></a>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.area")}:
								</th>
								<td>
									${sellerApply.area}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.applyStatus")}:
								</th>
								<td>
									[#if  sellerApply.applyStatus =="AUDIT_WAITING"]
										<span class="label label-info">${message("rebate.common.auditStatus.AUDIT_WAITING")}</span>
									[#elseif sellerApply.applyStatus =="AUDIT_PASSED"]
										<span class="label label-success">${message("rebate.common.auditStatus.AUDIT_PASSED")}</span>
									[#elseif sellerApply.applyStatus =="AUDIT_FAILED"]
										<span class="label label-warning">${message("rebate.common.auditStatus.AUDIT_FAILED")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.notes")}:
								</th>
								<td>
									${sellerApply.notes}
								</td>
							</tr>
						</table>
</body>
</html>