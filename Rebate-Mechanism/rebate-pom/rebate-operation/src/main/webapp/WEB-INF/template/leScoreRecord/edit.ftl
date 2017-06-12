<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.leScoreRecord.withdraw.edit")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
</head>
<body>
	 <div class="content">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.leScoreRecord")}</a> </li>
                <li><a href="#">${message("rebate.leScoreRecord.withdraw.list")}</a></li>
                <li class="active">${message("rebate.leScoreRecord.withdraw.edit")}</li>
          </ol>
		  <form id="inputForm" action="update.jhtml" method="post">
						<input type="hidden" name="id" value="${leScoreRecord.id}" />
						<table class="input">
							<tr>
								<th>
									${message("rebate.leScoreRecord.endUser.userName")}:
								</th>
								<td>
									[#if  leScoreRecord.endUser??]
										${leScoreRecord.endUser.nickName}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.endUser.cellPhoneNum")}:
								</th>
								<td>
									[#if  leScoreRecord.endUser??]
										${leScoreRecord.endUser.cellPhoneNum}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.amount")}:
								</th>
								<td>
									[#if  leScoreRecord.amount??]
										${leScoreRecord.amount * -1}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.userCurLeScore")}:
								</th>
								<td>
									${leScoreRecord.userCurLeScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.motivateLeScore")}:
								</th>
								<td>
									${leScoreRecord.motivateLeScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.incomeLeScore")}:
								</th>
								<td>
									${leScoreRecord.incomeLeScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.agentLeScore")}:
								</th>
								<td>
									${leScoreRecord.agentLeScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.recommender")}:
								</th>
								<td>
									[#if leScoreRecord.recommender??]
									${leScoreRecord.recommender}
									[#else]
									--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.recommenderPhoto")}:
								</th>
								<td>
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original=="${leScoreRecord.recommenderPhoto}" alt="${message("rebate.leScoreRecord.recommenderPhoto")}"></li>
									</ul>	
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.common.createDate")}:
								</th>
								<td>
									${leScoreRecord.createDate}
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("rebate.leScoreRecord.withdrawStatus")}:
								</th>
								<td>
									<select  name="withdrawStatus">
										<option value="">${message("rebate.common.auditStatus.select")}</option>
										<option value="AUDIT_PASSED">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_PASSED")}</option>
										<option value="AUDIT_FAILED">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_FAILED")}</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.remark")}:
								</th>
								<td>
									<textarea  name="remark" rows="6" cols="60">${leScoreRecord.remark}</textarea>
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
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>

<script type="text/javascript">
$(function() {

	$('.viewer-images').viewer();
	$('.img-lazy').lazyload();
	
	var $inputForm = $("#inputForm");
	var $submit = $("#submit");	
	
	// 表单验证
	$inputForm.validate({
		rules: {
			withdrawStatus: "required",
			remark: "required"
		},
		submitHandler:function(form){
			$submit.attr("disabled",true);
			$.ajax({
				url:$inputForm.attr("action"),
				type:"POST",
				data:$inputForm.serialize(),
				dataType:"json",
				cache:false,
				success:function(message){
					alert(message.content);
					if(message.type == "success"){
						setTimeout("location.href='details.jhtml?id=${leScoreRecord.id}'",1000);
					}else{
						$submit.attr("disabled",false);
					}
					
				}
			});
		}
	});


});
</script>     
</body>
</html>