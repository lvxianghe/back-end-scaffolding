package org.xiaoxingbomei.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Schema(name = "BusinessLogCommon",description = "对系统操作日志进行落库用的实体")
public class BusinessLogCommon implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private String id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "机构id")
    private String orgId;

    @Schema(description = "机构名")
    private String orgName;

    @Schema(description = "渠道id")
    private String channelId;

    @Schema(description = "渠道名")
    private String channelName;

    @Schema(description = "设备id")
    private String deviceId;

    @Schema(description = "设备名")
    private String deviceName;

    @Schema(description = "页面id")
    private String pageId;
    @Schema(description = "页面名")
    private String pageName;

    @Schema(description = "模块id")
    private String moduleId;
    @Schema(description = "模块名")
    private String moduleName;

    @Schema(description = "操作类型id")
    private String operateTypeId;

    @Schema(description = "操作类型名")
    private String operateTypeName;

    @Schema(description = "操作时间")
    private String operateTime;

    @Schema(description = "备注")
    private String content;


    // 初始化
    public static BusinessLogCommon initBusinessLogCommon(BusinessLogCommon businessLogCommon)
    {
        businessLogCommon.setId(UUID.randomUUID().toString());
        return businessLogCommon;
    }


}
