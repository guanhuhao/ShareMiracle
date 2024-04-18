package com.sharemiracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sharemiracle.dto.UserPageQueryDTO;
import com.sharemiracle.entity.User;
import org.apache.ibatis.annotations.Mapper;

public interface UserMapper extends BaseMapper<User> {

//    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);
}
