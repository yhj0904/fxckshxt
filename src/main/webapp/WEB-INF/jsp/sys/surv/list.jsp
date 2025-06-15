<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 설문 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
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
			<option value="survTitle" <c:if test="${search.so eq 'survTitle'}">selected</c:if>><spring:message code="survMgtVO.survTitle"/></option>
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
			<col style=""/> <!-- 설문ID -->
			<col style=""/> <!-- 설문제목 -->
			<col style=""/> <!-- 시작일자 -->
			<col style=""/> <!-- 종료일자 -->
			<col style=""/> <!-- 사용유무 -->
			<col style=""/> <!-- 등록일 -->
			<col style=""/> <!-- 문항관리 -->
		</colgroup>
		<thead>
			<tr>
				<th><spring:message code="board.no"/></th>
				<th><spring:message code="survMgtVO.survId"/></th>
				<th><spring:message code="survMgtVO.survTitle"/></th>
				<th><spring:message code="survMgtVO.survDate1"/></th>
				<th><spring:message code="survMgtVO.survDate2"/></th>
				<th><spring:message code="survMgtVO.useYn"/></th>
				<th><spring:message code="survMgtVO.inptDttm"/></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${list }" varStatus="i">
			<tr>
				<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
				<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.survId }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.survId}"/></a></td>
				<td><c:out value="${item.survTitle}"/></td>
				<td><c:out value="${item.survDate1}"/></td>
				<td><c:out value="${item.survDate2}"/></td>
				<td><c:out value="${item.useYn}"/></td>
				<td><c:out value="${item.inptDttm}"/></td>
				<td>
					<c:choose>					
						<c:when test='${item.survState eq "N"}'>
							<a href="javascript:fn_manageView('<c:out value="${item.survId }"/>');" title=""><spring:message code="survMgtVO.message.manage"/></a>
							<a href="javascript:fn_modifyState('<c:out value="${item.survId }"/>', 'S');" title=""><spring:message code="survMgtVO.message.start"/></a>
						</c:when>
						<c:when test='${item.survState eq "S"}'>
							<a href="javascript:fn_modifyState('<c:out value="${item.survId }"/>', 'E');" title=""><spring:message code="survMgtVO.message.end"/></a>
						</c:when>
						<c:when test='${item.survState eq "E"}'>
							<a href="javascript:fn_resultView('<c:out value="${item.survId }"/>');" title=""><spring:message code="survMgtVO.message.result"/></a>
						</c:when>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty list}">
			<tr><td class="no_data" colspan="8"><spring:message code="board.noData" /></td></tr>
		</c:if>
		</tbody>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
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
	
	//문항관리
	function fn_manageView(sId) {
		document.listForm.sId.value = sId;
		document.listForm.action = GV_PRESENT_PATH + "/manage.do";
		document.listForm.submit();
	}
	
	//설문상태 변경
	function fn_modifyState(survId, survState) {		
		var confirmMsg = "";
		
		if(survState == "S"){
			confirmMsg = '<spring:message code="survMgtVO.message.confirmStart" />';
		} else if (survState == "E"){
			confirmMsg = '<spring:message code="survMgtVO.message.confirmEnd" />';
		}
		
		if(confirm(confirmMsg)){
			gf_ajax({
				url : GV_PRESENT_PATH + "/modifyState.do",
				type : "POST",
				data : {survId: survId, survState : survState},
			}).then(function(response){
				if(response){
					fn_search();
				} else {
					alert("<spring:message code='message.error'/>");
				}
			});
		}
	}
	
	//설문결과
	function fn_resultView(sId) {
		document.listForm.sId.value = sId;
		document.listForm.action = GV_PRESENT_PATH + "/result.do";
		document.listForm.submit();
	}
	
</script>