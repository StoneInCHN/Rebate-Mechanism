<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.agent.add")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog-plus.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script type="text/javascript">

$().ready(function() {
	var $areaId = $("#areaId");
	var $selectEndUser = $("#selectEndUser");
	// 地区选择
	$areaId.lSelect({
		url: "${base}/console/common/area.jhtml"
	});
	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			areaId: {
				required: true
			},
			agencyLevel: {
				required: true
			}
		}
	});
	$selectEndUser.click(function(){
		window.dialog({
           id: 'selectEndUser-dialog',
           title: '选择用户',
           url:'${base}/console/common/selectedEndUser4Agent.jhtml',
           quickClose: true,
           okValue: '确定',
		   ok: function () {
				var $endUserFrame = $(window.frames["selectEndUser-dialog"].document);				
				var $ids = $endUserFrame.find(':input[name="ids"]:enabled:checked');
				if($ids && $ids.length >1){
					alert("只能选择一个用户");
					return false;
				}else if($ids && $ids.length ==1){
					var userName = $($ids[0]).parent().next().find("span").text();
					$("#endUserId").val($($ids[0]).val());
					$("#endUserName").val(userName);
				}else{
					alert("请选择一个用户");
					return false;
				}
			},
			cancelValue: '取消'
        }).show(this);
		return false;
	})
});
</script>
</head>
<body>
 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.agent")}</a> </li>
                <li><a href="#">${message("rebate.agent.list")}</a></li>
                <li class="active">${message("rebate.agent.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.agent.area")}:
								</th>
								<td>
									<input type="hidden" id="areaId"  name="areaId"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.agent.endUser")}:
								</th>
								<td>
									<input type="hidden" id="endUserId"  class="text" name="endUserId"/>
									<input type="text" id="endUserName" readonly class="text" maxlength="20" />
									<a type="btn btn-default" id="selectEndUser">选择用户</a>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.agent.agencyLevel")}:
								</th>
								<td>
									<select name="agencyLevel">
										<option value="PROVINCE">${message("rebate.agent.agencyLevel.PROVINCE")}</option>
										<option value="CITY">${message("rebate.agent.agencyLevel.CITY")}</option>
										<option value="COUNTY">${message("rebate.agent.agencyLevel.COUNTY")}</option>
									</select>
								</td>
							</tr>
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