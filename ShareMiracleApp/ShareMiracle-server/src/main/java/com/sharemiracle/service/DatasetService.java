package com.sharemiracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharemiracle.dto.DatasetDTO;
import com.sharemiracle.dto.DatasetDeleteDTO;
import com.sharemiracle.dto.DatasetOrganDTO;
import com.sharemiracle.dto.DatasetQueryDTO;
import com.sharemiracle.entity.Dataset;
import com.sharemiracle.result.Result;

import java.util.List;

public interface DatasetService {
    void add(DatasetDTO datasetDTO);

    void delete(DatasetDeleteDTO datasetDeleteDTO);

    void deleteBatch(DatasetDeleteDTO datasetDeleteDTO);

    Result<String> update(DatasetDTO datasetDTO);

    Result<String> updateStatus(DatasetDTO datasetDTO);

    void updateDatasetOrgan(DatasetOrganDTO datasetOrganDTO);

    Dataset selectById(DatasetQueryDTO datasetQueryDTO);

    List<Long> selectAll();
}
