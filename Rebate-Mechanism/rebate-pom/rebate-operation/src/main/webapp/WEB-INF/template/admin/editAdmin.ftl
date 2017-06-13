[#assign shiro=JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.admin.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript">
function updateCellPhone(){
		var cellPhone = $("#cellPhoneNum").val();
		alert(cellPhone);
		var phoneValid = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/; 
		 if(!phoneValid.test(cellPhone)){
		 	var error = dialog({
				title: '编辑平台提现信息',
				content: "请输入正确手机号",
				okValue: '确定',
				ok: function () {}
			});
			error.showModal();
			return false;
		 }else{
		 					$.ajax({
								url: "updateCellPhone.jhtml",
								type: "POST",
								data: {
									cellPhone:cellPhone
								},
								beforeSend:function(){
									$("#updateCellPhoneBtn").attr("disabled","disabled");
								},
								dataType: "json",
								cache: false,
								success: function(message) {
												var error = dialog({
													title: '编辑平台提现信息',
													content: message.content,
													okValue: '确定',
													ok: function () {}
												});
												error.showModal();
								}
							});
		 }

}
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
								//alert("success");
								time(o);
							}else{
								alert(message.content);
							}
						}
					});
}  
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.admin")}</a> </li>
                <li><a href="#">${message("rebate.admin.list")}</a></li>
                <li class="active">${message("rebate.admin.edit")}</li>
          </ol>
		 <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${admin.id}" />
						<input type="hidden" name="username" value="${admin.username}" />
						<table class="input tabContent">
							<tr>
								<th>
									${message("rebate.admin.username")}:
								</th>
								<td>
									${admin.username}
								</td>
							</tr>
							[@shiro.hasPermission name="rebate:systemWithdrawal"]
							[#if admin.isSystem == true]
							<tr>
								<th>
									${message("rebate.admin.cellPhoneNum")}:
								</th>
								<td>
									<input type="text" id="cellPhoneNum" name="cellPhoneNum" class="text" maxlength="20" value="${admin.cellPhoneNum}"/>
									<input type="button" id="updateCellPhoneBtn" class="button" value="保存" onclick="updateCellPhone()'" />
								</td>
							</tr>	
							<tr>
								<th>
									银行卡管理:
								</th>
								<td>
										<div class="button-group">
								              <a  id="addButton" class="btn btn-default"><i class="fa fa-plus"></i><span>添加</span></a>
								              <a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
								              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
								         </div>
								         <table id="listTable" class="table table-striped table-bordered">
										 		<thead>
													<tr>
														<th class="check">
															<input type="checkbox" id="selectAll" />
														</th>
														<th>
															银行卡号
														</th>
														<th>
															银行名称
														</th>
													</tr>
												</thead>
												<tbody>
													[#list cardList as card]
													<tr>
														<td>
															<input type="checkbox"  name="ids" value="${card.id}" />
														</td>
														<td>
															${card.cardNum}
														</td>
														<td>
															${card.bankName}
														</td>
													[/#list]
												</tbody>
												</table>
								</td>
							</tr>																		
							[/#if]
		                     [/@shiro.hasPermission]
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
     </div>
</body>
</html>