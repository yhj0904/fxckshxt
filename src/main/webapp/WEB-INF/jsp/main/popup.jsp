<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: popup.jsp
 * @Description : 메인팝업
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<c:if test="${!empty COM_POPUP_LIST }">
	
	<div class="popup_wrap">
		<c:forEach var="popup" items="${COM_POPUP_LIST}" varStatus="i">
		
			<fmt:parseNumber var="pTopVal" value="${(popup.popHeight+70)/2}" integerOnly="true" />
			<fmt:parseNumber var="pLeftVal" value="${popup.popWidth/2}" integerOnly="true" />
			
			<c:set var="pTop" value="calc(50% - ${pTopVal }px)"/>
			<c:set var="pLeft" value="calc(50% - ${pLeftVal }px)"/>
			
			<div id="COM_POPUP_<c:out value='${popup.popId }'/>" class="modal_popup" tabindex="0" style="top:<c:out value='${pTop }'/>; left:<c:out value='${pLeft }'/>; width:<c:out value='${popup.popWidth }'/>px; z-index:<c:out value='${i.index + 8000 }'/>" data-seq="${i.count }">
				<div class="tit">
					<c:out value='${popup.popNm }'/>
					<a class="close" href="javascript:fn_closeLayerPopup('<c:out value="${popup.popId }"/>')"><span class="none"><spring:message code="button.close"/></span></a>
				</div>
				<div class="con" style="max-width:<c:out value='${popup.popWidth }'/>px; max-height:<c:out value='${popup.popHeight }'/>px;">
					<c:choose>
						<c:when test='${popup.popType eq "IMAGE"}'>
							<%-- <a class="ir" href="<c:out value='${popup.popLink }'/>" title="새창열림" target="_blank" style="width:<c:out value='${popup.popWidth }'/>px; height:<c:out value='${popup.popHeight }'/>px;background-image:url(<c:out value='${popup.viewFile.viewUrl }'/>);">
								<span><c:out value='${popup.imgExplain }'/></span>
							</a> --%>
							<a class="ir" href="<c:out value='${popup.popLink }'/>" title="새창열림" target="_blank" style="max-width:<c:out value='${popup.popWidth }'/>px; max-height:<c:out value='${popup.popHeight }'/>px;">
								<img src="<c:out value='${popup.viewFile.viewUrl }'/>" style="width: 100%;">
								<span><c:out value='${popup.imgExplain }'/></span>
							</a>
						</c:when>
						<c:when test='${popup.popType eq "HTML"}'>
							<c:out value="${popup.popCont }" escapeXml="false"/>
						</c:when>
					</c:choose>
				</div>
				<div class="bot">
					<label for="COM_POP_<c:out value='${popup.popId }'/>" class="check">
						<input type="checkbox" id="COM_POP_<c:out value='${popup.popId }'/>" class="checkPop" onclick="fn_closeLayerPopupToday('<c:out value='${popup.popId }'/>')"><i></i><spring:message code="button.close.today"/>
					</label>
				</div>
			</div>
		</c:forEach>
		<script>
			$(function(){
				$(".modal_popup").draggable();
				
				$(".checkPop").focusout(function(){
					$(this).parents(".modal_popup").remove();
				});
			})
			function fn_closeLayerPopup(popId) {
				$("#COM_POPUP_"+popId).remove();			
			};
			function fn_closeLayerPopupToday(popId){
				gf_setCookie(popId, 1, 1);
				fn_closeLayerPopup(popId);
			};
		</script>
	</div>
</c:if>