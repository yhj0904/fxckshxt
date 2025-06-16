<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 게시글 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">

	<form:hidden path="cnslerId"/>

	<table class="detail_table">
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
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeTrack" value="Y" <c:if test="${cnslerVO.cnslTypeTrack eq 'Y' }">checked</c:if> /> 진로</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeJob" value="Y" <c:if test="${cnslerVO.cnslTypeJob eq 'Y' }">checked</c:if> /> 취업</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeLife" value="Y" <c:if test="${cnslerVO.cnslTypeLife eq 'Y' }">checked</c:if> /> 생활</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeResume" value="Y" <c:if test="${cnslerVO.cnslTypeResume eq 'Y' }">checked</c:if> /> 입사지원서</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeIntv" value="Y" <c:if test="${cnslerVO.cnslTypeIntv eq 'Y' }">checked</c:if> /> 모의면접</label>
			</td>
		</tr>
		<tr>
			<th>상담장소</th>
			<td><input type="text" name="cnslPlace" value="${cnslerVO.cnslPlace }" style="width: 400px;" /></td>
		</tr>
   		<tr>
   			<th>사용여부</th>
   			<td>
   				<label><input type="radio" name="useYn" value="Y" <c:if test="${cnslerVO.useYn eq 'Y'}" >checked</c:if> /> Y</label>
   				<label><input type="radio" name="useYn" value="N" <c:if test="${cnslerVO.useYn eq 'N'}" >checked</c:if> /> N</label>
			</td>
   		</tr>
	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a></li>
			<li><a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${cnslerVO.cnslerId}'/>">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		document.detailForm.action = GV_PRESENT_PATH + "/modifyAction.do";
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

	//취소
	function fn_detailView() {
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}	
	
</script>