package com.sharemiracle.dto;

import lombok.Data;

@Data
public class UserDTO {

    /**
     * 主键 自增
     */
    private Long id;

    /**
     * 用户名字（真实姓名）
     */
    private String name;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 用户头像url
     */
    private String logoUrl;
}
