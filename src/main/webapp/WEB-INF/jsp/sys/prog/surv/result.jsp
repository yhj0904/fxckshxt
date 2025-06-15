<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: result.jsp
 * @Description : 설문 결과 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>

<link rel="stylesheet" type="text/css" href="/css/common/chart/Chart.min.css" />
<script type="text/javascript" src="/js/common/chart/Chart.min.js"></script>
<script type="text/javascript" src="/js/common/chart/Chart.bundle.min.js"></script>
<script type="text/javascript" src="/js/common/chart/Chart.util.js"></script>
<script>
	var CHART_OPTION = {
		responsive: true,
		legend: {display : false},
		scales: {
            xAxes: [{
            	gridLines: {
                    color: "rgba(0, 0, 0, 0)",
                },
            	ticks: {
                    min: 0,
                    stepSize: 1
                }
            }],
            yAxes: [{
                gridLines: {
                    color: "rgba(0, 0, 0, 0)",
                }   
            }]
        }
	};
</script>
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
						<th>프로그램</th>
						<td>[ <c:out value="${progVO.semstrCd}"/> ] <c:out value="${progVO.progNm}"/></td>
					</tr>
					<tr>
						<th>기간</th>
						<td><c:out value="${progVO.progSdt}"/> ~ <c:out value="${progVO.progEdt}"/></td>
					</tr>
					<tr>
						<th>주관기간</th>
						<td><c:out value="${progVO.eduOrgNm}"/></td>
					</tr>
					<tr>
						<th>진행상태</th>
						<td><c:out value="${progVO.statusCdNm}"/></td>
					</tr>
					<tr>
						<th>설문기간</th>
						<td><c:out value="${progVO.surveySdt}"/> ~ <c:out value="${progVO.surveyEdt}"/></td>
					</tr>
					<tr>
						<th>내용</th>
						<td><c:out value="${progVO.progSumry}" escapeXml="false" /></td>
					</tr>
			  	</table>
			</div>
		</li>
		
		<li>
			<div class="sub_tit"><spring:message code="survMgtVO.message.userList"/></div>
			<div class="sub_cont">
				<table class="list_table">
					<colgroup>
						<col style=""/>
						<col style=""/>
						<col style=""/>
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="board.no"/></th>
							<th><spring:message code="survAnswVO.user"/></th>
							<th><spring:message code="survAnswVO.survey"/></th>
						</tr>
					</thead>
					<tbody id="trgtList">
						<c:forEach var="user" items="${userList }" varStatus="i">
							<tr>
								<td><c:out value='${i.count }'/></td>
								<td><c:out value='${user.inptNm }'/>(<c:out value='${user.inptId }'/>)</td>
								<td><a href="javascript:fn_resultUserView('<c:out value='${progVO.progId }'/>','<c:out value='${user.inptId }'/>')"><spring:message code="survMgtVO.button.userResult"/></a></td>
							</tr>
						</c:forEach>
					</tbody>
			  	</table>
			</div>
		</li>
		<li>
			<div class="sub_tit"><spring:message code="survMgtVO.message.survResult"/></div>
			<div class="sub_cont">
				<table class="survey_table">
					<colgroup>
						<col style="width:50px;"/> <!-- NO -->
						<col style="width:calc(100% - 50px);"/> <!-- 내용 -->
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="board.no"/></th>
							<th><spring:message code="survQuesVO.quesContent"/></th>	
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
										<dl>
											<dt><spring:message code="survQuesVO.quesMemo"/></dt>
											<dd><pre><c:out value="${question.quesMemo}" escapeXml="false"/></pre></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.ncsryYn"/></dt>
											<dd><c:out value="${question.ncsryYn}"/></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.avgPoint"/></dt>
											<dd><c:out value="${question.avgPoint}"/></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.answCnt"/></dt>
											<dd><c:out value="${question.answCnt}"/></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.answPercent"/></dt>
											<dd><c:out value="${question.answPercent}"/></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.quesItems"/></dt>
											<dd>
												<c:choose>
													<c:when test='${question.quesType eq "ETC"}'>
														<c:forEach var='item' items='${question.itemList }' varStatus='j'>
															<a class="button etcViewBtn" data-ques-idx="<c:out value="${question.quesIdx}"/>" data-item-idx="<c:out value="${item.itemIdx}"/>" data-view="N"><spring:message code="survQuesVO.etcResult"/></a>
															<div class="etc_list" style="display:none;"></div>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<canvas id="question_<c:out value="${question.quesIdx}"/>"></canvas>
														<script>
															var chartLabels = [<c:forEach var='item' items='${question.itemList }' varStatus='j'><c:if test='${j.index ne 0}'>,</c:if>'<c:out value="${item.itemTitle}"/> (<c:out value="${item.answPercent}"/>)'</c:forEach>];
															var chartDatas = [<c:forEach var='item' items='${question.itemList }' varStatus='j'><c:if test='${j.index ne 0}'>,</c:if><c:out value="${item.answCnt}"/></c:forEach>];
															new Chart(document.getElementById('question_<c:out value="${question.quesIdx}"/>').getContext('2d'), {
																type: 'horizontalBar',
																data: {labels: chartLabels, datasets: [{data: chartDatas}]},
																options: CHART_OPTION
															});		
														</script>
													</c:otherwise>
												</c:choose>
											</dd>
										</dl>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
			  	</table>
			</div>
		</li>
	</ul>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button list_btn" href="javascript:fn_excelDown(<c:out value='${progVO.progId }' />);">엑셀 다운</a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${survMgtVO.survId }'/>">
  	<input type="hidden" name="progId" >
  	<input type="hidden" name="sUser" value="">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form>

<script type="text/javaScript" defer="defer">
	
	//목록
	function fn_listView() {
		document.detailForm.action = "/sys/prog.do";
		document.detailForm.submit();
	}
	
	//기타의견 보기
	$(document).on("click", ".etcViewBtn", function(){

		var obj = $(this)
		var quesIdx = obj.attr("data-ques-idx");
		var itemIdx = obj.attr("data-item-idx");
		var viewYn = obj.attr("data-view");
		
		if(obj.next().css("display") == "none"){			
			if(viewYn == "N"){
				gf_ajax({
					url : "/sys/prog_surv/etcView.do",
					type : "POST",
					data : { progId : '<c:out value="${progVO.progId}"/>', quesIdx : quesIdx, itemIdx : itemIdx },
				}).then(function(response){
					if(!gf_isNull(response)){
						var str = "<ul class='dot_list'>";
						$.each(response, function (i, item) {
							str += "<li>"+gf_nvl(item, '-')+"</li>";
						});
						str += "</ul>";
						obj.next().append(str);
					}
					obj.attr("data-view", "Y");
					obj.next().css("display", "block");
				});
			} else {
				obj.next().css("display", "block");
			}
		} else {
			obj.next().css("display", "none");
		}
	})
	
	//목록
	function fn_resultUserView(progId, userId) {
		document.detailForm.sId.value = progId;
		document.detailForm.sUser.value = userId;
		document.detailForm.action = GV_PRESENT_PATH + "/resultUser.do";
		document.detailForm.submit();
	}
	
	//엑셀다운
	function fn_excelDown(progId){
		document.detailForm.progId.value = progId;
		document.detailForm.action = "/sys/prog_surv/excelDown.do";
		document.detailForm.submit();
	}
</script>