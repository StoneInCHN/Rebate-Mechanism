<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.account.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $password = $("#password");
	var $name = $("#name");
	var $submit = $(":submit");
	
	
	// 表单验证
	$inputForm.validate({
		rules: {
			password: {
				required: true,
				pattern: /^[^\s&\"<>]+$/,
				minlength: 6
			},
			rePassword: {
				equalTo: "#password"
			},
			nickName: {
				required: true
			}
		},
		messages: {
			password: {
				pattern: "${message("rebate.reg.passwordIllegal")}",
				minlength: "${message("rebate.password.minlength",6)}"
			},
			repassword:{
					 equalTo:"${message("rebate.reg.passwordIllegal")}"
			},
			name:{
				required:"${message("rebate.reg.nameRequired")}"
			}
		},
		
		submitHandler:function(form){
			$submit.attr("disabled",true);
			$.ajax({
				url:$inputForm.attr("action"),
				type:"POST",
				data:{
						password:$password.val(),
						name:$name.val()
				},
				dataType:"json",
				cache:false,
				success:function(message){
					$.message(message);
					$submit.attr("disabled",false);
					setTimeout("location.href='accountInfo.jhtml'",1000);
				}
			});
		}
	});
});
</script>
</head>
<body>
     <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.account.settingGroup")}</a> </li>
                <li><a href="#">${message("rebate.main.account.setting")}</a></li>
                <li class="active">${message("rebate.account.edit")}</li>
      </ol>

</body>
</html>