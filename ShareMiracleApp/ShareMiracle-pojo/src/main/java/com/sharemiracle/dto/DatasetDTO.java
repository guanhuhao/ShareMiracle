package com.sharemiracle.dto;

import com.sharemiracle.entity.Organization;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class DatasetDTO implements Serializable {

    //模型数据id
    private Long id;

    //模型名字
    private String name;

    //模型是否公开（1公开 0私有）
    private Integer isPublic;

    //模型存储url
    private String datasetUrl;

    // 组织列表（标明哪些组织有权限使用该数据，该组织需创建者在内）
    private List<Organization> shareOrganization = new ArrayList<Organization>();

}
