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
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog-plus.css" rel="stylesheet" type="text/css" />
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
									${message("rebate.endUser.cellPhoneNum")}:
								</th>
								<td>
									[#if  seller.endUser??]
										${seller.endUser.cellPhoneNum}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.nickName")}:
								</th>
								<td>
									[#if  seller.endUser??]
										${seller.endUser.nickName}
									[#else]
										--
									[/#if]
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
									${message("rebate.seller.address")}:
								</th>
								<td>
									<span>${seller.address}</span><span style="margin-left:20px;"><a id="viewPosition" class="btn btn-info" style="float:none;width:100px !important">查看位置点</a></span>
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
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original="${seller.licenseImgUrl}" alt="${message("rebate.seller.licenseImgUrl")}"></li>
									  </ul>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.storePhoto")}:
								</th>
								<td>
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original="${seller.storePictureUrl}" alt="${message("rebate.seller.storePhoto")}"></li>
									  </ul>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.envImages")}:
								</th>
								<td>
									<!-- a block container is required -->
									  <ul  class="viewer-images clearfix">
									  	[#list envImages as images]	
											 <li><img class="img-lazy img-rounded" data-original="${images.source}" alt="${images.title}"></li>
										[/#list]
									  </ul>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.rateScore")}:
								</th>
								<td>
									${seller.rateScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.avgPrice")}:
								</th>
								<td>
									${seller.avgPrice}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.discount")}:
								</th>
								<td>
									${seller.discount}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.businessTime")}:
								</th>
								<td>
									${seller.businessTime}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.favoriteNum")}:
								</th>
								<td>
									${seller.favoriteNum}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.totalOrderNum")}:
								</th>
								<td>
									${seller.totalOrderNum}
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
									<p>${seller.description}</p>
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
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();
		$("#viewPosition").click(function(){
			window.dialog({
	           id: 'selectEndUser-dialog',
	           title: '查看商家位置点',
	           url:'./viewPosition.jhtml?id=${seller.id}',
          	   quickClose: true
       		 }).show(this);
			return false;
		})
	})
</script>	
</body>
</html>