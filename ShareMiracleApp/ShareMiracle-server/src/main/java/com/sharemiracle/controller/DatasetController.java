package com.sharemiracle.controller;

import com.sharemiracle.dto.DatasetDTO;
import com.sharemiracle.dto.DatasetDeleteDTO;
import com.sharemiracle.dto.DatasetOrganDTO;
import com.sharemiracle.dto.DatasetQueryDTO;
import com.sharemiracle.entity.Dataset;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.serviceImpl.DatasetServiceImpl;
import com.sharemiracle.vo.DatasetOrganVO;
import com.sharemiracle.vo.DatasetQueryAllVO;
import com.sharemiracle.vo.DatasetQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



@Slf4j
@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Resource
    private DatasetServiceImpl datasetService;

    /**
     * 新建数据集
     */
    @PostMapping("/add")
    public Result buildModelController(@RequestBody DatasetDTO datasetDTO) {
        log.info("新建数据集：{}", datasetDTO);
        datasetService.add(datasetDTO);
        return Result.success();
    }

    /**
     * 删除数据集
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody DatasetDeleteDTO datasetDeleteDTO) {
        log.info("删除数据集数据：{}", datasetDeleteDTO.getId());
        datasetService.delete(datasetDeleteDTO);
        return Result.success();
    }

    /**
     * 批量删除数据集
     */
    @DeleteMapping("/delete-batch")
    public Result deleteBatch(@RequestBody DatasetDeleteDTO datasetDeleteDTO) {
        log.info("批量删除数据集数据：{}", datasetDeleteDTO.getIds());
        datasetService.deleteBatch(datasetDeleteDTO);
        return Result.success();
    }

    /**
     * 修改数据集信息
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody DatasetDTO datasetDTO) {
        log.info("修改数据集信息：{}", datasetDTO);
        return datasetService.update(datasetDTO);
    }

    /**
     * 修改数据集私有状态
     */
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestBody DatasetDTO datasetDTO) {
        log.info("修改数据集私有状态：{}",datasetDTO.getId());
        return datasetService.updateStatus(datasetDTO);
    }


    /**
     * 修改数据集有权使用组织
     */
    @PutMapping("/organ")
    public Result<DatasetOrganVO> updateDatasetOrgan(@RequestBody DatasetOrganDTO datasetOrganDTO) {
        log.info("修改数据集有权使用组织: {}",datasetOrganDTO);

        datasetService.updateDatasetOrgan(datasetOrganDTO);

        log.info("修改数据集有权使用组织成功");

        return Result.success();
    }


    /**
     * 请求数据集信息
     */
    @GetMapping("/query-by-id")
    public Result<DatasetQueryVO> selectById(@RequestBody  DatasetQueryDTO datasetQueryDTO) {
        log.info("请求数据集信息: {}",datasetQueryDTO);

        Dataset dataset = datasetService.selectById(datasetQueryDTO);

        DatasetQueryVO datasetqueryVO = DatasetQueryVO.builder()
                .datasetUrl(dataset.getDatasetUrl())
                .build();
        return Result.success(datasetqueryVO);
    }


    /**
     * 查询当前用户有权使用的所有数据集
     */
    @GetMapping("/query-all")
    public  Result<DatasetQueryAllVO> selectAll() {
        log.info("查询当前用户有权使用的所有数据集");

        List<Long> list = datasetService.selectAll();

        DatasetQueryAllVO datasetqueryallVO = DatasetQueryAllVO.builder()
                .ids(list)
                .build();
        return Result.success(datasetqueryallVO);
    }

}
