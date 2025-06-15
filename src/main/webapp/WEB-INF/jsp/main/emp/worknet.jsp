<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 게시글 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>

<div class="table_tab">
	<ul>
		<c:forEach var="api" items="${apiList.list }" varStatus="i">
			<li <c:if test="${sCate eq api.cd }">class="active"</c:if>>
				<a href="javascript:fn_changeCategory('<c:out value="${api.cd }" escapeXml="false" />');">
					<c:out value="${api.cdNm }" escapeXml="false" />
				</a>
			</li>	
		</c:forEach>	
	</ul>
</div>
<div class="board_wrap">

	<c:if test='${bbsMgtVO.header ne null and bbsMgtVO.header ne ""}'>
		<div class="board_top">
			<c:out value="${bbsMgtVO.header }" escapeXml="false"/>
		</div>
	</c:if>
	
	<div class="board_cont">
		<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
			<div class="emp_search_box">
				<%-- <select name="so" title="<spring:message code="search.choose"/>">
					<option value="title" <c:if test="${search.so eq 'title'}">selected</c:if>><spring:message code="bbsVO.title"/></option>
					<c:if test='${bbsMgtVO.nonameYn ne "Y" }'>
						<option value="writer" <c:if test="${search.so eq 'writer'}">selected</c:if>><spring:message code="bbsVO.writer"/></option>
						<option value="inptId" <c:if test="${search.so eq 'inptId'}">selected</c:if>><spring:message code="bbsVO.inptId"/></option>
					</c:if>			
				</select>
				<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
				<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
				<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button> --%>
				<div class="emp_info_list">
					<ul>
						<li class="emp_label"><label for="occupation"><b>직종선택</b></label></li>
						<li>
							<select id="occupation" name="occupation" class="emp_form_select" onchange="fn_changeOccup01(this.value);"  title="직종선택 대분류 선택" >
								<option value="">전체</option>
								<c:forEach var="depth01" items="${rgnDepth01 }" varStatus="i">
									<option value="${depth01.cd }" <c:if test="${search.occupation eq depth01.cd }">selected</c:if>>
										<c:out value="${depth01.cdNm }" escapeXml="false"/>
									</option>
								</c:forEach>	
							</select>
							<select id="occupation02" name="occupation02" class="emp_form_select" title="직종선택 대분류 선택" >
								<option value="">전체</option>
								<c:forEach var="depth02" items="${rgnDepth02 }" varStatus="i">
									<option value="${depth02.cd }" <c:if test="${search.occupation eq depth02.cd }">selected</c:if>>
										<c:out value="${depth02.cdNm }" escapeXml="false"/>
									</option>
								</c:forEach>	
							</select>
						</li>
					</ul>
				</div>
				<div class="emp_info_list emp_cond">
					<ul>
						<li class="emp_label"><label for="education"><b>학력선택</b></label></li>
						<li>
							<select id="education" name="education" class="emp_form_select" title="직종선택 대분류 선택" >
								<option value="">전체</option>
								<c:forEach var="eduCode" items="${eduCode.list }" varStatus="i">
									<c:set var="cdVal" value="${fn:split(eduCode.cd,'_')}" />
									<option value="${cdVal[fn:length(cdVal)-1]}" <c:if test="${search.education eq cdVal[fn:length(cdVal)-1] }">selected</c:if>>
										<c:out value="${eduCode.cdNm }" escapeXml="false"/>
									</option>
								</c:forEach>	
							</select>
						</li>
					</ul>
					<ul>
						<li class="emp_label"><label for="empTpGb"><b>고용조건</b></label></li>
						<li>
							<select id="empTpGb" name="empTpGb" class="emp_form_select" title="직종선택 대분류 선택" >
								<option value="">전체</option>
								<c:forEach var="empTpGbCode" items="${empTpGbCode.list }" varStatus="i">
									<option value="${empTpGbCode.cd }" <c:if test="${search.empTpGb eq empTpGbCode.cd}">selected</c:if>>
										<c:out value="${empTpGbCode.cdNm }" escapeXml="false"/>
									</option>
								</c:forEach>	
							</select>
						</li>
					</ul>
					<ul>
						<li class="emp_label"><label for="career"><b>경력선택</b></label></li>
						<li>
							<select id="career" name="career" class="emp_form_select" title="직종선택 대분류 선택" >
								<option value="">전체</option>
								<c:forEach var="crrCode" items="${crrCode.list }" varStatus="i">
									<option value="${crrCode.cd }" <c:if test="${search.career eq crrCode.cd}">selected</c:if>>
										<c:out value="${crrCode.cdNm }" escapeXml="false"/>
									</option>
								</c:forEach>	
							</select>
						</li>
					</ul>
				</div>
				<div class="emp_info_list pay_cond">
					<ul>
						<li class="emp_label"><label for="salTp"><b>급여조건</b></label></li>
						<li>
							<select id="salTp" name="salTp" class="emp_form_select" title="직종선택 대분류 선택" >
								<option value="">전체</option>
								<c:forEach var="payCode" items="${payCode.list }" varStatus="i">
									<option value="${payCode.cd }" <c:if test="${search.salTp eq payCode.cd}">selected</c:if>>
										<c:out value="${payCode.cdNm }" escapeXml="false"/>
									</option>
								</c:forEach>	
							</select>
							<c:choose>
								<c:when test="${search.minPay eq 0 }">
									<input type="number" id="minPay" name="minPay" placeholder="최소금액" />
								</c:when>
								<c:otherwise>
									<input type="number" id="minPay" name="minPay" placeholder="최소금액" value="${search.minPay }"/>
								</c:otherwise>
							</c:choose>
							~
							<c:choose>
								<c:when test="${search.maxPay eq 0 }">
									<input type="number" id="maxPay" name="maxPay" placeholder="최대금액" />
								</c:when>
								<c:otherwise>
									<input type="number" id="maxPay" name="maxPay" placeholder="최대금액" value="${search.maxPay }" />
								</c:otherwise>
							</c:choose>
							
							<p>※ 급여조건 검색시 '연봉/월급/일급/시급' 항목을 선택하신 다음 반드시 '숫자'로 기입해 주십시오.</p>
						</li>
					</ul>
				</div>
				<div class="emp_info_list keyword_wr">
					<ul>
						<li class="emp_label"><label for="keyword"><b>키워드</b></label></li>
						<li>
							<input type="text" id="keyword" name="keyword" placeholder="검색어를 입력해주세요." value="${search.keyword }" />
							<p>※ 직종이나 업무 등 키워드 형태로 입력해주세요.</p>
						</li>
					</ul>
				</div>
				<div class="emp_search_btn_list">
					<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
					<button type="button" class="reset_btn" onclick="gf_reset();">초기화</button>
				</div>
			</div>
		</form>
		<form:form id="listForm" name="listForm" method="post" autocomplete="off">
			<% //TOTAL COUNT %>
			<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
					
			<% //게시글 유형 %>
			<div class="emp-table-style">
				<table class="emp_table">
					<colgroup>
						<col style="width: 20%;"/> <!--  -->
						<col style=""/> <!--  -->
						<col style="width: 26.5%;"/> <!--  -->
						<col style=""/> <!--  -->
					</colgroup>
					<thead>
						<tr>
							<th>기업명</th>
							<th>공고명/담당업무/지원자격</th>
							<th>근무조건</th>
							<th>등록일/마감일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${list }" varStatus="i">
							<tr>
								<td><c:out value="${item.company }" escapeXml="false"/></td>
								<td>
									<strong class="emp_title">
										<a href="<c:out value="${item.wantedInfoUrl }" escapeXml="false"/>">
											<b><c:out value="${item.title }" escapeXml="false"/></b>
										</a>
										<a href="<c:out value="${item.wantedMobileInfoUrl }" escapeXml="false"/>" class="emp_title_mobile">
											<b><c:out value="${item.title }" escapeXml="false"/></b>
										</a>
									</strong>
									<div class="emp_info_area">
										<dl>
										  <dt>경력</dt>
										  <dd><c:out value="${item.career }" escapeXml="false"/></dd>
										</dl>
										<dl>
										  <dt>학력</dt>
										  <dd>
											<c:out value="${item.minEdubg }" escapeXml="false"/>
											<c:if test="${!empty item.maxEdubg }">
												<c:out value="${item.maxEdubg }" escapeXml="false"/>
											</c:if>
										  </dd>
										</dl>
									</div>
								</td>
								<td>
									<div class="emp_pay">
										<c:forEach var="pay" items="${payCode.list }" varStatus="i">
											<c:if test="${pay.cdNm eq item.salTpNm }">
												<c:set var="info" value="${pay.cd }"/>
											</c:if>
										</c:forEach>
										<em class="emp_pay_info emp_pay_<c:out value='${fn:toLowerCase(info) }'/>"><c:out value="${item.salTpNm }" escapeXml="false"/></em>
										<c:out value="${item.sal }" escapeXml="false"/>
									</div>
									<span class="emp_local"><c:out value="${item.region }" escapeXml="false"/> | <c:out value="${item.career }" escapeXml="false"/></span>
									<span class="emp_holidayTp"><c:out value="${item.holidayTpNm }" escapeXml="false"/></span>
								</td>
								<td class="td_date">  
									<c:out value="${item.regDt }" escapeXml="false"/> 
									<br/>
									~
									<br/>
									<strong class="emp_closing_date"><c:out value="${item.closeDt }" escapeXml="false"/></strong>	
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty list}">
							<tr>
								<td class="no_data" colspan="4"><spring:message code="board.noData" /></td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
			
			<% /** 페이징 */ %>
			<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
			<% /** //페이징 */ %>
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>		  
		   	<% /** 검색조건 유지 */ %>
		   	<input type="hidden" name="sId">
			<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
			<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
			<input type="hidden" name="education" value="<c:out value='${search.education }'/>">
			<input type="hidden" name="empTpGb" value="<c:out value='${search.empTpGb }'/>">
			<input type="hidden" name="career" value="<c:out value='${search.career }'/>">
			<input type="hidden" name="salTp" value="<c:out value='${search.salTp }'/>">
			<input type="hidden" name="occupation" value="<c:out value='${search.occupation }'/>">
			<input type="hidden" name="minPay" value="<c:out value='${search.minPay }'/>">
			<input type="hidden" name="maxPay" value="<c:out value='${search.maxPay }'/>">
			<input type="hidden" name="keyword" value="<c:out value='${search.keyword }'/>">
		   	<input type="hidden" name="checkedSId">
		  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
		  	<% /** //검색조건 유지 */ %>
		</form:form>
	</div>
	
	<c:if test='${bbsMgtVO.footer ne null and bbsMgtVO.footer ne ""}'>
		<div class="board_bottom">
			<c:out value="${bbsMgtVO.footer }" escapeXml="false"/>
		</div>
	</c:if>
	
	<script type="text/javaScript" defer="defer">
	
		//검색
		var occ_arr = "";
		function fn_search(){
			document.listForm.sId.value = "";
			var searchFrm = document.boardSearchForm;
			var occ01 = searchFrm.occupation.value;
			var occ02 = searchFrm.occupation02.value;

			//직종선택
			if(!gf_isNull(occ01) && !gf_isNull(occ02)){
				document.listForm.occupation.value = occ01 + "|" + occ02;
			} else if(!gf_isNull(occ01)){
				document.listForm.occupation.value = occ01;
			} else if(!gf_isNull(occ02)){
				document.listForm.occupation.value = occ02;
			}
			document.listForm.empTpGb.value = searchFrm.empTpGb.value;
			document.listForm.career.value = searchFrm.career.value;
			document.listForm.salTp.value = searchFrm.salTp.value;
			document.listForm.minPay.value = searchFrm.minPay.value;
			document.listForm.maxPay.value = searchFrm.maxPay.value;
			document.listForm.keyword.value = searchFrm.keyword.value;
			//document.getElementById("_search_so_").value = searchFrm.so.value;
			//document.getElementById("_search_sv_").value = searchFrm.sv.value;
			document.listForm.education.value = searchFrm.education.value;
			gf_movePage(1);
		}
		
		//검색 초기화
		function gf_reset() {
			document.listForm.occupation.value = "";
			document.listForm.empTpGb.value = "";
			document.listForm.career.value = "";
			document.listForm.salTp.value = "";
			document.listForm.minPay.value = "";
			document.listForm.maxPay.value = "";
			document.listForm.keyword.value = "";
			document.listForm.education.value = "";
			gf_movePage(1);
		}
		
		//직종선택
		var occ = "<c:out value='${search.occupation}' escapeXml='false' />";
		if(!gf_isNull(occ)){
			var occStatus = 0;
			if(occ.indexOf("|") != -1){
				var occ_arr = occ.split('|');
				var occ_val01 = occ_arr[0];
				var occ_val02 = occ_arr[1];
				occStatus = 1;
				
				$("select#occupation").val(occ_val01).prop("selected",true);

				fn_changeOccup01(occ_val01);	
			} else{
				fn_changeOccup01(occ);
			}
		}
		
		//console.log(occ_val)
		function fn_changeOccup01(target){
			if(gf_nvl(target, "") != ""){
				gf_ajax({
					url : "/commCd/view.json",
					type : "POST",
					data : { 
						cd: target
					},
				}).then(function(response){
					var data = response.list || [];
					if(!gf_isNull(data)){
						$("select#occupation02 option").remove();
						$("select#occupation02").append('<option value="">전체</option>');
						
						data.forEach(function(item, idx){
							if(occStatus == 1 && occ_val02 == gf_nvl(item.cd,"")){
								$("select#occupation02").append('<option value="'+gf_nvl(item.cd,"")+'" selected>'+gf_nvl(item.cdNm,"")+'</option>');
							} else{
								$("select#occupation02").append('<option value="'+gf_nvl(item.cd,"")+'">'+gf_nvl(item.cdNm,"")+'</option>');
							}
							
						});
					}
					//console.log(response);
				});	
			} else {
				$("select#occupation02 option").remove();
				
				//option
				var option = `<option value="">전체</option><c:forEach var="depth02" items="${rgnDepth02 }" varStatus="i"><option value="${depth02.cd }"><c:out value="${depth02.cdNm }" escapeXml="false"/></option></c:forEach>`; 
				$("select#occupation02").append(option);
			}
		}
			
		
		//
		function fn_changeCategory(sCate){
			if(sCate == "WORKNET"){
				document.listForm.sCate.value = sCate;
				document.listForm.action = gf_getPathName();
				document.listForm.submit();	
			} else{
				gf_alert("준비 중입니다.");
			}
		}
		
	</script>
</div>