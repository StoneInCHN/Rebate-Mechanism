<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${message("rebate.orderRealTimeReport.list")}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/main.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/style/dialog.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 Support for IE -->
  <!--[if lt IE 9]>
  <script src="${base}/resources/js/html5shim.js"></script>
  <![endif]-->
</head>
<body>
<ol class="breadcrumb">
        <li><a ><i class="fa fa-user"></i> ${message("rebate.main.reportManager")}</a> </li>
        <li class="active">${message("rebate.report.list")}</li>
  </ol>
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="apiReport_table_list">
	<form id="listForm" action="orderRealTimeReport.jhtml" method="get">
          <ol class="breadcrumb">
                <li><a ><i class="fa fa-user"></i> ${message("rebate.report.orderRealTimeReport")}</a> </li>
                <li class="active">${message("rebate.orderRealTimeReport.list")}</li>
          </ol>
	         
 		 <div class="button-group">
              <!--<a  id="deleteButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>删除</span></a>
              <a  id="lockedButton" class="btn btn-default disabled"><i class="fa fa-times"></i><span>禁用</span></a>-->
              <a  id="refreshButton" class="btn btn-default"> <i class="fa fa-refresh"></i><span>刷新</span></a>
         </div>
        <table id="listTable" class="table table-striped table-bordered table-hover table-nowrap">
			<thead>
				<tr>
					<!--<th class="check">
						<input type="checkbox" id="selectAll" />
					</th>-->
					<th>
						<a href="javascript:;"  name="currentTime">${message("rebate.orderRealTimeReport.current")}</a>
					</th>
					<th>
						<a href="javascript:;" name="orderCount">${message("rebate.orderRealTimeReport.orderCount")}</a>
					</th>
					<th>
						<a href="javascript:;" name="orderAmount">${message("rebate.orderRealTimeReport.orderAmount")}</a>
					</th>
					<th>
						<a href="javascript:;"  name="orderRebateAmount">${message("rebate.orderRealTimeReport.orderRebateAmount")}</a>
					</th>
					<th>
						<a href="javascript:;"  name="sellerOrderCount">${message("rebate.orderRealTimeReport.sellerOrderCount")}</a>
					</th>
					<th>
						<a href="javascript:;"  name="sellerOrderAmount">${message("rebate.orderRealTimeReport.sellerOrderAmount")}</a>
					</th>
					<th>
						<a href="javascript:;"  name="sellerOrderRebateAmount">${message("rebate.orderRealTimeReport.sellerOrderRebateAmount")}</a>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						${currentTime}
					</td>
					<td>
						${orderCount}
					</td>
					<td>
						${orderAmount}
					</td>
					<td>
						${orderRebateAmount}
					</td>
					<td>
						${sellerOrderCount}
					</td>
					<td>
						${sellerOrderAmount}
					</td>
					<td>
						${sellerOrderRebateAmount}
					</td>
				</tr>
			</tbody>
		</table>
    </form>
    <div><font color='red'>注:以上数据为当日数据</font></div>
  </div>
</div>
<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/resources/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${base}/resources/js/report.js"></script>
<script type="text/javascript">
$(function () {
	  $('[data-toggle="tooltip"]').tooltip();
	})
</script>
</body>
</html>