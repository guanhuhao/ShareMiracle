package com.sharemiracle.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据集组织关系实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetOrgan implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键（自增）
     */
    Long id;
    /**
     * 数据集id
     */
    Long datasetId;
    /**
     * 组织id
     */
    Long organizationId;

}
