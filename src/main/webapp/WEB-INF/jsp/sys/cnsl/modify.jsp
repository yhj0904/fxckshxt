<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="kr.co.nanwe.user.service.UserService" %>
<%@ page import="kr.co.nanwe.user.service.UserVO" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 게시글 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="cnslerVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">

	<form:hidden path="cnslId"/>
	<form:hidden path="cnslerId"/>
	
	<p style="font-size: 24px; font-weight: bold; padding-bottom: 20px;">1. 요청자 정보</p>
	<table class="detail_table">
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
   		<tr>
   			<th>신청자</th>
   			<td><c:out value="${cnslerVO.userNm }" /></td>
   		</tr>
   		<tr>
   			<th>단과대학</th>
   			<td><c:out value="${cnslerVO.deptNmKor }" /></td>
   		</tr>
	</table>
	
	<p style="font-size: 24px; font-weight: bold; padding: 20px 0;">2. 상담신청 내용</p>
	<table class="detail_table">
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
   		<tr>
			<th>상담분야</th>
			<td colspan="3">
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeTrack" value="Y" <c:if test="${cnslerVO.cnslTypeTrack eq 'Y' }">checked</c:if> /> 진로</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeJob" value="Y" <c:if test="${cnslerVO.cnslTypeJob eq 'Y' }">checked</c:if> /> 취업</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeLife" value="Y" <c:if test="${cnslerVO.cnslTypeLife eq 'Y' }">checked</c:if> /> 생활</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeResume" value="Y" <c:if test="${cnslerVO.cnslTypeResume eq 'Y' }">checked</c:if> /> 입사지원서</label>
				<label class="cnslLabel"><input type="checkbox" name="cnslTypeIntv" value="Y" <c:if test="${cnslerVO.cnslTypeIntv eq 'Y' }">checked</c:if> /> 모의면접</label>
			</td>
		</tr>
  		<tr>
  			<th>단과대학</th>
  			<td>
  				<select name="cnslColgCd" id="cnslColg">
  					<option value="">단과대학을 선택해주세요.</option>
  					<c:forEach items="${deptList }" var="dept">
  						<option value="${dept.deptCd }" <c:if test="${cnslerVO.cnslColgCd eq dept.deptCd }">selected</c:if> >${dept.deptNmKor }</option>
  					</c:forEach>
  				</select>
  			</td>
  			<th>상담방법</th>
  			<td>
  				<select name="cnslMthCd" id="cnslMthCd">
  					<option value="">선택</option>
  					<c:forEach var="mth" items="${progMth }">
  						<option value="${mth.cd }" <c:if test="${cnslerVO.cnslMthCd eq mth.cd }">selected</c:if> >${mth.cdNm }</option>
  					</c:forEach>
  				</select>
  			</td>
  		 </tr>
  		 <tr>
  			<th>이메일/카톡아이디</th>
  			<td colspan="3">
  				<form:input path="chatId" />
  			</td>
  		</tr>
  		 <tr>
  			<th>상담날짜</th>
  			<td>
  				<input type="date" name="hopeDt" id="hopeDt" value="${cnslerVO.hopeDt }" style="padding: 10px; border: solid 1px #ccc;" />
  			</td>
  			<th>상담선생님</th>
  			<td>
  				<c:set var="userId" value="${cnslerVO.cnslerId}" />
  				<%
						UserService userService = (UserService) webApplicationContext.getBean("userService");
					
						//학부 이름
						String userId = (String) pageContext.getAttribute("userId");
						UserVO userVO = userService.selectUser(userId);
						pageContext.setAttribute("userVO", userVO);
				%>
				
  				<input type="text" id="userNm" name="userNm" readonly="readonly" value='${userVO.userNm }' />
  				<button type="button" class="search_btn" onclick="fn_getUserList();"><spring:message code="button.search"/></button>
  			</td>
  		</tr>
  		<tr>
  			<th>상담시간</th>
  			<td>
  				<select name="hopeTm" id="hopeTm">
  					<option>${cnslerVO.hopeTm }</option>
  				</select>
  			</td>
  			<th>상담장소</th>
  			<td>
  				<input type="text" id="cnslPlace" name="cnslPlace" readonly="readonly" value="${cnslerVO.cnslPlace }" />
  			</td>
  		</tr>
  		<tr>
  			<th>상담시 참고사항</th>
  			<td colspan="3">
  				<textarea rows="" cols="" name="reqstText"><c:out value="${cnslerVO.reqstText }" /></textarea>
  			</td>
  		</tr>
	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li><a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a></li>
			<li><a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${cnslerVO.cnslId}'/>">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		document.detailForm.action = GV_PRESENT_PATH + "/modifyAction.do";
		document.detailForm.submit();
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
	
	//단과별 상담원 조회한다.
	function fn_getUserList() {
		
		if($("#cnslColg option:selected").val() == ''){
			gf_alert("단과를 먼저 선택해주세요.");
			return;
		}
		
		if($("#hopeDt").val() == ''){
			gf_alert("상담날짜를 먼저 선택해주세요.");
			return;
		}
		
		var cnslColgCd = $("#cnslColg option:selected").val();
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
	}
	
</script>