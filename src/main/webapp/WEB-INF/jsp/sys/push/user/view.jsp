<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 앱사용자관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="userId"/>
	
	<table class="detail_table">
		<caption>앱사용자관리 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="pushAppUserVO.userId"/></th>
			<td><c:out value="${pushAppUserVO.userId}"/></td>
		</tr>		
		<tr>
			<th><spring:message code="pushAppUserVO.deptCd"/></th>
			<td><c:out value="${pushAppUserVO.deptCd}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.userNm"/></th>
			<td><c:out value="${pushAppUserVO.userNm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.userDv"/></th>
			<td><c:out value="${pushAppUserVO.userDv}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.adminDv"/></th>
			<td><c:out value="${pushAppUserVO.adminDv}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.deviceDv"/></th>
			<td><c:out value="${pushAppUserVO.deviceDv}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.deviceId"/></th>
			<td><c:out value="${pushAppUserVO.deviceId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.userMobile"/></th>
			<td><c:out value="${pushAppUserVO.userMobile}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.note"/></th>
			<td><c:out value="${pushAppUserVO.note}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.inptId"/></th>
			<td><c:out value="${pushAppUserVO.inptId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.inptIp"/></th>
			<td><c:out value="${pushAppUserVO.inptIp}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.inptDttm"/></th>
			<td><c:out value="${pushAppUserVO.inptDttm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.inptProg"/></th>
			<td><c:out value="${pushAppUserVO.inptProg}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.modiId"/></th>
			<td><c:out value="${pushAppUserVO.modiId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.modiIp"/></th>
			<td><c:out value="${pushAppUserVO.modiIp}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.modiDttm"/></th>
			<td><c:out value="${pushAppUserVO.modiDttm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="pushAppUserVO.modiProg"/></th>
			<td><c:out value="${pushAppUserVO.modiProg}"/></td>
		</tr>
	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${pushAppUserVO.userId }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
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
</script>