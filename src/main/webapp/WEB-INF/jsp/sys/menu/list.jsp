<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 메뉴 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
	<div class="search_box">
		<select name="sMenuCd" title="<spring:message code="search.choose"/>">
			<c:if test="${!empty siteList }">
				<c:forEach var="site" items="${siteList }">
					<option value="<c:out value="${site.siteCd }"/>" <c:if test="${site.siteCd eq sMenuCd}">selected</c:if>><c:out value="${site.siteNm }"/></option>
				</c:forEach>
			</c:if>
		</select>	
		<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
	</div>
</form>
<div class="grid_wrap">
	<div class="col30">
		<div class="col_inner">
			<div id="btn_wrap1" class="btn_wrap">
				<ul>
					<li>
						<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
					</li>
				</ul>
			</div>
			<form:form id="listForm" name="listForm" method="post" autocomplete="off">
				<div class="tree_wrap">
					<% /** 트리 구조를 위한 LEVEL 변수 */ %>
					<c:set var="treeLevel" value="1"/>
					<ul class="tree_list">
						<li>
							<a href="#open_tree" class="tree_item folder open" data-id="0">메뉴목록</a>
							<ul>
								<c:forEach var="item" items="${list }" varStatus="i">
									<% /** 현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
									<c:choose>
										<c:when test="${i.index eq 0}"></c:when>
										<c:when test="${item.menuLvl > treeLevel}">
											<c:out value='<ul>' escapeXml='false'/>
										</c:when>
										<c:when test="${item.menuLvl < treeLevel}">
											<c:out value='</li>' escapeXml="false"/>
											<c:forEach begin="${item.menuLvl }" end="${treeLevel - 1}" step="1">
												<c:out value='</ul></li>' escapeXml="false"/>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:out value='</li>' escapeXml="false"/>
										</c:otherwise>
									</c:choose>
									<c:out value='<li>' escapeXml="false"/>
									<% /** //현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
									
									<a href="#open_tree" class="tree_item<c:if test='${item.childCnt > 0 }'> folder open</c:if>" data-id="<c:out value='${item.menuId }' />">
										<c:out value='${item.menuNm }' />(<c:out value='${item.menuId }' />)
									</a>
									
									<% /** 트리 구조를 위한 LEVEL 변수 */ %>
									<c:set var="treeLevel" value="${item.menuLvl}"/>
									
									<% /** 마지막인경우 모두 닫는다 */ %>
									<c:if test="${i.last}">
										<c:out value='</li>' escapeXml="false"/>
										<c:if test="${item.menuLvl > 1 }">
											<c:forEach begin="1" end="${item.menuLvl - 1}" step="1">
												<c:out value="</ul></li>" escapeXml="false"/>
											</c:forEach>
										</c:if>
									</c:if>
									<% /** //마지막인경우 모두 닫는다 */ %>
								</c:forEach>
							</ul>
						</li>
					</ul>
			  	</div>
			</form:form>
		</div>
	</div>
	<div class="col70">
		<div class="col_inner">
			<div id="btn_wrap2" class="btn_wrap">
				<ul>
					<li>
						<a class="button save_btn" href="javascript:fn_registerAction();"><spring:message code="button.save" /></a>
					</li>
					<li>
						<a class="button remove_btn" href="javascript:fn_removeAction();"><spring:message code="button.remove" /></a>
					</li>
				</ul>
			</div>
			<form:form modelAttribute="menuVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			
				<input type="hidden" name="registerFlag" value="C"/>
			
				<table class="detail_table">
					<caption>메뉴 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><label for="menuCd"><spring:message code="menuVO.menuCd" /></label></th>
						<td>
							<c:choose>
								<c:when test="${sMenuCd ne null and sMenuCd ne '' }">
									<form:hidden path="menuCd" value="${sMenuCd }"/>
									<c:if test="${!empty siteList }">								
										<c:forEach var="site" items="${siteList }">
											<c:if test="${site.siteCd eq sMenuCd}"><c:out value="${site.siteNm }"/></c:if>
										</c:forEach>
									</c:if>
								</c:when>
								<c:otherwise>
									<form:select path="menuCd">
										<form:option value="" label="--선택--" />
										<c:if test="${!empty siteList }">							
											<c:forEach var="site" items="${siteList }">
												<form:option value="${site.siteCd }" label="${site.siteNm }" />
											</c:forEach>				
										</c:if>
									</form:select>
								</c:otherwise>
							</c:choose>
			       			<span class="form_error" data-path="menuCd"><form:errors path="menuCd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuId"><spring:message code="menuVO.menuId" /></label></th>
						<td>
							<form:input path="menuId" readonly="true"/>
							<span class="form_error" data-path="menuId"><form:errors path="menuId" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="hiMenuId"><spring:message code="menuVO.hiMenuId" /></label></th>
						<td>
							<form:input path="hiMenuId" readonly="true"/>
							<span class="form_error" data-path="hiMenuId"><form:errors path="hiMenuId" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuNm"><spring:message code="menuVO.menuNm" /></label></th>
						<td>
							<form:input path="menuNm" cssClass="w_full"/>
							<span class="form_error" data-path="menuNm"><form:errors path="menuNm" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuAbbrNm"><spring:message code="menuVO.menuAbbrNm" /></label></th>
						<td>
							<form:input path="menuAbbrNm" />
							<span class="form_error" data-path="menuAbbrNm"><form:errors path="menuAbbrNm" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuEngNm"><spring:message code="menuVO.menuEngNm" /></label></th>
						<td>
							<form:input path="menuEngNm" cssClass="w_full"/>
							<span class="form_error" data-path="menuEngNm"><form:errors path="menuEngNm" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuEngAbbrNm"><spring:message code="menuVO.menuEngAbbrNm" /></label></th>
						<td>
							<form:input path="menuEngAbbrNm" />
							<span class="form_error" data-path="menuEngAbbrNm"><form:errors path="menuEngAbbrNm" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuDesc"><spring:message code="menuVO.menuDesc" /></label></th>
						<td>
							<form:input path="menuDesc" cssClass="w_full"/>
							<span class="form_error" data-path="menuDesc"><form:errors path="menuDesc" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuLvl"><spring:message code="menuVO.menuLvl" /></label></th>
						<td>
							<form:input path="menuLvl" readonly="true"/>
							<span class="form_error" data-path="menuLvl"><form:errors path="menuLvl" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="menuLink"><spring:message code="menuVO.menuLink" /></label></th>
						<td>
							<form:input path="menuLink" cssClass="w_full"/>
							<span class="form_error" data-path="menuLink"><form:errors path="menuLink" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="progYn"><spring:message code="menuVO.progYn" /></label></th>
						<td>
							<form:radiobutton path="progYn" value="Y" label="Y"/>
							<form:radiobutton path="progYn" value="N" label="N"/>
							<span class="form_error" data-path="progYn"><form:errors path="progYn" /></span>
						</td>
					</tr>
					<tr data-menu-type="PROGRAM">
						<th><label for="progPath"><spring:message code="menuVO.progPath" /></label></th>
						<td>
							<form:hidden path="progCd"/>
							<form:input path="progPath" readonly="true"/>
							<button type="button" class="search_btn" onclick="fn_getProgramList();"><spring:message code="button.search" /></button>
							<span class="form_error" data-path="progPath"><form:errors path="progPath" /></span>
						</td>
					</tr>
					<tr data-menu-type="PROGRAM" data-program-type="PARAM">
						<th><label for="progParam"><spring:message code="menuVO.progParam" /></label></th>
						<td>
							<form:hidden path="progParam"/>
							<select id="selectProgParam"></select>
							<span class="form_error" data-path="progParam"><form:errors path="progParam" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="sortOrd"><spring:message code="menuVO.sortOrd" /></label></th>
						<td>
							<form:input path="sortOrd" />
							<span class="form_error" data-path="sortOrd"><form:errors path="sortOrd" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="useYn"><spring:message code="menuVO.useYn" /></label></th>
						<td>
							<form:radiobutton path="useYn" value="Y" label="Y"/>
							<form:radiobutton path="useYn" value="N" label="N"/>
							<span class="form_error" data-path="useYn"><form:errors path="useYn"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="viewYn"><spring:message code="menuVO.viewYn" /></label></th>
						<td>
							<form:radiobutton path="viewYn" value="Y" label="Y"/>
							<form:radiobutton path="viewYn" value="N" label="N"/>
							<span class="form_error" data-path="viewYn"><form:errors path="viewYn"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="uploadFile"><spring:message code="text.image" /></label></th>
						<td>
							<img id="menuFileImg" src="/images/common/no_img.png" alt="NO IMG" style="width:100px; height:auto;">
			   				<div id="menuFileInput">
			    				<label class="file">
			    					<span class="name"><spring:message code="text.file.placeholder" /></span>
			    					<form:input type="file" path="uploadFile" data-img="menuFileImg"/>
			    					<span class="btn"><spring:message code="button.upload" /></span>
			    				</label>
			   				</div>
							<span class="form_error" data-path="uploadFile"><form:errors path="uploadFile" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="useSso"><spring:message code="menuVO.useSso" /></label></th>
						<td>
							<form:radiobutton path="useSso" value="Y" label="Y"/>
							<form:radiobutton path="useSso" value="N" label="N"/>
							<span class="form_error" data-path="useSso"><form:errors path="useSso"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="note"><spring:message code="menuVO.note" /></label></th>
						<td>
							<form:textarea path="note" />
							<span class="form_error" data-path="note"><form:errors path="note" /></span>
						</td>
					</tr>
					<c:if test='${!empty authList}'>
						<tr>
							<th><spring:message code="menuVO.auth" /></th>
							<td>
								<ul class="check_list">
									<c:forEach var="auth" items="${authList }" varStatus="i">
										<li>
											<label for="auth_<c:out value='${i.count }'/>" class="check">
												<input type="checkbox" id="auth_<c:out value='${i.count }'/>" name="menuAuth" value="<c:out value="${auth.authCd }"/>"><i></i>
												<c:out value="${auth.authNm }"/>
											</label>
										</li>
									</c:forEach>
								</ul>				
							</td>
						</tr>		
					</c:if>
				</table>
				
			   	<% /** 이중방지 토큰 */ %>
				<double-submit:preventer/>
				
				<% /** 검색조건 유지 */ %>
			   	<input type="hidden" name="sMenuCd" value="<c:out value='${sMenuCd }'/>">
			  	<% /** //검색조건 유지 */ %>
			
			</form:form>
			<div id="copyFileInput" style="display:none;">
				<label class="file">
					<span class="name"><spring:message code="text.file.placeholder" /></span>
					<input name="uploadFile" data-img="menuFileImg" type="file">
					<span class="btn"><spring:message code="button.upload" /></span>
				</label>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="menuVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" defer="defer">

	//데이터 변경여부 확인 변수
	var is_change = false;
	var PROG_PARAM = '';
	
	//현재 메뉴 저장 후 같은 메뉴인 경우 조회 생략
	var present_menu_id = '<c:out value="${menuVO.menuId}"/>';;
	var present_menu_info = null;
	
	$(document).ready(function(){
		
		//프로그램인지 체크 후 display
		var progYn = '<c:out value="${menuVO.progYn}"/>';
		if(progYn == "Y"){
			$("tr[data-menu-type='PROGRAM']").css("display", "table-row");
			fn_getProgParamList('<c:out value="${menuVO.progCd}"/>');
		} else {
			$("tr[data-menu-type='PROGRAM']").css("display", "none");
			$("tr[data-program-type='PARAM']").css("display", "none");
		}
		
		$("input[name='progYn']").click(function(){
			if($(this).val() == "Y"){
				$("tr[data-menu-type='PROGRAM']").css("display", "table-row");
				fn_getProgParamList('<c:out value="${menuVO.progCd}"/>');
			} else {
				$("tr[data-menu-type='PROGRAM']").css("display", "none");
				$("tr[data-program-type='PARAM']").css("display", "none");
			}
		})
		
		$("input,select,textarea,checkbox,radio").change(function(){
			is_change = true;
		});
		
		if(gf_isNull(present_menu_id)){
			present_menu_info = null;
			present_menu_id = "0";
			$(".tree_item[data-id='0']").addClass("present");
			fn_resetInput();
		} else {
			$(".tree_item[data-id='"+present_menu_id+"']").addClass("present");	
			fn_resetInput();		
			document.detailForm.menuCd.value = '<c:out value="${menuVO.menuCd}"/>';
			document.detailForm.menuId.value = '<c:out value="${menuVO.menuId}"/>';
			document.detailForm.hiMenuId.value = '<c:out value="${menuVO.hiMenuId}"/>';
			document.detailForm.menuNm.value = '<c:out value="${menuVO.menuNm}"/>';
			document.detailForm.menuAbbrNm.value = '<c:out value="${menuVO.menuAbbrNm}"/>';
			document.detailForm.menuEngNm.value = '<c:out value="${menuVO.menuEngNm}"/>';
			document.detailForm.menuEngAbbrNm.value = '<c:out value="${menuVO.menuEngAbbrNm}"/>';
			document.detailForm.menuDesc.value = '<c:out value="${menuVO.menuDesc}"/>';
			document.detailForm.menuLvl.value = '<c:out value="${menuVO.menuLvl}"/>';
			document.detailForm.menuLink.value = '<c:out value="${menuVO.menuLink}"/>';
			document.detailForm.progPath.value = '<c:out value="${menuVO.progPath}"/>';
			document.detailForm.progParam.value = '<c:out value="${menuVO.progParam}"/>';
			document.detailForm.progCd.value = '<c:out value="${menuVO.progCd}"/>';
			document.detailForm.sortOrd.value = '<c:out value="${menuVO.sortOrd}"/>';
			document.detailForm.note.value = '<c:out value="${menuVO.note}"/>';	
			
			$('#detailForm input[name="progYn"]').filter('[value="<c:out value="${menuVO.progYn}"/>"]').prop("checked", true);
			$('#detailForm input[name="useYn"]').filter('[value="<c:out value="${menuVO.useYn}"/>"]').prop("checked", true);
			$('#detailForm input[name="viewYn"]').filter('[value="<c:out value="${menuVO.viewYn}"/>"]').prop("checked", true);
			$('#detailForm input[name="useSso"]').filter('[value="<c:out value="${menuVO.useSso}"/>"]').prop("checked", true);
		}
	});

	//트리 구조 처리
	$(".tree_item").click(function(){
		
		//값이 변경된경우 confirm
		if (is_change || $(".tree_item[data-id='tempCreateCode']").length > 0){
			var msg = confirm('<spring:message code="message.confirm.nosave" />');
			if(msg == false){
				return false;
			}
		}
		$(".tree_item[data-id='tempCreateCode']").remove();
		
		var menuId = $(this).attr("data-id");
		$(".tree_item").removeClass("present");
		$(this).addClass("present");
		
		//ROOT인 경우 초기화
		if(menuId == "0"){
			present_menu_info = null;
			present_menu_id = "0";
			fn_resetInput();
		} else {
			fn_getComMenuInfo(menuId);
		}
	});
	
	//메뉴 정보를 조회한다.
	function fn_getComMenuInfo(menuId){		
		if(present_menu_id != menuId) {			
			present_menu_id = menuId;
			gf_ajax({
				url : "/menu/view.json",
				type : "POST",
				data : { id : menuId },
			}).then(function(response){
				present_menu_info = response;
				
				//FLAG
				document.detailForm.registerFlag.value = "U";
				
				//초기화
				fn_resetInput();
				
				//조회 결과 SET
				document.detailForm.menuCd.value = response.menuCd;
				document.detailForm.menuId.value = response.menuId;
				document.detailForm.hiMenuId.value = response.hiMenuId;
				document.detailForm.menuNm.value = response.menuNm;
				document.detailForm.menuAbbrNm.value = response.menuAbbrNm;
				document.detailForm.menuEngNm.value = response.menuEngNm;
				document.detailForm.menuEngAbbrNm.value = response.menuEngAbbrNm;
				document.detailForm.menuDesc.value = response.menuDesc;
				document.detailForm.menuLvl.value = response.menuLvl;
				document.detailForm.menuLink.value = response.menuLink;
				document.detailForm.progPath.value = response.progPath;
				document.detailForm.progParam.value = response.progParam;
				document.detailForm.progCd.value = response.progCd;
				document.detailForm.sortOrd.value = response.sortOrd;
				document.detailForm.note.value = response.note;
				
				$('#detailForm input[name="progYn"]').filter("[value="+response.progYn+"]").prop("checked", true);
				$('#detailForm input[name="useYn"]').filter("[value="+response.useYn+"]").prop("checked", true);
				$('#detailForm input[name="viewYn"]').filter("[value="+response.viewYn+"]").prop("checked", true);
				$('#detailForm input[name="useSso"]').filter("[value="+response.useSso+"]").prop("checked", true);
									
				if(response.progYn == "Y"){
					$("tr[data-menu-type='PROGRAM']").css("display", "table-row");
					fn_getProgParamList(response.progCd);
				} else {
					$("tr[data-menu-type='PROGRAM']").css("display", "none");
					$("tr[data-program-type='PARAM']").css("display", "none");
				}
				
				//권한체크
				if(response.authList != null){
					$.each(response.authList, function (idx, auth) {
						$('#detailForm input[name="menuAuth"]').filter("[value="+auth.authCd+"]").prop("checked", true);
					});
				}
				
				PROG_PARAM = response.progParam;
				
				//메뉴아이콘 체크
				//초기화
				$("#menuFileImg").attr("src", "/images/common/no_img.png");
				$("#menuFileInput").html($("#copyFileInput").html());
				$("#menuFileInput").find("input[type='file']").attr("id", "uploadFile");
				if(response.fileNo > 0 && response.viewFile != null){
					$("#menuFileImg").attr("src", response.viewFile.viewUrl);
					$("#menuFileInput").find(".name").html(response.viewFile.oname);
				}
				
				is_change = false;
			});		
		}
	};

	//프로그램 정보를 조회한다.
	function fn_getProgramList() {
		var id = "program";
		var url = "/menu/programList.json";
		var param = {menuCd : '<c:out value="${sMenuCd}"/>'};
		var columns = [
			{key:"code", name:"코드"},
			{key:"name", name:"프로그램명"},
			{key:"uri", name:"URI"}
		];
		var callback = function(data){
			var code = data.code;
			var uri = data.uri;
			document.detailForm.progCd.value = code;
			document.detailForm.progPath.value = uri;
			
			fn_getProgParamList(code);
		};
		gf_openSearchPop(id, url, param, columns, callback);
	};
	
	//검색
	function fn_search(){
		var searchFrm = document.boardSearchForm;
		searchFrm.action = GV_PRESENT_PATH + "/list.do";
		searchFrm.submit();
	}
	
	//등록
	function fn_registerView() {
		
		//값이 변경된경우 confirm
		if (is_change || $(".tree_item[data-id='tempCreateCode']").length > 0){
			var msg = confirm('<spring:message code="message.confirm.nosave" />');
			if(msg == false){
				return false;
			}
		}
		$(".tree_item[data-id='tempCreateCode']").remove();
		
		//FLAG
		document.detailForm.registerFlag.value = "C";
		
		//초기화
		fn_resetInput();
		
		//현재 선택된 트리의 정보를 가져와 하위메뉴로 등록된다.
		var str = "";
		var parentObj = null;
		if(present_menu_info != null && present_menu_id != "0"){			
			document.detailForm.menuLvl.value = present_menu_info.menuLvl + 1;
			document.detailForm.hiMenuId.value = present_menu_info.menuId;
			document.detailForm.menuCd.value = present_menu_info.menuCd;
			parentObj = $(".tree_item[data-id='"+present_menu_info.menuId+"']");
		} else {			
			document.detailForm.menuLvl.value = 1;
			document.detailForm.hiMenuId.value = null;
			document.detailForm.menuCd.value = document.detailForm.sMenuCd.value;
			parentObj = $(".tree_item[data-id='0']");
		}
		
		if(parentObj.next("ul").length > 0){
			str += '<li>';
			str += '<a href="#open_tree" class="tree_item" data-id="tempCreateCode">';
			str += 'NEW';
			str += '</a>';
			str += '</li>';
			parentObj.next("ul").append(str);
		} else {
			str += '<ul><li>';
			str += '<a href="#open_tree" class="tree_item" data-id="tempCreateCode">';
			str += 'NEW';
			str += '</a>';
			str += '</li></ul>';
			parentObj.parent("li").append(str);
		}
		$(".tree_item").removeClass('present');
		$(".tree_item[data-id='tempCreateCode']").addClass('present');
		
		//코드 입력
		$('#detailForm input[name="menuId"]').prop("readonly", false);
		
		$('#detailForm input[name="progYn"]').filter("[value='N']").prop("checked", true);
		$('#detailForm input[name="useYn"]').filter("[value='Y']").prop("checked", true);
		$('#detailForm input[name="viewYn"]').filter("[value='Y']").prop("checked", true);
		$('#detailForm input[name="useSso"]').filter("[value='N']").prop("checked", true);
		
		$("tr[data-menu-type='PROGRAM']").css("display", "none");
		$("tr[data-program-type='PARAM']").css("display", "none");
				
		present_menu_id = "0";
		present_menu_info = null;
		
		PROG_PARAM = "";
		
		//메뉴아이콘 체크
		$("#menuFileImg").attr("src", "/images/common/no_img.png");
		$("#menuFileInput").html($("#copyFileInput").html());
		$("#menuFileInput").find("input[type='file']").attr("id", "uploadFile");
		
		is_change = false;
		
		document.detailForm.menuId.focus();
	}
	
	function fn_getProgParamList(progCd){
		$("#selectProgParam").html("");
		if(progCd == "CONTENT"){
			gf_ajax({
				url : "/menu/contentList.json",
				type : "POST",
			}).then(function(response){
				if(response != null){
					var opt = "<option value=''>--선택--</option>";
					$.each(response, function(i, item){
						if(item.ID == PROG_PARAM) {
							opt += "<option value='"+item.ID+"' selected>"+item.NAME+"</option>"
						} else {
							opt += "<option value='"+item.ID+"'>"+item.NAME+"</option>"
						}
					});						
					$("#selectProgParam").html(opt);
					$("tr[data-program-type='PARAM']").css("display", "table-row");
				}
			});
		} else if(progCd == "BOARD"){
			gf_ajax({
				url : "/menu/bbsMgtList.json",
				type : "POST",
			}).then(function(response){
				if(response != null){
					var opt = "<option value=''>--선택--</option>";
					$.each(response, function(i, item){
						if(item.ID == PROG_PARAM) {
							opt += "<option value='"+item.ID+"' selected>"+item.NAME+"</option>"
						} else {
							opt += "<option value='"+item.ID+"'>"+item.NAME+"</option>"
						}
					});						
					$("#selectProgParam").html(opt);
					$("tr[data-program-type='PARAM']").css("display", "table-row");
				}
			});
		} else if(progCd == "SCHEDULE"){
			gf_ajax({
				url : "/menu/schMgtList.json",
				type : "POST",
			}).then(function(response){
				if(response != null){
					var opt = "<option value=''>--선택--</option>";
					$.each(response, function(i, item){
						if(item.ID == PROG_PARAM) {
							opt += "<option value='"+item.ID+"' selected>"+item.NAME+"</option>"
						} else {
							opt += "<option value='"+item.ID+"'>"+item.NAME+"</option>"
						}
					});						
					$("#selectProgParam").html(opt);
					$("tr[data-program-type='PARAM']").css("display", "table-row");
				}
			});
		} else {
			$("tr[data-program-type='PARAM']").css("display", "none");
		}
	}
	
	$(document).on("change", "#selectProgParam", function(){
		$("#progParam").val($(this).val());
	});

	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (frm.registerFlag.value != "C" && frm.registerFlag.value != "U"){
			return;
		} else {
			if (!validateMenuVO(frm)) {
				return;
			} else {	
				var msg = confirm('<spring:message code="message.confirm.save" />');
				if(msg == true){					
					if (frm.registerFlag.value == "C"){	
						gf_ajax({
							url : "/menu/isIdExist.json",
							type : "POST",
							data : { id : frm.menuId.value },
						}).then(function(response){
							var isExist = response;
							if(!isExist){
								frm.action = GV_PRESENT_PATH + "/registerAction.do";
								frm.submit();
							} else {
								alert('<spring:message code="message.alert.existdata" />');
								frm.menuId.focus()
							}
						});						
					} else if (frm.registerFlag.value == "U"){						
						frm.action = GV_PRESENT_PATH + "/modifyAction.do";
						frm.submit();						
					} else {						
						frm.action = "";
						return;						
					}
				}			
			}
		}
	}
	
	//삭제
	function fn_removeAction() {		
		if(present_menu_info != null && present_menu_id != "0"){			
			if(present_menu_info.list.length > 0){
				alert('<spring:message code="message.alert.remove.parent" />');
				return
			}
		}
		
		if (document.detailForm.registerFlag.value != "U"){
			alert('<spring:message code="message.alert.nosave" />');
			return;
		}
		
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}
	
	//값 초기화
	function fn_resetInput(){
		document.detailForm.menuId.value = "";
		document.detailForm.hiMenuId.value = "";
		document.detailForm.menuNm.value = "";
		document.detailForm.menuAbbrNm.value = "";
		document.detailForm.menuEngNm.value = "";
		document.detailForm.menuEngAbbrNm.value = "";
		document.detailForm.menuDesc.value = "";
		document.detailForm.menuLvl.value = "0";
		document.detailForm.menuLink.value = "";
		document.detailForm.progPath.value = "";
		document.detailForm.progParam.value = "";
		document.detailForm.progCd.value = "";
		document.detailForm.sortOrd.value = 0;
		document.detailForm.note.value = "";
		
		$('#detailForm input[name="progYn"]').removeAttr('checked');
		$('#detailForm input[name="useYn"]').removeAttr('checked');
		$('#detailForm input[name="viewYn"]').removeAttr('checked');
		$('#detailForm input[name="useSso"]').removeAttr('checked');
		$('#detailForm input[name="menuAuth"]').removeAttr('checked');
		
		$('#detailForm input[name="menuCd"]').prop("readonly", true);
		$('#detailForm input[name="menuId"]').prop("readonly", true);
		
		$("tr[data-menu-type='PROGRAM']").css("display", "none");
		$("tr[data-program-type='PARAM']").css("display", "none");
		
		$(".form_error").html("");
	}
	
</script>