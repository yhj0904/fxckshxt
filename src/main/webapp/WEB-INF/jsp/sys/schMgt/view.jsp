<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 일정관리 상세조회 화면
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
		<caption>일정관리 상세조회</caption>
		<colgroup>
			<col width="150" />
			<col width="?" />
		</colgroup>
		<tr>
			<th><spring:message code="schMgtVO.code" /></th>
			<td>
				<c:out value="${schMgtVO.code}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.title" /></th>
			<td>
				<c:out value="${schMgtVO.title}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.header" /></th>
			<td>
				<c:out value="${schMgtVO.header}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.footer" /></th>
			<td>
				<c:out value="${schMgtVO.footer}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.typeCd" /></th>
			<td>
				<c:out value="${schMgtVO.typeCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.skinCd" /></th>
			<td>
				<c:out value="${schMgtVO.skinCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.fileYn" /></th>
			<td>
				<c:out value="${schMgtVO.fileYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.fileCnt" /></th>
			<td>
				<c:out value="${schMgtVO.fileCnt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.fileSize" /></th>
			<td>
				<c:out value="${schMgtVO.fileSize}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.fileExt" /></th>
			<td>
				<c:out value="${schMgtVO.fileExt}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.cateYn" /></th>
			<td>
				<c:out value="${schMgtVO.cateYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.category" /></th>
			<td>
				<c:out value="${schMgtVO.category}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.supplYn"/></th>
			<td><c:out value="${schMgtVO.supplYn}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl01Title"/></th>
			<td><c:out value="${schMgtVO.suppl01Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl02Title"/></th>
			<td><c:out value="${schMgtVO.suppl02Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl03Title"/></th>
			<td><c:out value="${schMgtVO.suppl03Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl04Title"/></th>
			<td><c:out value="${schMgtVO.suppl04Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl05Title"/></th>
			<td><c:out value="${schMgtVO.suppl05Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl06Title"/></th>
			<td><c:out value="${schMgtVO.suppl06Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl07Title"/></th>
			<td><c:out value="${schMgtVO.suppl07Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl08Title"/></th>
			<td><c:out value="${schMgtVO.suppl08Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl09Title"/></th>
			<td><c:out value="${schMgtVO.suppl09Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.suppl10Title"/></th>
			<td><c:out value="${schMgtVO.suppl10Title}"/></td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.inptId" /></th>
			<td>
				<c:out value="${schMgtVO.inptId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.inptIp" /></th>
			<td>
				<c:out value="${schMgtVO.inptIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.inptDttm" /></th>
			<td>
				<c:out value="${schMgtVO.inptDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.modiId" /></th>
			<td>
				<c:out value="${schMgtVO.modiId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.modiIp" /></th>
			<td>
				<c:out value="${schMgtVO.modiIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.modiDttm" /></th>
			<td>
				<c:out value="${schMgtVO.modiDttm}" />
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
  	<input type="hidden" name="sId" value="<c:out value='${schMgtVO.code }'/>">
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