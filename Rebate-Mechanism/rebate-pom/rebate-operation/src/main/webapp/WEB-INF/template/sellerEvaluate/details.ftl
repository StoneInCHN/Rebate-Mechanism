<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerEvaluate.details")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
</script>
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerEvaluate")}</a> </li>
                <li><a href="list.jhtml">${message("rebate.sellerEvaluate.list")}</a></li>
                <li class="active">${message("rebate.sellerEvaluate.details")}</li>
          </ol>
			<table class="input tabContent">
							<tr>
								<th>
									${message("rebate.sellerEvaluate.endUser")}:
								</th>
								<td>
									[#if  sellerEvaluate.endUser??]
										${sellerEvaluate.endUser.nickName}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerEvaluate.seller")}:
								</th>
								<td>
									[#if  sellerEvaluate.seller??]
										${sellerEvaluate.seller.name}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerEvaluate.score")}:
								</th>
								<td>
									[#if  sellerEvaluate.score??]
										${sellerEvaluate.score}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerEvaluate.content")}:
								</th>
								<td>
									[#if  sellerEvaluate.content??]
										${sellerEvaluate.content}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerEvaluate.sellerReply")}:
								</th>
								<td>
									[#if  sellerEvaluate.sellerReply??]
										${sellerEvaluate.sellerReply}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerEvaluate.status")}:
								</th>
								<td>
									[#if  sellerEvaluate.status =="ACITVE"]
										${message("rebate.sellerEvaluate.status.ACITVE")}
									[#elseif sellerEvaluate.status =="INACTIVE"]
										${message("rebate.sellerEvaluate.status.INACTIVE")}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.sellerEvaluate.evaluateImages")}:
								</th>
								<td>
									<!-- a block container is required -->
									  <ul  class="viewer-images clearfix">
									  	[#list sellerEvaluate.evaluateImages as images]	
											 <li><img class="img-lazy img-rounded" data-original="${images.source}" alt="${images.title}"></li>
										[/#list]
									  </ul>
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									[#if  sellerEvaluate.status =="ACITVE"]
										<input id="inActiveButton" type="button" class="btn btn-danger" value="禁用" />
									[#elseif sellerEvaluate.status =="INACTIVE"]
										<input id="activeButton" type="button" class="btn btn-info" value="启用" />
									[/#if]
									<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>  
     </div>
 <script type="text/javascript" src="${base}/resources/js/jquery.js"></script>    
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();
		var $activeButton = $("#activeButton");
		var $inActiveButton = $("#inActiveButton");
		
		// 启用
		$activeButton.click( function() {
			$.dialog({
				type: "warn",
				content: message("admin.dialog.activeConfirm"),
				ok: message("admin.dialog.ok"),
				cancel: message("admin.dialog.cancel"),
				onOk: function() {
					$.ajax({
						url: "active.jhtml",
						type: "POST",
						data: {ids:${sellerEvaluate.id}},
						dataType: "json",
						cache: false,
						success: function(message) {
							if (message.type == "success") {
								setTimeout(function() {
									location.reload(true);
								}, 1000);
							}
						}
					});
				}
			});
		});
		
		// 禁用
		$inActiveButton.click( function() {
			$.dialog({
				type: "warn",
				content: message("admin.dialog.inActiveConfirm"),
				ok: message("admin.dialog.ok"),
				cancel: message("admin.dialog.cancel"),
				onOk: function() {
					$.ajax({
						url: "inActive.jhtml",
						type: "POST",
						data: {ids:${sellerEvaluate.id}},
						dataType: "json",
						cache: false,
						success: function(message) {
							if (message.type == "success") {
								setTimeout(function() {
									location.reload(true);
								}, 1000);
							}
						}
					});
				}
			});
		});
		
	})
</script>
</body>
</html>