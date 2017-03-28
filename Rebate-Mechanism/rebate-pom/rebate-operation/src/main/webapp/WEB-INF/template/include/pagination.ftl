<input type="hidden" id="searchProperty" name="searchProperty" value="${page.searchProperty}" />
<input type="hidden" id="orderProperty" name="orderProperty" value="${page.orderProperty}" />
<input type="hidden" id="orderDirection" name="orderDirection" value="${page.orderDirection}" />
	<div class="clearfix">
		<span class="pagination-info">当前第<input type="text" id="pageNumber" name="pageNumber" value="${pageNumber}"/> 页/每页展示 <input type="text" id="pageSize" name="pageSize" value="${page.pageSize}" />条 /共${page.total}条</span>
		<ul class="pagination pull-right">
		[#if isFirst]
			 <li class="disabled"><span>首页</span></li>
		[#else]
			<li >
				<a  href="javascript: $.pageSkip(${firstPageNumber});"><span>首页</span></a>
			</li>
		[/#if]
		[#if hasPrevious]
			<li>
				<a  href="javascript: $.pageSkip(${previousPageNumber});">上页</a>
			</li>
		[#else]
			<li class="disabled"><span>上页</span></li>
		[/#if]
		[#list segment as segmentPageNumber]
			[#if segmentPageNumber_index == 0 && segmentPageNumber > firstPageNumber + 1]
				<li ><a href="#">...</a></li>
			[/#if]
			[#if segmentPageNumber != pageNumber]
			<li>
				<a href="javascript: $.pageSkip(${segmentPageNumber});">${segmentPageNumber}</a>
				</li>
			[#else]
				<li class="active"><a href="#">${segmentPageNumber}</a></li>
			[/#if]
			[#if !segmentPageNumber_has_next && segmentPageNumber < lastPageNumber - 1]
				<li ><a href="#">...</a></li>
			[/#if]
		[/#list]
		[#if hasNext]
			 <li ><a href="javascript: $.pageSkip(${nextPageNumber});">下页</a></li>
		[#else]
			 <li class="disabled"><span>下页</span></li>
		[/#if]
		[#if isLast]
			 <li class="disabled"><span>尾页</span></li>
		[#else]
			<li>
				<a  href="javascript: $.pageSkip(${lastPageNumber});">尾页</a>
			</li>
		[/#if]
		<li class="pageSkip" style="display:none">
			${message("csh.page.totalPages", totalPages)} ${message("csh.page.pageNumber", '<input id="pageNumber" name="pageNumber" value="' + pageNumber + '" maxlength="9" onpaste="return false;" />')}<button type="submit" style="display:none">GO</button>
		</li>
	</ul>
</div>
	
	
	