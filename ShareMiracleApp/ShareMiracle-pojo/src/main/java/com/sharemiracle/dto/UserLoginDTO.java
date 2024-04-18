package com.sharemiracle.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
public class UserLoginDTO implements Serializable {
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名
     */
    private String username;

}
