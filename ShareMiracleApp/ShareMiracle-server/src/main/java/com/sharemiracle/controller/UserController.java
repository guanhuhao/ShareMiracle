package com.sharemiracle.controller;

import com.sharemiracle.constant.MessageConstant;
import com.sharemiracle.dto.UserDTO;
import com.sharemiracle.dto.UserLoginDTO;
import com.sharemiracle.dto.UserPageQueryDTO;
import com.sharemiracle.dto.UserPwdModDTO;
import com.sharemiracle.entity.User;
import com.sharemiracle.properties.JwtProperties;
import com.sharemiracle.result.PageResult;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.UserInfoService;
import com.sharemiracle.service.UserService;
import com.sharemiracle.vo.UserLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 用户注册
     * @param userDTO
     * @return
     */
    @PostMapping
    @ApiOperation("注册用户")
    public Result<String> register(@Valid @RequestBody UserDTO userDTO) {
        log.info("用户注册: {}", userDTO.getUsername());
        return userService.register(userDTO);
    }

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录用户")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO.getUsername());
        try {
            Result<UserLoginVO> resultVO = userService.login(userLoginDTO);
            resultVO.setMsg("登录成功");
            return resultVO;
        } catch (Exception e) {
            return Result.error(MessageConstant.LOGIN_FAILED);
        }
    }

    /**
     * 用户登出
     * @param token
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result<String> userLogout(String token) {
        return userService.logout(token);
    }

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public Result<User> userQueryById(@PathVariable("id") Long userId) {
        return userInfoService.userQueryById(userId);
    }

    /**
     * 分页查询员工
     * @param userPageQueryDTO
     * @param token
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(("用户分页查询"))
    public Result<PageResult> userPageQuery(
            UserPageQueryDTO userPageQueryDTO,
            @RequestHeader("Authorization") String token) {
        log.info("用户分页查询，参数为{}", userPageQueryDTO);
        PageResult pageResult = userInfoService.pageQuery(userPageQueryDTO, token);
        return Result.success(pageResult);
    }

    /**
     * 修改用户信息
     * @param userDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改用户信息")
    public Result<String> userInfoModify(@Valid @RequestBody UserDTO userDTO) {
        log.info("用户修改信息：{}", userDTO.getUsername());
        return userInfoService.modifyInfo(userDTO);
    }

    /**
     * 修改用户权限(仅管理员可操作普通用户)
     * @param authority
     * @return
     */
    @PutMapping("/authority/{status}")
    @ApiOperation("修改用户权限/添加管理员权限")
    public Result<String> userAuthorityModify(
            @PathVariable("status") Long authority, // authority 表示用户的状态，1 为普通用户，0 为管理员
            @RequestParam("id") String userId, // 查询参数，用来指定哪个用户ID的权限需要修改
            @RequestHeader("Authorization") String token // jwt 令牌
    ) {
        log.info("管理员修改用户权限:{}", userId);
        return userInfoService.modifyUserAuthority(userId, authority, token);
    }

    /**
     * 修改用户状态(仅管理员可操作普通用户)
     * @param status
     * @param userId
     * @return
     */
    @PutMapping("/status/{status}")
    @ApiOperation("启用/禁用普通用户")
    public Result<String> userStatusModify(
            @PathVariable("status") Long status, // status 表示用户的状态，1 启用，0 禁用
            @RequestParam("id") String userId, // 查询参数，用来指定哪个用户ID的状态需要修改
            @RequestHeader("Authorization") String token // jwt 令牌
    ) {
        log.info("管理员修改用户状态:{}, {}", userId, status);
        return userInfoService.modifyUserStatus(userId, status, token);
    }

    /**
     * 修改用户密码
     * @param userPwdModDTO
     * @return
     */
    @PutMapping("/editPassword")
    @ApiOperation("修改用户密码")
    public Result<String> userPasswordModify(@RequestBody UserPwdModDTO userPwdModDTO) {
        log.info("用户修改密码, id: {}", userPwdModDTO.getUserId());
        return userInfoService.modifyUserPassword(userPwdModDTO);
    }

    /**
     * 检测邮箱有效性
     * @param email
     * @return
     */
    @GetMapping("/validEmail")
    @ApiOperation("查询当前邮箱是否有效")
    public Result<Boolean> userValidEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }
}
