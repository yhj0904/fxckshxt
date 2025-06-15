<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 사용자 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */
%>
<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
	<div class="search_box">
		<div class="period">
			<label><spring:message code="search.period"/></label>
			<input type="text" name="sd" value="<c:out value='${search.sd }'/>" onclick="gf_datepicker(this);" title="<spring:message code="search.startdate"/>">
			<span>~</span>
			<input type="text" name="ed" value="<c:out value='${search.ed }'/>" onclick="gf_datepicker(this);" title="<spring:message code="search.enddate"/>">
		</div>
		<select name="so" title="<spring:message code="search.choose"/>">
			<option value="userId" <c:if test="${search.so eq 'userId'}">selected</c:if>><spring:message code="userVO.userId"/></option>
			<option value="userNm" <c:if test="${search.so eq 'userNm'}">selected</c:if>><spring:message code="userVO.userNm"/></option>
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
			<col style=""/> <!-- 사용자ID -->
			<col style=""/> <!-- 사용자명 -->
			<!-- <col style=""/> 사용시작일시
			<col style=""/> 사용종료일시 -->
			<col style=""/> <!-- 유저타입 -->
			<col style=""/> <!-- 부서명 -->
			<!-- <col style=""/> 생년월일 -->
			<col style=""/> <!-- EMAIL -->
			<col style=""/> <!-- 사용여부 -->
			<col style=""/> <!-- 입력일시 -->
		</colgroup>
		<thead>
			<tr>
				<th><spring:message code="board.no"/></th>
				<th><spring:message code="userVO.userId"/></th>
				<th><spring:message code="userVO.userNm"/></th>
				<%-- <th><spring:message code="userVO.useSttDttm"/></th>
				<th><spring:message code="userVO.useEndDttm"/></th> --%>
				<th><spring:message code="userVO.userTypeCd"/></th>
				<th>학과명</th>
				<%-- <th><spring:message code="userVO.birth"/></th> --%>
				<th><spring:message code="userVO.email"/></th>
				<th><spring:message code="userVO.useYn"/></th>
				<th><spring:message code="userVO.inptDttm"/></th>				
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.userId }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.userId}"/></a></td>
					<td><c:out value="${item.userNm}"/></td>
					<%-- <td><c:out value="${item.useSttDttm}"/></td>
					<td><c:out value="${item.useEndDttm}"/></td> --%>
					<td><c:out value="${item.userTypeCdNm}"/></td>
					<td><c:out value="${item.deptNm}" escapeXml="false" /></td>
					<%-- <td><c:out value="${item.birth}"/></td> --%>
					<td><c:out value="${item.email}"/></td>
					<td><c:out value="${item.useYn}"/></td>
					<td><c:out value="${item.inptDttm}"/></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="10"><spring:message code="board.noData" /></td></tr>
			</c:if>
		</tbody>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button list_btn" href="javascript:fn_excelDown();">엑셀 다운</a></li>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button list_btn" href="/sys/user/del/list.do">탈퇴목록</a>
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
		document.getElementById("_search_sd_").value = searchFrm.sd.value;
		document.getElementById("_search_ed_").value = searchFrm.ed.value;
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
	
	//엑셀다운
	function fn_excelDown(){
		document.listForm.action = "/sys/user/excelDown.do";
		document.listForm.submit();
	}
	
</script>