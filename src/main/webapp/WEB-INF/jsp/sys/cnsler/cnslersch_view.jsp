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

<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<input type="hidden" name="cnslerId" value="${cnslerVO.cnslerId }" />
	<input type="hidden" name="schDt" value="${cnslerVO.schDt }" />
	<input type="hidden" name="sId" value="${cnslerVO.cnslerId }" />
	
	<table class="detail_table">
		<caption>상담일정 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th>상담원</th>
			<td><c:out value="${userVO.userNm }" /></td>
		</tr>
		<tr>
			<th>상담원 아이디</th>
			<td><c:out value="${cnslerVO.cnslerId }" /></td>
		</tr>
		<tr>
			<th>상담일</th>
			<td><c:out value="${cnslerVO.schDt }" /></td>
		</tr>
		<tr>
			<th>상담시간</th>
			<td>
				<c:choose>
					<c:when test="${cnslerVO.cnslAmYn eq 'Y' && cnslerVO.cnslPmYn eq 'Y' }">종일</c:when>
					<c:when test="${cnslerVO.cnslAmYn eq 'Y'}">오전</c:when>
					<c:when test="${cnslerVO.cnslPmYn eq 'Y'}">오후</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th>입력ID</th>
			<td><c:out value="${cnslerVO.inptId }" /></td>
		</tr>
		<tr>
			<th>입력IP</th>
			<td><c:out value="${cnslerVO.inptIp }" /></td>
		</tr>
		<tr>
			<th>입력일</th>
			<td><c:out value="${cnslerVO.inptDttm }" /></td>
		</tr>
		<c:if test="${cnslerVO.modiId ne null }">
			<tr>
				<th>수정ID</th>
				<td><c:out value="${cnslerVO.modiId }" /></td>
			</tr>
			<tr>
				<th>수정IP</th>
				<td><c:out value="${cnslerVO.modiIp }" /></td>
			</tr>
			<tr>
				<th>수정일</th>
				<td><c:out value="${cnslerVO.modiDttm }" /></td>
			</tr>
		</c:if>
   	</table>
   	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
</form:form>

<div class="btn_wrap">
	<ul>
		<li><a class="button modify_btn" href="javascript:fn_modifyView();">일정<spring:message code="button.modify" /></a></li>
		<li><a class="button remove_btn" href="javascript:fn_removeView();">일정<spring:message code="button.remove" /></a></li>
		<li><a class="button list_btn" href="javascript:fn_listView('<c:out value='${cnslerVO.cnslerId }' />');">달력</a></li>
	</ul>
</div>

<script>
//목록
function fn_listView(sId) {
	document.detailForm.sId.value = sId;
	document.detailForm.action = GV_PRESENT_PATH + "/cnslerSch.do";
	document.detailForm.submit();
}

//수정
function fn_modifyView() {
	document.detailForm.action = GV_PRESENT_PATH + "/cnslerSch_modify.do";
	document.detailForm.submit();
}

//삭제
function fn_removeView() {
	
	var msg = gf_confirm('<spring:message code="message.confirm.remove" />', function(e){
		if(e == "Y"){				
			document.detailForm.action = GV_PRESENT_PATH + "/cnslerSchRemoveAction.do";
			document.detailForm.submit();
		}
	});
	
}
</script>