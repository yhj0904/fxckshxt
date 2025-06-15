<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 템플릿관리 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
	<div class="search_box">
		<select name="so" title="<spring:message code="search.choose"/>">
			<option value="templateCd" <c:if test="${search.so eq 'templateCd'}">selected</c:if>><spring:message code="templateMgtVO.templateCd"/></option>
			<option value="templateNm" <c:if test="${search.so eq 'templateNm'}">selected</c:if>><spring:message code="templateMgtVO.templateNm"/></option>
		</select>		
		<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
		<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
		<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
	</div>
</form>
<form:form id="listForm" name="listForm" method="post" autocomplete="off">
	<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
	<table class="list_table">
		<colgroup>
			<col style=""/>
			<col style=""/>
			<col style=""/>
			<col style=""/>
			<col style=""/>
			<col style=""/>
			<col style=""/>
		</colgroup>
		<thead>
			<tr>
				<th>No.</th>
				<th><spring:message code="templateMgtVO.templateCd"/></th>
				<th><spring:message code="templateMgtVO.templateNm"/></th>
				<th><spring:message code="templateMgtVO.useYn"/></th>
				<th><spring:message code="templateMgtVO.inptDttm"/></th>
				<th><spring:message code="button.preview"/></th>
				<th><spring:message code="button.copy"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.templateCd }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.templateCd}"/></a></td>
					<td><c:out value="${item.templateNm}"/></td>
					<td><c:out value="${item.useYn}"/></td>
					<td><c:out value="${item.inptDttm}"/></td>
					<td><a class="table_btn" href="javascript:fn_preview('<c:out value="${item.templateCd }"/>');"><spring:message code="button.preview"/></a></td>
					<td><a class="table_btn" href="javascript:fn_copyView('<c:out value="${item.templateCd }"/>');"><spring:message code="button.copy"/></a></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="7"><spring:message code="board.noData" /></td></tr>
			</c:if>
		</tbody>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId">
   	<input type="hidden" name="checkedSId">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>
   	
	   	
	<% /** 페이징 */ %>
	<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
	<% /** //페이징 */ %>
</form:form>

<script type="text/javaScript" defer="defer">

	//검색
	function fn_search(){
		var searchFrm = document.boardSearchForm;
		document.getElementById("_search_so_").value = searchFrm.so.value;
		document.getElementById("_search_sv_").value = searchFrm.sv.value;
		gf_movePage(1);
	}
	
	//등록
	function fn_registerView() {
		document.listForm.action = GV_PRESENT_PATH + "/register.do";
		document.listForm.submit();
	}
	
	//복사
	function fn_copyView(sId) {
		document.listForm.sId.value = sId;
		document.listForm.action = GV_PRESENT_PATH + "/copy.do";
		document.listForm.submit();
	}
	
	//수정
	function fn_modifyView() {
		document.listForm.action = GV_PRESENT_PATH + "/modify.do";
		document.listForm.submit();
	}

	//상세보기
	function fn_detailView(sId) {
		document.listForm.sId.value = sId;
		document.listForm.action = GV_PRESENT_PATH + "/view.do";
		document.listForm.submit();
	}

	//미리보기
	function fn_preview(sId) {
		window.open(GV_PRESENT_PATH + "/preview.do?sId="+sId, "pop-preview", "fullscreen=yes");
	}
	
</script>