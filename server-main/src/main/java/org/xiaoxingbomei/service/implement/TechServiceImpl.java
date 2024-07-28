package org.xiaoxingbomei.service.implement;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.service.TechService;
import org.xiaoxingbomei.utils.Request_Utils;
import org.xiaoxingbomei.vo.User;

import java.util.List;

@Service
@Slf4j
public class TechServiceImpl implements TechService
{

    @Autowired
    @Qualifier("mongoTemplateOfLocal")
    private MongoTemplate mongoTemplateOfLocal;

//    @Autowired
//    @Qualifier("mongoTemplateOfDgs")
//    private MongoTemplate mongoTemplateOfDgs;


    @Override
    public GlobalEntity mongodbOfInsert(String paramString)
    {
        User user = JSONObject.parseObject(paramString, User.class);

//        String name = mongoTemplateOfDgs.getDb().getName();
//        log.info("dgs database name:{}",name);

        mongoTemplateOfLocal.insert(user);

        return GlobalEntity.success("测试 mongodbTemplate insert 成功");
    }

    @Override
    public GlobalEntity mongodbOfMultiInsert(String paramString)
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
    public GlobalEntity mongodbOfSave(String paramString)
    {
        return GlobalEntity.success("测试 mongodbTemplate multi insert 成功");
    }

    @Override
    public GlobalEntity mongodbOfSearch(String paramString)
    {
        return GlobalEntity.success("测试 mongodb search 成功");
    }

    @Override
    public GlobalEntity mongodbOfUpdate(String paramString)
    {
        return GlobalEntity.success("测试 mongodb update 成功");
    }

    @Override
    public GlobalEntity mongodbOfDelete(String paramString)
    {
        return GlobalEntity.success("测试 mongodbTemplate delete 成功");
    }

}
