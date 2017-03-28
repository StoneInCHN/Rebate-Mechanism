<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.seller.details")}</title>
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
          <li><a href="list.jhtml">${message("rebate.seller.list")}</a></li>
          <li class="active">${message("rebate.seller.details")}</li>
      </ol>
	<table class="input">
							<tr>
								<th>
									${message("rebate.seller.name")}:
								</th>
								<td>
									${seller.name}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.sellerCategory")}:
								</th>
								<td>
									[#if  seller.sellerCategory??]
										${seller.sellerCategory.categoryName}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.contactPerson")}:
								</th>
								<td>
									${seller.contactPerson}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.contactCellPhone")}:
								</th>
								<td>
									${seller.contactCellPhone}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.address")}:
								</th>
								<td>
									${seller.address}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.licenseNum")}:
								</th>
								<td>
									${seller.licenseNum}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.licenseImgUrl")}:
								</th>
								<td>
									<a href="${base}${seller.licenseImgUrl}" target="1024"><img src="${base}${seller.licenseImgUrl}"  style="max-width:100px;max-height:100px;padding:5px" alt="${message("rebate.seller.licenseImgUrl")}"></a>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.storePhoto")}:
								</th>
								<td>
									<a href="${base}${seller.storePhoto}" target="1024"><img src="${base}${seller.storePhoto}"  style="max-width:100px;max-height:100px;padding:5px" alt="${message("rebate.seller.storePhoto")}"></a>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.area")}:
								</th>
								<td>
									${seller.area}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.accountStatus")}:
								</th>
								<td>
									${message("rebate.common.accountStatus."+seller.accountStatus)}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.description")}:
								</th>
								<td>
									${seller.description}
								</td>
							</tr>
						</table>	
</body>
</html>