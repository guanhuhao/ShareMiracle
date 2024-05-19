package com.sharemiracle.vo;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.sharemiracle.dto.ElasticSearchItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSearchVO implements Serializable {
    /**
     * 检索到的项
     */
    private int cnt;
    /**
     * 检索分数
     */
    private List<Double> score;
    /**
     * 检索结果
     */
    private List<ElasticSearchItemDTO> Result;

}
