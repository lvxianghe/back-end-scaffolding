package org.xiaoxingbomei.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoxingbomei.common.utils.PassWord_Utils;
import org.xiaoxingbomei.entity.auth.SysUser;
import org.xiaoxingbomei.Enum.UserStatus;
import org.xiaoxingbomei.repository.auth.UserRepository;
import org.xiaoxingbomei.service.auth.UserService;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassWord_Utils passwordUtils;

    @Override
    @Transactional
    public SysUser createUser(SysUser user)
    {
        log.info("创建用户: {}", user.getUsername());

        // 检查用户名是否存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在: " + user.getUsername());
        }

        // 检查邮箱是否存在
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在: " + user.getEmail());
        }

        // 加密密码
        user.setPassword(passwordUtils.encode(user.getPassword()));

        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        SysUser savedUser = userRepository.save(user);
        log.info("用户创建成功: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    @Transactional
    public SysUser updateUser(SysUser user)
    {
        log.info("更新用户: {}", user.getUserId());

        Optional<SysUser> existingUser = userRepository.findById(user.getUserId());
        if (!existingUser.isPresent()) {
            throw new RuntimeException("用户不存在: " + user.getUserId());
        }

        SysUser updatedUser = userRepository.save(user);
        log.info("用户更新成功: {}", updatedUser.getUsername());
        return updatedUser;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId)
    {
        log.info("删除用户: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("用户删除成功: {}", userId);
    }

    @Override
    public Optional<SysUser> findById(Long userId)
    {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<SysUser> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<SysUser> findAllUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public List<SysUser> findUsersByStatus(UserStatus status)
    {
        return userRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword)
    {
        log.info("修改用户密码: {}", userId);

        Optional<SysUser> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            log.error("用户不存在: {}", userId);
            return false;
        }

        SysUser user = userOpt.get();

        // 验证旧密码
        if (!passwordUtils.matches(oldPassword, user.getPassword())) {
            log.error("旧密码错误: {}", userId);
            return false;
        }

        // 更新密码
        user.setPassword(passwordUtils.encode(newPassword));
        userRepository.save(user);

        log.info("用户密码修改成功: {}", userId);
        return true;
    }

    @Override
    @Transactional
    public boolean resetPassword(Long userId, String newPassword)
    {
        log.info("重置用户密码: {}", userId);

        Optional<SysUser> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            log.error("用户不存在: {}", userId);
            return false;
        }

        SysUser user = userOpt.get();
        user.setPassword(passwordUtils.encode(newPassword));
        userRepository.save(user);

        log.info("用户密码重置成功: {}", userId);
        return true;
    }

    @Override
    public boolean validatePassword(String username, String password)
    {
        Optional<SysUser> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return false;
        }

        return passwordUtils.matches(password, userOpt.get().getPassword());
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Long userId, UserStatus status)
    {
        log.info("更新用户状态: {} -> {}", userId, status);

        Optional<SysUser> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            log.error("用户不存在: {}", userId);
            return false;
        }

        SysUser user = userOpt.get();
        user.setStatus(status);
        userRepository.save(user);

        log.info("用户状态更新成功: {} -> {}", userId, status);
        return true;
    }

    @Override
    public boolean existsByUsername(String username)
    {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<String> getUserRoles(Long userId)
    {
        return userRepository.findUserRoles(userId);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return userRepository.findUserPermissions(userId);
    }

}