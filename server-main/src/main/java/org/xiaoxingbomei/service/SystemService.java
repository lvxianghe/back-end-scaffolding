package org.xiaoxingbomei.service;

public interface SystemService
{
    /**
     * 获取系统时间
     * @return
     */
    String getSystemTime();

    /**
     * 获取系统名称
     * @return
     */
    String getSystemName();

    /**
     * 获取系统版本
     * @return
     */
    String getSystemVersion();

    /**
     * 获取系统描述
     * @return
     */
    String getSystemDescription();

    /**
     * 获取系统状态
     * @return
     */
    String getSystemStatus();

    /**
     * 获取系统状态描述
     * @return
     */
    String getSystemStatusDescription();

    /**
     * 获取系统状态码
     * @return
     */
    String getSystemStatusCode();

    /**
     * 获取系统状态码描述
     * @return
     */
    String getSystemStatusCodeDescription();


}
