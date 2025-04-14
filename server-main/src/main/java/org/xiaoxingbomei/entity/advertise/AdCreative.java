package org.xiaoxingbomei.entity.advertise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告主
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdCreative
{
    private String adCreativeId;
    private String adCreativeName;
    private String adCreativeType;
    private String materialType;
    private String height;
    private String width;
    private String auditStatus;
    private String adUrl;
}
