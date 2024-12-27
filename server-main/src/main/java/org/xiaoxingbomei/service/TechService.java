package org.xiaoxingbomei.service;

import org.springframework.web.multipart.MultipartFile;
import org.xiaoxingbomei.common.entity.GlobalEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TechService
{

    public GlobalEntity mongodb_Insert     (String paramString);
    public GlobalEntity mongodb_MultiInsert(String paramString);
    public GlobalEntity mongodb_Save       (String paramString);
    public GlobalEntity mongodb_Search     (String paramString);
    public GlobalEntity mongodb_Update     (String paramString);
    public GlobalEntity mongodb_Delete     (String paramString);

    public GlobalEntity fastJson_ObjectToJsonString(String paramString);
    public GlobalEntity fastJson_JsonStringToObject(String paramString);
    public GlobalEntity fastJson_PrettyPrint       (String paramString);
    public GlobalEntity fastJson_JSONStringToMap   (String paramString);
    public GlobalEntity fastJson_JSONStringToList  (String paramString);

    public GlobalEntity redis_StringSet          (String paramString);
    public GlobalEntity redis_StringSetWithExpire(String paramString);
    public GlobalEntity redis_StringGet          (String paramString);
    public GlobalEntity redis_StringDelete       (String paramString);
    public GlobalEntity redis_StringIncr         (String paramString);
    public GlobalEntity redis_StringDecr         (String paramString);


    public GlobalEntity cookie_create(String paramString, HttpServletResponse response);
    public GlobalEntity cookie_update(String paramString);
    public GlobalEntity cookie_search(String paramString,HttpServletRequest request);
    public GlobalEntity cookie_delete(String paramString,HttpServletRequest request,HttpServletResponse response);

    public GlobalEntity session_create(String paramString, HttpServletRequest request);
    public GlobalEntity session_update(String paramString, HttpServletRequest request);
    public GlobalEntity session_search(String paramString, HttpServletRequest request);
    public GlobalEntity session_delete(String paramString, HttpServletRequest request);

    public GlobalEntity elasticsearch_createIndex       (String paramString);
    public GlobalEntity elasticsearch_deleteIndex       (String paramString);
    public GlobalEntity elasticsearch_insertDocument    (String paramString);
    public GlobalEntity elasticsearch_getDocument       (String paramString);
    public GlobalEntity elasticsearch_deleteDocument    (String paramString);
    public GlobalEntity elasticsearch_updateDocument    (String paramString);
    public GlobalEntity elasticsearch_bulkInsertDocument(String paramString);
    public GlobalEntity elasticsearch_batch             (String paramString);
    public GlobalEntity elasticsearch_match             (String paramString);
    public GlobalEntity elasticsearch_term              (String paramString);

    public GlobalEntity kafka_Product(String paramString);
    public GlobalEntity kafka_Consume(String paramString);

    public GlobalEntity minio_createBucket(String paramString);
    public GlobalEntity minio_searchBucket(String paramString);
    public GlobalEntity minio_deleteBucket(String paramString);
    public GlobalEntity minio_uploadFile  (String paramString);
    public GlobalEntity minio_downloadFile(String paramString);
    public GlobalEntity minio_deleteFile  (String paramString);

    public GlobalEntity fastexcel_downloadExcel(String paramString);
    public GlobalEntity fastexcel_uploadExcel  (MultipartFile paramString);


}
