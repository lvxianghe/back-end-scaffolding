package org.xiaoxingbomei.service.implement;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jdk.nashorn.internal.objects.Global;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.config.minio.MinioConfig;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.service.TechService;
import org.xiaoxingbomei.utils.Exception_Utils;
import org.xiaoxingbomei.utils.Minio_Utils;
import org.xiaoxingbomei.utils.Redis_Utils;
import org.xiaoxingbomei.utils.Request_Utils;
import org.xiaoxingbomei.vo.User;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

//    @Autowired
//    @Qualifier("mongoTemplateOfDgs")
//    private MongoTemplate mongoTemplateOfDgs;

    @Autowired
    private Redis_Utils redisUtils;

    @Autowired
    private Minio_Utils minioUtils;

    @Autowired
    private MinioConfig minioConfig;




    @Override
    public GlobalEntity mongodb_Insert(String paramString)
    {
        User user = JSONObject.parseObject(paramString, User.class);

//        String name = mongoTemplateOfDgs.getDb().getName();
//        log.info("dgs database name:{}",name);

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
}
