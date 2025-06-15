<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="kr.co.nanwe.dept.service.DeptService" %>
<%@ page import="kr.co.nanwe.dept.service.DeptVO" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>
<%@ page import="kr.co.nanwe.user.service.UserService" %>
<%@ page import="kr.co.nanwe.user.service.UserVO" %>
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
<form:form commandName="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="cnslId"/>
	
	<p style="font-size: 24px; font-weight: bold; padding-bottom: 20px;">1. 요청자 정보</p>
	<table class="detail_table">
		<caption>게시글 상세조회</caption>
		<colgroup>
   			<col width="200"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th>상담 신청자(아이디/학번)</th>
			<td><c:out value="${cnslerVO.userNm }" />( <c:out value="${cnslerVO.userId }" /> / <c:out value="${cnslerVO.stdNo }" /> )</td>
		</tr>
		<tr>
			<th>신분구분</th>
			<c:set var="userTypeCd" value="${cnslerVO.userTypeCd}" />
			<%
				CommCdService commCdService = (CommCdService) webApplicationContext.getBean("commCdService");
			
				String userTypeCd = (String) pageContext.getAttribute("userTypeCd");
				CommCdVO userTypeCdVO = commCdService.selectCommCd(userTypeCd);
				pageContext.setAttribute("userTypeCdVO", userTypeCdVO);
			%>
			<td><c:out value="${userTypeCdVO.cdNm }" /></td>
		</tr>
		<c:set var="colgCd" value="${cnslerVO.cnslColgCd}" />
		<c:set var="deptCd" value="${cnslerVO.cnslDeptCd}" />
		<%
			DeptService deptService = (DeptService) webApplicationContext.getBean("deptService");
		
			//학부 이름
			String colgCd = (String) pageContext.getAttribute("colgCd");
			DeptVO colgCdVO = deptService.selectDept(colgCd);
			pageContext.setAttribute("colgCdVO", colgCdVO);
			
			//학과 이름
			String deptCd = (String) pageContext.getAttribute("deptCd");
			DeptVO deptCdVO = deptService.selectDept(deptCd);
			pageContext.setAttribute("deptCdVO", deptCdVO);
		%>
		<tr>
			<th>신청자 학부</th>
			<td>
				<c:out value="${colgCdVO.deptNmKor }" />
			</td>
		</tr>
		<tr>
			<th>신청자 학과</th>
			<td>
				<c:out value="${deptCdVO.deptNmKor }" />
			</td>
		</tr>
		<tr>
			<th>학년</th>
			<td>
				<c:out value="${cnslerVO.grade }" /> 학년
			</td>
		</tr>
		<tr>
			<th>성별</th>
			<td><c:out value="${cnslerVO.sexCd }" /></td>
		</tr>
		<tr>
			<th><spring:message code="userVO.mbphNo" /></th>
			<td>
		        <c:set var="phoneNumber" value="${userVO.mbphNo}" />
		        <c:out value="${fn:substring(phoneNumber, 0, 3)}"/>-
		        <c:out value="${fn:substring(phoneNumber, 3, 7)}"/>-
		        <c:out value="${fn:substring(phoneNumber, 7, 11)}"/>
		    </td>
		</tr>
  	</table>
  	
  	<p style="font-size: 24px; font-weight: bold; padding: 20px 0;">2. 상담신청 내용</p>
  	<table class="detail_table">
  		<colgroup>
   			<col width="200"/>
   			<col width="?"/>
   		</colgroup>
  		<tr>
			<th>상담 요청 선생님(아이디)</th>
			<td><c:out value="${cnslerVO.cnslerNm }" />(<c:out value="${cnslerVO.cnslerId }" />)</td>
		</tr>
		<tr>
			<th>상담분야</th>
			<td>
				<ul>
					<c:if test="${cnslerVO.cnslTypeTrack eq 'Y'}">
						<li>진로</li>
					</c:if>
					<c:if test="${cnslerVO.cnslTypeJob eq 'Y'}">
						<li>취업</li>
					</c:if>
					<c:if test="${cnslerVO.cnslTypeLife eq 'Y'}">
						<li>생활</li>
					</c:if>
					<c:if test="${cnslerVO.cnslTypeResume eq 'Y'}">
						<li>입사지원서</li>
					</c:if>
					<c:if test="${cnslerVO.cnslTypeIntv eq 'Y'}">
						<li>모의면접</li>
					</c:if>
				</ul>
			</td>
		</tr>
		<tr>
			<th>상담장소</th>
			<td><c:out value="${cnslerVO.cnslPlace }" /></td>
		</tr>
		<tr>
			<th>상담요청 날짜</th>
			<td><c:out value="${cnslerVO.hopeDt }" /></td>
		</tr>
		<tr>
			<th>상담요청 시간</th>
			<td><c:out value="${cnslerVO.hopeTm }" /></td>
		</tr>
  	</table>
  	
  	<div class="btn_wrap">
		<ul>
			<%-- <li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
			<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li> --%>
			<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${cnslerVO.cnslId }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//상세보기
	function fn_detailView(sId) {
		document.detailForm.sId.value = sId;
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
		document.detailForm.submit();
	}
	
	//답변
	function fn_replyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/reply.do";
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