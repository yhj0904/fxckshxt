<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 프로그램 상세  화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.16		신한나			최초생성
 */
%>

<form:form modelAttribute="progVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	<form:hidden path="progId" />
	<div class="register2 prog-view">
		<div class="wrap">
			<div class="top_tt">
				<div class="con">
					<div class="left">
						<div class="left_c">
							<%-- <div class="step step02">
								<p><c:out value="${progVO.progCareerCdNm }"/></p>
							</div> --%>
							<!--//step-->
							<div class="course course01">
								<p><c:out value="${progVO.progTypeCdNm }"/></p>
							</div>
							<!--//course-->
						</div>
						<!--//left_c-->
						<div class="b_title typo-f">
							<h3><c:out value="${progVO.progNm }"/></h3>
						</div>
						<!--//b_title-->
					</div>
					<!--//left-->
					<div class="right">
						<div class="btns">
							<div class="enroll">
								<c:choose>
									<c:when test="${progVO.ddayCd  eq '02' }">
										<a class="btn btn-default" href="javascript:fn_progChkAppl();">신청하기</a>
										<%-- <c:choose>
											<c:when test="${!empty loginInfo }">
												<a class="btn btn-default" href="javascript:fn_progChkAppl();">신청하기</a>
											</c:when>
											<c:otherwise>
												<a class="btn btn-default" href="javascript:fn_redirectLogin('/login.do', 'self');">신청하기</a>
											</c:otherwise>
										</c:choose> --%>
									</c:when>
									<c:otherwise>
										<a class="btn btn-default disabled" href="javascript:void(0);">신청하기</a>
									</c:otherwise>
								</c:choose>
							</div>
							<!--//enroll-->
						</div>
						<!--//btns-->
					</div>
					<!--//right-->
				</div>
				<!--//con-->
			</div>
			<!--//top_tt-->
			<div class="p_ex">
				<div class="wrap">
					<c:choose>
						<c:when test="${!empty progVO.viewFile.viewUrl }">
							<div class="image" style="background: #000 url('<c:out value="${progVO.viewFile.viewUrl }"/>') no-repeat;">
						</c:when>
						<c:otherwise>
							<div class="image" style="background: #000 url('/images/common/no_img.png') no-repeat;">
						</c:otherwise>
					</c:choose>
						<c:choose>
	                		<c:when test="${progVO.ddayCd eq '01' }">
	                			<p class="day orange"><c:out value="${progVO.dday }"/></p>
	                		</c:when>
	                		<c:when test="${progVO.ddayCd eq '02' }">
	                			<p class="day blue"><c:out value="${progVO.dday }"/></p>
	                		</c:when>
	                		<c:otherwise>
	                			<p class="day"><c:out value="${progVO.dday }"/></p>
	                		</c:otherwise>
	                	</c:choose>
					</div>
					<!--//image-->
					<div class="p_list">
						<ul>
							<li>
								<strong>· 교육기간</strong>
								<p><c:out value="${progVO.progSdt }"/> ~ <c:out value="${progVO.progEdt }"/></p>
							</li>
							<li>
								<strong>· 교육시간</strong>
								<p><c:out value="${progVO.progStm }"/> ~ <c:out value="${progVO.progEtm }"/></p>
							</li>
							<li>
								<strong>· 모집기간</strong>
								<p><c:out value="${progVO.reqstSdt }"/> ~ <c:out value="${progVO.reqstEdt }"/></p>
							</li>
							<li>
								<strong>· 교육방법</strong>
								<p><c:out value="${progVO.progMthCdNm }"/></p>
							</li>
							<c:choose>
								<c:when test="${progVO.progMthCd eq 'PROG_MTH_010'}">
									<li>
										<strong>· 교육장소</strong>
										<p><c:out value="${progVO.progPlace}"/></p>
									</li>
								</c:when>
								<c:when test="${progVO.progMthCd eq 'PROG_MTH_020'}">
									<li>
										<strong>· 접근방식</strong>
										<p><c:out value="${progVO.progContactCdNm}"/></p>
									</li>
									<c:if test="${progVO.progContactCd eq 'PROG_CONTACT_010'}">
										<li>
											<strong>· 온라인 주소</strong>
											<p><c:out value="${progVO.progUrl}"/></p>
										</li>
									</c:if>
								</c:when>
							</c:choose>
							<li>
								<strong>· 문의처</strong>
								<p>
									<c:out value="${progVO.eduMngNm}"/>
									<c:if test="${!empty progVO.eduTelNo}">
										(<c:out value="${progVO.eduTelNo}"/>)
									</c:if>
								</p>
							</li>
							<c:if test="${!empty progVO.rm}">
								<li>
									<strong>· 비고</strong>
									<p>
										<c:out value="${progVO.rm}" escapeXml="false" />
									</p>
								</li>
							</c:if>
						</ul>
					</div>
					<!--//p_list-->
				</div>
				<!--//wrap-->
			</div>
			<!--//p_ex-->
			<div class="edu">
				<div class="typo-f">
					<h4>교육내용</h4>
				</div>
				<div class="ctt">
					<p><c:out value="${progVO.progSumry}" escapeXml="false" /></p>
				</div>
				<!--//ctt-->
			</div>
			<!--//edu-->
			
			<c:if test="${!empty progVO.viewFiles }">
				<div class="edu">
					<div class="typo-f">
						<h4>첨부파일</h4>
					</div>
					<div class="ctt">
						<ul class="file_list">
							<c:forEach var="viewFile" items="${progVO.viewFiles }">
								<li>
									<a href="javascript:gf_download('<c:out value="${viewFile.oname }"/>','<c:out value="${viewFile.fpath }"/>','<c:out value="${viewFile.fname }"/>', <c:out value="${viewFile.fno }"/>);" title="<spring:message code="button.download" />">
										<c:out value="${viewFile.oname }"/>
									</a>
								</li>
							</c:forEach>
						</ul>
					</div>
					<!--//ctt-->
				</div>
			</c:if>
			<!--//edu-->
			
			<div class="btm_btn">
				<div class="cencel">
					<a class="btn-cm-def" href="javascript:fn_listView();">취소</a>
				</div>
				<!--//cencel-->
				<div class="enroll">
					<c:choose>
						<c:when test="${progVO.ddayCd  eq '02' }">
							<a class="btn btn-default" href="javascript:fn_progChkAppl();">신청하기</a>
							<%-- <c:choose>
								<c:when test="${!empty loginInfo }">
									<a class="btn btn-default" href="javascript:fn_progChkAppl();">신청하기</a>
								</c:when>
								<c:otherwise>
									<a class="btn btn-default" href="javascript:fn_redirectLogin('/login.do', 'self');">신청하기</a>
								</c:otherwise>
							</c:choose> --%>
						</c:when>
						<c:otherwise>
							<a class="btn btn-default disabled" href="javascript:void(0);">신청하기</a>
						</c:otherwise>
					</c:choose>
				</div>
				<!--//enroll-->
			</div>
			<!--//btm_btn-->
		</div>
		<!--//wrap-->
	</div>

	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer />

	<% /** 검색조건 유지 */ %>
	<input type="hidden" name="sId" value="<c:out value='${progVO.progId }'/>">
	<%-- <input type="hidden" name="sCode" value="<c:out value='${bbsVO.bbsCd }'/>"> --%>
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	<% /** //검색조건 유지 */ %>
</form:form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//프로그램 신청 전 확인 창
	function fn_progChkAppl(){
		//gf_confirm("프로그램 신청을 하시겠습니까?", function(e){console.log("e1 >>>> " + e)}, function(e){console.log("e2 >>>> " + e)});
		
		gf_confirm("프로그램 신청을 하시겠습니까?", function(e){
			if(e == "Y"){
				//fn_progApply >> 태우기 전 입력폼에 데이터 입력 받고 함수 실행. - 2024-06-17
				fn_progApply();
			}
		});
	}
	
	//프로그램 신청
	function fn_progApply(){
		var pId = 0;
		<c:if test="${!empty progVO.progId}">
			pId = <c:out value="${progVO.progId}" />;
		</c:if>
		
		gf_ajax({
			url : "/prog/apply.json",
			type : "POST",
			dataType : "json",
			data : {progId : pId},
		}).then(function(response){
			//팝업 삭제
			$("#PROG-APPL-COMPLETE").remove();
			
			if(!gf_isNull(response)){
				if(gf_nvl(response.resultCode, "") == "ERROR_CREATE"){
					gf_alert("<spring:message code='message.program.error' />");
				} else if(gf_nvl(response.resultCode, "") == "ERROR_END"){
					gf_alert("<spring:message code='message.program.end' />");
				} else if(gf_nvl(response.resultCode, "") == "OK"){
					fn_applyView(pId);
				}
			}
		});
	}
	
	//로그인 페이지 이동
	function fn_redirectLogin(url, type){
		gf_alert("로그인 후  신청가능합니다.", function(){
	 		if (gf_isNull(type)){
				type = "self";
			}
			
			if (gf_isNull(url)){
				return;
			}
			
			switch (type) {
				case "self" :
					window.location.href = url;
					break;
				case "blank" :
					window.open("about:blank").location.href= url;
					break;					
			}
			
		});
	}
	
	//팝업 CLOSE
	function fn_popClose(){
		$("#PROG-APPL-COMPLETE").remove();
		
		location.reload();
	}
	
	//신청페이지  이동
	function fn_applyView(sId) {
		document.detailForm.sId.value = sId;
		document.detailForm.action = GV_PRESENT_PATH + "/apply.do";
		document.detailForm.submit();
	}

</script>