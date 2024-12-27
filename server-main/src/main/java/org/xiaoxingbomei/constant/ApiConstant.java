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
    public class Kakarot
    {

    }
    /**
     * 业务：xiaoxingbomei
     */
    public class Xiaoxingbomei
    {

    }

    /**
     * 业务：
     */
    public class Xxx3
    {

    }


    /**
     * 持续不断的学习
     */
    public class Study
    {
        public static final String getTechLearningInfo          = "/study/getTechLearningInfo";          // 获取技术学习信息

        public static final String java_io_Xxx                  = "java_io_Xxx"; // java-io-

        public static final String java_stream_Xxx              = "java_stream_Xxx"; // java-stream-

        public static final String java_multi_Xxx0              = "java_multi_Xxx0"; // java-并发-创建线程（）
        public static final String java_multi_Xxx1              = "java_multi_Xxx1"; // java-并发-创建线程（）
        public static final String java_multi_Xxx2              = "java_multi_Xxx2"; // java-并发-创建线程（）
        public static final String java_multi_Xxx3              = "java_multi_Xxx3"; // java-并发-创建线程（）
        public static final String java_multi_Xxx4              = "java_multi_Xxx4"; // java-并发-创建线程（）
        public static final String java_multi_Xxx5              = "java_multi_Xxx5"; // java-并发-创建线程（）
        public static final String java_multi_Xxx6              = "java_multi_Xxx6"; // java-并发-创建线程（）

        public static final String java_Async_Xxx               = "java_Async_Xxx"; // java-异步-

        public static final String mongodb_Insert               = "/study/mongodb_Insert";               // mongodb-创建
        public static final String mongodb_MultiInsert          = "/study/mongodb_MultiInsert";          // mongodb-批量创建
        public static final String mongodb_Save                 = "/study/mongodb_Save";                 // mongodb-创
        public static final String mongodb_Search               = "/study/mongodb_Search";               // mongodb-查询
        public static final String mongodb_Update               = "/study/mongodb_Update";               // mongodb-修改
        public static final String mongodb_Delete               = "/study/mongodb_Delete";               // mongodb-删除

        public static final String fastJson_ObjectToJsonString  = "/study/fastJson_ObjectToJsonString";  // fastJson-对象转JSON字符串（序列化）
        public static final String fastJson_JsonStringToObject  = "/study/fastJson_JsonStringToObject";  // fastJson-JSON字符串转对象（反序列化）
        public static final String fastJson_PrettyPrint         = "/study/fastJson_PrettyPrint";         // fastJson-格式化JSON输出
        public static final String fastJson_JSONStringToMap     = "/study/fastJson_JSONStringToMap";     // fastJson-JSON转为Map
        public static final String fastJson_JSONStringToList    = "/study/fastJson_JSONStringToList";    // fastJson-JSON转为List

        public static final String redis_StringSet              = "/study/redis_StringSet";              // redis-存储
        public static final String redis_StringSetWithExpire    = "/study/redis_StringSetWithExpire";    // redis-存储携带过期时间
        public static final String redis_StringGet              = "/study/redis_StringGet";              // redis-获取
        public static final String redis_StringDelete           = "/study/redis_StringDelete";           // redis-删除
        public static final String redis_StringIncr             = "/study/redis_StringIncr";             // redis-自增
        public static final String redis_StringDecr             = "/study/redis_StringDecr";             // redis-自减

        public static final String cookie_create                = "/study/cookie_create";                // cookie-创建
        public static final String cookie_update                = "/study/cookie_update";                // cookie-修改
        public static final String cookie_search                = "/study/cookie_search";                // cookie-查询
        public static final String cookie_delete                = "/study/cookie_delete";                // cookie-删除

        public static final String session_create               = "/study/session_create";               // session-创建
        public static final String session_update               = "/study/session_update";               // session-修改
        public static final String session_search               = "/study/session_search";               // session-查询
        public static final String session_delete               = "/study/session_delete";               // session-删除

        public static final String elasticsearch_createIndex         = "/study/elasticsearch_createIndex";         // elasticsearch-创建索引
        public static final String elasticsearch_deleteIndex         = "/study/elasticsearch_deleteIndex";         // elasticsearch-删除索引
        public static final String elasticsearch_insertDocument      = "/study/elasticsearch_insertDocument";      // elasticsearch-插入单条数据
        public static final String elasticsearch_getDocument         = "/study/elasticsearch_getDocument";         // elasticsearch-查询单条数据
        public static final String elasticsearch_deleteDocument      = "/study/elasticsearch_deleteDocument";      // elasticsearch-删除单条数据
        public static final String elasticsearch_updateDocument      = "/study/elasticsearch_updateDocument";      // elasticsearch-更新单条文档
        public static final String elasticsearch_bulkInsertDocument  = "/study/elasticsearch_bulkInsertDocument";  // elasticsearch-更新文档
        public static final String elasticsearch_batchDeleteDocument = "/study/elasticsearch_batchDeleteDocument"; // elasticsearch-批量删除文档
        public static final String elasticsearch_match               = "/study/elasticsearch_match";               // elasticsearch-match查询
        public static final String elasticsearch_term                = "/study/elasticsearch_term";                // elasticsearch-term查询
        public static final String elasticsearch_bool                = "/study/elasticsearch_bool";                // elasticsearch-bool查询
        public static final String elasticsearch_highlight           = "/study/elasticsearch_highlight";           // elasticsearch-高亮显示
        public static final String elasticsearch_multimatch          = "/study/elasticsearch_multimatch";          // elasticsearch-multimatch

        public static final String xxljob_Xxx                   = "/study/xxljob_Xxx";         // xxljob-

        public static final String camunda_Xxx                  = "/study/camunda_Xxx";        // camunda-

        public static final String mybatis_Xxx                  = "/study/mybatis_Xxx";        // mybatis-

        public static final String mybatisplus_Xxx              = "/study/mybatisplus_Xxx";    // mybatisplus-

        public static final String feign_Xxx                    = "/study/feign_Xxx";          // feign-

        public static final String stream_Xxx                   = "/study/stream_Xxx";         // stream-

        public static final String mapstruct_Xxx                = "/study/mapstruct_xx";       // mapstruct-

        public static final String netty_Xxx                    = "/study/netty_Xxx";          // netty-

        public static final String kafka_Product                = "/study/kafka_Product";      // kafka-生产一条消息
        public static final String kafka_Consume                = "/study/kafka_Consume";      // kafka-消费一条消息

        public static final String rocketMq_Xxx                 = "/study/rocketMq_Xxx";       // rocketMq-

        public static final String minio_createBucket           = "/study/minio_createBucket"; // minio-创建bucket
        public static final String minio_searchBucket           = "/study/minio_searchBucket"; // minio-列出bucket和对象
        public static final String minio_deleteBucket           = "/study/minio_deleteBucket"; // minio-删除bucket
        public static final String minio_uploadFile             = "/study/minio_uploadFile";   // minio-上传文件
        public static final String minio_downloadFile           = "/study/minio_downloadFile"; // minio-下载文件
        public static final String minio_deleteFile             = "/study/minio_deleteFile";   // minio-删除文件

        public static final String fastexcel_downloadExcel      = "/study/fastexcel_downloadExcel"; // fastexcel-下载模板
        public static final String fastexcel_uploadExcel        = "/study/fastexcel_uploadExcel";   // fastexcel-上传文件
        public static final String fastexcel_ReadExcel          = "/study/fastexcel_ReadExcel";     // fastexcel-读取excel文件
        public static final String fastexcel_CreateExcel        = "/study/fastexcel_CreateExcel";   // fastexcel-创建excel文件
     }


    /**
     * 算法
     */
    public class LeetCode
    {
        public static final String easy_1       = "/leetCode/easy_1";   // 两数之和
        public static final String easy_136     = "/leetCode/easy_136"; // 只出现一次的数字
        public static final String easy_206     = "/leetCode/easy_206"; // 反转链表

        public static final String normal_1 = "/leetCode/normal_1"; //

        public static final String hard_1   = "/leetCode/hard_1"; //
    }

}
