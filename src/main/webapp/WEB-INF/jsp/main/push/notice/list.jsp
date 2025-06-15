<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 알림관리 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="sub_title">
	알림발송내역
</div>
<div class="sub_content">
	<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
		<div class="search_box">
			<select name="so" title="<spring:message code="search.choose"/>">
				<option value="userId" <c:if test="${search.so eq 'userId'}">selected</c:if>><spring:message code="pushNoticeVO.userId"/></option>
				<option value="grpNm" <c:if test="${search.so eq 'userNm'}">selected</c:if>><spring:message code="pushNoticeVO.userNm"/></option>
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
				<col style=""/>
				<col style=""/>
			</colgroup>
			<thead>
				<tr>
					<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
					<th><spring:message code="board.no"/></th>
					<th><spring:message code="pushNoticeVO.noticeTitle"/></th>
					<th><spring:message code="pushNoticeVO.userNm"/></th>
					<th><spring:message code="pushNoticeVO.totalCnt"/></th>
					<th><spring:message code="pushNoticeVO.pushCnt"/></th>
					<th><spring:message code="pushNoticeVO.smsCnt"/></th>
					<th><spring:message code="pushNoticeVO.pushState"/></th>
					<th><spring:message code="pushNoticeVO.noticeDt"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${list }" varStatus="i">
					<tr>
						<td><label><input type="checkbox" name="checkRow" value="<c:out value="${item.noticeNo }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label></td>
						<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
						<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.noticeNo }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.noticeTitle}"/></a></td>
						<td><c:out value="${item.userNm}"/>(<c:out value="${item.userId}"/>)</td>
						<td><c:out value="${item.totalCnt}"/></td>
						<td><c:out value="${item.pushCnt}"/></td>
						<td><c:out value="${item.smsCnt}"/></td>
						<td><c:out value="${item.pushState}"/></td>
						<td><c:out value="${item.noticeDt}"/></td>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr><td class="no_data" colspan="9"><spring:message code="board.noData" /></td></tr>
				</c:if>
			</tbody>
	  	</table>
		
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
	
		//상세보기
		function fn_detailView(sId) {
			document.listForm.sId.value = sId;
			document.listForm.action = GV_PRESENT_PATH + "/view.do";
			document.listForm.submit();
		}	
	</script>
</div>