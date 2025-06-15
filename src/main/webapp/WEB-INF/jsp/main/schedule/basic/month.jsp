<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: month.jsp
 * @Description : 일정 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>			
<div class="calendar">
	<div class="cal_tit">			
		<p><c:out value='${calendar.sYear }'/>년 <c:out value='${calendar.sMonth }'/>월</p>
		<a class="cal_btn prev" href="javascript:fn_changeCalendar('PREV')"><span class="none">이전</span></a>
		<a class="cal_btn next" href="javascript:fn_changeCalendar('NEXT')"><span class="none">다음</span></a>
	</div>
	<div class="cal_cont">
		<table>
			<thead>
				<tr>
					<th><span class="sun">일</span></th>
					<th><span>월</span></th>
					<th><span>화</span></th>
					<th><span>수</span></th>
					<th><span>목</span></th>
					<th><span>금</span></th>
					<th><span class="sat">토</span></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="cal" items="${calendar.list }" varStatus="i">
					<c:if test='${cal.dayNum eq 1}'>
						<tr>
					</c:if>
					<td>
						<span class="<c:out value='${cal.background }'/>"><c:out value='${cal.day }'/></span>
						<ul>
							<c:forEach var="item" items="${cal.schList }" varStatus="i">
								<li>
									<c:choose>
										<c:when test="${item.schCd eq 'PROGRAM' }">
											<a class="link" href="javascript:fn_progDetailView('<c:out value="${item.schId }"/>');" title="<spring:message code="board.detail"/>">
										</c:when>
										<c:otherwise>
											<a class="link" href="javascript:fn_detailView('<c:out value="${item.schId }"/>');" title="<spring:message code="board.detail"/>">
										</c:otherwise>
									</c:choose>
										<c:if test='${schMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
											<strong>[<c:out value="${item.category}"/>]</strong>
										</c:if>
										<c:out value="${item.title}"/>
									</a>
								</li>
							</c:forEach>
						</ul>
					</td>
					<c:if test='${cal.dayNum eq 7}'>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>		
	</div>
</div>  	
<div class="btn_wrap">
	<ul>
		<c:if test='${schMgtVO.adminUser or schMgtVO.regiAuthYn}'>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
		</c:if>
		<c:if test='${schMgtVO.adminUser}'>
			<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
		</c:if>
	</ul>
</div>