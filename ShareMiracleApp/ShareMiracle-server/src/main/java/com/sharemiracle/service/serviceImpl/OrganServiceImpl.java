package com.sharemiracle.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharemiracle.entity.Organization;
import com.sharemiracle.entity.UserOrgan;
import com.sharemiracle.mapper.OrganMapper;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.OrganService;
import com.sharemiracle.service.UserOrganService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrganServiceImpl extends ServiceImpl<OrganMapper, Organization> implements OrganService {

    @Resource
    private UserOrganService userOrganService;

    /**
     * 新建组织
     * @param organ
     * @return
     */
    @Override
    public Result<String> addOrgan(Organization organ) {
        boolean isSuccess = save(organ);
        if(!isSuccess){
            return Result.error("新增组织失败！");
        }
        return Result.success("新增组织成功！");
    }

    /**
     * 当前用户加入给定组织
     * @param organId
     * @return
     */
    @Override
    public Result<String> joinOrgan(Long organId) {
        // TODO:修改userId为->从当前用户线程获取
        Long userId = 1L;
        UserOrgan userOrgan = new UserOrgan();
        userOrgan.setOrganizationId(organId);
        userOrgan.setUserId(userId);
        // TODO:默认权限设置为几？
        userOrgan.setAuthority(0);
        boolean isSuccess = userOrganService.save(userOrgan);
        if(!isSuccess){
            return Result.error("加入组织失败！");
        }
        return Result.success("加入组织成功！");
    }

    /**
     * 设置给定用户在给定组织中的权限
     * @param organId
     * @param userId
     * @param auth
     * @return
     */
    @Override
    public Result<String> authority(Long organId, Long userId, Integer auth) {
//        LambdaUpdateWrapper<UserOrgan> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(UserOrgan::getUserId, userId)
//                .eq(UserOrgan::getOrganizationId, organId);
//        // 用户组织表中查询为空
//        if(userOrgan == null){
//            return Result.error("用户未在组织中！");
//        }
//        userOrganService.update(null, updateWrapper);
        boolean isSuccess = userOrganService.update().set("authority", auth)
                .eq("user_id", userId)
                .eq("organization_id", organId).update();
        if(!isSuccess){
            return Result.error("用户权限更新失败！");
        }
        return Result.success("用户权限更新成功！");
    }

    /**
     * 启用/禁用用户组织账号
     * @param status
     * @param userId
     * @return
     */
    @Override
    public Result<String> status(Integer status, Long userId, Long organId) {
        boolean isSuccess = userOrganService.update().set("status", status)
                .eq("user_id", userId)
                .eq("organization_id", organId).update();
        if(!isSuccess){
            return Result.error("用户状态更新失败！");
        }
        return Result.success("用户状态更新成功！");
    }

    /**
     * 删除给定组织中的给定用户
     * @param organId
     * @param userId
     * @return
     */
    @Override
    public Result<String> deleteOrganUser(String organId, String userId) {
        boolean isSuccess = userOrganService.remove(new QueryWrapper<UserOrgan>()
                .eq("user_id", userId)
                .eq("organization_id", organId));
        if(!isSuccess){
            Result.error("删除该组织中的该用户失败");
        }
        return Result.success("删除组织中的用户成功！");
    }

    /**
     * 删除给定组织
     * @param organId
     * @return
     */
    @Override
    public Result<String> deleteOrgan(String organId) {
        boolean isSuccess = remove(new QueryWrapper<Organization>()
                .eq("organization_id", organId));
        if(!isSuccess){
            Result.error("删除组织失败");
        }
        return Result.success("删除组织成功！");
    }
}
