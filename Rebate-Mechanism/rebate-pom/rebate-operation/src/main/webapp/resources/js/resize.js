	function getDocHeight(doc){
		//在IE中doc.body.scrollHeight的可信度最高
		//在Firefox中，doc.height就可以了
		var docHei = 0;
		var scrollHei;//scrollHeight
		var offsetHei;//offsetHeight，包含了边框的高度
		
		if (doc.height){
		//Firefox支持此属性，IE不支持
		docHei = doc.height;
		}
		else if (doc.body){
		//在IE中，只有body.scrollHeight是与当前页面的高度一致的，
		//其他的跳转几次后就会变的混乱，不知道是依照什么取的值！
		//似乎跟包含它的窗口的大小变化有关
		if(doc.body.offsetHeight) docHei = offsetHei = doc.body.offsetHeight;
		//if(doc.body.scrollHeight) docHei = scrollHei = doc.body.scrollHeight;
		}
		else if(doc.documentElement){
		if(doc.documentElement.offsetHeight) docHei = offsetHei = doc.documentElement.offsetHeight;
		//if(doc.documentElement.scrollHeight) docHei = scrollHei = doc.documentElement.scrollHeight;
		}
		/*
		docHei = Math.max(scrollHei,offsetHei);//取最大的值，某些情况下可能与实际页面高度不符！
		*/
		return docHei;
	}
	function doReSize(){
		var iframeWin = window.frames['iframe'];
		var iframeEl = window.document.getElementById? window.document.getElementById('iframe'): document.all? document.all['iframe']: null;
		if ( iframeEl && iframeWin ){
		var docHei = getDocHeight(iframeWin.document);
		if (docHei != iframeEl.style.height) iframeEl.style.height = docHei + 'px';
		}
		else if(iframeEl){
		var docHei = getDocHeight(iframeEl.contentDocument);
		if (docHei != iframeEl.style.height) iframeEl.style.height = docHei + 'px';
		}
	}
	function runResizeTask(){
		doReSize();
		setTimeout("runResizeTask()",1000);//每隔1秒执行一次
	}
	runResizeTask();