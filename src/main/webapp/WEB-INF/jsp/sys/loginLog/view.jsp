<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 로그인 로그 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	<table class="detail_table">
		<caption>로그인 로그 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="loginLogVO.loginCode" /></th>
			<td>
				<c:out value="${loginLogVO.loginCode}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginType" /></th>
			<td>
				<c:out value="${loginLogVO.loginType}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginId" /></th>
			<td>
				<c:out value="${loginLogVO.loginId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginNm" /></th>
			<td>
				<c:out value="${loginLogVO.loginNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginIp" /></th>
			<td>
				<c:out value="${loginLogVO.loginIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginOs" /></th>
			<td>
				<c:out value="${loginLogVO.loginOs}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginDevice" /></th>
			<td>
				<c:out value="${loginLogVO.loginDevice}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginBrowser" /></th>
			<td>
				<c:out value="${loginLogVO.loginBrowser}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginDttm" /></th>
			<td>
				<c:out value="${loginLogVO.loginDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginSuccess" /></th>
			<td>
				<c:out value="${loginLogVO.loginSuccess}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginErrMsg" /></th>
			<td>
				<c:out value="${loginLogVO.loginErrMsg}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="loginLogVO.loginUrl" /></th>
			<td>
				<c:out value="${loginLogVO.loginUrl}" />
			</td>
		</tr>
	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${loginLogVO.loginCode }'/>">
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
		document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
		document.detailForm.submit();
	}

	//상세보기
	function fn_desIdlView(sId) {
		document.detailForm.sIdsIdlue = sId;
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
</script>