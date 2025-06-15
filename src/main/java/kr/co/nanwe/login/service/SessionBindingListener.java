package kr.co.nanwe.login.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import kr.co.nanwe.cmmn.util.SessionUtil;

public class SessionBindingListener implements HttpSessionBindingListener{
	
	public static ConcurrentHashMap<String, HttpSession> loginUsers = new ConcurrentHashMap<String, HttpSession>();
	
	private static SessionBindingListener sessionBindingListener = null;
	
	public static synchronized SessionBindingListener getInstance(){
        if(sessionBindingListener == null){
        	sessionBindingListener = new SessionBindingListener();
        }
        return sessionBindingListener;
    }
	
    @Override
    public void valueBound(HttpSessionBindingEvent event){
        loginUsers.put(event.getName(), event.getSession());
    }
 
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        loginUsers.remove(event.getName(), event.getSession());
    }
    
    /*
     * 현재 접속한 총 사용자 수
     * @return int  현재 접속자 수
     */
    public int getUserCount(){
        return loginUsers.size();
    }
    
    /*
     * 현재 접속중인 총 사용자 리스트
     * @return List<LoginVO>  현재 접속자 수
     */
    public List<LoginVO> getUserList(){
    	List<LoginVO> list = new ArrayList<LoginVO>();
    	Enumeration<String> e = loginUsers.keys();
        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            LoginVO loginVO = (LoginVO) loginUsers.get(key).getAttribute(SessionUtil.LOGIN_SESSION_KEY);
            if(loginVO != null) {
            	loginVO.setSessionKey(key);
            	list.add(loginVO);
            }
        }
        return list;
    }
        
    /*
     * 현재 접속중인지 체크
     */
    public boolean findBySessionKey(String sessionKey){
        return loginUsers.containsKey(sessionKey);
    }
    
    /*
     * 세션 종료
     */
    public void removeUserBySessionKey(String sessionKey){
    	if(findBySessionKey(sessionKey)) {
    		Enumeration<String> e = loginUsers.keys();
            while (e.hasMoreElements()){
                String key = (String) e.nextElement();
                if (key.equals(sessionKey)){
                    loginUsers.get(key).invalidate();
                }
            }
    	}
    }
}