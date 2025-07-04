<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 나의 수강 내역  화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.18		신한나			최초생성
 */
%>
<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>

<% //게시글 유형 %>
<form:form id="listForm" name="listForm" method="post" autocomplete="off">

	<!-- 나의 프로그램 신청목록 -->
	<section id="program" class="board_cont" >
		<!-- <h2 class="mypage_title">나의 프로그램 신청 목록</h2> -->
		
		<form:form id="listForm" name="listForm" method="post" autocomplete="off">
			<div class="scroll">
			<table class="list_table2">
			
				<colgroup>
					<col style="width: 5%;"/> <!-- CHECKBOX -->
					<col style="width: 6%;"/> <!-- NO -->
					<col style="width: 5%;"/> <!-- 상태 -->
					<col style="width: auto;"/> <!-- 프로그램 명 -->
					<col style="width: 12%;"/> <!-- 교육방법 -->
					<col style="width: 20%;"/> <!-- 교육일 -->
					<col style="width: 13%;"/> <!-- 커리어단계 -->
					<col style="width: 10%;"/> <!-- 만족도조사 -->
					<col style="width: 10%;"/> <!-- 이수현황 -->
				</colgroup>
				<thead>
					<tr>
						<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
						<th>번호</th>
						<th>상태</th>
						<th>프로그램 명</th>
						<th>교육방법</th>
						<th>교육일</th>
						<th>커리어<br/>단계</th>
						<th>만족도조사</th>
						<th>이수<br>현황</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="myprog" items="${list }" varStatus="i">
						<tr <c:if test='${(myprog.PROG_REQST_CD eq "PROG_REQST_090")}'>class="prog_cancel"</c:if>>
							<td>
								<c:choose>
									<c:when test='${(myprog.PROG_REQST_CD eq "PROG_REQST_010") and (myprog.USER_SURVEY_YN eq "N") and (myprog.COMPL_CD eq "PROG_COMPL_020")}'>
											<label><input type="checkbox" name="checkRow" value="<c:out value="${myprog.PROG_ID}"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
									</c:when>
									<c:when test='${(myprog.PROG_REQST_CD eq "PROG_REQST_090")}'>
										<span style="color:#da0000;">신청<br/>취소</span>
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
							<td>
								<c:out value="${myprog.STATUS_NM }" escapeXml="false" />
							</td>
							<td class="subj">
								<a href="/prog/view.do?sId=<c:out value='${myprog.PROG_ID }'/>" class="link">
									<c:out value="${myprog.PROG_NM }" escapeXml="false" />
								</a>
							</td>
							<td class="subj">
								<c:out value="${myprog.PROG_MTH_CD_NM }" />
								<c:if test="${!empty myprog.PROG_CONTACT_CD_NM}">
									(<c:out value="${myprog.PROG_CONTACT_CD_NM}"/>)
								</c:if>
							</td>
							<td><c:out value="${myprog.PROG_SDT} ~ ${myprog.PROG_EDT }" /></td>
							<td><c:out value="${myprog.PROG_CAREER_CD_NM }" /></td>
							<td>
								<c:choose>
									<c:when test="${myprog.USER_SURVEY_YN eq 'Y' }">
										 완료
									</c:when>
									<c:when test="${(myprog.USER_SURVEY_YN eq 'N') and (myprog.SURVEY_DT_CD eq 2) and (myprog.PROG_REQST_CD ne 'PROG_COMPL_090')}">
										<a class="button" href="javascript:fn_progSurv('<c:out value="${myprog.PROG_ID }"/>');">설문하기</a>
									</c:when>
									<c:otherwise>
									
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:out value="${myprog.COMPL_NM }" /></td>
						</tr>
					</c:forEach>
					<c:if test="${empty list }">
						<td colspan="8"><spring:message code="board.noData"/></td>
					</c:if>
				</tbody>
			</table>
			</div>
		</form:form>
		
		
		<div class="btn_wrap">
			<ul>
				<li><a href="javascript:fn_progApplCancel();" class="button cancel_btn">신청 취소</a></li>
			</ul>
		</div>	
	</section>
	
	<% /** 페이징 */ %>
	<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
	<% /** //페이징 */ %>
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>		  
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
   	<input type="hidden" name="checkedSId">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>
</form:form>
<script type="text/javaScript" defer="defer">
	
	//설문하기
	function fn_progSurv(progId){
		var url = "/prog_surv/view.do?progId="+progId;
		var width = 800;
		var height = 900;
		var popupX = (window.screen.width / 2) - (width / 2);
		var popupY = (window.screen.width / 2) - (height / 2);
		window.open(url, '', 'status=no, width='+width+', height='+height+', left='+popupX+', top='+popupY+', screenX='+popupX+', screenY='+popupY+', scrollbars=yes')
	}

	//상세보기
	function fn_detailView(sId) {
		document.listForm.sId.value = sId;
		document.listForm.action = GV_PRESENT_PATH  + "/view.do";
		document.listForm.submit();
	}
	
	//신청취소
	function fn_progApplCancel(){
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
				console.log('checkedSId >>>>>>>>> ' + checkedSId);
				chkCnt++;
			}
		}
		console.log(rowArr);
		console.log(checkedSId);
		console.log(chkCnt);
		if(chkCnt <= 0) {
			gf_alert('<spring:message code="message.alert.nocheck" />');
		} else {
			
			gf_confirm("신청취소를 하시겠습니까?", function(e){
				if(e == "Y"){
					$.ajax({
						url : GV_PRESENT_PATH + "/cancel.do",
						data : {
							progReqstCd : 'PROG_REQST_090',
							checkedSId : checkedSId
						},
						type : 'POST',
						beforeSend : function() {
							gf_startAjaxing();
						},
						success : function(response) {
							gf_endAjaxing();
							gf_alert("총 " + gf_nvl(response, 0) + "건이 취소되었습니다.", function(){
								location.reload();
							});
						},
						error : function(response) {
							gf_endAjaxing();
						}
					});
				}
			})
			
		}
	}
	
	//Row 선택
	function fn_checkRow() {
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkRow");
		var rowCnt = rowArr.length;
		for (var i = 0; i < rowArr.length; i++) {
			if (rowArr[i].type == "checkbox" && rowArr[i].checked) chkCnt++;
		}
		if(!gf_isNull(document.getElementById("checkAllRow"))){
			if (chkCnt == rowCnt) {
				document.getElementById("checkAllRow").checked = true;
			} else {
				document.getElementById("checkAllRow").checked = false;
			}
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
</script>

<style>
td.subj {
  max-width: 200px; /* 최대 너비 설정, 필요에 따라 조절 */
  white-space: nowrap; /* 줄바꿈 없이 한 줄로 표시 */
  overflow: hidden; /* 내용이 넘치면 숨김 처리 */
  text-overflow: ellipsis; /* 넘친 텍스트를 말줄임표로 표시 */
}
</style>
