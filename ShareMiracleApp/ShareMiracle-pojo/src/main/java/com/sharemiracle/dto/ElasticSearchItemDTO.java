package com.sharemiracle.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ElasticSearchItemDTO implements Serializable {
    //数据类型
    private String datatype;

    //模态类型
    private String modal;

    //检索名称
    private String name;

    //适用任务
    private int size;

    //描述方式
    private String description;

}
