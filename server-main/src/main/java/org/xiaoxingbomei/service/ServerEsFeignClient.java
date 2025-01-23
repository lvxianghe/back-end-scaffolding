package org.xiaoxingbomei.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.common.entity.SystemEntity;

import java.util.List;


/**
 * server-es 的 feign客户端
 */
@FeignClient("server-es")
public interface ServerEsFeignClient
{
    /**
     * 系统
     */
    @RequestMapping(value ="/es/system/es_getSystemInfo",method = RequestMethod.POST)
    public GlobalEntity<List<SystemEntity>> getSystemInfo(@RequestBody String paramString);


    /**
     * 用户
     */


    /**
     *
     */

}
