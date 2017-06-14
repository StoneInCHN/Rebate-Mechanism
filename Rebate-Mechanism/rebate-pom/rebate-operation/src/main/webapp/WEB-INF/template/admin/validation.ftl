<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.leScoreRecord.withdraw.details")}</title>
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
	  <ol class="breadcrumb">
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.admin")}</a> </li>
          <li><a href="#">${message("rebate.admin.list")}</a></li>
          <li class="active">管理员身份验证</li>
      </ol>
      <form id="inputForm" action="editAdmin.jhtml" method="post" class="form-horizontal" role="form">
      <table class="input">
				      <tr>
								<th>
									${message("rebate.admin.username")}:
								</th>
								<td>
									${admin.username}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.admin.password")}:
								</th>
								<td>
									<input type="password" id="password" name="password" class="text" maxlength="20" />
								</td>
							</tr>			
							<tr>
								<th>
									短信验证码:
								</th>
								<td>
									<input type="text" id="smsCode" name="smsCode" class="text" maxlength="20" />
									<input type="button" id="smsCodeBtn" class="btn btn-primary" value="请求验证码" onclick="reqeustSmsCode(this)" />
								</td>
							</tr>																																								
							<th>
								&nbsp;
							</th>
							<td>
								<input type="submit" class="btn btn-primary" value="继续" />
								<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
							</td>
						</tr>
						</table>
						</form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>

<script type="text/javascript">  
var wait=60;  
function time(o) {  
        if (wait == 0) {  
            o.removeAttribute("disabled");            
            o.value="获取验证码";  
            wait = 60;  
        } else {  
            o.setAttribute("disabled", true);  
            o.value="重新发送(" + wait + ")";  
            wait--;  
            setTimeout(function() {  
                time(o)  
            },  
            1000); 
        }  
}  
function reqeustSmsCode(o){
					$.ajax({
						url: "reqeustSmsCode.jhtml",
						type: "POST",
						beforeSend:function(){
							$("#smsCodeBtn").attr("disabled","disabled");
						},
						cache: false,
						success: function(message) {
							if(message.type == "success"){
								time(o);
							}else{
								 var error = dialog({
									title: '管理员身份验证',
									content: message.content,
									okValue: '确定',
									ok: function () {}
								});
								error.showModal();
								$("#smsCodeBtn").attr("disabled",false);
							}
						}
					});
}  
</script> 													
</body>
</html>