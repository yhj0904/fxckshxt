<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 게시글 목록 화면
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
	<div class="prog-title">
		<h2><c:out value="${progVO.progNm }" escapeXml="false" /></h2>
	</div>
	<div class="prog-info-wr prog-if-box01">
		<ul>
			<!-- <li><a class="button">만족도 조사 다운로드</a></li> -->
			<li class="<c:out value="${progVO.progCareerCd }"/>"><span><c:out value="${progVO.progCareerCdNm }"/></span></li>
			<li class="<c:out value="${progVO.progMthCd }"/>"><span><c:out value="${progVO.progMthCdNm }"/></span></li>
			<li class="<c:out value="${progVO.progTypeCd }"/>"><span><c:out value="${progVO.progTypeCdNm }"/></span></li>
		</ul>
	</div>
</div>
<form:form id="listForm" name="listForm" method="post" autocomplete="off">
	<table class="list_table fixed">
		<colgroup>
			<c:if test='${loginInfo.statDvcd eq "SYS_ADM" }'>
				<col style="width: 3%;"/> <!-- CHECKBOX -->
			</c:if>
			<col style="width: 5%;"/> <!-- NO -->
			<col style=""/> <!-- 회원분류 -->
			<col style=""/> <!-- 이름 -->
			<col style="width: 5%;"/> <!-- 신청구분 -->
			<col style=""/> <!-- 휴대전화번호 -->
			<col style=""/> <!-- 이메일 -->
			<col style=""/> <!-- 소속학과 -->
			<col style=""/> <!-- 학번 -->
			<col style="width: 6%;"/> <!-- 성별 -->
			<col style=""/> <!-- 만족도 조사 여부 -->
			<col style=""/> <!-- 이수결과 -->
		</colgroup>
		<thead>
			<tr>
				<c:if test='${loginInfo.statDvcd eq "SYS_ADM" }'>
					<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
				</c:if>
				<th><spring:message code="board.no"/></th>
				<th>회원분류</th>
				<th>이름</th>
				<th>신청상태</th>
				<th>휴대전화번호</th>
				<th>이메일</th>
				<th>소속학과</th>
				<th>학번</th>
				<th>성별</th>
				<th>만족도 조사<br/>여부</th>
				<th>이수결과</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list }" varStatus="i">
				<tr>
					<c:choose>
						<c:when test='${(loginInfo.statDvcd eq "SYS_ADM") and (item.PROG_REQST_CD eq "PROG_REQST_010")}'>
							<td>
								<label><input type="checkbox" name="checkRow" value="<c:out value="${item.USER_ID }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>								
							</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>
					<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
					<td>
						<c:out value="${item.USER_TYPE_NM }"/>
					</td>
					<td>
						<a class="link" href="javascript:fn_detailView('<c:out value="${item.USER_ID }"/>');">
							<c:out value="${item.USER_NM }"/>
						</a>
					</td>
					<td>
						<c:out value="${item.PROG_REQST_CD_NM }"/>
					</td>
					<td>
						<c:out value="${item.MBPH_NO }"/>
					</td>
					<td>
						<c:out value="${item.EMAIL }"/>
					</td>
					<td>
						<c:out value="${item.DEPT_NM }"/>
					</td>
					<td>
						<c:out value="${item.STD_NO }"/>
					</td>
					<td>
						<c:out value="${item.SEX_CD_NM }"/>
					</td>
					<td>
						<c:out value="${item.SURVEY_YN_NM }"/>
						<c:if test="${item.SURVEY_YN eq 'Y' }">
							<ul style="margin-top: 8px;">
								<li><a class="button">만족도 조사 다운로드</a></li>
							</ul>
						</c:if>
					</td>
					<td>
						<c:out value="${item.COMPL_NM }"/>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr><td class="no_data" colspan="12"><spring:message code="board.noData" /></td></tr>
			</c:if>
		</tbody>
  	</table>
  	
  	<div class="btn_wrap">
		<ul>
			<c:if test="${!empty PROG_COMPL }">
				<c:forEach var="compl" items="${PROG_COMPL }" >
					<li>
						<a class="button modify_btn" href="javascript:fn_saveComplList('<c:out value="${compl.cd }"/>');"><c:out value="${compl.cdNm }"/> 처리</a>
					</li>
				</c:forEach>
			</c:if>
			<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_progList();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
  	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${progVO.progId }'/>">
   	<input type="hidden" name="userId">
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
			var msg = gf_confirm('<spring:message code="message.confirm.remove" />', function(){
				if(e == "Y"){				
					gf_startAjaxing();
					
					document.listForm.checkedSId.value = checkedSId;
					document.listForm.action = GV_PRESENT_PATH + "/checkRemoveAction.do";
					document.listForm.submit();
				}
			});
		}
	}

	//이수결과
	function fn_saveComplList(code) {
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
			var msg = gf_confirm('<spring:message code="message.confirm.modify" />', function(e){
				if(e == "Y"){
					gf_startAjaxing();
					//document.listForm.sId.value = sId;
					document.listForm.checkedSId.value = checkedSId;
					document.listForm.sCode.value = code;
					document.listForm.action = GV_PRESENT_PATH + "/checkComplAction.do";
					document.listForm.submit();
				}
			});
		}
	}
	
	//상세보기
	function fn_detailView(userId) {
		document.listForm.userId.value = userId;
		document.listForm.action = GV_PRESENT_PATH + "/view.do";
		document.listForm.submit();
	}
	
	//상세보기
	function fn_progList() {
		document.listForm.action = "/sys/prog/list.do";
		document.listForm.submit();
	}
	
</script>