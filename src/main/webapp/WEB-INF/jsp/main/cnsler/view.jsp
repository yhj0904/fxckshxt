<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="kr.co.nanwe.dept.service.DeptService" %>
<%@ page import="kr.co.nanwe.dept.service.DeptVO" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>
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
	
	<div class="board_cont">
		<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/main/board/${GV_BOARD_SKIN_CODE }/register.jsp"></c:import>
			</c:if>
		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="">
		   	
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>
		  	
		  	<table class="detail_table cnsler_t">
		  		<!--<colgroup>
		  			<col style="width: 20%">
		  			<col style="width: 30%">
		  			<col style="width: 20%">
		  			<col style="width: 30%">
		  		</colgroup>-->
		  		<tr>
		  			<th>신청자</th>
		  			<td colspan="3">
		  				<input type="text" id="userId" name="userId" value="${cnslerVO.userNm }" readonly="readonly" />
		  			</td>
		  		</tr>
		  		<tr>
					<th>상담분야</th>
					<td colspan="3">
						<label class="cnslLabel"><input type="checkbox" name="cnslTypeTrack" onClick="return false;" value="Y" <c:if test="${cnslerVO.cnslTypeTrack eq 'Y'}">checked</c:if> /> 진로</label>
						<label class="cnslLabel"><input type="checkbox" name="cnslTypeJob" onClick="return false;" value="Y" <c:if test="${cnslerVO.cnslTypeJob eq 'Y'}">checked</c:if> /> 취업</label>
						<label class="cnslLabel"><input type="checkbox" name="cnslTypeLife" onClick="return false;" value="Y" <c:if test="${cnslerVO.cnslTypeLife eq 'Y'}">checked</c:if> /> 생활</label>
						<label class="cnslLabel"><input type="checkbox" name="cnslTypeResume" onClick="return false;" value="Y" <c:if test="${cnslerVO.cnslTypeResume eq 'Y'}">checked</c:if> /> 입사지원서</label>
						<label class="cnslLabel"><input type="checkbox" name="cnslTypeIntv" onClick="return false;" value="Y" <c:if test="${cnslerVO.cnslTypeIntv eq 'Y'}">checked</c:if> /> 모의면접</label>
					</td>
				</tr>
		  		<tr>
		  			<th>단과대학</th>
		  			<td>
		  				<c:set var="cnslColgCd" value="${cnslerVO.cnslColgCd}" />
						<%
							DeptService deptService = (DeptService) webApplicationContext.getBean("deptService");
							
							//학부 이름
							String cnslColgCd = (String) pageContext.getAttribute("cnslColgCd");
							DeptVO colgCdVO = deptService.selectDept(cnslColgCd);
							pageContext.setAttribute("colgCdVO", colgCdVO);
						%>
						<c:out value="${colgCdVO.deptNmKor }" />
		  			</td>
		  			<th>상담방법</th>
		  			<td>
		  				<c:set var="cnslMthCd" value="${cnslerVO.cnslMthCd}" />
						<%
							CommCdService commCdService = (CommCdService) webApplicationContext.getBean("commCdService");
						
							String cnslMthCd = (String) pageContext.getAttribute("cnslMthCd");
							CommCdVO cnslMthCdVO = commCdService.selectCommCd(cnslMthCd);
							pageContext.setAttribute("cnslMthCdVO", cnslMthCdVO);
						%>
						<c:out value="${cnslMthCdVO.cdNm}"/>
		  			</td>
		  		 </tr>
		  		 <tr>
		  			<th>이메일/카톡아이디</th>
		  			<td colspan="3">
		  				<c:out value="${cnslerVO.chatId}"/>
		  			</td>
		  		</tr>
		  		 <tr>
		  			<th>상담날짜</th>
		  			<td>
		  				<c:out value="${cnslerVO.hopeDt }" />
		  			</td>
		  			<th>상담시간</th>
		  			<td>
		  				<c:out value="${fn:substring(cnslerVO.hopeTm, 0, 2) }"/>:<c:out value="${fn:substring(cnslerVO.hopeTm, 2, 4) }"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>상담선생님</th>
		  			<td>
		  				<c:out value="${cnslerVO.cnslerNm }" />
		  			</td>
		  			<th>상담장소</th>
		  			<td>
		  				<c:out value="${cnslerVO.cnslPlace }" />
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>상담시 참고사항</th>
		  			<td colspan="3">
		  				<c:out value="${cnslerVO.reqstText }" />
		  			</td>
		  		</tr>
		  	</table>
		  	
		  	
		  	<div class="btn_wrap">
				<ul>
					<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
					<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
				</ul>
			</div>
		</form:form>
	</div>
	
	<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
	<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
	<script type="text/javaScript" defer="defer">
	
		//목록
		function fn_listView() {
			document.detailForm.action = "/cnsler/list.do";
			document.detailForm.submit();
		}
		
		//등록
		function fn_registerView() {
			document.detailForm.action = "/cnsler/register.do";
			document.detailForm.submit();
		}
	</script>

</div>