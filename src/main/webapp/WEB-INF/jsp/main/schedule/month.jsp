<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: month.jsp
 * @Description : 일정 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="board_wrap">
	<c:if test='${schMgtVO.header ne null and schMgtVO.header ne ""}'>
		<div class="board_top">
			<c:out value="${schMgtVO.header }" escapeXml="false"/>
		</div>
	</c:if>
	
	<div class="board_cont">
		<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
			<div class="search_box">
				<select name="so" title="<spring:message code="search.choose"/>">
					<option value="title" <c:if test="${search.so eq 'title'}">selected</c:if>><spring:message code="schVO.title"/></option>
					<option value="writer" <c:if test="${search.so eq 'writer'}">selected</c:if>><spring:message code="schVO.writer"/></option>
					<option value="inptId" <c:if test="${search.so eq 'inptId'}">selected</c:if>><spring:message code="schVO.inptId"/></option>
				</select>
				<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
				<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
				<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
			</div>
		</form>
		<form:form id="listForm" name="listForm" method="post" autocomplete="off">			
			<c:if test='${!empty schMgtVO and GV_SCHEDULE_SKIN_CODE ne null and GV_SCHEDULE_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/main/schedule/${GV_SCHEDULE_SKIN_CODE }/month.jsp"></c:import>
			</c:if>			
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>		  
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
			<input type="hidden" name="sDate" value="<c:out value='${sDate }'/>">
			<input type="hidden" name="sOption" value="">
		   	<input type="hidden" name="checkedSId">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>	
		</form:form>
		
		<script type="text/javaScript" defer="defer">
			
			//검색
			function fn_search(){
				var searchFrm = document.boardSearchForm;
				document.getElementById("_search_so_").value = searchFrm.so.value;
				document.getElementById("_search_sv_").value = searchFrm.sv.value;
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
				document.listForm.action = "/schedule/<c:out value='${schedulePath}'/>/register.do";
				document.listForm.submit();
			}
			
			//수정
			function fn_modifyView() {
				document.listForm.action = "/schedule/<c:out value='${schedulePath}'/>/modify.do";
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
						document.listForm.action = "/schedule/<c:out value='${schedulePath}'/>/checkRemoveAction.do";
						document.listForm.submit();
					}
				}
			}
		
			//상세보기
			function fn_detailView(sId) {
				document.listForm.sId.value = sId;
				document.listForm.action = "/schedule/<c:out value='${schedulePath}'/>/view.do";
				document.listForm.submit();
			}
			
			//상세보기
			function fn_progDetailView(sId) {
				document.listForm.sId.value = sId;
				document.listForm.action = "/prog/view.do";
				document.listForm.submit();
			}
			
			function fn_changeCategory(sCate){
				document.listForm.sCate.value = sCate;
				document.listForm.action = gf_getPathName();
				document.listForm.submit();
			}
			
			function fn_changeCalendar(sOption){
				document.listForm.sOption.value = sOption;
				document.listForm.action = gf_getPathName();
				document.listForm.submit();
			}
			
		</script>
	</div>
	
	<c:if test='${schMgtVO.footer ne null and schMgtVO.footer ne ""}'>
		<div class="board_bottom">
			<c:out value="${schMgtVO.footer }" escapeXml="false"/>
		</div>
	</c:if>
</div>