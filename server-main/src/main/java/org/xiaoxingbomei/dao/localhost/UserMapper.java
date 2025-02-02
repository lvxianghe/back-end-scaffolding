package org.xiaoxingbomei.dao.localhost;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiaoxingbomei.vo.User;

import java.util.List;

@Mapper
public interface UserMapper
{
    List<User> getAllUserInfo();

    // 批量插入用户信息
    void createUserInfo(@Param("list") List<User> users);


}
