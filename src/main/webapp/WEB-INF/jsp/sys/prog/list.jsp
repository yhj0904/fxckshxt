<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 프로그램 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */
%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="yearStart"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set> 

<div>
	<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
</div>
<div class="search-form-wr">
	<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
		<div class="search_box">
			<select name="srch_year" title="년도 선택" id="year">
				<option value="" >년도</option>
				<c:forEach var="year1" begin="0" end="10" step="1">
					<option value="${yearStart - year1 }" <c:if test="${(yearStart - year1) eq search_year }">selected</c:if>><c:out value="${yearStart - year1 }"/></option>
				</c:forEach>
			</select>
			<select name="srch_semstrCd" title="학기 선택" id="semstrCd">
				<option value="" >학기</option>
				<option value="10" >1학기</option>
				<option value="20" >2학기</option>
			</select>
			<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
			<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
			<button type="button" class="reset_btn" onclick="fn_reset();"><spring:message code="button.search.reset"/></button>
		</div>
	</form>
	<div class="btn_wrap">
		<ul>
			<c:if test='${loginInfo.statDvcd eq "SYS_ADM" }'>
				<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
				<li><a class="button list_btn" href="javascript:fn_survResult();">설문결과</a></li>
				<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
			</c:if>
		</ul>
	</div>
</div>
<form:form id="listForm" name="listForm" method="post" autocomplete="off">
	<table class="list_table fixed">
		<colgroup>
			<c:if test='${loginInfo.statDvcd eq "SYS_ADM" }'>
				<col style="width: 3%;"/> <!-- CHECKBOX -->
			</c:if>
			<col style="width: 4.2%;"/> <!-- NO -->
			<col style="width: 4.5%;"/> <!-- 상태 -->
			<col style="width: 25%;"/> <!-- 프로그램 명 -->
			<col style="width: 7%;"/> <!-- 모집인원 / 신청인원 -->
			<col style="width: 10%;"/> <!-- 교육방법 -->
			<col style="width: 8%;"/> <!-- 신청일 -->
			<col style="width: 8%;"/> <!-- 교육일 -->
			<col style=""/> <!-- 커리어 단게 -->
			<col style=""/> <!-- 담당자(연락처) -->
			<col style=""/> <!-- 수강생 관리 -->
		</colgroup>
		<thead>
			<tr>
				<c:if test='${loginInfo.statDvcd eq "SYS_ADM" }'>
					<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
				</c:if>
				<th><spring:message code="board.no"/></th>
				<th><spring:message code="progVO.statusCd"/></th>
				<th><spring:message code="progVO.progNm"/></th>
				<th><spring:message code="progVO.applNmpr"/><br/>(<spring:message code="progVO.reqstNmpr"/>)</th>
				<th><spring:message code="progVO.progMthCd"/></th>
				<th>신청일</th>
				<th>교육일</th>
				<th><spring:message code="progVO.progCareerCd"/></th>
				<th>담당자<br/>연락처</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<c:if test='${loginInfo.statDvcd eq "SYS_ADM"}'>
						<td>
							<c:choose>
								<c:when test='${item.delYn eq "Y" }'>
									&nbsp;
								</c:when>
								<c:otherwise>
									<label><input type="checkbox" name="checkRow" value="<c:out value="${item.progId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
								</c:otherwise>
							</c:choose>						
						</td>
					</c:if>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><c:out value="${item.dday}"/></td>
					<td>
						<div class='tit'>
							<a class="link" href="javascript:fn_detailView('<c:out value="${item.progId }"/>');" title="<spring:message code="board.detail"/>">
								<c:out value="${item.progNm}" escapeXml="false"/>
							</a>
						</div>
					</td>	
					<td><c:out value="${item.applNmpr}"/>/<c:out value="${item.reqstNmpr}"/></td>
					<td>
						<ul>
							<li><c:out value="${item.progMthCdNm}"/></li>
							<c:if test="${!empty item.progContactCdNm}">
								<li>(<c:out value="${item.progContactCdNm}"/>)</li>
							</c:if>
						</ul>
					</td>
					<td>
						<c:out value="${item.reqstSdt} ~ ${item.reqstEdt}"/>
					</td>
					<td>
						<c:out value="${item.progSdt} ~ ${item.progEdt}"/>
					</td>
					<td><c:out value="${item.progCareerCdNm}"/></td>
					<td>
						<ul>
							<li><c:out value="${item.eduMngNm}"/></li>
							<li>(<c:out value="${item.eduTelNo}"/>)</li>
						</ul>
					</td>
					<td>
						<a class="button appl" href="javascript:fn_progApplList('<c:out value="${item.progId }"/>');">수강생 관리</a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="11"><spring:message code="board.noData" /></td></tr>
			</c:if>
		</tbody>
  	</table>
  	
  	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
   	<input type="hidden" name="checkedSId">
	<input type="hidden" id="search_year" name="search_year" value="<c:out value='${search.search_year }'/>">
	<input type="hidden" id="search_semstrCd" name="search_semstrCd" value="<c:out value='${search.search_semstrCd }'/>">
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
		//document.getElementById("_search_so_").value = searchFrm.so.value;
		document.getElementById("_search_sv_").value = searchFrm.sv.value;
		document.getElementById("search_year").value = searchFrm.year.value;
		document.getElementById("search_semstrCd").value = searchFrm.semstrCd.value;
		//document.getElementById("_search_sd_").value = searchFrm.sd.value;
		//document.getElementById("_search_ed_").value = searchFrm.ed.value;
		gf_movePage(1);
	}
	
	function fn_reset() {
		document.getElementById("_search_sv_").value = "";
		document.getElementById("search_year").value = "";
		document.getElementById("search_semstrCd").value = "";
		location.href = GV_PRESENT_PATH + "/list.do";
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
			gf_alert('<spring:message code="message.alert.nocheck" />');
		} else {
			var msg = gf_confirm('<spring:message code="message.confirm.remove" />', function(e){
				if(e == "Y"){
					gf_startAjaxing();
					
					document.listForm.checkedSId.value = checkedSId;
					document.listForm.action = GV_PRESENT_PATH + "/checkRemoveAction.do";
					document.listForm.submit();	
				}
			});
		}
	}
	
	//선택삭제
	function fn_survResult() {
		var checkedSId = "";
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkRow");
		var rowCnt = rowArr.length;
		for (var i = 0; i < rowArr.length; i++) {
			if (rowArr[i].type == "checkbox" && rowArr[i].checked){
				checkedSId += rowArr[i].value;
				chkCnt++;
			}
		}
		if(chkCnt <= 0) {
			gf_alert('<spring:message code="message.alert.nocheck" />');
			
			return false;
		}
		
		if(chkCnt > 1) {
			gf_alert('설문 결과 선택은 1개만 선택해주세요.');
			
			return false;
		}
		
		document.listForm.checkedSId.value = checkedSId;
		document.listForm.action = "/sys/prog_surv/result.do?progId=" + checkedSId;
		document.listForm.submit();	
	}

	//상세보기
	function fn_detailView(sId) {
		document.listForm.sId.value = sId;
		document.listForm.action = GV_PRESENT_PATH + "/view.do";
		document.listForm.submit();
	}
	
	function fn_progApplList(sId){
		document.listForm.sId.value = sId;
		document.listForm.action = "/sys/prog/apply/list.do";
		document.listForm.submit();
	}
	
</script>