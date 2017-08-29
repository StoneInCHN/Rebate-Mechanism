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
<link href="${base}/resources/style/dialog-plus.css" rel="stylesheet" type="text/css" />
</head>
<body>
	  <ol class="breadcrumb">
          <li><a ><i class="fa fa-user"></i> 平台提现</a> </li>
          <li><a href="list.jhtml">平台提现列表</a></li>
          <li class="active">提现页面</li>
      </ol>
      <div class="content-search accordion-group">
             <div class="accordion-heading" role="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  请先核对提现信息
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="accordion-inner">
						<table class="queryFiled">
							<tr>
								<th>
									提现人
								</th>
								<td>
									<input type="text" name="operator" class="text" value="${admin.username}" maxlength="20" disabled="disabled"/>
								</td>
								<th>
									提现人手机号
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${admin.cellPhoneNum}" maxlength="20" disabled="disabled"/>
								</td>
								<th>
									银行卡号
								</th>
								<td>
									<input type="text" id="cardNum" name="cardNum" class="text" value="${defaultCard.cardNum}" maxlength="20" disabled="disabled"/>
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
      <table class="input">
							<tr>
								<th>
									请输入提现金额:
								</th>
								<td>
									<input type="text" id="amount" name="amount" class="text" maxlength="6" />
								</td>
							</tr>
							<th>
								&nbsp;
							</th>
							<td>
								<input type="button" id="singlePay" class="btn btn-info" value="平台提现"/>
								<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='add.jhtml'" />
							</td>
						</tr>
						</table>	
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript">
	$(function(){
		$("#singlePay").click(function(){
		var amount = $("#amount").val();
		 var numValid = /^[0-9]*[1-9][0-9]*$/; //正整数
		 if(!numValid.test(amount)){
		 	var error = dialog({
				title: '平台提现',
				content: "请输入正确金额",
				okValue: '确定',
				ok: function () {}
			});
			error.showModal();
			return false;
		 }
		 if($("#cardNum").val() == null || $("#cardNum").val() == ""){
		 	var error = dialog({
				title: '平台提现',
				content: "银行卡信息有误，请先配置有效的银行卡",
				okValue: '确定',
				ok: function () {}
			});
			error.showModal();
			return false;		 
		  }		 
		 if(amount > 200000){
		 	var error = dialog({
				title: '平台提现',
				content: "单笔提现金额不能超过20万",
				okValue: '确定',
				ok: function () {}
			});
			error.showModal();
			return false;		 
		  }
			var d = dialog({
				title: '平台提现',
				content: "请确认提现金额："+ $("#amount").val() +"元，提现银行卡："+ $("#cardNum").val()+"，保证提现后还剩足够的金额用于平台商家货款结算和乐分提现！！",
				okValue: '确定',
				ok: function () {
					var _that = this;
					if(_that.cancelDisplay == true){
					_that.content('提交中,请耐心稍等(不要关闭此窗口)...');
							$.ajax({
								url: "singlePay.jhtml",
								type: "POST",
								data: {
									amount:amount
								},
								beforeSend:function(){
									$("#singlePay").attr("disabled","disabled");
								},
								dataType: "json",
								cache: false,
								success: function(message) {
									setTimeout(function() {
											_that.content(message.content+",正在跳转倒计时5秒...");
									}, 1000);
									setTimeout(function() {
											_that.content(message.content+",正在跳转倒计时4秒...");
									}, 2000);
									setTimeout(function() {
											_that.content(message.content+",正在跳转倒计时3秒...");
									}, 3000);
									setTimeout(function() {
											_that.content(message.content+",正在跳转倒计时2秒...");
									}, 4000);
									setTimeout(function() {
											_that.content(message.content+",正在跳转倒计时1秒...");
									}, 5000);
									setTimeout(function() {
											location.reload();
									}, 6000);
								}
							});
					}
					_that.cancelDisplay =false;
					return false;
				},
				cancelValue: '取消',
				cancel: function () {}
			});
			d.showModal();
		});
	})
</script>												
</body>
</html>