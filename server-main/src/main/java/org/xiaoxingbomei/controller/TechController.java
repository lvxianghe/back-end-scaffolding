package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoxingbomei.constant.ApiConstant;
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
    @RequestMapping(value = ApiConstant.Study.elasticsearch_createIndex,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_createIndex(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_createIndex(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-删除索引")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_deleteIndex,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_deleteIndex(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_deleteIndex(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-插入单条文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_insertDocument,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_insertDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_insertDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-查询单条文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_getDocument,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_getDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_getDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-删除单条文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_deleteDocument,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_deleteDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_deleteDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-更新单条文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_updateDocument,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_updateDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_updateDocument(paramString);
        return ret;
    }

    @Operation(summary = "elasticsearch学习接口", description = "elasticsearch-批量插入或者更新文档")
    @RequestMapping(value = ApiConstant.Study.elasticsearch_bulkInsertDocument,method = RequestMethod.POST)
    public GlobalEntity elasticsearch_bulkInsertDocument(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.elasticsearch_bulkInsertDocument(paramString);
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
