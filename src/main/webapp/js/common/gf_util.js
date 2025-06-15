/**
 * ========================
 * 공통 UTIL JS
 * ========================
 * gf_isNull            : 입력값이 null에 해당하는 경우 모두를 한번에 체크한다.
 * gf_getBrowser		: 브라우저 return
 * gf_nvl              : 입력값이 null인 경우에 지정한 값으로 반환.
 * gf_toString         : 입력값을 String으로 변환
 * gf_toNumber	        : 입력값을 숫자값으로 변환
 * gf_indexOf          : 전체 문자열 중 특정 문자열이 포함된 위치를 반납
 * gf_lastIndexOf      : 전체 문자열 중 마지막에서 특정 문자열이 포함된 위치를 반납
 * gf_length           : 입력값 형태에 따라서 길이 또는 범위를 구하는 함수
 * gf_getHref			: 현재페이지의 Href를 가져옴
 * gf_getHost			: 현재페이지의 호스트명 가져옴
 * gf_getPort			: 현재페이지의 포트번호를 가져옴
 * gf_getHostName		: 현재페이지의 호스트명과 포트번호 가져옴
 * gf_getPathName		: 현재페이지의 PathName을 가져옴
 * gf_getProtocol		: 현재페이지의 Protocal을 가져옴
 * gf_moveHref			: 특정 URL로 이동
 * gf_movePath			: 특정 Path로 이동
 * gf_moveHome			: 메인으로 이동
 * gf_trim            	: 입력된 문자열의 좌우측 공백을 제거한 문자열을 구함.
 * gf_getFileName      : 파일경로에서 파일명을 가져온다.
 * gf_getFileExt       : 파일 확장자를 가져온다.
 * gf_replaceAll		: 입력된 문자열의 특정 문자를 변경
 * gf_htmlToChars      : html형식의 문자열에서 태그문자를 특수문자로 변형
 * gf_specToChars      : 특수문자가 들어있는 문자열에서 html형식의 문자로 변형
 * gf_right            : 문자열의 오른쪽부분을 지정한 길이만큼 Return 한다.
 * gf_left             : 문자열의 왼쪽부분을 지정한 길이만큼 Return 한다.
 * gf_openPop		   : 팝업창 오픈
 * gf_copyTxt			: 텍스트 복사
 * gf_setCookie		   : 쿠키 설정
 * gf_isCapsLock	    : 쿠키 설정
 */

function gf_isNull(val){
	
	if (new String(val).valueOf() == "undefined" || new String(val).valueOf() == "null" || val == undefined || val == null) {
		return true;
	}
	else {
		var strVal = new String(val);
		if (strVal == null || strVal.trim() == "") {
			return true;
		}
		if (strVal.length == 0){
			return true;
		}
	}
	return false;	
}

function gf_getBrowser(){
    
	if( (navigator.userAgent.indexOf("MSIE") != -1) || (navigator.appName == "Netscape" && navigator.userAgent.indexOf("Trident") != -1 ) ){
         return "MSIE";
     }else if(navigator.userAgent.indexOf("Chrome") != -1){
         return "Chrome";
     }else if(navigator.userAgent.indexOf("Firefox") != -1){
         return "Firefox";
     }else  if(navigator.userAgent.indexOf("Mozilla") != -1){
         return "Mozilla";
     }else if(navigator.userAgent.indexOf("Opera") != -1){
         return "Opera";
     }else if(navigator.userAgent.indexOf("Safari") != -1){
         return "Safari";
     }else if(navigator.userAgent.indexOf("Mac") != -1){
         return "Mac";
     }else{
         return "";
     }
}

function gf_nvl(val, val2){
	if (!gf_isNull(val)){
		return val;
	} else {
		return val2;
	}
	
}

function gf_toString(val){
	if (gf_isNull(val)){
		return new String();
	} else {
		return new String(val);
	}
}

function gf_toNumber(val){
	if (gf_isNull(val)){
		return 0;
	} else {
		var str = gf_toString(val);
		return parseInt(str);
	}
}

function gf_indexOf(val, str, startIndex){
	
	if (gf_isNull(startIndex)){
		startIndex = 0;
	}
	
	return gf_toString(val).indexOf(str, startIndex);
}

function gf_lastIndexOf(val, str , endIndex){
	
	if (gf_isNull(endIndex)){
		endIndex = 0;
	}
	
	return gf_toString(val).lastIndexOf(str, endIndex);
}

function gf_length(val){
	return gf_toString(val).length;
}

function gf_getHref(){
	return document.location.href;
}

function gf_getHost(){
	return document.location.host;
}

function gf_getPort(){
	return document.location.port;
}

function gf_getHostName(){
	return document.location.hostname;
}

function gf_getPathName(){
	return document.location.pathname;
}

function gf_getProtocol(){
	return document.location.protocol;
}

function gf_moveHref(url, type){
	
	if (gf_isNull(type)){
		type = "self";
	}
	
	if (gf_isNull(url)){
		return;
	}
	
	switch (type) {
		case "self" :
			window.location.href = url;
			break;
		case "blank" :
			window.open("about:blank").location.href= url;
			break;
			
	}
}

/*function gf_movePath(pathName, type){
	if (gf_isNull(type)){
		type = "self";
	}
	if (gf_isNull(pathName)){
		return;
	}
	if (gf_indexOf(pathName, "/") == -1 || gf_indexOf(pathName, "/") != 0) {
		pathName = "/" + pathName;
	}
	var url = gf_getProtocol() + "//" + gf_getHost() + pathName;
	
	switch (type) {
		case "self" :
			window.location.href = url;
			break;
		case "blank" :
			window.open("about:blank").location.href= url;
			break;
	}
	return;
}*/

function gf_moveHome(){
	/*var url = gf_getProtocol() + "//" + gf_getHost();
	window.location.href = url;*/
	window.location.href = "/index.do";
}

function gf_trim(val){
	
	if (gf_isNull(val)){
		return "";
	} else {
		return gf_toString(val).replace(/^\s+|\s+$/g, '');
	}
	
}

function gf_getFileName(filePath)
{
	var fileName = "";
	filePath = gf_toString(filePath);
	for (var i=0; i<filePath.length; i++){
		if (gf_right(filePath, i + 1).indexOf("\\") > -1){
			break;
		}
		if (gf_right(filePath, i + 1).indexOf("\/") > -1){
			break;
		}
		fileName = gf_right(filePath, i + 1);
	}
	return fileName;
}

function gf_getFileExt(fileName)
{
	fileName = gf_toString(fileName);
	var arrFileName = fileName.split(".");
	var fileExt = arrFileName[arrFileName.length - 1];
	
	return gf_trim(fileExt);
}

function gf_replaceAll(val, oldVal, replaceVal) {
	if (gf_isNull(val)){
		return "";
	}
	return val.split(oldVal).join(replaceVal);
}

function gf_htmlToChars(val) {
	
	if (gf_isNull(val)){
		return "";
	}
	
	val = gf_replaceAll(val, "\&", '&amp;');
	val = gf_replaceAll(val, "\'", '&apos;');
	val = gf_replaceAll(val, "\"", '&quot;');
	val = gf_replaceAll(val, "\'", '&#39;');
	val = gf_replaceAll(val, "<", '&lt;');
	val = gf_replaceAll(val, ">", '&gt;');
	
	return val;
}

function gf_specToChars(val) {
	
	if (gf_isNull(val)){
		return "";
	}
	
	val = gf_replaceAll(val, "\&amp;", '&');
	val = gf_replaceAll(val, "\&quot;", '"');
	val = gf_replaceAll(val, "\&#39;", '\'');
	val = gf_replaceAll(val, "\&lt;", '<');
	val = gf_replaceAll(val, "\&gt;", '>');
	
	return val;
}

function gf_right(val, size)
{
	if (gf_isNull(val)){
		return "";
	}
	
	var start = gf_toString(val).length;
	var end = Number(start) - Number(size);
	
	return val.substring(start, end);
}

function gf_left(val, size)
{
	if (gf_isNull(val)){
		return "";
	}
	
	return gf_toString(val).substr(0, size);
}

function gf_resetForm(frm)
{
	var fileDom;
	var elements = frm.elements;
	for (var i = 0; i < elements.length; i++) {
		if (elements[i].type == 'hidden') continue;
        if (elements[i].type == 'text') elements[i].value = '';
        if (elements[i].type == 'checkbox') elements[i].checked = false;
        if (elements[i].type == 'radio') elements[i].checked = false;
        if (elements[i].type == 'select-one') elements[i].options[0].selected = true;
        if (elements[i].type == 'textarea') elements[i].value = '';
    }
}

function gf_openPop(url, width, height)
{
	if(gf_isNull(width)){
		width = "800";
	}
	
	if(gf_isNull(height)){
		height = "600";
	}
	
	var x = (screen.availWidth - width) / 2;
	var y = (screen.availHeight - height) / 2;
	
	window.open(url, "", "width="+width+", height="+height+", left="+x+", top="+y+", toolbar=no, menubar=no, scrollbars=no, resizable=yes");  
}

function gf_copyToClip(txt){
	if(gf_isNull(txt)){
		return;
	}
	if (gf_getBrowser() == "MSIE") {
		window.clipboardData.setData("Text", txt);
		alert("복사되었습니다.");
		return;
	} else {
		var encodeTxt = encodeURI(txt);
		var temp = document.createElement("input");
		document.body.appendChild(temp);
		temp.value = encodeTxt;
		temp.select();
		document.execCommand('copy');
		document.body.removeChild(temp);
		alert("주소가 복사되었습니다.");
	}
}

function gf_setCookie(name, value, days, path){
	if(!gf_isNull(gf_getCookie(name))){
		gf_removeCookie(name);
	}
	var _path = "/";
	if(!gf_isNull(path)){
		_path = path;
	}
	if(!gf_isNull(days)){
		if(days == 0){
			document.cookie = name + "=" + value + ";path="+_path;
		} else {
			var date = new Date();
			date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
			document.cookie = name + "=" + value + ";path="+_path+";expires="+date.toUTCString();
		}
	} else {
		var date = new Date();
		date.setHours(23, 59, 59, 999);
		document.cookie = name + "=" + value + ";path="+_path+";expires="+date.toUTCString();
	}
};

function gf_getCookie(name){
	var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	return value? value[2] : null;
};

function gf_removeCookie(name){
	document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};

function gf_getCalendar(year, month) {
	
	var result = {};
	var _cal = [];
	
	var _date;
	var _year = gf_toNumber(year);
	var _month = gf_toNumber(month);
	if (!gf_isNull(year) && !gf_isNull(month)) {
		_year = gf_toNumber(year);
		_month = gf_toNumber(month);
		_date = new Date(_year, _month-1, 1);
	} else {		
		_date = new Date();
		_year = _date.getYear();
		_month = _date.getMonth +1;
	}
	
	var _date1 = new Date(_date.getFullYear(), _date.getMonth(), 1);
	var _firstDay = _date1.getDate();	
	var _firstYoil = _date1.getDay();
	
	var _date2 = new Date(_date.getFullYear(), _date.getMonth()+1, 0);
	var _lastDay = _date2.getDate();
	var _lastYoil = _date2.getDay();
	
	var _prevDate = new Date (_date1.setDate(_date1.getDate() - 1 ));
	var _prevYear = _prevDate.getFullYear();
	var _prevMonth = _prevDate.getMonth() + 1;
	var _prevLastDay = _prevDate.getDate();
	
	var _nextDate = new Date (_date2.setDate(_date2.getDate() +1 ));
	var _nextYear = _nextDate.getFullYear();
	var _nextMonth = _nextDate.getMonth() + 1;
	
	var yoilIdx = 0;
	
	if (_firstYoil != 0){
		for (var i=_firstYoil-1; i>=0; i--) {
			var _holiday = false;
			if(yoilIdx == 0){
				_holiday = true;
			}	
			var strMonth = "";
			if(_prevMonth < 10){
				strMonth += "0" + gf_toString(_prevMonth);
			} else {
				strMonth = gf_toString(_prevMonth);				
			}
			var day = _prevLastDay-i;
			var strDay = "";
			if(day < 10){
				strDay += "0" + gf_toString(day);
			} else {
				strDay = gf_toString(day);				
			}
			var date = _prevYear + "-" + strMonth + "-" + strDay;
			var row = { year:_prevYear, month:_prevMonth, day : day, yoil : yoilIdx, holiday : _holiday, date:date };
			yoilIdx++;
			if(yoilIdx > 6){
				yoilIdx = 0;
			}
			_cal.push(row);
		}
	}
	
	for (var i=_firstDay; i<=_lastDay; i++){
		var _holiday = false;
		if(yoilIdx == 0){
			_holiday = true;
		}
		var strMonth = "";
		if(_month < 10){
			strMonth += "0" + gf_toString(_month);
		} else {
			strMonth = gf_toString(_month);				
		}
		var day = i;
		var strDay = "";
		if(day < 10){
			strDay += "0" + gf_toString(day);
		} else {
			strDay = gf_toString(day);				
		}
		var date = _year + "-" + strMonth + "-" + strDay;
		var row = { year:_year, month:_month, day : day, yoil : yoilIdx, holiday : _holiday, date:date };
		yoilIdx++;
		if(yoilIdx > 6){
			yoilIdx = 0;
		}
		_cal.push(row);
	}
	
	if (_lastYoil != 6){
		var tempIdx = yoilIdx;
		for (var i=1; i<=(6-tempIdx+1); i++) {
			var _holiday = false;
			if(yoilIdx == 0){
				_holiday = true;
			}
			var strMonth = "";
			if(_nextMonth < 10){
				strMonth += "0" + gf_toString(_nextMonth);
			} else {
				strMonth = gf_toString(_nextMonth);				
			}
			var day = i;
			var strDay = "";
			if(day < 10){
				strDay += "0" + gf_toString(day);
			} else {
				strDay = gf_toString(day);				
			}
			var date = _nextYear + "-" + strMonth + "-" + strDay;
			var row = { year:_nextYear, month:_nextMonth, day : day, yoil : yoilIdx, holiday : _holiday, date:date };
			yoilIdx++;
			if(yoilIdx > 6){
				yoilIdx = 0;
			}
			_cal.push(row);
		}
	}
	
	result.year = _year;
	result.month = _month;
	result.calendar = _cal;
	
	return result;
}

function gf_numberWithCommas(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}