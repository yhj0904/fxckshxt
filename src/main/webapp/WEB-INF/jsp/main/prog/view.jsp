<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 게시글 상세조회 화면
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
<form:form modelAttribute
			<form:hidden path="bbsId"/>
			<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/main/board/${GV_BOARD_SKIN_CODE }/view.jsp"></c:import>
			</c:if>
			<div class="prev_next_table">
				<c:import url="/WEB-INF/jsp/main/board/near_bbs.jsp"></c:import>
			</div>
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>			
		   	<% /** 검색조건 유지 */ %>
		  	<input type="hidden" name="sId" value="<c:out value='${bbsVO.bbsId }'/>">
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

	<script type="text/javaScript" defer="defer">
	
		//목록
		function fn_listView() {
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>.do";
			document.detailForm.submit();
		}
		
		//상세보기
		function fn_detailView(sId) {
			document.detailForm.sId.value = sId;
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>/view.do";
			document.detailForm.submit();
		}
		
		//등록
		function fn_registerView() {
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>/register.do";
			document.detailForm.submit();
		}
		
		//답변
		function fn_replyView() {
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>/reply.do";
			document.detailForm.submit();
		}
		
		//수정
		function fn_modifyView() {
			document.detailForm.action = "/board/<c:out value='${boardPath}'/>/modify.do";
			document.detailForm.submit();
		}
		
		//삭제
		function fn_removeView() {
			var msg = confirm('<spring:message code="message.confirm.remove" />');
			if(msg == true){
				document.detailForm.action = "/board/<c:out value='${boardPath}'/>/removeAction.do";
				document.detailForm.submit();
			}
		}
		
	</script>
</div>