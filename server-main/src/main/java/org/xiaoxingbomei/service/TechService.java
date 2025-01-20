package org.xiaoxingbomei.service;

import org.springframework.web.multipart.MultipartFile;
import org.xiaoxingbomei.common.entity.GlobalEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TechService
{

    GlobalEntity java_multi_createThreadByThread    (String paramString);
    GlobalEntity java_multi_createThreadByRunnable  (String paramString);
    GlobalEntity java_multi_createThreadByCallable  (String paramString);
    GlobalEntity java_multi_createThreadByThreadPool(String paramString);

    GlobalEntity java_multi_threadStateNew         (String paramString);
    GlobalEntity java_multi_threadStateRunnable    (String paramString);
    GlobalEntity java_multi_threadStateBlocked     (String paramString);
    GlobalEntity java_multi_threadStateWaiting     (String paramString);
    GlobalEntity java_multi_threadStateTimedWaiting(String paramString);
    GlobalEntity java_multi_threadStateTerminated  (String paramString);

    GlobalEntity dynamicThreadPool_create(String paramString);
    GlobalEntity dynamicThreadPool_update(String paramString);
    GlobalEntity dynamicThreadPool_get   (String paramString);
    GlobalEntity dynamicThreadPool_delete(String paramString);




    GlobalEntity mongodb_Insert     (String paramString);
    GlobalEntity mongodb_MultiInsert(String paramString);
    GlobalEntity mongodb_Save       (String paramString);
    GlobalEntity mongodb_Search     (String paramString);
    GlobalEntity mongodb_Update     (String paramString);
    GlobalEntity mongodb_Delete     (String paramString);

    GlobalEntity fastJson_ObjectToJsonString(String paramString);
    GlobalEntity fastJson_JsonStringToObject(String paramString);
    GlobalEntity fastJson_PrettyPrint       (String paramString);
    GlobalEntity fastJson_JSONStringToMap   (String paramString);
    GlobalEntity fastJson_JSONStringToList  (String paramString);


    GlobalEntity redis_expire       (String paramString);

    GlobalEntity redis_StringSet          (String paramString);
    GlobalEntity redis_StringSetWithExpire(String paramString);
    GlobalEntity redis_StringGet          (String paramString);
    GlobalEntity redis_StringDelete       (String paramString);
    GlobalEntity redis_StringIncr         (String paramString);
    GlobalEntity redis_StringDecr         (String paramString);

    GlobalEntity redis_hashSave     (String paramString);
    GlobalEntity redis_hashFind     (String paramString);
    GlobalEntity redis_hashFindAll  (String paramString);
    GlobalEntity redis_hashDelete   (String paramString);
    GlobalEntity redis_hashDeleteAll(String paramString);

    GlobalEntity redis_listLeftPush (String paramString);
    GlobalEntity redis_listRightPush(String paramString);
    GlobalEntity redis_listLeftPop  (String paramString);
    GlobalEntity redis_listRightPop (String paramString);
    GlobalEntity redis_listRange    (String paramString);
    GlobalEntity redis_listRemove   (String paramString);
    GlobalEntity redis_listRemoveByIndex   (String paramString);
    GlobalEntity redis_listRemoveByRange   (String paramString);
    GlobalEntity redis_listSize     (String paramString);
    GlobalEntity redis_listSet      (String paramString);

    GlobalEntity redis_setAdd      (String paramString);
    GlobalEntity redis_setAddAll   (String paramString);
    GlobalEntity redis_setGetAll   (String paramString);
    GlobalEntity redis_setGetRandom(String paramString);
    GlobalEntity redis_setIsMember (String paramString);
    GlobalEntity redis_setRemove   (String paramString);
    GlobalEntity redis_setSize     (String paramString);

    GlobalEntity redis_zsetAdd                   (String paramString);
    GlobalEntity redis_zsetGetByRange            (String paramString);
    GlobalEntity redis_zsetGetByRangeReverse     (String paramString);
    GlobalEntity redis_zsetGetByScoreRange       (String paramString);
    GlobalEntity redis_zsetGetByScoreRangeReverse(String paramString);
    GlobalEntity redis_zsetSize                  (String paramString);
    GlobalEntity redis_zsetRemove                (String paramString);
    GlobalEntity redis_zsetRemoveByRange         (String paramString);
    GlobalEntity redis_zsetRemoveByScoreRange    (String paramString);
    GlobalEntity redis_zsetGetScore              (String paramString);
    GlobalEntity redis_zsetIncrementScore        (String paramString);


    GlobalEntity cookie_create(String paramString, HttpServletResponse response);
    GlobalEntity cookie_update(String paramString);
    GlobalEntity cookie_search(String paramString,HttpServletRequest request);
    GlobalEntity cookie_delete(String paramString,HttpServletRequest request,HttpServletResponse response);

    GlobalEntity session_create(String paramString, HttpServletRequest request);
    GlobalEntity session_update(String paramString, HttpServletRequest request);
    GlobalEntity session_search(String paramString, HttpServletRequest request);
    GlobalEntity session_delete(String paramString, HttpServletRequest request);

    GlobalEntity elasticsearch_createIndex(String paramString);
    GlobalEntity elasticsearch_deleteIndex(String paramString);
    GlobalEntity elasticsearch_existsIndex(String paramString);
    GlobalEntity elasticsearch_getIndex   (String paramString);

    GlobalEntity elasticsearch_indexDocument(String paramString);
    GlobalEntity elasticsearch_getDocument(String paramString);
    GlobalEntity elasticsearch_existsDocument(String paramString);
    GlobalEntity elasticsearch_updateDocument(String paramString);
    GlobalEntity elasticsearch_deleteDocument(String paramString);
    GlobalEntity elasticsearch_bulkInsert(String paramString);


    GlobalEntity kafka_Product(String paramString);
    GlobalEntity kafka_Consume(String paramString);

    GlobalEntity minio_createBucket(String paramString);
    GlobalEntity minio_searchBucket(String paramString);
    GlobalEntity minio_deleteBucket(String paramString);
    GlobalEntity minio_uploadFile  (String paramString);
    GlobalEntity minio_downloadFile(String paramString);
    GlobalEntity minio_deleteFile  (String paramString);

    GlobalEntity fastexcel_downloadExcel(String paramString);
    GlobalEntity fastexcel_uploadExcel  (MultipartFile paramString);


}
