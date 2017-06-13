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
</head>
<body>
	  <ol class="breadcrumb">
          <li><a ><i class="fa fa-user"></i> 系统提现</a> </li>
          <li><a href="list.jhtml">系统提现列表</a></li>
          <li class="active">管理员身份验证</li>
      </ol>
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
									<input type="button" class="btn btn-primary" value="请求验证码" onclick="reqeustSmsCode(this)" />
								</td>
							</tr>																																								
							<th>
								&nbsp;
							</th>
							<td>
								<input type="button" class="btn btn-primary" value="继续" onclick="location.href='validationPwdSms.jhtml'" />
								<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
							</td>
						</tr>
						</table>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
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
}
function reqeustSmsCode(o){
					$.ajax({
						url: "reqeustSmsCode.jhtml",
						type: "POST",
						beforeSend:function(){
							$("#singlePay").attr("disabled","disabled");
						},
						cache: false,
						success: function(message) {
							if(message.type == "success"){
								time(o);
							}else{
								
							}
						}
					});
}  
</script> 													
</body>
</html>