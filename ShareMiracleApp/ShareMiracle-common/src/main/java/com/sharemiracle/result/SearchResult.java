package com.sharemiracle.result;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.sharemiracle.dto.ElasticSearchItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult implements Serializable {

    private long total; //总记录数

    private List<Hit<ElasticSearchItemDTO>> records; //当前页数据集合

}
