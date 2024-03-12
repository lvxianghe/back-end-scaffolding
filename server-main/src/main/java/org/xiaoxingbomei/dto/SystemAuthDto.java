package org.xiaoxingbomei.dto;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.exception.BusinessException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SystemAuthDto
{
    private String userId;      //
    private String userName;    //
    private String orgId;       //
    private String orgName;     //
    private String supOrgId;    //
    private String supOrgName;  //
    private String roleId;      //
    private String auth;        //
    private String token;       //


    // 初始化
    public static SystemAuthDto initSystemAuthDto(String paramString)
    {
        SystemAuthDto dto = null;
        if(!StringUtils.isEmpty(paramString))
        {
            dto = JSON.parseObject(paramString,SystemAuthDto.class);
        }
        return dto;
    }


    // 通用检查入参
    public static void checkCommonParams(String paramString)
    {
        if(org.apache.commons.lang3.StringUtils.isEmpty(paramString))
        {
            throw new BusinessException(GlobalCodeEnum.ERROR,"缺少必要参数");
        }
    }


}