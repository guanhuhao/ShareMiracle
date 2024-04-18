package com.sharemiracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharemiracle.entity.Model;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelMapper extends BaseMapper<Model> {

    /**
     * 新建模型数据
     *
     * @return
     */
    int insert(Model model);

    /*
     * 删除模型数据
     * */

    /*@Delete("delete from share_miracle.r_model_organization where share_miracle.r_model_organization.model_id = #{id}")
    void deletebyid(Long id);
    */
    @Delete("delete from share_miracle.t_model where share_miracle.t_model.id = #{id}")
    void deletebyid2(Long id);

    @Select("select authority from share_miracle.r_user_organization,share_miracle.r_model_organization where status = 1 and user_id = #{id} and model_id = #{modelId} and share_miracle.r_model_organization.organization_id = share_miracle.r_user_organization.organization_id")
    Integer getauByid(Long id,Long modelId);

    /*
     * 更新模型数据
     * */
    void update(Model model);
    void updateByid(Model model);

    @Select("select user_id from share_miracle.t_model where id = #{id}")
    Long getUseridbyId(Long id);

    Model selectById(Long modelId);


    Long selectAuthorityById(Long id);


    void updateDatasetOrgan(Long datasetId,Long organId);

    void insertModelOrgan(Long modelId, Long organizationId);

    @Select("select authority from share_miracle.r_user_organization,share_miracle.r_model_organization where share_miracle.r_user_organization.user_id = #{userId} and share_miracle.r_model_organization.model_id = #{modelId} and share_miracle.r_model_organization.organization_id = share_miracle.r_user_organization.organization_id and status = 1")
    Integer  selectAuthorityByid(Long userId, Long modelId);


    List<Long> selectOrganId(Long userId);

    List<Long> selectAll(Long organID);

    int selectStatus(Long userId,Long organID);

    List<Long> selectAllByUserId(Long userId);

    List<Long> selectAllisPublic();




}
