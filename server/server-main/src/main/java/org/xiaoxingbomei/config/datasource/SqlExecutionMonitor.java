package org.xiaoxingbomei.config.datasource;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * SQL执行监控器
 * 统一监控数据源切换和SQL执行情况
 */
@Slf4j
public class SqlExecutionMonitor
{

    /**
     * 线程本地变量存储方法调用信息
     */
    private static final ThreadLocal<MethodContext> METHOD_CONTEXT = new ThreadLocal<>();

    /**
     * 方法上下文信息
     */
    public static class MethodContext
    {
        private String className;
        private String methodName;
        private long startTime;
        private String dataSource;

        public MethodContext(String className, String methodName, String dataSource) {
            this.className = className;
            this.methodName = methodName;
            this.dataSource = dataSource;
            this.startTime = System.currentTimeMillis();
        }

        // getters
        public String getClassName() { return className; }
        public String getMethodName() { return methodName; }
        public long getStartTime() { return startTime; }
        public String getDataSource() { return dataSource; }
    }

    /**
     * 数据源切换监控切面
     */
    @Aspect
    @Component
    @Slf4j
    public static class DataSourceAspect
    {

        @Around("@annotation(com.baomidou.dynamic.datasource.annotation.DS) || " +
                "@within(com.baomidou.dynamic.datasource.annotation.DS)")
        public Object around(ProceedingJoinPoint point) throws Throwable {
            String className = point.getTarget().getClass().getSimpleName();
            String methodName = point.getSignature().getName();
            String currentDataSource = DynamicDataSourceContextHolder.peek();
            if (currentDataSource == null) {
                currentDataSource = "master"; // 默认数据源
            }

            // 设置方法上下文
            METHOD_CONTEXT.set(new MethodContext(className, methodName, currentDataSource));

            try {
                log.info("🚀 [方法开始] {}#{} -> 数据源: {}", className, methodName, currentDataSource);
                return point.proceed();
            } finally {
                MethodContext context = METHOD_CONTEXT.get();
                if (context != null) {
                    long duration = System.currentTimeMillis() - context.getStartTime();
                    log.info("✅ [方法结束] {}#{} -> 耗时: {}ms", className, methodName, duration);
                }
                METHOD_CONTEXT.remove();
            }
        }
    }

    /**
     * MyBatis SQL拦截器
     */
    @Intercepts({
            @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
            @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
    })
    @Component
    @Slf4j
    public static class SqlInterceptor implements Interceptor
    {

        @Override
        public Object intercept(Invocation invocation) throws Throwable
        {
            long start = System.currentTimeMillis();

            try {
                // 获取SQL相关信息
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                Object parameter = invocation.getArgs()[1];
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);

                // 获取完整SQL
                String completeSql = getCompleteSql(mappedStatement.getConfiguration(), boundSql);

                // 获取方法上下文
                MethodContext context = METHOD_CONTEXT.get();
                String dataSource = DynamicDataSourceContextHolder.peek();
                if (dataSource == null) {
                    dataSource = "master";
                }

                // 统一格式打印SQL执行信息
                printSqlExecutionInfo(mappedStatement, completeSql, dataSource, context);

                // 执行SQL
                Object result = invocation.proceed();

                long end = System.currentTimeMillis();
                log.info("🔥 [SQL执行完成] 耗时: {}ms", (end - start));

                return result;

            } catch (Exception e) {
                long end = System.currentTimeMillis();
                log.error("❌ [SQL执行失败] 耗时: {}ms, 错误: {}", (end - start), e.getMessage());
                throw e;
            }
        }

        /**
         * 统一打印SQL执行信息
         */
        private void printSqlExecutionInfo(MappedStatement mappedStatement, String completeSql,
                                           String dataSource, MethodContext context)
        {

            String sqlId = mappedStatement.getId();
            String sqlType = mappedStatement.getSqlCommandType().toString();

            // 构建统一的日志格式
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append("\n").append("=" .repeat(100));
            logBuilder.append("\n🔍 [SQL执行监控]");

            if (context != null) {
                logBuilder.append("\n📍 调用方法: ").append(context.getClassName()).append("#").append(context.getMethodName());
            }

            logBuilder.append("\n💾 数据源: ").append(dataSource);
            logBuilder.append("\n📋 SQL类型: ").append(sqlType);
            logBuilder.append("\n🆔 Mapper方法: ").append(sqlId);
            logBuilder.append("\n📝 完整SQL: ").append(formatSql(completeSql));
            logBuilder.append("\n").append("=" .repeat(100));

            log.info(logBuilder.toString());
        }

        /**
         * 获取完整的SQL语句（包含参数值）
         */
        private String getCompleteSql(Configuration configuration, BoundSql boundSql)
        {
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

            if (parameterMappings.size() > 0 && parameterObject != null) {
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
                } else {
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    for (ParameterMapping parameterMapping : parameterMappings) {
                        String propertyName = parameterMapping.getProperty();
                        if (metaObject.hasGetter(propertyName)) {
                            Object obj = metaObject.getValue(propertyName);
                            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                        } else if (boundSql.hasAdditionalParameter(propertyName)) {
                            Object obj = boundSql.getAdditionalParameter(propertyName);
                            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                        } else {
                            sql = sql.replaceFirst("\\?", "缺失参数");
                        }
                    }
                }
            }
            return sql;
        }

        /**
         * 获取参数值的字符串表示
         */
        private String getParameterValue(Object obj)
        {
            String value;
            if (obj instanceof String) {
                value = "'" + obj.toString() + "'";
            } else if (obj instanceof Date) {
                DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
                value = "'" + formatter.format((Date) obj) + "'";
            } else {
                if (obj != null) {
                    value = obj.toString();
                } else {
                    value = "null";
                }
            }
            return value;
        }

        /**
         * 格式化SQL语句
         */
        private String formatSql(String sql) {
            return sql.replaceAll("(?i)\\b(SELECT|FROM|WHERE|AND|OR|ORDER BY|GROUP BY|HAVING|INSERT|UPDATE|DELETE|SET|VALUES)\\b", "\n    $1")
                    .replaceAll("\n\\s*\n", "\n")
                    .trim();
        }

        @Override
        public Object plugin(Object target) {
            return Plugin.wrap(target, this);
        }
    }

}