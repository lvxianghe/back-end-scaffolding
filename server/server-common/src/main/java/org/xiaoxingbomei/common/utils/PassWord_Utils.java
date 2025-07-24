package org.xiaoxingbomei.common.utils;


import cn.hutool.crypto.digest.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 密码工具类
 */
@Slf4j
@Component
public class PassWord_Utils
{

    /**
     * 加密密码
     * 原理：每次调用该方法都会生成一个新的盐，然后用相同的盐和相同的算法再次加密一遍用户输入的密码
     * 用处：保证 即便是同一个密码，加密后的结果也是不一样的
     */
    public String encode(String rawPassword)
    {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 验证密码
     * 原理：先从数据库中获取加密后的密码提取盐，用相同的盐和相同的算法再次加密一遍用户输入的密码，然后对比两次加密后的密码是否一致
     * 用处：不用解密密码即可验证密码的正确性
     */
    public boolean matches(String rawPassword, String encodedPassword)
    {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

}
