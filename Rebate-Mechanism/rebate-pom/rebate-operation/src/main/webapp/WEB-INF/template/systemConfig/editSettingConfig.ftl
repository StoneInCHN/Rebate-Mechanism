<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.systemConfig.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/editor/kindeditor-min.js"></script>
<script type="text/javascript" src="${base}/resources/js/editor/lang/zh_CN.js"></script>
<script type="text/javascript">
    KindEditor.ready(function(K) {
				K.create('textarea[name="configValue"]', {
					autoHeightMode : true,
					afterCreate : function() {
						var self = this;
						self.loadPlugin('autoheight');
					},
					cssPath : '${base}/resources/js/editor/plugins/code/prettify.css',
					resizeType : 1,
					allowPreviewEmoticons : false,
					allowImageUpload : true,
					items : [
						'source','fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link'],
					uploadJson: '${base}/console/common/uploadImg.jhtml',
					afterChange: function() {
						this.sync();
					}
				});
			});
</script>
</head>
<body>
	<div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.systemConfig")}</a> </li>
                <li><a href="list.jhtml">${message("rebate.systemConfig.list")}</a></li>
                <li class="active">${message("rebate.systemConfig.edit")}</li>
          </ol>
		  <form id="inputForm" action="updateSettingConfig.jhtml" method="post">
				<input type="hidden" name="id" value="${settingConfig.id}" />
				<input type="hidden" name="configKey" value="${configKey}" />
				<table class="input">
					<tr>
						<th>
							${message("rebate.systemConfig.configValue")}:
						</th>
						<td>
							<textarea name="configValue" cols="50" rows="5">${settingConfig.configValue}</textarea>
						</td>
					</tr>
					<tr>
						<th>
							${message("rebate.systemConfig.remark")}:
						</th>
						<td>
							<textarea name="remark" cols="50" rows="3">${settingConfig.remark}</textarea>
						</td>
					</tr>
					<tr>
						<th>
							&nbsp;
						</th>
						<td>
							<input type="submit" id="submit" class="btn btn-info" value="${message("rebate.common.submit")}" />
							<input type="button" class="btn btn-danger" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
						</td>
					</tr>
				</table>
		</form>
     </div>
</body>
</html>