var GV_AJAXING = false;
function gf_ajax(jsonArr) {
	
	if(!GV_AJAXING){
		
		if(gf_isNull(jsonArr)){
			return;
		}
		var _url = jsonArr.url;
		var _type = jsonArr.type;
		var _data = {};
		if(jsonArr.data != null){
			_data = jsonArr.data
		};
		var _succcess = jsonArr.success;
		var _error = jsonArr.error;
		
		if(gf_isNull(_url)){
			return;
		}
		
		if(gf_isNull(_type)){
			_type = "POST";
		}
		
		var _dataType = "json";
		if(!gf_isNull(jsonArr.dataType)){
			_dataType = jsonArr.dataType;
		}
		
		var _contentType = "application/x-www-form-urlencoded; charset=UTF-8";
		if(!gf_isNull(jsonArr.contentType)){
			_contentType = jsonArr.contentType;
			if(_type.toUpperCase() !=  "GET" &&_contentType.toLowerCase().indexOf("application/json") != -1) {
				_data = JSON.stringify(_data);
			}
		}
		var option = {
			  type : _type
			, url : _url
			, data : _data
			, dataType : _dataType
			, contentType : _contentType
			, success : function() {}
			, error : function(response) {
				var status = response.status;
				if(status == 404 ) {
					alert("서버 접속 경로가 잘못 되었습니다. 경로를 확인해 주십시오.");
				} else {
					alert("서버 접속 중 처리에 실패했습니다.");
				}
			}
		};
		
		return $.ajax(option);
	} else {
		alert(GV_MESSAGE.DATA_ING);
	}
}

function gf_isAjaxing(){
	return GV_AJAXING;
}

function gf_startAjaxing(){
	GV_AJAXING = true;
	gf_createLoadingPop();
}

function gf_endAjaxing(){
	GV_AJAXING = false;
	gf_removeLoadingPop();
}

function gf_createLoadingPop(){	
	var back 					= document.createElement("div");
	    back.style.zIndex		= "99999";
	    back.style.position		= "fixed";
	    back.style.width		= "100%";
	    back.style.height		= "100%";
	    back.style.top			= "0";
	    back.style.left			= "0";
	    back.style.display		= "block";
	    back.id					= "_LOADING_";
	    back.innerHTML			= "<img src='/images/common/loading.gif' alt='Loading...' style='position:absolute;top:50%;left:50%;'/>";
	    //back.style.background	= "rgba(0,0,0,0.5)";

    document.getElementsByTagName("body")[0].appendChild(back);
}

function gf_removeLoadingPop(){
	$("#_LOADING_").remove();
}