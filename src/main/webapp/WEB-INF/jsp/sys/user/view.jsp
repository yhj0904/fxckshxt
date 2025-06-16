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
<form:form modelAttribute
	
	<form:hidden path="userId"/>
	
	<table class="detail_table">
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
			<th><spring:message code="userVO.authCd" /></th>
			<td>
				<c:out value="${userVO.authCd}" />
			</td>
		</tr>
		<%-- <tr>
			<th><spring:message code="userVO.useSttDttm" /></th>
			<td>
				<c:out value="${userVO.useSttDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.useEndDttm" /></th>
			<td>
				<c:out value="${userVO.useEndDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.deptNm" /></th>
			<td>
				<c:out value="${userVO.deptNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.birth" /></th>
			<td>
				<c:out value="${userVO.birth}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.postNo" /></th>
			<td>
				<c:out value="${userVO.postNo}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.addr" /></th>
			<td>
				<c:out value="${userVO.addr}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.detlAddr" /></th>
			<td>
				<c:out value="${userVO.detlAddr}" />
			</td>
		</tr> --%>
		<tr>
			<th><spring:message code="userVO.mbphNo" /></th>
			<td>
				<c:out value="${userVO.mbphNo}" />
			</td>
		</tr>
		<%-- <tr>
			<th><spring:message code="userVO.telNo" /></th>
			<td>
				<c:out value="${userVO.telNo}" />
			</td>
		</tr> --%>
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
		<tr>
			<th><spring:message code="userVO.useYn" /></th>
			<td>
				<c:out value="${userVO.useYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.note" /></th>
			<td>
				<c:out value="${userVO.note}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.inptId" /></th>
			<td>
				<c:out value="${userVO.inptId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.inptIp" /></th>
			<td>
				<c:out value="${userVO.inptIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.inptDttm" /></th>
			<td>
				<c:out value="${userVO.inptDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.modiId" /></th>
			<td>
				<c:out value="${userVO.modiId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.modiIp" /></th>
			<td>
				<c:out value="${userVO.modiIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="userVO.modiDttm" /></th>
			<td>
				<c:out value="${userVO.modiDttm}" />
			</td>
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
  	<input type="hidden" name="sId" value="<c:out value='${userVO.userId }'/>">
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