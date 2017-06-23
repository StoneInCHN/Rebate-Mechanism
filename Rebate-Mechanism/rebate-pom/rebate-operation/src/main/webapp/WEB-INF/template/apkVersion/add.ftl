<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.apkVersion.add")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js">
</script><script type="text/javascript" src="${base}/resources/js/jquery.form.js"></script>

<script type="text/javascript">
$().ready(function() {
	var $file = $("#file");
	var $inputForm = $("#inputForm");
	var $submitBtn = $("#submitBtn");
	$("#versionName").focus(function(){
		$("#versionName").attr("readonly","readonly");
	});
	$file.change(function(){
		if($file.val() != null && $file.val().substr($file.val().length-4,4) == ".apk"){
			$inputForm.attr("action","getApkInfo.jhtml");
			$inputForm.attr("target","addApkIfram");
			$("#versionName").attr("readonly","readonly");
			$("#updateContent").attr("readonly","readonly");
			$submitBtn.attr("disabled","disabled");
			$inputForm.submit();
			$("#addApkIfram").load(function(){
				var body = $(window.frames['addApkIfram'].document.body);
				var data = eval('(' + body[0].textContent + ')');  
				if(data != null){
						$("#versionName").val(data.versionName);
						$("#versionCode").val(data.versionCode);
						$submitBtn.attr("disabled",false);
						$("#updateContent").removeAttr("readonly");
				}
		     });
		}     
	});	
	$submitBtn.click( function() {
			$("#versionName").removeAttr("readonly");
			$inputForm.attr("action", "save.jhtml");
			$inputForm.attr("target", "_self");
	});
	var $loadingBar = $(".loadingBar");
	// 表单验证
	$inputForm.validate({
		rules: {
			versionName: {
				required: true,
				remote: {
					url: "checkVersion.jhtml",
					cache: false
				}
			},
			versionCode:{
				required: true
			},
			isForced:{
				required: true
			},
			file: {
				required: true,
				nameEndWith:".apk"
			},
			updateContent: {
				required: true
			}
		},
		messages: {
			versionName: {
				remote: "${message("apkVersion.version.validate.exist")}"
			},
			file:{
				nameEndWith:"${message("apkVersion.file.name.error")}"
			}
		}
			
	});
});
</script>
</head>
<body>
 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.Version")}</a> </li>
                <li><a href="#">${message("rebate.apkVersion.list")}</a></li>
                <li class="active">${message("rebate.apkVersion.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.apkVersion.name")}:
								</th>
								<td>
									<input type="text" id = "versionName" name="versionName" class="text" maxlength="20" readonly = readonly/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.apkVersion.apkPath")}:
								</th>
								<td>
									<input type="file" id = "file" name="file"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.apkVersion.code")}:
								</th>
								<td>
									<input type="text" id = "versionCode" name="versionCode" class="text" maxlength="20"  readonly = readonly/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.apkVersion.forceUpdate")}:
								</th>
								<td>
									<input type="radio" value="true" name="isForced" checked="checked" />${message("rebate.common.true")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="false" name="isForced" />${message("rebate.common.false")}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.apkVersion.updateContent")}:
								</th>
								<td>
									<textarea  id = "updateContent" name="updateContent" rows="6" cols="60" readonly = readonly></textarea>
								</td>
							</tr>
							<tr>
								<td  colspan="2">
									<span class="loadingBar" style="margin-left:120px;display:none"></span>
								</td>
							</tr>
						</table>
						<table class="input">
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" id="submitBtn" class="button" value="${message("rebate.common.submit")}" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>                                     
                     </form>
     </div>
</body>
</html>
<iframe id='addApkIfram' name='addApkIfram' style="display:none"/>