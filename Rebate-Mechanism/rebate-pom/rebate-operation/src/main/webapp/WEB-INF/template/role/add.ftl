<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("csh.role.add")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<style type="text/css">
.authorities label {
	min-width: 120px;
	_width: 120px;
	display: block;
	float: left;
	padding-right: 4px;
	_white-space: nowrap;
}
.authorities th a{
	color:#666;
	font-weight:bold;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $selectAll = $("#inputForm .selectAll");
	
	
	$selectAll.click(function() {
		var $this = $(this);
		var $thisCheckbox = $this.closest("tr").find(":checkbox");
		if ($thisCheckbox.filter(":checked").size() > 0) {
			$thisCheckbox.prop("checked", false);
		} else {
			$thisCheckbox.prop("checked", true);
		}
		return false;
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name:{
				required: true
			},
			authorities: "required",
			description:{
				maxlength:200
			}
		}
	
	});
	
});
</script>
</head>
<body>
		<div class="mainbar">
		<div class="page-head">
			<div class="bread-crumb">
				<a ><i class="fa fa-user"></i> ${message("csh.main.role")}</a> 
				<span class="divider">/</span> 
				<a href="list.jhtml" ><i class="fa fa-list"></i>${message("csh.role.list")}</a>
				<span class="divider">/</span> 
				<span  class="bread-current"><i class="fa fa-plus"></i>${message("csh.role.add")}</span>
			</div>
			<div class="clearfix"></div>
		</div>
		<div class="matter">
        <div class="container">
          <div class="row">
            <div class="col-md-12">
              <div class="widget wgreen">
                <div class="widget-head">
                  <div class="pull-left"><i class="fa fa-plus"></i>${message("csh.role.add")}</div>
                  <div class="clearfix"></div>
                </div>
                <div class="widget-content">
                  <div class="padd">
          				<form id="inputForm" action="save.jhtml" method="post">
							<table class="input">
								<tr>
									<th>
										<span class="requiredField">*</span>${message("csh.role.name")}:
									</th>
									<td>
										<input type="text" name="name" class="text" maxlength="200" />
									</td>
								</tr>
								<tr>
									<th>
										${message("csh.role.description")}:
									</th>
									<td>
										<textarea  name="description" class="text" maxlength="200"></textarea>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>
								</tr>
								<tr class="authorities">
									<th>
										<a href="javascript:;" class="selectAll" title="${message("csh.role.selectAll")}">${message("csh.main.systemNav")}</a>
									</th>
									<td>
										<span class="fieldSet">
											<label>
												<input type="checkbox" name="authorities" value="admin:admin" /><span>${message("csh.role.admin")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:role" /><span>${message("csh.role.role")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:account" /><span>${message("csh.account.settingGroup")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:apply" /><span>${message("csh.main.apply")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:area" /><span>${message("csh.main.area")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:tenantAccount" /><span>${message("csh.main.tenantAccount")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:tenantInfo" /><span>${message("csh.main.tenantInfo")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:deviceType" /><span>${message("csh.main.deviceType")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:deviceInfo" /><span>${message("csh.main.deviceInfo")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:advertisement" /><span>${message("csh.main.advertisement")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:distributor" /><span>${message("csh.main.distributor")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:vehicleBrand" /><span>${message("csh.main.vehicleBrand")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:vehicleLine" /><span>${message("csh.main.vehicleLine")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:vehicleBrandDetail" /><span>${message("csh.main.vehicleBrandDetail")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:serviceCategory" /><span>${message("csh.main.serviceCategory")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:feedBack" /><span>${message("csh.main.feedBack")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:vehicle" /><span>${message("csh.main.vehicle")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:endUser" /><span>${message("csh.main.endUser")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:clearingRecord" /><span>${message("csh.main.clearingRecord")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:carServiceRecord" /><span>${message("csh.main.carServiceRecord")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:walletRecord" /><span>${message("csh.main.walletRecord")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:deviceProvide4distributor" /><span>${message("csh.main.deviceInfo.list4distributor")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:reportUserReg" /><span>${message("csh.report.reportUserReg")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:reportDeviceBind" /><span>${message("csh.report.reportDeviceBind")}</span>
											</label>
											<label>
												<input type="checkbox" name="authorities" value="admin:faultCode" /><span>${message("csh.main.faultCode")}</span>
											</label>
										</span>
									</td>
								</tr>
								<tr>
									<th>
										&nbsp;
									</th>
									<td>
										<input type="submit" class="button" value="${message("csh.common.submit")}" />
										<input type="button" class="button" value="${message("csh.common.back")}" onclick="location.href='list.jhtml'" />
									</td>
								</tr>
							</table>
						</form>
                  </div>
                </div>
              </div>  
            </div>
          </div>
        </div>
	   </div>
	</div>
	<script type="text/javascript" src="${base}/resources/js/custom.js"></script>
</body>
</html>
