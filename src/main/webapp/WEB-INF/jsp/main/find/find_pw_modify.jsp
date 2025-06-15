<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: find_id.jsp
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
		<p>비밀번호변경</p>
	</div>
	<ul class="find_list">
		<li>
			<form id="detailForm" name="detailForm" method="post" action="/find/userPwResult.do">
				<input type="hidden" name="userType" value="<c:out value='${userType }'/>">
				<input type="hidden" name="userId" value="<c:out value='${userId }'/>">
				<table class="detail_table">
					<tr>
						<th><label for="inputPassword">새 비밀번호</label></th>
						<td>
							<input type="password" id="inputPassword">
							<input type="password" id="password" name="password" style="display:none;">
						</td>
					</tr>
					<tr>
						<th><label for="inputPasswordCheck">새 비밀번호 확인</label></th>
						<td>
							<input type="password" id="inputPasswordCheck">
							<input type="password" id="passwordCheck" name="passwordCheck" style="display:none;">
						</td>
					</tr>
				</table>
				<div class="find_btn">
					<a href="javascript:fn_changeUserPwAction()" class="button">비밀번호 변경</a>
				</div>
				
				<% /** 이중방지 토큰 */ %>
				<double-submit:preventer/>
				
			</form>
		</li>
	</ul>
</div>
<% /** RSA 관련 import */ %>
<script type="text/javascript" src="/js/common/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/common/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/common/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/common/rsa/rng.js"></script>
<% /** //RSA 관련 import */ %>
<script>
	function fn_changeUserPwAction(){
		//공백체크
		var password = $("#inputPassword").val();
		var passwordCheck = $("#inputPasswordCheck").val();
		if(gf_isNull(password)){
			alert("새 비밀번호를 입력해주세요.");
			return;
		} else if(gf_isNull(passwordCheck)){
			alert("새 비밀번호 확인을 입력해주세요.");
			return;				
		} else {
			gf_ajax({
				url : "/user/initRsa.json",
				type : "POST",
			}).then(function(response){
				if(response != null){
					var rsaModulus = response[0];
					var rsaExponent = response[1];
					var rsa = gf_initRSA(rsaModulus, rsaExponent);
					
					$("#password").val(password);
					$("#passwordCheck").val(passwordCheck);
					
					//RSA로 비밀번호 암호화
					if(gf_encryptRSA(rsa, $("#password")) && gf_encryptRSA(rsa, $("#passwordCheck"))) {
						fn_validPassword();
					} else { //암호화 실패시
						alert("Error Occur");
					}
				} else {
					alert("Error Occur");
				}
			});
		}
	}
	
	function fn_validPassword(){
		gf_ajax({
			url : "/find/checkPw.json",
			type : "POST",
			data : { password : $("#password").val(), passwordCheck : $("#passwordCheck").val()}
		}).then(function(response){
			if(response != null){
				if(response.result){
					$("#detailForm").submit();
				} else {
					alert(response.errMsg);
					return;
				}
			} else {
				alert("Error Occur");
			}
		});
	}
</script>