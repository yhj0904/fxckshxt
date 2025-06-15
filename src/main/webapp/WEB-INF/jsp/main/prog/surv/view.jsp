<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 설문조사  화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>

<div class="survey_wrap">
	<form id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
		<c:set var="questionCount" value="0"/>
		<input type="hidden" name="progId" value="<c:out value="${progSurvMgtVO.progId}"/>"/>
		<input type="hidden" name="survId" value="<c:out value="${progSurvMgtVO.quesList[0].survId}"/>"/>
		<ul class="sub_ul">
			<li>
				<div class="sub_cont">
					<div class="survey_info">
						<div class="survey_tit"><c:out value="${progSurvMgtVO.progNm}"/></div>
						<div class="survey_cont"><c:out value="${progSurvMgtVO.survMemo}" escapeXml="false"/></div>
					</div>
				</div>
			</li>
			
			<li>
				<div class="sub_cont">
					<ul class="survey_list">
						<c:forEach var="question" items="${progSurvMgtVO.quesList }" varStatus="i">
							<li>
								<input type="hidden" name="quesIdx" class="quesIdx" value="<c:out value="${question.quesIdx}"/>">
								<c:if test='${question.ncsryYn eq "Y" }'>
									<input type="hidden" class="quesNcsry" value="<c:out value="${question.quesIdx}"/>">
								</c:if>
								<c:set var="questionCount" value="${questionCount + 1 }"/>
								<div class="survey_ques">
									<span class="num"><c:out value='${i.count }'/>&#46;</span>
									<p class="tit<c:if test='${question.ncsryYn eq "Y" }'> required</c:if>"><c:out value="${question.quesTitle}"/></p>
									<div class="memo"><pre><c:out value="${question.quesMemo}" escapeXml="false"/></pre></div>
								</div>
								<div class="survey_item">
									<c:choose>
										<c:when test='${question.quesType eq "ETC" }'>
											<c:forEach var="item" items="${question.itemList }" varStatus="j">
												<input type="hidden" name="question_<c:out value="${question.quesIdx}"/>" class="question_item" value="<c:out value="${item.itemIdx}"/>">
												<textarea class="item_etc" name="etc_<c:out value="${question.quesIdx}"/>_<c:out value="${item.itemIdx}"/>"></textarea>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<ul>
												<c:forEach var="item" items="${question.itemList }" varStatus="j">
													<li>
														<c:choose>
															<c:when test='${question.quesType eq "RADIO" }'>
																<label class="radio">
																	<input type="radio" name="question_<c:out value="${question.quesIdx}"/>" class="question_item" value="<c:out value="${item.itemIdx}"/>" data-item-etc="<c:out value="${item.itemEtc}"/>"><i></i>
																	<c:out value="${item.itemTitle}"/>
																	<c:choose>
																		<c:when test='${item.itemEtc eq "Y"}'>
																			<input type="text" class="item_etc" name="etc_<c:out value="${question.quesIdx}"/>_<c:out value="${item.itemIdx}"/>" readonly/>
																		</c:when>
																		<c:otherwise>
																			<input type="hidden" class="item_etc" name="etc_<c:out value="${question.quesIdx}"/>_<c:out value="${item.itemIdx}"/>" value=""/>
																		</c:otherwise>
																	</c:choose>
																</label>
															</c:when>
															<c:when test='${question.quesType eq "CHECK" }'>
																<label class="check">
																	<input type="checkbox" name="question_<c:out value="${question.quesIdx}"/>" class="question_item" value="<c:out value="${item.itemIdx}"/>" data-item-etc="<c:out value="${item.itemEtc}"/>"><i></i>
																	<c:out value="${item.itemTitle}"/>
																	<c:choose>
																		<c:when test='${item.itemEtc eq "Y"}'>
																			<input type="text" class="item_etc" name="etc_<c:out value="${question.quesIdx}"/>_<c:out value="${item.itemIdx}"/>" readonly/>
																		</c:when>
																		<c:otherwise>
																			<input type="hidden" class="item_etc" name="etc_<c:out value="${question.quesIdx}"/>_<c:out value="${item.itemIdx}"/>" value=""/>
																		</c:otherwise>
																	</c:choose>
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
			</li>
			<li>
				<div class="sub_cont tc">
					<a class="survey_btn" href="javascript:fn_registerAction();"><spring:message code='survMgtVO.button.survey'/></a>
				</div>
			</li>
		</ul>
	</form>

	<script type="text/javaScript" defer="defer">
		
		var QUESTION_COUNT = <c:out value="${questionCount}"/>;
		
		$(document).on("click", ".question_item" , function(){
			var type = $(this).prop("type");
			var checked = $(this).prop("checked");
			if(checked){
				if(type == "radio"){
					$(this).closest("ul").find(".item_etc").prop("readonly", true);
				}
				if($(this).attr("data-item-etc") == "Y"){
					$(this).siblings(".item_etc").prop("readonly", false);
				}
			} else {
				$(this).siblings(".item_etc").prop("readonly", true);
			}
		})
		
		//수정 처리
		function fn_registerAction() {
			
			//필수값 체크
			var ncsryObj;
			var ncsryCheck = false;
			$(".quesNcsry").each(function(){
				ncsryObj = $("[name='"+"question_"+$(this).val()+"']");
				var objType = ncsryObj.prop("type");
				if(objType == "hidden" && $.trim(ncsryObj.next().val()) == ""){
					ncsryObj = ncsryObj.next();
					ncsryCheck = true;
					return false;
				} else if ((objType == "checkbox" || objType == "radio") && !ncsryObj.is(":checked")){
					ncsryObj = ncsryObj.first();
					ncsryCheck = true;
					return false;
				}
			});	
			
			//필수값 체크가 안된경우
			if(ncsryCheck){
				alert("<spring:message code='survMgtVO.error.required'/>");
				ncsryObj.focus();
				return false;
			}
			
			var param = $("#detailForm").serializeObject();
			
			gf_ajax({
				url : GV_PRESENT_PATH + "/registerAction.do",
				type : "POST",
				data :  param,
				contentType :  "application/json",
			}).then(function(response){
				if(response.result){
					alert('<spring:message code="survMgtVO.success"/>');
					window.close();
				} else {
					alert(response.errMsg);
				}
			});
		}
	</script>
</div>