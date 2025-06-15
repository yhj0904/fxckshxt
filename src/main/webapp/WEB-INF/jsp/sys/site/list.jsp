<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 사이트관리 목록 화면
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
			<option value="siteCd" <c:if test="${search.so eq 'siteCd'}">selected</c:if>><spring:message code="siteVO.siteCd"/></option>
			<option value="siteNm" <c:if test="${search.so eq 'siteNm'}">selected</c:if>><spring:message code="siteVO.siteNm"/></option>
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
			<col style=""/> <!-- NO -->
			<col style=""/> <!-- 코드 -->
			<col style=""/> <!-- 사이트명 -->
			<col style=""/> <!-- 입력일시 -->
			<col style=""/> <!-- 사용유무 -->
		</colgroup>
		<thead>
		<tr>
			<th>No.</th>
			<th><spring:message code="siteVO.siteCd"/></th>
			<th><spring:message code="siteVO.siteNm"/></th>
			<th><spring:message code="siteVO.inptDttm"/></th>
			<th><spring:message code="siteVO.useYn"/></th>
		</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><c:out value="${item.siteCd}"/></td>
					<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.siteCd }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.siteNm}"/></a></td>
					<td><c:out value="${item.inptDttm}"/></td>
					<td><c:out value="${item.useYn}"/></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>
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
	
</script>