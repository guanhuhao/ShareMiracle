package com.sharemiracle.controller;
import com.sharemiracle.dto.ModelDTO;
import com.sharemiracle.dto.ModelDataOrganDTO;
import com.sharemiracle.dto.ModelDataQueryDTO;
import com.sharemiracle.dto.ModelIdsDTO;
import com.sharemiracle.entity.Model;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.ModelService;
import com.sharemiracle.vo.ModelDataOrganVO;
import com.sharemiracle.vo.ModelDataQueryAllVO;
import com.sharemiracle.vo.ModelDataQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模型数据管理
 */
@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    @Resource
    private ModelService modelService;

    /**
     * 1.新建模型数据
     */
    @PostMapping("add")
    public Result buildModelController(@RequestBody ModelDTO modelDTO) {
        log.info("新建模型数据：{}", modelDTO);
        modelService.add(modelDTO);
        return Result.success();
    }
    /**
     *2.删除模型数据
     */
    @DeleteMapping
    //TODO 用@RequestBody还是@RequestParam
    public Result delete(@RequestBody ModelIdsDTO modelIdsDTO) {
        log.info("批量删除模型数据：{}", modelIdsDTO);
        modelService.deleteBatch(modelIdsDTO);
        return Result.success();
    }
    /*
     * 3.修改模型数据
     */
    @PutMapping
    public Result<String> update(@RequestBody ModelDTO modelDTO) {
        log.info("修改模型数据：{}", modelDTO);
        return modelService.update(modelDTO);
    }
    /*
     * 4.修改模型私有状态
     */
    @PutMapping("/{status}")
    public Result<String> updateStatus(@PathVariable Boolean status,Long id) {
        log.info("修改模型私有状态：{},{}",status,id);
        return modelService.updateStatus(status,id);
    }

    /*
     * 5.修改模型有权使用组织
     */
    @PutMapping("/organ")
    public Result<ModelDataOrganVO> updateDatasetOrgan(@RequestBody ModelDataOrganDTO modelDataOrganDTO) {
        log.info("修改数据集有权使用组织: {}",modelDataOrganDTO);
        modelService.updateDatasetOrgan(modelDataOrganDTO);
        log.info("修改数据集有权使用组织成功");
        return Result.success();
    }

    /*
     * 6.请求模型数据信息
     */

    @GetMapping("/query-by-id")
    public Result<ModelDataQueryVO> selectById(@RequestBody ModelDataQueryDTO modelDataQueryDTO ) {
        log.info("请求数据集信息: {}",modelDataQueryDTO);

        Model model = modelService.selectById(modelDataQueryDTO);

        ModelDataQueryVO  modelDataQueryVO = ModelDataQueryVO.builder()
                .modelDataUrl(model.getModelUrl())
                .build();
        return Result.success(modelDataQueryVO);
    }


    /**
     * 7.查询当前用户有权使用的所有模型
     */

    @GetMapping("/query-all")
    public  Result<ModelDataQueryAllVO> selectAll() {
        log.info("查询当前用户有权使用的所有数据集");

        List<Long> list = modelService.selectAll();

        ModelDataQueryAllVO modelqueryallVO = ModelDataQueryAllVO.builder()
                .ids(list)
                .build();
        return Result.success(modelqueryallVO);
    }

}
