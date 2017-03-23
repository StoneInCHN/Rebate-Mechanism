<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerApplication.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>

</head>
<body>
	<div class="mainbar">
		<div class="page-head">
			<div class="bread-crumb">
				<a ><i class="fa fa-user"></i> ${message("rebate.main.sellerApplication")}</a> 
				<span class="divider">/</span> 
				<a href="list.jhtml" ><i class="fa fa-list"></i>${message("rebate.sellerApplication.list")}</a>
				<span class="divider">/</span>
				<a  class="bread-current"><i class="fa fa-pencil-square-o"></i>${message("rebate.sellerApplication.edit")}</a>
			</div>
			<div class="clearfix"></div>
		</div>
		<div class="matter">
        <div class="container">
          <div class="row">
            <div class="col-md-12">
              <div class="widget wgreen">
                <div class="widget-head">
                  <div class="pull-left">${message("rebate.sellerApplication.base")}</div>
                  <div class="clearfix"></div>
                </div>
                <div class="widget-content">
                  <div class="padd">
						<input type="hidden" name="id" value="${sellerApplication.id}" />
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
									[#if  sellerApplication.sellerCategory??]
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
									${message("rebate.common.auditStatus."+sellerApply.applyStatus)}
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
                  </div>
                </div>
              </div>  
            </div>
          </div>
        </div>
	   </div>
	</div>
	<script type="text/javascript" src="${base}/resources/js/custom.js"></script>
</body>
</html>