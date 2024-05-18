package com.sharemiracle.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharemiracle.entity.Dataset;

import java.util.List;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
public interface ElasticSearchMapper extends BaseMapper<Dataset> {

    int insert(Dataset dataset);

    void deleteById(Long id);

    void update(Dataset dataset);

    int selectStatus(Long userId,Long organID);

    void updateDatasetOrgan(Long datasetId,Long organId);

    void insertDatasetOrgan( Long datasetId, Long organizationId);

    Long selectAuthorityById(Long id);

    Dataset selectById(Long id);

    List<Long> selectAll(Long organID);

    List<Long> selectAllByUserId(Long userId);

    List<Long> selectAllisPublic();

    List<Long> selectOrganId(Long userId);

}
