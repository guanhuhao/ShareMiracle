package com.sharemiracle.service.serviceImpl;

import cn.hutool.core.collection.AvgPartition;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.sharemiracle.constant.MessageConstant;
import com.sharemiracle.dto.*;
import com.sharemiracle.entity.User;
import com.sharemiracle.mapper.ElasticSearchMapper;
import com.sharemiracle.result.Result;
import com.sharemiracle.result.SearchResult;
import com.sharemiracle.service.ElasticSearchService;
import com.sharemiracle.vo.EsSearchVO;
import com.sharemiracle.vo.UserLoginVO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;

//public class Item {
//    private String datatype;
//    private String modal;
//    private String task;
//    private String tag;
//    private String query;
//}

@Service
public class ElasticSearchServerImpl implements ElasticSearchService {

    @Autowired
    private ElasticsearchClient esClient;

    @Resource
    private ElasticSearchMapper esMapper;

    /**
     * 1.查询Es数据库
     */
    @Override
    public Result<EsSearchVO> search(SearchDTO searchDTO) throws IOException {
        List<String> dataTypes = searchDTO.getDatatype();
        List<String> modals = searchDTO.getModal();
        List<String> tags = searchDTO.getTag();

//        String tagBuilder;
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
        System.out.println("tags: " + query_keywords);

        SearchResponse<ElasticSearchItemDTO> searchResponse = esClient.search(s -> s.index("es")
                        .query(q -> q.match(t -> t
                                .field("description")
                                .query(query_keywords))),
                ElasticSearchItemDTO.class);



        // 打印结果
        System.out.println("Index information: " + searchResponse);
        List<Hit<ElasticSearchItemDTO>> hits = searchResponse.hits().hits();
        List<ElasticSearchItemDTO> results = new ArrayList<>();
        List<Double> scores = new ArrayList<>();
        for (Hit<ElasticSearchItemDTO> hit: hits) {
            ElasticSearchItemDTO items = hit.source();
            results.add(items);
            scores.add(hit.score());
//            System.out.println("找到产品 " + product.getSku() + "，得分 " + hit.score());
        }
        EsSearchVO esSearchVO = new EsSearchVO(
                hits.size(),
                scores,
                results
        );
        Result<EsSearchVO> result = Result.success(esSearchVO);
        result.setMsg(MessageConstant.ESSEAERCH_SUCCESS);
        return result;
    }

    @Override
    public void addItem(ElasticSearchItemDTO elasticSearchItemDTO) {

    }

}


