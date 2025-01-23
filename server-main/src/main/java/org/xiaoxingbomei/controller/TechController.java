package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.service.SystemService;
import org.xiaoxingbomei.service.TechService;

import org.xiaoxingbomei.common.entity.GlobalEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "技术controller",description = "用于学习测试技术的controller")
@RestController
public class TechController
{

    @Autowired
    private TechService techService;

    @Autowired
    private SystemService systemService;

    // =====================================================================================================

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-thread")
    @RequestMapping(value = ApiConstant.Study.java_multi_createThreadByThread,method = RequestMethod.POST)
    public GlobalEntity java_multi_createThreadByThread(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_createThreadByThread(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-Runnable")
    @RequestMapping(value = ApiConstant.Study.java_multi_createThreadByRunnable,method = RequestMethod.POST)
    public GlobalEntity java_multi_createThreadByRunnable(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_createThreadByRunnable(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-Callable")
    @RequestMapping(value = ApiConstant.Study.java_multi_createThreadByCallable,method = RequestMethod.POST)
    public GlobalEntity java_multi_createThreadByCallable(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_createThreadByCallable(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_createThreadByThreadPool,method = RequestMethod.POST)
    public GlobalEntity java_multi_createThreadByThreadPool(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_createThreadByThreadPool(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_threadStateNew,method = RequestMethod.POST)
    public GlobalEntity java_multi_threadStateNew(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_threadStateNew(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_threadStateRunnable,method = RequestMethod.POST)
    public GlobalEntity java_multi_threadStateRunnable(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_threadStateRunnable(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_threadStateBlocked,method = RequestMethod.POST)
    public GlobalEntity java_multi_threadStateBlocked(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_threadStateBlocked(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_threadStateWaiting,method = RequestMethod.POST)
    public GlobalEntity java_multi_threadStateWaiting(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_threadStateWaiting(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_threadStateTimedWaiting,method = RequestMethod.POST)
    public GlobalEntity java_multi_threadStateTimedWaiting(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_threadStateTimedWaiting(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "java-并发编程-创建线程-threadpool")
    @RequestMapping(value = ApiConstant.Study.java_multi_threadStateTerminated,method = RequestMethod.POST)
    public GlobalEntity java_multi_threadStateTerminated(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.java_multi_threadStateTerminated(paramString);
        return ret;
    }

    @Operation(summary = "java学习接口",description = "动态线程池-创建")
    @RequestMapping(value = ApiConstant.Study.dynamicThreadPool_create,method = RequestMethod.POST)
    public GlobalEntity dynamicThreadPool_create(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.dynamicThreadPool_create(paramString);
        return ret;
    }
    @Operation(summary = "java学习接口",description = "动态线程池-更新参数")
    @RequestMapping(value = ApiConstant.Study.dynamicThreadPool_update,method = RequestMethod.POST)
    public GlobalEntity dynamicThreadPool_update(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.dynamicThreadPool_update(paramString);
        return ret;
    }
    @Operation(summary = "java学习接口",description = "动态线程池-获取状态")
    @RequestMapping(value = ApiConstant.Study.dynamicThreadPool_get,method = RequestMethod.POST)
    public GlobalEntity dynamicThreadPool_get(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.dynamicThreadPool_get(paramString);
        return ret;
    }
    @Operation(summary = "java学习接口",description = "动态线程池-销毁")
    @RequestMapping(value = ApiConstant.Study.dynamicThreadPool_delete,method = RequestMethod.POST)
    public GlobalEntity dynamicThreadPool_delete(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.dynamicThreadPool_delete(paramString);
        return ret;
    }



    @Operation(summary = "mongodb学习接口",description = "mongodb-单条insert")
    @RequestMapping(value = ApiConstant.Study.mongodb_Insert,method = RequestMethod.POST)
    public GlobalEntity mongodb_Insert(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.mongodb_Insert(paramString);
        return ret;
    }
    @Operation(summary = "mongodb学习接口",description = "mongodb-批量insert")
    @RequestMapping(value = ApiConstant.Study.mongodb_MultiInsert,method = RequestMethod.POST)
    public GlobalEntity mongodb_MultiInsert(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.mongodb_MultiInsert(paramString);
        return ret;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-save")
    @RequestMapping(value = ApiConstant.Study.mongodb_Save,method = RequestMethod.POST)
    public GlobalEntity mongodb_Save(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.mongodb_Save(paramString);
        return ret;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-查询")
    @RequestMapping(value = ApiConstant.Study.mongodb_Search,method = RequestMethod.POST)
    public GlobalEntity getTechLearningInfo(@RequestBody String paramString)
    {
        return null;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-修改")
    @RequestMapping(value = ApiConstant.Study.mongodb_Update,method = RequestMethod.POST)
    public GlobalEntity mongodb_Update(@RequestBody String paramString)
    {
        return null;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-删除")
    @RequestMapping(value = ApiConstant.Study.mongodb_Delete,method = RequestMethod.POST)
    public GlobalEntity mongodb_Delete(@RequestBody String paramString)
    {
        return null;
    }

    @Operation(summary = "fastjson学习接口", description = "fastjson-对象转jsonstring（序列化）")
    @RequestMapping(value = ApiConstant.Study.fastJson_ObjectToJsonString,method = RequestMethod.POST)
    public GlobalEntity fastJson_ObjectToJsonString(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.fastJson_ObjectToJsonString(paramString);
        return ret;
    }

    @Operation(summary = "fastjson学习接口", description = "fastjson-JSON字符串转对象（反序列化）")
    @RequestMapping(value = ApiConstant.Study.fastJson_JsonStringToObject,method = RequestMethod.POST)
    public GlobalEntity fastJson_JsonStringToObject(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.fastJson_JsonStringToObject(paramString);
        return ret;
    }

    @Operation(summary = "fastjson学习接口", description = "fastjson-格式化JSON输出")
    @RequestMapping(value = ApiConstant.Study.fastJson_PrettyPrint,method = RequestMethod.POST)
    public GlobalEntity fastJson_PrettyPrint(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.fastJson_PrettyPrint(paramString);
        return ret;
    }

    @Operation(summary = "fastjson学习接口", description = "fastjson-JSON转为Map")
    @RequestMapping(value = ApiConstant.Study.fastJson_JSONStringToMap,method = RequestMethod.POST)
    public GlobalEntity fastJson_JSONStringToMap(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.fastJson_JSONStringToMap(paramString);
        return ret;
    }

    @Operation(summary = "fastjson学习接口", description = "fastjson-JSON转为List")
    @RequestMapping(value = ApiConstant.Study.fastJson_JSONStringToList,method = RequestMethod.POST)
    public GlobalEntity fastJson_JSONStringToList(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.fastJson_JSONStringToList(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "设置键的过期时间")
    @RequestMapping(value = ApiConstant.Study.redis_expire,method = RequestMethod.POST)
    public GlobalEntity redis_expire(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_expire(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "redis-String存储")
    @RequestMapping(value = ApiConstant.Study.redis_StringSet,method = RequestMethod.POST)
    public GlobalEntity redis_StringSet(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_StringSet(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "redis-String存储携带过期时间")
    @RequestMapping(value = ApiConstant.Study.redis_StringSetWithExpire,method = RequestMethod.POST)
    public GlobalEntity redis_StringSetWithExpire(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_StringSetWithExpire(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "redis-String获取")
    @RequestMapping(value = ApiConstant.Study.redis_StringGet,method = RequestMethod.POST)
    public GlobalEntity redis_StringGet(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_StringGet(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "redis-String刪除")
    @RequestMapping(value = ApiConstant.Study.redis_StringDelete,method = RequestMethod.POST)
    public GlobalEntity redis_StringDelete(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_StringDelete(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "redis-String自增")
    @RequestMapping(value = ApiConstant.Study.redis_StringIncr,method = RequestMethod.POST)
    public GlobalEntity redis_StringIncr(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_StringIncr(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "redis-String自减")
    @RequestMapping(value = ApiConstant.Study.redis_StringDecr,method = RequestMethod.POST)
    public GlobalEntity redis_StringDecr(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_StringDecr(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "hash-指定hash中添加或者更新")
    @RequestMapping(value = ApiConstant.Study.redis_hashSave,method = RequestMethod.POST)
    public GlobalEntity redis_hashSave(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_hashSave(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "查询hash中指定字段的值")
    @RequestMapping(value = ApiConstant.Study.redis_hashFind,method = RequestMethod.POST)
    public GlobalEntity redis_hashFind(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_hashFind(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "hash-查询整个hash中所有字段和值")
    @RequestMapping(value = ApiConstant.Study.redis_hashFindAll,method = RequestMethod.POST)
    public GlobalEntity redis_hashFindAll(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_hashFindAll(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "hash-删除hash中指定字段")
    @RequestMapping(value = ApiConstant.Study.redis_hashDelete,method = RequestMethod.POST)
    public GlobalEntity redis_hashDelete(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_hashDelete(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "hash-删除整个hash")
    @RequestMapping(value = ApiConstant.Study.redis_hashDeleteAll,method = RequestMethod.POST)
    public GlobalEntity redis_hashDeleteAll(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_hashDeleteAll(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "list-向列表左侧推入元素（左压栈）")
    @RequestMapping(value = ApiConstant.Study.redis_listLeftPush,method = RequestMethod.POST)
    public GlobalEntity redis_listLeftPush(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listLeftPush(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-向列表右侧推入元素（右压栈）")
    @RequestMapping(value = ApiConstant.Study.redis_listRightPush,method = RequestMethod.POST)
    public GlobalEntity redis_listRightPush(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listRightPush(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-从列表左侧弹出元素（左出栈）")
    @RequestMapping(value = ApiConstant.Study.redis_listLeftPop,method = RequestMethod.POST)
    public GlobalEntity redis_listLeftPop(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listLeftPop(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-从列表右侧弹出元素（右出栈）")
    @RequestMapping(value = ApiConstant.Study.redis_listRightPop,method = RequestMethod.POST)
    public GlobalEntity redis_listRightPop(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listRightPop(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-获取列表中的指定范围的元素")
    @RequestMapping(value = ApiConstant.Study.redis_listRange,method = RequestMethod.POST)
    public GlobalEntity redis_listRange(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listRange(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-删除列表中指定数量的某个值")
    @RequestMapping(value = ApiConstant.Study.redis_listRemove,method = RequestMethod.POST)
    public GlobalEntity redis_listRemove(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listRemove(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-通过索引删除范围内元素")
    @RequestMapping(value = ApiConstant.Study.redis_listRemoveByIndex,method = RequestMethod.POST)
    public GlobalEntity redis_listRemoveByIndex(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listRemoveByIndex(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-XXX")
    @RequestMapping(value = ApiConstant.Study.redis_listRemoveByRange,method = RequestMethod.POST)
    public GlobalEntity redis_listRemoveByRange(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listRemoveByRange(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-XXX")
    @RequestMapping(value = ApiConstant.Study.redis_listSize,method = RequestMethod.POST)
    public GlobalEntity redis_listSize(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listSize(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-XXX")
    @RequestMapping(value = ApiConstant.Study.redis_listSet,method = RequestMethod.POST)
    public GlobalEntity redis_listSet(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_listSet(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setAdd,method = RequestMethod.POST)
    public GlobalEntity redis_setAdd(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setAdd(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setAddAll,method = RequestMethod.POST)
    public GlobalEntity redis_setAddAll(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setAddAll(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setGetAll,method = RequestMethod.POST)
    public GlobalEntity redis_setGetAll(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setGetAll(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setGetRandom,method = RequestMethod.POST)
    public GlobalEntity redis_setGetRandom(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setGetRandom(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setIsMember,method = RequestMethod.POST)
    public GlobalEntity redis_setIsMember(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setIsMember(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setRemove,method = RequestMethod.POST)
    public GlobalEntity redis_setRemove(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setRemove(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-set")
    @RequestMapping(value = ApiConstant.Study.redis_setSize,method = RequestMethod.POST)
    public GlobalEntity redis_setSize(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_setSize(paramString);
        return ret;
    }

    @Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetAdd,method = RequestMethod.POST)
    public GlobalEntity redis_zsetAdd(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetAdd(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetGetByRange,method = RequestMethod.POST)
    public GlobalEntity redis_zsetGetByRange(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetGetByRange(paramString);
        return ret;
    }
    @Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetGetByRangeReverse,method = RequestMethod.POST)
    public GlobalEntity redis_zsetGetByRangeReverse(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetGetByRangeReverse(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetGetByScoreRange,method = RequestMethod.POST)
    public GlobalEntity redis_zsetGetByScoreRange(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetGetByScoreRange(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetGetByScoreRangeReverse,method = RequestMethod.POST)
    public GlobalEntity redis_zsetGetByScoreRangeReverse(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetGetByScoreRangeReverse(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetSize,method = RequestMethod.POST)
    public GlobalEntity redis_zsetSize(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetSize(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetRemove,method = RequestMethod.POST)
    public GlobalEntity redis_zsetRemove(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetRemove(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetRemoveByRange,method = RequestMethod.POST)
    public GlobalEntity redis_zsetRemoveByRange(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetRemoveByRange(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetRemoveByScoreRange,method = RequestMethod.POST)
    public GlobalEntity redis_zsetRemoveByScoreRange(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetRemoveByScoreRange(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetGetScore,method = RequestMethod.POST)
    public GlobalEntity redis_zsetGetScore(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetGetScore(paramString);
        return ret;
    }@Operation(summary = "redis学习接口", description = "list-zset")
    @RequestMapping(value = ApiConstant.Study.redis_zsetIncrementScore,method = RequestMethod.POST)
    public GlobalEntity redis_zsetIncrementScore(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.redis_zsetIncrementScore(paramString);
        return ret;
    }



    @Operation(summary = "cookie学习接口", description = "cookie-创建")
    @RequestMapping(value = ApiConstant.Study.cookie_create,method = RequestMethod.POST)
    public GlobalEntity cookie_create(@RequestBody String paramString, HttpServletResponse response)
    {
        GlobalEntity ret = techService.cookie_create(paramString, response);
        return ret;
    }

    @Operation(summary = "cookie学习接口", description = "cookie-更新")
    @RequestMapping(value = ApiConstant.Study.cookie_update,method = RequestMethod.POST)
    public GlobalEntity cookie_update(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.cookie_update(paramString);
        return ret;
    }

    @Operation(summary = "cookie学习接口", description = "cookie-查询")
    @RequestMapping(value = ApiConstant.Study.cookie_search,method = RequestMethod.POST)
    public GlobalEntity cookie_search(@RequestBody String paramString,HttpServletRequest request)
    {
        GlobalEntity ret = techService.cookie_search(paramString,request);
        return ret;
    }

    @Operation(summary = "cookie学习接口", description = "cookie-删除")
    @RequestMapping(value = ApiConstant.Study.cookie_delete,method = RequestMethod.POST)
    public GlobalEntity cookie_delete(@RequestBody String paramString,HttpServletRequest request,HttpServletResponse response)
    {
        GlobalEntity ret = techService.cookie_delete(paramString,request,response);
        return ret;
    }

    @Operation(summary = "session学习接口", description = "session-创建")
    @RequestMapping(value = ApiConstant.Study.session_create,method = RequestMethod.POST)
    public GlobalEntity session_create(@RequestBody String paramString, HttpServletRequest request)
    {
        GlobalEntity ret = techService.session_create(paramString,request);
        return ret;
    }
    @Operation(summary = "session学习接口", description = "session-更新")
    @RequestMapping(value = ApiConstant.Study.session_update,method = RequestMethod.POST)
    public GlobalEntity session_update(@RequestBody String paramString, HttpServletRequest request)
    {
        GlobalEntity ret = techService.session_update(paramString,request);
        return ret;
    }
    @Operation(summary = "session学习接口", description = "session-查询")
    @RequestMapping(value = ApiConstant.Study.session_search,method = RequestMethod.POST)
    public GlobalEntity session_search(@RequestBody String paramString, HttpServletRequest request)
    {
        GlobalEntity ret = techService.session_search(paramString,request);
        return ret;
    }
    @Operation(summary = "session学习接口", description = "session-删除")
    @RequestMapping(value = ApiConstant.Study.session_delete,method = RequestMethod.POST)
    public GlobalEntity session_delete(@RequestBody String paramString, HttpServletRequest request)
    {
        GlobalEntity ret = techService.session_delete(paramString,request);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-创建索引")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_createIndex, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_createIndex(@RequestBody String paramString)
    {
        return techService.elasticsearch_createIndex(paramString);
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-删除索引")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_deleteIndex, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_deleteIndex(@RequestBody String paramString)
    {
        return techService.elasticsearch_deleteIndex(paramString);
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-检查索引是否存在")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_existsIndex, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_existsIndex(@RequestBody String paramString)
    {
        return techService.elasticsearch_existsIndex(paramString);
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-获取索引元数据")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_getIndex, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_getIndex(@RequestBody String paramString)
    {
        return techService.elasticsearch_getIndex(paramString);
    }

    @Operation(summary = "elasticsearch-插入文档", description = "elasticsearch-插入文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_indexDocument, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_indexDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_indexDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch-获取文档", description = "elasticsearch-获取文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_getDocument, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_getDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_getDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch-检查文档是否存在", description = "elasticsearch-检查文档是否存在")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_existsDocument, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_existsDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_existsDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch-更新文档", description = "elasticsearch-更新文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_updateDocument, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_updateDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_updateDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch-删除文档", description = "elasticsearch-删除文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_deleteDocument, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_deleteDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_deleteDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch-批量操作文档", description = "elasticsearch-批量操作文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_bulkInsert, method = RequestMethod.POST)
    public GlobalEntity elasticsearch_bulkInsert(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_bulkInsert(paramString);
        return ret;
    }

    @Operation(summary = "kafka学习接口", description = "kafka-生产一条消息")
    @RequestMapping(value = ApiConstant.Study.kafka_Product,method = RequestMethod.POST)
    public GlobalEntity kafka_Product(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.kafka_Product(paramString);
        return ret;
    }

    @Operation(summary = "kafka学习接口", description = "kafka-消费一条消息")
    @RequestMapping(value = ApiConstant.Study.kafka_Consume,method = RequestMethod.POST)
    public GlobalEntity kafka_Consume(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.kafka_Consume(paramString);
        return ret;
    }


    @Operation(summary = "minio学习接口", description = "minio-创建bucket")
    @RequestMapping(value = ApiConstant.Study.minio_createBucket,method = RequestMethod.POST)
    public GlobalEntity minio_createBucket(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.minio_createBucket(paramString);
        return ret;
    }

    @Operation(summary = "minio学习接口", description = "minio-列出bucket和对象")
    @RequestMapping(value = ApiConstant.Study.minio_searchBucket,method = RequestMethod.POST)
    public GlobalEntity minio_searchBucket(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.minio_searchBucket(paramString);
        return ret;
    }

    @Operation(summary = "minio学习接口", description = "minio-删除bucket")
    @RequestMapping(value = ApiConstant.Study.minio_deleteBucket,method = RequestMethod.POST)
    public GlobalEntity minio_deleteBucket(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.minio_deleteBucket(paramString);
        return ret;
    }

    @Operation(summary = "minio学习接口", description = "minio-上传文件")
    @RequestMapping(value = ApiConstant.Study.minio_uploadFile,method = RequestMethod.POST)
    public GlobalEntity minio_uploadFile(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.minio_uploadFile(paramString);
        return ret;
    }

    @Operation(summary = "minio学习接口", description = "minio-下载文件")
    @RequestMapping(value = ApiConstant.Study.minio_downloadFile,method = RequestMethod.POST)
    public GlobalEntity minio_downloadFile(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.minio_downloadFile(paramString);
        return ret;
    }

    @Operation(summary = "minio学习接口", description = "minio-删除文件")
    @RequestMapping(value = ApiConstant.Study.minio_deleteFile,method = RequestMethod.POST)
    public GlobalEntity minio_deleteFile(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.minio_deleteFile(paramString);
        return ret;
    }


    @Operation(summary = "fastexcel学习接口", description = "fastexcel-下载excel模板")
    @RequestMapping(value = ApiConstant.Study.fastexcel_downloadExcel,method = RequestMethod.POST)
    public GlobalEntity fastexcel_downloadExcel(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.fastexcel_downloadExcel(paramString);
        return ret;
    }

    @Operation(summary = "fastexcel学习接口", description = "fastexcel-上传文件")
    @RequestMapping(value = ApiConstant.Study.fastexcel_uploadExcel,method = RequestMethod.POST)
    public GlobalEntity fastexcel_uploadExcel(@RequestParam("file") MultipartFile file)
    {
        GlobalEntity ret = techService.fastexcel_uploadExcel(file);
        return ret;
    }


}
