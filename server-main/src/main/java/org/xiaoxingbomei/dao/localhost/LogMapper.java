package org.xiaoxingbomei.dao.localhost;

import org.apache.ibatis.annotations.Mapper;
import org.xiaoxingbomei.vo.BusinessLogCommon;
import org.xiaoxingbomei.vo.SystemLogCommon;

/**
 * 日志操作 mapper
 */
@Mapper
public interface LogMapper
{
    int insertBusinessLogCommon(BusinessLogCommon businessLogCommon);

    int insertSystemLogCommon(SystemLogCommon systemLogCommon);
}
