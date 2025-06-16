<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 게시글 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="board_wrap">

	<c:if test='${bbsMgtVO.header ne null and bbsMgtVO.header ne ""}'>
		<div class="board_top">
			<c:out value="${bbsMgtVO.header }" escapeXml="false"/>
		</div>
	</c:if>
	
	<div class="board_cont">
		<form:form modelAttribute="bbsVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<form:hidden path="bbsId"/>
		   	<form:hidden path="bbsCd"/>
			<form:hidden path="category" />
			<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/main/board/${GV_BOARD_SKIN_CODE }/modify.jsp"></c:import>
			</c:if>	
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>		  	
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="<c:out value='${bbsVO.bbsId}'/>">
			<input type="hidden" name="sCode" value="<c:out value='${bbsVO.bbsCd }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
			<c:if test='${bbsPw ne null and bbsPw ne ""}'>
				<input type="hidden" name="bbsPw" value="<c:out value='${bbsPw }'/>">
			</c:if>	
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>  	
		</form:form>
	</div>
	
	<c:if test='${bbsMgtVO.footer ne null and bbsMgtVO.footer ne ""}'>
		<div class="board_bottom">
			<c:out value="${bbsMgtVO.footer }" escapeXml="false"/>
		</div>
	</c:if>

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
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>.do";
			document.detailForm.submit();
		}
		
		//수정 처리
		function fn_modifyAction() {
			frm = document.detailForm;
			if (!validateBbsVO(frm)) {
				return;
			} else {
				frm.action = "/board/<c:out value='${boardPath}'/>/modifyAction.do";
				frm.submit();
			}
			
		}
		
		//삭제
		function fn_removeView() {
			var msg = confirm('<spring:message code="message.confirm.remove" />');
			if(msg == true){
				document.detailForm.action = "/board/<c:out value='${boardPath}'/>/removeAction.do";
				document.detailForm.submit();
			}
		}
	
		//취소
		function fn_detailView() {
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>/view.do";
			document.detailForm.submit();
		}
		
	</script>
</div>