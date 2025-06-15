<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="kr.co.nanwe.dept.service.DeptService" %>
<%@ page import="kr.co.nanwe.dept.service.DeptVO" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 게시글 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<link rel="stylesheet" href="/css/cnsl/cnsl.css" />

<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
	<div class="search_box">
		<div class="period">
			<label><spring:message code="search.period"/></label>
			<input type="text" name="sd" value="<c:out value='${search.sd }'/>" onclick="gf_datepicker(this);" title="<spring:message code="search.startdate"/>">
			<span>~</span>
			<input type="text" name="ed" value="<c:out value='${search.ed }'/>" onclick="gf_datepicker(this);" title="<spring:message code="search.enddate"/>">
		</div>
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

<div class="btn_wrap" style="width: unset; float: left; margin-top: -30px;">
	<ul>
		<li><a class="button register_btn" href="javascript:fn_registCnslSch();">일정<spring:message code="button.register" /></a></li>
		<li><a class="button register_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>

<form:form id="listForm" name="listForm" method="post" autocomplete="off">
	
	<div class="calendar">
		<div class="cal_tit">			
			<p><c:out value='${calendar.sYear }'/>년 <c:out value='${calendar.sMonth }'/>월</p>
			<a class="cal_btn prev" href="javascript:fn_changeCalendar('<c:out value='${cnslerId }' />', 'PREV')"><span class="none">이전</span></a>
			<a class="cal_btn next" href="javascript:fn_changeCalendar('<c:out value='${cnslerId }' />', 'NEXT')"><span class="none">다음</span></a>
		</div>
		<div class="cal_cont">
			<table>
				<thead>
					<tr>
						<th><span class="sun">일</span></th>
						<th><span>월</span></th>
						<th><span>화</span></th>
						<th><span>수</span></th>
						<th><span>목</span></th>
						<th><span>금</span></th>
						<th><span class="sat">토</span></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="cal" items="${calendar.list }" varStatus="i">
						<c:if test='${cal.dayNum eq 1}'>
							<tr>
						</c:if>
						<td>
							<span class="<c:out value='${cal.background }'/>"><c:out value='${cal.day }'/></span>
							<ul>
								<c:forEach var="item" items="${cal.schList }" varStatus="i">
									<li>
										<a class="link" href="javascript:fn_detailView('<c:out value="${item.cnslerId }"/>', '<c:out value="${cal.date }"/>');" title="<spring:message code="board.detail"/>">
											<c:choose>
												<c:when test="${item.cnslAmYn eq 'Y' && item.cnslPmYn eq 'Y'}">
													종일
												</c:when>
												<c:when test="${item.cnslAmYn eq 'Y'}">
													오전
												</c:when>
												<c:when test="${item.cnslPmYn eq 'Y'}">
													오후
												</c:when>
												<c:otherwise>
													선택안함
												</c:otherwise>
											</c:choose>
										</a>
									</li>
								</c:forEach>
							</ul>
						</td>
						<c:if test='${cal.dayNum eq 7}'>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>		
		</div>
	</div>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button register_btn" href="javascript:fn_registCnslSch();">일정<spring:message code="button.register" /></a></li>
			<li><a class="button register_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" />
   	<input type="hidden" name="schDt" />
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
	<input type="hidden" name="sDate" value="<c:out value='${sDate }'/>">
	<input type="hidden" name="sOption" value="" />
   	<input type="hidden" name="checkedSId" />
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>
  	
	<% /** 페이징 */ %>
	<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
	<% /** //페이징 */ %>
</form:form>

<form name="schForm" class="schForm" method="post" autocomplete="off">
 	<% /** 값전달 부분 */ %>
  	<input type="hidden" name="cnslerId" id="cnslerId" />
  	<input type="hidden" name="stDt" id="stDt" />
  	<input type="hidden" name="edDt" id="edDt" />
  	<input type="hidden" name="cnslAmYn" id="cnslAmYn" />
  	<input type="hidden" name="cnslPmYn" id="cnslPmYn" />
  	<input type="hidden" name="selectDay" id="selectDay" />
  	<% /** 값전달 부분 */ %>
  	
  	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
</form>

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
	
	//목록
	function fn_listView() {
		document.listForm.action = GV_PRESENT_PATH + "/list.do";
		document.listForm.submit();
	}

	//상세보기
	function fn_detailView(sId, schDt) {
		document.listForm.sId.value = sId;
		document.listForm.schDt.value = schDt;
		document.listForm.action = "/sys/cnsler/cnslerSch_view.do";
		document.listForm.submit();
	}
	
	//달력 월변경
	function fn_changeCalendar(sId, sOption){
		document.listForm.sId.value = sId;
		document.listForm.sOption.value = sOption;
		document.listForm.action = gf_getPathName();
		document.listForm.submit();
	}
	
	//상담원일정 등록
	function fn_registCnslSch(){
		var url = "/sys/cnsler/cnslerRegisterSch.do?sId="+"<c:out value='${cnslerId}' />";
		var width = 600;
		var height = 500;
		var left = Math.ceil((window.screen.width - width)/2);
	    var top = Math.ceil((window.screen.height - height)/2);

		window.open(url, '', 'status=no, width='+width+', height='+height+', left='+left+', top='+top+', screenX='+left+', screenY='+top+', scrollbars=yes')
	}
	
	//상담일정 상세보기
	
</script>