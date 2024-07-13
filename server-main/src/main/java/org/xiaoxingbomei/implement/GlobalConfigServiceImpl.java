package org.xiaoxingbomei.implement;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ctrip.framework.apollo.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.service.GlobalConfigService;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GlobalConfigServiceImpl implements GlobalConfigService
{

    @Autowired
    private Config apolloConfig;

    @Override
    public GlobalEntity getApolloConfig()
    {
        Map<String, Object> configMap = new HashMap<>();

        // 获取一些常见和重要的配置信息
        String appId = apolloConfig.getProperty("apollo.app.id", "defaultAppId");
        log.info("appId:{}", appId);
        configMap.put("appId", appId);

        String clusterName = apolloConfig.getProperty("apollo.cluster", "defaultCluster");
        log.info("clusterName:{}", clusterName);
        configMap.put("clusterName", clusterName);

        String namespace = apolloConfig.getProperty("apollo.namespace", "application");
        log.info("namespace:{}", namespace);
        configMap.put("namespace", namespace);


        return GlobalEntity.success(configMap, GlobalCodeEnum.SUCCESS.getCode(),GlobalCodeEnum.SUCCESS.getMessage(),"获取Apollo配置","获取Apollo配置");
    }

    @Override
    public GlobalEntity getApolloValueByKey(String paramString)
    {

        JSONObject jsonObject = JSONUtil.parseObj(paramString);
        String apolloKey = jsonObject.getStr("apolloKey");

        Map<String, Object> configMap = new HashMap<>();

        // Split keys based on comma separator
        String[] keys = apolloKey.split(",");

        // 获取应用的配置项,并对应封装
        for (String key : keys)
        {
            String trimmedKey = key.trim(); // Trim whitespace
            String value = apolloConfig.getProperty(trimmedKey, null); // Null as default value
            configMap.put(trimmedKey, value);
        }

        // 获取应用的配置项

        return GlobalEntity.success(configMap, GlobalCodeEnum.SUCCESS.getCode(),GlobalCodeEnum.SUCCESS.getMessage(),"获取Apollo配置","获取Apollo配置");
    }

    @Override
    public GlobalEntity getNaocsConfig()
    {
        return null;
    }

    @Override
    public GlobalEntity getOpenFeignConfig()
    {
        return null;
    }

    @Override
    public GlobalEntity getReactiveFeignConfig()
    {
        return null;
    }

    @Override
    public GlobalEntity getLog4j2Config()
    {
        return null;
    }

    @Override
    public GlobalEntity getMybatisConfig()
    {
        return null;
    }

    @Override
    public GlobalEntity getRedisConfig()
    {
        return null;
    }

    @Override
    public GlobalEntity getElasticSearchConfig()
    {
        return null;
    }

    @Override
    public GlobalEntity getSaTokenConfig() {
        return null;
    }
}
