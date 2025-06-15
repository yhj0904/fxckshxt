package kr.co.nanwe.cmmn.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class XmlUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);
	
	public static String objectToXml(String rootName, Object object) {
		if(StringUtil.isNull(rootName)) {
			rootName = "root";
		}
		StringBuffer xmlSb = new StringBuffer(); 
		xmlSb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlSb.append(System.getProperty("line.separator"));
		xmlSb.append("<"+rootName+">");
		xmlSb.append(System.getProperty("line.separator"));
		if(object != null) {
			String str = convertXML(null, object);
			xmlSb.append(str);
		}
		xmlSb.append(System.getProperty("line.separator"));
		xmlSb.append("</"+rootName+">");
		return xmlSb.toString();
	}
	
	private static String convertXML(Object key, Object object) {
		String str = "";
		if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double || object instanceof String) {
			if(key == null) {
				str += object;
			} else {
				str += "<"+key+">"+object+"</"+key+">";
			}
		} else if (object instanceof Map<?, ?>) { // 해당 파라미터가 Map 일 경우
			if(key == null) {
				str += getMap((Map<?, ?>) object);
			} else {
				str += "<"+key+">";
				str += System.getProperty("line.separator");
				str += getMap((Map<?, ?>) object);
				str += System.getProperty("line.separator");
				str += "</"+key+">";
			}
		} else if (object instanceof List) { // 해당 파라미터가 List일 경우
			if(key == null) {
				str += getList((List) object);
			} else {
				str += "<"+key+">";
				str += System.getProperty("line.separator");
				str += getList((List) object);
				str += System.getProperty("line.separator");
				str += "</"+key+">";
			}			
		} else if(object.getClass().isArray()){ // 배열인경우
			if(key == null) {
				str += getArray((Object[]) object);
			} else {
				str += "<"+key+">";
				str += System.getProperty("line.separator");
				str += getArray((Object[]) object);
				str += System.getProperty("line.separator");
				str += "</"+key+">";
			}			
		} else if(!(object instanceof Collection) ){ // 해당 파라미터가 사용자 정의 클래스일 경우
			if(key == null) {
				str += getVO(object);
			} else {
				str += "<"+key+">";
				str += System.getProperty("line.separator");
				str += getVO(object);
				str += System.getProperty("line.separator");
				str += "</"+key+">";
			}
		}
		return str;
	}
	
	private static String getMap(Map<?, ?> map) {		
		String str = "";
		if(map != null) {
			Collection<?> k = map.keySet();
			Iterator<?> itr = k.iterator();
			int idx = 0;
			while(itr.hasNext()){
				Object key = itr.next();
				Object object = map.get(key);
				if(object != null) {
					if(idx > 0) {
                		str += System.getProperty("line.separator");
                	}
                    idx ++;
                    str += convertXML(key, object);
				}
			}
		}
		return str;
	}	
	
	private static String getVO(Object vo) {
		String str = "";
		if(vo != null) {
			try {
				int idx = 0;
	            for (Field field : vo.getClass().getDeclaredFields()) {
	                field.setAccessible(true);
	                String key = field.getName();
					Object object = field.get(vo);
					if(object != null) {
						if(idx > 0) {
	                		str += System.getProperty("line.separator");
	                	}
	                    idx ++;
	                    str += convertXML(key, object);
					}
	            }
		    } catch (Exception e) {
		    	LOGGER.debug(e.getMessage());
			}
		}		
		return str;
	}
	
	private static String getList(List list) {
		String str = "";
		if(list != null) {
			int idx = 0;
            for(Object object : list) {
            	if(object != null) {
                	if(idx > 0) {
                		str += System.getProperty("line.separator");
                	}
                    idx ++;
                    str += convertXML("row", object);
            	}
            }
		}		
		return str;
	}
	
	private static String getArray(Object[] arr) {
		String str = "";
		if(arr != null) {
			int idx = 0;
            for(Object object : arr) {
            	if(object != null) {
                	if(idx > 0) {
                		str += System.getProperty("line.separator");
                	}
                    idx ++;
                    str += convertXML("item", object);
            	}
            }
		}		
		return str;
	}
}
