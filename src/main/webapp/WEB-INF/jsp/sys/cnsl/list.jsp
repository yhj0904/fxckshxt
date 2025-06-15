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
		<select name="so" title="<spring:message code="search.choose"/>">
			<option value="cnslerId" <c:if test="${search.so eq 'cnslerId'}">selected</c:if>>상담원 아이디</option>
			<c:if test='${bbsMgtVO.nonameYn ne "Y" }'>
				<option value="cnslTypeCd" <c:if test="${search.so eq 'cnslTypeCd'}">selected</c:if>>상담분야</option>
				<option value="inptId" <c:if test="${search.so eq 'inptId'}">selected</c:if>><spring:message code="bbsVO.inptId"/></option>
			</c:if>
		</select>
		<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
		<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
		<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
	</div>
</form>
<form:form id="listForm" name="listForm" method="post" autocomplete="off">
	<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
	<% //게시글 유형 %>
	<c:if test='${bbsMgtVO.cateYn eq "Y" and !empty bbsMgtVO.categoryList }'>
		<div class="table_category">
			<ul>
				<li <c:if test="${sCate eq 'ALL' }">class="active"</c:if>><a href="javascript:fn_changeCategory('ALL');"><spring:message code="search.all"/></a></li>
				<c:forEach var="cate" items="${bbsMgtVO.categoryList }" varStatus="i">
					<li <c:if test="${sCate eq cate}">class="active"</c:if>><a href="javascript:fn_changeCategory('<c:out value='${cate }'/>');"><c:out value='${cate }'/></a></li>
				</c:forEach>
			</ul>
		</div>
	</c:if>
	<% //게시글 유형 %>
	<table class="list_table">
		<colgroup>
			<col style=""/>
			<col style=""/> <!-- NO -->
			<col style=""/> <!-- 제목 -->
			<col style=""/> <!-- 작성자 -->
			<col style=""/> <!-- 조회수 -->
			<col style=""/> <!--  -->
			<col style=""/> <!--  -->
			<col style=""/> <!--  -->
		</colgroup>
		<thead>
			<tr>
				<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
				<th><spring:message code="board.no"/></th>
				<th>상담번호</th>
				<th>상담원</th>
				<th>신청자(학번)</th>
				<th>단과/학과</th>
				<th>학년</th>
				<th>성별</th>
				<th>상담분야</th>
				<th>상담방법</th>
				<th>상담신청일</th>
				<th>진행상태</th>
				<th><spring:message code="bbsVO.inptDttm"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<td><label><input type="checkbox" name="checkRow" value="<c:out value="${item.CNSL_ID }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label></td>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.CNSL_ID}" />','<c:out value="${item.USER_ID}" />');" ><c:out value="${item.CNSL_ID}"/></a></td>
					<td><c:out value="${item.CNSLER_NM}"/></td>
					<td><c:out value="${item.USER_NM}"/></td>
					<c:set var="colgCd" value="${item.COLG_CD}" />
					<c:set var="deptCd" value="${item.DEPT_CD}" />
					<%
						DeptService deptService = (DeptService) webApplicationContext.getBean("deptService");
					
						//학부 이름
						String colgCd = (String) pageContext.getAttribute("colgCd");
						DeptVO colgCdVO = deptService.selectDept(colgCd);
						pageContext.setAttribute("colgCdVO", colgCdVO);
						
						//학과 이름
						String deptCd = (String) pageContext.getAttribute("deptCd");
						DeptVO deptCdVO = deptService.selectDept(deptCd);
						pageContext.setAttribute("deptCdVO", deptCdVO);
					%>
					<td><c:out value="${colgCdVO.deptNmKor}"/> / <c:out value="${deptCdVO.deptNmKor}"/></td>
					<td><c:out value="${item.GRADE}"/>학년</td>
					<td><c:out value="${item.SEX_CD}"/></td>
					<c:set var="cnslTypeCd" value="${item.CNSL_TYPE_CD}" />
					<%
						CommCdService commCdService = (CommCdService) webApplicationContext.getBean("commCdService");

						String cnslTypeCd = (String) pageContext.getAttribute("cnslTypeCd");
						CommCdVO cnslTypeCdVO = commCdService.selectCommCd(cnslTypeCd);
						pageContext.setAttribute("cnslTypeCdVO", cnslTypeCdVO);
					%>
					<td id="cateTd">
						<ul>
						<c:if test="${item.CNSL_TYPE_TRACK eq 'Y' }">
							<li>진로</li>
						</c:if>
						<c:if test="${item.CNSL_TYPE_JOB eq 'Y' }">
							<li>취업</li> 
						</c:if>
						<c:if test="${item.CNSL_TYPE_LIFE eq 'Y' }">
							<li>생활</li> 
						</c:if>
						<c:if test="${item.CNSL_TYPE_RESUME eq 'Y' }">
							<li>입사지원서</li> 
						</c:if>
						<c:if test="${item.CNSL_TYPE_INTV eq 'Y' }">
							<li>모의면접</li> 
						</c:if>
						</ul>
					</td>
					<c:set var="cnslMthCd" value="${item.CNSL_MTH_CD}" />
					<%
						String cnslMthCd = (String) pageContext.getAttribute("cnslMthCd");
						CommCdVO cnslMthCdVO = commCdService.selectCommCd(cnslMthCd);
						pageContext.setAttribute("cnslMthCdVO", cnslMthCdVO);
					%>
					<td><c:out value="${cnslMthCdVO.cdNm}"/></td>
					<td><c:out value="${item.HOPE_DT}"/></td>
					<c:set var="cnslStatCd" value="${item.CNSL_STATUS_CD}" />
					<%
						String cnslStatCd = (String) pageContext.getAttribute("cnslStatCd");
						CommCdVO cnslStatCdVO = commCdService.selectCommCd(cnslStatCd);
						pageContext.setAttribute("cnslStatCdVO", cnslStatCdVO);
					%>
					<td><c:out value="${cnslStatCdVO.cdNm}"/></td>
					<td><c:out value="${item.INPT_DTTM}"/></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="8"><spring:message code="board.noData" /></td></tr>
			</c:if>
		</tbody>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button list_btn" href="javascript:fn_excelDown();">엑셀다운</a></li>
			<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();">선택 삭제</a></li>
			<li><a class="button modify_btn" href="javascript:fn_checkCompl();">선택 상담완료처리</a></li>
			<li><a class="button reply_btn" href="javascript:fn_checkCancel();">선택 상담취소처리</a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId">
   	<input type="hidden" name="userId">
   	<input type="hidden" name="cnslStatusCd">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
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
	function fn_detailView(sId, userId) {
		document.listForm.sId.value = sId;
		document.listForm.userId.value = userId;
		document.listForm.action = GV_PRESENT_PATH + "/view.do";
		document.listForm.submit();
	}
	
	//카테고리
	function fn_changeCategory(sCate){
		document.listForm.sCate.value = sCate;
		document.listForm.action = gf_getPathName();
		document.listForm.submit();
	}
	
	//선택 상담완료
	function fn_checkCompl(){
		document.listForm.cnslStatusCd.value = 'CNSL_STATUS_STEP3';
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
			var msg = confirm('해당상담을 완료처리 하시겠습니까?');
			if(msg == true){				
				document.listForm.checkedSId.value = checkedSId;
				document.listForm.action = GV_PRESENT_PATH + "/updateCnslStat.do";
				document.listForm.submit();
			}
		}
	}
	
	//선택 상담완료
	function fn_checkCancel(){
		document.listForm.cnslStatusCd.value = 'CNSL_STATUS_STEP9';
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
			var msg = confirm('해당상담을 취소처리 하시겠습니까?');
			if(msg == true){				
				document.listForm.checkedSId.value = checkedSId;
				document.listForm.action = GV_PRESENT_PATH + "/updateCnslStat.do";
				document.listForm.submit();
			}
		}
	}
	
	//엑셀다운
	function fn_excelDown(){
		document.listForm.action = "/sys/cnsl/excelDown.do";
		document.listForm.submit();
	}
	
</script>