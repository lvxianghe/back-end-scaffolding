package org.xiaoxingbomei.service.implement;

import cn.hutool.core.collection.CollectionUtil;
import cn.idev.excel.FastExcel;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoxingbomei.config.fastexcel.CommonLogUploadDataListener;
import org.xiaoxingbomei.config.minio.MinioConfig;
import org.xiaoxingbomei.service.TechService;
import org.xiaoxingbomei.utils.ES_Utils;
import org.xiaoxingbomei.utils.Exception_Utils;
import org.xiaoxingbomei.vo.BusinessLogCommon;
import org.xiaoxingbomei.vo.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.utils.Minio_Utils;
import org.xiaoxingbomei.utils.Redis_Utils;
import org.xiaoxingbomei.common.utils.Request_Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TechServiceImpl implements TechService
{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier("mongoTemplateOfLocal")
    private MongoTemplate mongoTemplateOfLocal;

    // @Autowired
    // @Qualifier("mongoTemplateOfDgs")
    // private MongoTemplate mongoTemplateOfDgs;

    @Autowired
    private Redis_Utils redisUtils;

    @Autowired
    private Minio_Utils minioUtils;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumer<String,String> kafkaConsumer;

    @Autowired
    private CommonLogUploadDataListener commonLogUploadDataListener;


    @Autowired
    private ES_Utils esUtils;

    // @Autowired
    // private RestHighLevelClient restHighLevelClient;


    @Override
    public GlobalEntity mongodb_Insert(String paramString)
    {
        User user = JSONObject.parseObject(paramString, User.class);

    //  String name = mongoTemplateOfDgs.getDb().getName();
    //  log.info("dgs database name:{}",name);

        mongoTemplateOfLocal.insert(user);

        return GlobalEntity.success("测试 mongodbTemplate insert 成功");
    }

    @Override
    public GlobalEntity mongodb_MultiInsert(String paramString)
    {

        String      userList    = Request_Utils.getParam(paramString, "userList");
        List<User>  users       = JSONObject.parseArray(userList, User.class);

        if(!CollectionUtil.isEmpty(users))
        {

            mongoTemplateOfLocal.insert(users,User.class);
        }
        else
        {
            return GlobalEntity.error("测试 mongodbTemplate multi insert 失败，数据为空");
        }

        return GlobalEntity.success("测试 mongodbTemplate multi insert 成功");
    }

    @Override
    public GlobalEntity mongodb_Save(String paramString)
    {
        return GlobalEntity.success("测试 mongodbTemplate multi insert 成功");
    }

    @Override
    public GlobalEntity mongodb_Search(String paramString)
    {
        return GlobalEntity.success("测试 mongodb search 成功");
    }

    @Override
    public GlobalEntity mongodb_Update(String paramString)
    {
        return GlobalEntity.success("测试 mongodb update 成功");
    }

    @Override
    public GlobalEntity mongodb_Delete(String paramString)
    {
        return GlobalEntity.success("测试 mongodbTemplate delete 成功");
    }


    @Override
    public GlobalEntity fastJson_ObjectToJsonString(String paramString)
    {
        // 1、构建对象
        User user = new User("吕相赫", "139xxxx", "777", "999", "浙江杭州");
        
        // 2、对象转JSONString
        String jsonString           = JSON.toJSONString(user);
        String jsonStringPrettified = JSON.toJSONString(user, SerializerFeature.PrettyFormat);

        // 3、构建反参
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultJSONStirng",jsonString);
        resultMap.put("resultJSONStirngPrettified",jsonStringPrettified);

        return GlobalEntity.success(resultMap,"fastjson-序列化,将一个对象转为jsonString");

    }

    @Override
    public GlobalEntity fastJson_JsonStringToObject(String paramString)
    {
        // 1、接收前端jsonString
        String jsonString1 = Request_Utils.getParam(paramString,"jsonString1");
        String jsonString2 = Request_Utils.getParam(paramString,"jsonString2");

        // 2、JSONString转对象
        User user1 = JSON.parseObject(jsonString1, User.class);
        User user2 = JSON.parseObject(jsonString2, User.class);

        // 3、构建反参
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultObject1",user1.toString());
        resultMap.put("resultObject2",user2.toString());

        return GlobalEntity.success(resultMap,"fastjson-反序列化，将jsonString转成对象");
    }

    @Override
    public GlobalEntity fastJson_PrettyPrint(String paramString)
    {
        // 1、构建对象
        User user = new User("吕相赫", "139xxxx", "777", "999", "浙江杭州");

        // 2、对象转JSONString
        String jsonStringPrettified = JSONObject.toJSONString(user, SerializerFeature.PrettyFormat);

        // 3、构建反参
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultJSONStirngPrettified",jsonStringPrettified);
        return GlobalEntity.success(resultMap,"fastjson-格式化,美化打印jsonString");
    }

    @Override
    public GlobalEntity fastJson_JSONStringToMap(String paramString)
    {
        // 1、接收前端参数
        String jsonString = Request_Utils.getParam(paramString, "jsonString");

        // 2、jsonString转map
        HashMap<String, Object> map = JSONObject.parseObject(jsonString, HashMap.class);

        // 3、构建反参
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultMap",map.toString());
        return GlobalEntity.success(resultMap,"fastjson-JSON转为Map");

    }

    @Override
    public GlobalEntity fastJson_JSONStringToList(String paramString)
    {
        // 1、接收前端参数
        String jsonString = Request_Utils.getParam(paramString, "jsonString");

        // 2、JSON转为List
        List<User> userList = JSON.parseArray(jsonString, User.class);

        // 3、构建反参
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultMap",userList.toString());
        return GlobalEntity.success(resultMap,"fastjson-JSON转为List");
    }

    @Override
    public GlobalEntity redis_StringSet(String paramString)
    {
        // 1、判断redis链接状态，如果redis挂了就没必要进行了
        if(!redisUtils.isRedisConnected())
        {
            return GlobalEntity.error("redis暂不可用");
        }

        // 2、获取前端参数
        String redisStringValue = Request_Utils.getParam(paramString, "redisStringValue");
        boolean reditStringSetFlag = false;
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("successFlag",reditStringSetFlag);

        // 3、redis string set
        try
        {
            redisTemplate.opsForValue().set("keyByRedisTemplate",redisStringValue); //
            stringRedisTemplate.opsForValue().set("keyByStringRedisTemplate",redisStringValue);
            reditStringSetFlag = true;
        } catch (Exception e)
        {
            return GlobalEntity.error(resultMap,"redis-String存储,失败"+e.getMessage());
        }

        // 4、封装结果参数
        resultMap.put("successFlag",reditStringSetFlag);
        return GlobalEntity.success(resultMap,"redis-String存储");
    }

    @Override
    public GlobalEntity redis_StringSetWithExpire(String paramString)
    {
        // 1、判断redis链接状态，如果redis挂了就没必要进行了
        if(!redisUtils.isRedisConnected())
        {
            return GlobalEntity.error("redis暂不可用");
        }

        // 2、获取前端参数
        String redisStringValue = Request_Utils.getParam(paramString, "redisStringValue");
        boolean reditStringSetFlag = false;
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("successFlag",reditStringSetFlag);

        // 3、redis string set with expire
        try
        {
            redisTemplate.opsForValue().set("StringWithExpire",redisStringValue,120, TimeUnit.SECONDS);
            reditStringSetFlag = true;
        } catch (Exception e)
        {
            return GlobalEntity.error(resultMap,"redis-String存储,失败"+e.getMessage());
        }

        // 4、封装结果参数
        resultMap.put("successFlag",reditStringSetFlag);
        return GlobalEntity.success(resultMap,"redis-String存储");
    }

    @Override
    public GlobalEntity redis_StringGet(String paramString)
    {
        // 1、判断redis链接状态，如果redis挂了就没必要进行了
        if(!redisUtils.isRedisConnected())
        {
            return GlobalEntity.error("redis暂不可用");
        }

        // 2、获取前端参数
        String redisStringKey = Request_Utils.getParam(paramString, "redisStringKey");
        List<String> redisStringKeyList = Arrays.asList(redisStringKey.split(",", -1));

        // 3、查询对应的rediskey，循环封装进hashmap中
        Map<String, Object> resultMap = redisStringKeyList.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> redisTemplate.opsForValue().get(key)
                ));

        // 4、封装结果对象
        return GlobalEntity.success(resultMap,"redis-String获取");
    }

    @Override
    public GlobalEntity redis_StringDelete(String paramString)
    {
        // 1、判断redis链接状态，如果redis挂了就没必要进行了
        if(!redisUtils.isRedisConnected())
        {
            return GlobalEntity.error("redis暂不可用");
        }

        // 2、获取前端参数
        String redisStringKey = Request_Utils.getParam(paramString, "redisStringKey");
        List<String> redisStringKeyList = Arrays.asList(redisStringKey.split(",", -1));

        // 3、删除对应的redis key
        Long deleteResult = redisTemplate.delete(redisStringKeyList);

        // 封装
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("deleteResult",deleteResult);
        return GlobalEntity.success(resultMap,"redis-String刪除");
    }

    @Override
    public GlobalEntity redis_StringIncr(String paramString)
    {
        // 1、判断redis连接状态，如果redis挂了就没必要进行了
        if(!redisUtils.isRedisConnected())
        {
            return GlobalEntity.error("redis暂不可用");
        }

        // 2、获取前端参数
        String redisStringKey = Request_Utils.getParam(paramString, "redisStringKey"); // redis key
        String delta          = Request_Utils.getParam(paramString, "delta");          // 步长


        Long increment = 0L;
        // 2、获取前端参数
        try
        {
            // 将指定的键给定的增量，如果键不存在，则会先将该键设置为0，然后再进行自增操作
            increment = redisTemplate.opsForValue().increment(redisStringKey, Long.parseLong(delta));
        }catch (Exception e)
        {
            return GlobalEntity.error("redis自增失败"+e.getMessage());
        }

        // 3、封装结果参数
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("increment",increment);
        return GlobalEntity.success(resultMap,"redis-String自增");
    }

    @Override
    public GlobalEntity redis_StringDecr(String paramString)
    {
        // 1、判断redis连接状态，如果redis挂了就没必要进行了
        if(!redisUtils.isRedisConnected())
        {
            return GlobalEntity.error("redis暂不可用");
        }

        // 2、获取前端参数
        String redisStringKey = Request_Utils.getParam(paramString, "redisStringKey"); // redis key
        String delta          = Request_Utils.getParam(paramString, "delta");          // 步长

        Long decrement = 0L;
        // 2、获取前端参数
        try
        {
            // 将指定的键给定的增量，如果键不存在，则会先将该键设置为0，然后再进行自增操作
            decrement = redisTemplate.opsForValue().decrement(redisStringKey, Long.parseLong(delta));
        }catch (Exception e)
        {
            return GlobalEntity.error("redis自减失败"+e.getMessage());
        }

        // 3、封装结果参数
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("decrement",decrement);
        return GlobalEntity.success(resultMap,"redis-String自减");
    }

    @Override
    public GlobalEntity cookie_create(String paramString, HttpServletResponse response)
    {
        // 1、接收前端参数
        String userName = Request_Utils.getParam(paramString, "userName");

        // 2、创建一个Cookie对象
        Cookie cookie = new Cookie("userName", userName);
        Cookie cookie2 = new Cookie("userName2", "second"+userName);
        cookie.setMaxAge(60*60);   // 有效期1小时
        cookie.setPath("/");       // 根路径下的所有请求都能获得这个Cookie
        cookie.setHttpOnly(true);  // 设置cookie为httponly，浏览器脚本无法访问
        response.addCookie(cookie);// 将cookie添加到响应头中
        response.addCookie(cookie2);// 将cookie添加到响应头中

        return GlobalEntity.success("创建cookie成功");
    }

    @Override
    public GlobalEntity cookie_update(String paramString)
    {
        return null;
    }

    @Override
    public GlobalEntity cookie_search(String paramString,HttpServletRequest request)
    {
        // 1、获取前端参数
        String cookieKey = Request_Utils.getParam(paramString, "cookieKey");

        // 获取全部cookie
        HashMap<String, Object> resultMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (StringUtils.equals(cookieKey, cookie.getName()))
                {
                    resultMap.put("successFlag",true);
                    resultMap.put("userName",cookie.getValue());
                }
            }
        }
        return GlobalEntity.success(resultMap,"cookie-获取指定cookie成功");
    }

    @Override
    public GlobalEntity cookie_delete(String paramString,HttpServletRequest request,HttpServletResponse response)
    {
        // 1、获取前端参数
        String cookieKey = Request_Utils.getParam(paramString, "cookieKey");

        // 2、注销
        HashMap<String, Object> resultaMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if(StringUtils.equals(cookieKey, cookie.getName()))
                {
                    // 设置Cookie的过期时间为 0，浏览器会自动删除他
                    cookie.setMaxAge(0);
                    cookie.setPath("/"); // 确保删除时路径一致
                    // 将删除的Cookie添加到响应中
                    response.addCookie(cookie);
                    resultaMap.put("successFlag",true);
                    resultaMap.put("cookieKey",cookieKey);
                    resultaMap.put("cookieValue",cookie.getValue());
                }
            }
        }
        return GlobalEntity.success(resultaMap,"cookie-删除成功");
    }

    @Override
    public GlobalEntity session_create(String paramString, HttpServletRequest request)
    {
        // 1、接收前端参数
        String userId   = Request_Utils.getParam(paramString, "userId");
        String userName = Request_Utils.getParam(paramString, "userName");

        // 2、获取/创建session
        HashMap<String, Object> resultMap = new HashMap<>();
        if(true) // 这里模拟登陆成功
        {
            HttpSession session = request.getSession(true); // 如果不存在则创建一个新的session
            session.setAttribute("userId",userId);
            session.setAttribute("userName",userName);

            resultMap.put("successFlag",true);
            resultMap.put("userId",userId);
            resultMap.put("userName",userName);
            resultMap.put("sessionId",session.getId());
            // resultMap.put("servletContext",session.getServletContext()); // 加上这个会出现一个很奇怪的bug  以后再排查 todo
        }
        return GlobalEntity.success(resultMap,"session-创建并设置初始化信息成功");
    }

    @Override
    public GlobalEntity session_update(String paramString, HttpServletRequest request)
    {
        // 1、获取前端参数
        String sessionId = Request_Utils.getParam(paramString, "sessionId");
        String userId = Request_Utils.getParam(paramString, "userId");
        String useName = Request_Utils.getParam(paramString, "useName");

        // 2、修改session信息
        HashMap<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession(false); // 获取当前session，如果没有则为null
        if(session!=null)
        {
            session.setAttribute("userId",userId);
            session.setAttribute("userName",useName);
            resultMap.put("successFlag",true);
        }

        // 3、封装返回体
        resultMap.put("sessionId",session.getId());
        resultMap.put("userId",userId);
        resultMap.put("useName",useName);
        return GlobalEntity.success(resultMap,"session-修改session信息成功");
    }

    @Override
    public GlobalEntity session_search(String paramString, HttpServletRequest request)
    {
        // 1、获取前端参数
        String sessionId = Request_Utils.getParam(paramString, "sessionId");

        // 2、
        HashMap<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession(false); // 如果没有session，则返回null
        if(sessionId!=null)
        {
            resultMap.put("message","session is alive");
        }

        // 3、封装返回体
        resultMap.put("sessionId",session.getId());
        resultMap.put("successFlag",true);
        resultMap.put("inputSessionId",sessionId);
        resultMap.put("userName",session.getAttribute("userName"));
        resultMap.put("userId",session.getAttribute("userId"));

        return GlobalEntity.success(resultMap,"session-获取session信息成功");
    }

    @Override
    public GlobalEntity session_delete(String paramString, HttpServletRequest request)
    {
        // 1、获取前端参数
        String sessionId = Request_Utils.getParam(paramString, "sessionId");

        // 2、注销/销毁session
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("successFlag",false);
        HttpSession session = request.getSession(false);
        if(session!=null && StringUtils.equals(session.getId(),sessionId))
        {
            session.invalidate();
            resultMap.put("successFlag",true);
        }

        // 3、封装返回体
        resultMap.put("sessionId",sessionId);
        return GlobalEntity.success(resultMap,"session-删除session信息成功");
    }

    @Override
    public GlobalEntity elasticsearch_createIndex(String paramString)
    {
        HashMap<String, Object> resultMap = null;
        try {
            // 1、接收前端参数
            String indexName = Request_Utils.getParam(paramString,"indexName");

            // 2、客户端执行请求 IndicesClient,请求后获得响应
            CreateIndexResponse indexCreateResult = esUtils.createIndex(indexName,null,null);

            // 3、封装返回参数
            resultMap = new HashMap<>();
            resultMap.put("indexName",indexName);
            resultMap.put("responseString",indexCreateResult.toString());
        } catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("elasticsearch创建索引失败{}",e.getMessage());
        }
        return GlobalEntity.success(resultMap,"elasticsearch创建索引成功");
    }

    @Override
    public GlobalEntity elasticsearch_deleteIndex(String paramString)
    {
        HashMap<String, Object> resultMap = new HashMap<>();
        try
        {
            // 1、接收前端参数
            String indexName = Request_Utils.getParam(paramString, "indexName");

            // 2、删除索引
            DeleteIndexResponse deleteIndexResult = esUtils.deleteIndex(indexName);

            // 3、封装结果参数
            resultMap.put("responseString",deleteIndexResult.toString());
            resultMap.put("indexName",indexName);

        } catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("删除索引失败：{}",e.getMessage());
            return GlobalEntity.error(resultMap,"elasticsearch-删除索引失败"+e.getMessage());
        }
        return GlobalEntity.success(resultMap,"elasticsearch-删除索引成功");
    }

    @Override
    public GlobalEntity elasticsearch_insertDocument(String paramString)
    {
        // 1、接收前端参数
        String indexName = Request_Utils.getParam(paramString, "indexName"); // 索引
        String id        = Request_Utils.getParam(paramString, "id");        // 文档id（可选）

        // 2、封装要插入的数据
        HashMap<String, Object> insertEsMap = new HashMap<>();
        insertEsMap.put("tittle","吕相赫学习es");
        insertEsMap.put("content","一步一步努力精通es");
        insertEsMap.put("author","吕相赫");
        insertEsMap.put("createTime","2024-12-23");

        // 3、插入数据
        HashMap<String, Object> resultMap = new HashMap<>();
        try
        {
            IndexResponse insertDocumentResult = esUtils.insertDocument(indexName, id, insertEsMap);
            resultMap.put("responseString",insertDocumentResult.toString());
            resultMap.put("responseStringFlag",insertDocumentResult.result().name().equalsIgnoreCase("Created"));
        } catch (IOException e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("elasticsearch-插入单条数据失败{}",e.getMessage());
        }

        // 4、封装返回结果
        resultMap.put("insertEsMap",insertEsMap);
        resultMap.put("indexName",indexName);
        resultMap.put("id",id);
        return GlobalEntity.success(resultMap,"elasticsearch，插入单条数据成功");
    }

    @Override
    public GlobalEntity elasticsearch_getDocument(String paramString)
    {
        // 1、获取前端参数
        String indexName = Request_Utils.getParam(paramString, "indexName");
        String id = Request_Utils.getParam(paramString, "id");

        // 2、根据索引和文档id查询指定单条数据
        HashMap<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> document = esUtils.getDocument(indexName, id);
            resultMap.put("responseString",document.toString());
        } catch (IOException e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("elasticsearch-查询单条数据{}",e.getMessage());
        }

        // 3、封装结果
        resultMap.put("indexName",indexName);
        resultMap.put("id",id);

        return GlobalEntity.success(resultMap,"elasticsearch-搜索单条数据成功");
    }

    @Override
    public GlobalEntity elasticsearch_deleteDocument(String paramString)
    {
        // 1、获取前端参数
        String indexName = Request_Utils.getParam(paramString, "indexName");
        String id = Request_Utils.getParam(paramString, "id");

        // 2、删除单条数据
        HashMap<String, Object> resultMap = new HashMap<>();
        try
        {
            DeleteResponse deleteResponse = esUtils.deleteDocument(indexName, id);
            resultMap.put("responseString",deleteResponse.toString());
        } catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("elasticsearch-删除单条数据失败：{}",e.getMessage());
        }

        // 3、封装结果
        resultMap.put("indexName",indexName);
        resultMap.put("id",id);
        return GlobalEntity.success(resultMap,"elasticsearch-删除单条数据成功");

    }
    

    @Override
    public GlobalEntity elasticsearch_updateDocument(String paramString)
    {
        // 1、接收前端参数
        String indexName       = Request_Utils.getParam(paramString, "indexName");
        String id              = Request_Utils.getParam(paramString, "id");
        String updateMapString = Request_Utils.getParam(paramString, "updateMapString");
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<Object, Object> resultmap = new HashMap<>();
        try
        {
            HashMap updateHashMap = objectMapper.readValue(updateMapString, HashMap.class);
            UpdateResponse updateResponse = esUtils.updateDocument(indexName, id, updateHashMap);
            resultmap.put("responseString",updateResponse.toString());

        } catch (IOException e)
        {

            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("elasticsearch-更新单条数据{}",e.getMessage());
        }
        resultmap.put("indexName",indexName);
        resultmap.put("id",id);
        return GlobalEntity.success(resultmap,"elasticsearch-更新单条数据成功");
    }

    @Override
    public GlobalEntity elasticsearch_bulkInsertDocument(String paramString)
    {
        // 1、接收前端参数
        String indexName            = Request_Utils.getParam(paramString, "indexName"); // 需要批量操作的索引
        String docs                 = Request_Utils.getParam(paramString, "docs");      // 批量操作的文档
        List<HashMap> docsMapList   = JSON.parseArray(docs, HashMap.class);

        Map<String,Map<String,Object>> bulkInsertDocumentHashMap = new HashMap<>();

        for (HashMap docMap : docsMapList)
        {
            String docId = UUID.randomUUID().toString();
            bulkInsertDocumentHashMap.put(docId,docMap);
        }

        // 2、批量插入或更新文档
        HashMap<Object, Object> resultMap = new HashMap<>();
        try
        {
            BulkResponse bulkResponse = esUtils.bulkInsertDocument(indexName, bulkInsertDocumentHashMap);
            resultMap.put("responseString",bulkResponse.toString());
        } catch (IOException e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("elasticsearch-批量插入或更新文档失败{}",e.getMessage());
        }
        resultMap.put("indexName",indexName);
        resultMap.put("id",docs);
        return GlobalEntity.success(resultMap,"elasticsearch-批量插入数据或更新成功");

    }

    @Override
    public GlobalEntity elasticsearch_batch(String paramString)
    {
        return null;
    }

    @Override
    public GlobalEntity elasticsearch_match(String paramString)
    {
        return null;
    }

    @Override
    public GlobalEntity elasticsearch_term(String paramString)
    {
        return null;
    }

    @Override
    public GlobalEntity kafka_Product(String paramString)
    {
        // 1、接收前端参数
        String topic    = Request_Utils.getParam(paramString, "topic");
        String message  = Request_Utils.getParam(paramString, "message");

        // 2、
        kafkaTemplate.send(topic,message);
        log.info("kafka produced message:{}"+message);

        // 3、
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("successFlag",true);
        resultMap.put("message",message);
        resultMap.put("topic",topic);
        return GlobalEntity.success(resultMap,"kafka-生产消息-成功");
    }

    @Override
    public GlobalEntity kafka_Consume(String paramString)
    {
        // 1、接收前端参数
        String topic = Request_Utils.getParam(paramString, "topic");
        kafkaConsumer.subscribe(Arrays.asList(topic)); // 订阅指定的topic

        // 2、消费kafka消息
        boolean consumeFlag = true;
        while (consumeFlag)
        {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(1000); // 拉取消息，等待1000毫秒
            for (ConsumerRecord<String, String> record : records)
            {
                log.info
                        ("消费到消息: topic={},partition={},offset={},key={},value={}",
                        record.topic(),
                        record.partition(),
                        record.offset(),
                        record.key(),
                        record.value()
                        );
                consumeFlag = false;
            }
        }

        // 3、构建反参
        HashMap<String, Object> resultMap = new HashMap<>();
        return GlobalEntity.success("kafka-消費消息-成功");
    }

    @Override
    public GlobalEntity minio_createBucket(String paramString)
    {
        // 1、获取前端参数
        String bucketName = Request_Utils.getParam(paramString, "bucketName");

        // 2、尝试创建bucket，如果不存在则创建
        MinioClient minioClient = minioConfig.createMinioClient();

        try
        {
            boolean bucketExists = minioClient.bucketExists(bucketName);
            if(!bucketExists)
            {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("minio bucket created:{}",bucketName);
            }
            else
            {
                log.info("minio bucket already exists:{}",bucketName);
            }
        }catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("minio-创建bucket失败");
        }

        // 3、构建返回体
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("bucketName",bucketName);
        return GlobalEntity.success(resultMap,"minio-创建bucket成功");
    }

    @Override
    public GlobalEntity minio_searchBucket(String paramString)
    {
        // 1、获取前端参数
        String bucketName = Request_Utils.getParam(paramString, "bucketName");
        HashMap<String, Object> resultMap = new HashMap<>();

        // 2、列出minio的桶及对象信息
        try
        {
            // 创建minio客户端
            MinioClient minioClient = minioConfig.createMinioClient();

            // 如果有指定的bucket，则获取其及对象
            if(StringUtils.isEmpty(bucketName))
            {
                List<Bucket> allBuckets = minioClient.listBuckets();
                for (Bucket bucket : allBuckets)
                {
                    String tempBucketName = bucket.name();
                    List<String> objecNamesInBucket = minioUtils.listObjectsInBucket(minioClient,tempBucketName);
                    resultMap.put(tempBucketName,objecNamesInBucket);
                }
            }
            // 如果没有指定的桶，则列出全部的桶及其对象
            else
            {
                List<String> objecNamesInBucket = minioUtils.listObjectsInBucket(minioClient,bucketName);
                resultMap.put(bucketName,objecNamesInBucket);
            }
        }catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("minio-获取所有桶及对象-失败"+e.getMessage());
        }

        // 3、构建返回体
        return GlobalEntity.success(resultMap,"minio-获取所有桶及对象");
    }


    @Override
    public GlobalEntity minio_deleteBucket(String paramString)
    {
        // 1、获取前端参数
        String bucketName = Request_Utils.getParam(paramString, "bucketName");
        List<String> bucketNameList = Arrays.asList(bucketName.split(",", -1));

        // 2、删除bucket
        MinioClient minioClient = minioConfig.createMinioClient();
        try
        {
            for (String bucketToDelete : bucketNameList)
            {
                // 判断桶是否存在
                boolean bucketExist = minioUtils.isBucketExist(minioClient,bucketToDelete);
                if(!bucketExist)
                {
                    continue;
                }
                // 如果桶存在则删除桶及其下面全部对象
                minioUtils.deleteBucket(minioClient,bucketToDelete);
            }
        }catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("minio-删除bucket-失败:{}"+e.getMessage());
        }

        // 3、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("bucketName",bucketName);
        return GlobalEntity.success("minio-删除bucket-成功");
    }

    @Override
    public GlobalEntity minio_uploadFile(String paramString)
    {
        // 1、获取前端参数
        String bucketName = Request_Utils.getParam(paramString, "bucketName");
        String filePath   = Request_Utils.getParam(paramString, "filePath");
        String fileName   = Request_Utils.getParam(paramString, "fileName");

        // 2、确保文件编码 minio不需要这个 自动会编码中文
        // try
        // {
        //     fileName = URLEncoder.encode(fileName, "UTF-8");
        // }catch (UnsupportedEncodingException e)
        // {
        //     Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        //     return GlobalEntity.error("minio-上传文件-失败:"+e.getMessage());
        // }

        // 3、上传文件
        try
        {
            MinioClient minioClient = minioConfig.createMinioClient();
            minioClient.putObject
                    (
                            PutObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(fileName)
                                    .stream(
                                            Files.newInputStream(Paths.get(filePath)),
                                            Files.size(Paths.get(filePath)),
                                            -1
                                    )
                                    .contentType("application/octet-stream")
                                    .build()
                    );
            log.info("minio bucket uploaded:{}",bucketName);
        }
        catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("minio bucket uploaded falure:{}"+e.getMessage());
        }

        // 4、返回
        return GlobalEntity.success("minio bucket uploaded success");
    }

    @Override
    public GlobalEntity minio_downloadFile(String paramString)
    {
        // 1、获取前端参数
        String bucketName     = Request_Utils.getParam(paramString, "bucketName");  // 目标桶
        String objectName     = Request_Utils.getParam(paramString, "objectName");  // 目标下载文件
        String downloadPath   = Request_Utils.getParam(paramString, "downloadPath");// 下载到哪里

        // 2、下载文件 try-with-resource 自动释放资源
        // 获取minio客户端
        MinioClient minioClient = minioConfig.createMinioClient();
        try(
            // 创建对象输入流
            InputStream minioInputStream = minioClient.getObject
                    (
                            GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(objectName)
                                    .build()
                    );
            // 打开输出流并写入数据
            FileOutputStream fileOutputStream = new FileOutputStream(downloadPath))
            {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ( (bytesRead =minioInputStream.read(buffer)) != -1)
                {
                    fileOutputStream.write(buffer,0,bytesRead);
                    log.info("minio文件已经成功下载:" + objectName + " 到 " + downloadPath);
                }
            }catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("minio-下载文件-失败:{}"+e.getMessage());
        }

        // 3、封装返回体
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("bucketName",bucketName);
        resultMap.put("objectName",objectName);
        resultMap.put("downloadPath",downloadPath);
        return GlobalEntity.success(resultMap,"minio-下载文件");
    }

    @Override
    public GlobalEntity minio_deleteFile(String paramString)
    {
        return null;
    }

    @Override
    public GlobalEntity fastexcel_downloadExcel(String paramString)
    {
        try
        {
            // 1、接收前端参数
            String fileName     = Request_Utils.getParam(paramString, "fileName");
            String bucketName   = Request_Utils.getParam(paramString, "bucketName");

            // 2、创建minio客户端
            MinioClient minioClient = minioConfig.createMinioClient();

            // 3、创建输出流，根据类来读取excel模板，用于写入minio
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FastExcel.write(outputStream, BusinessLogCommon.class)
                     .sheet("模板")
                    .head(BusinessLogCommon.class)
                    .doWrite(Arrays.asList());

            // 4、创建输入流，读取excel模板，上传minio
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            minioClient.putObject
                    (
                            PutObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(fileName)
                                    .stream(
                                            inputStream,
                                            inputStream.available(),
                                            -1
                                    )
                                    .contentType("application/octet-stream")
                                    .build()
                    );
            // 5、设置并获取文件下载链接，附带过期时间
            String fileUrl = minioClient.getObjectUrl(bucketName, fileName).toString();
            HashMap<String, String> options = new HashMap<>();
            options.put("expiry", String.valueOf(TimeUnit.DAYS.toSeconds(1)));
            String presignedUrl = minioClient.getPresignedObjectUrl(Method.GET,bucketName,fileName,Integer.valueOf((int) TimeUnit.DAYS.toSeconds(1)),options);

            // 6、封装结果参数
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("bucketName",bucketName);
            resultMap.put("fileName",fileName);
            resultMap.put("url",fileUrl);
            resultMap.put("presignedUrl",presignedUrl);
            return GlobalEntity.success(resultMap,"下载模板文件成功："+fileUrl);

        } catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("{}",e.getMessage());
        }
    }

    @Override
    public GlobalEntity fastexcel_uploadExcel(MultipartFile file)
    {
        if(file.isEmpty())
        {
            return GlobalEntity.error("请选择日志文件上传");
        }

        try
        {
            FastExcel.read(file.getInputStream(), BusinessLogCommon.class,commonLogUploadDataListener).sheet().doRead();
            return GlobalEntity.success("文件上传并处理成功");
        }catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            return GlobalEntity.error("文件上传失败:{}",e.getMessage());
        }

    }
}
