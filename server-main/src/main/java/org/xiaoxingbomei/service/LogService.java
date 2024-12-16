package org.xiaoxingbomei.service;

import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.vo.BusinessLogCommon;
import org.xiaoxingbomei.vo.SystemLogCommon;

public interface LogService
{
    public GlobalEntity insertBusinessLogCommon(BusinessLogCommon message);

    public GlobalEntity insertSystemLogCommon(SystemLogCommon message);
}
