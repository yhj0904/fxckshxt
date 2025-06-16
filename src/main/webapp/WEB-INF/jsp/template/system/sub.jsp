<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
* @Class Name : sub.jsp
* @Description : 서브템플릿 (Spring 5.x 최적화)
* @Modification Information
* @
* @ 수정일 수정자 수정내용
* @ --------------------------------------------------------------------------
* @ 2020.01.06 임문환 최초생성
* @ 2025.06.15 개발자 Spring 5.x 최적화
*/
%>
<!DOCTYPE html>
<html lang="<c:out value='${GV_LANGUAGE}' default='ko'/>">
<head>
    <!-- 메타 태그 개선 -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <!-- 타이틀 설정 개선 -->
    <c:choose>
        <c:when test="${not empty GV_SITE_INFO}">
            <c:choose>
                <c:when test="${not empty GV_SITE_INFO.siteEngNm and GV_LANGUAGE eq 'en'}">
                    <title><c:out value="${GV_SITE_INFO.siteEngNm}"/></title>
                </c:when>
                <c:otherwise>
                    <title><c:out value="${GV_SITE_INFO.siteNm}"/></title>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <title><spring:message code="content.site.info.title" /></title>
        </c:otherwise>
    </c:choose>
    
    <!-- 공통 메타 태그 -->
    <c:import url="/WEB-INF/jsp/cmmn/meta.jsp"></c:import>
    
    <!-- 공통 CSS -->
    <c:import url="/WEB-INF/jsp/cmmn/css.jsp"></c:import>
    
    <!-- 시스템 레이아웃 CSS -->
    <link href="<c:url value='/css/template/system/layout.css'/>" 
          media="all" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/template/system/sub.css'/>" 
          media="all" rel="stylesheet" type="text/css">
    
    <!-- 게시판 스킨 CSS (조건부 로딩) -->
    <c:if test="${not empty bbsMgtVO and not empty GV_BOARD_SKIN_CODE}">
        <link href="<c:url value='/css/board/${GV_BOARD_SKIN_CODE}.css'/>" 
              media="all" rel="stylesheet" type="text/css">
    </c:if>
    
    <!-- 공통 JavaScript -->
    <c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>
</head>
<body>
    <!-- 접근성 개선: 스킵 네비게이션 -->
    <div class="skip-nav">
        <a href="#site_main" class="skip-link">본문 바로가기</a>
        <a href="#gnb" class="skip-link">메뉴 바로가기</a>
    </div>

    <!-- TOP 영역 -->
    <c:import url="/WEB-INF/jsp/cmmn/top.jsp"></c:import>

    <div class="wrap">
        <!-- HEADER -->
        <header role="banner">
            <c:import url="/WEB-INF/jsp/template/system/header.jsp"></c:import>
        </header>

        <!-- MAIN CONTENT -->
        <main role="main" class="main">
            <div id="site_main">
                <!-- 페이지 타이틀 -->
                <c:if test="${not empty GV_PROG_NM}">
                    <div class="stitle">
                        <h1>
                            <c:choose>
                                <c:when test="${not empty bbsMgtVO and GV_PRESENT_PATH eq '/sys/bbs'}">
                                    <c:out value="${bbsMgtVO.title}"/>
                                </c:when>
                                <c:when test="${not empty schMgtVO and GV_PRESENT_PATH eq '/sys/sch'}">
                                    <c:out value="${schMgtVO.title}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${GV_PROG_NM}"/>
                                </c:otherwise>
                            </c:choose>
                        </h1>
                    </div>
                </c:if>
                
                <!-- 브레드크럼 네비게이션 (선택사항) -->
                <c:if test="${not empty breadcrumbs}">
                    <nav class="breadcrumb" role="navigation" aria-label="breadcrumb">
                        <ol class="breadcrumb-list">
                            <c:forEach items="${breadcrumbs}" var="crumb" varStatus="status">
                                <li class="breadcrumb-item ${status.last ? 'active' : ''}">
                                    <c:choose>
                                        <c:when test="${status.last}">
                                            <span><c:out value="${crumb.name}"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='${crumb.url}'/>">
                                                <c:out value="${crumb.name}"/>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </c:forEach>
                        </ol>
                    </nav>
                </c:if>
                
                <!-- 실제 콘텐츠 영역 -->
                <div class="content-body">
                    <tiles:insertAttribute name="body"/>
                </div>
            </div>
        </main>

        <!-- FOOTER -->
        <footer role="contentinfo">
            <c:import url="/WEB-INF/jsp/template/system/footer.jsp"></c:import>
        </footer>
    </div>

    <!-- BOTTOM 스크립트 -->
    <c:import url="/WEB-INF/jsp/cmmn/bottom.jsp"></c:import>
    
    <!-- 에러 메시지 표시 (있을 경우) -->
    <c:if test="${not empty errorMessage}">
        <script type="text/javascript">
            alert('<c:out value="${errorMessage}" escapeXml="true"/>');
        </script>
    </c:if>
    
    <!-- 성공 메시지 표시 (있을 경우) -->
    <c:if test="${not empty successMessage}">
        <script type="text/javascript">
            alert('<c:out value="${successMessage}" escapeXml="true"/>');
        </script>
    </c:if>
</body>
</html>