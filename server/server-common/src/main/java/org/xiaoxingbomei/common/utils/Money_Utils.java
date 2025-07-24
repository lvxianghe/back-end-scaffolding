package org.xiaoxingbomei.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 金额工具类
 */
public class Money_Utils
{


    /**
     * 元转分
     */
    public static Long yuanToCent(BigDecimal number)
    {
        return number.multiply(new BigDecimal(100)).longValue();
    }

    /**
     * 分转元
     */
    public static BigDecimal centToYuan(Long number)
    {
        if(number==null)
        {
            return null;
        }
        return new BigDecimal(number.toString()).divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    }

}
