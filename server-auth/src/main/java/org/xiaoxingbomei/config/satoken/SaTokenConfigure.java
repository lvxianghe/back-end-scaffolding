package org.xiaoxingbomei.config.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiaoxingbomei.common.entity.LoginInfo;
import org.xiaoxingbomei.service.AuthService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer, StpInterface
{

    @Autowired
    private AuthService authService;
    // ======================================================


    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * sa-token内置方法
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object userId, String loginType)
    {
        ArrayList<String> permissionList = new ArrayList<String>();
        permissionList.add("main");
        permissionList.add("other");
        return permissionList;
    }

    /**
     * sa-token内置方法
     * 返回一个账号所拥有的角色集合
     */
    @Override
    public List<String> getRoleList(Object userId, String loginType)
    {
        LoginInfo loginInfo = (LoginInfo) StpUtil.getSessionByLoginId(userId).get((String) userId);
        List<String> roleList = loginInfo.getRoleList();
        return roleList;
    }
}