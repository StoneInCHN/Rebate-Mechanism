<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lSelect.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $areaId = $("#areaId");
	// 地区选择
	$areaId.lSelect({
		url: "${base}/console/common/area.jhtml"
	});
	
});
</script>
<table class="input" style="margin-bottom:50px;">
	<tr>
		<th>
			<span class="requiredField">*</span>${message("rebate.area.HotCity.areaId")}:
		</th>
		<td>
			<input type="hidden" id="areaId"  name="areaId"/>
		</td>
	</tr>
	<tr>
		<th>
			${message("rebate.common.order")}:
		</th>
		<td>
			<input type="text" id="cityOrder" name="cityOrder" class="text" maxlength="9" />
		</td>
	</tr>
</table> 
