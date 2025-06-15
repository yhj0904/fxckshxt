/**
 * ========================
 * 폼 필드 관련 공통 JS
 * ========================
 * 
 */
//변수설정
$(function(){
	
	var day_names = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
 	var day_names_min = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
	var month_names = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];	
	if(GV_LANGUAGE == 'ko') {
		day_names = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
	 	day_names_min = ['일', '월', '화', '수', '목', '금', '토'];
		month_names = ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'];
	}
	
	//datetime picker 설정
	$.datepicker.setDefaults({
		dateFormat: 'yy-mm-dd',
		//showOn: 'button',
		changeMonth: true,
		changeYear: true,
		showMonthAfterYear:true,
		dayNames: day_names,
     	dayNamesMin: day_names_min,
     	yearRange: '1900:2999',
     	monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
   		monthNames: month_names
	});
	
	//이메일 도메인 선택시 이벤트
	$('div.email > select').on('change', function(){
		var emailObj = $(this).closest('div.email');
		var dataPath = $(this).attr("data-path");
		var id = $(emailObj).find(".id").val();
		var domain = $(this).val();
		if(domain == 'etc'){
			$(emailObj).find('input.etc').css('display', 'inline-block');
			$(emailObj).find('input.etc').focus();
			domain = $(emailObj).find(".etc").val();
		} else {
			$(emailObj).find('input.etc').css('display', 'none');
			$(emailObj).find('input.etc').val("");
		}
		var email = id + "@" + domain;
		
		$("#"+dataPath).val(email);
	});
	
	$('div.email > input.id, div.email > input.etc').on('keyup', function(){
		var emailObj = $(this).closest('div.email');
		var dataPath = $(this).attr("data-path");
		var id = $(emailObj).find(".id").val();
		var domain = $(emailObj).find(".domain").val();
		if(domain == 'etc'){
			domain = $(emailObj).find(".etc").val();
		}
		var email = id + "@" + domain;
		
		$("#"+dataPath).val(email);
	});
	
	//드랍다운
	$('.dropdown>button').on('click', function() {
		var drop_obj = $(this).closest('.dropdown');
		if(!drop_obj.hasClass("open")){
			$(this).closest('.dropdown').addClass('open');
			$(this).next().find('li').last().focusout(function() {
				drop_obj.removeClass('open');
			});
			$(".dropdown").not(drop_obj).removeClass('open');
		} else {
			$(this).closest('.dropdown').removeClass('open');
		}
	}).focus(function() {
		$(this).mouseover();		
	}).next().mouseleave(function() {
		var drop_obj = $(this).closest('.dropdown');
		drop_obj.closest('.dropdown').removeClass('open');
	});
	
	//전화번호 변경 이벤트
	$('div.tel > select.tel1').on('change', function(){
		var divObj = $(this).closest('div.tel');
		var dataPath = $(this).attr("data-path");
		
		var tel1 = $(divObj).find(".tel1").val();
		var tel2 = $(divObj).find(".tel2").val();
		var tel3 = $(divObj).find(".tel3").val();
		
		var tel = tel1 + "-" + tel2 + "-" + tel3;
		
		$("#"+dataPath).val(tel);
	});
	
	$('div.tel > input.tel2, div.tel > input.tel3').on('keyup', function(){
		var divObj = $(this).closest('div.tel');
		var dataPath = $(this).attr("data-path");
		
		var tel1 = $(divObj).find(".tel1").val();
		var tel2 = $(divObj).find(".tel2").val();
		var tel3 = $(divObj).find(".tel3").val();
		
		var tel = tel1 + "-" + tel2 + "-" + tel3;
		
		$("#"+dataPath).val(tel);
	});
	
});

$(document).on("change", 'label.file > input[type=file]', function(){
	var labelObj = $(this).parent('label.file');
	var imgId = $(this).attr("data-img");
	if(this.files.length == 0){
		labelObj.find('span.name').text('Input File');
		if(!gf_isNull(imgId)){
			 $('#'+imgId).attr('src', '/images/common/no_img.png');
			 $('#'+imgId).attr('alt', 'NO IMG');
		}
		return;
	}
	var size = this.files[0].size;
	var name = this.files[0].name;
	var limitSize = $(this).attr("data-file");
	var fileSize = 10485760;
	if(!gf_isNull(limitSize)){
		fileSize = Number(limitSize) * 1024;
	}
	if(this.files[0].size && fileSize >= this.files[0].size){
		var fLength = name.length;
		var lastDot = name.lastIndexOf('.');
		var fExt = name.substring(lastDot, fLength).toLowerCase();
		var fName = name.substring(0, lastDot).toLowerCase();
		labelObj.find('span.name').text(name);
		//이미지 아이디 있는경우 미리보기 설정
		
		if(!gf_isNull(imgId)){				
			//확장자 체크 후 이미지 확장자가 아닌경우 RETURN				
			var reader = new FileReader();				 
	        reader.onload = function (e) {
	            $('#'+imgId).attr('src', e.target.result);
	            $('#'+imgId).attr('alt', fName);
	        }		 
	        reader.readAsDataURL(this.files[0]);
		}
	}
});

//datepicker
function gf_datepicker(obj, option) {
	var _option = {}
	if(!gf_isNull(option)){
		_option = option;
	}
	$(obj).datepicker(_option).datepicker("show");
	$('.ui-datepicker-trigger').hide();
}

//byte 체크
function gf_checkByte(obj, maxByte, countObj){
	
	if(gf_isNull(maxByte)){
		maxByte = 2000;
	}
	
	var str = obj.value;
	var str_len = str.length;

	var rbyte = 0;
	var rlen = 0;
	var one_char = "";
	var str2 = "";

	for(var i=0; i<str_len; i++){
		one_char = str.charAt(i);
		if(escape(one_char).length > 4){
		    rbyte += 3;
		}else{
		    rbyte++;
		}

		if(rbyte <= maxByte){
		    rlen = i+1;
		}
	}
	if (rbyte > maxByte){
	    str2 = str.substr(0, rlen);
	    obj.value = str2;
	    fn_checkByte(obj, maxByte);
	} else {
		if(!gf_isNull(countObj)){
			$(countObj).text(rbyte + "/" + maxByte);
		}	    
	}
}