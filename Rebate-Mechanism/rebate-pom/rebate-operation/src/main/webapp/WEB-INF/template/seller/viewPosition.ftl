<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>查看位置点</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="${base}/resources/style/bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="${base}/resources/style/font-awesome.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=D767b598a9f1e43c3cadc4fe26cdb610"></script>
	<style  type="text/css" >
		#allmap {width: 550px;height: 400px;overflow: hidden;margin:0}
	</style>
</head>
<body>
<div id="allmap"></div>
<script type="text/javascript">
	var map = new BMap.Map("allmap");    // 创建Map实例
	map.centerAndZoom(new BMap.Point(${seller.latitude}, ${seller.longitude}), 15);  // 初始化地图,设置中心点坐标和地图级别
	map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
	map.setCurrentCity("${seller.area}");          // 设置地图显示的城市 此项是必须设置的
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	
	var marker = new BMap.Marker(new BMap.Point(${seller.latitude}, ${seller.longitude})); // 创建点
	map.addOverlay(marker);    //增加点

</script>  
</body>
</html>