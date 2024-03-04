//package org.xiaoxingbomei.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.xiaoxingbomei.annotation.ControllerLog;
//import org.xiaoxingbomei.constant.ApiConstant;
//
//@RestController
//public class LoginController
//{
//
//
//    @Autowired
//    public AuthService authService;
//
//
//    // doLogin
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.doLogin)
//    public GlobalEntity doLogin(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//
//
//        ret = authService.doLogin(paramString);
//
//
//        return ret;
//    }
//
//
//    // isLogin
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.isLogin)
//    public GlobalEntity isLogin(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//
//
//        ret = authService.isLogin(paramString);
//
//
//        return ret;
//    }
//
//
//    // checkLogin
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.checkLogin)
//    public GlobalEntity checkLogin(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//
//
//        ret = authService.checkLogin(paramString);
//
//
//        return ret;
//    }
//
//
//    // tokenInfo
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.tokenInfo)
//    public GlobalEntity tokenInfo(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//
//
//        ret = authService.tokenInfo(paramString);
//
//
//        return ret;
//    }
//    // logOut
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.logOut)
//    public GlobalEntity logOut(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//
//
//        ret = authService.logOut(paramString);
//
//
//        return ret;
//    }
//
//
//    // getPermissionList
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.getPermissionList)
//    public GlobalEntity getPermissionList(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.getPermissionList(dto);// 核心调用
//
//
//        return ret;
//    }
//
//
//    // hasPermissionList
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.hasPermissionList)
//    public GlobalEntity hasPermissionList(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.hasPermissionList(dto);// 核心调用
//
//
//        return ret;
//    }
//    // checkPermissionList
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.checkPermissionList)
//    public GlobalEntity checkPermissionList(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.checkPermissionList(dto);// 核心调用
//
//        return ret;
//    }
//
//    // checkPermissionAnd
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.checkPermissionAnd)
//    public GlobalEntity checkPermissionAnd(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.checkPermissionAnd(dto);// 核心调用
//
//        return ret;
//    }
//
//    // CheckPermissionOr
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.CheckPermissionOr)
//    public GlobalEntity CheckPermissionOr(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.CheckPermissionOr(dto);// 核心调用
//
//        return ret;
//    }
//    // getRoleList
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.getRoleList)
//    public GlobalEntity getRoleList(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.getRoleList(dto);// 核心调用
//
//        return ret;
//    }
//
//    // hasRole
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.hasRole)
//    public GlobalEntity hasRole(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.hasRole(dto);// 核心调用
//
//        return ret;
//    }
//
//    // checkRole
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.checkRole)
//    public GlobalEntity checkRole(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.checkRole(dto);// 核心调用
//
//        return ret;
//    }
//    // checkRoleAnd
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.checkRoleAnd)
//    public GlobalEntity checkRoleAnd(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.checkRoleAnd(dto);// 核心调用
//
//        return ret;
//    }
//
//    // checkRoleOr
//    @ControllerLog
//    @RequestMapping(value = ApiConstant.Auth.checkRoleOr)
//    public GlobalEntity checkRoleOr(@RequestBody String paramString)
//    {
//        GlobalEntity ret = null;
//        // 校验参数
//        SystemAuthDto.checkCommonParams(paramString);
//        // 初始化
//        SystemAuthDto dto = SystemAuthDto.initSystemAuthDto(paramString);
//        if (null == dto)
//        {
//            return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),GlobalCodeEnum.ERROR.getMessage(),"初始化参数失败","",null);
//        }
//        ret = authService.checkRoleOr(dto);// 核心调用
//
//        return ret;
//    }
//
//
//
//
//}