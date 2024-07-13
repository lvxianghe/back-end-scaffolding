package org.xiaoxingbomei.implement;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.dto.SystemAuthDto;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.service.AuthService;
import org.xiaoxingbomei.utils.RequestParam_Utils;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService
{

    /**
     * doLogin
     */
    @Override
    public GlobalEntity doLogin(String paramString)
    {
        String userId = RequestParam_Utils.getParam(paramString, "userId");
        String auth = RequestParam_Utils.getParam(paramString, "auth");


        // 此处用代码替代真实情况下数据库比对操作
        if(StringUtils.equals("ysx",userId) & StringUtils.equals("ysx123",auth))
        {
            StpUtil.login(10001);
            return GlobalEntity.success(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMessage(),"登录成功","",null);
        }

        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), GlobalCodeEnum.ERROR.getMessage(), "登录失败","",null);
    }

    /**
     * isLogin
     */
    @Override
    public GlobalEntity isLogin(String paramString)
    {
        boolean login = StpUtil.isLogin();
        return GlobalEntity.success(GlobalCodeEnum.SUCCESS.getCode(),GlobalCodeEnum.SUCCESS.getMessage(),"","","是否登录:"+login);
    }

    /**
     * checkLogin
     */
    @Override
    public GlobalEntity checkLogin(String paramString)
    {
        StpUtil.checkLogin();
        return GlobalEntity.success(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMessage(), "","","checkLogin down");
    }
    /**
     * tokenInfo
     */
    @Override
    public GlobalEntity tokenInfo(String paramString)
    {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        SaResult tokenInfoResult = SaResult.data(tokenInfo);
        return GlobalEntity.success(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMessage(), "","",tokenInfoResult.getMsg());
    }


    /**
     * logOut
     */
    @Override
    public GlobalEntity logOut(String paramString)
    {
        StpUtil.logout();
        return GlobalEntity.success(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMessage(), "","",SaResult.ok().getMsg());
    }

    /**
     *
     */
    @Override
    public GlobalEntity getPermissionList(SystemAuthDto dto)
    {
        List<String> permissionList = StpUtil.getPermissionList();
        return GlobalEntity.success(GlobalCodeEnum.SUCCESS.getCode(),GlobalCodeEnum.SUCCESS.getMessage(), "获取权限list成功","",permissionList.toString());
    }
    @Override
    public GlobalEntity hasPermissionList(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity checkPermissionList(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity checkPermissionAnd(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity CheckPermissionOr(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity getRoleList(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity hasRole(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity checkRole(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity checkRoleAnd(SystemAuthDto dto) {
        return null;
    }

    @Override
    public GlobalEntity checkRoleOr(SystemAuthDto dto) {
        return null;
    }


}
