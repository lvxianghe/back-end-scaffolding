package org.xiaoxingbomei.entity.advertise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推广单元-地域
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitDistrict
{
    private String unitId;
    private String province;
    private String city;
}
