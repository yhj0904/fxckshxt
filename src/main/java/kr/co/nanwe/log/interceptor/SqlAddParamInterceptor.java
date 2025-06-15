package kr.co.nanwe.log.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: SqlAddParamInterceptor
 * @Description 	: SQL 세션 파라마티 추가
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) 
})
public class SqlAddParamInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			loginVO = new LoginVO();
		}
		boundSql.setAdditionalParameter("SESSION", SessionUtil.getLoginUser());
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}
}