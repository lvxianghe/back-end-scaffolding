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
        public static final String getRoleInfo              = "/role/getRoleInfo";           // 获取角色信息
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
     * 业务：大红书
     */
    public class BigRedBook
    {
        public static final String Xxx1         = "/redbook/Xxx1";
        public static final String Xxx2         = "/redbook/Xxx2";
        public static final String Xxx3         = "/redbook/Xxx3";
    }

    /**
     * 业务：知识库
     */
    public class Wiki
    {
        public static final String mine_knowledgeBases_search       = "/wiki/mine/knowledgeBases/search";     // 个人知识库列表查询
        public static final String mine_knowledgeBase_search        = "/wiki/mine/knowledgeBase/search";      // 个人知识库列表查询
        public static final String mine_knowledgeBase_create        = "/wiki/mine/knowledgeBase/create";      // 个人知识库创建
        public static final String mine_knowledgeBase_multiCreate   = "/wiki/mine/knowledgeBase/multiCreate"; // 个人知识库批量创建
    }
    /**
     * 业务：IM
     */
    public class Chat
    {
        public static final String search = "/chat/search";
        public static final String create = "/chat/create";
    }

    /**
     * 业务：Gpt
     */
    public class Gpt
    {
        public static final String Xxx1 = "/gpt/Xxx1"; //
        public static final String Xxx2 = "/gpt/Xxx2"; //
        public static final String Xxx3 = "/gpt/Xxx3"; //
    }

    /**
     * 业务：广告
     */
    public class Advertising
    {
        public static final String Xxx1 = "/ad/Xxx1"; //
        public static final String Xxx2 = "/ad/Xxx2"; //
        public static final String Xxx3 = "/ad/Xxx3"; //
    }


    /**
     * 业务：商城
     */
    public class Mall
    {
        public static final String Xxx1 = "/mall/Xxx1"; //
        public static final String Xxx2 = "/mall/Xxx2"; //
        public static final String Xxx3 = "/mall/Xxx3"; //
    }


    /**
     * 业务：数据资产-数据标准
     */
    public class DataAssetStd
    {
        public static final String Xxx1 = "/std/Xxx1"; //
        public static final String Xxx2 = "/std/Xxx2"; //
        public static final String Xxx3 = "/std/Xxx3"; //
    }

    /**
     * 业务：数据资产-数据质量
     */
    public class DataAssetQlty
    {
        public static final String Xxx1 = "/qlty/Xxx1"; //
        public static final String Xxx2 = "/qlty/Xxx2"; //
        public static final String Xxx3 = "/qlty/Xxx3"; //
    }



    /**
     * 持续不断的学习
     */
    public class Study
    {
        public static final String getTechLearningInfo                  = "/study/getTechLearningInfo";          // 获取技术学习信息

        public static final String java_io_Xxx                          = "/study/java_io_Xxx";     // java-io-

        public static final String java_stream_Xxx1                     = "/study/java_stream_Xxx"; // java-stream-
        public static final String java_stream_Xxx2                     = "/study/java_stream_Xxx"; // java-stream-

        public static final String java_multi_createThreadByThread      = "/study/java_multi_createThreadByThread";     // java-并发-创建线程（thread）
        public static final String java_multi_createThreadByRunnable    = "/study/java_multi_createThreadByRunnable";   // java-并发-创建线程（runnable）
        public static final String java_multi_createThreadByCallable    = "/study/java_multi_createThreadByCallable";   // java-并发-创建线程（callable）
        public static final String java_multi_createThreadByThreadPool  = "/study/java_multi_createThreadByThreadPool"; // java-并发-创建线程（线程池）
        public static final String java_multi_threadStateNew            = "/study/java_multi_threadStateNew";           // java-并发-线程状态-new
        public static final String java_multi_threadStateRunnable       = "/study/java_multi_threadStateRunnable";      // java-并发-线程状态-runnable
        public static final String java_multi_threadStateBlocked        = "/study/java_multi_threadStateBlocked";       // java-并发-线程状态-blocked
        public static final String java_multi_threadStateWaiting        = "/study/java_multi_threadStateWaiting";       // java-并发-线程状态-waiting
        public static final String java_multi_threadStateTimedWaiting   = "/study/java_multi_threadStateTimedWaiting";  // java-并发-线程状态-timedwaiting
        public static final String java_multi_threadStateTerminated     = "/study/java_multi_threadStateTerminated";    // java-并发-线程状态-terminated

        public static final String dynamicThreadPool_create             = "/study/dynamicThreadPool_create";     // 动态线程池-创建
        public static final String dynamicThreadPool_update             = "/study/dynamicThreadPool_update";     // 动态线程池-更新参数
        public static final String dynamicThreadPool_get                = "/study/dynamicThreadPool_get";        // 动态线程池-查询状态
        public static final String dynamicThreadPool_delete             = "/study/dynamicThreadPool_delete";     // 动态线程池-销毁
        public static final String dynamicThreadPool_createTask         = "/study/dynamicThreadPool_createTask"; // 动态线程池-创建任务

        public static final String java_Async_Xxx                       = "/study/java_Async_Xxx";               // java-异步-

        public static final String mongodb_Insert                       = "/study/mongodb_Insert";               // mongodb-创建
        public static final String mongodb_MultiInsert                  = "/study/mongodb_MultiInsert";          // mongodb-批量创建
        public static final String mongodb_Save                         = "/study/mongodb_Save";                 // mongodb-保存
        public static final String mongodb_Search                       = "/study/mongodb_Search";               // mongodb-查询
        public static final String mongodb_Update                       = "/study/mongodb_Update";               // mongodb-修改
        public static final String mongodb_Delete                       = "/study/mongodb_Delete";               // mongodb-删除

        public static final String fastJson_ObjectToJsonString          = "/study/fastJson_ObjectToJsonString";  // fastJson-对象转JSON字符串（序列化）
        public static final String fastJson_JsonStringToObject          = "/study/fastJson_JsonStringToObject";  // fastJson-JSON字符串转对象（反序列化）
        public static final String fastJson_PrettyPrint                 = "/study/fastJson_PrettyPrint";         // fastJson-格式化JSON输出
        public static final String fastJson_JSONStringToMap             = "/study/fastJson_JSONStringToMap";     // fastJson-JSON转为Map
        public static final String fastJson_JSONStringToList            = "/study/fastJson_JSONStringToList";    // fastJson-JSON转为List

        public static final String redis_StringSet                      = "/study/redis_StringSet";              // redis-String-存储
        public static final String redis_StringSetWithExpire            = "/study/redis_StringSetWithExpire";    // redis-String-存储携带过期时间
        public static final String redis_StringGet                      = "/study/redis_StringGet";              // redis-String-获取
        public static final String redis_StringDelete                   = "/study/redis_StringDelete";           // redis-String-删除
        public static final String redis_StringIncr                     = "/study/redis_StringIncr";             // redis-String-自增
        public static final String redis_StringDecr                     = "/study/redis_StringDecr";             // redis-String-自减

        public static final String redis_hashSave                       = "/study/redis_hashSave";               // redis-hash-指定hash中添加或者更新
        public static final String redis_hashFind                       = "/study/redis_hashFind";               // redis-hash-查询hash中指定字段的值
        public static final String redis_hashFindAll                    = "/study/redis_hashFindAll";            // redis-hash-查询整个hash中所有字段和值
        public static final String redis_hashDelete                     = "/study/redis_hashDelete";             // redis-hash-删除hash中指定字段
        public static final String redis_hashDeleteAll                  = "/study/redis_hashDeleteAll";          // redis-hash-删除整个hash

        public static final String redis_listLeftPush                   = "/study/redis_listLeftPush";           // redis-list-向列表左侧推入元素（左压栈）
        public static final String redis_listRightPush                  = "/study/redis_listRightPush";          // redis-list-向列表右侧推入元素（右压栈）
        public static final String redis_listLeftPop                    = "/study/redis_listLeftPop";            // redis-list-从列表左侧弹出元素（左出栈）
        public static final String redis_listRightPop                   = "/study/redis_listRightPop";           // redis-list-从列表右侧弹出元素（右出栈）
        public static final String redis_listRange                      = "/study/redis_listRange";              // redis-list-获取列表中的指定范围的元素
        public static final String redis_listRemove                     = "/study/redis_listRemove";             // redis-list-删除列表中指定数量的某个值
        public static final String redis_listSize                       = "/study/redis_listSize";               // redis-list-获取列表的长度
        public static final String redis_listSet                        = "/study/redis_listSet";                // redis-list-设置列表中指定索引的值
        public static final String redis_expire                         = "/study/redis_expire";                 // redis-list-设置键的过期时间

        public static final String cookie_create                        = "/study/cookie_create";                // cookie-创建
        public static final String cookie_update                        = "/study/cookie_update";                // cookie-修改
        public static final String cookie_search                        = "/study/cookie_search";                // cookie-查询
        public static final String cookie_delete                        = "/study/cookie_delete";                // cookie-删除

        public static final String session_create                       = "/study/session_create";               // session-创建
        public static final String session_update                       = "/study/session_update";               // session-修改
        public static final String session_search                       = "/study/session_search";               // session-查询
        public static final String session_delete                       = "/study/session_delete";               // session-删除

        public static final String elasticsearch_createIndex            = "/study/elasticsearch_createIndex";         // elasticsearch-创建索引
        public static final String elasticsearch_deleteIndex            = "/study/elasticsearch_deleteIndex";         // elasticsearch-删除索引
        public static final String elasticsearch_insertDocument         = "/study/elasticsearch_insertDocument";      // elasticsearch-插入单条数据
        public static final String elasticsearch_getDocument            = "/study/elasticsearch_getDocument";         // elasticsearch-查询单条数据
        public static final String elasticsearch_deleteDocument         = "/study/elasticsearch_deleteDocument";      // elasticsearch-删除单条数据
        public static final String elasticsearch_updateDocument         = "/study/elasticsearch_updateDocument";      // elasticsearch-更新单条文档
        public static final String elasticsearch_bulkInsertDocument     = "/study/elasticsearch_bulkInsertDocument";  // elasticsearch-更新文档
        public static final String elasticsearch_batchDeleteDocument    = "/study/elasticsearch_batchDeleteDocument"; // elasticsearch-批量删除文档
        public static final String elasticsearch_match                  = "/study/elasticsearch_match";               // elasticsearch-match查询
        public static final String elasticsearch_term                   = "/study/elasticsearch_term";                // elasticsearch-term查询
        public static final String elasticsearch_bool                   = "/study/elasticsearch_bool";                // elasticsearch-bool查询
        public static final String elasticsearch_highlight              = "/study/elasticsearch_highlight";           // elasticsearch-高亮显示
        public static final String elasticsearch_multimatch             = "/study/elasticsearch_multimatch";          // elasticsearch-multimatch

        public static final String xxljob_Xxx                           = "/study/xxljob_Xxx";         // xxljob-

        public static final String camunda_Xxx                          = "/study/camunda_Xxx";        // camunda-

        public static final String mybatis_Xxx                          = "/study/mybatis_Xxx";        // mybatis-

        public static final String mybatisplus_Xxx                      = "/study/mybatisplus_Xxx";    // mybatisplus-

        public static final String feign_Xxx                            = "/study/feign_Xxx";          // feign-

        public static final String mapstruct_Xxx                        = "/study/mapstruct_xx";       // mapstruct-

        public static final String netty_Xxx                            = "/study/netty_Xxx";          // netty-

        public static final String kafka_Product                        = "/study/kafka_Product";      // kafka-生产一条消息
        public static final String kafka_Consume                        = "/study/kafka_Consume";      // kafka-消费一条消息

        public static final String rocketMq_product                     = "/study/rocketMq_product";   // rocketMq-生产一条消息
        public static final String rocketMq_consume                     = "/study/rocketMq_consume";   // rocketMq-生产一条消息

        public static final String minio_createBucket                   = "/study/minio_createBucket"; // minio-创建bucket
        public static final String minio_searchBucket                   = "/study/minio_searchBucket"; // minio-列出bucket和对象
        public static final String minio_deleteBucket                   = "/study/minio_deleteBucket"; // minio-删除bucket
        public static final String minio_uploadFile                     = "/study/minio_uploadFile";   // minio-上传文件
        public static final String minio_downloadFile                   = "/study/minio_downloadFile"; // minio-下载文件
        public static final String minio_deleteFile                     = "/study/minio_deleteFile";   // minio-删除文件

        public static final String fastexcel_downloadExcel              = "/study/fastexcel_downloadExcel"; // fastexcel-下载模板
        public static final String fastexcel_uploadExcel                = "/study/fastexcel_uploadExcel";   // fastexcel-上传文件 读取excel中日志并落库
        public static final String fastexcel_ReadExcel                  = "/study/fastexcel_ReadExcel";     // fastexcel-读取excel文件
        public static final String fastexcel_CreateExcel                = "/study/fastexcel_CreateExcel";   // fastexcel-创建excel文件
     }


    /**
     * 算法
     */
    public class LeetCode
    {
        public static final String easy_1       = "/leetCode/easy_1";   // 两数之和
        public static final String easy_136     = "/leetCode/easy_136"; // 只出现一次的数字
        public static final String easy_206     = "/leetCode/easy_206"; // 反转链表

        public static final String normal_1     = "/leetCode/normal_1"; //

        public static final String hard_1       = "/leetCode/hard_1"; //
    }

}
