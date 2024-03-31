package org.xiaoxingbomei.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.wrapper.InputStreamHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


/**
 * 请求流转换为多次读取的请求流 过滤器
 *
 * 在我们日常的Java开发中，免不了和其他系统的业务交互，或者微服务之间的接口调用
 *
 * 如果我们想保证数据传输的安全，对接口出参加密，入参解密。
 *
 * 但是不想写重复代码，我们可以提供一个通用starter，提供通用加密解密功能
 *
 * 在接口调用链中，request的请求流只能调用一次，处理之后，如果之后还需要用到请求流获取数据，就会发现数据为空。
 *
 * 比如使用了filter或者aop在接口处理之前，获取了request中的数据，对参数进行了校验，那么之后就不能在获取request请求流了
 *
 * 继承HttpServletRequestWrapper，将请求中的流copy一份，复写getInputStream和getReader方法供外部使用。每次调用后的getInputStream方法都是从复制出来的二进制数组中进行获取，这个二进制数组在对象存在期间一致存在。
 *
 * 使用Filter过滤器，在一开始，替换request为自己定义的可以多次读取流的request。
 *
 * 这样就实现了流的重复获取
 */
@Component
@Order(HIGHEST_PRECEDENCE + 1)  // 优先级最高
public class HttpServletRequestInputStreamFilter implements Filter
{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 转换为可以多次获取流的request
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        InputStreamHttpServletRequestWrapper inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);

        // 放行
        chain.doFilter(inputStreamHttpServletRequestWrapper, response);
    }

    @Override
    public void destroy() {}

}