package com.sharemiracle.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户头像url
     */
    private String logoUrl;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
    /**
     * 用户密码（明文传递 加密存储）
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 性别
     */
    private String sex;
    /**
     * 用户名（凭证 唯一）
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
}
