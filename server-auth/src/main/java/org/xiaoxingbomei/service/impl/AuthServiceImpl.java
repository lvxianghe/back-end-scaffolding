package org.xiaoxingbomei.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mysql.cj.CacheAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoxingbomei.common.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.common.entity.LoginInfo;
import org.xiaoxingbomei.common.entity.request.GlobalRequest;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.common.utils.Date_Utils;
import org.xiaoxingbomei.common.utils.Request_Utils;
import org.xiaoxingbomei.config.bloomFilter.LoginUserBloomFilter;
import org.xiaoxingbomei.dao.localhost.AuthMapper;
import org.xiaoxingbomei.entity.SysPermission;
import org.xiaoxingbomei.entity.SysRole;
import org.xiaoxingbomei.entity.SysUser;
import org.xiaoxingbomei.service.AuthService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.xiaoxingbomei.common.CacheConstant.CACHE_KEY_SEPARATOR;

/**
 *
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private LoginUserBloomFilter loginUserBloomFilter;

    // ========================================================================


    @Override
    public GlobalResponse registerCheck(String paramString)
    {
        // 1、获取前端参数
        String loginId     = Request_Utils.getParam(paramString, "loginId");
        if(StringUtils.isEmpty(loginId))
        {
            return GlobalResponse.error("账号不能为空");
        }

        // 2、先查询布隆过滤器
        if(!loginUserBloomFilter.mightContainLoginUser(loginId))
        {
            log.info("账号{}一定不存在布隆过滤器中",loginId);
            return GlobalResponse.error("账号已被注册");
        }

        // 3、如果布隆过滤器存在，则查询数据库进行二次查询
        if(loginUserBloomFilter.mightContainLoginUser(loginId))
        {
            SysUser sysUser = authMapper.getUserByLoginId(loginId);
            if(sysUser != null)
            {
                return GlobalResponse.error("账号已被注册");
            }
        }

        return GlobalResponse.success("账号未被注册");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse register(String paramString)
    {
        // 1、获取前端参数
        String loginId     = Request_Utils.getParam(paramString, "loginId");    // 登录账号
        String name        = Request_Utils.getParam(paramString, "name");       // 姓名
        String password    = Request_Utils.getParam(paramString, "password");   // 密码
        String gender      = Request_Utils.getParam(paramString, "gender");     // 性别
        String phoneNumber = Request_Utils.getParam(paramString, "phoneNumber");// 电话号码
        String idCard      = Request_Utils.getParam(paramString, "idCard");     // 证件号

        // 2、防重判断
        authService.registerCheck(paramString);

        // 3、组装注册信息
        SysUser newSysUser = new SysUser();
        newSysUser.setLoginId(loginId);
        newSysUser.setName(name);
        newSysUser.setPassword(password);
        newSysUser.setGender(gender);
        newSysUser.setPhoneNumber(phoneNumber);
        newSysUser.setIdCard(idCard);
        newSysUser.setUserType("1");
        newSysUser.setStatus("1");

        String currentTime = Date_Utils.getCurrentTime();
        newSysUser.setCreateTime(currentTime);
        newSysUser.setUpdateTime(currentTime);

        // 3、注册信息落库
        authMapper.insertNewSysUser(newSysUser);

        // 4、布隆过滤器添加账号
        loginUserBloomFilter.addLoginUser(loginId);

        // 4、新用户授予默认角色  todo 这里应该从角色列表去获取类型为默认的角色
        List<String> defaultRoleList = new ArrayList<>();
        defaultRoleList.add("tourist");
        authMapper.insertUserRoles(loginId,defaultRoleList);

        // 5、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("newSysUserInfo",newSysUser);
        return GlobalResponse.success(resultMap,"新用户注册成功");
    }

    @Override
    public GlobalResponse login(String paramString)
    {
        // 1、解析前端参数
        String loginType = Request_Utils.getParam(paramString, "loginType");

        // 2、登录方式路由
        if(StringUtils.equals("1", loginType))  // 密码登录
        {
            GlobalResponse ret = loginByPassword(paramString);
            return ret;
        }

        if(StringUtils.equals("2", loginType))  // 验证码登录
        {
            loginByXxx(paramString);
        }

        if(StringUtils.equals("3",loginType))   // 单点登录
        {
            loginBySso(paramString);
        }

        return null;
    }

    // 登录-密码
    private GlobalResponse loginByPassword(String paramString)
    {
        // 1、接收前端参数
        String userId   = Request_Utils.getParam(paramString, "loginId");
        String password = Request_Utils.getParam(paramString, "password");

        // 此处用代码替代真实情况下数据库比对操作
        if(StringUtils.equals("ysx",userId) & StringUtils.equals("ysx123",password))
        {
            StpUtil.login(userId);
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setLoginId(userId);
            loginInfo.setPassword(password);
            List<String> roleKeyList = authService.getRole(paramString);
            loginInfo.setRoleList(roleKeyList);
            StpUtil.getSession().set(userId,loginInfo);
            return GlobalResponse.success(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMessage(),"登录成功","",null);
        }

        if(StringUtils.equals("lhx",userId) & StringUtils.equals("lhx123",password))
        {
            StpUtil.login(userId);
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setLoginId(userId);
            loginInfo.setPassword(password);
            List<String> roleKeyList = authService.getRole(paramString);
            loginInfo.setRoleList(roleKeyList);
            StpUtil.getSession().set(userId,loginInfo);
            return GlobalResponse.success(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMessage(),"登录成功","",null);
        }

        return GlobalResponse.error("登录失败");
    }

    // 登录-验证码
    private GlobalResponse loginByXxx(String paramString)
    {
        return null;
    }

    // 登录-单点登录
    private GlobalResponse loginBySso(String paramString)
    {
        return null;
    }

    @Override
    public GlobalResponse isLogin(String paramString)
    {
        boolean loginResult = StpUtil.isLogin();
        return GlobalResponse.success(loginResult);
    }

    @Override
    public GlobalResponse checkLogin(String paramString)
    {
        StpUtil.checkLogin();
        return GlobalResponse.success("已登录");
    }

    @Override
    public GlobalResponse logout(String paramString)
    {
        if(StpUtil.isLogin())
        {
            StpUtil.logout();
            return GlobalResponse.success("已登出");
        }
        return GlobalResponse.success("未登录，无需登出");
    }


    @Override
    public GlobalResponse getSaTokenInfo(String paramString)
    {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return GlobalResponse.success(tokenInfo,"获取sa-token信息");
    }

    @Override
    public GlobalResponse getUserId(String paramString)
    {
        Object userId = StpUtil.getLoginId();
        return GlobalResponse.success(userId,"获取登录用户id");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse createRole(String paramString)
    {
        // 1、获取前端参数
        String roleKey     = Request_Utils.getParam(paramString, "roleKey");
        String roleName    = Request_Utils.getParam(paramString, "roleName");
        String description = Request_Utils.getParam(paramString, "description");
        String permissions = Request_Utils.getParam(paramString, "permissions");

        // 2、创建角色
        SysRole sysRole = new SysRole();
        sysRole.setRoleKey(roleKey);
        sysRole.setRoleName(roleName);
        sysRole.setDescription(description);

        authMapper.insertRole(sysRole);

        // 3、关联权限
        if(StringUtils.isNotEmpty(permissions))
        {
            List<String> permissionList = Arrays.asList(permissions.split(",", -1));
            // 去除多余空格(可选)
            permissionList = permissionList.stream().map(String::trim).collect(Collectors.toList());
            log.info("当前角色需要关联的权限为+{}",permissionList);
            authMapper.batchInsertRolePermissions(roleKey,permissionList);
        }

        // 4、清理缓存（可选）

        // 5、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("roleKey", roleKey);
        resultMap.put("roleName",roleName);
        resultMap.put("permissions",permissions);

        return GlobalResponse.success(resultMap,"新增角色成功");
    }

    @Override
    public List<String> getRole(String paramString)
    {
        // 1、获取前端参数
        String userId = Request_Utils.getParam(paramString, "userId");

        List<String> roleKeyList = new ArrayList<>();

        // 2、如果是获取指定用户的角色信息
        if(StringUtils.isNotEmpty(userId))
        {
            List<SysRole> roleList = authMapper.getRolesByUserId(userId);
            roleKeyList = roleList.stream()
                    .filter(role -> role != null) // 过滤 null SysRole
                    .map(SysRole::getRoleKey)
                    .filter(rolekey -> rolekey != null) // 过滤 null roleKey
                    .collect(Collectors.toList());
            return roleKeyList;
        }

        // 3、如果是获取全部角色信息
        List<SysRole> allRoles = authMapper.getAllRoles();
        roleKeyList = allRoles.stream()
                .filter(role -> role != null)       // 过滤 null SysRole
                .map(SysRole::getRoleKey)
                .filter(rolekey -> rolekey != null) // 过滤 null roleKey
                .collect(Collectors.toList());
        return roleKeyList;
    }

    @Override
    public GlobalResponse updateRole(String paramString)
    {
        return null;
    }

    @Override
    public GlobalResponse deleteRole(String paramString)
    {
        return null;
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public GlobalResponse assignUserRoles(String paramString)
    {
        // 1、获取前端参数
        String loginId = Request_Utils.getParam(paramString, "loginId");
        String roles   = Request_Utils.getParam(paramString, "roles");
        if(StringUtils.isEmpty(roles) || StringUtils.isEmpty(loginId))
        {
            return GlobalResponse.error("用户名或者角色信息不能为空");
        }

        // 2、删除用户现有的角色关联
        authMapper.deleteUserRoles(loginId);

        // 3、建立新的角色关联
        List<String> roleList = Arrays.asList(roles.split(",", -1));
        roleList = roleList.stream().map(String::trim).collect(Collectors.toList()); // 去除多余空格
        log.info("为用户{}分配角色{}",loginId,roleList);
        authMapper.insertUserRoles(loginId,roleList);

        // 4、清理缓存（如果有的话）

        // 5、如果用户已经登录，更新会话中的角色信息
        if(StpUtil.isLogin(loginId))
        {
            LoginInfo loginInfo = (LoginInfo)StpUtil.getSessionByLoginId(loginId).get(loginId);
            if(loginInfo!=null)
            {
                // 更新登录信息中的角色列表
                loginInfo.setRoleList(roleList);
                StpUtil.getSession().set(loginId,loginInfo);
                log.info("更新用户{}的会话角色信息{}",loginId,roleList);
            }
        }

        // 6、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("loginId",   loginId);
        resultMap.put("roleList", roleList);
        return GlobalResponse.success(resultMap,"用户角色分配成功");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse createPermission(String paramString)
    {
        // 1、获取前端参数
        String permissionKey    = Request_Utils.getParam(paramString, "permissionKey");
        String permissionName   = Request_Utils.getParam(paramString, "permissionName");
        String parentId         = Request_Utils.getParam(paramString, "parentId");
        String description      = Request_Utils.getParam(paramString, "description");
        String permissionType   = Request_Utils.getParam(paramString, "permissionType");

        if(StringUtils.isEmpty(permissionKey)|| StringUtils.isEmpty(permissionName))
        {
            return GlobalResponse.error("权限名称或标识不能为空");
        }

        // 2、创建权限
        SysPermission sysPermission = new SysPermission();
        sysPermission.setPermissionKey(permissionKey);
        sysPermission.setPermissionName(permissionName);
        sysPermission.setPermissionType(permissionType);
        sysPermission.setDescription(description);
        sysPermission.setStatus("1");

        authMapper.insertPermission(sysPermission);

        // 3、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("permissionKey", permissionKey);
        resultMap.put("permissionName", permissionName);
        resultMap.put("parentId", parentId);
        resultMap.put("description", description);

        return GlobalResponse.success(resultMap,"新增权限成功");
    }

    @Override
    public GlobalResponse getAllPermission(String paramString)
    {
        // 1、查询全部权限
        List<SysPermission> allPermission = authMapper.getAllPermission();

        // 2、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("allPermission",allPermission);

        return GlobalResponse.success(resultMap,"获取全部权限成功");
    }

    @Override
    public GlobalResponse assignRolePermissions(String paramString)
    {
        // 1、获取前端参数
        String roleKey      = Request_Utils.getParam(paramString, "roleKey");
        String permissions  = Request_Utils.getParam(paramString, "permissions");

        List<String> permissionList = null;

        if(StringUtils.isEmpty(roleKey))
        {
            return GlobalResponse.error("角色标识不能为空");
        }

        // 2、删除角色现有权限关联
        authMapper.deleteRoleAllPermissions(roleKey);

        // 3、建立新的权限关联
        if(StringUtils.isNotEmpty(permissions))
        {
            permissionList = Arrays.asList(permissions.split(",", -1));
            permissionList = permissionList.stream().map(String::trim).collect(Collectors.toList());
            log.info("为角色【{}】分配权限【{}】",roleKey,permissionList);
            // 批量插入角色权限关联
            authMapper.batchInsertRolePermissions(roleKey,permissionList);
        }

        // 4、更新缓存或者会话存储（重要）

        // 5、封装结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("roleKey", roleKey);
        resultMap.put("permissions", permissionList);
        return GlobalResponse.success(resultMap);
    }

    @Override
    public GlobalResponse getRolePermissions(String paramString)
    {
        return null;
    }

    @Override
    public GlobalResponse getUserPermissions(String paramString)
    {
        return null;
    }
}
