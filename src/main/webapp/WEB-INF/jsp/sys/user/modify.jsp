<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 사용자 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */
%>
<form:form modelAttribute="userVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">

	<table class="detail_table">
		<caption>사용자 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
   		<tr>
			<th><label for="uploadFile"><spring:message code="text.image" /></label></th>
			<td>
				<c:choose>
   					<c:when test="${userVO.viewFile ne null}">
   						<img id="userFileImg" src="<c:out value='${userVO.viewFile.viewUrl }'/>" alt="<c:out value='${userVO.viewFile.oname }'/>">
   					</c:when>
   					<c:otherwise>
   						<img id="userFileImg" src="/images/common/no_img.png" alt="NO IMG">
   					</c:otherwise>
   				</c:choose>
   				<div>
    				<label class="file">
    					<c:choose>
		   					<c:when test="${userVO.viewFile ne null}">
		   						<span class="name"><c:out value='${userVO.viewFile.oname }'/></span>
		   					</c:when>
		   					<c:otherwise>
		   						<span class="name"><spring:message code="text.file.placeholder" /></span>
		   					</c:otherwise>
		   				</c:choose>    					
    					<form:input type="file" path="uploadFile" data-img="userFileImg"/>
    					<span class="btn"><spring:message code="button.upload" /></span>
    				</label>
   				</div>
				<span class="form_error" data-path="uploadFile"><form:errors path="uploadFile" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="userId"><spring:message code="userVO.userId" /></label></th>
			<td>
				<c:out value="${userVO.userId }"/>
				<form:hidden path="userId"/>
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
			<th><label for="authCd"><spring:message code="userVO.authCd" /></label></th>
			<td>
				<form:select path="authCd">
					<form:option value="" label="--선택--" />
					<c:forEach var="auth" items="${authList }">
						<form:option value="${auth.cd }" label="${auth.cdNm }" />
					</c:forEach>
				</form:select>
       			<span class="form_error" data-path="authCd"><form:errors path="authCd"/></span>
			</td>
		</tr>
		<%-- <tr>
			<th><label for="birth"><spring:message code="userVO.birth" /></label></th>
			<td>
				<form:input path="birth" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="birth"><form:errors path="birth" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="postNo"><spring:message code="userVO.postNo" /></label></th>
			<td>
				<form:input path="postNo" readonly="true" onclick="fn_execPostCode();"/>
				<button type="button" class="search_btn" onclick="fn_execPostCode();">Search</button>
				<span class="form_error" data-path="postNo"><form:errors path="postNo" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="addr"><spring:message code="userVO.addr" /></label></th>
			<td>
				<form:input path="addr" cssClass="w_full"/>
				<span class="form_error" data-path="addr"><form:errors path="addr" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="detlAddr"><spring:message code="userVO.detlAddr" /></label></th>
			<td>
				<form:input path="detlAddr" cssClass="w_full"/>
				<span class="form_error" data-path="detlAddr"><form:errors path="detlAddr" /></span>
			</td>
		</tr> --%>
		<tr>
			<th><label for="mbphNo"><spring:message code="userVO.mbphNo" /></label></th>
			<td>
				<form:input path="mbphNo" placeholder="“-”를 뺀 숫자만 입력해주세요 (예 : 01012345678)" />
				<span class="form_error" data-path="mbphNo"><form:errors path="mbphNo" /></span>
			</td>
		</tr>
		<%-- <tr>
			<th><label for="telNo"><spring:message code="userVO.telNo" /></label></th>
			<td>
				<div class="tel">
  					<c:import url="/WEB-INF/jsp/cmmn/data_tel.jsp">
  						<c:param name="path" value="telNo"/>
   						<c:param name="value" value="${userVO.telNo }"/>
   					</c:import>
   				</div>
				<form:hidden path="telNo" />
				<span class="form_error" data-path="telNo"><form:errors path="telNo" /></span>
			</td>
		</tr> --%>
		<tr>
			<th><label for="email"><spring:message code="userVO.email" /></label></th>
			<td>
				<div class="email">
  					<c:import url="/WEB-INF/jsp/cmmn/data_email.jsp">
  						<c:param name="path" value="email"/>
  						<c:param name="value" value="${userVO.email }"/>
   					</c:import>
   				</div>
   				<form:hidden path="email" />
				<span class="form_error" data-path="email"><form:errors path="email" /></span>
   				<input type="hidden" id="email_email"/>
			</td>
		</tr>
		<tr>
			<th><label for="sex"><spring:message code="joinVO.sex" /></label></th>
			<td>
				<label class="radio" for="sex1"><form:radiobutton path="sex" value="1"/><i></i><c:out value='남성'/></label>
				<label class="radio" for="sex2"><form:radiobutton path="sex" value="2"/><i></i><c:out value='여성'/></label>
			</td>
		</tr>
		<tr>
			<th><label for="userTypeCd"><spring:message code="joinVO.userTypeCd" /></label></th>
			<td>
				<c:forEach var="item" items="${uerTpList }" varStatus="i">
					<label class="radio" for="userTypeCd<c:out value='${i.count }'/>">
						<form:radiobutton path="userTypeCd" value="${item.cd }"/><i></i><c:out value='${item.cdNm }'/>
					</label>
				</c:forEach>
			</td>
		</tr>
		<tr class="mr-info">
			<th><label for="userTypeDetCd"><spring:message code="joinVO.userTypeDetCd" /></label></th>
			<td>
				<c:forEach var="item" items="${uerTpdetList }" varStatus="i">
					<label class="radio" for="userTypeDetCd<c:out value='${i.count }'/>">
						<form:radiobutton path="userTypeDetCd" value="${item.cd }"/><i></i><c:out value='${item.cdNm }'/>
					</label>
				</c:forEach>
			</td>
		</tr>
		<tr class="mr-info">
			<th><label for="colgCd"><spring:message code="joinVO.colgCd" /></label></th>
			<td>
				<ul class="colg-list">
					<li class="">
						<input type="text" id="hiColgNm" name="hiColgNm" readonly value="${userVO.colgNm }" />
					</li>
					<li class="search-sec">
						<input type="text" id="colgNm" readonly onclick="fn_searchColgCd();"  placeholder="검색하기" value="${userVO.deptNm }" />
						<i class="fas fa-search i-colg"></i>
					</li>
				</ul>
				<form:hidden path="deptCd" />
				<input id="deptNm" name="deptNm" value="<c:out value='${userVO.deptNm }' escapeXml='false' />" type="hidden">
				<form:hidden path="colgCd" />
			</td>
		</tr>
		<tr class="mr-info">
			<th><label for="stdNo"><spring:message code="joinVO.stdNo" />/<spring:message code="joinVO.grade" /></label></th>
			<td>
				<form:input path="stdNo" placeholder="학번을 입력해주세요 (예:2012)" />
				<form:input path="grade" placeholder="학년을 입력해주세요 (1/2/3/4/5/6)" maxlength="1" />
			</td>
		</tr>
		<tr class="mr-info02">
			<th><label for="local"><spring:message code="joinVO.local" /></label></th>
			<td>
				<c:forEach var="local" items="${userLocList }" varStatus="i">
					<label class="radio" for="local<c:out value='${i.count }'/>">
						<form:radiobutton path="local" value="${local.cd }"/><i></i><c:out value='${local.cdNm }'/>
					</label>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th><label for="useYn"><spring:message code="userVO.useYn" /></label></th>
			<td>
				<form:radiobutton path="useYn" value="Y" label="Y"/>
				<form:radiobutton path="useYn" value="N" label="N"/>
				<span class="form_error" data-path="useYn"><form:errors path="useYn" /></span>
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
				<a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${userVO.userId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="userVO" staticJavascript="false" xhtml="true" cdata="false"/>
<% /** RSA 관련 import */ %>
<script type="text/javascript" src="/js/common/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/common/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/common/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/common/rsa/rng.js"></script>
<% /** //RSA 관련 import */ %>
<script type="text/javaScript" defer="defer">
	
	$(function(){
		fn_addColgCd();
		
		//초기화면 SET
		var userTypeCd = '<c:out value="${userVO.userTypeCd}"/>';
		if(userTypeCd == "USER_TYPE_010" || userTypeCd == "USER_TYPE_020"){
			$("tr.mr-info02").css("display", "none");
			$("tr.mr-info").css("display", "table-row");
		} else if(userTypeCd == "USER_TYPE_030"){
			$("tr.mr-info").css("display", "none");
			$("tr.mr-info02").css("display", "table-row");
		} else {
			$("tr.mr-info").css("display", "none");
			$("tr.mr-info02").css("display", "none");
		}
		
		//재학생, 졸업생 > 회원상세분류, 단과대학/학과, 학년/학번
		$("input[name='userTypeCd']").click(function(){
			if($(this).val() == "USER_TYPE_010" || $(this).val() == "USER_TYPE_020"){
				$("tr.mr-info02").css("display", "none");
				$("tr.mr-info").css("display", "table-row");
				
				$("input[name='local']").prop('checked', false);
			} else {
				$("tr.mr-info").css("display", "none");
				$("tr.mr-info02").css("display", "table-row");
				
				$("input[name='userTypeDetCd']").prop('checked', false);
				
				$("input[id='hiColgNm']").val("");
				$("input[id='colgNm']").val("");
				$("input[name='colgCd']").val("");
				$("input[name='deptCd']").val("");
				$("input[name='deptNm']").val("");
				$("input[name='stdNo']").val("");
				$("input[name='grade']").val("");
			}
		});
		
	});

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {		
		frm = document.detailForm;
		if (!validateUserVO(frm)) {
			return;
		} else if(gf_isNull(frm.userId.value)){
			gf_alert("아이디를 입력해주세요.");
			return;
		} else if(gf_isNull(frm.userNm.value)){
			gf_alert("이름을 입력해주세요.");
			return;
		} else if(gf_isNull(frm.mbphNo.value)){
			gf_alert("휴대폰번호를 입력해주세요.");
			return;
		} else if(gf_isNull($(".email .id").val())){
			gf_alert("이메일 아이디를 입력해주세요.");
			return;
		} else if(gf_isNull($(".email .domain").val())){
			gf_alert("이메일을 선택해주세요.");
			return;
		} else if(gf_isNull(frm.sex.value)){
			gf_alert("성별을 선택해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_010" || gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_020") 
					&& gf_isNull(frm.userTypeCd.value)){
			gf_alert("회원구분을 선택해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_010" || gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_020")
					&& gf_isNull(frm.userTypeDetCd.value)){
			gf_alert("회원상세분류를 선택해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_010" || gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_020")
					&& gf_isNull($("#colgNm").val())){
			gf_alert("단과대학/학과를 선택해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_010" || gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_020")
					&& gf_isNull(frm.stdNo.value)){
			gf_alert("학번을 입력해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_010" || gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_020")
					&& gf_isNull(frm.grade.value)){
			gf_alert("학년을 입력해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_030" && gf_isNull(frm.local.value))){
			gf_alert("거주지를 선택해주세요.");
			return;
		} else if(frm.mbphNo.value.length != 11 || !/^010?([0-9]{3,4})?([0-9]{4})$/.test(frm.mbphNo.value)){
			gf_alert("휴대폰번호를 다시 입력해주세요.");
			return;
		} else {		
			
			//학과명 disabled 처리
			document.getElementById("colgNm").disabled = true;
			
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
						frm.action = GV_PRESENT_PATH + "/modifyAction.do";
						frm.submit();
					} else { //암호화 실패시
						gf_alert("Error Occur");
					}
				} else {
					gf_alert("Error Occur");
				}
			});
		}
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}

	//취소
	function fn_detailView() {
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
	//우편번호 검색
// 	function fn_execPostCode() {
//         new daum.Postcode({
//             oncomplete: function(data) {
//                 // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

//                 // 각 주소의 노출 규칙에 따라 주소를 조합한다.
//                 // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
//                 var addr = ''; // 주소 변수
//                 var extraAddr = ''; // 참고항목 변수

//                 //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
//                 if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
//                     addr = data.roadAddress;
//                 } else { // 사용자가 지번 주소를 선택했을 경우(J)
//                     addr = data.jibunAddress;
//                 }

//                 // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
//                 if(data.userSelectedType === 'R'){
//                     // 법정동명이 있을 경우 추가한다. (법정리는 제외)
//                     // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
//                     if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
//                         extraAddr += data.bname;
//                     }
//                     // 건물명이 있고, 공동주택일 경우 추가한다.
//                     if(data.buildingName !== '' && data.apartment === 'Y'){
//                         extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
//                     }
//                     // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
//                     if(extraAddr !== ''){
//                         extraAddr = ' (' + extraAddr + ')';
//                     }
//                     // 조합된 참고항목을 해당 필드에 넣는다.
//                     //document.getElementById("sample6_extraAddress").value = extraAddr;
                
//                 } else {
//                     //document.getElementById("sample6_extraAddress").value = '';
//                 }

//                 // 우편번호와 주소 정보를 해당 필드에 넣는다.
//                 document.getElementById('postNo').value = data.zonecode;
//                 document.getElementById("addr").value = addr;
//                 // 커서를 상세주소 필드로 이동한다.
//                 document.getElementById("detlAddr").focus();
//             }
//         }).open();
//     }
	
	function fn_addColgCd(){
		gf_ajax({
			url : "/dept/getColgList.json",
			type : "POST",
			contentType :  "application/json",
			data : {authDvcd : "<c:out value="${selectAuthDvcd}" />"},
		}).then(function(response){
			
			$("#site_main").remove("#DEPT-POP");
			
			var str = "";
			if(!gf_isNull(response)){
				
				str += "<div id='DEPT-POP' style='display: none;'>";
				str += "<div class='dept-tit'><h1>단과대학/학과 선택</h1><button id='close-btn' onClick='fn_closeColgCd();'><span class='sound-only'>닫기</span></button></div>";
				str += "<div class='dept-pop-inner'>";
				str += "<table class='detail_table dept_table'>";
				str += "<thead>";
				str += "<tr>";
				str += "<th>단과대학</th>";
				str += "<th>학과</th>";
				str += "<th></th>";
				str += "</tr>";
				str += "</thead>";
				str += "<tbody>";
				
				response.forEach(function(item, index){
					str += "<tr>";
					str += "<td>"+ gf_nvl(item.hiDeptNmKor, '') +"</td>";
					str += "<td>"+ gf_nvl(item.deptNmKor, '') +"</td>";
					str += "<td><button type='button' class='button' onClick=\"fn_setColgCd(\'" + gf_nvl(item.hiDeptNmKor, '') + "\', \'" + gf_nvl(item.deptNmKor, '') + "\', \'" + gf_nvl(item.deptCd, '') + "\' , \'" + gf_nvl(item.hiDeptCd, '') + "\')\">선택</button></td>";
					str += "</tr>";
				});
				
				str += "</tbody>";
				str += "</table>";
				str += "</div>";
				str += "</div>";
				
				$("#site_main").append(str);
				
			}
		});
	}
	
	//단과대학/학과선택 팝업 open
	function fn_searchColgCd(){
		$("#DEPT-POP").show();
	}
	
	//단과대학/학과선택 팝업 close
	function fn_closeColgCd(){
		$("#DEPT-POP").hide();
	}
	
	//CODE DATA SET
	function fn_setColgCd(hiCdNm, cdNm, cd, hiCd){
		$("#hiColgNm").val(hiCdNm);
		$("#colgNm").val(cdNm);
		$("#colgCd").val(hiCd);
		$("#deptCd").val(cd);
		$("#deptNm").val(cdNm);
		
		$("#DEPT-POP").hide();
	}
</script>