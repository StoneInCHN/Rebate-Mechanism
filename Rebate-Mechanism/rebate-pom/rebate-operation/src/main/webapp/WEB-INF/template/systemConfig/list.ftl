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
								<a href="javascript:;" class="sort" name="configValue">${message("rebate.systemConfig.isEnabled")}</a>
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
								<span data-toggle="tooltip" data-placement="top" title="${systemConfig.remark}">${message("rebate.systemConfig.configKey."+systemConfig.configKey)}</span>
							</td>
							<td>
								[#if  systemConfig.configValue??]
									${systemConfig.configValue}
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
								<a href="edit.jhtml?id=${systemConfig.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
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
				  <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
		    </div>
		    <div role="tabpanel" class="tab-pane" id="license">
			   <p>
					一、软件使用协议
					  本协议是用户 (自然人、法人或社会团体)与XXXX公司之间关于“XXXX”软件产品（以下
					简称“本软件产品”）的法律协议。一旦安装、复制或以其他方式使用本软件产品，即表示同意接受协议各项条件的约束。如果用户
					不同意协议的条件，请不要使用本软件产品。
					二、软件产品保护条款
					  1）本软件产品之著作权及其它知识产权等相关权利或利益（包括但不限于现已取得或未来可取得之著作权、专利权、商标权、
					营业秘密等）皆为XXXX公司所有。本软件产品受中华人民共和国版权法及国际版权条约和其他知识产权法及条约的保护
					。用户仅获得本软件产品的非排他性使用权。
					  2）用户不得：删除本软件及其他副本上一切关于版权的信息；对本软件进行反向工程，如反汇编、反编译等； 
					  3）本软件产品以现状方式提供，XXXX公司不保证本软件产品能够或不能够完全满足用户需求，在用户手册、帮助
					文件、使用说明书等软件文档中的介绍性内容仅供用户参考，不得理解为对用户所做的任何承诺。XXXX有限公司保留对软件
					版本进行升级，对功能、内容、结构、界面、运行方式等进行修改或自动更新的权利。
					  4）为了更好地服务于用户，或为了向用户提供具有个性的信息内容的需要，本软件产品可能会收集、传播某些信息，但XXXX公司承诺不向未经授权的第三方提供此类信息，以保护用户隐私。
					  5）使用本软件产品由用户自己承担风险，在适用法律允许的最大范围内，XXXX公司在任何情况下不就因使用或不
					能使用本软件产品所发生的特殊的、意外的、非直接或间接的损失承担赔偿责任。即使已事先被告知该损害发生的可能性。 
					  6）XXXX公司定义的信息内容包括：文字、软件、声音；本公司为用户提供的商业信息，所有这些内容受版权、商
					标权、和其它知识产权和所有权法律的保护。所以，用户只能在本公司授权下才能使用这些内容，而不能擅自复制、修改、编撰这些
					内容、或创造与内容有关的衍生产品。
					  7）如果您未遵守本协议的任何一项条款，XXXX公司有权立即终止本协议，并保留通过法律手段追究责任。
					三、XXXX公司具有对以上各项条款内容的最终解释权和修改权。如用户对XXXX公司的解释或修改有异议，
					应当立即停止使用本软件产品。用户继续使用本软件产品的行为将被视为对XXXX公司的解释或修改的接受。
					四、因本协议所发生的纠纷，双方同意按照中华人民共和国法律，由XXXX公司所在地的有管辖权的法院管辖。
					XXXX公司
			    </p>
			    <a  id="refreshButton" class="btn btn-default pull-right"> <i class="fa fa-edit"></i><span>修改</span></a>
		    </div>
		  </div>
		</div>
	<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script>
	$(function () {
  $('[data-toggle="tooltip"]').tooltip();
})
</script>
</body>
</html>