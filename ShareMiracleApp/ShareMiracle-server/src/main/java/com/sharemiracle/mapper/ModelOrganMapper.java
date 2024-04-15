package com.sharemiracle.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface ModelOrganMapper {
    /*
    * 插入model和ordan对应表
    * */
    void insert(Long modelId, Long organizationId);

}
