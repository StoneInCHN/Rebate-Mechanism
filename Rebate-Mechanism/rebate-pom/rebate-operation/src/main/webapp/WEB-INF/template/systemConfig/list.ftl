<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.systemConfig.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog-plus.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.systemConfig")}</a> </li>
                <li class="active">${message("rebate.systemConfig.list")}</li>
          </ol>
 		 <div class="button-group clearfix ">
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <div class="setting-tab">
		  <!-- Nav tabs -->
		  <ul class="nav nav-tabs" role="tablist">
		    <li role="presentation" class="active"><a href="#setting" aria-controls="setting" role="tab" data-toggle="tab">基础数据配置</a></li>
		    <li role="presentation"><a href="#paymenttype" aria-controls="paymenttype" role="tab" data-toggle="tab">支付方式</a></li>
		    <li role="presentation"><a href="#about" aria-controls="about" role="tab" data-toggle="tab">关于</a></li>
		    <li role="presentation"><a href="#license" aria-controls="license" role="tab" data-toggle="tab">许可协议</a></li>
		   <!-- <li role="presentation"><a href="#withdrawRule" aria-controls="withdrawRule" role="tab" data-toggle="tab">提现规则</a></li> -->
		    <li role="presentation"><a href="#other" aria-controls="other" role="tab" data-toggle="tab">其他</a></li>
		  </ul>
		  <!-- Tab panes -->
		  <div class="tab-content">
		    <div role="tabpanel" class="tab-pane active" id="setting">
		    	<table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
					<thead>
						<tr>
							<th>
								<a href="javascript:;" class="sort" name="configKey">${message("rebate.systemConfig.configKey")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="configValue">${message("rebate.systemConfig.configValue")}</a>
							</th>
							<th>
								<a href="javascript:;" name="remark">${message("rebate.systemConfig.remark")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="isEnabled">${message("rebate.systemConfig.isEnabled")}</a>
							</th>
							<th>
								<span>${message("rebate.common.handle")}</span>
							</th>
						</tr>
					</thead>
					<tbody>
						[#list systemConfigs as systemConfig]
						<tr>
							<td>
								<span  title="${systemConfig.remark}">${message("rebate.systemConfig.configKey."+systemConfig.configKey)}</span>
							</td>
							<td>
								[#if  systemConfig.configValue??]
									${systemConfig.configValue}
								[#else]
									--
								[/#if]
							</td>
							<td>
								[#if  systemConfig.remark??]
								<span title="${systemConfig.remark}">${abbreviate(systemConfig.remark,50, "...")}</span>
									
								[#else]
									--
								[/#if]
							</td>
							<td>
								[#if  systemConfig.isEnabled]
									<span class="label label-success">${message("rebate.systemConfig.isEnabled.true")}</span>
								[#else]
									<span class="label label-danger">${message("rebate.systemConfig.isEnabled.false")}</span>
								[/#if]
								
							</td>
							<td>
								[#if  systemConfig.configKey != "USER_RECOMMEND_COMMISSION_PERCENTAGE"]
									<a href="edit.jhtml?id=${systemConfig.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
								[/#if]
							</td>
						</tr>
						[/#list]
					</tbody>
				</table>
		    </div>
		    <div role="tabpanel" class="tab-pane" id="paymenttype">
		    	<table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
					<thead>
						<tr>
							<th>
								<a href="javascript:;" class="sort" name="configKey">${message("rebate.systemConfig.configKey")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="configValue">${message("rebate.systemConfig.configValue")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="configOrder">${message("rebate.systemConfig.configOrder")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="configValue">${message("rebate.systemConfig.isEnabled")}</a>
							</th>
							<th>
								<span>${message("rebate.common.handle")}</span>
							</th>
						</tr>
					</thead>
					<tbody>
						[#list paymentTypes as systemConfig]
						<tr>
							<td>
								${message("rebate.systemConfig.configKey."+systemConfig.configKey)}
							</td>
							<td>
								${systemConfig.configValue}
							</td>
							<td>
								[#if  systemConfig.configOrder??]
									${systemConfig.configOrder}
								[#else]
									--
								[/#if]
							</td>
							<td>
								[#if  systemConfig.isEnabled]
									<span class="label label-success">${message("rebate.systemConfig.isEnabled.true")}</span>
								[#else]
									<span class="label label-danger">${message("rebate.systemConfig.isEnabled.false")}</span>
								[/#if]
								
							</td>
							<td>
								<a href="editPay.jhtml?id=${systemConfig.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
							</td>
						</tr>
						[/#list]
					</tbody>
				</table>
		    </div>
		    <div role="tabpanel" class="tab-pane" id="about">
				<a  id="editAbout" class="btn btn-default" href="editSettingConfig.jhtml?configKey=ABOUT_US&id=${about.id}"><i class="fa fa-edit"></i><span>修改</span></a>	
				<p style="margin:10px">
					[#if  about.isEnabled]
						<span class="label label-success">${message("rebate.systemConfig.isEnabled.true")}</span>
					[#else]
						<span class="label label-danger">${message("rebate.systemConfig.isEnabled.false")}</span>
					[/#if]
				</p>
				<p>${about.configValue}</p>
		    </div>
		    <div role="tabpanel" class="tab-pane" id="license">
		       <a  id="editLicense" class="btn btn-default" href="editSettingConfig.jhtml?configKey=LICENSE_AGREEMENT&id=${license.id}"><i class="fa fa-edit"></i><span>修改</span></a>	
				<p style="margin:10px">
					[#if  license.isEnabled]
						<span class="label label-success">${message("rebate.systemConfig.isEnabled.true")}</span>
					[#else]
						<span class="label label-danger">${message("rebate.systemConfig.isEnabled.false")}</span>
					[/#if]
				</p>
				<p>${license.configValue}</p>
		    </div>
			<!--
		     <div role="tabpanel" class="tab-pane" id="withdrawRule">
		       <a  id="editLicense" class="btn btn-default" href="editSettingConfig.jhtml?configKey=WITHDRAW_RULE&id=${withdrawRule.id}"><i class="fa fa-edit"></i><span>修改</span></a>	
				<p style="margin:10px">
					[#if  withdrawRule.isEnabled]
						<span class="label label-success">${message("rebate.systemConfig.isEnabled.true")}</span>
					[#else]
						<span class="label label-danger">${message("rebate.systemConfig.isEnabled.false")}</span>
					[/#if]
				</p>
				<p>${withdrawRule.configValue}</p>
		    </div>
			-->
			 <div role="tabpanel" class="tab-pane" id="other">
			 	<table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
					<thead>
						<tr>
							<th>
								<a href="javascript:;" class="sort" name="configKey">${message("rebate.settingConfig.configKey")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="configValue">${message("rebate.settingConfig.configValue")}</a>
							</th>
							<th>
								<a href="javascript:;" class="sort" name="isEnabled">${message("rebate.settingConfig.isEnabled")}</a>
							</th>
							<th>
								<span>${message("rebate.common.handle")}</span>
							</th>
						</tr>
					</thead>
					<tbody>
							[#list settingConfigs as settingConfig]
							<tr>
								<td>
									<span data-toggle="tooltip" data-placement="top" title="${settingConfig.remark}">${message("rebate.settingConfig.configKey."+settingConfig.configKey)}</span>
								</td>
								<td>
									${settingConfig.configValue}
								</td>
								<td>
									[#if  settingConfig.isEnabled]
										<span class="label label-success">${message("rebate.settingConfig.isEnabled.true")}</span>
									[#else]
										<span class="label label-danger">${message("rebate.settingConfig.isEnabled.false")}</span>
									[/#if]
									
								</td>
								<td>
									<a href="editOther.jhtml?id=${settingConfig.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
								</td>
							</tr>
						[/#list]
					</tbody>
				</table>
			 
		      
		    </div>
		  </div>
		</div>
	<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/dialog-plus.js"></script>
<script>
	$(function () {
	  $('[data-toggle="tooltip"]').tooltip();
	})
</script>
</body>
</html>