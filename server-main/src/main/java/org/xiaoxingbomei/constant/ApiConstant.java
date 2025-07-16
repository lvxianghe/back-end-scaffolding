package org.xiaoxingbomei.constant;

/**
 * 所有接口的路由定义
 */
public class ApiConstant
{

    public class System
    {
        public static final String getSystemInfo = "/api/system/info";
        public static final String health = "/api/system/health";
        public static final String updateSystemInfo = "/api/system/update";
        public static final String getRedisInfo = "/api/system/redis-info";
        public static final String testRedis = "/api/system/redis-test";
    }

    public class Auth
    {
        public static final String login = "/login";
        public static final String getCurrentUserInfo = "/userInfo";
        
        // 服务间调用接口
        public static final String getUserById = "/auth/user/getUserById";
        public static final String getUserPermissions = "/auth/user/getUserPermissions";
        public static final String getUserRoles = "/auth/user/getUserRoles";
        public static final String verifyPassword = "/auth/verify";
    }

    public class Chat
    {
        // AI聊天相关接口（预留）
    }

}
