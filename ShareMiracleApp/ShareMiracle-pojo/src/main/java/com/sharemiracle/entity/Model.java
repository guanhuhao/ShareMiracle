package com.sharemiracle.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 模型实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键（自增）
     */
    private Long id;

    /**
     * 模型名
     */
    private String name;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 模型是否公开
     */
    private Boolean isPublic;

    /**
     * 存储模型的url
     */
    private String modelUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最近一次更新时间
     */
    private LocalDateTime updateTime;
}