package org.xiaoxingbomei.entity.advertise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 广告主 &  推广单元 关联
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdCreativeUnit
{
    private String adCreativeId;
    private String adUnitId;
}
