<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 시스템 로그 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form commandName="sysLogVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	<table class="detail_table">
		<caption>시스템 로그 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="sysLogVO.sysCode" /></th>
			<td>
				<c:out value="${sysLogVO.sysCode}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.programCd" /></th>
			<td>
				<c:out value="${sysLogVO.programCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.programNm" /></th>
			<td>
				<c:out value="${sysLogVO.programNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.infoCd" /></th>
			<td>
				<c:out value="${sysLogVO.infoCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.infoNm" /></th>
			<td>
				<c:out value="${sysLogVO.infoNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.programUri" /></th>
			<td>
				<c:out value="${sysLogVO.programUri}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.className" /></th>
			<td>
				<c:out value="${sysLogVO.className}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.methodName" /></th>
			<td>
				<c:out value="${sysLogVO.methodName}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.methodDesc" /></th>
			<td>
				<c:out value="${sysLogVO.methodDesc}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.processCode" /></th>
			<td>
				<c:out value="${sysLogVO.processCode}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.processTime" /></th>
			<td>
				<c:out value="${sysLogVO.processTime}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logParam" /></th>
			<td>
				<pre><c:out value="${sysLogVO.logParam}" /></pre>
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logId" /></th>
			<td>
				<c:out value="${sysLogVO.logId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logName" /></th>
			<td>
				<c:out value="${sysLogVO.logName}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logIp" /></th>
			<td>
				<c:out value="${sysLogVO.logIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logOs" /></th>
			<td>
				<c:out value="${sysLogVO.logOs}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logDevice" /></th>
			<td>
				<c:out value="${sysLogVO.logDevice}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logBrowser" /></th>
			<td>
				<c:out value="${sysLogVO.logBrowser}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logUrl" /></th>
			<td>
				<c:out value="${sysLogVO.logUrl}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.logDttm" /></th>
			<td>
				<c:out value="${sysLogVO.logDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.errYn" /></th>
			<td>
				<c:out value="${sysLogVO.errYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.privacyYn" /></th>
			<td>
				<c:out value="${sysLogVO.privacyYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="sysLogVO.errMsg" /></th>
			<td>
				<pre><c:out value="${sysLogVO.errMsg}" /></pre>
			</td>
		</tr>
  	</table>
  	
  	<table class="list_table">
		<caption>DB 로그 목록</caption>
		<thead>
			<tr>
				<th><spring:message code="dbLogVO.sqlId" /></th>
				<th><spring:message code="dbLogVO.sqlMethod" /></th>
				<th><spring:message code="dbLogVO.logSql" /></th>
				<th><spring:message code="dbLogVO.logDttm" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${dbLogList }" varStatus="i">
				<tr>
					<td><c:out value='${item.sqlId }' /></td>
					<td><c:out value='${item.sqlMethod }' /></td>
					<td class="tl"><pre><c:out value="${item.logSql}" /></pre></td>
					<td><c:out value='${item.logDttm }' /></td>
				</tr>
			</c:forEach>
			<c:if test="${empty dbLogList}">
				<tr>
					<td colspan="4"><spring:message code="board.noData" /></td>
				</tr>
			</c:if>
		</tbody>
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
  	<input type="hidden" name="sId" value="<c:out value='${sysLogVO.sysCode }'/>">	
  	<input type="hidden" name="searchProgram" value="<c:out value='${searchProgram }'/>">
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