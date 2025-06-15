<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: year.jsp
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
		<p><c:out value='${calendar.sYear }'/>년</p>
		<a class="cal_btn prev" href="javascript:fn_changeCalendar('PREV')"><span class="none">이전</span></a>
		<a class="cal_btn next" href="javascript:fn_changeCalendar('NEXT')"><span class="none">다음</span></a>		
	</div>
	
	<table class="detail_table">
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<c:forEach var="cal" items="${calendar.list }">
			<tr>
				<th><c:out value='${cal.month }'/>월</th>
				<td>
					<ul>
						<c:forEach var="item" items="${cal.schList }" varStatus="i">
							<li>
								<a class="link" href="javascript:fn_detailView('<c:out value="${item.schId }"/>');" title="<spring:message code="board.detail"/>">
									<c:if test='${schMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
										<strong>[<c:out value="${item.category}"/>]</strong>
									</c:if>
									<c:out value="${item.title}"/>
									<c:out value="${item.startMonth}"/>월 <c:out value="${item.startDay}"/>일
									<c:if test='${item.endDttm ne null and item.endDttm ne ""}'>~ <c:out value="${item.endMonth}"/>월 <c:out value="${item.endDay}"/>일</c:if>
								</a>
							</li>
						</c:forEach>
					</ul>
				</td>
			</tr>
		</c:forEach>
  	</table>
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