package com.sharemiracle.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 组织实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键（自增）
     */
    private Long id;
    /**
     * 组织名字
     */
    private String name;
    /**
     * 组织类型（如学校等）
     */
    private Integer type;
    /**
     * 组织头像url
     */
    private String logoUrl;
}