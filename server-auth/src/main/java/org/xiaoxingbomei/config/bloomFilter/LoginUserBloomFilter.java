package org.xiaoxingbomei.config.bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.dao.localhost.AuthMapper;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * 登录用户的布隆过滤器
 * 用户快速判断用户是否注册
 */
@Component
@Slf4j
public class LoginUserBloomFilter
{

    @Autowired
    private AuthMapper authMapper;

    // 预计用户数量
    private static final int EXPECTED_NUMBER_OF_USERS = 1000000;

    // 误判率
    private static final double FALSE_POSITIVE_RATE = 0.01;

    //
    private BloomFilter<String> bloomFilter;

    @PostConstruct
    public void init()
    {
        // 初始化布隆过滤器
        bloomFilter = BloomFilter.create
                (
                        Funnels.stringFunnel(StandardCharsets.UTF_8),
                        EXPECTED_NUMBER_OF_USERS,
                        FALSE_POSITIVE_RATE
                );
        // 从数据库中加载所有用户
        authMapper.getAllUser().forEach(user -> bloomFilter.put(user.getLoginId()));
    }

    // 添加用户到布隆过滤器
    public void addLoginUser(String loginId)
    {
        bloomFilter.put(loginId);
        log.info("添加用户{}到布隆过滤器",loginId);
    }

    // 判断用户是否在布隆过滤器中
    public boolean mightContainLoginUser(String loginId)
    {
        log.info("判断用户{}是否在布隆过滤器中",loginId);
        return bloomFilter.mightContain(loginId);
    }

}
