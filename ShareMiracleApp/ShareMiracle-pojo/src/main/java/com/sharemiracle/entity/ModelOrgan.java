package com.sharemiracle.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模型组织关系实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelOrgan implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键（自增）
     */
    private Long id;
    /**
     *  模型id
     */
    private Long modelId;
    /**
     * 组织id
     */
    private Long organizationId;
}