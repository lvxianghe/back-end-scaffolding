package org.xiaoxingbomei.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.xiaoxingbomei.aspect.ControllerLogAspectByPath;
import org.xiaoxingbomei.entity.GlobalRequestContext;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * mybatis log 拦截器
 */
@Slf4j
@Intercepts
        ({
        @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})
        })
public class MybatisLogInterceptor implements Interceptor
{

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 获取 StatementHandler 对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        // 获取 BoundSql 对象
        BoundSql boundSql = statementHandler.getBoundSql();

        // 获取 SQL 语句
        String sql = boundSql.getSql();

        // 获取参数对象
        Object parameterObject = boundSql.getParameterObject();

        // 获取Mapper对象


        // 获取请求上下文信息
        GlobalRequestContext requestContext = ControllerLogAspectByPath.getGlobalRequestContext();

        // 打印完整的 SQL 和参数
        String formattedSql = formatSqlWithParameters(sql, boundSql);

        // 继续执行并记录结束时间
        Object result = invocation.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // 美化打印样式
        log.info("\n----------------------------------------------------------\n\t{}{}{}{}{}{}{}",
                 " << mybatis complete sql log >>",
                "\n\t [request ip]   \t:    \t"  + requestContext.getIp(),
                "\n\t [request url]  \t:    \t"  + requestContext.getUrl(),
                "\n\t [request path] \t:    \t"  + requestContext.getClassName()+requestContext.getMethodName(),
                "\n\t [sql spendTime]\t:    \t"  + executionTime,
                "\n\t [complete sql] \t: \n \t"  + formattedSql,
                "\n----------------------------------------------------------\n");

//        log.info("\n完整的sql为:\n {}", formattedSql);

        return invocation.proceed();
    }

    private String formatSqlWithParameters(String sql, BoundSql boundSql)
    {
        Map<String, Object> parameters = new HashMap<>();
        if (boundSql.getParameterObject() instanceof Map)
        {
            parameters = (Map<String, Object>) boundSql.getParameterObject();
        } else
        {
            parameters.put("param", boundSql.getParameterObject());
        }

        // 替换 SQL 语句中的参数占位符
        String formattedSql = sql;
        for (Map.Entry<String, Object> entry : parameters.entrySet())
        {
            String placeholder = "?" + entry.getKey(); // 假设占位符是 "?paramName"
            formattedSql = formattedSql.replaceFirst("\\?", escapeSql(entry.getValue().toString()));
        }
        return formattedSql;
    }

    private String escapeSql(String value)
    {
        if (value == null) return "NULL";
        return value.replace("'", "''"); // 转义单引号
    }

    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}
