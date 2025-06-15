<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 게시글 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>

<div id="cnslRegi" class="board_wrap">

	<div class="board_cont">
	<div class="scroll">
		<form:form commandName="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/main/board/${GV_BOARD_SKIN_CODE }/register.jsp"></c:import>
			</c:if>
		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId" value="">
<%--		   	<input type="hidden" name="stdNo" value="${userVO.stdNo }" />--%>
<%--		   	<input type="hidden" name="sexCd" value="${userVO.sex }" />--%>
<%--		   	<input type="hidden" name="grade" value="${userVO.grade }" />--%>
<%--		   	<input type="hidden" name="userTypeCd" value="${userVO.userTypeCd }" />--%>
<%--		   	<input type="hidden" name="cnslColgCd" value="${userVO.colgCd }" />--%>
<%--		   	<input type="hidden" name="cnslDeptCd" value="${userVO.deptCd }" />--%>
		   	<input type="hidden" name="cnslStatusCd" value="CNSL_STATUS_STEP1" />
		   	<input type="hidden" name="cnslerId" id="cnslerId" />
		   	
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>
		  	
		  	<table class="detail_table">
		  		<colgroup>
		  			<col style="width: 20%">
		  			<col style="width: 25%">
		  			<col style="width: 20%">
		  			<col style="width: 35%">
		  		</colgroup>
				<tr>
					<th>상담제목</th>
					<td colspan="3">
						<input type="text" id="title" name="title" value=""/>
					</td>
				</tr>
		  		<tr>
		  			<th>신청자</th>
		  			<td colspan="3">
		  				<input type="text" id="cnslUserNm" name="cnslUserNm" value=""/>
		  			</td>
		  		</tr>
				<tr>
					<th>비밀번호</th>
					<td colspan="3">
						<input type="password" id="pw" name="pw" value=""/>
					</td>
				</tr>
		  		<tr>
					<th>상담분야</th>
					<td colspan="3">
						<label class="cnslLabel"><input type="checkbox" id="cnslTypeTrack" name="cnslTypeTrack" value="Y" /> 진로</label>
						<label class="cnslLabel"><input type="checkbox" id="cnslTypeJob" name="cnslTypeJob" value="Y" /> 취업</label>
						<label class="cnslLabel"><input type="checkbox" id="cnslTypeLife" name="cnslTypeLife" value="Y" /> 생활</label>
						<label class="cnslLabel"><input type="checkbox" id="cnslTypeResume" name="cnslTypeResume" value="Y" /> 입사지원서</label>
						<label class="cnslLabel"><input type="checkbox" id="cnslTypeIntv" name="cnslTypeIntv" value="Y" /> 모의면접</label>
					</td>
				</tr>
				<tr>
					<th><label for="userTypeCd"><spring:message code="joinVO.userTypeCd" /></label></th>
					<td class="userType">
						<c:forEach var="item" items="${userTpList }" varStatus="i">
							<c:if test="${item.cd ne 'USER_TYPE_040' and item.cd ne 'USER_TYPE_030'}">
								<label class="radio" for="userTypeCd<c:out value='${i.count }'/>">
									<form:radiobutton id="userTypeCd${i.count }" path="userTypeCd" value="${item.cd }"/><i></i><c:out value='${item.cdNm }'/>
								</label>
							</c:if>
						</c:forEach>
					</td>
				</tr>
				<tr class="mr-info">
					<th><label for="colgCd"><spring:message code="joinVO.colgCd" /></label></th>
					<td>
						<ul class="colg-list">
							<li class="">
								<input type="text" id="cnslColgNm" disabled />
							</li>
							<li class="search-sec">
								<input type="text" id="cnslDeptNm" readonly onclick="fn_searchColgCd();"  placeholder="검색하기"  />
								<i class="fas fa-search i-colg"></i>
							</li>
						</ul>
						<input type="hidden" id="colgCd" value=""/>
						<form:hidden path="cnslDeptCd" />
						<form:hidden path="cnslColgCd" />
					</td>
				</tr>
				<tr class="mr-info">
					<th><label for="stdNo"><spring:message code="joinVO.stdNo" />/<spring:message code="joinVO.grade" /></label></th>
					<td>
						<form:input path="stdNo" placeholder="학번을 입력해주세요 (예:2012)" />
						<form:input path="grade" placeholder="학년을 입력해주세요 (1/2/3/4/5/6)" maxlength="1" />
					</td>
				</tr>
<%--		  		<tr>--%>
<%--		  			<th>단과대학</th>--%>
<%--		  			<td>--%>
<%--		  				<select name="cnslColgCd" id="cnslColgCd">--%>
<%--		  					<option value="">단과대학을 선택해주세요.</option>--%>
<%--		  					<c:forEach items="${deptList }" var="dept">--%>
<%--		  						<option value="${dept.deptCd }">${dept.deptNmKor }</option>--%>
<%--		  					</c:forEach>--%>
<%--		  				</select>--%>
<%--		  			</td>--%>
<%--					<th>학과</th>--%>
<%--					<td>--%>
<%--						<select name="cnslDeptCd" id="cnslDeptCd">--%>
<%--							<option value="">학과를 선택해주세요.</option>--%>
<%--						</select>--%>
<%--					</td>--%>
<%--		  		 </tr>--%>
<%--				<tr>--%>
<%--					<th>학년</th>--%>
<%--					<td>--%>
<%--						<input type="text" id="grade" name="grade"/>--%>
<%--					</td>--%>
<%--					<th>학번</th>--%>
<%--					<td>--%>
<%--						<input type="text" id="stdNo" name="stdNo"/>--%>
<%--					</td>--%>
<%--		  		 <tr>--%>
			<tr>
				<th>성별</th>
				<td>
					<label class="radio" for="sexCd1">
						<input id="sexCd1" name="sexCd" type="radio" value="1"><i></i><c:out value='남성'/>
					</label>
					<label class="radio" for="sexCd2">
						<input id="sexCd2" name="sexCd" type="radio" value="2"><i></i><c:out value='여성'/>
					</label>
				</td>
				<th>상담방법</th>
				<td>
					<select name="cnslMthCd" id="cnslMthCd">
						<option value="">선택</option>
						<c:forEach var="mth" items="${progMth }">
							<option value="${mth.cd }">${mth.cdNm }</option>
						</c:forEach>
					</select>
				</td>
			</tr>
					<th>전화번호</th>
					<td>
						<form:input path="mbphNo" />
					</td>
		  			<th>이메일/카톡아이디</th>
		  			<td>
		  				<form:input path="chatId" />
		  			</td>
		  		</tr>
		  		 <tr>
		  			<th>상담날짜</th>
		  			<td>
		  				<input type="date" name="hopeDt" id="hopeDt" />
		  				<script>
				  			// "hopeDt"라는 ID를 가진 input 요소의 값이 오늘 날짜 이전인지 확인하는 함수
			  			    function isDateBeforeToday(dateString) {
			  			        const selectedDate = new Date(dateString);
			  			        const today = new Date();
			  			        return selectedDate < today;
			  			    }
	
			  			    // input 값이 변경될 때마다 실행되는 이벤트 핸들러
			  			    $("#hopeDt").on("change", function() {
			  			        const inputValue = $(this).val();
	
			  			        // 유효성 검사: 오늘 날짜 이전이면 선택 불가능하도록 설정
			  			        if (isDateBeforeToday(inputValue)) {
			  			            alert("오늘보다 이전 날짜는 선택하실 수 없습니다.");
			  			            // 선택된 값을 초기화하거나 다른 조치를 취할 수 있습니다.
			  			            $(this).val("");
			  			        }
			  			    });
		  				</script>
		  			</td>
		  			<th>상담선생님</th>
		  			<td>
		  				<input type="text" id="userNm" name="userNm" readonly="readonly" />
		  				<button type="button" class="search_btn" onclick="fn_getUserList();"><spring:message code="button.search"/></button>
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>상담시간</th>
		  			<td>
		  				<select name="hopeTm" id="hopeTm">
		  					
		  				</select>
		  			</td>
		  			<th>상담장소</th>
		  			<td>
		  				<input type="text" id="cnslPlace" name="cnslPlace" readonly="readonly" />
		  			</td>
		  		</tr>
		  		<tr>
		  			<th>상담시 참고사항</th>
		  			<td colspan="3">
		  				<textarea rows="" cols="" name="reqstText"></textarea>
		  			</td>
		  		</tr>
		  	</table>
		  	
		  	
		  	<div class="btn_wrap">
				<ul>
					<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a></li>
					<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
				</ul>
			</div>
		</form:form>
		</div>
	</div>
	
	<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
	<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
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

				$("input[id='cnslColgNm']").val("");
				$("input[id='cnslDeptNm']").val("");
				$("input[name='cnslColgCd']").val("");
				$("input[name='colgCd']").val("");
				$("input[name='cnslDeptCd']").val("");
				$("input[name='stdNo']").val("");
				$("input[name='grade']").val("");
			}
		});

	});

	function fn_addColgCd(){
		gf_ajax({
			url : "/dept/getColgList.json",
			type : "GET",
			data : {authDvcd : "COM_USER"},
			dataType : "json",
			contentType:"application/json",
		}).then(function(response){

			$("#cnslRegi").remove("#DEPT-POP");

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

				$("#cnslRegi").append(str);

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
		$("#cnslColgNm").val(hiCdNm);
		$("#cnslDeptNm").val(cdNm);
		$("#cnslColgCd").val(hiCd);
		$("#colgCd").val(hiCd);
		$("#cnslDeptCd").val(cd);

		$("#DEPT-POP").hide();
	}

	//목록
	function fn_listView() {
		document.detailForm.action = "/cnsler/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		var frm = document.detailForm;

		//상담 제목 체크
		if ($('#title').val().length < 1) {
			gf_alert("상담제목을 입력해 주세요.");
			return;
		}

		//신청자 체크
		if($('#cnslUserNm').val().indexOf(' ') != -1) {
			gf_alert("신청자에 공백이 포함되어 있습니다.");
			return;
		} else if ($('#cnslUserNm').val().length < 1) {
			gf_alert("신청자를 입력해 주세요.");
			return;
		}

		//비밀번호 체크
		if($('#pw').val().indexOf(' ') != -1) {
			gf_alert("비밀번호에 공백이 포함되어있습니다.");
			return;
		} else if ($('#pw').val().length < 4) {
			gf_alert("비밀번호를 4자 이상 입력해주세요.");
			return;
		} else if ($('#pw').val().trim() == "") {
			gf_alert("비밀번호를 입력해주세요.");
			return;
		}

		//분야선택 체크
		if(!$('#cnslTypeTrack').is(':checked') && !$('#cnslTypeJob').is(':checked') && !$('#cnslTypeLife').is(':checked') && !$('#cnslTypeResume').is(':checked') && !$('#cnslTypeIntv').is(':checked')){
			gf_alert("상담 분야를 하나 이상 선택해주세요.");
			return;
		}

		// 회원구분
		if(gf_isNull(frm.userTypeCd.value)){
			gf_alert("회원구분을 선택해주세요.");
			return;
		} else if((gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_010" || gf_nvl(frm.userTypeCd.value, "") == "USER_TYPE_020")
				&& gf_isNull($("#cnslColgNm").val())){
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
		}
		
		//방법 선택
		if(!$('#cnslMthCd > option:selected').val()) {
		    gf_alert("상담 방법을 선택해주세요.");
		    
		    return;
		}
		
		//상담 날짜 선택
		if($('#hopeDt').val() == '') {
		    gf_alert("희망하는 상담 날짜를 선택해주세요.");
		    
		    return;
		}
		
		//시간 선택
		if(!$('#hopeTm > option:selected').val()) {
		    gf_alert("희망하는 상담 시간을 선택해주세요.");
		    
		    return;
		}
		
		//상담자 선택
		if($('#userNm').val() == '') {
		    gf_alert("희망하는 상담 선생님을 선택해주세요.");
		    
		    return;
		}
		
		gf_confirm("상담을 신청하시겠습니까?", function(e){
			if(e == "Y"){
				document.detailForm.action = "/cnsler/registerAction.do";
				document.detailForm.submit();
			}
		});
	}
		
	//단과별 상담원 조회한다.
	function fn_getUserList() {
		
		if($("#colgCd").val() == ''){
			gf_alert("단과를 먼저 선택해주세요.");
			return;
		}
		
		if($("#hopeDt").val() == ''){
			gf_alert("상담날짜를 먼저 선택해주세요.");
			return;
		}
		
		var cnslColgCd = $("#colgCd").val();
		var schDt = $("#hopeDt").val();
		var id = "userList";
		var url = "/cnsl/selectCnslerByColg.json?cnslColgCd=" + cnslColgCd + "&schDt=" + schDt;
		var param = {cnslColgCd: cnslColgCd, schDt: schDt};
		var columns = [
			{key:"cnslerId", name:"아이디"},
			{key:"userNm", name:"이름"},
			{key:"cnslPlace", name:"장소"},
			{key:"ableTm", name:"가능시간"},
		];
		var callback = function(data){
			var cnslerId = data.cnslerId;
			var userNm = data.userNm;
			var cnslPlace = data.cnslPlace;
			
			document.detailForm.cnslerId.value = cnslerId;
			document.detailForm.userNm.value = userNm;
			document.detailForm.cnslPlace.value = cnslPlace;
			
			fn_timeSet();
		};
		gf_openSearchPop(id, url, param, columns, callback);
	};
	
	//상담원 선택시 시간조회
	function fn_timeSet(){
		var cnslerId = $("#cnslerId").val();
		var schDt = $("#hopeDt").val();
		
		$.ajax({
			type : 'POST',
			url : '/cnsl/selectCnslerTmList.json',
			data : {cnslerId: cnslerId, schDt: schDt},
			async : false,
			success : function(data) {
  				var strHours = '';
  				var time = '';
  				
  				//예약된시간 배열선언
  				var unableTmArr = [];
  				
  				strHours += '<option value="" selected>시간선택</option>';
  				
  				$.each(data, function(index, item) { // 데이터 =item
  					unableTmArr.push(item.hopeTm);
				});
  				
  				for(var i = 10 ; i < 19; i++){
  					time = i;
  					valueTime = i + "00";
  					if(i == 12){
  						
  					} else {
	  					if(unableTmArr.includes(valueTime)){
	  						strHours += '<option value="'+valueTime+'" disabled>'+time+'시</option>';
	  					} else {
	  						strHours += '<option value="'+valueTime+'">'+time+'시</option>';
	  					}
  					}
					
  				}
				
				$("#hopeTm").html(strHours);
			},
			error : function(xhr, status) {
				alert('[' + status + ']\n\n' + xhr.responseText);
				hasError = true;
			}
		});
	};
	
	</script>
</div>


<style>
.layer_pop.search_pop{max-width: 50%;}
option:disabled{background-color: #ccc; text-decoration: line-through;}
</style>