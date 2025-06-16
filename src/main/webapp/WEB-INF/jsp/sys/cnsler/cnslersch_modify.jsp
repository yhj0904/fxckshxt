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

<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<input type="hidden" name="cnslerId" value="${cnslerVO.cnslerId }" />
	<input type="hidden" name="schDt" value="${cnslerVO.schDt }" />
	<input type="hidden" name="sId" value="${cnslerVO.cnslerId }" />
	
	<table class="detail_table">
		<caption>상담일정 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th>상담원</th>
			<td><c:out value="${userVO.userNm }" /></td>
		</tr>
		<tr>
			<th>상담원 아이디</th>
			<td><c:out value="${cnslerVO.cnslerId }" /></td>
		</tr>
		<tr>
			<th>상담일</th>
			<td><c:out value="${cnslerVO.schDt }" /></td>
		</tr>
		<tr>
			<th>상담시간</th>
			<td>
   				<label class="cnslLabel"><input type="checkbox" class="timeYn" id="cnslAmYn" name="cnslAmYn" value="Y" <c:if test="${cnslerVO.cnslAmYn eq 'Y' }" >checked</c:if> /> 오전</label>
				<label class="cnslLabel"><input type="checkbox" class="timeYn" id="cnslPmYn" name="cnslPmYn" value="Y" <c:if test="${cnslerVO.cnslPmYn eq 'Y' }" >checked</c:if> /> 오후</label>
				<label class="cnslLabel"><input type="checkbox" id="cnslBoth" name="cnslBoth" <c:if test="${cnslerVO.cnslAmYn eq 'Y' && cnslerVO.cnslPmYn eq 'Y' }" >checked</c:if> /> 종일</label>
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
		<tr>
			<th>입력ID</th>
			<td><c:out value="${cnslerVO.inptId }" /></td>
		</tr>
		<tr>
			<th>입력IP</th>
			<td><c:out value="${cnslerVO.inptIp }" /></td>
		</tr>
		<tr>
			<th>입력일</th>
			<td><c:out value="${cnslerVO.inptDttm }" /></td>
		</tr>
   	</table>
   	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
</form:form>

<div class="btn_wrap">
	<ul>
		<li><a class="button modify_btn" href="javascript:fn_modifyAction('<c:out value='${cnslerVO.cnslerId }' />');">일정<spring:message code="button.modify" /></a></li>
		<li><a class="button remove_btn" href="javascript:fn_removeView();">일정<spring:message code="button.remove" /></a></li>
		<li><a class="button list_btn" href="javascript:fn_listView('<c:out value='${cnslerVO.cnslerId }' />');">달력</a></li>
	</ul>
</div>

<script>
//목록
function fn_listView(sId) {
	document.detailForm.sId.value = sId;
	document.detailForm.action = GV_PRESENT_PATH + "/cnslerSch.do";
	document.detailForm.submit();
}

//수정
function fn_modifyAction() {
	
	var msg = gf_confirm('일정을 정말 수정하시겠습니까?', function(e){
		if(e == "Y"){				
			document.detailForm.action = GV_PRESENT_PATH + "/cnslerSchModifyAction.do";
			document.detailForm.submit();
		}
	});
}

//삭제
function fn_removeView() {
	
	var msg = gf_confirm('<spring:message code="message.confirm.remove" />', function(e){
		if(e == "Y"){				
			document.detailForm.action = GV_PRESENT_PATH + "/cnslerSchRemoveAction.do";
			document.detailForm.submit();
		}
	});
	
}
</script>