package org.xiaoxingbomei.entity.advertise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推广计划
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlan
{
    private String planId;
    private String userId;
    private String planName; //
    private String planStatus;
    private String startDate;
    private String endDate;
}
