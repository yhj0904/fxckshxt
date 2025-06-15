<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 프로그램 수강생 관리 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.16		신한나			최초생성
 */
%>

<form id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<div>
		<table class="detail_table">
			<caption>프로그램 수강생 상세조회</caption>
			<colgroup>
		  		<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
		  	</colgroup>
		  	<tbody>
		  		<tr>
		  			<th>프로그램 명</th>
		  			<td colspan="3">
		  				<c:out value="${progUser.PROG_NM }" escapeXml="false"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>이름</th>
		  			<td colspan="3">
		  				<c:out value="${progUser.USER_NM }" escapeXml="false"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>신청상태</th>
		  			<td>
		  				<c:out value="${progUser.PROG_REQST_CD_NM}"/>
		  			</td>
		  		</tr>
		  		<c:choose>
	  				<c:when test="${!empty progUser.REQST_CNCL_DT and (progUser.PROG_REQST_CD eq 'PROG_REQST_090') }">
	  					<th>신청취소일</th>
			  			<td>
			  				<c:out value="${progUser.REQST_CNCL_DT}"/>
			  			</td>
	  				</c:when>
	  				<c:when test="${progUser.PROG_REQST_CD eq 'PROG_REQST_010' }">
	  					<th>신청일</th>
			  			<td>
			  				<c:out value="${progUser.INPT_DTTM}"/>
			  			</td>
	  				</c:when>
  				</c:choose>
		  		<tr>
		  			<th>교육방법</th>
		  			<td colspan="3">
		  				<c:out value="${progUser.PROG_MTH_CD_NM }"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>교육일자</th>
		  			<td>
		  				<c:out value="${progUser.PROG_SDT} ~ ${progUser.PROG_EDT}"/>
		  			</td>
		  			<th>교육시간</th>
		  			<td>
		  				<c:out value="${progUser.PROG_STM} ~ ${progUser.PROG_ETM}"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>휴대전화번호</th>
		  			<td>
		  				<c:out value="${progUser.MBPH_NO }"/>
		  			</td>
		  			<th>이메일</th>
		  			<td>
		  				<c:out value="${progUser.EMAIL }"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>소속학과</th>
		  			<td>
		  				<c:out value="${progUser.DEPT_NM }"/>
		  			</td>
		  			<th>학번</th>
		  			<td>
		  				<c:out value="${progUser.STD_NO }"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>성별</th>
		  			<td>
		  				<c:out value="${progUser.SEX_CD }"/>
		  			</td>
		  			<th>회원분류</th>
		  			<td>
		  				<c:out value="${progUser.USER_TYPE_NM }"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>만족도 조사 참여여부</th>
		  			<td>
		  				<c:out value="${progUser.SURVEY_YN_NM }"/>
		  			</td>
		  			<th>교육이수 여부</th>
		  			<td>
		  				<c:out value="${progUser.COMPL_NM }"/>
		  			</td>
		  		</tr>
		  	</tbody>
		</table>
	</div>
	
	<div class="btn_wrap">
		<ul>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${progUser.PROG_ID }'/>">
  	<input type="hidden" name="userId" value="<c:out value='${progUser.USER_ID }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
</script>