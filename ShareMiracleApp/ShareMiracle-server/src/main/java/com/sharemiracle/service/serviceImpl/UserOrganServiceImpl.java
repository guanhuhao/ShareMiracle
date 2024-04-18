package com.sharemiracle.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharemiracle.entity.UserOrgan;
import com.sharemiracle.mapper.UserOrganMapper;
import com.sharemiracle.service.UserOrganService;
import org.springframework.stereotype.Service;

@Service
public class UserOrganServiceImpl extends ServiceImpl<UserOrganMapper, UserOrgan> implements UserOrganService {
    // 在这里实现接口中的方法
}
