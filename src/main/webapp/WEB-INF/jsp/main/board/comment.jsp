<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%
/**
 * @Class Name 	: comment.jsp
 * @Description : 게시글 댓글 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="comment_wrap">
	<div class="comment_top">
		<a href="javascript:fn_openComment();" class="button comment_btn"><spring:message code="board.cmnt.list" /></a>
	</div>
	<div id="comment_body" class="comment_body">
		<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.cmntAuthYn}'>
			<div class="comment_input">
				<div class="writer">
					<ul>
						<c:choose>
							<c:when test='${!empty LOGIN_USER }'>
								<li>
									<span><c:out value="${LOGIN_USER.loginNm }"/></span>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<label for="comment_writer"><spring:message code="bbsCmntVO.writer" /></label>
									<input type="text" id="comment_writer" autocomplete="off"/>
								</li>
								<li>
									<label for="comment_pw"><spring:message code="bbsCmntVO.pw" /></label>
									<input type="password" id="comment_pw" autocomplete="off"/>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
				<textarea id="comment_txt" autocomplete="off"></textarea>
				<a href="javascript:fn_registerComment()" class="button register_btn"><spring:message code="board.cmnt.register" /></a>
			</div>
		</c:if>
		<div id="comment_list" class="comment_list"></div>
		<div id="comment_paging" class="comment_paging"></div>
	</div>
	<script type="text/javaScript">
	
		function fn_openComment(){
			if(!$("#comment_body").hasClass("open")){
				$("#comment_body").addClass("open");
				fn_moveCommentPage(1);
			} else {
				$("#comment_body").removeClass("open");
			}			
		}
	
		function fn_moveCommentPage(currentPage) {			
			gf_ajax({
				url : "/board/cmnt/list.json",
				type : "POST",
				dataType : "json",
				data : { currentPage : currentPage, bbsCd : '<c:out value="${bbsVO.bbsCd}"/>', bbsId : '<c:out value="${bbsVO.bbsId}"/>' },
			}).then(function(response){
				if(response.result){
					fn_getCommentList(response.list, response.paging);
				} else {
					alert("Error");
				}
			});
		}
		
		function fn_registerComment(){
			var params = {bbsCd : '<c:out value="${bbsVO.bbsCd}"/>', bbsId : '<c:out value="${bbsVO.bbsId}"/>'};
			var cmntTxt = $("#comment_txt").val();
			if (gf_isNull(cmntTxt)){
				alert("댓글 내용을 입력해주세요.");
				$("#comment_txt").focus();
				return;
			}
			params['cmntTxt'] = cmntTxt;
			if("<c:out value='${GV_IS_LOGIN}'/>" != "true"){
				var writer = $("#comment_writer").val();
				var pw = $("#comment_pw").val();
				if (gf_isNull(writer)){
					alert("작성자를 입력해주세요.");
					$("#comment_writer").focus();
					return;
				} else if (gf_isNull(pw)){
					alert("비밀번호를 입력해주세요.");
					$("#comment_pw").focus();
					return;
				} else {
					params['writer'] = writer;
					params['pw'] = pw;
				}
			}
			gf_ajax({
				url : "/board/cmnt/registerAction.json",
				type : "POST",
				dataType : "json",
				data : params,
			}).then(function(response){
				if(response.result){
					$("#comment_writer").val("");
					$("#comment_pw").val("");
					$("#comment_txt").val("");
					alert('<spring:message code="message.success.create" />');
					fn_getCommentList(response.list, response.paging);
				} else {
					alert("Error");
				}
			});
		}
		
		function fn_removeComment(cmntId){
			var msg = confirm('<spring:message code="message.confirm.remove" />');
			if(msg == true){				
				var currentPage = $("#comment_paging .page_btn.current_page span").html();				
				gf_ajax({
					url : "/board/cmnt/removeAction.json",
					type : "POST",
					dataType : "json",
					data : {  bbsCd : '<c:out value="${bbsVO.bbsCd}"/>', bbsId : '<c:out value="${bbsVO.bbsId}"/>', cmntId : cmntId, currentPage : currentPage},
				}).then(function(response){
					if(response.result){
						alert('<spring:message code="message.success.remove" />');
						fn_getCommentList(response.list, response.paging);
					} else {
						alert("Error");
					}
				});
			}
		}
		
		function fn_getCommentList(list, paging){
			
			//리스트 세팅
			if(list.length > 0){
				var listStr = '';
				listStr += '<ul>';
				$.each(list, function (idx, item) {
					listStr += '<li>';
					listStr += '<div class="writer">'+item.writer+'</div>';
					listStr += '<div class="txt">'+item.cmntTxt+'</div>';
					listStr += '<div class="date">'+item.inptDttm+'</div>';
					if(item.inptId == '<c:out value="${LOGIN_USER.loginId }"/>' || <c:out value="${bbsMgtVO.adminUser }"/>){
						listStr += '<a href="javascript:fn_removeComment(\''+item.cmntId+'\')" class="button remove_btn"><spring:message code="button.remove" /></a>';	
					}
					listStr += '</li>';
				});
				listStr += '</ul>';
				
				$("#comment_list").html(listStr);
				
				//페이징 세팅
				if(!gf_isNull(paging)) {
					var pageStr = '';							
					pageStr += '<div class="paging">';
					if (paging.currentPage != 1 && paging.currentPage != null && paging.currentPage != ''){
						pageStr += '<a href="javascript:fn_moveCommentPage(1)" title="<spring:message code="paging.first" />" class="page_btn page_first"><img src="/images/common/btn/paging_first.png"></a>';
					}
					if (paging.startPage > 1){
						pageStr += '<a href="javascript:fn_moveCommentPage('+(paging.startPage - paging.pageSize)+')" title="<spring:message code="paging.prev" />" class="page_btn page_prev">';
						pageStr += '	<img src="/images/common/btn/paging_prev.png" alt="<spring:message code="paging.prev" />">';
						pageStr += '</a>';
					}							
					for (var i=paging.startPage; i<=paging.endPage; i++){
						if(i == paging.currentPage){
							pageStr += '<a href="javascript:fn_moveCommentPage('+i+')" title="(<spring:message code="paging.current" />)<spring:message code="paging.move" arguments="'+i+'"/>" class="page_btn page_num current_page">';
						} else {
							pageStr += '<a href="javascript:fn_moveCommentPage('+i+')" title="'+i+'<spring:message code="paging.move" arguments="'+i+'"/>" class="page_btn page_num">';
						}
						pageStr += '	<span>'+i+'</span>';
						pageStr += '</a>';								
					}					
					if (paging.endPage != paging.lastPage){
						pageStr += '<a href="javascript:fn_moveCommentPage('+(paging.startPage + paging.pageSize)+')" title="<spring:message code="paging.next" />" class="page_btn page_next">';
						pageStr += '	<img src="/images/common/btn/paging_next.png" alt="<spring:message code="paging.next" />">';
						pageStr += '</a>';
					}							
					if (paging.currentPage != paging.lastPage && paging.lastPage != 0){
						pageStr += '<a href="javascript:fn_moveCommentPage('+paging.lastPage+')" title="<spring:message code="paging.last" />" class="page_btn page_last" >';
						pageStr += '	<img src="/images/common/btn/paging_last.png" alt="<spring:message code="paging.last" />">';
						pageStr += '</a>';
					}
					pageStr += '</div>';
					
					$("#comment_paging").html(pageStr);
				}
			} else {
				$("#comment_list").html("");
				$("#comment_paging").html("");
			}
		}
	</script>
</div>
