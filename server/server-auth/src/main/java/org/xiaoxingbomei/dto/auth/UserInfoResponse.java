package org.xiaoxingbomei.dto.auth;

import lombok.Data;
import org.xiaoxingbomei.Enum.UserStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息响应DTO
 */
@Data
public class UserInfoResponse
{

    private Long          userId;
    private String        username;
    private String        nickname;
    private String        email;
    private String        phone;
    private String        avatar;
    private UserStatus    status;
    private LocalDateTime createTime;
    private List<String>  roles;
    private List<String>  permissions;

}