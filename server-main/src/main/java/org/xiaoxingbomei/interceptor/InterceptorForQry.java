//package org.xiaoxingbomei.interceptor;
//
//import java.util.Properties;
//
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Plugin;
//import org.apache.ibatis.plugin.Signature;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//
//
//
///**
// * 打印结果拦截器 〈功能详细描述〉
// * @since
// */
//@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
//        Object.class, RowBounds.class, ResultHandler.class})})
//public class InterceptorForQry implements Interceptor
//{
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public Object intercept(Invocation invocation)
//            throws Throwable
//    {
//        Object result = invocation.proceed(); // 执行请求方法，并将所得结果保存到result中
//        String str = FastJsonUtils.toJSONString(result);
//        System.out.println(str);
//        return result;
//    }
//
//    public Object plugin(Object target)
//    {
//        return Plugin.wrap(target, this);
//    }
//
//    public void setProperties(Properties arg0)
//    {}
//}
//
//
