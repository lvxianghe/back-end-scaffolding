package org.xiaoxingbomei.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 *
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
public class SqlPrintInterceptor implements Interceptor
{

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        // 获取 StatementHandler 对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        // 获取 SQL 语句
        String sql = statementHandler.getBoundSql().getSql();

        // 获取 Mapper 方法的调用者类名
        String mapperClassName = statementHandler.getBoundSql().getParameterObject().getClass().getName();

        // 检查 Mapper 类是否在指定的包中
        if (mapperClassName.startsWith("com.example.dao"))
        {
            // 自定义日志输出
            log.debug("Custom SQL Log for DAO Package: " + sql.trim());
        }

        // 执行原方法
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target)
    {
        // 包装目标对象，生成代理对象
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties)
    {
        // 设置属性
    }
}