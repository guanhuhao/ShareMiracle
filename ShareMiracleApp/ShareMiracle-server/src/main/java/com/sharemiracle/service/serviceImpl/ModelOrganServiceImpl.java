package com.sharemiracle.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharemiracle.entity.ModelOrgan;
import com.sharemiracle.entity.UserOrgan;
import com.sharemiracle.mapper.ModelOrganMapper;
import com.sharemiracle.mapper.UserOrganMapper;
import com.sharemiracle.service.ModelOrganService;
import com.sharemiracle.service.UserOrganService;
import org.springframework.stereotype.Service;

@Service
public class ModelOrganServiceImpl extends ServiceImpl<ModelOrganMapper, ModelOrgan> implements ModelOrganService {

}
