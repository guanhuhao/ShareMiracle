package com.sharemiracle.service.serviceImpl;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.sharemiracle.constant.MessageConstant;
import com.sharemiracle.dto.*;
import com.sharemiracle.mapper.ElasticSearchMapper;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.ElasticSearchService;
import com.sharemiracle.vo.EsSearchVO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ElasticSearchServerImpl implements ElasticSearchService {

    @Autowired
    private ElasticsearchClient esClient;

    //@Resource
    //private ElasticSearchMapper esMapper;

    /**
     * 1.查询Es数据库
     */
    @Override
    public EsSearchVO search(SearchDTO searchDTO) throws IOException {
        // 解析前端DTO
        List<String> dataTypes = searchDTO.getDatatype();
        List<String> modals = searchDTO.getModal();
        List<String> tags = searchDTO.getTag();

        //构建查询语句关键词
        StringBuilder tagBuilder = new StringBuilder();
        for( String tag : dataTypes){
            tagBuilder.append(String.format("%s ",tag));
        }
        for( String tag : modals){
            tagBuilder.append(String.format("%s ",tag));
        }
        for( String tag : tags){
            tagBuilder.append(String.format("%s ",tag));
        }
        tagBuilder.append(String.format("%s ",searchDTO.getQuery()));

        String query_keywords = tagBuilder.toString();
        log.info("build query token:{}",query_keywords);

        // 查询es数据库
        // TODO: 实现分页查询
        SearchResponse<ElasticSearchItemDTO> searchResponse = esClient.search(s -> s.index("es")
                        .query(q -> q.match(t -> t
                                .field("description")
                                .query(query_keywords))),
                ElasticSearchItemDTO.class);



        //解析查询结果
        List<Hit<ElasticSearchItemDTO>> hits = searchResponse.hits().hits();
        List<ElasticSearchItemDTO> results = new ArrayList<>();
        List<Double> scores = new ArrayList<>();
        for (Hit<ElasticSearchItemDTO> hit: hits) {
            ElasticSearchItemDTO items = hit.source();
            results.add(items);
            scores.add(hit.score());
        }

        // 构建前端视图
        return new EsSearchVO(
                hits.size(),
                scores,
                results
        );
    }

    @Override
    public void addItem(ElasticSearchItemDTO elasticSearchItemDTO) {

    }

}


