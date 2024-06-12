package org.xiaoxingbomei.vo;

import lombok.Builder;
import lombok.Data;
import org.xiaoxingbomei.Enum.DesensitizationTypeEnum;
import org.xiaoxingbomei.annotation.Desensitization;

@Data
@Builder
public class User
{

    //手机号
    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String phone;

    //身份证号
    @Desensitization(type = DesensitizationTypeEnum.ID_CARD)
    private String idCard;

    //银行卡
    @Desensitization(type = DesensitizationTypeEnum.BANK_CARD)
    private String bankCard;

    // 地址
    @Desensitization(type = DesensitizationTypeEnum.CUSTOM_RULE,start = 0,end = 3)
    private String address;

}
