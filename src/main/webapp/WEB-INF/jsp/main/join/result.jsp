<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: result.jsp
 * @Description : 회원가입결과
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			css 변경
 */
%>
<div class="join_wrap">
	<!-- <div class="join_title">
		<h2>회원가입</h2>
	</div>	 -->
	<div class="join_step">
		<ul>
			<li>
				<img src="/images/common/join_step1_off.png" alt="개인정보 이용약관 동의"/>
				<span>약관동의</span>								
			</li>
			<li>
				<img src="/images/common/join_step2_off.png" alt="회원정보 입력"/>
				<span>정보입력</span>
			</li>
			<li>
				<img src="/images/common/join_step3_on.png" alt="(현재) 회원가입 완료"/>
				<span>가입완료</span>
			</li>
		</ul>
	</div>	
	<div class="join_content">
		<div class="join_result">
			<h2>환영합니다!</h2>
			<p>
				<b><c:out value='${successUser.userNm }'/></b>님, 회원가입을 축하합니다.<br/>
				회원님의 아이디는 <b><c:out value='${successUser.userId }'/></b>입니다.
				<c:if test="${successUser.useYn eq 'N' }"><br/>관리자 승인 후 홈페이지를 이용하실 수 있습니다.</c:if>
			</p>
		</div>
		<div class="btn_wrap">
			<ul>
				<li>
					<a href="/">메인으로</a>
				</li>
				<li>
					<a class="agree" href="/login.do">로그인</a>
				</li>
			</ul>
		</div>
	</div>
	<script type="text/javaScript" defer="defer">
	
	</script>
</div>