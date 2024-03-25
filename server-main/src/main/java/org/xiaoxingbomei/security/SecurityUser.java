package org.xiaoxingbomei.security;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 安全用户请求头
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUser {

    /**
     * 用户信息请求头
     */
    public static final String HEADER_SECURITY_USER = "x-gateway-security-user";

    /**
     * 用户id
     */
    private Long id;

    /**
     * 登陆账号
     */
    private String loginAcct;

    /**
     * 用户姓名
     */
    private String userNm;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String moblNum;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户编号
     */
    private String tenantCd;

    /**
     * 角色代码列表
     */
    private List<String> roleCodes;

    /**
     * 岗位代码列表
     */
    private List<String> positionCodes;

    /**
     * 机构信息
     */
    //private LoginOrg org;
}