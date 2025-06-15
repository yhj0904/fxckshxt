package kr.co.nanwe.cmmn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.login.service.LoginVO;

public class SessionShareInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // 세션에서 로그인 정보 가져오기
        LoginVO loginVO = (LoginVO) session.getAttribute(SessionUtil.LOGIN_SESSION_KEY);
        
        if (loginVO != null) {
            // 세션 정보를 RequestContextHolder에 저장
            SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, loginVO);
        }
        
        return true;
    }
} 