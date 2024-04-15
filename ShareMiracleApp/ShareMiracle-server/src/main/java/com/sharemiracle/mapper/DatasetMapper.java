package com.sharemiracle.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharemiracle.entity.Dataset;

import java.util.List;

public interface DatasetMapper extends BaseMapper<Dataset> {

    int insert(Dataset dataset);

    Dataset selectById(Long id);

    List<Long> selectAll(Long userId);

    void updateDatasetOrgan(Long datasetId,Long organId);

    void insertDatasetOrgan(Long datasetId, Long organizationId);

    Long selectAuthorityById(Long id);

    void deletebyId(Long id);

    Dataset update(Dataset dataset);

    Long selectOrganId(Long userId);

    int selectStatus(Long userId);
}
