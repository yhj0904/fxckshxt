<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 일정 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="board_wrap">

	<c:if test='${schMgtVO.header ne null and schMgtVO.header ne ""}'>
		<div class="board_top">
			<c:out value="${schMgtVO.header }" escapeXml="false"/>
		</div>
	</c:if>
	
	<div class="board_cont">
		<form:form modelAttribute="schVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<form:hidden path="schCd" value="${schMgtVO.code }"/>			
			<c:if test='${!empty schMgtVO and GV_SCHEDULE_SKIN_CODE ne null and GV_SCHEDULE_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/main/schedule/${GV_SCHEDULE_SKIN_CODE }/register.jsp"></c:import>
			</c:if>			
		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>		  	
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
			<input type="hidden" name="sDate" value="<c:out value='${sDate }'/>">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>  	
		</form:form>
				
		<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
		<validator:javascript formName="schVO" staticJavascript="false" xhtml="true" cdata="false"/>
		<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
		<script type="text/javaScript" defer="defer">	
			
			$(function(){
				
				//에디터사용시
				var editorArray = [{ id : "contents" }];
				gf_initCkEditor(editorArray);
				
				//첨부파일 사용시
				var fileYn = '<c:out value="${schMgtVO.fileYn}"/>';
				if(fileYn == "Y"){
					var multiOption = {
										listId : "uploadList",
										inputId : "uploadFiles",
										inputName : "uploadFiles",
										uploadCount : "<c:out value='${schMgtVO.fileCnt}'/>",
										uploadFileSize : "<c:out value='${schMgtVO.fileSize}'/>",
										uploadFileExt : "<c:out value='${schMgtVO.fileExt}'/>"
									};
					gf_initMultiSelector(multiOption);
				}
			});
			
			//목록
			function fn_listView() {
				document.detailForm.action = "/schedule/<c:out value='${schedulePath}'/>/list.do";
				document.detailForm.submit();
			}
			
			//등록 처리
			function fn_registerAction() {
				frm = document.detailForm;
				if (!validateSchVO(frm)) {
					return;
				} else {
					frm.action = "/schedule/<c:out value='${schedulePath}'/>/registerAction.do";
					frm.submit();
				}
			}
			
		</script>
	</div>
	
	<c:if test='${schMgtVO.footer ne null and schMgtVO.footer ne ""}'>
		<div class="board_bottom">
			<c:out value="${schMgtVO.footer }" escapeXml="false"/>
		</div>
	</c:if>
</div>