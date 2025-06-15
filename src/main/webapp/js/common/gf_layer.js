var LayerPopup = {
	
	target : {},
	layerId : "",
	layerInnerId : "",
	
	show : function (type, id, title, html){
		
		var layerId = "LAYER_" + id;
		var layerInnerId = layerId + "_INNER";
		LayerPopup.layerId = layerId;
		LayerPopup.layerInnerId = layerInnerId;
		
		var str  = "";
			str += "<a href='#"+layerInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
			str += "<div id='"+layerInnerId+"' class='layer_pop' tabindex='0'>";
			str += "	<div class='layer_title'>";
			str += 			title;
			str += "	</div>";
			str += "	<div class='layer_content'>";
			str += 			html;
			str += "	</div>";
			str += "	<div class='layer_bottom'>";
			if ( type == "ALERT") {
			str += "		<ul class='layer_button'>";
			str += "			<li><button type='button' class='pop_btn agree'>"+GV_MESSAGE.OK +"</button></li>";
			str += "		</ul>";
			} else if ( type == "CONFIRM") {
			str += "		<ul class='layer_button two'>";
			str += "			<li><button type='button' class='pop_btn agree'>"+GV_MESSAGE.YES +"</button></li>";
			str += "			<li><button type='button' class='pop_btn disagree'>"+GV_MESSAGE.NO +"</button></li>";
			str += "		</ul>";
			} else {
			str += "		<ul class='layer_button'>";
			str += "			<li><button type='button' class='pop_btn agree'>"+GV_MESSAGE.OK +"</button></li>";	
			str += "		</ul>";
			}
			str += "	</div>";
			str += "</div>";
			str += "<a href='#"+layerInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
			
		var pop 				= document.createElement("div");
			pop.id 		  		= layerId
			pop.style.zIndex	= "9992";
			pop.style.position	= "relative";
			pop.style.width		= "100%";
			pop.style.height	= "100%";
			pop.style.top		= "0";
			pop.style.left		= "0";
			pop.style.display	= "block";
			pop.innerHTML		= str;
			pop.onclick			= function(e){ gf_closeLayerPop(id);};
			
		gf_openlayerBack();
		document.getElementById("LAYER_BACK").appendChild(pop);
		document.getElementById(layerInnerId).addEventListener("click", function(e) { e.stopPropagation();});
		document.getElementById(layerInnerId).focus();
		document.getElementById("LAYER_BACK").getElementsByClassName("pop_focus")[0].addEventListener("focus", function(e) {document.getElementById(layerInnerId).focus();});
		document.getElementById("LAYER_BACK").getElementsByClassName("pop_focus")[1].addEventListener("focus", function(e) {document.getElementById(layerInnerId).focus();});
		
		if ( type == "ALERT") {
			LayerPopup.target.alert = $("#"+layerInnerId);
		} else if ( type == "CONFIRM") {
			LayerPopup.target.confirm = $("#"+layerInnerId);
		}
	},

	showPwChk : function (type, id, title, html){

		var layerId = "LAYER_" + id;
		var layerInnerId = layerId + "_INNER";
		LayerPopup.layerId = layerId;
		LayerPopup.layerInnerId = layerInnerId;

		var str  = "";
		str += "<a href='#"+layerInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
		str += "<div id='"+layerInnerId+"' class='layer_pop' tabindex='0'>";
		str += "	<div class='layer_title'>";
		str += 			"비밀번호 체크"
		str += "	</div>";
		str += "	<div class='layer_content'>";
		str += 			html;
		str += "	</div>";
		str += "	<div class='layer_bottom'>";
		str += "		<ul class='layer_button two'>";
		str += "			<li><button type='button' class='pop_btn agree'>확인</button></li>";
		str += "			<li><button type='button' class='pop_btn disagree'>닫기</button></li>";
		str += "		</ul>";
		str += "	</div>";
		str += "</div>";
		str += "<a href='#"+layerInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";

		var pop 				= document.createElement("div");
		pop.id 		  		= layerId
		pop.style.zIndex	= "9992";
		pop.style.position	= "relative";
		pop.style.width		= "100%";
		pop.style.height	= "100%";
		pop.style.top		= "0";
		pop.style.left		= "0";
		pop.style.display	= "block";
		pop.innerHTML		= str;
		pop.onclick			= function(e){ gf_closeLayerPop(id);};

		gf_openlayerBack();
		document.getElementById("LAYER_BACK").appendChild(pop);
		document.getElementById(layerInnerId).addEventListener("click", function(e) { e.stopPropagation();});
		document.getElementById(layerInnerId).focus();
		document.getElementById("LAYER_BACK").getElementsByClassName("pop_focus")[0].addEventListener("focus", function(e) {document.getElementById(layerInnerId).focus();});
		document.getElementById("LAYER_BACK").getElementsByClassName("pop_focus")[1].addEventListener("focus", function(e) {document.getElementById(layerInnerId).focus();});

		if ( type == "ALERT") {
			LayerPopup.target.alert = $("#"+layerInnerId);
		} else if ( type == "CONFIRM") {
			LayerPopup.target.confirm = $("#"+layerInnerId);
		}
	},

	// alert_popup
	alert : function(msg, callback) {
		LayerPopup.show("ALERT", "ALERT", GV_MESSAGE.ALERT, msg);
		
		if (typeof callback != 'undefined' && callback) {
			$(LayerPopup.target.alert).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				if (typeof callback == 'function') {
					callback();
				} else {
					if (callback) {
						if (callback.indexOf("(") == -1) eval(callback + "()");
						else eval(callback);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("ALERT");
			});
		} else {
			$(LayerPopup.target.alert).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("ALERT");
			});
		}
	},

	// confirm_popup
	confirm : function(msg, callback1, callback2) {
		
		LayerPopup.show("CONFIRM", "CONFIRM", GV_MESSAGE.ALERT, msg);
		if (typeof callback1 != 'undefined' && callback1) {
			$(LayerPopup.target.confirm).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				if (typeof callback1 == 'function') {
					callback1("Y");
				} else {
					if (callback1) {
						if (callback1.indexOf("(") == -1) eval(callback1 + "()");
						else eval(callback1);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("CONFIRM");
			});
		} else {
			$(LayerPopup.target.confirm).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("CONFIRM");
			});
		}
		
		if (typeof callback2 != 'undefined' && callback2) {
			$(LayerPopup.target.confirm).find(".pop_btn.disagree").click(function(e) {
				e.preventDefault();
				if (typeof callback2 == 'function') {
					callback2("N");
				} else {
					if (callback2) {
						if (callback2.indexOf("(") == -1) eval(callback2 + "()");
						else eval(callback2);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("CONFIRM");
			});
		} else {
			$(LayerPopup.target.confirm).find(".pop_btn.disagree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("CONFIRM");
			});
		}
	},

	pop : function(msg, callback) {
		LayerPopup.show("POP", "POP", "", msg);
		
		if (typeof callback != 'undefined' && callback) {
			$(LayerPopup.target.pop).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				if (typeof callback == 'function') {
					callback();
				} else {
					if (callback) {
						if (callback.indexOf("(") == -1) eval(callback + "()");
						else eval(callback);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("POP");
			});
		} else {
			$(LayerPopup.target.pop).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("POP");
			});
		}
	},

	// confirmPwChk
	confirmPwChk : function(msg, callback1, callback2) {
		LayerPopup.showPwChk("CONFIRM", "CONFIRM", GV_MESSAGE.ALERT, msg);

		if (typeof callback1 != 'undefined' && callback1) {
			$(LayerPopup.target.confirm).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				if (typeof callback1 == 'function') {
					callback1("Y");
				} else {
					if (callback1) {
						if (callback1.indexOf("(") == -1) eval(callback1 + "()");
						else eval(callback1);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("CONFIRM");
			});
		} else {
			$(LayerPopup.target.confirm).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("CONFIRM");
			});
		}

		if (typeof callback2 != 'undefined' && callback2) {
			$(LayerPopup.target.confirm).find(".pop_btn.disagree").click(function(e) {
				e.preventDefault();
				if (typeof callback2 == 'function') {
					callback2("N");
				} else {
					if (callback2) {
						if (callback2.indexOf("(") == -1) eval(callback2 + "()");
						else eval(callback2);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("CONFIRM");
			});
		} else {
			$(LayerPopup.target.confirm).find(".pop_btn.disagree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("CONFIRM");
			});
		}
	},

	pop : function(msg, callback) {
		LayerPopup.show("POP", "POP", "", msg);

		if (typeof callback != 'undefined' && callback) {
			$(LayerPopup.target.pop).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				if (typeof callback == 'function') {
					callback();
				} else {
					if (callback) {
						if (callback.indexOf("(") == -1) eval(callback + "()");
						else eval(callback);
					} else {
						if (typeof (confirmAfter) == "function") {
							confirmAfter();
						}
					}
				}
				$(this).unbind("click");
				gf_closeLayerPop("POP");
			});
		} else {
			$(LayerPopup.target.pop).find(".pop_btn.agree").click(function(e) {
				e.preventDefault();
				gf_closeLayerPop("POP");
			});
		}
	},

	
	init : function (type, id, title, html, callBackTrue, callBackFalse){
		
		var layerId = "LAYER_" + id;
		var layerInnerId = layerId + "_INNER";
		
		var str  = "";
			str += "<a href='#"+layerInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
			str += "<div id='"+layerInnerId+"' class='layer_pop' tabindex='0'>";
			str += "	<div class='layer_title'>";
			str += 			title;
			str += "	</div>";
			str += "	<div class='layer_content'>";
			str += 			html;
			str += "	</div>";
			str += "	<div class='layer_bottom'>";
			if ( type == "ALERT") {
			str += "		<ul class='layer_button'>";
			str += "			<li><button type='button' class='pop_btn agree'>"+GV_MESSAGE.OK +"</button></li>";
			str += "		</ul>";
			} else if ( type == "CONFIRM") {
			str += "		<ul class='layer_button two'>";
			str += "			<li><button type='button' class='pop_btn agree'>"+GV_MESSAGE.YES +"</button></li>";
			str += "			<li><button type='button' class='pop_btn'>"+GV_MESSAGE.NO +"</button></li>";
			str += "		</ul>";
			} else {
			str += "		<ul class='layer_button'>";
			str += "			<li><button type='button' class='pop_btn agree'>"+GV_MESSAGE.OK +"</button></li>";	
			str += "		</ul>";
			}
			str += "	</div>";
			str += "</div>";
			str += "<a href='#"+layerInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
			
		var pop 				= document.createElement("div");
			pop.id 		  		= layerId
			pop.style.zIndex	= "9992";
			pop.style.position	= "relative";
			pop.style.width		= "100%";
			pop.style.height	= "100%";
			pop.style.top		= "0";
			pop.style.left		= "0";
			pop.style.display	= "block";
			pop.innerHTML		= str;
			pop.onclick			= function(e){ gf_closeLayerPop(id);};
			
		gf_openlayerBack();
		document.getElementById("LAYER_BACK").appendChild(pop);
		document.getElementById(layerInnerId).addEventListener("click", function(e) { e.stopPropagation();});
		document.getElementById(layerInnerId).focus();
		document.getElementById("LAYER_BACK").getElementsByClassName("pop_focus")[0].addEventListener("focus", function(e) {document.getElementById(layerInnerId).focus();});
		document.getElementById("LAYER_BACK").getElementsByClassName("pop_focus")[1].addEventListener("focus", function(e) {document.getElementById(layerInnerId).focus();});
		
		document.getElementById(layerInnerId).getElementsByClassName("pop_btn")[0].addEventListener("click", function(e) { gf_closeLayerPop(id); });
		if ( type == "CONFIRM") {
			document.getElementById(layerInnerId).getElementsByClassName("pop_btn")[1].addEventListener("click", function(e) { gf_closeLayerPop(id); });
		}
	},

	// popup close
	close : function() {
		$('.modal_progress_close', LayerPopup.target.progress).click();
	}
}

function gf_openlayerBack() {
	if( document.getElementById("LAYER_BACK") ){
		return;
	} else {
		var back 					= document.createElement("div");
		    back.style.zIndex		= "9991";
		    back.style.position		= "fixed";
		    back.style.width		= "100%";
		    back.style.height		= "100%";
		    back.style.top			= "0";
		    back.style.left			= "0";
		    back.style.display		= "block";
		    back.id					= "LAYER_BACK";
		    back.style.background	= "rgba(0,0,0,0.5)";
		    back.onclick			= function(e){ gf_closeLayerBack() };
    	
	    document.getElementsByTagName("body")[0].appendChild(back);
	}
}

function gf_closeLayerBack() {
	if( !document.getElementById("LAYER_BACK") ){
		return;
	} else {
		var back = document.getElementById("LAYER_BACK");
		document.getElementsByTagName("body")[0].removeChild(back);
	}
}

function gf_closeLayerPop(id){
	var layerId = "LAYER_" + id;
	var pop = document.getElementById(layerId);
	if( !pop ){
		return;
	} else {
		document.getElementById("LAYER_BACK").removeChild(pop);
		if ( !document.getElementsByClassName("layer_pop").length) {
			gf_closeLayerBack();
		}
	}
}

function gf_alert(msg, callback){
	LayerPopup.alert(msg, callback);
}
function gf_confirm(msg, callback1, callback2){
	LayerPopup.confirm(msg, callback1, callback2);	
}
function gf_confirmPwChk(msg, callback1, callback2){
	LayerPopup.confirmPwChk(msg, callback1, callback2);
}
function gf_pop(msg, callback){
	LayerPopup.pop(msg, callback);	
}