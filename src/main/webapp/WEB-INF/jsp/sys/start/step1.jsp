<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: step1.jsp
 * @Description : 최초 관리자 등록
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="sys_start" style="padding:50px;">
	<div class="start_title">
		<p>관리자 최초 등록 화면입니다.</p>
	</div>
	<div class="start_content">
		<form:form modelAttribute="userVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<form:hidden path="useYn"/>
			<form:hidden path="authCd"/>
			
			<table class="detail_table">
				<caption>사용자 등록</caption>
				<colgroup>
		   			<col width="150"/>
		   			<col width="?"/>
		   		</colgroup>
				<tr>
					<th><label for="userId"><spring:message code="userVO.userId" /></label></th>
					<td>
						<form:input path="userId"/>
		       			<span class="form_error" data-path="userId"><form:errors path="userId"/></span>
					</td>
				</tr>
				<tr>
					<th><label for="password"><spring:message code="userVO.password" /></label></th>
					<td>
						<form:password path="password"/>
		       			<span class="form_error" data-path="password"><form:errors path="password"/></span>
					</td>
				</tr>
				<tr>
					<th><label for="passwordCheck"><spring:message code="userVO.passwordCheck" /></label></th>
					<td>
						<form:password path="passwordCheck"/>
		       			<span class="form_error" data-path="passwordCheck"><form:errors path="passwordCheck"/></span>
					</td>
				</tr>
				<tr>
					<th><label for="userNm"><spring:message code="userVO.userNm" /></label></th>
					<td>
						<form:input path="userNm"/>
		       			<span class="form_error" data-path="userNm"><form:errors path="userNm"/></span>
					</td>
				</tr>
				<tr>
					<th><label for="note"><spring:message code="userVO.note" /></label></th>
					<td>
						<form:input path="note" cssClass="w_full"/>
						<span class="form_error" data-path="note"><form:errors path="note" /></span>
					</td>
				</tr>
			</table>
		   	
		   	<div class="btn_wrap">
				<ul>
					<li>
						<a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a>
					</li>
				</ul>
			</div>
			
		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
			
		</form:form>
	</div>
	
	<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
	<validator:javascript formName="userVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<% /** RSA 관련 import */ %>
	<script type="text/javascript" src="/js/common/rsa/rsa.js"></script>
	<script type="text/javascript" src="/js/common/rsa/jsbn.js"></script>
	<script type="text/javascript" src="/js/common/rsa/prng4.js"></script>
	<script type="text/javascript" src="/js/common/rsa/rng.js"></script>
	<% /** //RSA 관련 import */ %>
	<script type="text/javaScript" defer="defer">
		
		//등록 처리
		function fn_registerAction() {
			frm = document.detailForm;
			if (!validateUserVO(frm)) {
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
						//RSA로 비밀번호 암호화
						if(gf_encryptRSA(rsa, $("#password")) && gf_encryptRSA(rsa, $("#passwordCheck"))) {
							frm.action = "/sys/start/step1Action.do";
							frm.submit();
						} else { //암호화 실패시
							alert("Error Occur");
						}
					} else {
						alert("Error Occur");
					}
				});
			}
		}		
	</script>
</div>