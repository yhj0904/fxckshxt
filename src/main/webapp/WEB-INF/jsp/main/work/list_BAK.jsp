<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 프로그램 목록  화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.16		신한나			최초생성
 */
%>
<div class="board_wrap">
	<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
	
	<% //게시글 유형 %>
	<div class="search-form-wr">
		<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
			<div class="search_box">
				<select name="so" title="<spring:message code="search.choose"/>">
					<option value="title" <c:if test="${search.so eq 'title'}">selected</c:if>><spring:message code="bbsVO.title"/></option>
					<c:if test='${bbsMgtVO.nonameYn ne "Y" }'>
						<option value="writer" <c:if test="${search.so eq 'writer'}">selected</c:if>><spring:message code="bbsVO.writer"/></option>
						<option value="inptId" <c:if test="${search.so eq 'inptId'}">selected</c:if>><spring:message code="bbsVO.inptId"/></option>
					</c:if>			
				</select>
				<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
				<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
				<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
			</div>
		</form>
	</div>
	<form:form id="listForm" name="listForm" method="post" autocomplete="off">
		<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
		<% //게시글 유형 %>
		<c:if test='${bbsMgtVO.cateYn eq "Y" and !empty bbsMgtVO.categoryList }'>
			<div class="table_category">
				<ul>
					<li <c:if test="${sCate eq 'ALL' }">class="active"</c:if>><a href="javascript:fn_changeCategory('ALL');"><spring:message code="search.all"/></a></li>
					<c:forEach var="cate" items="${bbsMgtVO.categoryList }" varStatus="i">
						<li <c:if test="${sCate eq cate}">class="active"</c:if>><a href="javascript:fn_changeCategory('<c:out value='${cate }'/>');"><c:out value='${cate }'/></a></li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<% //게시글 유형 %>
		<table class="list_table">
			<colgroup>
				<c:if test='${bbsMgtVO.adminUser}'>
					<col class="mobile" style="width: 8%;"/> <!-- CHECKBOX -->
				</c:if>
				<col class="mobile" style="width: 8%;"/> <!-- NO -->
				<col style="width: auto;"/> <!-- 제목 -->
				<col class="mobile" style="width: 17%"/> <!-- 작성자 -->
				<col class="mobile" style="width: 15%"/> <!--  -->
				<col class="mobile" style="width: 9%;"/> <!-- 조회수 -->
			</colgroup>
			<thead class="mobile">
				<tr>
					<c:if test='${bbsMgtVO.adminUser}'>
						<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
					</c:if>
					<th><spring:message code="board.no"/></th>
					<th><spring:message code="bbsVO.title"/></th>
					<th><spring:message code="bbsVO.writer"/></th>
					<th><spring:message code="bbsVO.inptDttm"/></th>
					<th><spring:message code="bbsVO.viewCnt"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${list }" varStatus="i">
					<tr <c:if test='${bbsMgtVO.adminUser}'>class="chk"</c:if>>
						<c:if test='${bbsMgtVO.adminUser}'>
							<td class="chk-box">
								<c:choose>
									<c:when test='${item.delYn eq "Y" }'>
										&nbsp;
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="checkRow" value="<c:out value="${item.bbsId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
									</c:otherwise>
								</c:choose>						
							</td>
						</c:if>
						<td class="mobile"><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
						<td class="subj">
							<div class="tit" data-depth="<c:out value="${item.bbsDepth }"/>">
								<c:if test="${item.bbsDepth > 1}">
									<span class="ico_reply"></span>
								</c:if>
								<c:choose>
									<c:when test='${item.delYn eq "Y" }'>
										<spring:message code="board.deleteboard" />
									</c:when>
									<c:otherwise>
										<a class="link" href="javascript:fn_detailView('<c:out value="${item.bbsId }"/>');" title="<spring:message code="board.detail"/>">
											<c:if test='${bbsMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
												<strong>[<c:out value="${item.category}"/>]</strong>
											</c:if>
											<c:out value="${item.title}" escapeXml="false" />
										</a>
										<c:if test='${item.secret eq "Y"}'>
											<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
										</c:if>
									</c:otherwise>
								</c:choose>
							</div>
						</td>
						<td>
							<c:choose>
								<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
									<spring:message code="board.noname" />
								</c:when>
								<c:otherwise>
									<c:out value="${item.writer}"/>
								</c:otherwise>
							</c:choose>
						</td>	
						<td><c:out value='${fn:substring(item.inptDttm, 0, 10)}'/></td>
						<td><c:out value="${item.viewCnt}"/></td>
					
				</c:forEach>
				<c:if test="${empty list}">
					<tr>
						<c:choose>
							<c:when test="${bbsMgtVO.adminUser}">
								<td class="no_data" colspan="6"><spring:message code="board.noData" /></td>
							</c:when>
							<c:otherwise>
								<td class="no_data" colspan="5"><spring:message code="board.noData" /></td>	
							</c:otherwise>
						</c:choose>
					</tr>
				</c:if>
			</tbody>
		</table>
		   	
		<div class="btn_wrap">
			<ul>
				<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.regiAuthYn}'>
					<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
				</c:if>
				<c:if test='${bbsMgtVO.adminUser}'>
					<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
				</c:if>
			</ul>
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
	   	<input type="hidden" name="checkedSId">
	  		<input type="hidden" id="search_year" name="search_year" value="<c:out value='${search.search_year }'/>">	
		<input type="hidden" id="search_semstrCd" name="search_semstrCd" value="<c:out value='${search.search_semstrCd }'/>">
	  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	<% /** //검색조건 유지 */ %>
	</form:form>
	<script type="text/javaScript" defer="defer">
	
		//검색
		function fn_search(){
			var searchFrm = document.boardSearchForm;
			/* document.getElementById("_search_so_").value = searchFrm.so.value; */
			document.getElementById("_search_sv_").value = searchFrm.sv.value;
			document.getElementById("search_year").value = searchFrm.year.value;
			document.getElementById("search_semstrCd").value = searchFrm.semstrCd.value;
			gf_movePage(1);
		}
		
		//초기화
		function fn_reset() {
			document.getElementById("_search_sv_").value = "";
			document.getElementById("search_year").value = "";
			document.getElementById("search_semstrCd").value = "";
			location.href = GV_PRESENT_PATH + "/list.do";
		}
		
		//Row 선택
		function fn_checkRow() {
			var chkCnt = 0;
			var rowArr = document.getElementsByName("checkRow");
			var rowCnt = rowArr.length;
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox" && rowArr[i].checked) chkCnt++;
			}
			if(!gf_isNull(document.getElementById("checkAllRow"))){
				if (chkCnt == rowCnt) {
					document.getElementById("checkAllRow").checked = true;
				} else {
					document.getElementById("checkAllRow").checked = false;
				}
			}
		}
		
		//Row 전체선택
		function fn_checkAllRow() {
			var rowArr = document.getElementsByName("checkRow");
			var checked = document.getElementById("checkAllRow").checked;
			if (checked) {
				for (var i = 0; i < rowArr.length; i++) {
					if (rowArr[i].type == "checkbox") rowArr[i].checked = true;
				}
			} else {
				for (var i = 0; i < rowArr.length; i++) {
					if (rowArr[i].type == "checkbox") rowArr[i].checked = false;
				}
			}
		}
		
		//목록
		function fn_listView() {
			document.detailForm.action = GV_PRESENT_PATH + "/list.do";
			document.detailForm.submit();
		}
		
		//등록
		function fn_registerView() {
			document.listForm.action = "/board/<c:out value='${boardPath}'/>/register.do";
			document.listForm.submit();
		}
		
		//선택삭제
		function fn_removeCheckedRow() {
			var checkedSId = "";
			var chkCnt = 0;
			var rowArr = document.getElementsByName("checkRow");
			var rowCnt = rowArr.length;
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox" && rowArr[i].checked){
					if(i != 0){
						checkedSId += "|";
					}
					checkedSId += rowArr[i].value;
					chkCnt++;
				}
			}
			if(chkCnt <= 0) {
				alert('<spring:message code="message.alert.nocheck" />');
			} else {
				var msg = confirm('<spring:message code="message.confirm.remove" />');
				if(msg == true){				
					document.listForm.checkedSId.value = checkedSId;
					document.listForm.action = "/board/<c:out value='${boardPath}'/>/checkRemoveAction.do";
					document.listForm.submit();
				}
			}
		}
	
		//상세보기
		function fn_detailView(sId) {
			document.listForm.sId.value = sId;
			document.listForm.action = GV_PRESENT_PATH  + "/view.do";
			document.listForm.submit();
		}
		
	</script>
</div>
