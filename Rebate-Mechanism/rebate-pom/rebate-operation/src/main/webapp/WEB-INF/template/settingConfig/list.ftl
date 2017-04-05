<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.settingConfig.list")}</title>
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
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.settingConfig")}</a> </li>
                <li class="active">${message("rebate.settingConfig.list")}</li>
          </ol>
		  <div class="content-search accordion-group">
             <div class="accordion-heading" role="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  查询条件
                  </a>
             </div>
             <div id="collapseOne" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="accordion-inner">
						<table class="queryFiled">
							<tr>
								<th>
									${message("rebate.settingConfig.configKey")}:
								</th>
								<td>
									<input type="text" name="configKey" class="text" value="${configKey}"maxlength="20" />
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <div>
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
						<a href="javascript:;" class="sort" name="configOrder">${message("rebate.settingConfig.configOrder")}</a>
					</th>
					<th>
						<a href="javascript:;" class="sort" name="configValue">${message("rebate.settingConfig.isEnabled")}</a>
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
						${message("rebate.settingConfig.configKey."+settingConfig.configKey)}
					</td>
					<td>
						[#if  settingConfig.configValue??]
							${settingConfig.configValue}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  settingConfig.configOrder??]
							${settingConfig.configOrder}
						[#else]
							--
						[/#if]
					</td>
					<td>
						[#if  settingConfig.isEnabled]
							<span class="label label-success">${message("rebate.settingConfig.isEnabled.true")}</span>
						[#else]
							<span class="label label-danger">${message("rebate.settingConfig.isEnabled.false")}</span>
						[/#if]
						
					</td>
					<td>
						<a href="edit.jhtml?id=${settingConfig.id}" title="${message("csh.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
					</td>
				</tr>
				[/#list]
			</tbody>
		</table>
	<div> 
    </form>

</body>
</html>