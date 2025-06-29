package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.SystemEntity;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;

/**
 * 系统管理控制器
 */
@RestController
@RequestMapping("/api/system")
@Tag(name = "系统管理", description = "系统信息管理相关接口")
public class SystemController {

    @GetMapping("/info")
    @Operation(summary = "获取系统信息", description = "获取当前系统的基本信息")
    public GlobalResponse<SystemEntity> getSystemInfo() {
        SystemEntity system = new SystemEntity();
        system.setSystemNumber("SYS001");
        system.setSystemChineseName("后端脚手架系统");
        system.setSystemEnglishName("Backend Scaffolding System");
        system.setSystemVersion("1.0.0");
        system.setSystemDescription("基于SpringBoot + SpringCloud的后端脚手架项目");
        system.setSystemStatus("1");
        
        return GlobalResponse.success(system);
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查系统健康状态")
    public GlobalResponse<String> health() {
        return GlobalResponse.success("系统运行正常");
    }

    @PostMapping("/update")
    @Operation(summary = "更新系统信息", description = "更新系统的基本信息")
    public GlobalResponse<String> updateSystemInfo(
            @Parameter(description = "系统信息", required = true)
            @RequestBody SystemEntity systemEntity) {
        // 这里可以添加实际的更新逻辑
        return GlobalResponse.success("系统信息更新成功");
    }
}
