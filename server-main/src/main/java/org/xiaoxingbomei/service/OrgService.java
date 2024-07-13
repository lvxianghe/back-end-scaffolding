package org.xiaoxingbomei.service;

import org.springframework.stereotype.Service;
import org.xiaoxingbomei.controller.request.GetOrgInfoDto;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.vo.Org;

public interface OrgService
{

    GlobalEntity getAllOrgInfo();

    GlobalEntity getAllOrgInfoByOrgId(GetOrgInfoDto dto);

}
