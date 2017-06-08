<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerApplication.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog-plus.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript">
$(function() {

	var $inputForm = $("#inputForm");
	var $submit = $("#submit");	
	$('.viewer-images').viewer();
	$('.img-lazy').lazyload();
	
	// 表单验证
	$inputForm.validate({
		rules: {
			applyStatus: {
				required:true
			},
			notes: {
				required:true
			},
			limitAmountByDay:{
				required:true,
				number:true
			}
		},
		submitHandler:function(form){
			$submit.attr("disabled",true);
			$.ajax({
				url:$inputForm.attr("action"),
				type:"POST",
				data:$inputForm.serialize(),
				dataType:"json",
				cache:false,
				success:function(message){
					$.message(message);
					if(message.type =="success"){
						setTimeout("location.href='details.jhtml?id=${sellerApply.id}'",1000);
					}else{
						$submit.attr("disabled",false);
					}
				}
			});
		}
	});
	
	$("#viewPosition").click(function(){
			window.dialog({
           id: 'selectEndUser-dialog',
           title: '查看商家位置点',
           url:'./viewPosition.jhtml?id=${sellerApply.id}',
           quickClose: true
        }).show(this);
		return false;
	})

});
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerApply")}</a> </li>
                <li><a href="#">${message("rebate.sellerApplication.list")}</a></li>
                <li class="active">${message("rebate.sellerApplication.edit")}</li>
          </ol>
		  <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${sellerApply.id}" />
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
									${message("rebate.sellerApplication.area")}:
								</th>
								<td>
									${sellerApply.area}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.address")}:
								</th>
								<td>
									<span>${sellerApply.address}</span><span style="margin-left:20px;"><a id="viewPosition" class="btn btn-info" style="float:none;width:100px !important">查看位置点</a></span>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.license")}:
								</th>
								<td>
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original="${sellerApply.licenseImgUrl}" alt="${message("rebate.sellerApplication.licenseImgUrl")}"></li>
									</ul>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.storePhoto")}:
								</th>
								<td>
									 <ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original="${sellerApply.storePhoto}" alt="${message("rebate.sellerApplication.storePhoto")}"></li>
									  </ul>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.envImages")}:
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
									${message("rebate.sellerApplication.discount")}:
								</th>
								<td>
									${sellerApply.discount}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.limitAmountByDay")}:
								</th>
								<td>
									<input type="text" name="limitAmountByDay" class="text" maxlength="50" value="${sellerApply.limitAmountByDay}"/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.description")}:
								</th>
								<td>
									<textarea readonly="readonly" rows="6" cols="60">${sellerApply.description}</textarea>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.sellerApplication.applyStatus")}:
								</th>
								<td>
									<select  name="applyStatus">
										<option value="">${message("rebate.common.auditStatus.select")}</option>
										<option value="AUDIT_PASSED">${message("rebate.common.auditStatus.AUDIT_PASSED")}</option>
										<option value="AUDIT_FAILED">${message("rebate.common.auditStatus.AUDIT_FAILED")}</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerApplication.notes")}:
								</th>
								<td>
									<textarea  name="notes" rows="4" cols="40">${sellerApply.notes}</textarea>
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" id="submit" class="btn btn-info" value="${message("rebate.common.submit")}" />
									<input type="button" class="btn btn-danger" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>
					</form>
     </div>
</body>
</html>