<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 컨텐츠관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="siteCd"/>
	<form:hidden path="siteNm"/>
	<form:hidden path="templateCd"/>
	
	<table class="detail_table">
		<caption>사이트 코드 수정</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="siteVO.siteCd"/></th>
			<td>				
				<c:out value='${siteVO.siteCd }'/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="siteVO.siteNm"/></th>
			<td>				
				<c:out value='${siteVO.siteNm }'/>
			</td>
		</tr>
		<tr>
			<th><label for="indexCode"><spring:message code="siteVO.indexCode"/></label></th>
			<td>				
				<form:textarea path="indexCode"/>
				<span class="form_error" data-path="indexCode"><form:errors path="indexCode"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="indexCss"><spring:message code="siteVO.indexCss"/></label></th>
			<td>				
				<form:textarea path="indexCss"/>
				<span class="form_error" data-path="indexCss"><form:errors path="indexCss"/></span>
			</td>
		</tr>
	</table>
	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a>
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
   	<input type="hidden" name="sId" value="<c:out value='${siteVO.siteCd}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="siteVO" staticJavascript="false" xhtml="true" cdata="false"/>
<c:import url="/WEB-INF/jsp/cmmn/code_mirror.jsp"></c:import>
<script type="text/javaScript" defer="defer">

	var indexCode;
	var indexCss;
	
	$(function(){		
		indexCode = ComCodeMirror.edit($("#indexCode"));
		indexCss = ComCodeMirror.edit($("#indexCss"));		
	});
	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validateSiteVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/codeAction.do";
			frm.submit();
		}		
	}
	
	//취소
	function fn_detailView() {
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
</script>