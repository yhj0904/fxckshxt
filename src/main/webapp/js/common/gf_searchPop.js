function gf_openSearchPop(id, url, param, columns, callback) {
	gf_ajax({
		url : url,
		type : "POST",
		dataType : "json",
		data : {},
	}).then(function(result){
		gf_getSearchPop(id, result, columns, callback);
	});
}

function gf_getSearchPop(id, data, columns, callback){
	
	var searchPopId = "SEARCH_POP_" + id;
	var searchPopInnerId = searchPopId + "_INNER";
	
	var str  = "";
		str += "<a href='#"+searchPopInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
		str += "<div id='"+searchPopInnerId+"' class='layer_pop search_pop' tabindex='0'>";
		str += "	<div class='layer_title'>";
		str += "		SEARCH POP";
		str += "	</div>";
		str += "	<div class='layer_content'>";
		str += "	<div class='scroll_box'>";
		str += "		<table class='list_table'>";
		if(!gf_isNull(data)){
			for(var i=0; i < data.length; i++){
				str += "	<tr>";
				str += "		<td>";
				str += 			i+1;
				str += "		</td>";
				for(var j=0; j < columns.length; j++){
					str += "	<td>";
					str += 		data[i][columns[j].key];
					str += "	</td>";
				}
				str += "		<td>";
				str += "			<a href='#none' class='button search_pop_select' data-idx='"+i+"'>선택</a>";
				str += "		</td>";
				str += "	</tr>";
			}
		}
		str += "		</table>";
		str += "	</div>";
		str += "	</div>";
		str += "	<div class='layer_bottom'>";
		str += "		<ul class='layer_button'>";
		str += "			<li><button type='button' id='"+id+"_pop_close' class='pop_btn'>"+GV_MESSAGE.CLOSE +"</button></li>";	
		str += "		</ul>";
		str += "	</div>";
		str += "</div>";
		str += "<a href='#"+searchPopInnerId+"' class='pop_focus none' tabindex='0'>FOCUS RETURN</a>";
		
	var pop 				= document.createElement("div");
		pop.id 		  		= searchPopId
		pop.style.zIndex	= "9992";
		pop.style.position	= "relative";
		pop.style.width		= "100%";
		pop.style.height	= "100%";
		pop.style.top		= "0";
		pop.style.left		= "0";
		pop.style.display	= "block";
		pop.innerHTML		= str;
		pop.onclick			= function(e){ gf_closeLayerPop(id);};
	
	
	if( document.getElementById("SEARCH_POP_BACK") ){
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
		    back.id					= "SEARCH_POP_BACK";
		    back.style.background	= "rgba(0,0,0,0.5)";
		    back.onclick			= function(e){
									    	if( !document.getElementById("SEARCH_POP_BACK") ){
									    		return;
									    	} else {
									    		var back = document.getElementById("SEARCH_POP_BACK");
									    		document.getElementsByTagName("body")[0].removeChild(back);
									    	}
		    							};
    	
	    document.getElementsByTagName("body")[0].appendChild(back);
	}
	document.getElementById("SEARCH_POP_BACK").appendChild(pop);
	document.getElementById(searchPopInnerId).addEventListener("click", function(e) { e.stopPropagation();});
	document.getElementById(searchPopInnerId).focus();
	document.getElementById(id+"_pop_close").addEventListener("click", function(e) {document.getElementsByTagName("body")[0].removeChild(document.getElementById("SEARCH_POP_BACK"));});
	document.getElementById("SEARCH_POP_BACK").getElementsByClassName("pop_focus")[0].addEventListener("focus", function(e) {document.getElementById(searchPopInnerId).focus();});
	document.getElementById("SEARCH_POP_BACK").getElementsByClassName("pop_focus")[1].addEventListener("focus", function(e) {document.getElementById(searchPopInnerId).focus();});
	
	$("#"+searchPopInnerId).find(".search_pop_select").click(function(){
		var idx = $(this).attr("data-idx");
		var returnData = data[idx];
		if (typeof callback != 'undefined' && callback) {
			if (typeof callback == 'function') {
				callback(returnData);
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
		}
		var back = document.getElementById("SEARCH_POP_BACK");
		document.getElementsByTagName("body")[0].removeChild(back);
	});
}