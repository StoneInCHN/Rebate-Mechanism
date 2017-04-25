<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.messageInfo.add")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.placeholder.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/editor/kindeditor-min.js"></script>
<script type="text/javascript" src="${base}/resources/js/editor/lang/zh_CN.js"></script>
<script type="text/javascript">
    KindEditor.ready(function(K) {
				K.create('textarea[name="content"]', {
					autoHeightMode : true,
					afterCreate : function() {
						var self = this;
						self.loadPlugin('autoheight');
					},
					cssPath : '${base}/resources/js/editor/plugins/code/prettify.css',
					resizeType : 1,
					allowPreviewEmoticons : false,
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'image','link','preview'],
					afterChange: function() {
						this.sync();
					}
				});
			});
$().ready(function() {

	var $inputForm = $("#inputForm");
	// 表单验证
	$inputForm.validate({
		rules: {
			title: {
				required: true,
				maxlength: 100
			},
			configOrder: {
				number:true
			},
			isEnabled: {
				required: true
			},
			content: {
				required: true,
			}
		}
	});

});
</script>
</head>
<body>
 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.messageInfo")}</a> </li>
                <li><a href="#">${message("rebate.messageInfo.list")}</a></li>
                <li class="active">${message("rebate.messageInfo.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.messageInfo.title")}:
								</th>
								<td>
									<input type="text" name="title" class="text" maxlength="100" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.messageInfo.configOrder")}:
								</th>
								<td>
									<input type="text" id="configOrder" name="configOrder" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.messageInfo.isEnabled")}:
								</th>
								<td>
									<input type="radio" value="true" name="isEnabled" checked="checked" />${message("rebate.common.true")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="false" name="isEnabled" />${message("rebate.common.false")}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.messageInfo.content")}:
								</th>
								<td>
									<textarea  name="content" rows="6" cols="60"></textarea>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.messageInfo.remark")}:
								</th>
								<td>
									<textarea  name="remark" rows="3" cols="60"></textarea>
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