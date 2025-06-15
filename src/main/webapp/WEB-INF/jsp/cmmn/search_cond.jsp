<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: search_cond.jsp
 * @Description : 검색조건
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<input type="hidden" id="_search_cp_" name="cp" value="<c:out value='${search.cp }'/>">
<input type="hidden" id="_search_so_" name="so" value="<c:out value='${search.so }'/>">
<input type="hidden" id="_search_sv_" name="sv" value="<c:out value='${search.sv }'/>">
<input type="hidden" id="_search_sd_" name="sd" value="<c:out value='${search.sd }'/>">
<input type="hidden" id="_search_ed_" name="ed" value="<c:out value='${search.ed }'/>">
<input type="hidden" id="_search_oc_" name="oc" value="<c:out value='${search.oc }'/>">
<input type="hidden" id="_search_ob_" name="ob" value="<c:out value='${search.ob }'/>">