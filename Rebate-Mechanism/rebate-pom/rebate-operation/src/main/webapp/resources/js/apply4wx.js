    $(function(){
    	var $current_fs, $next_fs, $previous_fs; //fieldsets
    	var left, opacity, scale; //fieldset properties which we will animate
    	var animating; //flag to prevent quick multi-click glitches 
    	var $applyForm = $("#applyForm");
		var $areaId = $("#areaId");
		
    	var map = new BMap.Map("allmap");            // 创建Map实例
    	    var point = new BMap.Point(116.404, 39.915); // 创建点坐标
    	    map.centerAndZoom(point,15);                 // 初始化地图,设置中心点坐标和地图级别。
    	    map.addControl(new BMap.ZoomControl());      //添加地图缩放控件
    	    function showInfo(e){
    			//alert(e.point.lng + ", " + e.point.lat);
    			$("#longitude").val(e.point.lng);
    			$("#latitude").val(e.point.lat);
    			map.clearOverlays()
    			var marker1 = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));
    			map.addOverlay(marker1);
    		}
    		map.addEventListener("click", showInfo);
    		
		// 地区选择
		$areaId.lSelect({
			url: "../console/common/area.jhtml",
			cssStyle: {"margin": "4px 0","height":"20px","display":"block"},
		});
		
		 formValidate =  $applyForm.validate({
			rules: {
				tenantName:{
					required:true
				},
				contactPhone: {
					required:true,
					isMobile:true
				},
				contactPerson:{
					required:true
				},
				email:{
					required:true,
					email:true
				},
				areaId_select:{
					required:true
				},
				address:{
					required:true
				}
			},
			messages:{
				tenantName:{
					required:"商家企业名字不能为空"
				},
				contactPhone: {
					required:"联系电话不能为空"
				},
				contactPerson:{
					required:"联系人不能为空"
				},
				email:{
					required:"邮箱不能为空",
					email:"邮箱格式不正确"
				},
				areaId_select:{
					required:"地区不能为空"
				},
				address:{
					required:"地址不能为空"
				},
				licenseFile:{
					required:"请上传营业执照"
				},
				photoFile:{
					required:"请上传门店照片"
				}
			},
			submitHandler:function(form){
	            $applyForm.ajaxSubmit({
			      	dataType:"json",
			       	beforeSubmit:function(){
			       		$('input[type="submit"]').attr("disabled","disabled");
			       	},
			       	success:function(result){
			       		if(result.type == "success" ){
							console.log(result);
							alert("信息提交成功!");
							location.reload();
						} else {
							alert("提交失败,请重新提交");
							return false;
						}
			       		
			       }
	            });
	        }     
		});
		
		
        $(':input[type="file"]').change(function(evt){
            if (!window.FileReader) return;
            var $this = $(this);
            var files = evt.target.files;
            for (var i = 0, f; f = files[i]; i++) {
                if (!f.type.match('image.*')) {
                    continue;
                }
                var reader = new FileReader();
                reader.onload = (function(theFile) {
                    return function(e) {
                        // img 元素
                       // document.getElementById('previewImage').src = e.target.result;
                        $this.next().show().find("img").attr("src",e.target.result);
                    };
                })(f);
                reader.readAsDataURL(f);
            }
        });
        
        $(".next").click(function(){
        	var flag =false;
        	$current_fs = $(this).parent().parent();
        	var $this =$(this);
        	var btnId = $this.attr("id");
        	if("firstNextBtn" == btnId){
        		flag = formValidate.form();
        		var address = $("select[name='areaId_select']  option:selected").text() +$("input[name='address']").val();
        		console.log(address)
        		if(address && address !="请选择地址..."){
        			var myGeo = new BMap.Geocoder();
            		// 将地址解析结果显示在地图上,并调整地图视野
            		myGeo.getPoint(address, function(point){
            			if (point) {
            				$("#longitude").val(point.lng);
                			$("#latitude").val(point.lat);
            				map.clearOverlays()
            				map.centerAndZoom(point, 15);
            				map.addOverlay(new BMap.Marker(point));
            			}else{
            				//alert("您选择地址没有解析到结果!");
            			}
            		}, address);
        		}
        		//flag = true;
        	}else if("secondNextBtn" == btnId){
        		var $longitude = $(':input[name="longitude"]');
        		var $latitude = $(':input[name="latitude"]');
        		if($longitude.val() && $latitude.val()){
        			flag = true;
        			$("#licenseFile").rules("add",{
	       				 required: true
	       			});
        			$("#photoFile").rules("add",{
	       				 required: true
	       			});
        		}else{
        			alert("请上传位置点");
        		}
        	}
        	if(flag){
        		$next_fs = $(this).parent().parent().next();
        		$("#progressbar li").eq($("fieldset").index($next_fs)).addClass("active");
        		$next_fs.show();
        	    $current_fs.hide();
        		$current_fs.animate({opacity: 0}, {
        			step: function(now, mx) {
        				scale = 1 - (1 - now) * 0.2;
        				left = (now * 50)+"%";
        				opacity = 1 - now;
        				$current_fs.css({'transform': 'scale('+scale+')'});
        				$next_fs.css({'left': left, 'opacity': opacity});
        			}, 
        			duration: 800, 
        			complete: function(){
        				$current_fs.hide();
        				animating = false;
        			}, 
        			easing: 'easeInOutBack'
        		});
        	}
        });

        $(".previous").click(function(){
        	if(animating) return false;
        	animating = true;
        	var $this =$(this);
        	var btnId = $this.attr("id");
        	if("threeNextBtn" == btnId){
        		$("#licenseFile").rules("remove"); 
        		$("#photoFile").rules("remove");  
        	}
        	$current_fs = $(this).parent().parent();
        	$previous_fs = $(this).parent().parent().prev();
        	$("#progressbar li").eq($("fieldset").index($current_fs)).removeClass("active");
            $current_fs.hide();
        	$previous_fs.show(); 
        	$current_fs.animate({opacity: 0}, {
        		step: function(now, mx) {
        			scale = 0.8 + (1 - now) * 0.2;
        			left = ((1-now) * 50)+"%";
        			opacity = 1 - now;
        			$current_fs.css({'left': left});
        			$previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
        		}, 
        		duration: 800, 
        		complete: function(){
        			$current_fs.hide();
        			animating = false;
        		}, 
        		easing: 'easeInOutBack'
        	});
        });
        
    })
