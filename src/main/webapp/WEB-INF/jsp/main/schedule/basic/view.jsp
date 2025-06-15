<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 일정 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<table class="detail_table">
	<caption>일정 상세조회</caption>
	<colgroup>
  			<col width="150"/>
  			<col width="?"/>
  		</colgroup>
	<%-- <tr>
		<th><spring:message code="schVO.schCd" /></th>
		<td><c:out value="${schVO.schCd}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.category" /></th>
		<td><c:out value="${schVO.category}" /></td>
	</tr> --%>
	<tr>
		<th><spring:message code="schVO.title" /></th>
		<td><c:out value="${schVO.title}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.contents" /></th>
		<td><c:out value="${schVO.contents}" escapeXml="false"/></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.period" /></th>
		<td><c:out value="${schVO.startDttm}"/><c:if test='${schVO.endDttm ne null and schVO.endDttm ne ""}'>~<c:out value="${schVO.endDttm}"/></c:if></td>
	</tr>
	<c:if test='${schMgtVO.supplYn eq "Y" }'>
		<c:if test='${schMgtVO.suppl01Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl01Title}" /></th>
				<td><c:out value="${schVO.schInfo01}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl02Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl02Title}" /></th>
				<td><c:out value="${schVO.schInfo02}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl03Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl03Title}" /></th>
				<td><c:out value="${schVO.schInfo03}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl04Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl04Title}" /></th>
				<td><c:out value="${schVO.schInfo04}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl05Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl05Title}" /></th>
				<td><c:out value="${schVO.schInfo05}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl06Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl06Title}" /></th>
				<td><c:out value="${schVO.schInfo06}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl07Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl07Title}" /></th>
				<td><c:out value="${schVO.schInfo07}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl08Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl08Title}" /></th>
				<td><c:out value="${schVO.schInfo08}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl09Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl09Title}" /></th>
				<td><c:out value="${schVO.schInfo09}" /></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl10Yn eq "Y" }'>
			<tr>
				<th><c:out value="${schMgtVO.suppl10Title}" /></th>
				<td><c:out value="${schVO.schInfo10}" /></td>
			</tr>
		</c:if>
	</c:if>
	<tr>
		<th><spring:message code="schVO.writer" /></th>
		<td><c:out value="${schVO.writer}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.viewCnt" /></th>
		<td><c:out value="${schVO.viewCnt}" /></td>
	</tr>
	<c:if test="${!empty schVO.viewFiles }">
		<tr>
			<th><spring:message code="text.uploadfile" /></th>
			<td>
				<ul class="file_list">
					<c:forEach var="viewFile" items="${schVO.viewFiles }">
						<li>
							<a href="javascript:gf_download('<c:out value="${viewFile.oname }"/>','<c:out value="${viewFile.fpath }"/>','<c:out value="${viewFile.fname }"/>', <c:out value="${viewFile.fno }"/>);" title="<spring:message code="button.download" />">
								<c:out value="${viewFile.oname }"/>
							</a>
						</li>
					</c:forEach>
				</ul>
			</td>
		</tr>
	</c:if>
	<%-- <tr>
		<th><spring:message code="schVO.inptId" /></th>
		<td><c:out value="${schVO.inptId}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.inptIp" /></th>
		<td><c:out value="${schVO.inptIp}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.inptDttm" /></th>
		<td><c:out value="${schVO.inptDttm}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.modiId" /></th>
		<td><c:out value="${schVO.modiId}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.modiIp" /></th>
		<td><c:out value="${schVO.modiIp}" /></td>
	</tr>
	<tr>
		<th><spring:message code="schVO.modiDttm" /></th>
		<td><c:out value="${schVO.modiDttm}" /></td>
	</tr> --%>		
</table>  	
<div class="btn_wrap">
	<ul>
		<c:if test='${schMgtVO.adminUser or schMgtVO.regiAuthYn}'>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
		</c:if>
		<c:if test='${schMgtVO.adminUser or schVO.inptId eq LOGIN_USER.loginId}'>
			<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
		</c:if>
		<c:if test='${schMgtVO.adminUser or schVO.inptId eq LOGIN_USER.loginId}'>
			<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li>
		</c:if>
		<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>