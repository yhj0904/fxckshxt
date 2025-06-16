<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="kr.co.nanwe.dept.service.DeptService" %>
<%@ page import="kr.co.nanwe.dept.service.DeptVO" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>

<%
/**
 * @Class Name 	: view.jsp
 * @Description : 게시글 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<link rel="stylesheet" href="/css/cnsl/cnsl.css" />

<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="cnslerId"/>
	
	<table class="detail_table">
		<caption>게시글 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th>상담원 아이디</th>
			<td><c:out value="${cnslerVO.cnslerId }" /></td>
		</tr>
		<tr>
			<th>상담분야</th>
			<td>
				<label class="cnslLabel"><input type="checkbox" onClick="return false;" <c:if test="${cnslerVO.cnslTypeTrack eq 'Y' }">checked</c:if> /> 진로</label>
				<label class="cnslLabel"><input type="checkbox" onClick="return false;" <c:if test="${cnslerVO.cnslTypeJob eq 'Y' }">checked</c:if> /> 취업</label>
				<label class="cnslLabel"><input type="checkbox" onClick="return false;" <c:if test="${cnslerVO.cnslTypeLife eq 'Y' }">checked</c:if> /> 생활</label>
				<label class="cnslLabel"><input type="checkbox" onClick="return false;" <c:if test="${cnslerVO.cnslTypeResume eq 'Y' }">checked</c:if> /> 입사지원서</label>
				<label class="cnslLabel"><input type="checkbox" onClick="return false;" <c:if test="${cnslerVO.cnslTypeIntv eq 'Y' }">checked</c:if> /> 모의면접</label>
			</td>
		</tr>
		<tr>
			<th>상담장소</th>
			<td>
				<c:out value="${cnslerVO.cnslPlace }" />
			</td>
		</tr>
		<tr>
			<th>사용여부</th>
			<td><c:out value="${cnslerVO.useYn }" /></td>
		</tr>
		<tr>
			<th>담당 학부</th>
			<td>
				<c:if test="${empty colgList }">선택된 학부가 없습니다.</c:if>
				<c:forEach items="${colgList }" var="colg">
					<ul>
						<li>
							<c:out value="${colg.colgNmKor }" />
						</li>
					</ul>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th>담당 학과</th>
			<td>
				<c:if test="${empty deptList }">선택된 학부가 없습니다.</c:if>
				<c:forEach items="${deptList }" var="dept">
					<ul>
						<li>
							<c:out value="${dept.deptNmKor }" />
						</li>
					</ul>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th>입력아이디</th>
			<td><c:out value="${cnslerVO.inptId }" /></td>
		</tr>
		<tr>
			<th>입력일시</th>
			<td><c:out value="${cnslerVO.inptDttm }" /></td>
		</tr>
		<tr>
			<th>수정아이디</th>
			<td><c:out value="${cnslerVO.modiId }" /></td>
		</tr>
		<tr>
			<th>수정일시</th>
			<td><c:out value="${cnslerVO.modiDttm }" /></td>
		</tr>
  	</table>
  	
  	<div class="btn_wrap">
		<ul>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
			<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
			<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
			<li><a class="button register_btn" href="javascript:fn_selectDept();">상담학과</a></li>
			<li><a class="button register_btn" href="javascript:fn_cnslerSch('<c:out value="${cnslerVO.cnslerId }" />');">상담 월별 일정</a></li>
		</ul>
	</div>
	
	<!-- 값 전달부분 -->
	<input type="hidden" id="colg_values" name="colg_values" />
	<input type="hidden" id="dept_values" name="dept_values" />
	<!-- 값 전달부분 끝 -->
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${cnslerVO.cnslerId }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//상세보기
	function fn_detailView(sId) {
		document.detailForm.sId.value = sId;
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
		document.detailForm.submit();
	}
	
	//답변
	function fn_replyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/reply.do";
		document.detailForm.submit();
	}
	
	//수정
	function fn_modifyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/modify.do";
		document.detailForm.submit();
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}
	
	//학과 등록
	function fn_selectDept() {
		var url = "/sys/cnsler/selectDept.do?userId="+"<c:out value='${cnslerVO.cnslerId}' />";
		var width = 800;
		var height = 900;
		var popupX = (window.screen.width / 2) - (width / 2);
		var popupY = (window.screen.width / 2) - (height / 2);
		window.open(url, '', 'status=no, width='+width+', height='+height+', left='+popupX+', top='+popupY+', screenX='+popupX+', screenY='+popupY+', scrollbars=yes')
	}
	
	//상담원 일정보기
	function fn_cnslerSch(sId) {
		document.detailForm.sId.value = sId;
		document.detailForm.action = GV_PRESENT_PATH + "/cnslerSch.do";
		document.detailForm.submit();
	}
</script>