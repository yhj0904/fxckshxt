package kr.co.nanwe.cmmn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.nanwe.cmmn.util.SessionUtil;

public class SessionShareInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // 전자정부 LoginVO 세션 확인
        egovframework.com.cmm.LoginVO egovLoginVO = (egovframework.com.cmm.LoginVO) session.getAttribute("loginVO");
        // 나눔웨 LoginVO 세션 확인
        kr.co.nanwe.login.service.LoginVO nanweLoginVO = (kr.co.nanwe.login.service.LoginVO) session.getAttribute(SessionUtil.LOGIN_SESSION_KEY);
        
        if (egovLoginVO != null && nanweLoginVO == null) {
            // 전자정부 LoginVO를 나눔웨 LoginVO로 변환
            nanweLoginVO = convertEgovToNanwe(egovLoginVO);
            session.setAttribute(SessionUtil.LOGIN_SESSION_KEY, nanweLoginVO);
            SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, nanweLoginVO);
        } else if (nanweLoginVO != null && egovLoginVO == null) {
            // 나눔웨 LoginVO를 전자정부 LoginVO로 변환
            egovLoginVO = convertNanweToEgov(nanweLoginVO);
            session.setAttribute("loginVO", egovLoginVO);
        }
        
        return true;
    }
    
    private kr.co.nanwe.login.service.LoginVO convertEgovToNanwe(egovframework.com.cmm.LoginVO egovLoginVO) {
        kr.co.nanwe.login.service.LoginVO nanweLoginVO = new kr.co.nanwe.login.service.LoginVO();
        nanweLoginVO.setLoginId(egovLoginVO.getId());
        nanweLoginVO.setLoginNm(egovLoginVO.getName());
        nanweLoginVO.setLoginPw(egovLoginVO.getPassword());
        nanweLoginVO.setEmail(egovLoginVO.getEmail());
        nanweLoginVO.setDeptCd(egovLoginVO.getOrgnztId());
        nanweLoginVO.setLoginIp(egovLoginVO.getIp());
        // 필요한 다른 필드들도 추가
        return nanweLoginVO;
    }
    
    private egovframework.com.cmm.LoginVO convertNanweToEgov(kr.co.nanwe.login.service.LoginVO nanweLoginVO) {
        egovframework.com.cmm.LoginVO egovLoginVO = new egovframework.com.cmm.LoginVO();
        egovLoginVO.setId(nanweLoginVO.getLoginId());
        egovLoginVO.setName(nanweLoginVO.getLoginNm());
        egovLoginVO.setPassword(nanweLoginVO.getLoginPw());
        egovLoginVO.setEmail(nanweLoginVO.getEmail());
        egovLoginVO.setOrgnztId(nanweLoginVO.getDeptCd());
        egovLoginVO.setIp(nanweLoginVO.getLoginIp());
        // 필요한 다른 필드들도 추가
        return egovLoginVO;
    }
} 