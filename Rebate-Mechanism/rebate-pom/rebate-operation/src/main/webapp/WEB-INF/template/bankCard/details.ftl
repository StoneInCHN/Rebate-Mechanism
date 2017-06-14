<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.bankCard.details")}</title>
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
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.bankCard")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.bankCard.list")}</a></li>
          <li class="active">${message("rebate.bankCard.details")}</li>
      </ol>
	<table class="input">
							<tr>
								<th>
									${message("rebate.bankCard.ownerName")}:
								</th>
								<td>
									${bankCard.ownerName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.reservedMobile")}:
								</th>
								<td>
									${bankCard.reservedMobile}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.bankLogo")}:
								</th>
								<td>
									${bankCard.bankLogo}
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original="${bankCard.bankLogo}" alt="${message("rebate.bankCard.bankLogo")}"></li>
									  </ul>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.cardNum")}:
								</th>
								<td>
									${bankCard.cardNum}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.bankName")}:
								</th>
								<td>
									${bankCard.bankName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.cardType")}:
								</th>
								<td>
									${bankCard.cardType}
								</td>
							</tr>
								<tr>
								<th>
									${message("rebate.bankCard.idCard")}:
								</th>
								<td>
									${bankCard.idCard}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.cellPhoneNum")}:
								</th>
								<td>
									[#if  bankCard.endUser]
										${bankCard.endUser.cellPhoneNum}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.endUser")}:
								</th>
								<td>
									[#if  bankCard.endUser]
										${bankCard.endUser.nickName}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.bankCard.isDefault")}:
								</th>
								<td>
									[#if  bankCard.isDefault]
										<span class="label label-success">${message("rebate.common.true")}</span>
									[#else]
										<span class="label label-danger">${message("rebate.common.false")}</span>
									[/#if]
								</td>
							</tr>
								<tr>
								<th>
									${message("rebate.bankCard.delStatus")}:
								</th>
								<td>
									[#if  bankCard.delStatus]
										<span class="label label-success">${message("rebate.common.true")}</span>
									[#else]
										<span class="label label-info">${message("rebate.common.false")}</span>
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.common.createDate")}:
								</th>
								<td>
									${bankCard.createDate?string("yyyy-MM-dd HH:mm:ss")}
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
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();
	})
</script>	
</body>
</html>