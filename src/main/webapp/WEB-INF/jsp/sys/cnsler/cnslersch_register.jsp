<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="kr.co.nanwe.dept.service.DeptService" %>
<%@ page import="kr.co.nanwe.dept.service.DeptVO" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 게시글 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<link rel="stylesheet" href="/css/cnsl/cnsl.css" />

<p class="title">상담 일정 등록</p>

<form:form commandName="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<input type="hidden" name="cnslerId" id="cnslerId" value="${userVO.userId }" />
	
	<table class="detail_table">
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
   		<tr>
   			<th>상담원</th>
   			<td>${userVO.userNm }</td>
   		</tr>
   		<tr>
   			<th>상담 일자</th>
   			<td>
   				<input type="date" name="stDt" id="stDt" />
   				 ~ 
   				<input type="date" name="edDt" id="edDt" />
   			</td>
   		</tr>
   		<tr>
   			<th>주기</th>
   			<td>
   				<label class="cnslLabel" style="display: none;"><input name="selectDay" type="checkbox" value="0" />일</label>
   				<label class="cnslLabel"><input name="selectDay" type="checkbox" value="2" />월</label>
   				<label class="cnslLabel"><input name="selectDay" type="checkbox" value="3" />화</label>
   				<label class="cnslLabel"><input name="selectDay" type="checkbox" value="4" />수</label>
   				<label class="cnslLabel"><input name="selectDay" type="checkbox" value="5" />목</label>
   				<label class="cnslLabel"><input name="selectDay" type="checkbox" value="6" />금</label>
   				<label class="cnslLabel"><input name="selectDay" type="checkbox" value="7" />토</label>
   				<input type="hidden" name="selectDay" id="selectDay" />
   				<script>
   					//초기값셋팅
   					$(document).ready(function(){
   						$('input[name=selectDay]').prop("checked", true);
   						$("#selectDay").val("0234567_");
   					});
   				
   					//체크박스 변경시 요일 값변경
					$("input[name=selectDay]").change(function () {
					    var selectDay = "_______";
					    
					    // 각 체크박스의 값을 반영
					    $("input[name=selectDay]").each(function(index) {
					        if ($(this).is(":checked")) {
					            selectDay = selectDay.substr(0, index) + $(this).val() + selectDay.substr(index + 1);
					        } else {
					            selectDay = selectDay.substr(0, index) + '_' + selectDay.substr(index + 1);
					        }
					    });
					
					    // 변경된 값을 숨은 필드에 할당하여 유지
					    $("#selectDay").val(selectDay);
					});
	   			</script>
   			</td>
   		</tr>
   		<tr>
   			<th>시간</th>
   			<td>
   				<label class="cnslLabel"><input type="checkbox" class="timeYn" id="cnslAmYn" name="cnslAmYn" value="Y" checked /> 오전</label>
				<label class="cnslLabel"><input type="checkbox" class="timeYn" id="cnslPmYn" name="cnslPmYn" value="Y" checked /> 오후</label>
				<label class="cnslLabel"><input type="checkbox" id="cnslBoth" name="cnslBoth" checked /> 종일</label>
				<script>
				$(document).ready(function() {
					$("#cnslBoth").click(function() {
						if($("#cnslBoth").is(":checked")){ 
							$("input[name=cnslAmYn]").prop("checked", true);
							$("input[name=cnslPmYn]").prop("checked", true);
						}
						else {
							$("input[name=cnslAmYn]").prop("checked", false);
							$("input[name=cnslPmYn]").prop("checked", false);
						}
					});
					
					$(".cnslLabel").click(function(){
						if($("input[name=cnslAmYn]").is(":checked") == false || $("input[name=cnslPmYn]").is(":checked") == false){$("input[name=cnslBoth]").prop("checked", false);}
						else{$("input[name=cnslBoth]").prop("checked", true);}
					});
				});
				</script>
   			</td>
   		</tr>
   		<!-- <tr>
   			<th>상담여부</th>
   			<td>
   				<label><input type="radio" name="useYn" checked /> 상담진행</label>
   				<label><input type="radio" name="useYn" /> 상담없음</label>
   			</td>
   		</tr> -->
   	</table>
   	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
</form:form>

<div class="btn_wrap">
	<ul style="display: flex; justify-content: center; float: unset;">
		<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a></li>
		<li><a class="button list_btn" href="javascript:fn_cancel();">취소</a></li>
	</ul>
</div>

<script>
//등록 처리
function fn_registerAction() {
	
	if($('#stDt').val() == ''){
		gf_alert("등록 시작일자를 선택하시오.");
		return;
	}
	
	if($('#edDt').val() == ''){
		$('#edDt').val($('#stDt').val());
	}
	
	if($('#selectDay').val() == '' || $('#selectDay').val() == '________'){
		gf_alert("등록주기를 선택하시오.");
		return
	}
	
	if(!$('#cnslAmYn').is(':checked') && !$('#cnslPmYn').is(':checked')){
		gf_alert("상담시간을 선택하시오.");
		return
	}
	
	var msg = confirm('일정을 등록하시겠습니까?');
	
	if(msg == true){
		var cnslerId = $("#cnslerId").val();
		var stDt = $("#stDt").val();
		var edDt = $("#edDt").val();
		var selectDay = $("#selectDay").val();
		var cnslAmYn = $('#cnslAmYn').is(':checked');
		var cnslPmYn = $('#cnslPmYn').is(':checked');
		
		opener.document.getElementById("cnslerId").value = cnslerId;
		opener.document.getElementById("stDt").value = stDt;
		opener.document.getElementById("edDt").value = edDt;
		opener.document.getElementById("selectDay").value = selectDay;
		
		if(cnslAmYn){
			opener.document.getElementById("cnslAmYn").value = "Y";
		} else{
			opener.document.getElementById("cnslAmYn").value = "N";
		}
		if(cnslPmYn){
			opener.document.getElementById("cnslPmYn").value = "Y";
		} else{
			opener.document.getElementById("cnslPmYn").value = "N";
		}
		
		opener.document.schForm.action = "/sys/cnsler/cnslerSchRegisterAction.do";
		opener.document.schForm.submit();
		
		self.close();
	}
}

//취소
function fn_cancel(){
	window.close();
}
</script>