package com.sharemiracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharemiracle.entity.UserOrgan;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrganMapper extends BaseMapper<UserOrgan> {

    @Select("select authority from share_miracle.r_user_organization,share_miracle.r_model_organization where share_miracle.r_user_organization.user_id = #{userId} and share_miracle.r_model_organization.model_id = #{modelId} and share_miracle.r_model_organization.organization_id = share_miracle.r_user_organization.organization_id and status = 1")
    Integer  selectAuthorityByid(Long userId, Long modelId);
}
