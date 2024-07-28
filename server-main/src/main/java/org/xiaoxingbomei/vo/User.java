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

    // @Id
    @Schema(description = "名字")
    private String name;

    @Schema(description = "手机号")
    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String phone;

    @Schema(description = "身份证号")
    @Desensitization(type = DesensitizationTypeEnum.ID_CARD)
    private String idCard;

    @Schema(description = "银行卡")
    @Desensitization(type = DesensitizationTypeEnum.BANK_CARD)
    private String bankCard;

    @Schema(description = "地址")
    @Desensitization(type = DesensitizationTypeEnum.CUSTOM_RULE,start = 0,end = 3)
    private String address;

}
