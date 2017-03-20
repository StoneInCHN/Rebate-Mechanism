<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("csh.admin.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/list.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
<div class="mainbar">
				<div class="page-head">
					<div class="bread-crumb">
						<a ><i class="fa fa-user"></i> ${message("csh.main.area")}</a> 
						<span class="divider">/</span> 
						<span  class="bread-current"><i class="fa fa-list"></i>${message("csh.area.list")}(${message("csh.page.total", page.total)})</span>
					</div>
					<div class="clearfix"></div>
				</div>
			
			<form id="listForm" action="list.jhtml" method="get">
				  <div class="container operation" style="margin-bottom: 5px;" >
					<div class="row">
						  <div class="col-xs-12 col-md-12 col-lg-12">
										<div class="btn-group operationButton">
										  <button type="button" id="areaAddButton" class="btn btn-default"><i class="fa fa-plus"></i>&nbsp;&nbsp;${message("csh.common.add")}</button>
										</div>
										<div class="btn-group operationButton">
										  <button type="button" id="refreshButton" class="btn btn-default"><i class="fa fa-refresh"></i>&nbsp;&nbsp;${message("csh.common.refresh")}</button>
										</div>
						  </div>
					</div>
				</div>
				
				<div class="matter">
					<div class="container">
						<div class="row">
			              <div class="col-md-12">
			                <div class="widget">
									 <div class="widget-head">
						                  <div class="pull-left"><i class="fa fa-list"></i>${message("csh.area.list")}</div>
						                  <div class="clearfix"></div>
						              </div>
						              <div class="widget-content">
										<table id="listTable" class="table table-striped table-bordered table-hover list">
											<tr>
												<th colspan="5" class="green" style="text-align: center;">
													[#if parent??]${message("csh.area.parent")} - ${parent.name}[#else]${message("csh.area.root")}[/#if]
												</th>
											</tr>
											[#list areas?chunk(5, "") as row]
												<tr>
													[#list row as area]
														[#if area?has_content]
															<td>
																<a href="list.jhtml?parentId=${area.id}" title="${message("csh.common.view")}">${area.name}</a>
																<a href="edit.jhtml?id=${area.id}">[${message("csh.common.edit")}]</a>
																<a href="javascript:;" class="delete" val="${area.id}">[${message("csh.common.delete")}]</a>
															</td>
														[#else]
															<td>
																&nbsp;
															</td>
														[/#if]
													[/#list]
												</tr>
											[/#list]
											[#if !areas?has_content]
												<tr>
													<td colspan="5" style="text-align: center; color: red;">
														${message("csh.area.emptyChildren")} <a href="add.jhtml[#if parent??]?parentId=${parent.id}[/#if]" style="color: gray">${message("csh.common.add")}</a>
													</td>
												</tr>
											[/#if]
										</table>
									</div>
								</div>
							</div>
						</div>
					 </div>
				</div>
			</form>
</div>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/custom.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $delete = $("#listTable a.delete");
	var $areaAddButton = $("#areaAddButton");
	[@flash_message /]
	
	$areaAddButton.click(function(){
		location.href="add.jhtml[#if parent??]?parentId=${parent.id}[/#if]";
	});

	// 删除
	$delete.click(function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("csh.ialog.deleteConfirm")}",
			onOk: function() {
				$.ajax({
					url: "delete.jhtml",
					type: "POST",
					data: {id: $this.attr("val")},
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$this.parent().html("&nbsp;");
						}
					}
				});
			}
		});
		return false;
	});
	
});
</script>
</body>
</html>