//设置当前日期 
var ma = [ [ '1', '3', '5', '7', '8', '10' ], [ '4', '6', '9', '11' ] ];
var dd = new Date();
var now = pasDate(dd);
var count = 24;

function getXDate(){
	var dateArray = new Array();
	var xdate = new Array();
	var needDate = now;
	dateArray.push(now);
	for(var i=1;i<=count;i++){
		needDate = trans(needDate, '0');
		if(i%2 == 0){
			dateArray.push(needDate);
		}
	}
	for(var j=dateArray.length-1;j>=0;j--){
		xdate.push(dateArray[j]);
	}
	return xdate;
}


// 转化日期函数
function pasDate(da) {
	return da.getFullYear()+"-"+(da.getMonth()+1)+"-"+da.getDate();
//	var yp = da.indexOf('年'), mp = da.indexOf('月'), dp = da.indexOf('日');
//	var y = da.substr(0, yp), m = da.substr(yp + 1, mp - yp - 1), d = da
//			.substr(mp + 1, dp - mp - 1);
//	return [ y, m, d ];
}
// 判断数组a是否存在在元素n
function check(n, a) {
	for (var i = 0, len = a.length; i < len; i++) {
		if (a[i] == n) {
			return true;
		}
	}
	return false;
}
// 闰年
function isLeap(y) {
	return ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) ? true : false;
}
// 实现加减：'1'加，'0'减
function trans(o, f) {
	var d = o.split('-');
	var l = isLeap(d[0]);
	if (f == '1') {
		if ((check(d[1], ma[0]) && (d[2] == '31'))
				|| (check(d[1], ma[1]) && (d[2] == '30'))
				|| (d[1] == '2' && d[2] == '28' && !l)
				|| (d[1] == '2' && d[2] == '29' && l)) {
			return d[0] + '-' + (d[1] * 1 + 1) + '-' + 1;
		} else if (d[1] == '12' && d[2] == '31') {
			return (d[0] * 1 + 1) + '-' + '01-01';
		} else {
			return d[0] + '-' + d[1] + '-' + (d[2] * 1 + 1);
		}
	} else if (f == '0') {
		if (check(d[1] - 1, ma[0]) && (d[2] == '1')) {
			return d[0] + '-' + (d[1] - 1) + '-' + '31';
		} else if (check(d[1] - 1, ma[1]) && (d[2] == '1')) {
			return d[0] + '-' + (d[1] - 1) + '-' + '30';
		} else if (d[1] == '1' && d[2] == '1') {
			return (d[0] - 1) + '-' + '12-31';
		} else if (d[1] == '3' && d[2] == '1' && !l) {
			return d[0] + '-' + '2-28';
		} else if (d[1] == '3' && d[2] == '1' && l) {
			return d[0] + '-' + '2-29';
		} else {
			return d[0] + '-' + d[1] + '-' + (d[2] - 1);
		}
	} else {
		return;
	}
}

//$('Add').onclick = function() {
//	var v = $('date').value;
//	$('date').value = trans(v, '1');
//}
//
//$('Minus').onclick = function() {
//	var v = $('date').value;
//	$('date').value = trans(v, '0');
//}


Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
} 
