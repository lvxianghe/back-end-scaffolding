package org.xiaoxingbomei.service;


import org.xiaoxingbomei.common.entity.GlobalEntity;

import javax.servlet.http.HttpServletResponse;

public interface UserService
{
    GlobalEntity getAllUserInfo();
    void exportUserInfoTemplate(HttpServletResponse response) throws Exception;
}
