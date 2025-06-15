<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 그룹관리 목록 화면
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
			<option value="userId" <c:if test="${search.so eq 'userId'}">selected</c:if>><spring:message code="pushGrpMstVO.grpCreUser"/></option>
			<option value="grpNm" <c:if test="${search.so eq 'grpNm'}">selected</c:if>><spring:message code="pushGrpMstVO.grpNm"/></option>
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
				<th><spring:message code="pushGrpMstVO.grpCd"/></th>
				<th><spring:message code="pushGrpMstVO.grpCreUser"/></th>
				<th><spring:message code="pushGrpMstVO.grpNm"/></th>
				<th><spring:message code="pushGrpMstVO.memCnt"/></th>
				<th><spring:message code="pushGrpMstVO.inptDttm"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<td><label><input type="checkbox" name="checkRow" value="<c:out value="${item.grpCd }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label></td>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.grpCd }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.grpCd}"/></a></td>
					<td><c:out value="${item.grpCreUser}"/></td>
					<td><c:out value="${item.grpNm}"/></td>
					<td><c:out value="${item.memCnt}"/></td>
					<td><c:out value="${item.inptDttm}"/></td>
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
			<li>
				<a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a>
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
	
	//엑셀다운
	function fn_excelDown() {
		document.listForm.action = GV_PRESENT_PATH + "/excelDown.do";
		document.listForm.submit();
		document.listForm.action = gf_getPathName();
	}
	
	//엑셀업로드
	function fn_excelUploadView() {
		document.listForm.action = GV_PRESENT_PATH + "/excelUpload.do";
		document.listForm.submit();
	}
	
</script>