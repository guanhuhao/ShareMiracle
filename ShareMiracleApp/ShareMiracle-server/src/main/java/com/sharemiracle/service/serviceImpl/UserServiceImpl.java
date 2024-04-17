package com.sharemiracle.service.serviceImpl;

import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.sharemiracle.constant.JwtClaimsConstant;
import com.sharemiracle.constant.MessageConstant;
import com.sharemiracle.constant.StatusConstant;
import com.sharemiracle.dto.UserDTO;
import com.sharemiracle.dto.UserLoginDTO;
import com.sharemiracle.entity.User;
import com.sharemiracle.mapper.UserMapper;
import com.sharemiracle.properties.JwtProperties;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.UserService;
import com.sharemiracle.utils.JwtUtil;
import com.sharemiracle.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.sharemiracle.constant.RedisConstant.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 校验手机号格式是否正确
        String phone = userDTO.getPhone();
        if (!Validator.isMobile(phone)) {
            // 格式不正确，返回错误信息
            return Result.error("手机号格式不正确");
        }
        // 检查数据库中是否存在该用户名
        // TODO: 用户名唯一性的后端校验 可采用Redis集合优化 UNIQUE KEY判断  先查再增
        String username = userDTO.getUsername();
//        int count = Math.toIntExact(baseMapper.selectCount(new QueryWrapper<User>().eq("username", username)));
//        if (count > 0) {
//            return Result.error("该用户名已被占用");
//        }

        User user = new User();

        // 拷贝对象属性
        BeanUtils.copyProperties(userDTO, user);
        // 将性别转换为整数
        user.setSex(parseGender(userDTO.getSex()));

        // md5加密
        user.setPassword(DigestUtil.md5Hex(userDTO.getPassword()));

        // 设置未从DTO拷贝的其余属性
        user.setCreateTime(LocalDateTime.now()); // 设置当前时间为注册时间
        user.setAuthority(1); // 设置默认权限为普通用户
        user.setStatus(1); // 设置默认状态为可用

        // 使用 Redis 分布式锁在高并发环境下避免对同一个用户名进行注册, 顺便进行用户名唯一性校验
        String lockKey = LOCK_USERNAME + username;
        try {
            Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);
            if (lockAcquired == null || !lockAcquired) {
                return Result.error("该用户名注册流程处理中，请稍后重试");
            }
            boolean isSaveCompleted = save(user);
            if (!isSaveCompleted) {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            log.error("注册用户失败", e);
            return Result.error("服务器错误");
        } finally {
            stringRedisTemplate.delete(lockKey);
        }

        return Result.success("注册成功");
    }

    @Override
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        // 检验用户名和密码是否为空
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Result.error(MessageConstant.ACCOUNT_OR_PASSWORD_IS_BLANK);
        }

        // 校验用户名是否存在，如果不存在直接返回“用户名或密码错误”
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userLoginDTO.getUsername()));
        if (user == null) {
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 验证用户名密码是否正确，如果不正确返回“用户名或密码错误”（注意：数据库中密码采用md5加密存储）
        String inputPasswordMd5 = SecureUtil.md5(password);
        if (!user.getPassword().equals(inputPasswordMd5)) {
            return Result.error(MessageConstant.PASSWORD_ERROR);
        }

        // 账号状态是可用而不是禁用的
        if (Objects.equals(user.getStatus(), StatusConstant.DISABLE)) {
            return Result.error(MessageConstant.ACCOUNT_LOCKED);
        }
        // 用户认证通过后，保存用户信息到userHolder

        // 登录认证token是否合法 去redis存储jwt令牌的存在

        // 用户名和密码均正确，登录成功
        // 进行jwt校验
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId().toString());
        // 创建JWT token
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        // 如果验证成功，token放入redis中 token：user信息 并设置过期时间
        log.info("生成jwt token: {}", token);
        storeTokenInRedis(token, user);

        // 创建UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO(
                user.getId(),
                user.getName(),
                token, // 这里是之前生成的JWT token
                user.getUsername()
        );
        return Result.success(userLoginVO);
    }

    @Override
    public Result<String> logout(@RequestHeader("Authorization") String token) {
//        // 请求头中获取JWT令牌
//        String token = request.getHeader(jwtProperties.getUserTokenName());
//        if (token != null) {
//            stringRedisTemplate.delete(AUTH_TOKEN + token); // 删除令牌
//            log.info("用户登出，令牌：{}", token);
//            return Result.success("已成功退出登录");
//        } else {
//            return Result.success("登出请求无效");
//        }
        // TODO: 登出后处理jwt令牌
        if (token != null) {
            stringRedisTemplate.delete(AUTH_TOKEN + token);
            log.info("用户登出，令牌：{}", token);
            return Result.success("已成功退出登录");
        } else {
            return Result.success("登出请求无效");
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

    // 在UserService中添加一个方法来存储JWT到Redis
    private void storeTokenInRedis(String token, User user) {
        String key = AUTH_TOKEN + token; // 一个常量，例如"auth:token:"
        long ttl = jwtProperties.getUserTtl(); // 从配置中获取JWT的有效期
        // 转换过期时间为秒
        long ttlInSeconds = TimeUnit.MILLISECONDS.toSeconds(ttl);

        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("userId", user.getId().toString());
        userDetails.put("username", user.getUsername());

        // 将用户信息转换为JSON字符串
        String userDetailsJson = new Gson().toJson(userDetails);

        // 设置键的过期时间为配置中的userTtl
        stringRedisTemplate.opsForValue().set(key, userDetailsJson, ttlInSeconds, TimeUnit.SECONDS);
    }

    // 检查令牌是否有效
    private boolean isTokenValid(String token) {
        String key = AUTH_TOKEN + token; // 使用相同的前缀
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
