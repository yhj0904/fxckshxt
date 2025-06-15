package kr.co.nanwe.log.service;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;

import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.annotation.PrivacyLog;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: SysLogAspect
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class SysLogAspect {
	
	private final String PREFIX_CODE = "SYS_";
	
	@Resource(name="sysLogService")
	private SysLogService sysLogService;

	/**
	 * 시스템 로그정보를 생성한다.
	 * sevice Class의 insert로 시작되는 Method
	 *
	 * @param ProceedingJoinPoint
	 * @return Object
	 * @throws Exception
	 */
	public Object logInsert(ProceedingJoinPoint joinPoint) throws Throwable {
		String processCode = "C";
		return startStopWatch(joinPoint, processCode);
	}
 
	/**
	 * 시스템 로그정보를 생성한다.
	 * sevice Class의 update로 시작되는 Method
	 *
	 * @param ProceedingJoinPoint
	 * @return Object
	 * @throws Exception
	 */
	public Object logUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
		String processCode = "U";
		return startStopWatch(joinPoint, processCode);
 
	}
 
	/**
	 * 시스템 로그정보를 생성한다.
	 * sevice Class의 delete로 시작되는 Method
	 *
	 * @param ProceedingJoinPoint
	 * @return Object
	 * @throws Exception
	 */
	public Object logDelete(ProceedingJoinPoint joinPoint) throws Throwable {
		String processCode = "D";
		return startStopWatch(joinPoint, processCode);
 
	}
 
	/**
	 * 시스템 로그정보를 생성한다.
	 * sevice Class의 select로 시작되는 Method
	 *
	 * @param ProceedingJoinPoint
	 * @return Object
	 * @throws Exception
	 */
	public Object logSelect(ProceedingJoinPoint joinPoint) throws Throwable {
		String processCode = "R";
		return startStopWatch(joinPoint, processCode);
	}
	
	/**
	 * 시스템 로그정보를 생성한다.
	 * sevice Class의 excelUpload로 시작되는 Method
	 *
	 * @param ProceedingJoinPoint
	 * @return Object
	 * @throws Exception
	 */
	public Object logUploadExcel(ProceedingJoinPoint joinPoint) throws Throwable {
		String processCode = "E";
		return startStopWatch(joinPoint, processCode);
	}
	
	/**
	 * 
	 * 시스템 로그코드 STOPWATCH
	 * @return 
	 * @throws Throwable 
	 */
	private Object startStopWatch(ProceedingJoinPoint joinPoint, String processCode) throws Throwable {
		
		String sysCode = "";
		String className = "";
		String methodName = "";
		String methodDesc = "";
		String errYn = "0";
		String errMsg = "";
		String dbLogYn = "Y";
		String privacyYn = "N";
		
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		
		StopWatch stopWatch = new StopWatch();
		
		try { 
			
			sysCode = getSysCode(processCode);
			
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			className = joinPoint.getTarget().getClass().getName();
			methodName = signature.getName();
			Method method = signature.getMethod();
			
			if (method.getDeclaringClass().isInterface()) {
		        method = joinPoint.getTarget().getClass().getDeclaredMethod(methodName, method.getParameterTypes());
		        
		        MethodDescription methodDescription = method.getAnnotation(MethodDescription.class);
		        if(methodDescription != null) {
		        	methodDesc = methodDescription.value();
		        }
		        
		        PrivacyLog privacyLog = method.getAnnotation(PrivacyLog.class);
		        if(privacyLog != null) {
		        	privacyYn = privacyLog.value();
		        }
		    }

			//DB로그 불필요시 processCode 식별자를 통해 N 처리
			if(processCode.equals("E") || processCode.equals("Y")) {
				dbLogYn = "N";
			}
			
			//SELECT이면서 개인정보가 접근이 아닌경우 로그기록 PASS
			if(processCode.equals("R") && privacyYn.equals("N")) {
				dbLogYn = "N";
			}
			
			request.setAttribute("DB_LOG_YN", dbLogYn);
			request.setAttribute("SYS_LOG_CODE", sysCode);
			
			stopWatch.start(); 
			Object retValue = joinPoint.proceed();
			return retValue;
			
		} catch (Throwable e) {
			
			errYn = "1";
			errMsg = e.getMessage();
			throw e;
			
		} finally {
			
			stopWatch.stop(); 
			
			String processTime = Long.toString(stopWatch.getTotalTimeMillis());
			
			if(!"kr.co.nanwe.log.service.impl.SysLogServiceImpl".equals(className) 
					&& !"kr.co.nanwe.login.service.impl.LoginServiceImpl".equals(className)
					&& !"kr.co.nanwe.template.service.impl.TemplateServiceImpl".equals(className)
					&& !("kr.co.nanwe.bbs.service.impl.BbsServiceImpl".equals(className) && "updateBbsViewCnt".equals(methodName))
					&& !("kr.co.nanwe.site.service.impl.SiteServiceImpl".equals(className) && "updateSiteCode".equals(methodName))) {
				//SELECT이면서 개인정보가 접근이 아닌경우 로그기록 PASS
				if(!(processCode.equals("R") && privacyYn.equals("N"))) {
					insertSysLog(request, sysCode, processCode, className, methodName, methodDesc, processTime, errYn, errMsg, privacyYn);
				}
			}
			
			//시스템 로그코드 제거
			if(request.getAttribute("SYS_LOG_CODE") != null) {
				request.removeAttribute("SYS_LOG_CODE");
			}
 
		} 
	}
	
	/**
	 * 시스템 로그코드를 생성한다
	 * @return sysCode
	 * @throws 
	 */
	private String getSysCode(String processCode) {
		String sysCode = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Calendar cal = Calendar.getInstance();
		String dttm = sdf.format(cal.getTime());
		SecureRandom rand = new SecureRandom();
		int randN = rand.nextInt(10);
		sysCode = PREFIX_CODE + processCode + dttm + randN;
		return sysCode;
	}

	
	/**
	 * 시스템 로그코드 DB 입력
	 * @return 
	 * @throws 
	 */
	private void insertSysLog(HttpServletRequest request, String sysCode, String processCode, String className, String methodName, String methodDesc, String processTime, String errYn, String errMsg, String privacyYn) {
		
		String programUri = "";
		
		String programCd = "";
		String programNm = "";
		String infoCd = "";
		String infoNm = "";

		
		if(request.getAttribute("SYS_LOG_PROGRAM_URI") != null && !"".equals(request.getAttribute("SYS_LOG_PROGRAM_URI"))) {
			programUri = (String) request.getAttribute("SYS_LOG_PROGRAM_URI");
		}
		
		if(request.getAttribute("SYS_LOG_PROGRAM_CD") != null && !"".equals(request.getAttribute("SYS_LOG_PROGRAM_CD"))) {
			programCd = (String) request.getAttribute("SYS_LOG_PROGRAM_CD");
		}
		
		if(request.getAttribute("SYS_LOG_PROGRAM_NM") != null && !"".equals(request.getAttribute("SYS_LOG_PROGRAM_NM"))) {
			programNm = (String) request.getAttribute("SYS_LOG_PROGRAM_NM");
		}
		
		if(request.getAttribute("SYS_LOG_PROGRAM_INFO_CD") != null && !"".equals(request.getAttribute("SYS_LOG_PROGRAM_INFO_CD"))) {
			infoCd = (String) request.getAttribute("SYS_LOG_PROGRAM_INFO_CD");
		}
		
		if(request.getAttribute("SYS_LOG_PROGRAM_INFO_NM") != null && !"".equals(request.getAttribute("SYS_LOG_PROGRAM_INFO_NM"))) {
			infoNm = (String) request.getAttribute("SYS_LOG_PROGRAM_INFO_NM");
		}		
		
		//로그인정보
		String logId = "";
		String logName = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			logId = loginInfo.getLoginId();
			logName = loginInfo.getLoginNm();
		}
		String logIp = ClientUtil.getUserIp();
		String logOs = ClientUtil.getUserOS();
		String logDevice = ClientUtil.getUserDevice();
		HashMap<String,String> browser = ClientUtil.getUserBrowser();
		String typeKey = browser.get(ClientUtil.TYPEKEY);
		String versionKey = browser.get(ClientUtil.VERSIONKEY);
		String logBrowser = typeKey + " " + versionKey;
		String logUrl = RequestUtil.getRequestURL();
		
		String logParam = "";
		Map<String, Object> logParamMap = null;
		Enumeration<String> params = request.getParameterNames();
		if(params != null) {
			logParamMap = new HashMap<String, Object>();
			while (params.hasMoreElements()){				
				String name = (String)params.nextElement();	
				
				//전자정부 중복방지 파라미터는 continue;
				if("egovframework.double.submit.preventer.parameter.name".equals(name)) {
					continue;
				}
				//값이 없는 경우 continue
				if(StringUtil.isNull(request.getParameter(name))) {
					continue;
				}
				if(RequestUtil.PASS_PARAMS.contains(name.toUpperCase()) || RequestUtil.DENIED_PARAM_LIST.contains(name)) {
					logParamMap.put(name, "?");
				} else {
					logParamMap.put(name, request.getParameter(name));
				}
				
			}
			if(logParamMap.size() > 0) {
				logParam = logParamMap.toString();
			}
		}
		

		SysLogVO sysLog = new SysLogVO();
		sysLog.setProgramCd(programCd);
		sysLog.setProgramNm(programNm);
		sysLog.setInfoCd(infoCd);
		sysLog.setInfoNm(infoNm);
		sysLog.setProgramUri(programUri);
		sysLog.setSysCode(sysCode);
		sysLog.setClassName(className);
		sysLog.setMethodName(methodName);
		sysLog.setMethodDesc(methodDesc);
		sysLog.setProcessCode(processCode);
		sysLog.setProcessTime(processTime);
		sysLog.setLogId(logId);
		sysLog.setLogName(logName);
		sysLog.setLogIp(logIp);
		sysLog.setLogOs(logOs);
		sysLog.setLogDevice(logDevice);
		sysLog.setLogBrowser(logBrowser);
		sysLog.setLogUrl(logUrl);
		sysLog.setErrYn(errYn);
		sysLog.setErrMsg(errMsg);
		sysLog.setPrivacyYn(privacyYn);
		sysLog.setLogParam(logParam);
		
		sysLogService.insertSysLog(sysLog);
	}
}
