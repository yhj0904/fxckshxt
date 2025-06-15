package kr.co.nanwe.rest.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @Class Name 		: XmlAbstractView
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component
public class XmlAbstractView extends AbstractView {
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer xmlSb = new StringBuffer();
		xmlSb.append(map.get("xmlResult"));
		response.setContentType("application/xml");		
		response.setCharacterEncoding("utf-8");		
		response.setHeader("Cache-Control", "no-cache");		
		response.setContentLength(xmlSb.toString().getBytes("utf-8").length);
		response.getWriter().print(xmlSb.toString());
	}
}
