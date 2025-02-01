package org.xiaoxingbomei.service;


import org.xiaoxingbomei.common.entity.GlobalEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService
{
    GlobalEntity getAllUserInfo();
    void exportUserInfoTemplate(HttpServletResponse response) throws Exception;

    List<String> discoverNewWords(List<String> texts);

    void updateLocalDictionary(List<String> newWords);
}
