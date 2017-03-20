<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.util.UUID"%>
<%@page import="java.security.interfaces.RSAPublicKey"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.rebate.beans.Setting"%>
<%@page import="org.rebate.utils.SettingUtils"%>
<%@page import="org.rebate.utils.SpringUtils"%>
<%@page import="org.rebate.beans.Setting.CaptchaType"%>
<%@page import="org.rebate.service.RSAService"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
String base = request.getContextPath();
String captchaId = UUID.randomUUID().toString();
ApplicationContext applicationContext = SpringUtils.getApplicationContext();
Setting setting = SettingUtils.get();
if (applicationContext != null) {
%>
<shiro:authenticated>
<%
	response.sendRedirect(base + "/console/common/main.jhtml");
%>
</shiro:authenticated>
<%
}
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="zh-cn" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="zh-cn" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="zh-cn"> <!--<![endif]-->
<!--[if lt IE 9]>

	<script src="<%=base%>/resources/js/excanvas.min.js"></script>

	<script src="<%=base%>/resources/js/respond.min.js"></script>  

	<![endif]-->  
<!-- BEGIN HEAD -->
<head>
	<%
if (applicationContext != null) {
	RSAService rsaService = SpringUtils.getBean("rsaServiceImpl", RSAService.class);
	RSAPublicKey publicKey = rsaService.generateKey(request);
	String modulus = Base64.encodeBase64String(publicKey.getModulus().toByteArray());
	String exponent = Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray());
	
	String message = null;
	String attrName = FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
	String loginFailure = (String) request.getAttribute(attrName);
	if (loginFailure != null) {
		if (loginFailure.equals("org.apache.shiro.authc.pam.UnsupportedTokenException")) {
			message = "rebate.captcha.invalid";
		} else if (loginFailure.equals("org.apache.shiro.authc.UnknownAccountException")) {
			message = "rebate.login.unknownAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.DisabledAccountException")) {
			message = "rebate.login.disabledAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
			message = "rebate.login.lockedAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
				message = "rebate.login.incorrectCredentials";
		} else if (loginFailure.equals("org.apache.shiro.authc.AuthenticationException")) {
			message = "rebate.login.authentication";
		}else{
			message = "rebate.login.incorrectCredentials";
		}
	}
%>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title><%=SpringUtils.getMessage("rebate.login.title")%> </title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="<%=base%>/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
	<link href="<%=base%>/resources/style/login.css" rel="stylesheet" type="text/css" />
	<link rel="shortcut icon" type="image/x-icon" href="<%=base%>/resources/images/carlife.ico" media="screen" /> 
	<script src="<%=base%>/resources/js/jquery.js" type="text/javascript"></script>   
	<script type="text/javascript" src="<%=base%>/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/jquery.placeholder.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/jsbn.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/prng4.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/rng.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/rsa.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/base64.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/common.js"></script>
	<script type="text/javascript" src="<%=base%>/resources/js/input.js"></script>
	<script src="<%=base%>/resources/js/jquery.backstretch.min.js" type="text/javascript"></script>
	<script type="text/javascript"> 
	function loadTopWindow(){
		if (window.top!=null && window.top.document.URL!=document.URL)
		{ window.top.location= document.URL; }
	}
	</script>  
<script type="text/javascript">
	$().ready( function() {
		var $loginForm = $("#loginForm");
		var $enPassword = $("#enPassword");
		var $username = $("#username");
		var $password = $("#password");
		var $captcha = $("#captcha");
		var $captchaImage = $("#captchaImage");
		var $isRememberUsername = $("#isRememberUsername");
		var $alertError = $("#alertError");
		
		// 记住用户名
		if(getCookie("adminUsername") != null) {
			$isRememberUsername.prop("checked", true);
			$username.val(getCookie("adminUsername"));
			$password.focus();
		} else {
			$isRememberUsername.prop("checked", false);
			$username.focus();
		}
		
		// 更换验证码
		$captchaImage.click( function() {
			$captchaImage.attr("src", "<%=base%>/console/common/captcha.jhtml?captchaId=<%=captchaId%>&timestamp=" + (new Date()).valueOf());
		});
		
		// 表单验证、记住用户名
		$loginForm.submit( function() {
			if ($username.val() == "") {
				$alertError.removeClass("hide");
				$alertError.find("span").text("<%=SpringUtils.getMessage("rebate.login.usernameRequired")%>");
				$username.addClass("error");
				$password.removeClass("error");
				$captcha.removeClass("error");
				return false;
			}
			if ($password.val() == "") {
				$alertError.removeClass("hide");
				$alertError.find("span").text("<%=SpringUtils.getMessage("rebate.login.passwordRequired")%>");
				$password.addClass("error");
				$username.removeClass("error");
				$captcha.removeClass("error");
				return false;
			}
			if ($captcha.val() == "") {
				$alertError.removeClass("hide");
				$alertError.find("span").text("<%=SpringUtils.getMessage("rebate.login.captchaRequired")%>");
				$captcha.addClass("error");
				$username.removeClass("error");
				$password.removeClass("error");
				return false;
			}
			if ($isRememberUsername.prop("checked")) {
				addCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
			} else {
				removeCookie("adminUsername");
			}
			
			var rsaKey = new RSAKey();
			rsaKey.setPublic(b64tohex("<%=modulus%>"), b64tohex("<%=exponent%>"));
			var enPassword = hex2b64(rsaKey.encrypt($password.val()));
			$enPassword.val(enPassword);
		});
		
		<%if (message != null) {%>
		$alertError.removeClass("hide");
		$alertError.find("span").text("<%=SpringUtils.getMessage(message)%>");
	<%}%>
	});
</script>
<%} else {%>
<title>提示信息 </title>
<meta http-equiv="expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<link href="<%=base%>/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="<%=base%>/resources/style/login.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="<%=base%>/resources/images/carlife.ico" media="screen" /> 
<%}%>
</head>
<body class="login" onload="loadTopWindow()">
	<div class="logo">
		<img src="<%=base%>/resources/images/logo.png" alt="<%=SpringUtils.getMessage("rebate.apply.logo")%>" /> 
	</div>
	<div class="content">
		<form class="form-vertical login-form" id="loginForm" action="login.jsp" method="post">
			<input type="hidden" id="enPassword" name="enPassword" />
			<input type="hidden" id="localUrl" />
			<%if (ArrayUtils.contains(setting.getCaptchaTypes(), CaptchaType.adminLogin)) {%>
						<input type="hidden" name="captchaId" value="<%=captchaId%>" />
			<%}%>
			<h3 class="form-title">用户登陆</h3>
			<div id="alertError" class="alert alert-error hide">
				<button class="close" data-dismiss="alert"></button>
				<span>Enter any username and password.</span>
			</div>
			<div  class="control-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9"><%=SpringUtils.getMessage("rebate.login.username")%></label>
				<div class="controls">
					<div class="input-icon left">
						<i class="fa fa-user"></i>
						<input type="text" id="username" name="username" class="m-wrap placeholder-no-fix" maxlength="30"  placeholder ="<%=SpringUtils.getMessage("rebate.login.username")%>"/>
					</div>
				</div>
			</div>
			<div  class="control-group">
				<label class="control-label visible-ie8 visible-ie9"><%=SpringUtils.getMessage("rebate.login.password")%></label>
				<div class="controls">
					<div class="input-icon left">
						<i class="fa fa-lock"></i>
						<input type="password" id="password"  class="m-wrap placeholder-no-fix" maxlength="20" autocomplete="off" placeholder="<%=SpringUtils.getMessage("rebate.login.password")%>"/>
					</div>
				</div>
			</div>
			<%if (ArrayUtils.contains(setting.getCaptchaTypes(), CaptchaType.adminLogin)) {%>
			<div  class="control-group">
				<label class="control-label visible-ie8 visible-ie9"><%=SpringUtils.getMessage("rebate.login.captcha.imageTitle")%></label>
				<div class="controls">
					<div class="input-icon left">
						<i class="fa fa-lock"></i>
						<input type="text" id="captcha" name="captcha" class="m-wrap placeholder-no-fix captcha" maxlength="4" autocomplete="off" placeholder="<%=SpringUtils.getMessage("rebate.login.captcha.imageTitle")%>"/>
						<img id="captchaImage" src="<%=base%>/console/common/captcha.jhtml?captchaId=<%=captchaId%>" title="<%=SpringUtils.getMessage("rebate.login.captcha.imageTitle")%>" />
					</div>
				</div>
			</div>	
			<%}%>
			<div class="form-actions">
					<input type="checkbox" class="checkbox" id="isRememberUsername" value="true" /><%=SpringUtils.getMessage("rebate.login.rememberUsername")%>
				<input type="submit" class="btn blue pull-right" value="<%=SpringUtils.getMessage("rebate.login.login")%>"/>        
			</div>
		</form>
	</div>
	<div class="copyright">
		2017 &copy; rights reserved.
	</div>
	<script type="text/javascript">
    $(function(){
    	//背景图片滚动显示
    	  $.backstretch([
    	 		        "resources/images/login/bg/1.jpg",
    	 		        "resources/images/login/bg/2.jpg",
    	 		        "resources/images/login/bg/3.jpg",
    	 		        "resources/images/login/bg/4.jpg"
    	 		        ], {
    	 		          fade: 1000,
    	 		          duration: 8000
    	 });
        //解决IE下不支持placeholder
       /*  if($.browser.msie) {
            $(":input[placeholder]").each(function(){
                $(this).placeholder();
            });
        } */
    })
</script>
</body>
</html>