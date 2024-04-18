package com.sharemiracle.dto;

import lombok.Data;

@Data
public class UserPwdModDTO {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 旧密码
     */
    private String oldPassword;
}
