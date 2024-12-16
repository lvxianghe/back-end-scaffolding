package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoxingbomei.annotation.ServiceSwitch;
import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.constant.Constant;
import org.xiaoxingbomei.vo.User;

/**
 * 用户级别controller
 */
@RestController
@Tag(name="用户controller",description = "用户controller")
public class UserController
{

    @ServiceSwitch(switchKey = Constant.SwitchConfigCode.XX_SWITCH)
    @RequestMapping("/testDesensitization")
    public GlobalEntity testDesensitization(@RequestBody String paramString)
    {
        User user = User.builder().
                idCard("411321199502051515").
                phone("15628409697").
                bankCard("6215594200001119631").
                address("浙江省杭州市上城区").build();

        return GlobalEntity.success(user.toString(),"","","","");

    }

}
