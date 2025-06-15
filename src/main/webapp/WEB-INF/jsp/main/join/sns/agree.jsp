<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: agree.jsp
 * @Description : 약관동의
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="join_wrap">
	<div class="join_title">
		<h2>회원가입</h2>
	</div>	
	<div class="join_step">
		<ul>
			<li>
				<img src="/images/common/join_step1_on.png" alt="(현재)개인정보 이용약관 동의"/>
				<span>약관동의</span>								
			</li>
			<li>
				<img src="/images/common/join_step3_off.png" alt="회원가입 완료"/>
				<span>가입완료</span>
			</li>
		</ul>
	</div>	
	<div class="join_content">
		<form id="detailForm" name="detailForm" method="post" action="/join/sns/<c:out value='${snsType }'/>/registerAction.do">
			<input type="hidden" name="agree" value="false">
			<input type="hidden" name="joinToken" value="<c:out value='${joinToken }'/>">
			<input type="hidden" name="authToken" value="<c:out value='${authToken }'/>">
			<div class="term_wrap">
				<ul>
					<li>
						<label class="check" for="allTermCheck">
							<input type="checkbox" id="allTermCheck" onclick="fn_allTermCheck();">
							<i></i>이용약관, 개인정보 수집 및 이용 및 선택항목에 모두 동의합니다.
						</label>
					</li>
					<c:forEach var="item" items="${joinVO.termList }" varStatus="i">
						<li>
							<div class="term_title">
								<input type="hidden" name="termList[<c:out value='${i.index }'/>].termsDvcd" class="termsDvcd" value="<c:out value='${item.termsDvcd }'/>"/>
								<input type="hidden" name="termList[<c:out value='${i.index }'/>].termsId" class="termsId" value="<c:out value='${item.termsId }'/>"/>
								<input type="hidden" name="termList[<c:out value='${i.index }'/>].checked" class="checked" value="0"/>
								<label class="check" for="term_<c:out value='${item.termsId }'/>">
									<c:choose>
										<c:when test='${item.necessaryYn eq "Y" }'>
											<input type="checkbox" id="term_<c:out value='${item.termsId }'/>" class="checkTerm necessary" value="<c:out value='${item.termsId }'/>" onclick="fn_checkTerm();">
										</c:when>
										<c:otherwise>
											<input type="checkbox" id="term_<c:out value='${item.termsId }'/>" class="checkTerm" value="<c:out value='${item.termsId }'/>" onclick="fn_checkTerm();">
										</c:otherwise>
									</c:choose>
									<i></i><c:out value='${item.termsNm }'/>
									<c:choose>
										<c:when test='${item.necessaryYn eq "Y" }'><span class="f_red">(필수)</span></c:when>
										<c:otherwise><span>(선택)</span></c:otherwise>
									</c:choose>
								</label>
							</div>
							<c:if test='${item.termsCont ne null and item.termsCont ne "" }'>
								<div class="term_box scroll_box" tabindex="0">
									<c:out value='${item.termsCont }' escapeXml="false"/>
								</div>
							</c:if>
						</li>
					</c:forEach>
				</ul>
			</div>
			
			<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
			
		</form>
		<div class="btn_wrap">
			<ul>
				<li>
					<a href="javascript:fn_joinCancel();">비동의</a>
				</li>
				<li>
					<a class="agree" href="javascript:fn_agreeAction();">동의</a>
				</li>
			</ul>
		</div>
	</div>
	<script type="text/javaScript" defer="defer">
		
		function fn_agreeAction() {
			//필수 약관 체크
			document.detailForm.agree.value = false;
			if (!fn_checkEssentialTerm()) {
				alert("이용약관과 개인정보 수집 및 이용에 대한 안내 모두 동의해주세요.");
				return;
			} else {
				document.detailForm.agree.value = true;
				document.detailForm.submit();
			}
		}
	
		/* 약관 전체 선택 */
		function fn_allTermCheck() {
			if($("#allTermCheck").is(":checked")){
				$(".checkTerm").prop("checked", true);
				$("input.checked").val("1");
			} else {
				$(".checkTerm").prop("checked", false);
				$("input.checked").val("0");
			}
		}
	
		/* 약관 선택 */
		function fn_checkTerm() {
			var chkCnt = 0;
			var rowCnt = 0;
			$.each($(".checkTerm"),function(i, item){
				rowCnt ++;
				var termsId = $(item).val();
				if($(item).is(":checked")){
					$(".termsId[value='"+termsId+"']").next(".checked").val("1");
					chkCnt++;
				} else {
					$(".termsId[value='"+termsId+"']").next(".checked").val("0");
				}
			});
			if (chkCnt == rowCnt) {
				$("#allTermCheck").prop("checked", true);
			} else {
				$("#allTermCheck").prop("checked", false);
			}
		}
	
		function fn_checkEssentialTerm() {
			var chkCnt = 0;
			var rowCnt = 0;
			$.each($(".checkTerm.necessary"),function(i, item){
				rowCnt ++;
				if($(item).is(":checked")){
					chkCnt++;
				}
			});
			if(chkCnt == rowCnt){
				return true;
			} else {
				return false;
			}
		}
	
		function fn_joinCancel() {
			location.href = "/";
		}
		
	</script>
</div>