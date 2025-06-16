<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 게시글 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<link rel="stylesheet" href="/css/cnsl/cnsl.css" />

<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">

	<table class="detail_table">
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th>상담원 아이디</th>
			<td>
				<form:input path="cnslerId" />
				<button type="button" class="search_btn" onclick="fn_getUserList();"><spring:message code="button.search"/></button>
			</td>
		</tr>
		<tr>
			<th>상담분야</th>
			<td>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeTrack" value="Y" checked /> 진로</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeJob" value="Y" checked /> 취업</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeLife" value="Y" checked /> 생활</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeResume" value="Y" checked /> 입사지원서</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeIntv" value="Y" checked /> 모의면접</label>
			</td>
		</tr>
		<tr>
			<th>상담장소</th>
			<td>
				<form:input path="cnslPlace" value="학생성공처 대학일자리플러스센터" style="min-width: 400px" />
			</td>
		</tr>
		<!-- <tr>
			<th>상담가능시간</th>
			<td>
				<label class="cnslLabel"><input type="checkbox" class="timeYn" id="cnslAmYn" name="cnslAmYn" value="Y" /> 오전</label>
				<label class="cnslLabel"><input type="checkbox" class="timeYn" id="cnslPmYn" name="cnslPmYn" value="Y" /> 오후</label>
				<label class="cnslLabel"><input type="checkbox" id="cnslBoth" name="cnslBoth" /> 종일</label>
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
						
						$(".cnslLabel").click(function(){
							if($("input[name=cnslAmYn]").is(":checked") == false || $("input[name=cnslPmYn]").is(":checked") == false){$("input[name=cnslBoth]").prop("checked", false);}
							else{$("input[name=cnslBoth]").prop("checked", true);}
						});
					});
				});
				</script>
			</td>
		</tr> -->
		<tr>
			<th>상담진행여부</th>
			<td>
				<label><input type="radio" name="useYn" value="Y" checked /> 상담 진행</label>
				<label><input type="radio" name="useYn" value="N" /> 상담 없음</label>
			</td>
		</tr>
	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();">취소</a></li>
		</ul>
	</div>
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="bbsVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	
	
	$(function(){
		
		//에디터사용시
		var editorYn = '<c:out value="${bbsMgtVO.editorYn}"/>';
		if(editorYn == "Y"){
			var editorArray = [{ id : "contents" , useUpload : '<c:out value="${bbsMgtVO.editorFileYn}"/>' }];
			gf_initCkEditor(editorArray);
		}
		
		//첨부파일 사용시
		var fileYn = '<c:out value="${bbsMgtVO.fileYn}"/>';
		if(fileYn == "Y"){
			var multiOption = {
								listId : "uploadList",
								inputId : "uploadFiles",
								inputName : "uploadFiles",
								uploadCount : "<c:out value='${bbsMgtVO.fileCnt}'/>",
								uploadFileSize : "<c:out value='${bbsMgtVO.fileSize}'/>",
								uploadFileExt : "<c:out value='${bbsMgtVO.fileExt}'/>"
							};
			gf_initMultiSelector(multiOption);
		}
	});
	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		frm.action = GV_PRESENT_PATH + "/registerAction.do";
		frm.submit();
	}
	
	//사용자 목록을 조회한다.
	function fn_getUserList() {
		var id = "userList";
		var url = "/sys/user/cnslerUserList.json";
		var param = {};
		var columns = [
			{key:"userId", name:"아이디"},
			{key:"userNm", name:"이름"}
		];
		var callback = function(data){
			var userId = data.userId;
			document.detailForm.cnslerId.value = userId;
		};
		gf_openSearchPop(id, url, param, columns, callback);
	};
</script>

<style>
.layer_pop.search_pop{max-width: 50%;}
</style>