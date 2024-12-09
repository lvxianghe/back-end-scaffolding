package org.xiaoxingbomei.service;

import org.xiaoxingbomei.entity.GlobalEntity;

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

    public GlobalEntity minio_createBucket(String paramString);
    public GlobalEntity minio_searchBucket(String paramString);
    public GlobalEntity minio_deleteBucket(String paramString);
    public GlobalEntity minio_uploadFile  (String paramString);
    public GlobalEntity minio_downloadFile(String paramString);
    public GlobalEntity minio_deleteFile  (String paramString);


}
