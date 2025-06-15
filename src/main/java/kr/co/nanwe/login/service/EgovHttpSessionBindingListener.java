package kr.co.nanwe.login.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import kr.co.nanwe.cmmn.util.SessionUtil;

public class EgovHttpSessionBindingListener implements HttpSessionBindingListener{
	
	private static EgovHttpSessionBindingListener egovHttpSessionBindingListener = null;
	
	public static synchronized EgovHttpSessionBindingListener getInstance(){
        if(egovHttpSessionBindingListener == null){
        	egovHttpSessionBindingListener = new EgovHttpSessionBindingListener();
        }
        return egovHttpSessionBindingListener;
    }
	
    @Override
    public void valueBound(HttpSessionBindingEvent event){
        if (EgovMultiLoginPreventor.findBySessionKey(event.getName())){
            EgovMultiLoginPreventor.invalidateBySessionKey(event.getName());
        }
        EgovMultiLoginPreventor.loginUsers.put(event.getName(), event.getSession());
    }
 
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        EgovMultiLoginPreventor.loginUsers.remove(event.getName(), event.getSession());
    }
    
    /*
     * 현재 접속한 총 사용자 수
     * @return int  현재 접속자 수
     */
    public int getUserCount(){
        return EgovMultiLoginPreventor.loginUsers.size();
    }
    
    /*
     * 현재 접속중인 총 사용자 리스트
     * @return List<LoginVO>  현재 접속자 수
     */
    public List<LoginVO> getUserList(){
    	List<LoginVO> list = new ArrayList<LoginVO>();
    	Enumeration<String> e = EgovMultiLoginPreventor.loginUsers.keys();
        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            LoginVO loginVO = (LoginVO) EgovMultiLoginPreventor.loginUsers.get(key).getAttribute(SessionUtil.LOGIN_SESSION_KEY);
            if(loginVO != null) {
            	loginVO.setSessionKey(key);
            	list.add(loginVO);
            }
        }
        return list;
    }
    
    /*
     * 세션 종료
     */
    public void removeUserBySessionKey(String sessionKey){
    	if (EgovMultiLoginPreventor.findBySessionKey(sessionKey)){
            EgovMultiLoginPreventor.invalidateBySessionKey(sessionKey);
        }
    }
}