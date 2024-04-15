package com.sharemiracle.service;

import com.sharemiracle.dto.ModelDTO;
import com.sharemiracle.dto.ModelDataOrganDTO;
import com.sharemiracle.dto.ModelDataQueryDTO;
import com.sharemiracle.dto.ModelIdsDTO;
import com.sharemiracle.entity.Model;
import com.sharemiracle.result.Result;

import java.util.List;

public interface ModelService {

    void add(ModelDTO modelDTO);

    void deleteBatch(ModelIdsDTO modelIdsDTO);

    Result update(ModelDTO modelDTO);

    Result updateStatus(Boolean status, Long id);

    void updateDatasetOrgan(ModelDataOrganDTO modelDataOrganDTO);


    Model selectById(ModelDataQueryDTO modelQueryDTO);


    List<Long> selectAll();
}
