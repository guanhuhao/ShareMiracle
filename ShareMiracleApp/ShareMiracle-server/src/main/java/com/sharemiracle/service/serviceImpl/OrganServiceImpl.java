package com.sharemiracle.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharemiracle.entity.Organization;
import com.sharemiracle.mapper.OrganMapper;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.OrganService;

public class OrganServiceImpl extends ServiceImpl<OrganMapper, Organization> implements OrganService {


    @Override
    public Result<String> addOrgan(Organization organ) {
        boolean isSuccess = save(organ);
        if(!isSuccess){
            return Result.error("新增组织失败");
        }
        return Result.success("200");
    }

    @Override
    public Result<String> joinOrgan(Long organId) {
        // TODO
        return Result.success("200");
    }

    @Override
    public Result<String> authority(Long organId, Long userId) {
        // TODO
        return Result.success("200");
    }

    @Override
    public Result<String> status(String status, Long userId) {
        // TODO
        return Result.success("200");
    }

    @Override
    public Result<String> deleteOrganUser(String organId, String userId) {
        // TODO
        return Result.success("200");
    }

    @Override
    public Result<String> deleteOrgan(String organId) {
        // TODO
        return Result.success("200");
    }
}
