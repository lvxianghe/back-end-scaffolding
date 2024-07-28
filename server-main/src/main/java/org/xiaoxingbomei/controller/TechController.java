package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.service.TechService;

@Tag(name = "技术controller",description = "用于学习测试技术的controller")
@RestController
public class TechController
{

    @Autowired
    private TechService techService;

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-insert")
    @RequestMapping(value = ApiConstant.Tech.mongodbOfInsert,method = RequestMethod.POST)
    public GlobalEntity mongodbOfInsert(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.mongodbOfInsert(paramString);
        return ret;
    }
    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-insert")
    @RequestMapping(value = ApiConstant.Tech.mongodbOfMultiInsert,method = RequestMethod.POST)
    public GlobalEntity mongodbOfMultiInsert(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.mongodbOfMultiInsert(paramString);
        return ret;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-save")
    @RequestMapping(value = ApiConstant.Tech.mongodbOfSave,method = RequestMethod.POST)
    public GlobalEntity mongodbOfSave(@RequestBody String paramString)
    {
        GlobalEntity ret = techService.mongodbOfSave(paramString);
        return ret;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-查询")
    @RequestMapping(value = ApiConstant.Tech.mongodbOfSearch,method = RequestMethod.POST)
    public GlobalEntity getTechLearningInfo(@RequestBody String paramString)
    {
        return null;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-修改")
    @RequestMapping(value = ApiConstant.Tech.mongodbOfUpdate,method = RequestMethod.POST)
    public GlobalEntity mongodbOfUpdate(@RequestBody String paramString)
    {
        return null;
    }

    @Operation(summary = "mongodb学习接口",description = "mongodb-测试-删除")
    @RequestMapping(value = ApiConstant.Tech.mongodbOfDelete,method = RequestMethod.POST)
    public GlobalEntity mongodbOfDelete(@RequestBody String paramString)
    {
        return null;
    }



}
