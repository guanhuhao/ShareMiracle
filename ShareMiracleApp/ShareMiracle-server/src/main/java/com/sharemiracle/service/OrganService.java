package com.sharemiracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharemiracle.entity.Organization;
import com.sharemiracle.result.Result;

public interface OrganService extends IService<Organization> {

    Result<String> addOrgan(Organization organ);

    Result<String> joinOrgan(Long organId);

    Result<String> authority(Long organId, Long userId, Integer auth);

    Result<String> status(Integer status, Long userId, Long organId);

    Result<String> deleteOrganUser(String organId, String userId);

    Result<String> deleteOrgan(String organId);
}
