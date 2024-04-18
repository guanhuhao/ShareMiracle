package com.sharemiracle.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharemiracle.context.BaseContext;
import com.sharemiracle.entity.Organization;
import com.sharemiracle.entity.User;
import com.sharemiracle.entity.UserOrgan;
import com.sharemiracle.mapper.OrganMapper;
import com.sharemiracle.result.Result;
import com.sharemiracle.service.OrganService;
import com.sharemiracle.service.UserOrganService;
import com.sharemiracle.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrganServiceImpl extends ServiceImpl<OrganMapper, Organization> implements OrganService {

    @Resource
    private UserOrganService userOrganService;

    @Resource
    private UserService userService;

    @Resource

    /**
     * 新建组织
     * @param organ
     * @return
     */
    @Override
    public Result<String> addOrgan(Organization organ) {
        // 1.系统管理员权限校验
        Long currentUserId = BaseContext.getCurrentId();
        User currentUser = userService.getById(currentUserId);
        if(currentUser.getAuthority()==1){
            // 1.1.用户非管理员
            return Result.error("您不是系统管理员！");
        }
        // 1.2.用户为管理员
        // 2.创建组织
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
        /*// 1.从当前用户线程获取id
        Long userId = BaseContext.getCurrentId();
        // TODO: 2.申请消息存入队列，等待管理员批准(消费)

        // 3.提交申请结果
        boolean isSuccess = true; // TODO:"申请提交成功/失败";
        if(!isSuccess){
            return Result.error("提交申请失败！");
        }
        return Result.success("提交申请成功！等待管理员批准");*/

        // 1.获取当前用户id
        Long currentUserId = BaseContext.getCurrentId();
        // 2.存入用户-组织表(加入组织)
        UserOrgan userOrgan = new UserOrgan();
        userOrgan.setOrganizationId(organId);
        userOrgan.setUserId(currentUserId);
        // 3.默认权限设置
        userOrgan.setAuthority(2);
        userOrgan.setStatus(0); //初始禁用状态
        boolean isSuccess = userOrganService.save(userOrgan);
        if(!isSuccess){
            return Result.error("加入组织失败！");
        }
        return Result.success("加入组织成功！");
    }

    /**
     * 修改给定用户在给定组织中的权限
     * @param organId
     * @param userId
     * @param auth
     * @return
     */
    @Override
    public Result<String> authority(Long organId, Long userId, Integer auth) {
        // 1.获取当前用户
        Long currentUserId = BaseContext.getCurrentId();
        QueryWrapper<UserOrgan> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", currentUserId)
                .eq("organization_id", organId);
        UserOrgan currentUserOrgan = userOrganService.getOne(wrapper1);

        // 2.获取要操作的用户
        QueryWrapper<UserOrgan> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("user_id", userId)
                .eq("organization_id", organId);
        UserOrgan userOrgan = userOrganService.getOne(wrapper2);

        // 3.权限校验
        if(currentUserOrgan.getAuthority() >= userOrgan.getAuthority()){
            // 3.1.当前用户比要修改的用户权限小或相等，无法修改
            return Result.error("权限不足！");
        }
        // 3.2.权限足够
        // 4.修改用户权限
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
        // 1.获取当前用户
        Long currentUserId = BaseContext.getCurrentId();
        QueryWrapper<UserOrgan> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", currentUserId)
                .eq("organization_id", organId);
        UserOrgan currentUserOrgan = userOrganService.getOne(wrapper1);

        // 2.获取要操作的用户
        QueryWrapper<UserOrgan> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("user_id", userId)
                .eq("organization_id", organId);
        UserOrgan userOrgan = userOrganService.getOne(wrapper2);

        // 3.权限校验
        if(currentUserOrgan.getAuthority() >= userOrgan.getAuthority()){
            // 3.1.当前用户比要修改的用户权限小或相等，无法修改
            return Result.error("权限不足！");
        }
        // 3.2.权限足够
        // 4.修改用户在组织中的状态
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
    public Result<String> deleteOrganUser(Long organId, Long userId) {
        // 1.获取当前用户
        Long currentUserId = BaseContext.getCurrentId();
        QueryWrapper<UserOrgan> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", currentUserId)
                .eq("organization_id", organId);
        UserOrgan currentUserOrgan = userOrganService.getOne(wrapper1);

        // 2.获取要操作的用户
        QueryWrapper<UserOrgan> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("user_id", userId)
                .eq("organization_id", organId);
        UserOrgan userOrgan = userOrganService.getOne(wrapper2);

        // 3.权限校验
        if(currentUserOrgan.getAuthority() >= userOrgan.getAuthority()){
            // 3.1.当前用户比要修改的用户权限小或相等，无法修改
            return Result.error("权限不足！");
        }
        // 3.2.权限足够
        // 4.删除组织中的用户
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
    public Result<String> deleteOrgan(Long organId) {
        // 1.获取当前用户
        Long currentUserId = BaseContext.getCurrentId();
        QueryWrapper<UserOrgan> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", currentUserId)
                .eq("organization_id", organId);
        UserOrgan currentUserOrgan = userOrganService.getOne(wrapper1);

        // 2.权限校验
        if(currentUserOrgan.getAuthority() != 0){
            // 2.1.当前用户非该组织创建者，无法删除
            return Result.error("权限不足！");
        }
        // 2.2.权限足够
        // 3.删除组织中的用户
        boolean isSuccess = remove(new QueryWrapper<Organization>()
                .eq("organization_id", organId));
        if(!isSuccess){
            Result.error("删除组织失败");
        }
        return Result.success("删除组织成功！");
    }
}
