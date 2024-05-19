package com.sharemiracle.controller;

import com.sharemiracle.constant.MessageConstant;
import com.sharemiracle.dto.SearchDTO;
import com.sharemiracle.dto.ElasticSearchItemDTO;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.ElasticSearchService;
import com.sharemiracle.service.serviceImpl.DatasetServiceImpl;
import com.sharemiracle.service.serviceImpl.ElasticSearchServerImpl;

import com.sharemiracle.vo.EsSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/es")
public class ElasticSearchController {
    @Resource
    private ElasticSearchService elasticSearchServerImpl;

    /**
     * 查找数据
     */
    @GetMapping("/search")
    public Result<EsSearchVO> searchController(@RequestBody SearchDTO searchDTO) throws IOException {
        log.info("查找数据：{}", searchDTO);
        EsSearchVO result = elasticSearchServerImpl.search(searchDTO);
        return Result.success(result, MessageConstant.ESSEAERCH_SUCCESS);
    }

    @PutMapping("/addItem")
    public Result addItemController(@RequestBody ElasticSearchItemDTO elasticSearchItemDTO) {
        log.info("添加Es数据：{}", elasticSearchItemDTO);
//        datasetService.add(searchDTO);
        return Result.success();
    }

}
