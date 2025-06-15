<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 게시글 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.26		신한나			관리자 게시판 구조 변경
 */
%>
<div class="board_wrap">
	<div class="board_cont">
		<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
			<div class="search_box">
				<div class="period">
					<label><spring:message code="search.period"/></label>
					<input type="text" name="sd" value="<c:out value='${search.sd }'/>" onclick="gf_datepicker(this);" title="<spring:message code="search.startdate"/>">
					<span>~</span>
					<input type="text" name="ed" value="<c:out value='${search.ed }'/>" onclick="gf_datepicker(this);" title="<spring:message code="search.enddate"/>">
				</div>
				<select name="so" title="<spring:message code="search.choose"/>">
					<option value="title" <c:if test="${search.so eq 'title'}">selected</c:if>><spring:message code="bbsVO.title"/></option>
					<c:if test='${bbsMgtVO.nonameYn ne "Y" }'>
						<option value="writer" <c:if test="${search.so eq 'writer'}">selected</c:if>><spring:message code="bbsVO.writer"/></option>
						<option value="inptId" <c:if test="${search.so eq 'inptId'}">selected</c:if>><spring:message code="bbsVO.inptId"/></option>
					</c:if>
				</select>
				<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
				<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
				<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
			</div>
		</form>
		<form:form id="listForm" name="listForm" method="post" autocomplete="off">
			<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/sys/bbs/${GV_BOARD_SKIN_CODE }/list.jsp"></c:import>
			</c:if>
			<% /** 페이징 */ %>
			<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
			<% /** //페이징 */ %>
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>		  
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
		   	<input type="hidden" name="checkedSId">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>
		</form:form>
	</div>
		
	<script type="text/javaScript" defer="defer">
	
		//검색
		function fn_search(){
			var searchFrm = document.boardSearchForm;
			document.getElementById("_search_so_").value = searchFrm.so.value;
			document.getElementById("_search_sv_").value = searchFrm.sv.value;
			document.getElementById("_search_sd_").value = searchFrm.sd.value;
			document.getElementById("_search_ed_").value = searchFrm.ed.value;
			gf_movePage(1);
		}
		
		//Row 선택
		function fn_checkRow() {
			var chkCnt = 0;
			var rowArr = document.getElementsByName("checkRow");
			var rowCnt = rowArr.length;
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox" && rowArr[i].checked) chkCnt++;
			}
			if (chkCnt == rowCnt) {
				document.getElementById("checkAllRow").checked = true;
			} else {
				document.getElementById("checkAllRow").checked = false;
			}
		}
		
		//Row 전체선택
		function fn_checkAllRow() {
			var rowArr = document.getElementsByName("checkRow");
			var checked = document.getElementById("checkAllRow").checked;
			if (checked) {
				for (var i = 0; i < rowArr.length; i++) {
					if (rowArr[i].type == "checkbox") rowArr[i].checked = true;
				}
			} else {
				for (var i = 0; i < rowArr.length; i++) {
					if (rowArr[i].type == "checkbox") rowArr[i].checked = false;
				}
			}
		}
		
		//등록
		function fn_registerView() {
			document.listForm.action = GV_PRESENT_PATH + "/register.do";
			document.listForm.submit();
		}
		
		//수정
		function fn_modifyView() {
			document.listForm.action = GV_PRESENT_PATH + "/modify.do";
			document.listForm.submit();
		}
		
		//선택삭제
		function fn_removeCheckedRow() {
			var checkedSId = "";
			var chkCnt = 0;
			var rowArr = document.getElementsByName("checkRow");
			var rowCnt = rowArr.length;
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox" && rowArr[i].checked){
					if(i != 0){
						checkedSId += "|";
					}
					checkedSId += rowArr[i].value;
					chkCnt++;
				}
			}
			if(chkCnt <= 0) {
				alert('<spring:message code="message.alert.nocheck" />');
			} else {
				var msg = confirm('<spring:message code="message.confirm.remove" />');
				if(msg == true){				
					document.listForm.checkedSId.value = checkedSId;
					document.listForm.action = GV_PRESENT_PATH + "/checkRemoveAction.do";
					document.listForm.submit();
				}
			}
		}
	
		//상세보기
		function fn_detailView(sId) {
			document.listForm.sId.value = sId;
			document.listForm.action = GV_PRESENT_PATH + "/view.do";
			document.listForm.submit();
		}
		
		function fn_changeCategory(sCate){
			document.listForm.sCate.value = sCate;
			document.listForm.action = gf_getPathName();
			document.listForm.submit();
		}
		
	</script>
</div>