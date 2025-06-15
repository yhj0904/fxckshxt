<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: manage.jsp
 * @Description : 설문 문항&항목 관리 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<c:set var="trgtCount" value="0"/>
	<c:set var="maxSort" value="0"/>
	<input type="hidden" name="survId" value="<c:out value='${survMgtVO.survId}'/>">
	
	<ul class="sub_ul">
		
		<li>
			<div class="sub_tit"><spring:message code='survMgtVO.message.manage'/></div>
			<div class="sub_cont">
				<div style="text-align:right;">
					<a class="button" href="javascript:fn_addQuestion();"><spring:message code='survMgtVO.message.addQuestion'/></a>
				</div>
				<table class="survey_table">
					<colgroup>
						<col style="width:50px;"/> <!-- NO -->
						<col style="width:calc(100% - 350px);"/> <!-- 내용 -->			
						<col style="width:100px;"/> <!-- 순서 -->
						<col style="width:100px;"/> <!-- 사용유무 -->
						<col style="width:100px;"/> <!-- 버튼 -->
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="board.no"/></th>
							<th><spring:message code="survQuesVO.quesContent"/></th>			
							<th><spring:message code="survQuesVO.quesSort"/></th>
							<th><spring:message code="survQuesVO.useYn"/></th>
							<th></th>
						</tr>
					</thead>
					<tbody id="question_list">
						<c:forEach var="question" items="${survMgtVO.quesList }" varStatus="i">				
							<c:if test="${question.quesSort > maxSort }"><c:set var="maxSort" value="${question.quesSort}"/></c:if>
							<tr class="question_row" data-row-idx="${i.index }">
								<td><span class="row_num"><c:out value='${i.count }'/></span></td>
								<td>
									<div class="question_content">
										<input type="hidden" name="rowidx" class="rowidx" value="<c:out value='${i.index }'/>"/>
										<input type="hidden" name="quesFlag" class="quesFlag" value="U"/>
										<input type="hidden" name="quesIdx" class="quesIdx" value="<c:out value="${question.quesIdx}"/>"/>
										<dl>
											<dt><spring:message code="survQuesVO.quesType"/></dt>
											<dd>
												<input type="hidden" name="quesType" class="quesType" value="<c:out value="${question.quesType}"/>"/>
												<c:choose>
													<c:when test='${question.quesType eq "RADIO"}'><spring:message code='survMgtVO.quesType.radio'/></c:when>
													<c:when test='${question.quesType eq "CHECK"}'><spring:message code='survMgtVO.quesType.check'/></c:when>
													<c:when test='${question.quesType eq "ETC"}'><spring:message code='survMgtVO.quesType.etc'/></c:when>
												</c:choose>
											</dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.quesTitle"/></dt>
											<dd><input type="text" name="quesTitle" class="quesTitle w_full" value="<c:out value="${question.quesTitle}"/>"/></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.quesMemo"/></dt>
											<dd><textarea name="quesMemo" class="quesMemo"><c:out value="${question.quesMemo}"/></textarea></dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.ncsryYn"/></dt>
											<dd>
												<select name="quesNcsryYn" class="quesNcsryYn">
													<option value="Y" <c:if test='${question.ncsryYn eq "Y" }'>selected</c:if>>Y</option>
													<option value="N" <c:if test='${question.ncsryYn eq "N" }'>selected</c:if>>N</option>
												</select>
											</dd>
										</dl>
										<dl>
											<dt><spring:message code="survQuesVO.quesItems"/></dt>
											<dd style="text-align:right;">
												<a class="button addItemBtn" href="#none" data-row-idx="${i.index }" <c:if test='${question.quesType eq "ETC" }'>style='display:none;'</c:if>><spring:message code='survMgtVO.message.addItem'/></a>
												<table class="item_table">
													<colgroup>
														<col style="width:calc(100% - 350px);"/>
														<col style="width:80px;"/>
														<col style="width:80px;"/>
														<col style="width:80px;"/>
														<col style="width:80px;"/>
														<col style="width:80px;"/>
													</colgroup>
													<thead>
														<tr>
															<th><spring:message code="survItemVO.itemTitle"/></th>
															<th><spring:message code="survItemVO.itemPoint"/></th>
															<th><spring:message code="survItemVO.itemSort"/></th>
															<th><spring:message code="survItemVO.itemEtc"/></th>
															<th><spring:message code="survItemVO.useYn"/></th>
															<th></th>
														</tr>
													</thead>
													<tbody class="item_list" data-row-idx="<c:out value="${i.index}"/>" data-ques-type="${question.quesType }">
														<c:forEach var="item" items="${question.itemList }" varStatus="j">
															<tr>
																<c:choose>
																	<c:when test='${question.quesType eq "ETC" }'>
																		<td>																
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemFlag" class="itemFlag" value="U"/>
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemIdx" class="itemIdx" value="<c:out value="${item.itemIdx}"/>"/>
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemMemo" class="itemMemo" value="<c:out value="${item.itemMemo}"/>"/>																
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemTitle" class="itemTitle" value="<c:out value="${item.itemTitle}"/>"/>
																			<p><spring:message code='survMgtVO.message.etc'/></p>
																		</td>
																		<td><input type="text" name="row<c:out value="${i.index}"/>_itemPoint" class="itemPoint" value="<c:out value="${item.itemPoint}"/>"/></td>
																		<td>1<input type="hidden" name="row<c:out value="${i.index}"/>_itemSort" class="itemSort" value="1"/></td>
																		<td>Y<input type="hidden" name="row<c:out value="${i.index}"/>_itemEtc" class="itemEtc" value="Y"/></td>
																		<td>Y<input type="hidden" name="row<c:out value="${i.index}"/>_itemUseYn" class="itemUseYn" value="Y"/></td>
																		<td></td>
																	</c:when>
																	<c:otherwise>
																		<td>
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemFlag" class="itemFlag" value="U"/>
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemIdx" class="itemIdx" value="<c:out value="${item.itemIdx}"/>"/>
																			<input type="hidden" name="row<c:out value="${i.index}"/>_itemMemo" class="itemMemo" value="<c:out value="${item.itemMemo}"/>"/>
																			<input type="text" name="row<c:out value="${i.index}"/>_itemTitle" class="itemTitle" value="<c:out value="${item.itemTitle}"/>"/>
																		</td>
																		<td><input type="text" name="row<c:out value="${i.index}"/>_itemPoint" class="itemPoint" value="<c:out value="${item.itemPoint}"/>"/></td>
																		<td><input type="text" name="row<c:out value="${i.index}"/>_itemSort" class="itemSort" value="<c:out value="${item.itemSort}"/>"/></td>
																		<td>
																			<select name="row<c:out value="${i.index}"/>_itemEtc" class="itemEtc">
																				<option value="Y" <c:if test='${item.itemEtc eq "Y" }'>selected</c:if>>Y</option>
																				<option value="N" <c:if test='${item.itemEtc eq "N" }'>selected</c:if>>N</option>
																			</select>
																		</td>
																		<td>
																			<select name="row<c:out value="${i.index}"/>_itemUseYn" class="itemUseYn">
																				<option value="Y" <c:if test='${item.useYn eq "Y" }'>selected</c:if>>Y</option>
																				<option value="N" <c:if test='${item.useYn eq "N" }'>selected</c:if>>N</option>
																			</select>
																		</td>
																		<td><a class="button removeItemBtn" href="#none"><spring:message code="button.remove" /></a></td>
																	</c:otherwise>
																</c:choose>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</dd>
										</dl>
									</div>
								</td>
								<td><input type="text" name="quesSort" class="quesSort" value="<c:out value="${question.quesSort}"/>"/></td>
								<td>
									<select name="quesUseYn" class="quesUseYn">
										<option value="Y" <c:if test='${question.useYn eq "Y" }'>selected</c:if>>Y</option>
										<option value="N" <c:if test='${question.useYn eq "N" }'>selected</c:if>>N</option>
									</select>
								</td>
								<td><a class="button removeQuestionBtn" href="#none"><spring:message code="button.remove" /></a></td>
							</tr>
						</c:forEach>
					</tbody>
			  	</table>
			</div>
		</li>
	</ul>
	
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button" href="javascript:fn_addQuestion();"><spring:message code='survMgtVO.message.addQuestion'/></a>
			</li>
			<li>
				<a class="button save_btn" href="javascript:fn_manageAction();"><spring:message code="button.save" /></a>
			</li>
			<li>
				<a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${survMgtVO.survId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form>

<table style="display:none;">
	<tbody id="copy_question_list">
		<tr class="question_row" data-row-idx="-1">
			<td><span class="row_num">0</span></td>
			<td>
				<div class="question_content">
					<input type="hidden" name="rowidx" class="rowidx" value="-1"/>
					<input type="hidden" name="quesFlag" class="quesFlag" value="I"/>
					<input type="hidden" name="quesIdx" class="quesIdx" value="0"/>
					<dl>
						<dt><spring:message code="survQuesVO.quesType"/></dt>
						<dd>
							<select name="quesType" class="quesType" data-row-idx="-1">
								<option value="RADIO" selected><spring:message code='survMgtVO.quesType.radio'/></option>
								<option value="CHECK"><spring:message code='survMgtVO.quesType.check'/></option>
								<option value="ETC"><spring:message code='survMgtVO.quesType.etc'/></option>
							</select>
						</dd>
					</dl>
					<dl>
						<dt><spring:message code="survQuesVO.quesTitle"/></dt>
						<dd><input type="text" name="quesTitle" class="quesTitle w_full" value=""/></dd>
					</dl>
					<dl>
						<dt><spring:message code="survQuesVO.quesMemo"/></dt>
						<dd><textarea name="quesMemo" class="quesMemo"></textarea></dd>
					</dl>
					<dl>
						<dt><spring:message code="survQuesVO.ncsryYn"/></dt>
						<dd>
							<select name="quesNcsryYn" class="quesNcsryYn">
								<option value="Y" selected>Y</option>
								<option value="N">N</option>
							</select>
						</dd>
					</dl>
					<dl>
						<dt><spring:message code="survQuesVO.quesItems"/></dt>
						<dd style="text-align:right;">
							<a class="button addItemBtn" href="#none" data-row-idx="-1"><spring:message code='survMgtVO.message.addItem'/></a>
							<table class="item_table">
								<colgroup>
									<col style="width:calc(100% - 350px);"/>
									<col style="width:80px;"/>
									<col style="width:80px;"/>
									<col style="width:80px;"/>
									<col style="width:80px;"/>
									<col style="width:80px;"/>
								</colgroup>
								<thead>
									<tr>
										<th><spring:message code="survItemVO.itemTitle"/></th>
										<th><spring:message code="survItemVO.itemPoint"/></th>
										<th><spring:message code="survItemVO.itemSort"/></th>
										<th><spring:message code="survItemVO.itemEtc"/></th>										
										<th><spring:message code="survItemVO.useYn"/></th>
										<th></th>
									</tr>
								</thead>
								<tbody class="item_list" data-row-idx="-1" data-ques-type="">
								</tbody>
							</table>
						</dd>
					</dl>
				</div>
			</td>
			<td><input type="text" name="quesSort" class="quesSort" value="1"/></td>
			<td>
				<select name="quesUseYn" class="quesUseYn">
					<option value="Y" selected>Y</option>
					<option value="N">N</option>
				</select>
			</td>
			<td><a class="button removeQuestionBtn" href="#none"><spring:message code="button.remove" /></a></td>
		</tr>
	</tbody>
 </table>

<script type="text/javaScript" defer="defer">
	
	//현재 row idx
	var ROW_IDX = <c:out value='${fn:length(survMgtVO.quesList)-1}'/>;
	var MAX_SORT = <c:out value='${maxSort}'/>;
	var TRGT_COUNT = <c:out value='${trgtCount}'/>;

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//취소
	function fn_detailView() {
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_manageAction() {		
		var param = $("#detailForm").serializeObject();
		
		gf_ajax({
			url : GV_PRESENT_PATH + "/manageAction.do",
			type : "POST",
			data :  param,
			contentType :  "application/json",
		}).then(function(response){
			if(response){
				fn_detailView();
			} else {
				alert("<spring:message code='message.error.create'/>");
			}
		});
	}
	
	//문항 추가
	function fn_addQuestion() {	
		
		ROW_IDX++; //ROW 증가		
		MAX_SORT++ //SORT 증가
		
		$("#copy_question_list").find(".row_num").html(ROW_IDX+1);
		$("#copy_question_list").find(".rowidx").val(ROW_IDX);
		$("#copy_question_list").find("tr").attr('data-row-idx', ROW_IDX);
		$("#copy_question_list").find(".quesType").attr('data-row-idx', ROW_IDX);
		$("#copy_question_list").find(".item_list").attr('data-row-idx', ROW_IDX);
		$("#copy_question_list").find(".addItemBtn").attr('data-row-idx', ROW_IDX);
		
		$("#question_list").append($("#copy_question_list").html());
		
		//복사후 idx 초기화			
		$("#copy_question_list").find(".rowidx").val("-1");
		$("#copy_question_list").find("tr").attr('data-row-idx', "-1");
		$("#copy_question_list").find(".quesType").attr('data-row-idx', "-1");
		$("#copy_question_list").find(".item_list").attr('data-row-idx', "-1");
		$("#copy_question_list").find(".addItemBtn").attr('data-row-idx', "-1");
		
		//기본 항목 값 세팅
		fn_changeQuesType(ROW_IDX, "RADIO");
		
		//순서 세팅
		$(".question_row[data-row-idx='"+ROW_IDX+"']").find(".quesSort").val(MAX_SORT);
		
		//포커스 이동
		$(".question_row[data-row-idx='"+ROW_IDX+"']").find(".quesType").focus();
	}
	
	//문항 삭제
	$(document).on("click", ".removeQuestionBtn", function(){
		var parentRow = $(this).closest('tr');
		var flag = parentRow.find('.quesFlag').val();
		var changeFlag = "";
		if(flag == "I"){
			changeFlag = "N";			 
		} else if(flag == "U"){
			changeFlag = "D";
		} else if(flag == "D"){
			changeFlag = "U";
		} else if(flag == "N"){
			changeFlag = "I";
		}
		
		if(changeFlag == "N" || changeFlag == "D"){
			parentRow.find('.question_content').css("display","none");
			parentRow.find('.question_content').after('<p class="remove_txt"><spring:message code="survMgtVO.message.deleted"/></p>');
			$(this).html('<spring:message code="button.cancel"/>');
		} else {
			parentRow.find('.remove_txt').remove();
			parentRow.find('.question_content').css("display","block");
			$(this).html('<spring:message code="button.remove"/>');
		}
		
		parentRow.find('.quesFlag').val(changeFlag);
	});
	
	//문항 유형 변경
	$(document).on("change", ".quesType", function(){
		var quesType = $(this).val();
		var rowIdx = $(this).attr("data-row-idx");
		fn_changeQuesType(rowIdx, quesType);
		if(quesType == "ETC"){
			$(".addItemBtn[data-row-idx='"+rowIdx+"']").css("display", "none");
		} else {
			$(".addItemBtn[data-row-idx='"+rowIdx+"']").css("display", "inline-block");
		}
	});
	
	//유형 변경시 항목란 변경
	function fn_changeQuesType(rowIdx, quesType) {
		var presentQuesType = $("#question_list").find(".item_list[data-row-idx='"+rowIdx+"']").attr("data-ques-type");
		if(quesType == presentQuesType){
			return;
		} else if(quesType != "ETC" && presentQuesType != "ETC" && presentQuesType != ""){
			$("#question_list").find(".item_list[data-row-idx='"+rowIdx+"']").attr("data-ques-type", quesType);
			return;
		} else {
			$("#question_list").find(".item_list[data-row-idx='"+rowIdx+"']").empty();
			$("#question_list").find(".item_list[data-row-idx='"+rowIdx+"']").attr("data-ques-type", quesType);
			var str = fn_getItemHtml(rowIdx, quesType);
			$("#question_list").find(".item_list[data-row-idx='"+rowIdx+"']").append(str);
		}
	}
	
	//항목 추가
	$(document).on("click", ".addItemBtn", function(){
		var rowIdx = $(this).attr('data-row-idx');
		var itemList = $(".item_list[data-row-idx='"+rowIdx+"']");
		var quesType = itemList.attr("data-ques-type");
		if(quesType != "ETC"){
			var str = fn_getItemHtml(rowIdx, quesType);
			itemList.append(str);
		}
	});
	
	//항목 삭제
	$(document).on("click", ".removeItemBtn", function(){
		var parentRow = $(this).closest('tr');
		var flag = parentRow.find('.itemFlag').val();
		var changeFlag = "";
		if(flag == "I"){
			changeFlag = "N";			 
		} else if(flag == "U"){
			changeFlag = "D";
		} else if(flag == "D"){
			changeFlag = "U";
		} else if(flag == "N"){
			changeFlag = "I";
		}
		
		if(changeFlag == "N" || changeFlag == "D"){
			parentRow.find('.itemTitle').prop("readonly" , true);
			parentRow.find('.itemPoint').prop("readonly" , true);
			parentRow.find('.itemSort').prop("readonly" , true);
			$(this).html('<spring:message code="button.cancel"/>');
		} else {
			parentRow.find('.itemTitle').prop("readonly" , false);
			parentRow.find('.itemPoint').prop("readonly" , false);
			parentRow.find('.itemSort').prop("readonly" , false);
			$(this).html('<spring:message code="button.remove"/>');
		}
		
		parentRow.find('.itemFlag').val(changeFlag);
	});
	
	function fn_getItemHtml(rowIdx, quesType){
		var str = "<tr>";
		if(quesType == "ETC"){
			str +='<td>';
			str +='	<input type="hidden" name="row'+rowIdx+'_itemFlag" class="itemFlag" value="I"/>';
			str +='	<input type="hidden" name="row'+rowIdx+'_itemIdx" class="itemIdx" value="0"/>';
			str +='	<input type="hidden" name="row'+rowIdx+'_itemMemo" class="itemMemo" value=""/>';															
			str +='	<input type="hidden" name="row'+rowIdx+'_itemTitle" class="itemTitle" value=""/>';
			str +='	<p><spring:message code="survMgtVO.message.etc"/></p>';
			str +='</td>';
			str +='<td><input type="text" name="row'+rowIdx+'_itemPoint" class="itemPoint" value="0"/></td>';
			str +='<td><input type="hidden" name="row'+rowIdx+'_itemSort" class="itemSort" value="1"/></td>';
			str +='<td><input type="hidden" name="row'+rowIdx+'_itemEtc" class="itemEtc" value="Y"/></td>';
			str +='<td><input type="hidden" name="row'+rowIdx+'_itemUseYn" class="itemUseYn" value="Y"/></td>';
			str +='<td></td>';
		} else {
			var sort = $("#question_list").find(".item_list[data-row-idx='"+rowIdx+"']").children('tr').length + 1;
			str +='<td>';
			str +='	<input type="hidden" name="row'+rowIdx+'_itemFlag" class="itemFlag" value="I"/>';
			str +='	<input type="hidden" name="row'+rowIdx+'_itemIdx" class="itemIdx" value="0"/>';
			str +='	<input type="hidden" name="row'+rowIdx+'_itemMemo" class="itemMemo" value=""/>';															
			str +='	<input type="text" name="row'+rowIdx+'_itemTitle" class="itemTitle" value=""/>';
			str +='</td>';
			str +='<td><input type="text" name="row'+rowIdx+'_itemPoint" class="itemPoint" value="0"/></td>';
			str +='<td><input type="text" name="row'+rowIdx+'_itemSort" class="itemSort" value="'+sort+'"/></td>';
			str +='<td>';
			str +='<select name="row'+rowIdx+'_itemEtc" class="itemEtc">';
			str +='	<option value="Y">Y</option>';
			str +='	<option value="N" selected>N</option>';
			str +='</select>';
			str +='</td>';
			str +='<td>';
			str +='<select name="row'+rowIdx+'_itemUseYn" class="itemUseYn">';
			str +='	<option value="Y" selected>Y</option>';
			str +='	<option value="N">N</option>';
			str +='</select>';
			str +='</td>';
			str +='<td><a class="button removeItemBtn" href="#none"><spring:message code="button.remove" /></a></td>';
		}
		str += "</tr>";
		
		return str;
	};
	
	//사용자 목록을 조회한다.
	function fn_getUserList() {
		var id = "userList";
		var url = "/sys/user/userList.json";
		var param = {};
		var columns = [
			{key:"userId", name:"아이디"},
			{key:"userNm", name:"이름"}
		];
		var callback = function(data){
			TRGT_COUNT++;
			var userId = data.userId;
			var userNm = data.userNm;
			var str = "<tr>";
			str += '<td><span class="trgt_count">'+TRGT_COUNT+'</span></td>';
			str += '<td><spring:message code="survTrgtVO.trgtTarget.id"/></td>';
			str += '<td>'+userId+'<input type="hidden" name="trgtId" value="'+userId+'"/></td>';
			str += '<td><a class="button removeTrgtBtn" href="#none"><spring:message code="button.remove"/></a></td>';
			str += '</tr>';
			$("#trgtList").append(str);
		};
		gf_openSearchPop(id, url, param, columns, callback);
	};
	
	//권한코드 목록
	function fn_getAuthList() {
		var id = "cdList";
		var url = "/auth/list.json";
		var param = {cd:"COM02"};
		var columns = [
			{key:"authCd", name:"권한코드"},
			{key:"authNm", name:"권한명"}
		];
		var callback = function(data){
			TRGT_COUNT++;
			var authCd = data.authCd;
			var authNm = data.authNm;
			var str = "<tr>";
			str += '<td><span class="trgt_count">'+TRGT_COUNT+'</span></td>';
			str += '<td><spring:message code="survTrgtVO.trgtTarget.cd"/></td>';
			str += '<td>'+authCd+'<input type="hidden" name="trgtCd" value="'+authCd+'"/></td>';
			str += '<td><a class="button removeTrgtBtn" href="#none"><spring:message code="button.remove"/></a></td>';
			str += '</tr>';
			$("#trgtList").append(str);
		};
		gf_openSearchPop(id, url, param, columns, callback);
	};
	
	//설문대상 삭제
	$(document).on("click", ".removeTrgtBtn", function(){
		$(this).closest('tr').remove();
		TRGT_COUNT = 0;
		$("#trgtList tr").each(function(index, item){
			TRGT_COUNT++;
			$(item).find(".trgt_count").html(TRGT_COUNT);
		});
	});
	
</script>