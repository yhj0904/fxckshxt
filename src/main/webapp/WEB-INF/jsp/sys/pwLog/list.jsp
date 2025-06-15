<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 패스워드 오류 로그 목록 화면
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
			<option value="loginId" <c:if test="${search.so eq 'loginId'}">selected</c:if>><spring:message code="loginLogVO.loginId"/></option>
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
				<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
				<th><spring:message code="board.no"/></th>
				<th><spring:message code="loginLogVO.loginType"/></th>
				<th><spring:message code="loginLogVO.loginId"/></th>
				<th>COUNT</th>
				<th>초기화</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${list }" varStatus="i">
			<tr>
				<td><label><input type="checkbox" name="checkRow" value="<c:out value="${item.USER_TYPE }"/>#<c:out value="${item.USER_ID }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label></td>
				<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
				<td><c:out value="${item.USER_TYPE}"/></td>
				<td><c:out value="${item.USER_ID}"/></td>
				<td><c:out value="${item.CNT}"/></td>
				<td><a class="button remove_btn" href="javascript:fn_removeAction('<c:out value="${item.USER_TYPE }"/>', '<c:out value="${item.USER_ID }"/>');">초기화</a></td>
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
				<a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sType">
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
	
	//초기화
	function fn_removeAction(sType, sId) {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.listForm.sType.value = sType;
			document.listForm.sId.value = sId;
			document.listForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.listForm.submit();
		}
	}
	
</script>