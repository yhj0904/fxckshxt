<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 게시판관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="code"/>

	<table class="detail_table">
		<caption>게시판관리 상세조회</caption>
		<colgroup>
			<col width="150" />
			<col width="?" />
		</colgroup>
		<tr>
			<th><spring:message code="bbsMgtVO.code" /></th>
			<td>
				<c:out value="${bbsMgtVO.code}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.title" /></th>
			<td>
				<c:out value="${bbsMgtVO.title}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.header" /></th>
			<td>
				<c:out value="${bbsMgtVO.header}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.footer" /></th>
			<td>
				<c:out value="${bbsMgtVO.footer}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.skinCd" /></th>
			<td>
				<c:out value="${bbsMgtVO.skinCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.pageCnt" /></th>
			<td>
				<c:out value="${bbsMgtVO.pageCnt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.rowCnt" /></th>
			<td>
				<c:out value="${bbsMgtVO.rowCnt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.fileYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.fileYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.fileCnt" /></th>
			<td>
				<c:out value="${bbsMgtVO.fileCnt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.fileSize" /></th>
			<td>
				<c:out value="${bbsMgtVO.fileSize}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.fileExt" /></th>
			<td>
				<c:out value="${bbsMgtVO.fileExt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.cateYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.cateYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.category" /></th>
			<td>
				<c:out value="${bbsMgtVO.category}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.replyYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.replyYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.cmntYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.cmntYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.editorYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.editorYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.editorFileYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.editorFileYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.noticeYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.noticeYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.noticeRowCnt" /></th>
			<td>
				<c:out value="${bbsMgtVO.noticeRowCnt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.secretYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.secretYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.nonameYn" /></th>
			<td>
				<c:out value="${bbsMgtVO.nonameYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.supplYn"/></th>
			<td><c:out value="${bbsMgtVO.supplYn}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl01Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl01Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl02Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl02Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl03Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl03Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl04Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl04Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl05Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl05Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl06Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl06Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl07Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl07Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl08Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl08Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl09Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl09Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.suppl10Title"/></th>
			<td><c:out value="${bbsMgtVO.suppl10Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.inptId" /></th>
			<td>
				<c:out value="${bbsMgtVO.inptId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.inptIp" /></th>
			<td>
				<c:out value="${bbsMgtVO.inptIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.inptDttm" /></th>
			<td>
				<c:out value="${bbsMgtVO.inptDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.modiId" /></th>
			<td>
				<c:out value="${bbsMgtVO.modiId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.modiIp" /></th>
			<td>
				<c:out value="${bbsMgtVO.modiIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bbsMgtVO.modiDttm" /></th>
			<td>
				<c:out value="${bbsMgtVO.modiDttm}" />
			</td>
		</tr>
	</table>

	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${bbsMgtVO.code }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
		document.detailForm.submit();
	}
	
	//수정
	function fn_modifyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/modify.do";
		document.detailForm.submit();
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}
	
</script>