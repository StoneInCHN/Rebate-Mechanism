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
</head>
<body>
	 <div>
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.admin")}</a> </li>
                <li><a href="#">${message("rebate.admin.list")}</a></li>
                <li class="active">编辑平台提现信息</li>
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
									<input type="button" id="updateCellPhoneBtn" class="button" value="保存" onclick="updateCellPhone()" />
								</td>
							</tr>	
							<tr>
								<th>
									银行卡管理:
								</th>
								<td>
										<div class="button-group">
								              <a  id="addCardBtn" class="btn btn-default"><i class="fa fa-plus"></i><span>添加</span></a>
								              <a  id="defaultCardBtn" class="btn btn-default" style="width:130px !important"><span>设为默认提现卡</span></a>
								              <a  id="deleteCardBtn" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
								              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
								         </div>
								         <table id="listTable" class="table table-striped table-bordered">
										 		<thead>
													<tr>
															<th class="check">
																<input type="checkbox" id="selectAll" />
															</th>
															<th>
																管理员
															</th>															
															<th>
																${message("rebate.bankCard.ownerName")}
															</th>
															<th>
																${message("rebate.bankCard.cardNum")}
															</th>
															<th>
																所属银行
															</th>
															<th>
																${message("rebate.bankCard.cardType")}
															</th>
															<th>
																${message("rebate.bankCard.idCard")}
															</th>
															<th>
																${message("rebate.bankCard.isDefault")}
															</th>
															<th>
																${message("rebate.bankCard.delStatus")}
															</th>
															<th>
																银行${message("rebate.bankCard.reservedMobile")}
															</th>
															<th>
																${message("rebate.common.createDate")}
															</th>																										
													</tr>
												</thead>
												<tbody>
													[#list cardList as bankCard]
														<tr>
															<td>
																<input type="checkbox"  name="ids" value="${bankCard.id}" />
															</td>
															<td>
																[#if bankCard.admin??]
																${bankCard.admin.username}
																[#else]
																--
																[/#if]
															</td>															
															<td>
																<span title="${bankCard.ownerName}">${bankCard.ownerName}</sapn>
															</td>
															<td>
																${bankCard.cardNum}
															</td>
															<td>
																${bankCard.bankName}
															</td>
															<td>
																${bankCard.cardType}
															</td>
															<td>
																${bankCard.idCard}
															</td>
															<td>
																[#if  bankCard.isDefault]
																	<span class="label label-success">${message("rebate.common.true")}</span>
																[#else]
																	<span class="label label-danger">${message("rebate.common.false")}</span>
																[/#if]
															</td>
															<td>
																[#if  bankCard.delStatus]
																	<span class="label label-success">${message("rebate.common.true")}</span>
																[#else]
																	<span class="label label-info">${message("rebate.common.false")}</span>
																[/#if]
															</td>
															<td>
																${bankCard.reservedMobile}
															</td>
															<td>
																${bankCard.createDate?string("yyyy-MM-dd HH:mm:ss")}
															</td>
														</tr>
													[/#list]
												</tbody>
												</table>
											<div class="button-group">
								              <input type="button" class="button"  style="float:left" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								         </div>
								</td>
							</tr>																		
							[/#if]
		                     [/@shiro.hasPermission]
						</table>
					</form>
     </div>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {
	//添加银行卡
	var $addCardBtn = $("#addCardBtn");
	$addCardBtn.click(function(){
		location.href="addBankCard.jhtml";
	});

	// 删除银行卡
	var $deleteCardBtn = $("#deleteCardBtn");
	var $selectAll = $("#selectAll");
	var $ids = $("#listTable input[name='ids']");
	$selectAll.click( function() {
		var $this = $(this);
		var $enabledIds = $("#listTable input[name='ids']:enabled");
		if ($this.prop("checked")) {
			$enabledIds.prop("checked", true);
			if ($enabledIds.filter(":checked").size() > 0) {
				$deleteCardBtn.removeClass("disabled");
			} else {
				$deleteCardBtn.addClass("disabled");
			}
		} else {
			$deleteCardBtn.addClass("disabled");
		}
	});
	$ids.click( function() {
		var $this = $(this);
		if ($this.prop("checked")) {
			$deleteCardBtn.removeClass("disabled");
		} else {
			$this.closest("tr").removeClass("selected");
			if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
				$deleteCardBtn.removeClass("disabled");
			} else {
				$deleteCardBtn.addClass("disabled");
			}
		}
	});	
	$deleteCardBtn.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		var $listTable = $("#listTable");
		var $pageTotal = $("#pageTotal");
		$.dialog({
			type: "warn",
			content: message("admin.dialog.deleteConfirm"),
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "deleteCard.jhtml",
					type: "POST",
					data: $checkedIds.serialize(),
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$pageTotal.text(parseInt($pageTotal.text()) - $checkedIds.size());
							$checkedIds.closest("tr").remove();
							if ($listTable.find("tr").size() <= 1) {
								setTimeout(function() {
									location.reload(true);
								}, 3000);
							}
						}
						$deleteCardBtn.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
	//设置默认银行卡
var $defaultCardBtn = $("#defaultCardBtn");
$defaultCardBtn.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		var $listTable = $("#listTable");
		var $pageTotal = $("#pageTotal");
		if($checkedIds.length > 1){
			var error = dialog({
				title: '编辑平台提现信息',
				content: "请选择一条记录",
				okValue: '确定',
				ok: function () {}
			});
			return false;
		}
		$.dialog({
			type: "warn",
			content: "确认设置选中银行卡为默认提现卡？",
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "updateCardDefault.jhtml",
					type: "POST",
					data: {bankCardId:$($checkedIds[0]).val()},
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$pageTotal.text(parseInt($pageTotal.text()) - $checkedIds.size());
							$checkedIds.closest("tr").remove();
							if ($listTable.find("tr").size() <= 1) {
								setTimeout(function() {
									location.reload(true);
								}, 3000);
							}
						}
						$defaultCardBtn.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
});

function updateCellPhone(){
		var cellPhone = $("#cellPhoneNum").val();
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
												var success = dialog({
													title: '编辑平台提现信息',
													content: message.content,
													okValue: '确定',
													ok: function () {}
												});
												success.showModal();
												$("#updateCellPhoneBtn").attr("disabled",false);
								},
								error: function(message) {
												var error = dialog({
													title: '编辑平台提现信息',
													content: '系统错误',
													okValue: '确定',
													ok: function () {}
												});
												error.showModal();
												$("#updateCellPhoneBtn").attr("disabled",false);
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
</body>
</html>