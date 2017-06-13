[#assign shiro=JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>${message("rebate.main.title")}</title>
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" media="screen">
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<!--[if lt IE 9]>
	<script src="${base}/resources/js/excanvas.min.js"></script>
	<script src="${base}/resources/js/respond.min.js"></script>  
<![endif]-->   

</head>
<body>
      <div class="header">
          <div class="logo">
              <img src="" alt="后台系统">
          </div>
          <ul id="userProfile"class="nav pull-right">
              <li class="dropdown pull-right">            
			            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
			              <i class="fa fa-user"></i> [@shiro.principal /] <b class="caret"></b>              
			            </a>
			            <ul class="dropdown-menu">
		             	 	<li><a href="../common/logout.jhtml" target="_top"><i class="fa fa-power-off"></i>&nbsp;&nbsp;${message("rebate.main.logout")}</a></li>
			            </ul>
			   </li>
          </ul>
      </div>
      <div class="main">
          <div class="sidebar">
              <div class="sidebar-content">
              	  [#list ["rebate:admin", "rebate:role","rebate:area","rebate:account","rebate:systemConfig","rebate:settingConfig","rebate:operationLog","rebate:topBanner","rebate:userHelp", "rebate:holidayConfig"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.systemNav")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:account"]
		                           <li>
		                               <a href="../account/accountInfo.jhtml" class="active" target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.account.settingGroup")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:admin"]
		                           <li>
		                               <a href="../admin/list.jhtml"  target="iframe"> <i class="fa fa-user"></i><span class="text-normal">${message("rebate.main.admin")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:role"]
		                           <li>
		                               <a href="../role/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.role")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:systemConfig"]
		                           <li>
		                               <a href="../systemConfig/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.systemConfig")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:settingConfig"]
		                           <li>
		                               <a href="../settingConfig/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.settingConfig")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:operationLog"]
		                           <li>
		                               <a href="../operationLog/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.operationLog")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:topBanner"]
		                           <li>
		                               <a href="../topBanner/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.topBanner")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                             [@shiro.hasPermission name="rebate:area"]
		                           <li>
		                               <a href="../area/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.area")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
 								[@shiro.hasPermission name="rebate:userHelp"]
		                           <li>
		                               <a href="../userHelp/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.userHelp")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:holidayConfig"]
		                           <li>
		                               <a href="../holidayConfig/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.holidayConfig")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:messageInfo"]
		                           <li>
		                               <a href="../messageInfo/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.messageInfo")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
					[/#list]
						[#list ["rebate:agent","rebate:agentCommission"] as permission]
							[@shiro.hasPermission name = permission]
			                   <div class="sidebar-nav">
			                       <div class="sidebar-title">
			                           <a href="#">
			                               <i class="fa fa-user"></i>
			                               <span class="text-normal">${message("rebate.main.agentManager")}</span>
			                           </a>
			                       </div>
			                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
					                   [@shiro.hasPermission name="rebate:agentCommission"]
				                       <li>
				                           <a href="../agentCommission/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.agentCommission")}</span></a>
			                           </li>
			                           [/@shiro.hasPermission]
			                            [@shiro.hasPermission name="rebate:agent"]
			                           <li>
			                               <a href="../agent/list.jhtml" target="iframe"><i class="fa fa-male"></i><span class="text-normal">${message("rebate.main.agent")}</span></a>
			                           </li>
			                           [/@shiro.hasPermission]
			                       </ul>
			                   </div>
							   	[#break /]
						[/@shiro.hasPermission]
					[/#list]
				 [#list ["rebate:sellerCategory","rebate:sellerApply","rebate:seller"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.sellerManager")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                       	  [@shiro.hasPermission name="rebate:sellerCategory"]
		                           <li>
		                               <a href="../sellerCategory/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.sellerCategory")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                     	  [@shiro.hasPermission name="rebate:sellerApply"]
		                           <li>
		                               <a href="../sellerApply/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.sellerApply")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:seller"]
		                           <li>
		                               <a href="../seller/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.seller")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:sellerEvaluate"]
		                           <li>
		                               <a href="../sellerEvaluate/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.sellerEvaluate")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
					 [#list ["rebate:withdraw","rebate:sellerClearingRecord","rebate:systemWithdrawal"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.withdraw")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:withdraw"]
		                           <li>
		                               <a href="../leScoreRecord/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.leScoreRecord.withdraw")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
									[@shiro.hasPermission name="rebate:sellerClearingRecord"]
		                           <li>
		                               <a href="../sellerClearingRecord/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.sellerClearingRecord")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:systemWithdrawal"]
		                           <li>
		                               <a href="../systemWithdrawRecord/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.systemWithdrawal")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
				[#list ["rebate:endUser","rebate:scoreRecord","rebate:leScoreRecord","rebate:leMindRecord","rebate:leBeanRecord","rebate:bankCard"]as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.endUserManager")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:endUser"]
		                           <li>
		                               <a href="../endUser/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.endUser.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:scoreRecord"]
		                           <li>
		                               <a href="../endUser/score/record.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.endUser.score.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:leMindRecord"]
		                           <li>
		                               <a href="../endUser/leMind/record.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.endUser.leMind.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:leScoreRecord"]
		                           <li>
		                               <a href="../endUser/leScore/record.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.endUser.leScore.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:leBeanRecord"]
		                           <li>
		                               <a href="../endUser/leBean/record.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.endUser.leBean.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                           [@shiro.hasPermission name="rebate:bankCard"]
		                           <li>
		                               <a href="../bankCard/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.bankCard")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
				[#list ["rebate:order","rebate:sallerOrder"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.orderManager")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:order"]
		                           <li>
		                               <a href="../order/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.order.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                            [@shiro.hasPermission name="rebate:sallerOrder"]
		                           <li>
		                               <a href="../sallerOrder/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.sallerOrder.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]

				[#list ["rebate:report"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.reportManager")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:report"]
		                           <li>
		                               <a href="../report/nationBonusReport.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.report.nationBonusReport")}</span></a>
		                           </li>
		                            <li>
		                               <a href="../report/userBonusReport.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.report.userBonusReport")}</span></a>
									</li>
									<li>
		                               <a href="../report/userRegReport.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.userRegReport")}</span></a>
									</li>
									[/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
				[#list ["rebate:bonusParam"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.bonusParam")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:bonusParam"]
		                           <li>
		                               <a href="../bonusParam/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.bonusParam.perDay")}</span></a>

		                           </li>
		                           [/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
				[#list ["rebate:version"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.Version")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:version"]
		                           <li>
		                               <a href="../apkVersion/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.apkVersion.info")}</span></a>
		                           </li>
		                           [/@shiro.hasPermission]
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
              </div>
          </div>
          <div class="content">
          		 <iframe  id="iframe" name="iframe" marginheight="0" marginwidth="0" frameborder="0" style="width:100%;height:97%" src="../account/accountInfo.jhtml"></iframe>
        	<div class="footer">
		              <p>   @2017 xxxx有限公司</p>
		      </div>
          </div>
      </div>
	<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
      <script>
          $(".sidebar-title").bind('click', function() {
              if ($(this).parent(".sidebar-nav").hasClass("sidebar-nav-fold")) {
                  $(this).next().slideDown(200);
                  $(this).parent(".sidebar-nav").removeClass("sidebar-nav-fold");
              } else {
                  $(this).next().slideUp(200);
                  $(this).parent(".sidebar-nav").addClass("sidebar-nav-fold");
              }
          });
		 $(".sidebar-trans a").bind('click',function(){
		 	var $this = $(this);
		 	 $(".sidebar-trans a").removeClass("active");
		 	 $this.addClass("active");
		 })
      </script>
</body>
</html>
