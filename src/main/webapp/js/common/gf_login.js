$(function() {
	//쿠키를 읽은 후 있는 경우
	if(!gf_isNull(gf_getCookie("SAVE_USER_ID"))){
		document.detailForm.inputId.value = gf_getCookie("SAVE_USER_ID");
		$("#saveId").prop("checked", true);
	}
});

//로그인 처리
function fn_loginAction() {
	
	var id = $("#inputId").val();
	var pw = $("#inputPw").val();
	$("#loginId").val(id);
	$("#loginPw").val(pw);
	frm = document.detailForm;
	if (!validateLoginVO(frm)) {
		return;
	}
	
	//아이디 저장 체크
	if ($("#saveId").is(":checked")) {
		var id = frm.loginId.value;
		gf_setCookie("SAVE_USER_ID", id, 0);
	} else {
		gf_removeCookie("SAVE_USER_ID");
	}

	//RSA 생성
	fn_initRsa();
};

//RSA 생성
function fn_initRsa() {		
	gf_ajax({
		url : "/user/initRsa.json",
		type : "POST",
		dataType : "json",
		data : {},
	}).then(function(response){
		if (response != null) {
			var rsaModulus = response[0];
			var rsaExponent = response[1];
			var rsa = gf_initRSA(rsaModulus, rsaExponent);
			//RSA로 비밀번호 암호화
			if (gf_encryptRSA(rsa, $("#loginId")) && gf_encryptRSA(rsa, $("#loginPw"))) {
				fn_checkLogin();
			} else { //암호화 실패시
				alert("Error");
			}
		} else {
			alert("Error");
		}
	});
}

//아이디 & 비밀번호 체크
function fn_checkLogin() {
	var checkUrl = "/login/checkLogin.json";
	if(String.prototype.startsWith) {
		if(GV_PRESENT_PATH.startsWith('/sys')){
			checkUrl = "/sys/login/checkLogin.json";
		}
	} else {
		if(checkUrl.substring(0, 10) === '/sys'){
			checkUrl = "/sys/login/checkLogin.json";
		}
	}
	
	gf_ajax({
		url : checkUrl,
		type : "POST",
		dataType : "json",
		data : {
			loginId : $("#loginId").val(),
			loginPw : $("#loginPw").val()
		},
	}).then(function(response){
		if(response.result){
			if(response.errCd == "IS_LOGIN"){									
				var msg = confirm(response.errMsg);
				if(msg == true){
					if(!gf_isNull(response.certification)){
						gf_openCertificationPop(response.certification);
					} else {
						document.detailForm.submit();							
					}						
				}
			} else {
				if(!gf_isNull(response.certification)){
					gf_openCertificationPop(response.certification);
				} else {
					document.detailForm.submit();							
				}
			}
		} else {
			alert(response.errMsg);
		}
	});
}

function fn_reCertification() {	
	gf_ajax({
		url : "/login/reCertification.json",
		type : "POST",
		dataType : "json",
	}).then(function(response){
		if(response.result){
			gf_initTimer(response.second);
		} else {
			alert(response.errMsg);
			gf_closeCertification();
		}
	});
}

function fn_checkCertification() {	
	if(gf_isNull($("#certKey").val())){
		alert(CERTIFICATION_MSG.EMPTY);
		$("#certKey").focus();
		return;
	}
	
	gf_ajax({
		url : "/login/checkCertification.json",
		type : "POST",
		dataType : "json",
		data : {
			certKey : $("#certKey").val()
		},
	}).then(function(response){
		if(response.result){
			$("#loginCertKey").val(response.encryptCertKey);
			document.detailForm.submit();
		} else {
			alert(response.errMsg);
		}
	});
}

$("#saveId").change(function() {
	if (!$("#saveId").is(":checked")) {
		gf_removeCookie("SAVE_USER_ID");
	}
});

//아이디 찾기
function fn_findUserId() {
	location.href = "/find/userId.do";
}

//아이디 찾기
function fn_findUserPw() {
	location.href = "/find/userPw.do";
}

//회원가입
function fn_joinView() {
	location.href = "/join.do";
}