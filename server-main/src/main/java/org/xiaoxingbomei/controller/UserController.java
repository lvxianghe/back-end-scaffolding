package org.xiaoxingbomei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.annotation.RequestLimiting;
import org.xiaoxingbomei.annotation.ServiceSwitch;
import org.xiaoxingbomei.constant.Constant;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.vo.User;

/**
 * 用户级别controller
 */
@RestController
public class UserController
{

//    @RequestLimiting
    @ServiceSwitch(switchKey = Constant.ConfigCode.XX_SWITCH)
    @RequestMapping("/testDesensitization")
    public GlobalEntity testDesensitization()
    {


        User user = User.builder().
                idCard("411321199502051515").
                phone("15628409697").
                bankCard("6215594200001119631").
                address("浙江省杭州市上城区").build();
        System.out.println(user);

        return GlobalEntity.success(user.toString(),"","","","");

    }

}
