package org.xiaoxingbomei.constant;

/**
 * 所有接口的路由定义
 */
public class ApiConstant
{

    public class Auth
    {
        // 认证相关接口
        public static final String login = "/auth/login";
        public static final String logout = "/auth/logout";
        public static final String isLogin = "/auth/isLogin";
        public static final String userInfo = "/auth/userInfo";
        
        // 服务间调用接口
        public static final String getUserById = "/auth/user/getUserById";
        public static final String getUserPermissions = "/auth/user/getUserPermissions";
        public static final String getUserRoles = "/auth/user/getUserRoles";
        public static final String verifyPassword = "/auth/verify";
        
        // 用户管理接口
        public static final String createUser = "/auth/users/create";
        public static final String listUsers = "/auth/users/list";
        public static final String getUserDetailById = "/auth/users/getDetailById";
        public static final String updateUser = "/auth/users/update";
        public static final String deleteUser = "/auth/users/delete";
        public static final String resetPassword = "/auth/users/resetPassword";
    }

    public class Token
    {
        // Token管理接口
        public static final String refreshToken = "/token/refresh";
        public static final String validateToken = "/token/validate";
        public static final String getTokenInfo = "/token/info";
        public static final String kickOut = "/token/kickout";
        public static final String batchKickOut = "/token/kickout/batch";
        public static final String getOnlineUsers = "/token/online";
        public static final String addToBlacklist = "/token/blacklist";
        
        // SSO接口
        public static final String ssoLogin = "/token/sso/login";
        public static final String validateTicket = "/token/sso/validate";
        public static final String ssoLogout = "/token/sso/logout";
        public static final String checkSsoLogin = "/token/sso/status";
        public static final String getSsoUserInfo = "/token/sso/userinfo";
        
        // 服务间Token接口
        public static final String generateServiceToken = "/token/service/generate";
        public static final String validateServiceToken = "/token/service/validate";
    }

}