<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 게시글 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.26		신한나			관리자 게시판 구조 변경
 */
%>
<div class="board_wrap">

	<div class="board_cont">
		<form:form commandName="bbsVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<form:hidden path="bbsCd" value="${bbsMgtVO.code }"/>
			<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/sys/bbs/${GV_BOARD_SKIN_CODE }/register.jsp"></c:import>
			</c:if>
		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>  	
		</form:form>
	</div>

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
			if (!validateBbsVO(frm)) {
				return;
			} else {
				frm.action = GV_PRESENT_PATH + "/registerAction.do";
				frm.submit();
			}
		}
		
	</script>
</div>