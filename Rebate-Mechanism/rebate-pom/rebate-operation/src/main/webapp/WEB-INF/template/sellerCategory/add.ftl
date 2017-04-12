<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.sellerCategory.add")}</title>
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
	var $inputForm = $("#inputForm");
	var $loadingBar = $(".loadingBar");
	// 表单验证
	$inputForm.validate({
		rules: {
			categoryName: {
				required: true
			},
			categorOrder:{
				required: true,
				digits:true
			},
			categoryPic:{
				required: true
			},
			isActive: {
				required: true
			}
		},
		submitHandler:function(form){
			$inputForm.ajaxSubmit({
			      	dataType:"json",
			       	beforeSubmit:function(){
			       		$('input[type="submit"]').attr("disabled","disabled");
			       		$loadingBar.show();
			       	},
			       	success:function(result){
			       		if(result.type == "error"){
			       			$loadingBar.hide();
			       			alert(result.content);
			       			$('input[type="submit"]').attr("disabled",false);
			       		}else{
			       			$loadingBar.hide();
			       			alert(result.content);
			       			location.href="list.jhtml";
			       		}
			       		
			       }
			});
		}
			
	});
});
</script>
</head>
<body>
 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.sellerCategory")}</a> </li>
                <li><a href="#">${message("rebate.sellerCategory.list")}</a></li>
                <li class="active">${message("rebate.sellerCategory.add")}</li>
          </ol>
		 <form id="inputForm" action="save.jhtml" method="post" class="form-horizontal" role="form">
                     	<table class="input tabContent">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.sellerCategory.categoryName")}:
								</th>
								<td>
									<input type="text" name="categoryName" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.sellerCategory.categoryPic")}:
								</th>
								<td>
									<input type="file" name="categoryPic"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.sellerCategory.categorOrder")}:
								</th>
								<td>
									<input type="text" name="categorOrder" class="text" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.sellerCategory.isActive")}:
								</th>
								<td>
									<input type="radio" value="true" name="isActive" checked="checked" />${message("rebate.common.true")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" value="false" name="isActive" />${message("rebate.common.false")}
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
									<input type="submit" class="button" value="${message("rebate.common.submit")}" />
									<input type="button" class="button" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>                                     
                     </form>
     </div>
</body>
</html>