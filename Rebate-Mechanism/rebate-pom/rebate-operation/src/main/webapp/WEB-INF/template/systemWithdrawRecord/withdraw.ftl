<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.leScoreRecord.withdraw.details")}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
</head>
<body>
	  <ol class="breadcrumb">
          <li><a ><i class="fa fa-user"></i> 系统提现</a> </li>
          <li><a href="list.jhtml">系统提现列表</a></li>
          <li class="active">详情</li>
      </ol>
      <div class="content-search accordion-group">
             <div class="accordion-heading" role="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  请先核对提现信息
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="accordion-inner">
						<table class="queryFiled">
							<tr>
								<th>
									提现人
								</th>
								<td>
									<input type="text" name="operator" class="text" value="${admin.username}" maxlength="20" disabled="disabled"/>
								</td>
								<th>
									提现人手机号
								</th>
								<td>
									<input type="text" name="cellPhoneNum" class="text" value="${admin.cellPhoneNum}" maxlength="20" disabled="disabled"/>
								</td>
								<th>
									银行卡号
								</th>
								<td>
									<input type="text" name="cardNum" class="text" value="${defaultCard.cardNum}" maxlength="20" disabled="disabled"/>
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
      <table class="input">
							<tr>
								<th>
									请输入提现金额:
								</th>
								<td>
									<input type="text" id="amount" name="amount" class="text" maxlength="6" />
								</td>
							</tr>
							<th>
								&nbsp;
							</th>
							<td>
								<input type="button" id="singlePay" class="btn btn-info" value="${message("rebate.sellerClearingRecord.singlePay")} onclick="location.href='add.jhtml'" />
								<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='add.jhtml'" />
							</td>
						</tr>
						</table>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>													
</body>
</html>