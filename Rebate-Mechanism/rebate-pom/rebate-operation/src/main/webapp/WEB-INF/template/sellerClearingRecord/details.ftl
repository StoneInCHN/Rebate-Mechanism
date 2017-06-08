<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerClearingRecord.details")}</title>
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
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerClearingRecord")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.sellerClearingRecord.list")}</a></li>
          <li class="active">${message("rebate.sellerClearingRecord.details")}</li>
      </ol>
	<table class="input">
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.sn")}:
								</th>
								<td width=500>
									${sellerClearingRecord.sn}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.clearingSn")}:
								</th>
								<td>
									${sellerClearingRecord.clearingSn}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.reqSn")}:
								</th>
								<td>
									${sellerClearingRecord.reqSn}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.endUser")}:
								</th>
								<td>
									${sellerClearingRecord.endUser.nickName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.seller")}:
								</th>
								<td>
									${sellerClearingRecord.seller.name}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.amount")}:
								</th>
								<td>
									${sellerClearingRecord.amount}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.totalOrderAmount")}:
								</th>
								<td>
									${sellerClearingRecord.totalOrderAmount}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.handlingCharge")}:
								</th>
								<td>
									${sellerClearingRecord.handlingCharge}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.clearingStatus")}:
								</th>
								<td>
									[#if  sellerClearingRecord.clearingStatus =="PROCESSING"]
										<span class="label label-info">${message("rebate.sellerClearingRecord.clearingStatus.PROCESSING")}</span>
									[#elseif sellerClearingRecord.clearingStatus =="SUCCESS"]
										<span class="label label-success">${message("rebate.sellerClearingRecord.clearingStatus.SUCCESS")}</span>
									[#elseif sellerClearingRecord.clearingStatus =="FAILED"]
										<span class="label label-warning">${message("rebate.sellerClearingRecord.clearingStatus.FAILED")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.isClearing")}:
								</th>
								<td>
									[#if sellerClearingRecord.isClearing ??]
										[#if sellerClearingRecord.isClearing ] ${message("rebate.common.true")} [/#if]
										[#if !sellerClearingRecord.isClearing ] ${message("rebate.common.false")} [/#if] 
									[#else] 
										--		
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.common.createDate")}:
								</th>
								<td>
									${sellerClearingRecord.createDate?string("yyyy-MM-dd HH:mm:ss")}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.bankCardId")}:
								</th>
								<td>
									${sellerClearingRecord.bankCardId}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.valid")}:
								</th>
								<td>
									[#if sellerClearingRecord.valid ??]
										[#if sellerClearingRecord.valid ] ${message("rebate.common.true")} [/#if]
										[#if !sellerClearingRecord.valid ] ${message("rebate.common.false")} [/#if] 
									[#else] 
										--		
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerClearingRecord.remark")}:
								</th>
								<td>
									${sellerClearingRecord.remark}
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									[#if  sellerClearingRecord.clearingStatus == "FAILED" && sellerClearingRecord.valid == true]
										<input type="button" id="singlePay" class="btn btn-info" value="${message("rebate.sellerClearingRecord.singlePay")}" />
									[/#if]
									<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>	
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();
		$("#singlePay").click(function(){
			var d = dialog({
				title: '货款提现',
				content: "确定要对货款进行支付吗?",
				okValue: '确定',
				ok: function () {
					var _that = this;
					_that.content('提交中,请稍等...');
					_that.cancelDisplay =false;
					$.ajax({
						url: "singlePay.jhtml",
						type: "POST",
						data: {
							id:${sellerClearingRecord.id}
						},
						beforeSend:function(){
							$("#singlePay").attr("disabled","disabled");
						},
						dataType: "json",
						cache: false,
						success: function(message) {
							if(message.type == "success"){
									_that.content(message.content);
									setTimeout(function() {
										location.reload();
									}, 1000);
							}else{
								_that.content(message.content);
								$("#singlePay").attr("disabled",false);
							}
						}
					});
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