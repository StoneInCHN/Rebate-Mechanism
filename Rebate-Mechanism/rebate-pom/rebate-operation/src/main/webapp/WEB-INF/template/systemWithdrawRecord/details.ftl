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
          <li><a ><i class="fa fa-user"></i> 平台提现</a> </li>
          <li><a href="list.jhtml">平台提现列表</a></li>
          <li class="active">详情</li>
      </ol>
      <table class="input">
							<tr>
								<th>
									提现金额:
								</th>
								<td>
									[#if  record.amount??]
										${record.amount}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									提现人:
								</th>
								<td>
									[#if  record.operator??]
										${record.operator}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									提现人手机号:
								</th>
								<td>
									[#if  record.cellPhoneNum??]
										${record.cellPhoneNum}
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									提现返回信息:
								</th>
								<td>
									[#if  record.withdrawMsg??]
										${record.withdrawMsg}
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									提现状态:
								</th>
								<td>
									[#if  record.status =="PROCESSING"]
										<span class="label label-info">${message("rebate.leScoreRecord.withdraw.status.PROCESSING")}</span>
									[#elseif record.status =="SUCCESS"]
										<span class="label label-success">${message("rebate.leScoreRecord.withdraw.status.SUCCESS")}</span>
									[#elseif record.status =="FAILED"]
										<span class="label label-warning">${message("rebate.leScoreRecord.withdraw.status.FAILED")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>	
							<tr>
								<th>
									是否已提现:
								</th>
								<td>
									[#if  record.isWithdraw ?? ]
										[#if  record.isWithdraw ]
											<span class="label label-success">${message("rebate.common.true")}</span>
										[#else]
											<span class="label label-danger">${message("rebate.common.false")}</span>
										[/#if]
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									提现手续费:
								</th>
								<td>
									[#if  record.handlingCharge??]
										${record.handlingCharge}
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									提现银行卡ID:
								</th>
								<td>
									[#if  record.bankCardId??]
										${record.bankCardId}
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									银行卡号:
								</th>
								<td>
									[#if  record.cardNum??]
										${record.cardNum}
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									交易批次号:
								</th>
								<td>
									[#if  record.reqSn??]
										${record.reqSn}
									[#else]
										--
									[/#if]
								</td>
							</tr>		
							<tr>
								<th>
									备注:
								</th>
								<td>
									[#if  record.remark??]
										${record.remark}
									[#else]
										--
									[/#if]
								</td>
							</tr>																																															
							<th>
								&nbsp;
							</th>
							<td>
								<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
							</td>
						</tr>
						</table>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/input.js"></script>													
</body>
</html>