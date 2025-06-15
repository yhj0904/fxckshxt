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
<link rel="stylesheet" href="/css/cnsl/cnsl.css" />

<form id="boardSearchForm" name="boardSearchForm" method="post" autocomplete="off">
	<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>" placeholder="단과를 입력하세요">
	<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
	<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
</form>


<form id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<% 
		String userId = request.getParameter("userId");
		pageContext.setAttribute("cnslerId", userId);
	%>
	<input type="hidden" name="cnslerId" value="${cnslerId }" />
	
	<table class="colgList">
		<thead>
			<tr>
				<th>단과대학</th>
				<th>학과</th>
				<th><input type="checkbox" id="checkAll" onclick="fn_checkAll();" />선택</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach var="dept" items="${deptVO }" varStatus="i">
				<tr>
					<td><c:out value="${dept.hiDeptNmKor }" /> </td>
					<td><c:out value="${dept.deptNmKor }" /> </td>
					<td>
						<input type="checkbox" class="chk_show" id="box_${i.index }" />
						<input type="checkbox" name="chk_colg" class="chk_colg" id="box_${i.index }_1" value="${dept.hiDeptCd }" style="display: none" />
						<input type="checkbox" name="chk_dept" class="chk_dept" id="box_${i.index }_2" value="${dept.deptCd }" style="display: none" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	
	<!-- 값 전달부분 -->
	<input type="hidden" id="colg_values" name="colg_values" />
	<input type="hidden" id="dept_values" name="dept_values" />
	<!-- 값 전달부분 끝 -->
	
	<div class="colg_submit">
		<a href="javascript:fn_colgsubmit();">학과 등록</a>
	</div>
</form>

<script type="text/javaScript" defer="defer">

//검색
function fn_search(){
	var searchFrm = document.boardSearchForm;
	document.getElementById("_search_sv_").value = searchFrm.sv.value;
	gf_movePage(1);
}

function fn_checkAll(){
	//배열선언
	var colg_arr = [];
	var dept_arr = [];
	
	//체크박스 전부체크
	if($('#checkAll').is(":checked") == false){
		$("input[type=checkbox]").prop("checked", false);
	}else{
		$("input[type=checkbox]").prop("checked", true);
	}
	
	//학부배열에누적
	$("input[name=chk_colg]:checked").each(function(i){
		colg_arr.push($(this).val());
	});
	
	//학과배열에누적
	$("input[name=chk_dept]:checked").each(function(i){
		dept_arr.push($(this).val());
	});
	
	//값 삽입
	$("#colg_values").val(colg_arr);
	$("#dept_values").val(dept_arr);
}

$(document).ready(function(){
	$('.chk_show').click(function(){
		//배열선언
		var colg_arr = [];
		var dept_arr = [];
		
		//체크될박스 아이디
		var chk_colgId = '#' + $(this).attr("id") + '_1';
		var chk_deptId = '#' + $(this).attr("id") + '_2';
		
		//chk_show의 체크여부에 따라 실제값들변경
		if($(this).is(":checked") == true){
			$(chk_colgId).prop('checked', true);
			$(chk_deptId).prop('checked', true);
		}else{
			$(chk_colgId).prop('checked', false);
			$(chk_deptId).prop('checked', false);	
		}
		
		//학부배열에누적
		$("input[name=chk_colg]:checked").each(function(i){
			colg_arr.push($(this).val());
		});
		
		//학과배열에누적
		$("input[name=chk_dept]:checked").each(function(i){
			dept_arr.push($(this).val());
		});
		
		//값 삽입
		$("#colg_values").val(colg_arr);
		$("#dept_values").val(dept_arr);
	});
});

//부서 입력
function fn_colgsubmit(){
	
	var msg = confirm('선택한 학과를 등록하시겠습니까?');
	
	if(msg == true){				
		var colg_arr = $("#colg_values").val();
		var dept_arr = $("#dept_values").val();
		
		opener.document.getElementById("colg_values").value = colg_arr;
		opener.document.getElementById("dept_values").value = dept_arr;
		
		document.detailForm.target = opener;
		document.detailForm.action = GV_PRESENT_PATH + "/colgRegist.do";
		document.detailForm.submit();
		
		self.close();
	}
	
}
</script>
