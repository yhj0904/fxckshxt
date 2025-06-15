<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 프로그램 등록  화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.16		신한나			최초생성
 */
%>

<div id="PROG_APPLY" class="prog_apply_wrap register2">
	<form:form commandName="progUserVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
		<form:hidden path="progId" />
		
		<table class="detail_table">
			<caption>프로그림 신청 정보 등록</caption>
			<colgroup>
	   			<col width="150"/>
	   			<col width="?"/>
	   		</colgroup>
			<c:if test="${not empty program }">
				<tr>
					<th><label for="progNm">프로그램명</label></th>
					<td>
						<input type="text" id="" name="" value="${program.progNm}" disabled="disabled" style="width: 100%;" />
		       			<%-- <span class="form_error" data-path="progNm"><form:errors path="progNm"/></span> --%>
					</td>
				</tr>
			</c:if>
			<tr>
				<th><label for="userNm"><spring:message code="joinVO.userNm" /></label></th>
				<td>
					<form:input path="userNm" placeholder="이름을 입력해주세요" />
	       			<span class="form_error" data-path="userNm"><form:errors path="userNm"/></span>
				</td>
			</tr>
			<tr>
				<th><label for="mbphNo"><spring:message code="joinVO.mbphNo" /></label></th>
				<td>
					<form:input path="mbphNo" placeholder="“-”를 뺀 숫자만 입력해주세요 (예 : 01012345678)" />
					<span class="form_error" data-path="mbphNo"><form:errors path="mbphNo" /></span>
				</td>
			</tr>
			<tr>
				<th><label for="email"><spring:message code="joinVO.email" /></label></th>
				<td>
					<div class="email">
	  					<c:import url="/WEB-INF/jsp/cmmn/data_email.jsp">
	  						<c:param name="path" value="email"/>
	  						<c:param name="value" value="${joinVO.email }"/>
	   					</c:import>
	   				</div>
	   				<form:hidden path="email" />
					<span class="form_error" data-path="email"><form:errors path="email" /></span>
	   				<input type="hidden" id="email_email"/>
				</td>
			</tr>
			<tr>
				<th><label for="sexCd"><spring:message code="joinVO.sex" /></label></th>
				<td>
					<label class="radio" for="sexCd1"><form:radiobutton path="sexCd" value="1"/><i></i><c:out value='남성'/></label>
					<label class="radio" for="sexCd2"><form:radiobutton path="sexCd" value="2"/><i></i><c:out value='여성'/></label>
				</td>
			</tr>
			<tr>
				<th><label for="userTypeCd"><spring:message code="joinVO.userTypeCd" /></label></th>
				<td class="userType">
					<c:forEach var="item" items="${uerTpList }" varStatus="i">
						<c:if test="${item.cd ne 'USER_TYPE_040'}">
							<label class="radio" for="userTypeCd<c:out value='${i.count }'/>">
								<form:radiobutton id="userTypeCd${i.count }" path="userTypeCd" value="${item.cd }"/><i></i><c:out value='${item.cdNm }'/>
							</label>
						</c:if>
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
							<input type="text" id="hiColgNm" disabled />
						</li>
						<li class="search-sec">
							<input type="text" id="colgNm" readonly onclick="fn_searchColgCd();"  placeholder="검색하기"  />
							<i class="fas fa-search i-colg"></i>
						</li>
					</ul>
					<form:hidden path="deptCd" />
					<form:hidden path="deptNm" />
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
		</table>
		   	
		<div class="btm_btn">
				<div class="cencel">
					<a class="btn-cm-def" href="javascript:fn_listView();">취소</a>
				</div>
				<!--//cencel-->
				<div class="enroll">		
					<a class="btn btn-default" href="javascript:fn_registerAction();">저장하기</a>
				</div>
				<!--//enroll-->
			</div>

		<% /** 이중방지 토큰 */ %>
		<double-submit:preventer/>

	</form:form>
	<script type="text/javaScript" defer="defer">
		$(function(){
			fn_addColgCd();
			
			//초기화면 SET
			var userTypeCd = '<c:out value="${progUserVO.userTypeCd}"/>';
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
		
		//등록 처리
		function fn_registerAction() {
			console.log('fn_registerAction');
			frm = document.detailForm;
			
			
			if(gf_isNull(frm.userNm.value)){
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
			} else if(gf_isNull(frm.sexCd.value)){
				gf_alert("성별을 선택해주세요.");
				return;
			} else if(gf_isNull(frm.userTypeCd.value)){
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
				//프로그램 신청 로직 추가
				frm.action = "/prog/applyAction.do";
				console.log('submit');
				frm.submit();
			}
		}
		
		function fn_addColgCd(){
			gf_ajax({
				url : "/dept/getColgList.json",
				type : "GET",
				data : {authDvcd : "<c:out value="${selectAuthDvcd}" />"},
				dataType : "json",
				contentType:"application/json",
			}).then(function(response){
				
				$("#PROG_APPLY").remove("#DEPT-POP");
				
				var str = "";
				if(!gf_isNull(response)){
					
					str += "<div id='DEPT-POP' style='display: none;'>";
					str += "<div class='dept-pop-inner'>";
					str += "<div class='dept-tit'><h1>단과대학/학과 선택</h1><button id='close-btn' onClick='fn_closeColgCd();'><span class='sound-only'>닫기</span></button></div>";
					str += "<div class='table-style2'>";
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
					str += "</div>";
					
					$("#PROG_APPLY").append(str);
					
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
		
		//목록
		function fn_listView() {
			document.detailForm.action = "/prog/list.do";
			document.detailForm.submit();
		}
	</script>
</div>