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
        public static final String getSystemInfo            = "/system/getSystemInfo";       // 获取系统信息
    }

    /**
     * 配置
     */
    public class Config
    {
        public static final String getApolloConfig          = "/config/getApolloConfig";        // 获取Apollo配置信息
        public static final String getApolloValueByKey      = "/config/getApolloValueByKey";    // 获取Apollo配置信息，通过key获取value

        public static final String getNacosConfig           = "/config/getNacosConfig";         // 获取Nacos配置信息

        public static final String getMybatisConfig         = "/config/getMybatisConfig";       // 获取Mybatis配置信息

        public static final String getOpenFeignConfig       = "/config/getOpenFeignConfig";     // 获取OpenFeign配置信息

        public static final String getRedisConfig           = "/config/getRedisConfig";         // 获取Redis配置信息

        public static final String getSaTokenConfig         = "/config/getSaTokenConfig";       // 获取Sa-token配置信息

        public static final String getElasticSearchConfig   = "/config/getElasticSearchConfig"; // 获取es配置信息

        public static final String getXxlJobConfig          = "/config/getXxlJobConfig";        // 获取xxl-job配置信息

        public static final String getCamundaConfig         = "/config/getCamundaConfig";       // 获取camunda配置信息
    }

    /**
     * 用户
     */
    public class User
    {
        public static final String getUserInfo              = "/user/getUserInfo";           //
        public static final String getUserCacheInfo         = "/user/getUserCacheInfo";      //
        public static final String removeUserCacheInfo      = "/user/removeUserCacheInfo";   //
    }

    /**
     * 机构
     */
    public class Org
    {
        public static final String getOrgInfo               = "/org/getOrgInfo";             //
        public static final String getOrgInfoByOrgId        = "/org/getOrgInfoByOrgId";      //
        public static final String getSupOrgInfo            = "/org/getSupOrgInfo";          //
        public static final String getSubOrgInfo            = "/org/getSubOrgInfo";          //
    }

    /**
     * 角色
     */
    public class Role
    {
        public static final String getRoleInfo              = "role/getRoleInfo";           // 获取角色信息
    }


    /**
     * 权限
     */
    public class Auth
    {

        public static final String getAuthInfo              = "/auth/getAuthInfo";

        public static final String generateCaptcha          = "/auth/generateCaptcha";      // 获取验证吗
        public static final String validateCaptcha          = "/auth/validateCaptcha";      // 校验验证吗


        public static final String doLogin                  = "/auth/doLogin";              // 登录
        public static final String isLogin                  = "/auth/isLogin";              // 是否登录，true/false
        public static final String checkLogin               = "/auth/checkLogin";           // 检查登录，抛出异常
        public static final String tokenInfo                = "/auth/tokenInfo";            // token信息
        public static final String logOut                   = "/auth/logOut";               // 注销

        public static final String getPermissionList        = "/auth/getPermissionList";    // 获取当前账号拥有的权限集合
        public static final String hasPermissionList        = "/auth/hasPermissionList";    // 当前账号是否拥有指定权限，true/false
        public static final String checkPermissionList      = "/auth/checkPermissionList";  // 当前账号是否含有指定权限标识，抛出异常
        public static final String checkPermissionAnd       = "/auth/checkPermissionAnd";   // 当前账号是否含有指定权限（指定多个，必须全部通过）
        public static final String CheckPermissionOr        = "/auth/CheckPermissionOr";    // 当前账号是否含有指定权限（指定多个，通过一个即可）

        public static final String getRoleList              = "/auth/getRoleList";          // 获取当前账号拥有的角色集合
        public static final String hasRole                  = "/auth/hasRole";              // 当前账号是否拥有指定角色，true/false
        public static final String checkRole                = "/auth/checkRole";            // 当前账号是否含有指定角色标识，如果验证未通过，则抛出异常
        public static final String checkRoleAnd             = "/auth/checkRoleAnd";         // 当前账号是否含有指定角色（指定多个，必须全部通过）
        public static final String checkRoleOr              = "/auth/checkRoleOr";          // 当前账号是否含有指定角色（指定多个，通过一个即可）
    }


    /**
     * 日志
     */
    public class Log
    {
        public static final String getLogInfo               = "/log/getLogInfo";                    // 获取当前工程所有的日志简介

        public static final String insertBusinessLogCommon  = "/log/insertBusinessLogCommon";       // 更新通用业务日志表

        public static final String insertSystemLogCommon    = "/log/insertSystemLogCommon";         // 更新通用业务日志表
    }


    /**
     * 业务：kakarot
     */
    public class BusinessOfKakarot
    {

    }
    /**
     * 业务：xiaoxingbomei
     */
    public class BusinessOfXiaoxingbomei
    {

    }

    /**
     * 业务：
     */
    public class BusinessOfXxx3
    {

    }


    public class Tech
    {
        public static final String getTechLearningInfo  = "/tech/getTechLearningInfo";      // 获取技术学习信息

        public static final String mongodbOfInsert      = "/tech/mongodbOfInsert";          // mongodb-创建
        public static final String mongodbOfMultiInsert = "/tech/mongodbOfMultiInsert";     // mongodb-批量创建
        public static final String mongodbOfSave        = "/tech/mongodbOfSave";            // mongodb-创建

        public static final String mongodbOfSearch      = "/tech/mongodbOfSearch";          // mongodb-查询
        public static final String mongodbOfUpdate      = "/tech/mongodbOfUpdate";          // mongodb-修改
        public static final String mongodbOfDelete      = "/tech/mongodbOfDelete";          // mongodb-删除

        public static final String fastJsonOfXxx1       = "/tech/fastJsonTestOfXxx1";       // fastJson-
        public static final String fastJsonOfXxx2       = "/tech/fastJsonTestOfXxx2";       // fastJson-
        public static final String fastJsonOfXxx3       = "/tech/fastJsonTestOfXxx3";       // fastJson-

        public static final String redisOfStringGet     = "/tech/redisTestOfGet";           // redis-
        public static final String redisOfStringSet     = "/tech/redisTestOfGet";           // redis-

        public static final String cookieOfCreate       = "/tech/cookieOfCreate";           // cookie-
        public static final String cookieOfUpdate       = "/tech/cookieOfUpdate";           // cookie-
        public static final String cookieOfSearch       = "/tech/cookieOfSearch";           // cookie-
        public static final String cookieOfDelete       = "/tech/cookieOfDelete";           // cookie-

        public static final String sessionOfCreate      = "/tech/sessionOfCreate";          // session-
        public static final String sessionOfUpdate      = "/tech/sessionOfUpdate";          // session-
        public static final String sessionOfSearch      = "/tech/sessionOfSearch";          // session-
        public static final String sessionOfDelete      = "/tech/sessionOfDelete";          // session-

        public static final String elasticsearchOfXxx   = "/tech/elasticsearchOfXxx";       // elasticsearch-

        public static final String xxljobOfXxx          = "/tech/xxljobOfXxx";              // xxljob-

        public static final String camundaOfXxx         = "/tech/camundaOfXxx";             // camunda-

        public static final String mybatisOfXxx         = "/tech/mybatisOfXxx";             // mybatis-

        public static final String feignOfXxx           = "/tech/feignOfXxx";               // feign-

        public static final String streamOfXxx          = "/tech/streamOfXxx";              // stream-







    }


}
