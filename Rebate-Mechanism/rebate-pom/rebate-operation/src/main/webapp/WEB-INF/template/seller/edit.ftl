<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.seller.edit")}</title>
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
          <li class="active">${message("rebate.seller.edit")}</li>
      </ol>
       <form id="inputForm" action="update.jhtml" method="post">
       	    <input type="hidden" name="id" value="${seller.id}" />
			<table class="input">
							<tr>
								<th>
									${message("rebate.seller.id")}:
								</th>
								<td>
									${seller.id}
								</td>
							</tr>
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
									<input type="hidden" name="contactCellPhone" class="text" maxlength="50" value="${seller.contactCellPhone}"/>
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
									<input type="hidden" id="areaId"  name="areaId" value="${(seller.area.id)!}" treePath="${(seller.area.treePath)!}"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.seller.address")}:
								</th>
								<td>
									<span><input type="text" class="text" style="width:400px" name="address"  value="${seller.address}"/></span><span style="margin-left:20px;"><a id="editPosition" class="btn btn-info" style="float:none;width:120px !important">查看并编辑位置点</a></span>
									<input type="hidden" id="latitude" name="latitude"  value="${seller.latitude}"/>
									<input type="hidden" id="longitude" name="longitude" value="${seller.longitude}"/>
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
									${message("rebate.seller.commitmentImages")}:
								</th>
								<td>
									<!-- a block container is required -->
									  <ul  class="viewer-images clearfix">
									  	[#list commitmentImages as images]	
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
									<input type="text" name="avgPrice" class="text" maxlength="50" value="${seller.avgPrice}"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.seller.discount")}:
								</th>
								<td>
									<input type="text" name="discount" class="text" maxlength="50" value="${seller.discount}"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.seller.limitAmountByDay")}:
								</th>
								<td>
									<input type="text" name="limitAmountByDay" class="text" maxlength="50" value="${seller.limitAmountByDay}"/>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.businessTime")}:
								</th>
								<td>
									<input type="text" name="businessTime" class="text" maxlength="50" value="${seller.businessTime}"/>
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
									<span class="requiredField">*</span>${message("rebate.seller.accountStatus")}:
								</th>
								<td>
									<select  name="accountStatus">
										<option [#if seller.accountStatus =="ACTIVED"] selected="selected" [/#if]  value="ACTIVED">${message("rebate.common.accountStatus.ACTIVED")}</option>
										<option [#if seller.accountStatus =="LOCKED"] selected="selected" [/#if] value="LOCKED">${message("rebate.common.accountStatus.LOCKED")}</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.seller.isBeanPay")}:
								</th>
								<td>
									 <input type="radio" name="isBeanPay" value="true" [#if seller.isBeanPay==true]checked[/#if]/>${message("rebate.common.true")}
									 <input type="radio" name="isBeanPay" value="false" [#if seller.isBeanPay==false]checked[/#if]/>${message("rebate.common.false")}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.seller.description")}:
								</th>
								<td>
									<textarea  name="description" rows="3" cols="60">${seller.description}</textarea>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.seller.remark")}:
								</th>
								<td>
									<textarea  name="remark" rows="3" cols="60">${seller.remark}</textarea>
								</td>
							</tr>
						</table>
						<table class="input">
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("rebate.common.submit")}" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>  	
</form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lSelect.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();

		var $inputForm = $("#inputForm");
		// 表单验证
		$inputForm.validate({
			rules: {
				areaId: {
					required: true
				},
				discount:{
					required: true,
					number:true
				},
				limitAmountByDay:{
					required: true,
					number:true
				},
				avgPrice:{
					number:true
				},
				description: {
					required: true,
				}
			}
		});		
		
		var $areaId = $("#areaId");
			// 地区选择
			$areaId.lSelect({
				url: "${base}/console/common/area.jhtml"
		});

		$("#editPosition").click(function(){
			window.dialog({
	           id: 'editPosition-dialog',
	           title: '查看并修改商家位置点',
	           url:'./editPosition.jhtml?id=${seller.id}',
          	   quickClose: true,
          	   okValue: '确定',
			   ok: function () {
					var $editPositionFrame = $(window.frames["editPosition-dialog"].document);				
					var $latitude = $editPositionFrame.find('#latitude');
					var $longitude = $editPositionFrame.find('#longitude');
					if($latitude.val()){
						$("#latitude").val($latitude.val())
					}
					if($longitude.val()){
						$("#longitude").val($longitude.val())
					}
			   },
			   cancelValue: '取消'
       		 }).show(this);
			return false;
		});
	})
</script>	
</body>
</html>