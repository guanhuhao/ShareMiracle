package com.sharemiracle.dto;

import com.sharemiracle.entity.Organization;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class SearchDTO implements Serializable {

    //数据类型
    private List<String> datatype;

    //模态类型
    private List<String> modal;

    //适用任务
    private List<String> task;

    //其他Tag
    private List<String> tag;

    //描述方式
    private String queryStr;

}
