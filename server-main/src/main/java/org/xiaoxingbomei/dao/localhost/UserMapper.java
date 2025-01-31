package org.xiaoxingbomei.dao.localhost;

import org.apache.ibatis.annotations.Mapper;
import org.xiaoxingbomei.vo.User;

import java.util.List;

@Mapper
public interface UserMapper
{
    List<User> getAllUserInfo();
}
