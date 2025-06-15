<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 시스템 로그 목록 화면
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
		<select name="sProgram" title="Choose Program">
			<option value="">All</option>
			<c:forEach var="pr" items="${programList }">
				<option value="<c:out value="${pr.code }"/>" <c:if test="${pr.code eq sProgram}">selected</c:if>><c:out value="${pr.name }"/></option>
			</c:forEach>
		</select>
		<select name="so" title="<spring:message code="search.choose"/>">
			<option value="sysCode" <c:if test="${search.so eq 'sysCode'}">selected</c:if>><spring:message code="sysLogVO.sysCode"/></option>
			<option value="className" <c:if test="${search.so eq 'className'}">selected</c:if>><spring:message code="sysLogVO.className"/></option>
			<option value="methodName" <c:if test="${search.so eq 'methodName'}">selected</c:if>><spring:message code="sysLogVO.methodName"/></option>
			<option value="methodDesc" <c:if test="${search.so eq 'methodDesc'}">selected</c:if>><spring:message code="sysLogVO.methodDesc"/></option>
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
			<col style=""/> <!-- 로그코드 -->
			<col style=""/> <!-- 프로그램 코드 -->
			<col style=""/> <!-- 프로그램명 -->
			<col style=""/> <!-- 메소드명 -->
			<col style=""/> <!-- 프로세스 코드 -->
			<col style=""/> <!-- 프로세스 시간 -->
			<col style=""/> <!-- 로그 ID -->
			<col style=""/> <!-- 이름 -->
			<col style=""/> <!-- 아이피 -->
			<col style=""/> <!-- 로그 시간 -->
			<col style=""/> <!-- 에러여부 -->
		</colgroup>
		<thead>
			<tr>
				<th><spring:message code="board.no"/></th>
				<th>
					<a href="javascript:gf_changeOrder('sysCode')" class="link order <c:if test='${searchVO.oc eq "sysCode"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.sysCode"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('programCd')" class="link order <c:if test='${searchVO.oc eq "programCd"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.programCd"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('programNm')" class="link order <c:if test='${searchVO.oc eq "programNm"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.programNm"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('methodName')" class="link order <c:if test='${searchVO.oc eq "methodName"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.methodName"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('processCode')" class="link order <c:if test='${searchVO.oc eq "processCode"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.processCode"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('processTime')" class="link order <c:if test='${searchVO.oc eq "processTime"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.processTime"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('logId')" class="link order <c:if test='${searchVO.oc eq "logId"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.logId"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('logName')" class="link order <c:if test='${searchVO.oc eq "logName"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.logName"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('logIp')" class="link order <c:if test='${searchVO.oc eq "logIp"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.logIp"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('logDttm')" class="link order <c:if test='${searchVO.oc eq "logDttm"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.logDttm"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('errYn')" class="link order <c:if test='${searchVO.oc eq "errYn"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="sysLogVO.errYn"/>
					</a>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.sysCode }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.sysCode}"/></a></td>
					<td><c:out value="${item.programCd}"/></td>
					<td><c:out value="${item.programNm}"/></td>
					<td><c:out value="${item.methodName}"/></td>
					<td><c:out value="${item.processCode}"/></td>
					<td><c:out value="${item.processTime}"/></td>
					<td><c:out value="${item.logId}"/></td>
					<td><c:out value="${item.logName}"/></td>
					<td><c:out value="${item.logIp}"/></td>
					<td><c:out value="${item.logDttm}"/></td>
					<td><c:out value="${item.errYn}"/></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="12"><spring:message code="board.noData" /></td></tr>
			</c:if>
		</tbody>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button download_btn" href="javascript:fn_excelDown();"><spring:message code="button.excelDownload" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId">	
  	<input type="hidden" name="sProgram" value="<c:out value='${sProgram }'/>">
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
		document.listForm.sProgram.value = searchFrm.sProgram.value;
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
	
</script>