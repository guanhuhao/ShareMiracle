package com.sharemiracle.service.serviceImpl;

import com.sharemiracle.dto.ModelDTO;
import com.sharemiracle.dto.ModelDataOrganDTO;
import com.sharemiracle.dto.ModelDataQueryDTO;
import com.sharemiracle.dto.ModelIdsDTO;
import com.sharemiracle.entity.Model;
import com.sharemiracle.entity.Organization;
import com.sharemiracle.exception.DeletionNotAllowedException;
import com.sharemiracle.mapper.ModelDataMapper;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.ModelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;


@Service
public class ModelServerImpl implements ModelService {

    @Autowired
    private ModelDataMapper modelDataMapper;

    /**
     * 1.新建模型数据
     */
    @Override
    public void add(ModelDTO modelDTO) {
        Model model = new Model();
        //创建时间，更新时间
        model.setCreateTime(LocalDateTime.now());
        model.setUpdateTime(LocalDateTime.now());

        //获得操作人id
        //Long userId = BaseContext.getCurrentId();
        Long userId = 1L;

        BeanUtils.copyProperties(modelDTO,model);
        model.setUserId(userId);
        //插入模型表
        modelDataMapper.insert(model);

        //插入模型-组织关系表
        Long modelId = model.getId();

        List<Organization> shareOrganization = modelDTO.getShareOrganization();
        if(shareOrganization != null && shareOrganization.size() > 0) {
            for (int i = 0; i < shareOrganization.size(); i++) {
                Organization organization = shareOrganization.get(i);
                Long organizationId = organization.getId();
                modelDataMapper.insertModelOrgan(modelId,organizationId);

            }
        }
    }

    /**
     *2.删除模型数据
     */
    @Override
    public void deleteBatch(ModelIdsDTO modelIdsDTO) {
        // 通过token获得删除者id
        //Long userId = BaseContext.getCurrentId();
        Long userId = 1L;
        String idsString = modelIdsDTO.getModelId();
        List<String> idss = Arrays.asList(idsString.split(","));
        List<Long>ids = new ArrayList<>();
        for (String s : idss) {
            ids.add(Long.valueOf(s));
        }

        for (Long id : ids) {
            Integer auth = modelDataMapper.selectAuthorityByid(userId, id);
            if(auth  == null || auth != 0) throw new DeletionNotAllowedException("删除失败");
        }

        for (Long id : ids) {
            //modelDataMapper.deletebyid(id);
            modelDataMapper.deletebyid2(id);
        }
    }

    /*
     * 3.修改模型数据
     * */
    @Override
    public Result update(ModelDTO modelDTO) {
        Model model = new Model();
        BeanUtils.copyProperties(modelDTO,model);
        //更新时间
        model.setUpdateTime(LocalDateTime.now());

        // 通过token获得修改者id
        //Long userId = BaseContext.getCurrentId();
        Long userId = 1L;


        Long buildId = modelDataMapper.getUseridbyId(model.getId());
        Integer au =modelDataMapper.getauByid(userId,model.getId());


        if(!buildId.equals(userId) || au  == null || au != 0) return Result.error("修改失败");
        modelDataMapper.updateByid(model);
        return Result.success();
    }

    /*
     * 4.修改模型状态
     * */
    @Override
    public Result updateStatus(Boolean status, Long id) {

        // 通过token获得修改者id
        //Long userId = BaseContext.getCurrentId();
        Long userId = 1L;
        Long onerId = modelDataMapper.getUseridbyId(id);
        Integer au =modelDataMapper.getauByid(userId,id);

        if(!userId.equals(onerId) || au  == null || au != 0)    return Result.error("您没有权限！");

        Model model = new Model();
        model.setId(id);
        model.setIsPublic(status);

        modelDataMapper.updateByid(model);
        return Result.success();
    }

    /*
     * 5.修改模型有权使用组织
     */
    public void updateDatasetOrgan(ModelDataOrganDTO modelDataOrganDTO) {
        // Long userId = BaseContext.getCurrentId();
        Long userId = 1L;
        Long datasetId = modelDataOrganDTO.getModelDataId();
        Long auth = modelDataMapper.selectAuthorityById(datasetId);
        if(!Objects.equals(auth, userId)){
            throw new DeletionNotAllowedException("无权修改");
        }
        List<Long> ids = modelDataOrganDTO.getIds();
        for(Long id : ids){
            modelDataMapper.updateDatasetOrgan(datasetId,id);
        }

    }
    /*
     * 6.请求模型数据信息
     */
    public Model selectById(ModelDataQueryDTO modelDataQueryDTO){
        Long id = modelDataQueryDTO.getModelId();
        return modelDataMapper.selectById(id);

    }
    /*
     * 7.查询当前用户有权使用的所有模型
     */
    public List<Long> selectAll() {
        // Long userId = BaseContext.getCurrentId();
        Long userId = 1L;
        List<Long> organIDs = modelDataMapper.selectOrganId(userId);

        if (organIDs.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> uniqueIds = new HashSet<>();
        for(Long organID : organIDs) {
            int status = modelDataMapper.selectStatus(userId,organID);
            if(status == 0){
                throw new DeletionNotAllowedException("查询失败");
            }
            uniqueIds.addAll(modelDataMapper.selectAll(organID));
        }
        uniqueIds.addAll(modelDataMapper.selectAllByUserId(userId));
        return new ArrayList<>(uniqueIds);
    }



}


