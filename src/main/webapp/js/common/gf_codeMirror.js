var ComCodeMirror = {
	
	option : {
		mode : 'text/html', // 문서타입
		lineNumbers : true, // 라인넘버 표시
		scrollbarStyle : "simple", // 스크롤바 스타일
		keyMap : "sublime", // 괄호강조
		matchBrackets : true, // 괄호강조
		tabSize : 2, // 탭키 간격
		highlightSelectionMatches : {
			showToken : /\w/,
			annotateScrollbar : true
		},
		readonly:true
	},
	
	edit : function(obj){		
		if(gf_isNull(obj)){
			return null;
		}
		
		var _selector = obj[0];
		
		return CodeMirror.fromTextArea(_selector, this.option);
	}	
}