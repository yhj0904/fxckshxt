<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 사용자 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */
%>
<link href="/css/common/temp/common.css" rel="stylesheet" />
<link href="/css/template/basic/mypage.css" rel="stylesheet" />

<form:form commandName="userVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="userId"/>
	<div class="scroll">
	<table class="detail_table mypage_table">
		<caption>사용자 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="userVO.userId" /></th>
			<td>
				<c:out value="${userVO.userId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.userNm" /></th>
			<td>
				<c:out value="${userVO.userNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.mbphNo" /></th>
			<td>
				<c:out value="${userVO.mbphNo}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.email" /></th>
			<td>
				<c:out value="${userVO.email}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.sex" /></th>
			<td>
				<c:out value="${userVO.sexNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.userTypeCd" /></th>
			<td>
				<c:out value="${userVO.userTypeCdNm}" />
			</td>
		</tr>
		<c:if test="${(userVO.userTypeCd eq 'USER_TYPE_010') or (userVO.userTypeCd eq 'USER_TYPE_020') }">
			<tr>
				<th><spring:message code="userVO.userTypeDetCd" /></th>
				<td>
					<c:out value="${userVO.userTypeDetCdNm}" />
				</td>
			</tr>
			<tr>
				<th><spring:message code="userVO.colgCd" /></th>
				<td>
					<c:out value="${userVO.colgNm}" /> / <c:out value="${userVO.deptNm}" escapeXml="false" /> 
				</td>
			</tr>
			<tr>
				<th><spring:message code="joinVO.stdNo" />/<spring:message code="joinVO.grade" /></th>
				<td>
					<c:out value="${userVO.stdNo}" /> / <c:out value="${userVO.grade}" /> 
				</td>
			</tr>
		</c:if>
		<c:if test="${(userVO.userTypeCd eq 'USER_TYPE_030')}">
			<tr>
				<th><spring:message code="userVO.local" /></th>
				<td>
					<c:out value="${userVO.localNm}" /> 
				</td>
			</tr>
		</c:if>
	</table>
	</div>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
	
</form:form>

<script type="text/javaScript" defer="defer">

	//수정
	function fn_modifyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/modify.do";
		document.detailForm.submit();
	}
	
</script>