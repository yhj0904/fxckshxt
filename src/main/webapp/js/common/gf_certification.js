var CERT_TIMER;
var CERT_SECOND;

//2차 인증
function gf_openCertificationPop (certification) {
		
	if(gf_isNull(certification)){
		alert("Error");
		return;
	}
	
	if(certification.result){
		var str =  '<div id="certification_pop" class="certification_pop open">';
			str += '	<div class="inner">';
			str += '		<div class="msg">'+certification.displayMsg+'</div>';
			str += '		<div class="input">';
			str += '			<div class="input_cert">';
			str += '				<input id="certKey" type="text" placeholder="'+CERTIFICATION_MSG.EMPTY+'">';
			str += '				<span id="certTimer" class="timer">00:00</span>';				
			str += '			</div>';
			str += '			<a href="javascript:fn_reCertification();">'+CERTIFICATION_MSG.RESEND+'</a>';			
			str += '		</div>';
			str += '		<div class="bottom">';
			str += '			<ul>';
			str += '				<li><a href="javascript:fn_checkCertification();" class="button">'+CERTIFICATION_MSG.CERT+'</a></li>';
			str += '				<li><a href="javascript:gf_closeCertification();" class="button ty2">'+GV_MESSAGE.CLOSE+'</a></li>';
			str += '			</ul>';
			str += '		</div>';
			str += '	</div>';
			str += '</div>';
		
		$("body").append(str);
		
		$("#certKey").focus();
		
		//interval 시작
		gf_initTimer(certification.second);
	} else {
		alert(certification.errMsg);
	}
}
function gf_closeCertification() {
	gf_clearTimer();
	$("#certification_pop").remove();
}

$("#saveId").change(function() {
	if (!$("#saveId").is(":checked")) {
		gf_removeCookie("SAVE_USER_ID");
	}
});

function gf_initTimer(second) {
	gf_clearTimer();
	CERT_SECOND = second / 1000;
	gf_setTimer();
}

function gf_setTimer(){
	
	var timer = $("#certTimer");

	rMinute = parseInt(CERT_SECOND / 60);
	rMinute = rMinute % 60;

	rSecond = CERT_SECOND % 60;

	if (CERT_SECOND > 0) {
		timer.html(Lpad(rMinute, 2) + ":" + Lpad(rSecond, 2));
		CERT_SECOND--;
		CERT_TIMER = setTimeout("gf_setTimer()", 1000);
	} else {
		alert(CERTIFICATION_MSG.TIME);
		gf_closeCertification();
	}
}

function gf_clearTimer(){
	clearTimeout(CERT_TIMER);
	CERT_SECOND = 0;
}

Lpad = function(str, len) {
	str = str + "";
	while (str.length < len) {
		str = "0" + str;
	}
	return str;
}