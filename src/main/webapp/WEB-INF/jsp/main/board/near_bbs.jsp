<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: near_bbs.jsp
 * @Description : 인접글
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<table>
	<colgroup>
  		<col width="?">
  		<col width="?">
  	</colgroup>
	<tr>
		<th scope="row"><spring:message code="board.next" /></th>
		<c:choose>
			<c:when test='${nearBbs.nextBbs ne null}'>
				<td class="title">
					<a href="javascript:fn_detailView('<c:out value="${nearBbs.nextBbs.bbsId }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${nearBbs.nextBbs.title}"/></a>
				</td>
				<td class="writer">
					<c:choose>
						<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
							<spring:message code="board.noname" />
						</c:when>
						<c:otherwise>
							<c:out value="${nearBbs.nextBbs.writer}"/>
						</c:otherwise>
					</c:choose>
				</td>
			</c:when>
			<c:otherwise>
				<td colspan="2"><spring:message code="board.nonext" /></td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<th scope="row"><spring:message code="board.prev" /></th>
		<c:choose>
			<c:when test='${nearBbs.prevBbs ne null}'>
				<td class="title">
					<a href="javascript:fn_detailView('<c:out value="${nearBbs.prevBbs.bbsId }"/>');" title="<spring:message code="board.detail"/>"><c:out value="${nearBbs.prevBbs.title}"/></a>
				</td>
				<td class="writer">
					<c:choose>
						<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
							<spring:message code="board.noname" />
						</c:when>
						<c:otherwise>
							<c:out value="${nearBbs.prevBbs.writer}"/>
						</c:otherwise>
					</c:choose>
				</td>
			</c:when>
			<c:otherwise>
				<td colspan="2"><spring:message code="board.noprev" /></td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>