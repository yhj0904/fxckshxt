<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: find_pw_result.jsp
 * @Description : 비밀번호 찾기
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="find_wrap">	
	<div class="find_title">
		<p><spring:message code="findUser.pw"/></p>
	</div>
	<div class="find_result">
		<p>비밀번호가 변경되었습니다.</p>
		<div class="result_btn">
			<ul>
				<li><a href="/login.do" class="button">로그인하기</a></li>
			</ul>
		</div>
	</div>
</div>