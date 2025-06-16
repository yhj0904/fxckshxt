<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : wk인력풀 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.11.07		신한나			최초생성
 */
%>
<div class="board_wrap">

	<c:if test='${bbsMgtVO.header ne null and bbsMgtVO.header ne ""}'>
		<div class="board_top">
			<c:out value="${bbsMgtVO.header }" escapeXml="false"/>
		</div>
	</c:if>
	
	<div class="board_cont">
		<form:form modelAttribute="laborVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			
			<input id="colgCd" name="colgCd" type="hidden" value="<c:out value="${userVO.colgCd }" escapeXml="false" />">
			<input id="userId" name="userId" type="hidden" value="<c:out value="${userVO.userId }" escapeXml="false" />">
			<input id="userTypeCd" name="userTypeCd" type="hidden" value="<c:out value="${userVO.userTypeCd }" escapeXml="false" />">
			<%-- <c:if test="${(userVO.userTypeCd eq 'USER_TYPE_010') or (userVO.userTypeCd eq 'USER_TYPE_020')}">
				<input id="grade" name="grade" type="hidden" value="<c:out value="${userVO.grade }" escapeXml="false" />">
			</c:if> --%>
			
			<input id="secret" name="secret" type="hidden" value="<c:out value="Y" escapeXml="false" />">
			
			<table class="detail_table">
				<colgroup>
			 		<col width="150"/>
			 		<col width="?"/>
			 	</colgroup>
			 	
			 	<tr>
					<th><label for="belong">소속 </label></th>
					<td>
						<input id="belong" name="belong" type="text" value="<c:out value='${userVO.belong }' escapeXml='false'/>" style="width: 100%;" placeholder="(예시)OO대학교 또는 지역 청년">
						<span class="form_error" data-path="belong"><form:errors path="belong" /></span>
					</td>
				</tr>
			 	
			 	<tr>
					<th><label for="userNm">성명 </label></th>
					<td>
						<input id="userNm" name="userNm" type="text" value="<c:out value='${userVO.userNm }' escapeXml='false'/>"  style="width: 100%;" placeholder="성명을 입력해주세요">
						<span class="form_error" data-path="userNm"><form:errors path="userNm" /></span>
					</td>
				</tr>
				<tr>
					<th><label for="pw">비밀번호* </label></th>
					<td>
						<input id="pw" name="pw" type="text" value=""  style="width: 100%;" placeholder="비밀번호를 입력해주세요">
					</td>
				</tr>
				
				<tr>
					<th><label for="sexCd">성별 </label></th>
					<td>
						<label class="radio" for="sexCd1">
							<input id="sexCd1" name="sexCd" type="radio" value="1" <c:if test="${userVO.sex eq '1'}">checked</c:if>><i></i><c:out value='남성'/>
						</label>
						<label class="radio" for="sexCd2">
							<input id="sexCd2" name="sexCd" type="radio" value="2" <c:if test="${userVO.sex eq '2'}">checked</c:if>><i></i><c:out value='여성'/>
						</label>
					</td>
				</tr>
				
				<tr>
					<th><label for="stdNo">학번</label></th>
					<td>
						<form:input path="stdNo" cssClass="w_full" value="${userVO.stdNo }" maxlegnth="10" placeholder="학번을 입력해주세요"/>
						<span class="form_error" data-path="stdNo"><form:errors path="stdNo" /></span>
					</td>
				</tr>
				
				<tr>
					<th><label for="deptCd">학과</label></th>
					<td>
					    <select name="deptCd" class="w_full">
					        <c:forEach items="${deptList }" var="dept">
		  						<option value="${dept.deptCd}">${dept.deptNmKor}</option>
		  					</c:forEach>
					    </select>
					    <span class="form_error" data-path="deptCd"><form:errors path="deptCd" /></span>
					</td>
				</tr>
				
				<!-- 추가1 -->
				<tr>
					<th><label for="grade">학년</label></th>
					<td class="w_grade_list">
						<label class="radio" for="grade1">
							<input id="grade1" name="grade" type="radio" value="1" <c:if test="${userVO.grade eq '1'}">checked</c:if>><i></i><c:out value='3학년'/>
						</label>
						<label class="radio" for="grade2">
							<input id="grade2" name="grade" type="radio" value="2" <c:if test="${userVO.grade eq '2'}">checked</c:if>><i></i><c:out value='4학년'/>
						</label>
						<label class="radio" for="grade3">
							<input id="grade3" name="grade" type="radio" value="3" <c:if test="${userVO.grade eq '3'}">checked</c:if>><i></i><c:out value='5학년'/>
						</label>
						<label class="radio" for="grade4">
							<input id="grade4" name="grade" type="radio" value="4" <c:if test="${userVO.grade eq '4'}">checked</c:if>><i></i><c:out value='졸업(2년 이내)'/>
						</label>
						<label class="radio" for="grade5">
							<input id="grade5" name="grade" type="radio" value="5" <c:if test="${userVO.grade eq '5'}">checked</c:if>><i></i><c:out value='지역 청년'/>
						</label>
						<label class="radio" for="grade6">
						    <input id="grade6" name="grade" type="radio" value="6" <c:if test="${userVO.grade eq '6'}">checked</c:if>><i></i><c:out value='대학원생'/>
						</label>
						<label class="radio" for="grade7">
						    <input id="grade7" name="grade" type="radio" value="7" <c:if test="${userVO.grade eq '7'}">checked</c:if>><i></i><c:out value='기타'/>
						</label>
						<div id="additionalInputContainer">
						    <c:if test="${userVO.grade eq '7'}">
						        <input type="text" id="additionalInput" name="etcGrade" value="${userVO.etcGrade}" placeholder="기타 정보 입력" style="width:100%;">
						    </c:if>
						</div>
					</td>
				</tr>
				<!-- 추가1 -->
				
				<!-- 추가2 -->
				<tr>
					<th><label for="mbphNo">핸드폰 번호</label></th>
					<td>
						<input id="mbphNo" name="mbphNo" type="text" style="width: 100%;" value="<c:out value='${userVO.mbphNo }' escapeXml='false'/>" placeholder="(형식 : 010-2200-3300)" maxlength="13" oninput="addPhoneHyphen(this);" autocapitalize="off" />
						<span class="form_error" data-path="mbphNo"><form:errors path="mbphNo" /></span>
					</td>
				</tr>
				<!-- 추가2 -->
				
				<tr>
					<th><label for="colgScore">학점(평균평점)</label></th>
					<td>
						<input id="colgScore" name="colgScore" type="number" style="width: 100%;" placeholder="학점을 입력해주세요" oninput="validateScore(this)">
						<br/><span class="form_error" data-path="colgScore"><form:errors path="colgScore" /></span>
					</td>
				</tr>
				
				<tr>
					<th><label for="license">보유자격증(해당자만 작성)</label></th>
					<td>
						<form:textarea path="license"/>
						<span class="form_error" data-path="license"><form:errors path="license" /></span>
					</td>
				</tr>
				
				<tr>
					<th><label for="langScore">공인어학점수(해당자만 작성)</label></th>
					<td>
						<form:textarea path="langScore"/>
						<span class="form_error" data-path="langScore"><form:errors path="langScore" /></span>
					</td>
				</tr>
				
				<tr>
					<th><label for="extAct">대외활동명(개수)</label></th>
					<td>
						<p class="warr">※ 지원 직무 경험 및 이력이 있는 경우도 작성 함.</p>
						<form:textarea path="extAct"/>
						<span class="form_error" data-path="extAct"><form:errors path="extAct" /></span>
					</td>
				</tr>
				
				<!-- 추가3 -->
				<tr>
					<th><label for="itvExp">면접경험</label></th>
					<td>
						<p class="warr">※ 지원 직무 경험 및 이력이 있는 경우도 작성 함.</p>
						<form:input path="itvExp" cssClass="w_full" placeholder="O(2회) 또는 X"/>
						<span class="form_error" data-path="itvExp"><form:errors path="itvExp" /></span>
					</td>
				</tr>
				<!-- 추가3 -->
				
				<tr>
					<th><label for="whProgram01">제공 받고싶은 항목은?<br>(복수선택 3개까지 가능)</label></th>
					<td>
						<ul style="padding-left: 0; margin-bottom: 0;">
							<c:forEach var="item" items="${wProgList.list }" varStatus="i">
								<li>
									<label class="check<c:if test="${item.cdNm eq '기타'}"> chkEtc</c:if>" data-target="chkInp" for="whProgram01<c:out value="${i.count }"/>" style="line-height: normal; height: auto; margin-left: 0;">
										<input type="checkbox" id="whProgram01<c:out value="${i.count }"/>" name="whProgram01" value="${item.cd }"/>
    									<i id="chk"></i><c:out value='${item.cdNm }'/>
									</label>
									<c:if test="${item.cdNm eq '기타'}">
							            <br/><input type="text" id="chkInp" name="etcProgram" id="etcProgram" style="display: none; width:100%;" placeholder="기타 정보 입력"/>
							        </c:if>
								</li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				
				<tr>
					<th><label for="whJob">희망직종</label></th>
					<td>
						<form:input path="whJob" cssClass="w_full" placeholder="(예 : 항공서비스직, 품질관리직, 회계직 등)" /><br/>
						<span class="form_error" data-path="whJob"><form:errors path="whJob" /></span>
					</td>
				</tr>
				
				<tr>
					<th><label for="whLocal">희망근무지</label></th>
					<td class="w_local_list">
						<label id="is_wl_label" class="check" for="whLocal0" style="line-height: normal; height: auto;">
							<input type="checkbox" id="whLocal0" value="ALL"/>
							<i></i>전체선택
						</label>
						<c:forEach var="item" items="${localList.list }" varStatus="i">
							<label class="check localchk" for="whLocal<c:out value="${i.count }"/>" style="line-height: normal; height: auto;">
								<input type="checkbox" id="whLocal<c:out value="${i.count }"/>" name="whLocal" value="${item.cd }"/>
								<i></i><c:out value='${item.cdNm }'/>
							</label>
						</c:forEach>
						<span class="form_error" data-path="whLocal"><form:errors path="whLocal" /></span>
					</td>
				</tr>
				
				<!-- 추가4 -->
				<tr>
					<th><label for="dsrdWage">희망임금(만원)</label></th>
					<td>
						<form:input path="dsrdWage" cssClass="w_full" placeholder="(예시)회사내규에 따름  또는 연봉3,000만원 이상" />
						<span class="form_error" data-path="dsrdWage"><form:errors path="dsrdWage" /></span>
					</td>
				</tr>
				<!-- 추가4 -->
				
				<tr>
					<th><label for="dsrdWorkCdt">희망 근로조건</label></th>
					<td>
						<label class="radio" for="dsrdWorkCdt01">
							<input id="dsrdWorkCdt01" name="dsrdWorkCdt" type="radio" value="1" <c:if test="${userVO.dsrdWorkCdt eq '1'}">checked</c:if>><i></i><c:out value='정규직'/>
						</label>
						<label class="radio" for="dsrdWorkCdt02">
							<input id="dsrdWorkCdt02" name=dsrdWorkCdt type="radio" value="2" <c:if test="${userVO.dsrdWorkCdt eq '2'}">checked</c:if>><i></i><c:out value='계약직 및 인턴'/>
						</label>
						<label class="radio" for="dsrdWorkCdt03">
							<input id="dsrdWorkCdt03" name="dsrdWorkCdt" type="radio" value="3" <c:if test="${userVO.dsrdWorkCdt eq '3'}">checked</c:if>><i></i><c:out value='상관없음'/>
						</label>
					</td>
				</tr>
				
				<tr>
					<th><label for="whCompany">현재까지 입사지원한 기업<br>(합격여부 관계없음)</label></th>
					<td>
						<form:textarea path="whCompany" cssClass="w_full"/><br/>
						<span class="form_error" data-path="whCompany"><form:errors path="whCompany" /></span>
					</td>
				</tr>
				
				<!-- 추가5 -->
				<tr>
					<th><label for="bandMember">밴드 가입 필수</label></th>
					<td>
						<p class="warr">
							WK-PLUS 인재풀 정보 공유방 밴드 가입 필수 --＞ 경제 주요 이슈 및 추천채용 및 일반채용 공지<br/>
							네이버 밴드 URL : <a href="https://band.us/n/a9ac09U2V4H26" target="_blank">https://band.us/n/a9ac09U2V4H26</a><br/>
						</p> 
						<label class="radio" for="bandMember01">
							<input id="bandMember01" name="bandMember" type="radio" value="Y"><i></i><c:out value='가입'/>
						</label>
						<label class="radio" for="bandMember02">
							<input id="bandMember02" name="bandMember" type="radio" value="N"><i></i><c:out value='미가입(추천채용 정보 및 다양한 선발에서 제외될 수 있음)'/>
						</label>
						<br/><span class="form_error" data-path="bandMember"><form:errors path="bandMember" /></span>
					</td>
				</tr>
				<!-- 추가5 -->
				
				<tr>
					<th><label for="agreeYn">개인정보 활용 동의 *</label></th>
					<td>
						<p class="warr">
							2024 취업희망 인재풀 등록을 희망시 아래와 같이 개인정보를 수집합니다.<br/><br/>
							1. 개인정보의 항목 : 소속, 성명, 학번, 학년, 단과대학, 학부(학과), 휴대전화<br/><br/>
							2. 개인정보 수집·사용 목적 : 2024 진로 · 취창업 상담 및 취업 지원<br/><br/>
							3. 개인정보 보유 및 사용기간 : 졸업 후 2년까지  상기 신청자 본인은 <br/>
							   WK-PLUS 인재발굴 프로젝트에 따른 개인정보의 수집 및 이용목적에 동의합니다.
						</p>
						<label class="radio" for="agreeYn01">
							<input id="agreeYn01" name="agreeYn" type="radio" value="Y"><i></i><c:out value='동의 함'/>
						</label>
						<label class="radio" for="agreeYn02">
							<input id="agreeYn02" name="agreeYn" type="radio" value="N" ><i></i><c:out value='동의하지 않음'/>
						</label><br/>
						<span class="form_error" data-path="agreeYn"><form:errors path="agreeYn" /></span>
					</td>
				</tr>
				
				<tr>
					<th><label for="jobPrepPlan">취업준비 계획 *<br/>(or 자신의 커리어 목표)</label></th>
					<td>
						<form:textarea path="jobPrepPlan" cssClass="w_full" /><br/>
						<span class="form_error" data-path="jobPrepPlan"><form:errors path="jobPrepPlan" /></span>
					</td>
				</tr>
			</table>
				   	
			<div class="btn_wrap">
				<ul>
					<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a></li>
					<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
				</ul>
			</div>

		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>  	
		</form:form>
	</div>
	
	<c:if test='${bbsMgtVO.footer ne null and bbsMgtVO.footer ne ""}'>
		<div class="board_bottom">
			<c:out value="${bbsMgtVO.footer }" escapeXml="false"/>
		</div>
	</c:if>

	<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
	<validator:javascript formName="laborVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
	<script type="text/javaScript" defer="defer">	
		
		$(function(){
			
			//희망근무지 선택  - 전체선택
			$("#is_wl_label").click(function() {
				if($("#whLocal0").is(":checked")){
					$("input[name=whLocal]").prop("checked", true);
				} else{
					$("input[name=whLocal]").prop("checked", false);
				}
			});
			
			$(".localchk").click(function() {
				var total = $("input[name=whLocal]").length;
				var checked = $("input[name=whLocal]:checked").length;
				
				if(total != checked){
					$("#whLocal0").prop("checked", false);
				} else{
					$("#whLocal0").prop("checked", true);
				} 
			});
			
			var local_check2 = $(".w_local_list").find("input[name='whLocal']:checked").length;
			var local2 = $(".w_local_list").find("input[name='whLocal']").length;
			if(local2 == local_check2){
				$("input#whLocal0").prop("checked", true);
			}
			
			//에디터사용시
			//var editorYn = '<c:out value="${bbsMgtVO.editorYn}"/>';
			//if(editorYn == "Y"){
			//	var editorArray = [{ id : "contents" , useUpload : '<c:out value="${bbsMgtVO.editorFileYn}"/>' }];
			//	gf_initCkEditor(editorArray);
			//}
			
			//첨부파일 사용시
			//var fileYn = '<c:out value="${bbsMgtVO.fileYn}"/>';
			//if(fileYn == "Y"){
			//	var multiOption = {
			//						listId : "uploadList",
			//						inputId : "uploadFiles",
			//						inputName : "uploadFiles",
			//						uploadCount : "<c:out value='${bbsMgtVO.fileCnt}'/>",
			//						uploadFileSize : "<c:out value='${bbsMgtVO.fileSize}'/>",
			//						uploadFileExt : "<c:out value='${bbsMgtVO.fileExt}'/>"
			//					};
			//	gf_initMultiSelector(multiOption);
			//}
		});
		
		//목록
		function fn_listView() {
			document.detailForm.action = GV_PRESENT_PATH + "/list.do";
			document.detailForm.submit();
		}
		
		//학년 - 기타
		document.addEventListener("DOMContentLoaded", function() {
		    var gradeRadios = document.querySelectorAll('input[name="grade"]');
		    gradeRadios.forEach(function(radio) {
		        radio.addEventListener('change', function() {
		            toggleInputField();
		        });
		    });
		
		    toggleInputField();
		});
		
		function toggleInputField() {
		    var selectedRadio = document.querySelector('input[name="grade"]:checked');
		    var container = document.getElementById("additionalInputContainer");
		
		    if (selectedRadio && selectedRadio.value === "7") {
		        if (!document.getElementById("additionalInput")) {
		            var input = document.createElement("input");
		            input.id = "additionalInput";
		            input.type = "text";
		            input.name = "etcGrade";
		            input.placeholder = "기타 정보 입력";
		            input.style.width = "100%";
		            container.appendChild(input);
		        }
		    } else {
		        var input = document.getElementById("additionalInput");
		        if (input) {
		            container.removeChild(input);
		        }
		    }
		}
		
		//제공항목 - 기타
		$(".chkEtc").click(function(){
			var checkBox = $(".chkEtc").children("input[type=checkbox]");
			if($(checkBox).prop("checked")){
				$("#chkInp").show();
			}else{
				$("#chkInp").hide();
			}
		});
		
		//자동 하이픈
		function addPhoneHyphen(num){
			num.value = num.value
		   .replace(/[^0-9]/g, '')
		   .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
			return num.value;
		}
		
		//학점
		function validateScore(input) {
		    // 입력값을 소수점 두 자리로 제한하는 정규식
		    var validFormat = /^\d{0,2}(\.\d{0,2})?$/;
		
		    // 입력값이 유효하지 않으면 가장 최근의 유효한 값으로 되돌림
		    if (!validFormat.test(input.value)) {
		      input.value = input.value.slice(0, -1);
		    }
		
		    // 입력값이 정의된 범위를 벗어나면 경고 및 값 조정
		    var score = parseFloat(input.value);
		    if (score > 4.5) {
		      input.value = "4.5";
		    } else if (score < 0) {
		      input.value = "0";
		    }
		}
		
		//개인정보 활용동의 검사
		function validateLaborVO(frm) {
			// 비밀번호 검사
			var inptPw = document.getElementById("pw");
			if (inptPw.value.trim() === "") {
				gf_alert("패스워드를 입력해주세요.");
				inptPw.focus();
				return false;
			} else if (inptPw.value.length < 4) {
				gf_alert("패스워드를 4자 이상 입력해주세요.");
				inptPw.focus();
				return false;
			} else if (inptPw.value.indexOf(" ") != -1) {
				gf_alert("패스워드에 공백이 포함되어있습니다.");
				inptPw.focus();
				return false;
			}

		    // 기타 학년 정보 검사
		    var etcGradeSelected = document.getElementById("grade7").checked;
		    var additionalInput = document.getElementById("additionalInput");
		    var additionalInputValue = additionalInput ? additionalInput.value : "";
		    if (etcGradeSelected && additionalInputValue.trim() === "") {
		        gf_alert("기타 학년 정보를 입력해주세요.");
		        additionalInput.focus();
		        return false;
		    }
		
		    // 개인정보 활용 동의 검사
		    var agreeYnYesSelected = document.getElementById("agreeYn01").checked;
		    if (!agreeYnYesSelected) {
		        gf_alert("개인정보 활용에 동의해주세요.");
		        document.getElementById("agreeYn01").focus();
		        return false;
		    }
		
		    // 기타 프로그램 정보 검사
		    var isEtcProgramSelected = false;
		    var checkboxes = document.querySelectorAll('input[name="whProgram01"]');
		    for (var i = 0; i < checkboxes.length; i++) {
		        if (checkboxes[i].checked && checkboxes[i].parentElement.classList.contains('chkEtc')) {
		            isEtcProgramSelected = true;
		            break;
		        }
		    }
		    
		    var etcProgramInput = document.getElementById("chkInp");
		    var etcProgramInputValue = etcProgramInput ? etcProgramInput.value : "";
		    if (isEtcProgramSelected && etcProgramInputValue.trim() === "") {
		        gf_alert("기타 프로그램 정보를 입력해주세요.");
		        etcProgramInput.style.display = "block";
		        etcProgramInput.focus();
		        return false;
		    }
		    
		    return true;
		}

		
		//등록 처리
		function fn_registerAction() {
			frm = document.detailForm;
			if (!validateLaborVO(frm)) {
				return;
			} else {
				frm.action = GV_PRESENT_PATH + "/registerAction.do"; //labr/registerAction.do
				frm.submit();
			}
		}
	</script>
</div>