<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.order.details")}</title>
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
          <li><a ><i class="fa fa-user"></i> ${message("rebate.sallerOrder.info")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.sallerOrder.list")}</a></li>
          <li class="active">${message("rebate.sallerOrder.details")}</li>
      </ol>
	<table class="input">
							<tr>
								<th>
									${message("rebate.order.sn")}:
								</th>
								<td width=500>
									${order.sn}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.endUser.cellPhoneNum")}:
								</th>
								<td>
									${order.endUser.cellPhoneNum}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.endUser.nickName")}:
								</th>
								<td>
									${order.endUser.nickName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.sellerId")}:
								</th>
								<td>
									${order.seller.id}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.sellerName")}:
								</th>
								<td>
									${order.seller.name}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.sellerDiscount")}:
								</th>
								<td>
									${order.seller.discount}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.sellerCategory")}:
								</th>
								<td>
									[#if order.seller.sellerCategory??]
										${order.seller.sellerCategory.categoryName}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.orderTime")}:
								</th>
								<td>
									${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.amount")}:
								</th>
								<td>
									${order.amount}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.paymentType")}:
								</th>
								<td>
									${order.paymentType}
								</td>
							</tr>
							<tr>			
								<td>
									[#if order.paymentChannel??]
										${message("rebate.sellerClearingRecord.paymentChannel."+order.paymentChannel)}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.paymentTime")}:
								</th>
								<td>
									[#if order.paymentTime]
										${order.paymentTime?string("yyyy-MM-dd HH:mm:ss")}
									[/#if]
									
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.status")}:
								</th>
								<td>
									[#if  order.status =="UNPAID"]
										<span class="label label-danger">${message("rebate.orderStatus.UNPAID")}</span>
									[#elseif order.status =="PAID"]
										<span class="label label-warning">${message("rebate.orderStatus.PAID")}</span>
									[#elseif order.status =="FINISHED"]
										<span class="label label-success">${message("rebate.orderStatus.FINISHED")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.seller.income")}:
								</th>
								<td>
									${order.sellerIncome}
								</td>
								<th>
									${message("rebate.order.rebateAmount")}:
								</th>
								<td>
									${order.rebateAmount}
								</td>
								<th>
									${message("rebate.order.discount")}:
								</th>
								<td>
									${order.sellerIncome/order.amount*10}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.userScore")}:
								</th>
								<td>
									${order.userScore}
								</td>	
								<th>
									${message("rebate.order.sellerScore")}:
								</th>
								<td>
									${order.sellerScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.remark")}:
								</th>
								<td>
									${order.remark}
								</td>
							</tr>
							<br/>
							[#if order.evaluate??]
							<tr>
								<th>${message("rebate.order.evaluate.info")}</th>
								<td></td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.endUser.cellPhoneNum")}:
								</th>
								<td>
									${order.endUser.cellPhoneNum}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.endUser.nickName")}:
								</th>
								<td>
									${order.endUser.nickName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.evaluate.score")}:
								</th>
								<td>
									${order.evaluate.score}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.order.evaluate.content")}:
								</th>
								<td>
									${order.evaluate.content}
								</td>
							</tr>
							[#if order.evaluate.evaluateImages??]
							<tr>
								<th>
									${message("rebate.order.evaluate.images")}:
								</th>
								<td>
									  <ul  class="viewer-images clearfix">
									  	[#list order.evaluate.evaluateImages as images]	
											 <li><img class="img-lazy img-rounded" data-original="${images.source}" alt="${images.title}"></li>
										[/#list]
									  </ul>
								</td>
							</tr>
					 	[/#if]
							[#if order.evaluate.sellerReply??]
							<tr>
								<th>
									${message("rebate.order.evaluate.sellerReply")}:
								</th>
								<td>
									${order.evaluate.sellerReply}
								</td>
							</tr>
							[/#if]
					[/#if]
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									[#if  order.status =="UNPAID"]
										<input type="button" id="setPaid" class="btn btn-info" value="${message("rebate.order.status.setPaid")}" />
									[/#if]
									<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>	
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();
		$("#setPaid").click(function(){
			var d = dialog({
				title: '提示',
				content: "确定要将订单状态改为[已支付]状态吗?",
				okValue: '确定',
				ok: function () {
					$.ajax({
						url: "updateStatus.jhtml",
						type: "POST",
						data: {
							id:${order.id},
							status:"PAID"
						},
						beforeSend:function(){
							$("#setPaid").attr("disabled","disabled");
						},
						dataType: "json",
						cache: false,
						success: function(message) {
							if(message.type == "success"){
									alert(message.content);
									setTimeout(function() {
										location.reload();
									}, 1000);
							}else{
								alert(message.content);
								$("#setPaid").attr("disabled",false);
							}
						}
					});
				},
				cancelValue: '取消',
				cancel: function () {}
			});
			d.show();
		});
	})
</script>	
</body>
</html>