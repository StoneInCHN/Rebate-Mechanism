<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.admin.add")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
</head>
<body>
 <div>
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.admin")}</a> </li>
                <li><a href="#">${message("rebate.admin.list")}</a></li>
                <li><a href="#">编辑平台提现信息</a></li>
                <li class="active">添加银行卡</li>
          </ol>
		 <form id="inputForm"  method="post" action= "saveBankCard.jhtml" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>持卡人姓名:
								</th>
								<td>
									<input type="text" id="ownerName" name="ownerName" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>银行卡号:
								</th>
								<td>
									<input type="text" id="cardNum" name="cardNum" class="text" maxlength="19" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>所属银行:
								</th>
								<td>
									<input type="text" id="bankName" name="bankName" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>银行卡类型:
								</th>
								<td>
									<input type="text" id="cardType" name="cardType" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>身份证号:
								</th>
								<td>
									<input type="text" id="idCard" name="idCard" class="text" maxlength="18" />
								</td>
							</tr>
							<tr class="roles">
								<th>
									<span class="requiredField">*</span>银行预留手机号:
								</th>
								<td>
									<input type="text" id="reservedMobile" name="reservedMobile" class="text" maxlength="11" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>设置为默认提现卡:
								</th>
								<td>
									<input type="radio" value="true" name="isDefault"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="false" name="isDefault"  checked="checked" />否
								</td>
							</tr>
						</table>
						<table class="input">
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" id="submitBtn" class="button" style="width:100px !important"  value="保存" />
									<input type="button" id="verifyCardBtn" class="button"  value="验证银行卡" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>                                     
                     </form>
     </div>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			ownerName: {
				required: true,
				minlength: 2,
				maxlength: 20
			},
			cardNum: {
				required: true,
				pattern: /^(\d{16}|\d{19})$/,
				minlength: 16,
				maxlength: 19
			},
			bankName: "required",
			cardType: "required",
			idCard:{
				required: true,
				minlength: 15,
				maxlength: 18				
			},
			isDefault: "required",
			reservedMobile:{
				required: true,
				maxlength: 11,
				isMobile:true		
			}
		}
	});
	var $submitBtn = $("#submitBtn");
	$submitBtn.hide();
	var $verifyCardBtn = $("#verifyCardBtn");
	$verifyCardBtn.click(function(){
				 		$.ajax({
								url: "verifyBankCard.jhtml",
								type: "POST",
								data: {
									cardNum:$("#cardNum").val(),
									ownerName:$("#ownerName").val(),
									idCard:$("#idCard").val(),
									reservedMobile:$("#reservedMobile").val()
								},
								beforeSend:function(){
									$verifyCardBtn.attr("disabled","disabled");
								},
								dataType: "json",
								cache: false,
								success: function(message) {
										var success = dialog({
													title: '添加银行卡',
													content: "验证结果："+message.content,
													okValue: '确定',
													ok: function () {},
													cancelValue: '取消',
													cancel: function () {}
												});
												success.showModal();
										if(message.type == "success"){
												$verifyCardBtn.hide();
												$("#cardNum").attr("readonly","readonly");
												$("#ownerName").attr("readonly","readonly");
												$("#idCard").attr("readonly","readonly");
												$("#reservedMobile").attr("readonly","readonly");
												$submitBtn.show();
										}else{
											$verifyCardBtn.attr("disabled",false);
										}
								},
								error: function(message) {
												var error = dialog({
													title: '添加银行卡',
													content: '系统错误',
													okValue: '确定',
													ok: function () {},
													cancelValue: '取消',
													cancel: function () {}													
												});
												error.showModal();
												$verifyCardBtn.attr("disabled",false);
								}
							});
	});

});
</script>     
</body>
</html>