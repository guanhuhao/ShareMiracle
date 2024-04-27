package com.sharemiracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharemiracle.dto.UserDTO;
import com.sharemiracle.dto.UserLoginDTO;
import com.sharemiracle.entity.User;
import com.sharemiracle.result.Result;
// import com.sharemiracle.vo.UserInfoVO;
import com.sharemiracle.vo.UserLoginVO;

public interface UserService extends IService<User> {
    /**
     * 注册/登录/登出用户业务方法
     *
     * @param userDTO
     * @return
     */
    Result<String> register(UserDTO userDTO);

    Result<UserLoginVO> login(UserLoginDTO userLoginDTO);

    Result<String> userInfo(String token);

    Result<String> logout(String token);

    Result<Boolean> checkEmail(String email);

}
