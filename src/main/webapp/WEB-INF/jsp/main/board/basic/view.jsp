<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<table class="detail_table">
	<caption>게시글 상세조회</caption>
	<colgroup>
  		<col width="150"/>
  		<col width="?"/>
  	</colgroup>
	<tr>
		<th><spring:message code="bbsVO.title" /></th>
		<td><c:out value="${bbsVO.title}" escapeXml="false"/></td>
	</tr>
	<tr>
		<th><spring:message code="bbsVO.writer" /></th>
		<td>
			<c:choose>
				<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
					<spring:message code="board.noname" />
				</c:when>
				<c:otherwise>
					<c:out value="${bbsVO.writer}" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<th><spring:message code="bbsVO.contents" /></th>
		<td class="content-container"><c:out value="${bbsVO.contents}" escapeXml="false"/></td>
	</tr>
	<c:if test='${bbsMgtVO.supplYn eq "Y" }'>
		<c:if test='${bbsMgtVO.suppl01Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl01Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo01}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl02Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl02Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo02}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl03Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl03Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo03}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl04Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl04Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo04}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl05Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl05Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo05}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl06Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl06Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo06}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl07Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl07Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo07}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl08Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl08Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo08}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl09Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl09Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo09}" /></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl10Yn eq "Y" }'>
			<tr>
				<th><c:out value="${bbsMgtVO.suppl10Title}" /></th>
				<td><c:out value="${bbsVO.bbsInfo10}" /></td>
			</tr>
		</c:if>
	</c:if>
	<c:if test="${!empty bbsVO.viewFiles }">
		<tr>
			<th><spring:message code="text.uploadfile" /></th>
			<td>
				<ul class="file_list">
					<c:forEach var="viewFile" items="${bbsVO.viewFiles }">
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
	<tr>
		<th><spring:message code="bbsVO.inptDttm" /></th>
		<td><c:out value="${bbsVO.inptDttm}"/></td>
	</tr>
</table>
<c:if test='${bbsMgtVO.cmntYn eq "Y" }'>
	<% /* 댓글 사용시 import */%>
	<c:import url="/WEB-INF/jsp/main/board/comment.jsp"></c:import>		
</c:if>
  	
<div class="btn_wrap">
	<ul>
		<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.regiAuthYn}'>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser or bbsVO.inptId eq LOGIN_USER.loginId}'>
			<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.replyAuthYn}'>
			<li><a class="button reply_btn" href="javascript:fn_replyView();"><spring:message code="button.reply" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser or bbsVO.inptId eq LOGIN_USER.loginId}'>
			<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li>
		</c:if>
		<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>

<style>
    .content-container {
        max-height: 500px; /* 최대 높이 설정 */
        overflow-y: auto; /* 세로 방향으로 내용이 넘칠 경우 스크롤바 생성 */
        overflow-x: hidden; /* 가로 방향 스크롤바는 숨김 */
    }
</style>