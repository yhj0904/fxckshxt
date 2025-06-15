package kr.co.nanwe.log.interceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: SqlLogInterceptor
 * @Description 	: DB로그기록을 위한 MyBatis 인터셉터
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Intercepts(
{ 
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})

public class SqlLogInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlLogInterceptor.class);
	
	private static final List<String> PASS_SQL_ID_LIST = new ArrayList<String>(Arrays.asList(
			  "kr.co.nanwe.file.service.impl.ComEditorFilesMapper.selectComEditorFilesByNoParent" //임시에디터파일 조회
			, "kr.co.nanwe.file.service.impl.ComEditorFilesMapper.deleteComEditorFilesByNoParent" //임시에디터파일 삭제
			, "kr.co.nanwe.template.service.impl.TemplateMapper.insertTemplateCode" //템플릿 코드 등록
			, "kr.co.nanwe.site.service.impl.SiteMapper.updateSiteCode" //사이트 수정
		));

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
	
		Object[] args = invocation.getArgs(); 
		MappedStatement ms = (MappedStatement)args[0]; 
		Object param = (Object)args[1]; 
		BoundSql boundSql = ms.getBoundSql(param);
		String sqlId = ms.getId();
		String sqlMethod = invocation.getMethod().getName();
		
		//세션 정보를 세팅한다.
		if(param != null) {
			if(param instanceof Map) {
				LoginVO loginVO = SessionUtil.getLoginUser();
				if(loginVO == null) {
					loginVO = new LoginVO();
				}
				@SuppressWarnings("unchecked")
				Map<String, Object> paramMap = (Map<String, Object>) param;
				paramMap.put("SESSION", loginVO);
				param = paramMap;
			}
		}
		
		//로그기록을 패스할 쿼리 아이디 리스트 체크
		if(PASS_SQL_ID_LIST.contains(sqlId)) {
			return invocation.proceed();
		}
		
		//로그인인경우 PASS
		if(sqlId.startsWith("kr.co.nanwe.login")) {
			return invocation.proceed();
		}
		
		String sysCode = "";
		String dbLogYn = "N";		
		
		//로그 코드 체크 후 시스템 로그에 관해서만 로그 기록
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		
		if(request.getAttribute("SYS_LOG_CODE") != null) {
			sysCode = (String) request.getAttribute("SYS_LOG_CODE");
		}
		
		if(sysCode == null || "".equals(sysCode)) {
			return invocation.proceed();
		}
		
		//DB로그 여부 확인 후 로그기록이 아닐 경우 진행
		if(request.getAttribute("DB_LOG_YN") != null) {
			dbLogYn = (String) request.getAttribute("DB_LOG_YN");
		}
		if(dbLogYn == null || "".equals(dbLogYn)) {
			dbLogYn = "N";
		}
		
		String logSql = boundSql.getSql();
		if(logSql != null) {
			if (param != null) {
				//파라미터 매핑
				List<ParameterMapping> paramList = boundSql.getParameterMappings();
				
				logSql = logSql.replaceAll("\\?", "#\\{PARAMETER\\}");		
				
				if (param instanceof Integer || param instanceof Long || param instanceof Float || param instanceof Double|| param instanceof String) {
					for (ParameterMapping mapping : paramList) {		
						String propValue = mapping.getProperty();
						logSql = getLogSql(propValue, param, logSql);
					}					
				} else if(param instanceof Map<?, ?>) { // 해당 파라미터가 Map 일 경우				
					for (ParameterMapping mapping : paramList) {		
						String propValue = mapping.getProperty();
						logSql = getMap(logSql, param, mapping, propValue);
					}
				} else { // 해당 파라미터가 사용자 정의 클래스일 경우
					for (ParameterMapping mapping : paramList) {
						String propValue = mapping.getProperty();
						logSql = getVo(logSql, param, mapping, propValue);
					}
				}		
				
				logSql = logSql.replaceAll("#\\{PARAMETER\\}", "\\?");
			} else {
				logSql = logSql.replaceAll("\\?", "#\\{PARAMETER\\}");
				List<ParameterMapping> paramList = boundSql.getParameterMappings();
				for (ParameterMapping mapping : paramList) {		
					String propValue = mapping.getProperty();
					logSql = getLogSql(propValue, param, logSql);
				}
				logSql = logSql.replaceAll("#\\{PARAMETER\\}", "\\?");
			}
		}
		
		//LOGGER 출력
		String ip = ClientUtil.getUserIp();
		if("localhost".equals(ip) || "127.0.0.1".equals(ip)) {
			printDbLog(sqlId, sqlMethod, logSql);
		}
		
		//DB LOG INSERT
		if(dbLogYn.equals("Y")) {
			insertDbLog(request, sysCode, sqlId, sqlMethod, logSql);
		}
		
		return invocation.proceed(); // 쿼리 실행
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
	@Override
	public void setProperties(Properties properties) {
	
	}
	
	private String getLogSql(String propValue, Object param, String logSql) {
		if(propValue != null && propValue.startsWith("SESSION")) {
			LoginVO loginVO = SessionUtil.getLoginUser();
			int dotIndex = propValue.indexOf(".");
			String value = "null";
			if(loginVO != null && dotIndex > -1) {
				String property = propValue.substring(dotIndex + 1);
				try {
					Field field = loginVO.getClass().getDeclaredField(property);
					field.setAccessible(true);
					if(field.get(loginVO) != null) {
						value = "'" + (String) field.get(loginVO) + "'";
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					value = "null";
				}				
			}
			logSql = logSql.replaceFirst("#\\{PARAMETER\\}", value);
		} else if (param != null) {
			if (param instanceof Integer || param instanceof Long || param instanceof Float || param instanceof Double) {
				logSql = logSql.replaceFirst("#\\{PARAMETER\\}", param.toString());
			} else if (param instanceof String) { // 해당 파라미터의 클래스가 String 일 경우 '' 처리
				if(((String) param).getBytes().length > 4000) {
					logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
				} else {
					logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "'" + param + "'");
				}
			} else {
				logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "'" + param.toString() + "'");
			}
		} else {
			logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "null");
		}
		return logSql;
	}
	
	private String getMap(String logSql, Object param, ParameterMapping mapping, String propValue) {
		if(propValue != null && propValue.startsWith("SESSION")) {
			return getLogSql(propValue, param, logSql);
		} else {
			try {
				if(propValue.contains(".")) {
					int dotIndex = propValue.indexOf(".");
					String prefixPropValue = propValue.substring(0, dotIndex);
					if(((Map<?, ?>) param).get(prefixPropValue) != null) {						
						propValue = propValue.substring(dotIndex + 1);
						if(propValue.contains(".")) {
							if (((Map<?, ?>) param).get(prefixPropValue) instanceof Map<?, ?>) { // 해당 파라미터가 Map 일 경우
								logSql = getMap(logSql, ((Map<?, ?>) param).get(prefixPropValue), mapping, propValue);
							} else { // 해당 파라미터가 사용자 정의 클래스일 경우
								logSql = getVo(logSql, ((Map<?, ?>) param).get(prefixPropValue), mapping, propValue);
							}
						} else {
							if (((Map<?, ?>) param).get(prefixPropValue) instanceof Map<?, ?>) { // 해당 파라미터가 Map 일 경우
								logSql = getSqlByMap(logSql, ((Map<?, ?>) param).get(prefixPropValue), propValue);
							} else { // 해당 파라미터가 사용자 정의 클래스일 경우
								logSql = getSqlByClass(logSql, ((Map<?, ?>) param).get(prefixPropValue), mapping, propValue);
							}
						}
					} else {
						logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
					}
				} else {
					logSql = getSqlByMap(logSql, param, propValue);
				}				
			} catch (Throwable e) {
				logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
			}
			return logSql;
		}
	}
	
	private String getVo(String logSql, Object param, ParameterMapping mapping, String propValue) {
		if(propValue != null && propValue.startsWith("SESSION")) {
			return getLogSql(propValue, param, logSql);
		} else {
			try {
				if(propValue != null && propValue.startsWith("SESSION")) {
					logSql = getLogSql(propValue, param, logSql);
				} else if(propValue.contains(".")) {
					int dotIndex = propValue.indexOf(".");
					String prefixPropValue = propValue.substring(0, dotIndex);
					Class<? extends Object> paramClass = param.getClass();			
					Field field = paramClass.getDeclaredField(prefixPropValue);
					field.setAccessible(true);
					if(field.get(param) != null) {
						propValue = propValue.substring(dotIndex + 1);
						if(propValue.contains(".")) {
							if (field.get(param) instanceof Map<?, ?>) { // 해당 파라미터가 Map 일 경우
								logSql = getMap(logSql, field.get(param), mapping, propValue);
							} else { // 해당 파라미터가 사용자 정의 클래스일 경우
								logSql = getVo(logSql, field.get(param), mapping, propValue);
							}
						} else {
							if (field.get(param) instanceof Map<?, ?>) { // 해당 파라미터가 Map 일 경우
								logSql = getSqlByMap(logSql, field.get(param), propValue);
							} else { // 해당 파라미터가 사용자 정의 클래스일 경우
								logSql = getSqlByClass(logSql, field.get(param), mapping, propValue);
							}
						}
					} else {
						logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
					}
				} else {
					logSql = getSqlByClass(logSql, param, mapping, propValue);
				}
			} catch (Throwable e) {
				logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
			}
			return logSql;
		}
	}
	
	private String getSqlByMap(String logSql, Object param, String propValue) throws Throwable {		
		if(propValue == null) {
			return logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
		}
		
		if(RequestUtil.PASS_PARAMS.contains(propValue.toUpperCase()) || RequestUtil.DENIED_PARAM_LIST.contains(propValue)) {
			return logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
		}
		
		Object object = ((Map<?, ?>)param).get(propValue);
		return getLogSql(propValue, object, logSql);
	}
	
	private String getSqlByClass(String logSql, Object param, ParameterMapping mapping, String propValue) throws Throwable {		
		if(propValue == null) {
			return logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
		}
		
		if(RequestUtil.PASS_PARAMS.contains(propValue.toUpperCase()) || RequestUtil.DENIED_PARAM_LIST.contains(propValue)) {
			return logSql.replaceFirst("#\\{PARAMETER\\}", "\\?");
		}
		
		Class<? extends Object> paramClass = param.getClass();
		
		Field field = paramClass.getDeclaredField(propValue); // 관련 멤버변수 Field 객체 얻어옴
		field.setAccessible(true); // 멤버변수의 접근자가 private일 경우 reflection을 이용하여 값을 해당 멤버변수의 값을 가져오기 위해 별도로 셋팅
		if(field.get(param) != null) {
			Class<?> javaType = mapping.getJavaType(); // 해당 파라미터로 넘겨받은 사용자 정의 클래스 객체의 멤버변수의 타입
			if (String.class == javaType) {
				logSql = getLogSql(propValue, (String) field.get(param), logSql);
			} else {
				logSql = getLogSql(propValue, field.get(param), logSql);
			}
		} else {
			logSql = logSql.replaceFirst("#\\{PARAMETER\\}", "null");
		}
		return logSql;
	}
	
	private void insertDbLog(HttpServletRequest request, String sysCode, String sqlId, String sqlMethod, String logSql) {
		
		//LOG 기록인 경우 RETURN
		if(sqlId.contains("kr.co.nanwe.log.service.impl.SysLogMapper")) {
			return;
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
		
		String sql =  " 	INSERT INTO "+CodeConfig.LOG_TABLE+" (";
			   sql += " 		  SYS_CODE";
			   sql += " 		, SQL_ID";
			   sql += " 		, SQL_METHOD";
			   sql += " 		, LOG_SQL";
			   sql += " 		, LOG_ID";
			   sql += " 		, LOG_NAME";
			   sql += " 		, LOG_IP";
			   sql += " 		, LOG_DTTM";
			   sql += " 		, LOG_ORDER";
			   sql += " 	) VALUES (";
			   sql += " 		  ?";
			   sql += " 		, ?";
			   sql += " 		, ?";
			   sql += " 		, ?";
			   sql += " 		, ?";
			   sql += " 		, ?";
			   sql += " 		, ?";
			   sql += " 		, CURRENT_DATE";
			   sql += " 		, (SELECT COALESCE(MAX(A.LOG_ORDER)+1, 1) FROM "+CodeConfig.LOG_TABLE+" A WHERE A.SYS_CODE = ?)";
			   sql += " 	)";
		jdbcTemplate.update(sql, sysCode, sqlId, sqlMethod, logSql, logId, logName, logIp, sysCode);
	}
	
	private void printDbLog(String sqlId, String sqlMethod, String logSql) {
		
		//LOG 기록인 경우 RETURN
		if(sqlId.contains("kr.co.nanwe.log.service.impl.SysLogMapper")) {
			return;
		}
		
		String logMessage = "";
		logMessage += "\n--------------------------------------------------------------------------------------------------------------------\n";
		logMessage += "* sqlId 	: " + sqlId + "\n";
		logMessage += "* sqlMethod : " + sqlMethod + "\n";
		logMessage += "* logSql : \n" + logSql + "\n";
		logMessage += "--------------------------------------------------------------------------------------------------------------------\n";
		LOGGER.debug("{}", logMessage);
	}
}