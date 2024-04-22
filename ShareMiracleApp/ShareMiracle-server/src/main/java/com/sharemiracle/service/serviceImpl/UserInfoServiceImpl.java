package com.sharemiracle.service.serviceImpl;

import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharemiracle.constant.MessageConstant;
import com.sharemiracle.dto.UserDTO;
import com.sharemiracle.dto.UserPageQueryDTO;
import com.sharemiracle.dto.UserPwdModDTO;
import com.sharemiracle.entity.User;
import com.sharemiracle.mapper.UserMapper;
import com.sharemiracle.properties.JwtProperties;
import com.sharemiracle.result.PageResult;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.UserInfoService;
import com.sharemiracle.service.UserService;
import com.sharemiracle.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sharemiracle.constant.RedisConstant.LOCK_USERNAME;

@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserMapper, User> implements UserInfoService {
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JwtProperties jwtProperties;

    @Override
    public Result<User> userQueryById(Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success(user);
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO, String token) {
        // 获取当前用户权限，测试阶段先定为0
        // int currentUserAuthority = 0;
        // 先获取token中的用户信息, 再根据权限查authority
        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Long currentUserId = Long.valueOf(claims.get("userId").toString());
        User currentUser = userMapper.selectById(currentUserId);
        int currentUserAuthority = currentUser.getAuthority();

        if (currentUserAuthority != 0) {
            // 如果用户权限不为0，不是管理员权限，那么不能查询其他用户的信息，因为用户只能查询到权限比自己低的用户的信息
//            log.info("用户id{}（权限级别：{}）尝试访问受限数据。", currentUser.getId(), currentUserAuthority);
            return new PageResult(0, Collections.emptyList()); // 只有管理员能查询所有用户
        }

        // 创建Page对象，传入当前页和每页显示的条数
        Page<User> page = new Page<>(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (userPageQueryDTO.getName() != null && !userPageQueryDTO.getName().isEmpty()) {
            // 如果查询姓名不为空，则模糊查询姓名且权限低于自己的用户信息
            queryWrapper.like("name", userPageQueryDTO.getName());
        }
        // 查到权限低于自己的用户，权限0 > 权限1
        queryWrapper.gt("authority", currentUserAuthority);

        // 使用Mapper的selectPage方法进行分页查询
        Page<User> resultPage = this.page(page, queryWrapper);

        long total = resultPage.getTotal();
        List<User> records = resultPage.getRecords();

        return new PageResult(total, records);
    }

    @Override
    public Result<String> modifyInfo(UserDTO userDTO) {
        // 用户不能更改为空
        if (userDTO.getId() == null) {
            return Result.error(MessageConstant.ACCOUNT_CANNOT_BE_BLANK);
        }
        // 当前用户不存在
        User existingUser = userMapper.selectById(userDTO.getId());
        if (existingUser == null) {
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 校验手机号格式是否正确
        if (!Validator.isMobile(userDTO.getPhone())) {
            return Result.error("手机号格式不正确");
        }
        // 使用 Redis 分布式锁来避免并发更新
        String lockKey = LOCK_USERNAME + userDTO.getUsername();
        Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);
        if (lockAcquired == null || !lockAcquired) {
            return Result.error("用户信息更新处理中，请稍后重试");
        }
        try {
            // 更新用户信息
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setPassword(DigestUtil.md5Hex(userDTO.getPassword()));
            // 将性别转换为整数
            user.setSex(parseGender(userDTO.getSex()));
            int affectedRows = userMapper.updateById(user);
            if (affectedRows > 0) {
                return Result.success(MessageConstant.USER_INFO_UPDATED_OK);
            } else {
                return Result.error(MessageConstant.USER_INFO_UPDATED_ERROR);
            }
        } catch (Exception e) {
            log.info(MessageConstant.USER_INFO_UPDATED_OK, e);
            return Result.error(MessageConstant.SERVER_ERROR);
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
    }

    @Override
    public Result<String> modifyUserAuthority(String userId, Long authority, String token) {
        // 从jwt中获取当前用户id -> 通过当前用户id查询相关权限
        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Long currentUserId = Long.valueOf(claims.get("userId").toString());
        User currentUser = userMapper.selectById(currentUserId);
        int currentUserAuthority = currentUser.getAuthority();

        // 仅管理员可更改普通用户的权限
        if (currentUserAuthority != 0) {
            log.info("当前用户:{} 权限不足", currentUserId);
            return Result.error(MessageConstant.AUTHORITY_INSUFFICIENT);
        }
        // 获取目标用户当前权限
        User targetUser = userMapper.selectById(Long.valueOf(userId));
        if (targetUser == null) {
            log.error("目标用户不存在: 用户ID {}", userId);
            return Result.error(MessageConstant.USER_NOT_EXISTS);
        }

        // 确保不更改管理员权限
        if (targetUser.getAuthority() == 0) {
            log.info("尝试修改管理员权限被拒绝: 用户ID {}", userId);
            return Result.error(MessageConstant.AUTHORITY_INSUFFICIENT);
        }

        // 更改普通用户的权限
        targetUser.setAuthority(Math.toIntExact(authority));
        int result = userMapper.updateById(targetUser);

        // 检查更新操作是否成功
        if (result == 1) {
            log.info("用户权限更新成功: 用户ID {}", userId);
            return Result.success("用户权限更新成功");
        } else {
            log.error("用户权限更新失败: 用户ID {}", userId);
            return Result.error(MessageConstant.AUTHORITY_UPDATED_ERROR);
        }
    }

    @Override
    public Result<String> modifyUserStatus(String userId, Long status, String token) {
        // 从jwt中获取当前用户id -> 通过当前用户id查询相关权限
        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Long currentUserId = Long.valueOf(claims.get("userId").toString());
        User currentUser = userMapper.selectById(currentUserId);
        int currentUserAuthority = currentUser.getAuthority();

        // 仅管理员可对普通用户进行封禁/解封操作
        if (currentUserAuthority != 0) {
            log.info("当前用户:{} 权限不足", currentUserId);
            return Result.error(MessageConstant.AUTHORITY_INSUFFICIENT);
        }

        // 不能更改管理员的状态
        User targetUser = new User();
        targetUser = userMapper.selectById(Long.valueOf(userId));
        if (targetUser == null) {
            log.error("目标用户不存在: 用户ID {}", userId);
            return Result.error(MessageConstant.USER_NOT_EXISTS);
        }
        if (targetUser.getAuthority() == 0) {
            log.info("尝试修改管理员权限被拒绝: 用户ID {}", userId);
            return Result.error(MessageConstant.AUTHORITY_INSUFFICIENT);
        }

        // 更改普通用户的账户状态
        targetUser.setStatus(Math.toIntExact(status));
        int result = userMapper.updateById(targetUser);

        // 检查更新操作是否成功
        if (result == 1) {
            log.info("用户状态更新成功: 用户ID {}", userId);
            return Result.success("用户状态更新成功");
        } else {
            log.error("用户状态更新失败: 用户ID {}", userId);
            return Result.error(MessageConstant.USER_STATUS_UPDATED_ERROR);
        }
    }

    @Override
    public Result<String> modifyUserPassword(UserPwdModDTO userPwdModDTO) {
        if (userPwdModDTO == null || userPwdModDTO.getUserId() == null) {
            return Result.error(MessageConstant.INFO_INCOMPLETE);
        }

        // 获取用户实体
        User user = userMapper.selectById(userPwdModDTO.getUserId());
        if (user == null) {
            return Result.error(MessageConstant.USER_NOT_EXISTS);
        }

        // 验证旧密码
        if (!DigestUtil.md5Hex(userPwdModDTO.getOldPassword()).equals(user.getPassword())) {
            return Result.error(MessageConstant.PASSWORD_ERROR);
        }

        // 获取加密后的新密码
        String newPassword = DigestUtil.md5Hex(userPwdModDTO.getNewPassword());

        // 更新用户密码
        user.setPassword(newPassword);
        int updatedRows = userMapper.updateById(user);
        if (updatedRows > 0) {
            return Result.success(MessageConstant.PASSWORD_EDIT_SUCCESS);
        } else {
            return Result.error(MessageConstant.PASSWORD_EDIT_FAILED);
        }
    }

    private Integer parseGender(String genderStr) {
        if ("男".equalsIgnoreCase(genderStr)) {
            return 1;
        } else if ("女".equalsIgnoreCase(genderStr)) {
            return 0;
        }
        return null; // 默认值
    }
}
