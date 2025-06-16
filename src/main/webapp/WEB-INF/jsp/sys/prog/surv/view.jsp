<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 설문 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	<ul class="sub_ul">
		<li>
			<div class="sub_cont">
				<table class="detail_table">
					<caption>설문 상세조회</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><spring:message code="survMgtVO.survId"/></th>
						<td><c:out value="${progSurvMgtVO.survId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.survTitle"/></th>
						<td><c:out value="${progSurvMgtVO.survTitle}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.survMemo"/></th>
						<td><c:out value="${progSurvMgtVO.survMemo}" escapeXml="false"/></td>
					</tr>
					<%-- <tr>
						<th><spring:message code="survMgtVO.survDate1"/></th>
						<td><c:out value="${progSurvMgtVO.survDate1}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.survDate2"/></th>
						<td><c:out value="${progSurvMgtVO.survDate2}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.useYn"/></th>
						<td><c:out value="${progSurvMgtVO.useYn}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.survState"/></th>
						<td><c:out value="${progSurvMgtVO.survState}"/></td>
					</tr> --%>
					<tr>
						<th><spring:message code="survMgtVO.inptId"/></th>
						<td><c:out value="${progSurvMgtVO.inptId}"/></td>
					</tr>
					<%-- <tr>
						<th><spring:message code="survMgtVO.inptIp"/></th>
						<td><c:out value="${progSurvMgtVO.inptIp}"/></td>
					</tr>
 					<tr>
						<th><spring:message code="survMgtVO.inptDttm"/></th>
						<td><c:out value="${progSurvMgtVO.inptDttm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.modiId"/></th>
						<td><c:out value="${progSurvMgtVO.modiId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="survMgtVO.modiIp"/></th>
						<td><c:out value="${progSurvMgtVO.modiIp}"/></td>
					</tr>
 					<tr>
						<th><spring:message code="survMgtVO.modiDttm"/></th>
						<td><c:out value="${progSurvMgtVO.modiDttm}"/></td>
					</tr> --%>
			  	</table>
			</div>
		</li>
		
		<li>
			<div class="sub_tit"><spring:message code='survMgtVO.message.manage'/></div>
			<div class="sub_cont">
				<table class="survey_table">
					<colgroup>
						<col style="width:50px;"/> <!-- NO -->
						<col style="width:calc(100% - 350px);"/> <!-- 내용 -->			
						<col style="width:100px;"/> <!-- 순서 -->
						<col style="width:100px;"/> <!-- 사용유무 -->
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="board.no"/></th>
							<th><spring:message code="survQuesVO.quesContent"/></th>			
							<th><spring:message code="survQuesVO.quesSort"/></th>
							<th><spring:message code="survQuesVO.useYn"/></th>
						</tr>
					</thead>
					<tbody id="question_list">
						<c:forEach var="question" items="${progSurvMgtVO.quesList }" varStatus="i">
							<tr class="question_row">
								<td><span class="row_num"><c:out value='${i.count }'/></span></td>
								<td>
									<div class="question_content">
										<dl>
											<dt><spring:message code="survQuesVO.quesType"/></dt>
											<dd>
												<c:choose>
													<c:when test='${question.quesType eq "RADIO"}'><spring:message code='survMgtVO.quesType.radio'/></c:when>
													<c:when test='${question.quesType eq "CHECK"}'><spring:message code='survMgtVO.quesType.check'/></c:when>
													<c:when test='${question.quesType eq "ETC"}'><spring:message code='survMgtVO.quesType.etc'/></c:when>
												</c:choose>
											</dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.quesTitle"/></dt>
											<dd><c:out value="${question.quesTitle}"/></dd>
										</dl>
										<%-- <dl>
											<dt><spring:message code="survQuesVO.quesMemo"/></dt>
											<dd><pre><c:out value="${question.quesMemo}" escapeXml="false"/></pre></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.ncsryYn"/></dt>
											<dd><c:out value="${question.ncsryYn}"/></dd>
										</dl> --%>
										<dl>
											<dt><spring:message code="survQuesVO.quesItems"/></dt>
											<dd>
												<table class="item_table">
													<colgroup>
														<col style="width:calc(100% - 350px);"/>
														<col style="width:80px;"/>
														<col style="width:80px;"/>
														<col style="width:80px;"/>
													</colgroup>
													<thead>
														<tr>
															<th><spring:message code="survItemVO.itemTitle"/></th>
															<th><spring:message code="survItemVO.itemPoint"/></th>
															<th><spring:message code="survItemVO.itemSort"/></th>
															<th><spring:message code="survItemVO.useYn"/></th>
														</tr>
													</thead>
													<tbody class="item_list">
														<c:forEach var="item" items="${question.itemList }" varStatus="j">
															<tr>
																<td>
																	<c:choose>
																		<c:when test='${question.quesType eq "ETC" }'><p><spring:message code='survMgtVO.message.etc'/></p></c:when>
																		<c:otherwise><c:out value="${item.itemTitle}"/></c:otherwise>
																	</c:choose>
																</td>
																<td><c:out value="${item.itemPoint}"/></td>
																<td><c:out value="${item.itemSort}"/></td>
																<td><c:out value="${item.useYn}"/></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</dd>
										</dl>
									</div>
								</td>
								<td><c:out value="${question.quesSort}"/></td>
								<td><c:out value="${question.useYn}"/></td>
							</tr>
						</c:forEach>
					</tbody>
			  	</table>
			</div>
		</li>
	</ul>
   	
   	<div class="btn_wrap">
		<ul>
			<c:choose>					
				<c:when test='${survMgtVO.survState eq "N"}'>
					<li><a class="button" href="javascript:fn_manageView();"><spring:message code="survMgtVO.message.manage" /></a></li>
					<li><a class="button" href="javascript:fn_modifyState('<c:out value="${progSurvMgtVO.survId }"/>', 'S');"><spring:message code="survMgtVO.message.start" /></a></li>
				</c:when>
				<c:when test='${survMgtVO.survState eq "S"}'>
					<li><a class="button" href="javascript:fn_modifyState('<c:out value="${progSurvMgtVO.survId }"/>', 'E');"><spring:message code="survMgtVO.message.end" /></a></li>
				</c:when>
				<c:when test='${survMgtVO.survState eq "E"}'>
					<li><a class="button" href="javascript:fn_resultView('<c:out value="${progSurvMgtVO.survId }"/>');"><spring:message code="survMgtVO.message.result" /></a></li>
				</c:when>
			</c:choose>	
			<li><a class="button register_btn" href="javascript:fn_manageView('<c:out value="${progSurvMgtVO.survId }"/>');" ><spring:message code="survMgtVO.message.manage"/></a></li>		
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
			<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
			<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${progSurvMgtVO.survId }'/>">
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
	
	//문항관리
	function fn_manageView() {
		document.detailForm.action = GV_PRESENT_PATH + "/manage.do";
		document.detailForm.submit();
	}
	
	//설문상태 변경
	function fn_modifyState(survId, survState) {
		
		var confirmMsg = "";
		
		if(survState == "S"){
			confirmMsg = '<spring:message code="survMgtVO.message.confirmStart" />';
		} else if (survState == "E"){
			confirmMsg = '<spring:message code="survMgtVO.message.confirmEnd" />';
		}
		
		if(confirm(confirmMsg)){
			gf_ajax({
				url : GV_PRESENT_PATH + "/modifyState.do",
				type : "POST",
				data : {survId: survId, survState : survState},
			}).then(function(response){
				if(response){
					document.detailForm.action = GV_PRESENT_PATH + "/view.do";
					document.detailForm.submit();
				} else {
					alert("<spring:message code='message.error'/>");
				}
			});
		}
	}
	
	//설문결과
	function fn_resultView() {
		document.detailForm.action = GV_PRESENT_PATH + "/result.do";
		document.detailForm.submit();
	}
	
</script>