package org.xiaoxingbomei.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.xiaoxingbomei.Enum.DesensitizationTypeEnum;
import org.xiaoxingbomei.annotation.Desensitization;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "银行卡")
    private String bankCard;

    @Schema(description = "地址")
//    @Desensitization(type = DesensitizationTypeEnum.CUSTOM_RULE,start = 0,end = 3)
    private String address;


}
