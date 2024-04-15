package com.sharemiracle.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户组织关系实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrgan implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键（自增）
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 组织id
     */
    private Long organizationId;
    /**
     * 用户在该组织中的权限(0:创建者 1:管理权限 2:无权限)
     */
    private Integer authority;
    /**
     * 用户在组织中的状态(0:禁用 1:可用)
     */
    private Integer status;
}