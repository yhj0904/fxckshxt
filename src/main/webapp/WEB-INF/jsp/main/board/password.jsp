<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: password.jsp
 * @Description : 게시글 암호 입력
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
		<form id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data" action="<c:out value='${formAction }'/>">			
			<table class="detail_table">
				<colgroup>
			 		<col width="150"/>
			 		<col width="?"/>
			 	</colgroup>
				<tr>
					<th><label for="bbsPw"><spring:message code="bbsVO.pw" /></label></th>
					<td><input type="text" id="bbsPw" name="bbsPw" class="w_full"/></td>
				</tr>
			</table>
   	
			<div class="btn_wrap">
				<ul>
					<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.input" /></a></li>
					<li><a class="button list_btn" href="javascript:fn_listView('<c:out value="${sId}"/>');"><spring:message code="button.list" /></a></li>
				</ul>
			</div>
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
			<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="<c:out value='${sId }'/>">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>
		</form>
	</div>
	
	<c:if test='${bbsMgtVO.footer ne null and bbsMgtVO.footer ne ""}'>
		<div class="board_bottom">
			<c:out value="${bbsMgtVO.footer }" escapeXml="false"/>
		</div>
	</c:if>
	
	<script type="text/javaScript" defer="defer">
		//목록
		function fn_listView(sId) {
			document.detailForm.action = "";
			if(sId.indexOf('LABR') != -1) {
				document.detailForm.action = "/labr/list.do";
			} else if (sId.indexOf('CNSL') != -1) {
				document.detailForm.action = "/cnsler/list.do";
			} else {
				document.detailForm.action = "/board/<c:out value='${boardPath}'/>.do";
			}
			document.detailForm.submit();
		}
		
		//등록 처리
		function fn_registerAction() {
			if(gf_isNull($("#bbsPw").val())){
				alert("게시글 비밀번호를 입력해주세요.");
				$("#bbsPw").focus();
				return;
			} else {
				document.detailForm.submit();
			}
		}
	</script>
</div>