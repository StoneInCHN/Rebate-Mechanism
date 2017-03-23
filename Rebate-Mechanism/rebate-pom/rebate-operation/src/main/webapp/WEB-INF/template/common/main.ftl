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
              	  [#list ["rebate:admin", "rebate:role","rebate:area","rebate:account"] as permission]
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
		                       </ul>
		                   </div>
						   	[#break /]
					[/@shiro.hasPermission]
				[/#list]
				 [#list ["rebate:sellerApply"] as permission]
						[@shiro.hasPermission name = permission]
		                   <div class="sidebar-nav">
		                       <div class="sidebar-title">
		                           <a href="#">
		                               <i class="fa fa-user"></i>
		                               <span class="text-normal">${message("rebate.main.systemNav")}</span>
		                           </a>
		                       </div>
		                       <ul class="sidebar-trans" style="overflow: hidden; display: block;">
		                     	  [@shiro.hasPermission name="rebate:sellerApply"]
		                           <li>
		                               <a href="../sellerApply/list.jhtml"  target="iframe"> <i class="fa fa-cog"></i><span class="text-normal">${message("rebate.main.sellerApply")}</span></a>
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