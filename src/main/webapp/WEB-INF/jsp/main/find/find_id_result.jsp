<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: find_id_result.jsp
 * @Description : 아이디 찾기
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="find_wrap">	
	<div class="find_title">
		<p><spring:message code="findUser.id"/></p>
	</div>
	<div class="find_result">
		<c:choose>
			<c:when test="${result.result }">
				<p><spring:message code="findUser.result.id" arguments="${result.id }"/></p>
				<ul>
					<%-- <c:if test='${result.phone ne null }'>
						<li>
							<span>휴대폰 <c:out value="${result.phone}"/></span>
							<a href="javascript:fn_sendUserId('phone')" class="button">아이디발송</a>				
						</li>
					</c:if> --%>
					<c:if test='${result.email ne null }'>
						<li>
							<span>이메일 <c:out value="${result.email}"/></span>
							<a href="javascript:fn_sendUserId('email')" class="button">아이디발송</a>				
						</li>
					</c:if>
				</ul>
			</c:when>
			<c:otherwise>
				<p>입력한 정보와 일치하는 아이디가 존재하지 않습니다.</p>
			</c:otherwise>
		</c:choose>
		<div class="result_btn">
			<ul>
				<li><a href="/login.do" class="button">로그인하기</a></li>
				<li><a href="/find/userPw.do" class="button">비밀번호 찾기</a></li>
			</ul>
		</div>
	</div>
</div>
<form id="sendUserForm" name="sendUserForm">
	<input type="hidden" id="sendType" name="sendType" value=""/>
	<c:forEach var="item" items="${param}">
		<input type="hidden" name="<c:out value="${item.key}"/>" value="<c:out value="${item.value}"/>"/>
	</c:forEach>
</form>
<script>
	function fn_sendUserId(type){
		$("#sendType").val(type);
		var data = $("#sendUserForm").serializeObject();
		gf_ajax({
			url : "/find/sendUserId.json",
			type : "POST",
			data : data,
			contentType :  "application/json",
		}).then(function(response){
			if(response){
				alert("아이디가 발송되었습니다.");
			} else {
				alert("아이디 발송에 실패하였습니다.");
			}
			location.href="/login.do";
			return;
		});
	}
</script>