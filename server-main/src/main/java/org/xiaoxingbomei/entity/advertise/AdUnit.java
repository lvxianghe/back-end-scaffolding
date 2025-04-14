package org.xiaoxingbomei.entity.advertise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推广单元
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnit
{
    private String unitId;
    private String unitStatus;
    private String positionType;

    private String planId;
}
