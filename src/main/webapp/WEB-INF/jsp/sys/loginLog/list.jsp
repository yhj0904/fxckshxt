<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 로그인 로그 목록 화면
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
			<option value="loginId" <c:if test="${search.so eq 'loginId'}">selected</c:if>><spring:message code="loginLogVO.loginId"/></option>
			<option value="loginNm" <c:if test="${search.so eq 'loginNm'}">selected</c:if>><spring:message code="loginLogVO.loginNm"/></option>
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
		</colgroup>
		<thead>
			<tr>
				<th><spring:message code="board.no"/></th>
				<th>
					<a href="javascript:gf_changeOrder('loginType')" class="link order <c:if test='${searchVO.oc eq "loginType"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginType"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('loginId')" class="link order <c:if test='${searchVO.oc eq "loginId"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginId"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('loginNm')" class="link order <c:if test='${searchVO.oc eq "loginNm"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginNm"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('loginIp')" class="link order <c:if test='${searchVO.oc eq "loginIp"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginIp"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('loginUrl')" class="link order <c:if test='${searchVO.oc eq "loginUrl"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginUrl"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('loginDttm')" class="link order <c:if test='${searchVO.oc eq "loginDttm"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginDttm"/>
					</a>
				</th>
				<th>
					<a href="javascript:gf_changeOrder('loginSuccess')" class="link order <c:if test='${searchVO.oc eq "loginSuccess"}'><c:out value='${searchVO.ob }'/></c:if>">
						<spring:message code="loginLogVO.loginSuccess"/>
					</a>
				</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${list }" varStatus="i">
			<tr>
				<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
				<td><c:out value="${item.loginType}"/></td>
				<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.loginCode }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${item.loginId}"/></a></td>
				<td><c:out value="${item.loginNm}"/></td>
				<td><c:out value="${item.loginIp}"/></td>
				<td><c:out value="${item.loginUrl}"/></td>
				<td><c:out value="${item.loginDttm}"/></td>
				<td><c:out value="${item.loginSuccess}"/></td>
			</tr>
		</c:forEach>
		<c:if test="${empty list}">
			<tr><td class="no_data" colspan="8"><spring:message code="board.noData" /></td></tr>
		</c:if>
		</tbody>
  	</table>
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button list_btn" href="/sys/log/pw.do">비밀번호 오류 목록</a>
			</li>
			<li>
				<a class="button download_btn" href="javascript:fn_excelDown();"><spring:message code="button.excelDownload" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId">
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