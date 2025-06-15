/**
 * ========================
 * 공통 Form 스크립트
 * ========================
 */
//페이지 이동
function gf_movePage(cp) {
	$("#_search_cp_").val(cp);
	var frm = $("#_search_cp_").parents("form");
	frm.action = gf_getPathName();
	frm.submit();
}

//검색 초기화
function gf_reset() {
	document.getElementById("_search_so_").value = "";
	document.getElementById("_search_sv_").value = "";
	document.getElementById("_search_sd_").value = "";
	document.getElementById("_search_ed_").value = "";
	document.getElementById("_search_sv_").value = "";
	document.getElementById("_search_oc_").value = "";
	document.getElementById("_search_ob_").value = "";
	gf_movePage(1);
}

//순서변경
function gf_changeOrder(oc) {
	if(document.getElementById("_search_oc_").value == oc && document.getElementById("_search_ob_").value == 'desc'){
		document.getElementById("_search_ob_").value = 'asc';
	} else if(document.getElementById("_search_oc_").value == oc && document.getElementById("_search_ob_").value == 'asc'){
		document.getElementById("_search_oc_").value = '';
		document.getElementById("_search_ob_").value = '';
	} else {
		document.getElementById("_search_oc_").value = oc;
		document.getElementById("_search_ob_").value = 'desc';
	}		
	gf_movePage(1);
}

function gf_initCkEditor(editorArray){
	
	var initUpload = false;
	var ckConfig = {
		language: GV_LANGUAGE
	};

	var ckUploadConfig = {
		language: GV_LANGUAGE,
		filebrowserUploadUrl : '/file/ckImageUpload.do',
		toolbar : "ImageUpload"
	};
	
	//CKEDITOR SET
	$.each(editorArray, function (i, item) {
		if(item.useUpload == 'Y'){
			if(!initUpload) initUpload = true;
			CKEDITOR.replace(item.id, ckUploadConfig);
		} else {
			CKEDITOR.replace(item.id, ckConfig);
		}
	});
	
	CKEDITOR.on('dialogDefinition', function (ev) {
		var dialogName = ev.data.name;
		var dialog = ev.data.definition.dialog;
		var dialogDefinition = ev.data.definition;			
		if (dialogName == 'image') {
			dialog.on('show', function (obj) {
				this.selectPage('Upload'); //업로드텝으로 시작
			});
			dialogDefinition.removeContents('advanced'); // 자세히탭 제거
			dialogDefinition.removeContents('Link'); // 링크탭 제거
		}
	});
	
	//폼에 임시 div 생성
	if(initUpload){
		var frm  = $("#"+editorArray[0].id).closest('form');
		frm.append('<div id="editorImgList" style="display:none;width:0px;height:0px;"></div>')
	}
}

function gf_callbackCkUpload(no){
	if($("#editorImgList").length > 0) {
		$("#editorImgList").append('<input type="hidden" name="_EDITOR_FILE_NO_" value="'+no+'"/>')
	}
}

function gf_changeLocale(language){
	var frm = document.getElementById("cmmmnLocaleForm");
	frm.language.value = language;
	frm.submit();
}

function gf_download(oname, fpath, fname, fno){
	var frm = document.getElementById("cmmmnDownloadForm");
	frm.oname.value = oname;
	frm.fpath.value = fpath;
	frm.fname.value = fname;
	
	if(!gf_isNull(fno)){
		frm.fno.value = fno;
	}
	
	frm.submit();
	
	frm.oname.value = '';
	frm.fpath.value = '';
	frm.fname.value = '';
	frm.fno.value = '';
}

function gf_initMultiSelector(option){
	
	var listId = option.listId;
	var inputId = option.inputId;
	var inputName = option.inputName;
	var uploadCount = option.uploadCount;
	var uploadFileSize = option.uploadFileSize;
	if(gf_isNull(uploadFileSize)){
		uploadFileSize = 1000;
	}
	var uploadFileExt = option.uploadFileExt;
	if(gf_isNull(uploadFileExt)){
		uploadFileExt = "ALL";
	}

	var listTarget = document.getElementById(listId);
	var fileTarget = document.getElementById(inputId);
	
	var multi_selector = new MultiSelector(listTarget, inputName, uploadCount, uploadFileSize, uploadFileExt);
	multi_selector.addElement(fileTarget);
	return multi_selector;
}

function MultiSelector(list_target, input_name, max_count, max_size, allow_ext){

	this.deny_ext = ["jsp", "java", "js", "xml", "php", "sql", "htm", "asp", "txt", "php3", "cgi", "exe", "inc", "htaccess", "html"];
	this.allow_ext;
	if(allow_ext != "ALL"){
		this.allow_ext = allow_ext.split(",");
	} else {
		this.allow_ext = "ALL";
	}
	this.list_target = list_target;
	this.list_id = list_target.id;
	
	this.input_name = input_name;
	
	this.max_size = Number(max_size);
	this.allow_ext = allow_ext;
	
	this.file_target = "";
	this.file_id = "";
	this.file_name = "";
	
	this.count = 0;
	this.id = 0;
	
	this.update_count = 0;
	if( max_count ){
		this.max_count = max_count;
	} else {
		this.max_count = -1;
	};
	
	this.current_count = 0;
	
	//VIEWFILES 개수
	var view_files_count = $(".file_list .viewFiles").length;
	this.count += view_files_count;
	
	/**
	 * Add a new file input element
	 */
	_base = this;
	this.addElement = function( element ){
		
		if( element.tagName == 'INPUT' && element.type == 'file' ){
			
			if(this.file_target == null || this.file_target == ""){
				this.file_target = element;
				this.file_id = element.id;
				//this.file_name = element.name;
			}
			
			//element.name = this.file_name;
			element.id =  this.file_id+ '_' + this.id;
			element.class = "";
			this.id++;

			element.multi_selector = this;

			element.onchange = function(){
				
				var sErrMsg1 = "No more than ["+ _base.max_count +"] attachments can be attached!";
				var sErrMsg2 = "The attachment file size has been exceeded.";
				var sErrMsg3 = "This is not an uploadable file.";
				if(GV_LANGUAGE == 'ko') {
					sErrMsg1 = "첨부파일 갯수는 ["+_base.max_count+"]이상 첨부할 수 없습니다!";
					sErrMsg2 = "첨부가능한 파일용량을 넘었습니다.";
					sErrMsg3 = "업로드할 수 있는 파일이 아닙니다.";
				}
				
				if( _base.update_count > 0 ){
					if( _base.count > (_base.max_count-_base.update_count) ){
						element.value = "";
						alert(sErrMsg);	return;
					}
				}
				
				if( _base.max_count > 0  && _base.count > _base.max_count ){
					element.value = "";
					alert(sErrMsg); return;
				}
				
				//파일사이즈 체크
				var size = element.files[0].size;
				if(size > _base.max_size){
					element.value = "";
					alert(sErrMsg2);	
					return;
				}
				
				//확장자 체크
				var name = element.files[0].name;
				var ext = name.split('.').pop().toLowerCase();
				if(_base.deny_ext.indexOf(ext) != -1) {
					element.value = "";
					alert(sErrMsg3);
					return;
				}
				if(_base.allow_ext != "ALL"){
					if(_base.allow_ext.indexOf(ext) < 0) {
						element.value = "";
						alert(sErrMsg3);
						return;
					}
				}
				
				var new_element = document.createElement( 'input' );
				new_element.type = 'file';
				
				this.parentNode.insertBefore( new_element, this );
				
				this.multi_selector.addElement( new_element );
				
				this.multi_selector.addListRowNew( this );				
				
				this.name = _base.input_name;
				this.style.position = 'absolute';
				this.style.left = '-1000px';
				this.style.top = '-1000px';
				this.style.display = 'none';
				this.style.visibility = 'hidden';
				this.style.width = '0';
				this.style.height = '0';
				this.style.overflow = 'hidden';

				new_element.onkeypress = function(){
					return false;
				};

			};
			
			if( this.max_count != -1 && this.count >= this.max_count ){
				// element.disabled = true;
			};
			
			this.count++;
			
			this.current_element = element;
			
		} else {
			alert( 'Error: not a file input element' );
		};

	};

	/**
	 * Add a new row to the list of files
	 */
	this.addListRowNew = function( element ){

		// Row div
		var new_row = document.createElement( 'div' );
		new_row.className = "file_add";
		
		// Delete button
		var new_row_button = document.createElement( 'button' );
		new_row_button.type = 'button';
		new_row_button.innerHTML = 'Delete';

		// References
		new_row.element = element;
		
		// Delete function
		new_row_button.onclick= function(){

			// Remove element from form
			this.parentNode.element.parentNode.removeChild( this.parentNode.element );

			// Remove this row from the list
			this.parentNode.parentNode.removeChild( this.parentNode );

			// Decrement counter
			this.parentNode.element.multi_selector.count--;

			// Re-enable input element (if it's disabled)
			this.parentNode.element.multi_selector.current_element.disabled = false;

			// which nixes your already queued uploads
			return false;
		};
		// Set row value
		// new_row.innerHTML = element.value;
		new_row.innerHTML = "<span>"+element.value+"</span>&nbsp;&nbsp;";
		
		// Add button
		new_row.appendChild( new_row_button );

		// Add it to the list
		this.list_target.appendChild( new_row );
	};
	
	/**
	 * Add a new row to the list of files
	 */
	this.addListRow = function( element ){

		// Row div
		var new_row = document.createElement( 'div' );

		// Delete button
		var new_row_button = document.createElement( 'input' );
		new_row_button.type = 'button';
		new_row_button.value = 'Delete';

		// References
		new_row.element = element;

		// Delete function
		new_row_button.onclick= function(){

			// Remove element from form
			this.parentNode.element.parentNode.removeChild( this.parentNode.element );

			// Remove this row from the list
			this.parentNode.parentNode.removeChild( this.parentNode );

			// Decrement counter
			this.parentNode.element.multi_selector.count--;

			// Re-enable input element (if it's disabled)
			this.parentNode.element.multi_selector.current_element.disabled = false;

			// which nixes your already queued uploads
			return false;
		};

		// Set row value
		new_row.innerHTML = element.value;

		// Add button
		new_row.appendChild( new_row_button );

		// Add it to the list
		this.list_target.appendChild( new_row );
	};
};

function gf_removeUpload (obj){
	var id = obj.getAttribute('data-id');
	if(!gf_isNull(id) && id != '0'){
		var vObj = document.getElementById(id);
		var vVal = vObj.getAttribute('data-val');
		var btnVal = obj.value;
		if(btnVal == "0"){
			obj.innerHTML = "Cancel";
			obj.value = "1";
			vObj.value = vVal;			
			vObj.parentNode.getElementsByTagName("a")[0].setAttribute("class", "line_through");
		} else if(btnVal == "1"){
			obj.innerHTML = "Delete";
			obj.value = "0";
			vObj.value = 0;			
			vObj.setAttribute("class", "");
			vObj.parentNode.getElementsByTagName("a")[0].removeAttribute("class");
		}		
	}
}

//RSA
function gf_initRSA(rsaModulusVal, rsaExponentVal) {
	if(gf_isNull(rsaModulusVal) || gf_isNull(rsaExponentVal)){
		return null;
	}
	var _RSA = new RSAKey();
	_RSA.setPublic(rsaModulusVal, rsaExponentVal);
	return _RSA;
}

function gf_encryptRSA(_RSA, obj){
	if(_RSA != null){
		var orgVal = obj.val();
		var encryptVal = _RSA.encrypt(orgVal);
		obj.val(encryptVal);
		return true;
	} else {
		return false;
	}
}


$.fn.serializeObject = function() {
	var result = {};
	var extend = function(i, element) {
		var node = result[element.name];
		if ("undefined" !== typeof node && node !== null) {
			if ($.isArray(node)) {
				node.push(element.value);
			} else {
				result[element.name] = [node, element.value];
			}
		} else {
			result[element.name] = element.value;
		}
	}
	$.each(this.serializeArray(), extend);
	return result;
}