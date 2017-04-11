<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.leScoreRecord.details")}</title>
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
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.leScoreRecord")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.leScoreRecord.list")}</a></li>
          <li class="active">${message("rebate.leScoreRecord.details")}</li>
      </ol>
      <table class="input">
							<tr>
								<th>
									${message("rebate.leScoreRecord.seller")}:
								</th>
								<td>
									[#if  leScoreRecord.seller??]
										${leScoreRecord.seller.name}
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.endUser")}:
								</th>
								<td>
									[#if  leScoreRecord.endUser??]
										${leScoreRecord.endUser.userName}
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
										${leScoreRecord.amount}
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
									${message("rebate.leScoreRecord.recommender")}:
								</th>
								<td>
									${leScoreRecord.recommender}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.recommenderPhoto")}:
								</th>
								<td>
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" src="${leScoreRecord.recommenderPhoto}" alt="${message("rebate.leScoreRecord.recommenderPhoto")}"></li>
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
									${message("rebate.leScoreRecord.withdrawStatus")}:
								</th>
								<td>
									[#if  leScoreRecord.withdrawStatus =="AUDIT_WAITING"]
										<span class="label label-info">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_WAITING")}</span>
									[#elseif leScoreRecord.withdrawStatus =="AUDIT_PASSED"]
										<span class="label label-success">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_PASSED")}</span>
									[#elseif leScoreRecord.withdrawStatus =="AUDIT_FAILED"]
										<span class="label label-warning">${message("rebate.leScoreRecord.withdrawStatus.AUDIT_FAILED")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.leScoreRecord.remark")}:
								</th>
								<td>
									${leScoreRecord.remark}
								</td>
							</tr>
						</table>				
</body>
</html>