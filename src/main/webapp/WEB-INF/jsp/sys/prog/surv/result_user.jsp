<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: result_user.jsp
 * @Description : 설문 결과 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form id="detailForm" name="detailForm" method="post" autocomplete="off">
	<ul class="sub_ul">
		<li>
			<div class="sub_cont">
				<table class="detail_table">
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><spring:message code="survMgtVO.survId"/></th>
						<td><c:out value="${progVO.progId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.survTitle"/></th>
						<td><c:out value="${progVO.progNm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.survMemo"/></th>
						<td><c:out value="${progVO.progCareerCdNm}" escapeXml="false"/></td>
					</tr>
			  	</table>
			</div>
		</li>
		
		<li>
			<div class="sub_tit"><spring:message code="survMgtVO.message.survResult"/></div>
			<div class="sub_cont">
				<div class="survey_wrap">
					<ul class="survey_list">
						<c:forEach var="question" items="${progSurvMgtVO.quesList }" varStatus="i">
							<li>
								<div class="survey_ques">
									<span class="num"><c:out value='${i.count }'/>&#46;</span>
									<p class="tit<c:if test='${question.ncsryYn eq "Y" }'> required</c:if>"><c:out value="${question.quesTitle}"/></p>
									<span class="point">(<c:out value="${question.avgPoint}"/>)</span>
									<div class="memo"><pre><c:out value="${question.quesMemo}" escapeXml="false"/></pre></div>
								</div>
								<div class="survey_item">
									<c:choose>
										<c:when test='${question.quesType eq "ETC" }'>
											<c:forEach var="answ" items="${question.answList }" varStatus="j">
												<textarea class="item_etc" disabled><c:out value="${answ.answContent}"/></textarea>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<ul>
												<c:forEach var="answ" items="${question.answList }" varStatus="j">
													<li>
														<c:choose>
															<c:when test='${question.quesType eq "RADIO" }'>
																<label class="radio">
																	<input type="radio" class="question_item" disabled <c:if test='${answ.inptId ne null and answ.inptId ne "" }'>checked</c:if>><i></i>
																	<c:out value="${answ.itemTitle}"/>(<c:out value="${answ.itemPoint}"/>)
																	<c:if test='${answ.itemEtc eq "Y"}'>
																		<input type="text" class="item_etc" value="<c:out value="${answ.answContent}"/>" disabled/>
																	</c:if>
																</label>
															</c:when>
															<c:when test='${question.quesType eq "CHECK" }'>
																<label class="check">
																	<input type="checkbox" class="question_item" disabled <c:if test='${answ.inptId ne null and answ.inptId ne "" }'>checked</c:if>><i></i>
																	<c:out value="${answ.itemTitle}"/>(<c:out value="${answ.itemPoint}"/>)
																	<c:if test='${answ.itemEtc eq "Y"}'>
																		<input type="text" class="item_etc" value="<c:out value="${answ.answContent}"/>" disabled/>
																	</c:if>
																</label>
															</c:when>
														</c:choose>
													</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
								</div>
							</li>				
						</c:forEach>
					</ul>
				</div>
			</div>
		</li>
	</ul>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button cancel_btn" href="javascript:fn_resultView();"><spring:message code="button.cancel" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${progVO.progId }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = "/sys/prog_surv/result.do?progId=" + <c:out value='${progVO.progId }'/>;
		document.detailForm.submit();
	}
	
	//설문결과
	function fn_resultView() {
		document.detailForm.action = GV_PRESENT_PATH + "/result.do";
		document.detailForm.submit();
	}
	
</script>