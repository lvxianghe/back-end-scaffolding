package org.xiaoxingbomei.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.service.auth.TokenService;

/**
 * Token清理定时任务
 */
@Component
@Slf4j
public class TokenTask
{

    @Autowired
    private TokenService tokenService;

    /**
     * 每小时清理一次过期Token
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanExpiredTokens()
    {
        log.info("开始执行Token清理任务...");
        tokenService.cleanExpiredTokens();
        log.info("Token清理任务执行完成");
    }
}