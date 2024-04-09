package com.sharemiracle.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据集实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dataset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键（自增）
     */
    private Long id;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 用户主键(该数据库由谁建立)
     */
    private Long userId;

    /**
     * 数据集是否公开
     */
    private Boolean isPublic;

    /**
     * 存储数据集的url
     */
    private String datasetUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 上次更新时间
     */
    private LocalDateTime updateTime;

}