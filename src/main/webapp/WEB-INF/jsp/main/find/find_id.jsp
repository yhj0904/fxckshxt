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
		<p><spring:message code="findUser.id"/></p>
	</div>
	<ul class="find_list">
		<%-- <c:if test='${findType eq "BOTH" or findType eq "PHONE" }'>
			<li>
				<div class="find_radio">
					<label class="radio">
						<input type="radio" name="findType" value="phone" <c:if test='${findType eq "BOTH" or findType eq "PHONE" }'>checked</c:if>><i></i>
						휴대폰으로 찾기
					</label>
				</div>
				<div class="find_input <c:if test='${findType eq "BOTH" or findType eq "PHONE" }'>active</c:if>" data-id="phone" >
					<table class="detail_table">
						<colgroup>
							<col style="width: 150px;"/> <!--  -->
							<col style=""/> <!--  -->
						</colgroup>
						<tr>
							<th><label for="phone_name">이름</label></th>
							<td><input type="text" id="phone_name" placeholder="<spring:message code="joinVO.userNm"/>"/></td>
						</tr>
						<tr>
							<th><label for="phone_birth">생년<br>월일</label></th>
							<td><input type="text" id="phone_birth" class="input_date" onclick="gf_datepicker(this);" placeholder="<spring:message code="joinVO.birth"/>" /></td>
						</tr>
						<tr>
							<th><label for="phone_phone">휴대폰<br>번호</label></th>
							<td>
								<div class="tel">
				  					<c:import url="/WEB-INF/jsp/cmmn/data_phone.jsp">
				  						<c:param name="path" value="phone_phone"/>
				   					</c:import>
				   				</div>
				   				<input type="hidden" id="phone_phone"/>
							</td>
						</tr>
					</table>
					<div class="find_btn">
						<a href="javascript:fn_findUserIdAction('phone')" class="button">아이디 찾기</a>
					</div>
				</div>
			</li>
		</c:if> --%>
		<c:if test='${findType eq "BOTH" or findType eq "EMAIL" }'>
			<li>
				<div class="find_radio">
					<label class="radio">
						<input type="radio" name="findType" value="email" <c:if test='${findType ne "BOTH" and findType eq "EMAIL" }'>checked</c:if>><i></i>
						이메일로 찾기
					</label>
				</div>
				<div class="find_input <c:if test='${findType ne "BOTH" and findType eq "EMAIL" }'>active</c:if>" data-id="email" >
					<table class="detail_table">
						<tr>
							<th><label for="email_name">이름</label></th>
							<td><input type="text" id="email_name" placeholder="<spring:message code="joinVO.userNm"/>" /></td>
						</tr>
						<%-- <tr>
							<th><label for="email_birth">생년<br>월일</label></th>
							<td><input type="text" id="email_birth" class="input_date hasDatepicker" onclick="gf_datepicker(this);" placeholder="<spring:message code="joinVO.birth"/>" /></td>
						</tr> --%>
						<tr>
							<th><label for="email_email">이메일</label></th>
							<td>
								<div class="email">
				  					<c:import url="/WEB-INF/jsp/cmmn/data_email.jsp">
				  						<c:param name="path" value="email_email"/>
				   					</c:import>
				   				</div>
				   				<input type="hidden" id="email_email"/>
							</td>
						</tr>
					</table>
					<div class="find_btn">
						<a href="javascript:fn_findUserIdAction('email')" class="button">아이디 찾기</a>
					</div>
				</div>
			</li>
		</c:if>
	</ul>
</div>
<form id="findUserForm" name="findUserForm" method="post" action=""></form>
<script>
	$("input[name='findType']").click(function(){
		var findType = $(this).val();
		$(".find_input").removeClass('active');
		$(".find_input[data-id='"+findType+"']").addClass('active');
		
	})
	function fn_findUserIdAction(type) {
		var alertMsg = "";
		$("#findUserForm")[0].reset();
		$("#findUserForm").html("");
		
		var name = "";
		var birth = "";
		var phone = "";
		var email = "";
		
		if(type == "phone"){
			name = $("#phone_name").val();
			//birth = $("#phone_birth").val();
			phone = $("#phone_phone").val();
			if(gf_isNull(name)){
				alert("이름을 입력해주세요.");
				$("#phone_name").focus();
				return;
			} /*else if(gf_isNull(birth)){
				alert("생년월일을 입력해주세요.");
				$("#phone_birth").focus();
				return;			
			} */else if(gf_isNull(phone)){
				alert("휴대폰번호를 입력해주세요.");
				$("#phone_phone").focus();
				return;			
			}
		} else if(type == "email"){
			name = $("#email_name").val();
			//birth = $("#email_birth").val();
			email = $("#email_email").val();
			if(gf_isNull(name)){
				alert("이름을 입력해주세요.");
				$("#email_name").focus();
				return;
			} /*else if(gf_isNull(birth)){
				alert("생년월일을 입력해주세요.");
				$("#email_birth").focus();
				return;			
			} */else if(gf_isNull(email)){
				alert("이메일을 입력해주세요.");
				$("#email_phone").focus();
				return;			
			}
		} else {
			alert("잘못된 요청입니다.");
			return false;
		}	
		
		$("#findUserForm").append('<input type="hidden" name="name" value="'+name+'"/>');
		//$("#findUserForm").append('<input type="hidden" name="birth" value="'+birth+'"/>');
		if(type == "phone"){
			$("#findUserForm").append('<input type="hidden" name="phone" value="'+phone+'"/>');
		} else if(type == "email"){
			$("#findUserForm").append('<input type="hidden" name="email" value="'+email+'"/>');
		}		
		$("#findUserForm").attr('action', "/find/userIdResult.do");
		$("#findUserForm").submit();
	}
</script>
<style>
.find_wrap .find_list li .find_input{display: block;}
</style>