<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.operationLog.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog-plus.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.area")}</a> </li>
                <li class="active">${message("rebate.area.list")}</li>
          </ol>
 		 <div class="button-group">
 		 	<a  id="areaAddButton" class="btn btn-default"><i class="fa fa-plus"></i>&nbsp;&nbsp;${message("rebate.common.add")}</button>
            <a  id="refreshButton" class="btn btn-default"><i class="fa fa-refresh"></i><span>${message("rebate.common.refresh")}</span></a>
         </div>
         <div style="margin-top:60px">
         			<div class="panel panel-info">
					  <div class="panel-heading">热门城市</div>
					  <div class="panel-body">
					  	<ul class="hot-city clearfix">
					  		[#list hotCitys as hotCity]
					  			<li class="hot-city-item"><a data-id="${hotCity.id}"><span class="label label-info">${hotCity.cityName}</span><i class="fa fa-times" title="${message("rebate.common.delete")}"></i></a></li>
					  		[/#list]
					  		<li><a id="addHotCity" class="btn btn-default" title="${message("rebate.common.add")}"><i class="fa fa-plus" ></i></a></li>
					  	</ul>
					  </div>
					</div>
        			<table id="listTable" class="table table-striped table-bordered table-hover list">
											<tr>
												<th colspan="5" class="green" style="text-align: center;">
													[#if parent??]${message("rebate.area.parent")} - ${parent.name}[#else]${message("rebate.area.root")}[/#if]
												</th>
											</tr>
											[#list areas?chunk(5, "") as row]
												<tr>
													[#list row as area]
														[#if area?has_content]
															<td>
																<a href="list.jhtml?parentId=${area.id}" title="${message("rebate.common.view")}">${area.name}</a>
																<a href="edit.jhtml?id=${area.id}">[${message("rebate.common.edit")}]</a>
																<a href="javascript:;" class="delete" val="${area.id}">[${message("rebate.common.delete")}]</a>
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
														${message("rebate.area.emptyChildren")} <a href="add.jhtml[#if parent??]?parentId=${parent.id}[/#if]" style="color: gray">${message("rebate.common.add")}</a>
													</td>
												</tr>
											[/#if]
										</table>
	<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/custom.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $delete = $("#listTable a.delete");
	var $areaAddButton = $("#areaAddButton");
	var $addHotCity = $("#addHotCity");
	var $removeHotCity = $(".hot-city i.fa-times");
	var $hotCityItem = $(".hot-city-item a");
	
	[@flash_message /]
	
	$areaAddButton.click(function(){
		location.href="add.jhtml[#if parent??]?parentId=${parent.id}[/#if]";
	});

	$hotCityItem.mousemove(function(){
	 	var $this = $(this);
	 	$this.find("i").css("display","block");
	}).mouseout(function(){
		var $this = $(this);
	  	$this.find("i").css("display","none");
	});

	// 删除
	$delete.click(function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("rebate.dialog.deleteConfirm")}",
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

	$addHotCity.click(function(){
	   window.dialog({
           id: 'hotCity-dialog',
           title: '添加热门城市',
           url:'addHotCity.jhtml',
           quickClose: true,
           okValue: '确定',
		   ok: function () {
				this.title('提交中…');
				var $hotCityFrame = $(window.frames["hotCity-dialog"].document);
				var data = {
					areaId:$hotCityFrame.find('#areaId').val(),
					cityOrder:$hotCityFrame.find('#cityOrder').val()
				}
				$.ajax({
					url: "saveHotCity.jhtml",
					type: "POST",
					data: data,
					dataType: "json",
					cache: false,
					success: function(res) {
						if(res.type == "success"){
							alert("添加成功");
							 window.location.reload()
						}
					}
				});
			},
			cancelValue: '取消'
        }).show(this);
		return false;
	});
	
	$removeHotCity.click(function(){
		var $this = $(this);
		var cityName= $this.prev().html();
		 window.dialog({
           id: 'hotCity-dialog',
           title: '删除热门城市',
           content:'你确定要从热门城市中删除 [ ' + cityName + ' ] 吗 ?',
           quickClose: true,
           okValue: '确定',
		   ok: function () {
				this.title('提交中…');
				$.ajax({
					url: "deleteHotCity.jhtml",
					type: "POST",
					data: {id: $this.parents("a").attr("data-id")},
					dataType: "json",
					cache: false,
					success: function(res) {
						if(res.type == "success"){
							$this.parents("li").remove();
						}
					}
				});
			},
			cancelValue: '取消'
        }).show(this);
        return false;
	});
	
});
</script>
</body>
</html>