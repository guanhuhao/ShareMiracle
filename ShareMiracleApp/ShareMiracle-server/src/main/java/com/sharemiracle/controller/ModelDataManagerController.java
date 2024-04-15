package com.sharemiracle.controller;
import com.sharemiracle.dto.ModelDTO;
import com.sharemiracle.dto.ModelDataOrganDTO;
import com.sharemiracle.dto.ModelDataQueryDTO;
import com.sharemiracle.dto.ModelIdsDTO;
import com.sharemiracle.entity.Dataset;
import com.sharemiracle.entity.Model;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.ModelDataService;
import com.sharemiracle.vo.ModelDataOrganVO;
import com.sharemiracle.vo.ModelDataQueryAllVO;
import com.sharemiracle.vo.ModelDataQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 模型数据管理
 */
@RestController
@RequestMapping("model")
@Slf4j
public class ModelDataManagerController {
    @Autowired
    private ModelDataService modelDataService;

    /**
     * 1.新建模型数据
     */
    @PostMapping
    public Result buildModelController(@RequestBody ModelDTO modelDTO) {
        log.info("新建模型数据：{}", modelDTO);
        modelDataService.add(modelDTO);
        return Result.success();
    }
    /**
     *2.删除模型数据
     */
    @DeleteMapping
    //TODO 用@RequestBody还是@RequestParam
    public Result delete(@RequestBody ModelIdsDTO modelIdsDTO) {
        log.info("批量删除模型数据：{}", modelIdsDTO);
        modelDataService.deleteBatch(modelIdsDTO);
        return Result.success();
    }
    /*
     * 3.修改模型数据
     */
    @PutMapping
    public Result<String> update(@RequestBody ModelDTO modelDTO) {
        log.info("修改模型数据：{}", modelDTO);
        return modelDataService.update(modelDTO);
    }
    /*
     * 4.修改模型私有状态
     */
    @PutMapping("/{status}")
    public Result<String> updateStatus(@PathVariable Boolean status,Long id) {
        log.info("修改模型私有状态：{},{}",status,id);
        return modelDataService.updateStatus(status,id);
    }

    /*
     * 5.修改模型有权使用组织
     */
    @PutMapping("/organ")
    public Result<ModelDataOrganVO> updateDatasetOrgan(@RequestBody ModelDataOrganDTO modelDataOrganDTO) {
        log.info("修改数据集有权使用组织: {}",modelDataOrganDTO);

        modelDataService.updateDatasetOrgan(modelDataOrganDTO);

        log.info("修改数据集有权使用组织成功");

        return Result.success();
    }


    /*
     * 6.请求模型数据信息
     */
    @GetMapping
    public Result<ModelDataQueryVO> selectById(@RequestBody ModelDataQueryDTO modelQueryDTO) {
        log.info("查询数据集信息: {}",modelQueryDTO);

        Model dataset = modelDataService.selectById(modelQueryDTO);

        ModelDataQueryVO modeldataqueryVO = ModelDataQueryVO.builder()
                .modelDataUrl(dataset.getModelUrl())
                .build();
        return Result.success(modeldataqueryVO);
    }

    /*
     * 7.查询当前用户有权使用的所有模型
     */
    @GetMapping("/all")
    public  Result<ModelDataQueryAllVO> selectAll() {
        log.info("查询所有数据集信息");

        List<Long> list = modelDataService.selectAll();

        ModelDataQueryAllVO datasetqueryallVO = ModelDataQueryAllVO.builder()
                .ids(list)
                .build();
        return Result.success(datasetqueryallVO);
    }
}
