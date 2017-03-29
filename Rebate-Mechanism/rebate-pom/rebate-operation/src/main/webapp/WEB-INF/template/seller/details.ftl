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
										 <li><img class="img-lazy img-rounded" data-original="${seller.storePhoto}" alt="${message("rebate.seller.storePhoto")}"></li>
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