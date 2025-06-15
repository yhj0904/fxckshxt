package kr.co.nanwe.login.service;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

public class EgovMultiLoginPreventor {
    public static ConcurrentHashMap<String, HttpSession> loginUsers = new ConcurrentHashMap<String, HttpSession>();
 
    public static boolean findBySessionKey(String sessionKey){
        return loginUsers.containsKey(sessionKey);
    }
 
    public static void invalidateBySessionKey(String sessionKey){
        Enumeration<String> e = loginUsers.keys();
        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            if (key.equals(sessionKey)){
                loginUsers.get(key).invalidate();
            }
        }
    }
}
