package com.sharemiracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharemiracle.dto.UserDTO;
import com.sharemiracle.dto.UserPageQueryDTO;
import com.sharemiracle.dto.UserPwdModDTO;
import com.sharemiracle.entity.User;
import com.sharemiracle.result.PageResult;
import com.sharemiracle.result.Result;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserInfoService extends IService<User> {
    public Result<User> userQueryById(Long userId);

    PageResult pageQuery(
            UserPageQueryDTO userPageQueryDTO,
            @RequestHeader("Authorization") String token
    );

    Result<String> modifyInfo(UserDTO userDTO);

    Result<String> modifyUserAuthority(String userId, Long authority, String token);

    Result<String> modifyUserStatus(String userId, Long status, String token);

    Result<String> modifyUserPassword(UserPwdModDTO userPwdModDTO);
}
