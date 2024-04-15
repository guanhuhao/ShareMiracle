package com.sharemiracle.mapper;

import com.sharemiracle.entity.Dataset;
import com.sharemiracle.entity.Model;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelDataMapper {

    /**
     * 新建模型数据
     */
    void insert(Model model);

    /*
    * 删除模型数据
    * */

    @Delete("delete from share_miracle.r_model_organization where share_miracle.r_model_organization.model_id = #{id}")
    void deletebyid(Long id);
    @Delete("delete from share_miracle.t_model where share_miracle.t_model.id = #{id}")
    void deletebyid2(Long id);

    @Select("select authority from share_miracle.r_user_organization,share_miracle.r_model_organization where status = 1 and user_id = #{id} and model_id = #{modelId} and share_miracle.r_model_organization.organization_id = share_miracle.r_user_organization.organization_id")
    Integer getauByid(Long id,Long modelId);

    /*
    * 更新模型数据
    * */
    void update(Model model);

    @Select("select user_id from share_miracle.t_model where id = #{id}")
    Long getUseridbyId(Long id);

    Model selectById(Long modelId);

    List<Long> selectAll(Long id);

    void updateDatasetOrgan(Long datasetId,Long organId);


}
