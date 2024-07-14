package org.xiaoxingbomei.constant;

/**
 * 全部Api常量
 * 1、系统
 * 2、用户
 * 3、机构
 * 4、角色
 * 5、权限
 * 6、业务-xxx
 * 7、业务-xxx
 */
public class ApiConstant
{

    /**
     * 系统
     */
    public class System
    {
        public static final String getSystemInfo        = "system/getSystemInfo";       // 获取系统信息
    }

    /**
     * 配置
     */
    public class Config
    {
        public static final String getApolloConfig          = "/apollo/getApolloConfig";        // 获取Apollo配置信息

        public static final String getApolloValueByKey      = "/apollo/getApolloValueByKey";    // 获取Apollo配置信息，通过key获取value

        public static final String getNacosConfig           = "/apollo/getNacosConfig";         // 获取Nacos配置信息

        public static final String getMybatisConfig         = "/apollo/getMybatisConfig";       // 获取Mybatis配置信息

        public static final String getOpenFeignConfig       = "/apollo/getOpenFeignConfig";     // 获取OpenFeign配置信息

        public static final String getRedisConfig           = "/apollo/getRedisConfig";         // 获取Redis配置信息

        public static final String getSaTokenConfig         = "/apollo/getSaTokenConfig";       // 获取Sa-token配置信息

        public static final String getElasticSearchConfig   = "/apollo/getElasticSearchConfig"; // 获取es配置信息

        public static final String getXxlJobConfig          = "/apollo/getXxlJobConfig";        // 获取xxl-job配置信息

        public static final String getCamundaConfig         = "/apollo/getCamundaConfig";       // 获取camunda配置信息
    }

    /**
     * 用户
     */
    public class User
    {
        public static final String getUserInfo          = "user/getUserInfo";           //
        public static final String getUserCacheInfo     = "user/getUserCacheInfo";      //
        public static final String removeUserCacheInfo  = "user/removeUserCacheInfo";   //
    }

    /**
     * 机构
     */
    public class Org
    {
        public static final String getOrgInfo           = "/org/getOrgInfo";             //
        public static final String getOrgInfoByOrgId    = "/org/getOrgInfoByOrgId";      //
        public static final String getSupOrgInfo        = "/org/getSupOrgInfo";          //
        public static final String getSubOrgInfo        = "/org/getSubOrgInfo";          //
    }

    /**
     * 角色
     */
    public class Role
    {
        public static final String getRoleInfo          = "role/getRoleInfo";           //
    }


    /**
     * 权限
     */
    public class Auth
    {
        public static final String doLogin              = "/auth/doLogin";              // 登录
        public static final String isLogin              = "/auth/isLogin";              // 是否登录，true/false
        public static final String checkLogin           = "/auth/checkLogin";           // 检查登录，抛出异常
        public static final String tokenInfo            = "/auth/tokenInfo";            // token信息
        public static final String logOut               = "/auth/logOut";               // 注销

        public static final String getPermissionList    = "/auth/getPermissionList";    // 获取当前账号拥有的权限集合
        public static final String hasPermissionList    = "/auth/hasPermissionList";    // 当前账号是否拥有指定权限，true/false
        public static final String checkPermissionList  = "/auth/checkPermissionList";  // 当前账号是否含有指定权限标识，抛出异常
        public static final String checkPermissionAnd   = "/auth/checkPermissionAnd";   // 当前账号是否含有指定权限（指定多个，必须全部通过）
        public static final String CheckPermissionOr    = "/auth/CheckPermissionOr";    // 当前账号是否含有指定权限（指定多个，通过一个即可）

        public static final String getRoleList          = "/auth/getRoleList";          // 获取当前账号拥有的角色集合
        public static final String hasRole              = "/auth/hasRole";              // 当前账号是否拥有指定角色，true/false
        public static final String checkRole            = "/auth/checkRole";            // 当前账号是否含有指定角色标识，如果验证未通过，则抛出异常
        public static final String checkRoleAnd         = "/auth/checkRoleAnd";         // 当前账号是否含有指定角色（指定多个，必须全部通过）
        public static final String checkRoleOr          = "/auth/checkRoleOr";          // 当前账号是否含有指定角色（指定多个，通过一个即可）
    }

    /**
     * 业务：
     */
    public class BusinessOfXxx1
    {

    }
    /**
     * 业务：
     */
    public class BusinessOfXxx2
    {

    }

    /**
     * 业务：
     */
    public class BusinessOfXxx3
    {

    }



}
