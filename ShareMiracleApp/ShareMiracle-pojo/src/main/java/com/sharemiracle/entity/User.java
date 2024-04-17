package com.sharemiracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键 自增
     */
    @TableId(value = "id", type = IdType.AUTO) // 指定主键生成策略为数据库自增
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
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像url
     */
    private String logoUrl;

    /**
     * 用户注册时间
     */
    private LocalDateTime createTime;

    /**
     * 用户性别
     */
    private Integer sex;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户权限（0 管理员 1 普通用户）
     */
    private Integer authority;

    /**
     * 用户状态（1代表可用 0代表禁用）
     */
    private Integer status;

}
