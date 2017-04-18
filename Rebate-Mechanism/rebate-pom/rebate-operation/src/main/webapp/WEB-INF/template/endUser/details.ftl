<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.endUser.details")}</title>
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
          <li><a ><i class="fa fa-user"></i> ${message("rebate.main.endUserManager")}</a> </li>
          <li><a href="list.jhtml">${message("rebate.endUser.list")}</a></li>
          <li class="active">${message("rebate.endUser.details")}</li>
      </ol>
	<table class="input">
							<tr>
								<th>
									${message("rebate.endUser.nickName")}:
								</th>
								<td>
									${endUser.nickName}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.mobile")}:
								</th>
								<td>
									${endUser.cellPhoneNum}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.regDate")}:
								</th>
								<td>
									${endUser.createDate?string("yyyy-MM-dd HH:mm:ss")}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.status")}:
								</th>
								<td>
									[#if  endUser.accountStatus =="ACTIVED"]
										<span class="label label-success">${message("rebate.common.accountStatus.ACTIVED")}</span>
									[#elseif endUser.accountStatus =="LOCKED"]
										<span class="label label-danger">${message("rebate.common.accountStatus.LOCKED")}</span>
									[#elseif endUser.accountStatus =="DELETE"]
										<span class="label label-warning">${message("rebate.common.accountStatus.DELETE")}</span>
									[#else]
										--
									[/#if]
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.area")}:
								</th>
								<td>
									${endUser.area}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.address")}:
								</th>
								<td>
									${endUser.address}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.userPhoto")}:
								</th>
								<td>
									<ul  class="viewer-images clearfix">
										 <li><img class="img-lazy img-rounded" data-original="${endUser.userPhoto}" alt="${message("rebate.endUser.userPhoto")}"></li>
									  </ul>
								</td>
							</tr>
							[#if endUser.agent??]
								<tr>
									<th>
										${message("rebate.endUser.agent.agencyLevel")}:
									</th>
									[#if  endUser.agent.agencyLevel =="PROVINCE"]
									<td>
										<span class="label label-success">${message("rebate.endUser.agent.agencyLevel.PROVINCE")}</span>
									</td>
									[#elseif endUser.agent.agencyLevel =="CITY"]
									<td>
										<span class="label label-danger">${message("rebate.endUser.agent.agencyLevel.CITY")}</span>
									</td>
									[#elseif endUser.agent.agencyLevel =="COUNTY"]
									<td>
										<span class="label label-warning">${message("rebate.endUser.agent.agencyLevel.COUNTY")}</span>
									</td>
									[/#if]
								</tr>
								<tr>
									<th>
										${message("rebate.endUser.agent.area")}:
									</th>
									<td>
										${endUser.agent.area}
									</td>
								</tr>
							[/#if]
							[#if endUser.wechatNickName??]
							<tr>
								<th>
									${message("rebate.endUser.wxNickName")}:
								</th>
								<td>
									${endUser.wechatNickName}
								</td>
							</tr>
							[/#if]
							<tr>
								<th>
									${message("rebate.endUser.curScore")}:
								</th>
								<td width=100>
									${endUser.curScore}
								</td>
								<th>
									${message("rebate.endUser.totalScore")}:
								</th>
								<td>
									${endUser.totalScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.curLeMind")}:
								</th>
								<td>
									${endUser.curLeMind}
								</td>
								<th>
									${message("rebate.endUser.totalLeMind")}:
								</th>
								<td>
									${endUser.totalLeMind}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.curLeScore")}:
								</th>
								<td>
									${endUser.curLeScore}
								</td>
								<th>
									${message("rebate.endUser.totalLeScore")}:
								</th>
								<td>
									${endUser.totalLeScore}
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.endUser.curLeBean")}:
								</th>
								<td>
									${endUser.curLeBean}
								</td>
								<th>
									${message("rebate.endUser.totalLeBean")}:
								</th>
								<td>
									${endUser.totalLeBean}
								</td>
							</tr>
							[#if endUser.sellerName??]
							<tr>
								<th>
									${message("rebate.endUser.sellerName")}:
								</th>
								<td>
									${endUser.sellerName}
								</td>
							</tr>
							[/#if]
							
							
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="button" class="btn btn-primary" value="${message("rebate.common.back")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>	
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('.viewer-images').viewer();
		$('.img-lazy').lazyload();
	})
</script>	
</body>
</html>