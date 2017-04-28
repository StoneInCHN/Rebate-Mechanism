<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.apkVersion.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/viewer.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.viewer-images li {
    float: inherit;
}
</style>
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
	<form id="listForm" action="list.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.main.Version")}</a> </li>
                <li class="active">${message("rebate.apkVersion.list")}</li>
          </ol>
		  <div class="content-search accordion-group">
             <div class="accordion-heading" sellerCategory="tab" id="headingOne">
                  <a class="accordion-toggle" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                      	  查询条件
                  </a>
             </div>
            <div id="collapseOne" class="accordion-body in collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="accordion-inner">
						<table class="queryFiled">
							<tr>
								<th>
									${message("rebate.apkVersion.name")}:
								</th>
								<td>
									<input type="text" name="versionName" class="text" value="${versionName}"  maxlength="50" />
								</td>
								<th>
									${message("rebate.apkVersion.forceUpdate")}:
								</th>
								<td>
									<select  name="isForced">
										<option value="">${message("rebate.common.All")}</option>
										<option [#if isForced == "true"] selected="selected" [/#if] value="true">${message("rebate.common.true")}</option>
										<option [#if isForced == "false"] selected="selected" [/#if] value="false">${message("rebate.common.false")}</option>
									</select>
								</td>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="btn " value="${message("rebate.common.submit")}" />
									<input type="reset" class="btn " value="${message("rebate.common.reset")}" onclick="location.href='list.jhtml'" />
								</td>
							</tr>
							<tr>
								<th>
									${message("rebate.apkVersion.uploadTime")}:
								</th>
								<td>
									<input type="text" id="uploadDateFrom" name="uploadDateFrom" class="text Wdate" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'uploadDateTo\')}'});" readonly [#if uploadDateFrom??]value="${uploadDateFrom?string("yyyy-MM-dd")}"[/#if] />
								</td>
								<th class="dateRange">
									---
								</th>
								<td>
									<input type="text" id="uploadDateTo" name="uploadDateTo" class="text Wdate" onclick="WdatePicker({minDate: '#F{$dp.$D(\'uploadDateFrom\')}'});" readonly [#if uploadDateTo??]value="${uploadDateTo?string("yyyy-MM-dd")}" [/#if]/>
								</td>
							</tr>
						</table>
                  </div>
             </div>
         </div>
 		 <div class="button-group">
              <a  id="addButton" class="btn btn-default"><i class="fa fa-plus"></i><span>添加</span></a>
              <a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
         <div>
	        <table id="listTable" class="table table-responsive table-condensed table-striped table-bordered table-hover table-nowrap">
				<thead>
					<tr>
						<th class="check">
							<input type="checkbox" id="selectAll" />
						</th>
						<th>
							<a href="javascript:;" class="sort" name="versionName">${message("rebate.apkVersion.name")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="versionCode">${message("rebate.apkVersion.versionCode")}</a>
						</th>
						<th>
							<a href="javascript:;" name="apkPath">${message("rebate.apkVersion.apkPath")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="isForced">${message("rebate.apkVersion.forceUpdate")}</a>
						</th>
						<th>
							<a href="javascript:;" name="updateContent">${message("rebate.apkVersion.updateContent")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="createDate">${message("rebate.apkVersion.uploadTime")}</a>
						</th>
						</th>
						<th>
							${message("rebate.common.handle")}
						</th>
					</tr>
				</thead>
				<tbody>
					[#list  page.content as version]
						<tr>
							<td>
								<input type="checkbox" name="ids" value="${version.id}"/>
							</td>
							<td>
								${version.versionName}
							</td>
							<td>
								${version.versionCode}
							</td>
							<td>
								${version.apkPath}
							</td>
							<td>
								${message(version.isForced?string('rebate.common.true', 'rebate.common.false'))}
							</td>
							<td>
								<span title="${version.updateContent}">${abbreviate(version.updateContent,50, "...")}</span>
							</td>
							<td>
								<span title="${version.createDate?string("yyyy-MM-dd HH:mm:ss")}">${version.createDate}</span>
							</td>
							<td>
								<a href="edit.jhtml?id=${version.id}" title="${message("rebate.common.edit")}"><i class="fa fa-pencil-square-o"></i></a>
							</td>
						</tr>
					[/#list]
				</tbody>
			</table>
		<div> 
    </form>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/viewer.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(function(){
	
	$('.viewer-images').viewer();
	//图片懒加载
	$('.img-lazy').lazyload();
	
})
</script>
</body>
</html>