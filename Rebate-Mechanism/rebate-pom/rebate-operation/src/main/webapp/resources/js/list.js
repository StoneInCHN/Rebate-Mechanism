
$().ready( function() {

	var $listForm = $("#listForm");
	var $pageTotal = $("#pageTotal");
	var $addButton = $("#addButton");
	var $deleteButton = $("#deleteButton");
	var $activeButton = $("#activeButton");
	var $inActiveButton = $("#inActiveButton");
	var $withdrawButton = $("#withdrawButton");
	var $lockedButton = $("#lockedButton");
	var $refreshButton = $("#refreshButton");
	var $moreOperation = $("#moreOperation");
	var $searchPropertyOption = $("#searchPropertyOption li");
	var $searchValue = $("#searchValue");
	var $listTable = $("#listTable");
	var $selectAll = $("#selectAll");
	var $ids = $("#listTable input[name='ids']");
	var $contentRow = $("#listTable tr:gt(0)");
	var $sort = $("#listTable a.sort");
	var $pageSize = $("#pageSize");
	var $searchProperty = $("#searchProperty");
	var $orderProperty = $("#orderProperty");
	var $orderDirection = $("#orderDirection");
	var $pageNumber = $("#pageNumber");
	var $promptButton = $('#promptButton');
	var $backButton = $("#backButton");
	var $deviceProvide = $("#deviceProvide");
	
	//添加
	$addButton.click(function(){
		location.href="add.jhtml";
	});
		
	// 删除
	$deleteButton.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		$.dialog({
			type: "warn",
			content: message("admin.dialog.deleteConfirm"),
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "delete.jhtml",
					type: "POST",
					data: $checkedIds.serialize(),
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$pageTotal.text(parseInt($pageTotal.text()) - $checkedIds.size());
							$checkedIds.closest("tr").remove();
							if ($listTable.find("tr").size() <= 1) {
								setTimeout(function() {
									location.reload(true);
								}, 3000);
							}
						}
						$deleteButton.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
	
	$withdrawButton.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		$.dialog({
			type: "warn",
			content: message("admin.dialog.withdrawConfirm"),
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "batchWithdrawal.jhtml",
					type: "POST",
					data: $checkedIds.serialize(),
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							setTimeout(function() {
								location.reload(true);
							}, 3000);
						}
						$withdrawButton.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
	
	// 启用
	$activeButton.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		$.dialog({
			type: "warn",
			content: message("admin.dialog.activeConfirm"),
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "active.jhtml",
					type: "POST",
					data: $checkedIds.serialize(),
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							setTimeout(function() {
								location.reload(true);
							}, 2000);
						}
						$activeButton.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
	
	// 禁用
	$inActiveButton.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		$.dialog({
			type: "warn",
			content: message("admin.dialog.inActiveConfirm"),
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "inActive.jhtml",
					type: "POST",
					data: $checkedIds.serialize(),
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							setTimeout(function() {
								location.reload(true);
							}, 2000);
						}
						$inActiveButton.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
	
	// 删除
	$lockedButton.click( function() {
		var $this = $(this);
		if ($this.hasClass("disabled")) {
			return false;
		}
		var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
		$.dialog({
			type: "warn",
			content: message("admin.dialog.lockedConfirm"),
			ok: message("admin.dialog.ok"),
			cancel: message("admin.dialog.cancel"),
			onOk: function() {
				$.ajax({
					url: "locked.jhtml",
					type: "POST",
					data: $checkedIds.serialize(),
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							setTimeout(function() {
								location.reload();
							}, 1000);							
						}
						$lockedButton.addClass("disabled");
						$selectAll.prop("checked", false);
						$checkedIds.prop("checked", false);
					}
				});
			}
		});
	});
	
	// 刷新
	$refreshButton.click( function() {
		location.reload(true);
		return false;
	});
	

	
	// 更多选项
	$moreOperation.mouseover( function() {
		var $this = $(this);
		var offset = $this.offset();
		var $menuWrap = $this.closest("div.menuWrap");
		var $popupMenu = $menuWrap.children("div.popupMenu");
		$popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
		$menuWrap.mouseleave(function() {
			$popupMenu.hide();
		});
	});
	
	
	// 搜索选项
	$searchPropertyOption.click( function() {
		var $this = $(this);
		$searchProperty.val($this.attr("title"));
		$searchPropertyOption.removeClass("active");
		$this.addClass("active");
		return false;
	});
	
	// 全选
	$selectAll.click( function() {
		var $this = $(this);
		var $enabledIds = $("#listTable input[name='ids']:enabled");
		if ($this.prop("checked")) {
			$enabledIds.prop("checked", true);
			if ($enabledIds.filter(":checked").size() > 0) {
				$deleteButton.removeClass("disabled");
				$withdrawButton.removeClass("disabled");
				$lockedButton.removeClass("disabled");
				$activeButton.removeClass("disabled");
				$inActiveButton.removeClass("disabled");
				$promptButton.removeClass("disabled");
				$contentRow.addClass("selected");
			} else {
				$deleteButton.addClass("disabled");
				$activeButton.addClass("disabled");
				$inActiveButton.addClass("disabled");
				$withdrawButton.addClass("disabled");
				$lockedButton.addClass("disabled");
			}
		} else {
			$enabledIds.prop("checked", false);
			$deleteButton.addClass("disabled");
			$withdrawButton.addClass("disabled");
			$lockedButton.addClass("disabled");
			$contentRow.removeClass("selected");
			$activeButton.addClass("disabled");
			$inActiveButton.addClass("disabled");
		}
	});
	
	// 选择
	$ids.click( function() {
		var $this = $(this);
		if ($this.prop("checked")) {
			$this.closest("tr").addClass("selected");
			$deleteButton.removeClass("disabled");
			$withdrawButton.removeClass("disabled");
			$lockedButton.removeClass("disabled");
			$promptButton.removeClass("disabled");
			$activeButton.removeClass("disabled");
			$inActiveButton.removeClass("disabled");
		
		} else {
			$this.closest("tr").removeClass("selected");
			if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
				$deleteButton.removeClass("disabled");
				$withdrawButton.removeClass("disabled");
				$lockedButton.removeClass("disabled");
				$promptButton.removeClass("disabled");
				$activeButton.removeClass("disabled");
				$inActiveButton.removeClass("disabled");
			} else {
				$deleteButton.addClass("disabled");
				$withdrawButton.addClass("disabled");
				$lockedButton.addClass("disabled");
				$promptButton.addClass("disabled");
				$activeButton.addClass("disabled");
				$inActiveButton.addClass("disabled");
			}
		}
	});
	
	// 排序
	$sort.click( function() {
		var orderProperty = $(this).attr("name");
		if ($orderProperty.val() == orderProperty) {
			if ($orderDirection.val() == "asc") {
				$orderDirection.val("desc");
			} else {
				$orderDirection.val("asc");
			}
		} else {
			$orderProperty.val(orderProperty);
			$orderDirection.val("asc");
		}
		$pageNumber.val("1");
		$listForm.submit();
		return false;
	});
	
	// 排序图标
	if ($orderProperty.val() != "") {
		$sort = $("#listTable a[name='" + $orderProperty.val() + "']");
		if ($orderDirection.val() == "asc") {
			$sort.removeClass("desc").addClass("asc");
		} else {
			$sort.removeClass("asc").addClass("desc");
		}
	}
	
	// 页码输入
	$pageNumber.keypress(function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key == 13 && $(this).val().length > 0) || (key >= 48 && key <= 57)) {
			return true;
		} else {
			return false;
		}
	});
	
	// 表单提交
	$listForm.submit(function() {
		if (!/^\d*[1-9]\d*$/.test($pageNumber.val())) {
			$pageNumber.val("1");
		}
				
		if ($searchValue.size() > 0 && $searchValue.val() != "" && $searchProperty.val() == "") {
			console.log($searchPropertyOption.eq(0));
			$searchProperty.val($searchPropertyOption.eq(0).attr("title"));
		}	
		
	});
	
	// 页码跳转
	$.pageSkip = function(pageNumber) {
		$pageNumber.val(pageNumber);
		$listForm.submit();
		return false;
	}
	

});
