package kr.co.nanwe.log.service;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.util.RequestUtil;

/**
 * @Class Name 		: ProgramLogAspect
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ProgramLogAspect {

	/**
	 * 시스템 로그기록을 위한 프로그램 정보를 생성한다.
	 * *Controller
	 *
	 * @param JoinPoint
	 * @return Object
	 * @throws Exception
	 */	
	public void setProgramInfo(JoinPoint joinPoint) throws Throwable {
		
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		
		Program program = method.getDeclaringClass().getAnnotation(Program.class);
		if(program != null) {
			
			String uri = RequestUtil.getURI();
			request.setAttribute("SYS_LOG_PROGRAM_URI", uri);
			
			request.setAttribute("SYS_LOG_PROGRAM_CD", program.code());
			request.setAttribute("SYS_LOG_PROGRAM_NM", program.name());
			
			ProgramInfo progrmaInfo = method.getAnnotation(ProgramInfo.class);
			if(progrmaInfo != null) {
				request.setAttribute("SYS_LOG_PROGRAM_INFO_CD", progrmaInfo.code());
				request.setAttribute("SYS_LOG_PROGRAM_INFO_NM", progrmaInfo.name());
			}			
		}
	}
	
	/**
	 * 시스템 로그기록을 위한 프로그램 정보를 제거한다
	 * 패키지 하위의 *Controller
	 *
	 * @param JoinPoint
	 * @return Object
	 * @throws Exception
	 */	
	public void removeProgramInfo(JoinPoint joinPoint) throws Throwable {
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		if(request.getAttribute("SYS_LOG_PROGRAM_URI") != null) {
			request.removeAttribute("SYS_LOG_PROGRAM_URI");
		}
		if(request.getAttribute("SYS_LOG_PROGRAM_CD") != null) {
			request.removeAttribute("SYS_LOG_PROGRAM_CD");
		}
		if(request.getAttribute("SYS_LOG_PROGRAM_NM") != null) {
			request.removeAttribute("SYS_LOG_PROGRAM_NM");
		}
		if(request.getAttribute("SYS_LOG_PROGRAM_INFO_CD") != null) {
			request.removeAttribute("SYS_LOG_PROGRAM_INFO_CD");
		}
		if(request.getAttribute("SYS_LOG_PROGRAM_INFO_NM") != null) {
			request.removeAttribute("SYS_LOG_PROGRAM_INFO_NM");
		}	
	}
 
}
