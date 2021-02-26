package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.service.IPermissionService;
import com.pc.rbac_system.service.IRedisService;
import com.pc.rbac_system.service.IRoleService;
import com.pc.rbac_system.vo.RoleSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.pc.rbac_system.mapper.RoleMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    IRedisService redisService;
    @Autowired
    IPermissionService permissionService;

    @Value("${jwt.expiration}")
    Long redisRoleInfoExpirationTimeSecond;

    @Override
    public Result findAllRoles() {
        List<Role> roles;
        if (redisService.hasKey("RBAC_SYSTEM:WX_ROLE:ROLES")) {
            roles = redisService.get("RBAC_SYSTEM:WX_ROLE:ROLES");
        } else {
            List<Role> allRoles = roleMapper.findAllRoles();
            for (Role role : allRoles) {
                role.setPermissions(permissionService.findPermissionsByRoleId(role.getId()));
            }
            roles = allRoles;
            redisService.set("RBAC_SYSTEM:WX_ROLE:ROLES", roles);
        }
        return Result.success(roles);
    }

    @Override
    public Result findRoleById(Long id) {
        Role roleById = roleMapper.findRoleById(id);
        roleById.setPermissions(permissionService.findPermissionsByRoleId(id));
        return Result.success(roleById);
    }

    @Override
    public Result findRoleByName(String name) {
        return null;
    }

    @Override
    public Result deleteRoleById(Long id) {
        Integer integer = roleMapper.deleteRoleById(id);
        if (integer > 0) {
            Result.success(true);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    @Transactional
    public Result addRole(Role role) {
        Integer rResult = roleMapper.addRole(role);
        Integer pResult = permissionService.updateRolePermissions(role);
        if (redisService.hasKey("RBAC_SYSTEM:WX_ROLE:ROLES")){
            redisService.del("RBAC_SYSTEM:WX_ROLE:ROLES");
        }
        if (rResult+pResult > 0) {
            return Result.success(role);
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result updateRole(Role role) {
        Integer integer = roleMapper.updateRole(role);
        Integer suc = permissionService.updateRolePermissions(role);
        if (redisService.hasKey("RBAC_SYSTEM:WX_ROLE:ROLES")) {
            redisService.del("RBAC_SYSTEM:WX_ROLE:ROLES");
        }
        if (integer+suc > 0) {
            return Result.success(true);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public List<Role> findRolesByUserId(Long id) {
        List<Role> list = new ArrayList<>();
        if (redisService.hasKey("RBAC_SYSTEM:WX_ROLE:LOGIN:INFO:"+id)){
            list = redisService.get("RBAC_SYSTEM:WX_ROLE:LOGIN:INFO:"+id);
        }else {
            list = roleMapper.findRolesByUserId(id);
            redisService.setWithExpire("RBAC_SYSTEM:WX_ROLE:LOGIN:INFO:"+id,list,redisRoleInfoExpirationTimeSecond);
        }
        return list;
    }

    @Override
    public Result findRolesBySearch(RoleSearchParam param) {
        if (redisService.hasKey("RBAC_SYSTEM:WX_ROLE:ROLES")) {
            List<Role> resultRoles = new ArrayList<>();
            List<Role> roles = redisService.get("RBAC_SYSTEM:WX_ROLE:ROLES");
            long count = Long.valueOf((param.getPage().getCurrentPage() - 1) * param.getPage().getMaxSize());
            AtomicLong currentCount = new AtomicLong(0);
            roles.stream()
                    .filter(
                            role ->
                            {
                                if (StringUtil.isNotEmpty(param.getName())) {
                                    if (!role.getName().trim().toLowerCase().contains(param.getName().toLowerCase())) {
                                        return false;
                                    }
                                }
                                if (null != param.getStartTime()) {
                                    if (role.getCreateTime().before(param.getStartTime())) {
                                        return false;
                                    }
                                }
                                if (null != param.getEndTime()) {
                                    if (role.getCreateTime().after(param.getEndTime())) {
                                        return false;
                                    }
                                }
                                if (null != param.getAdminCountMin()) {
                                    if (role.getAdminCount() < (param.getAdminCountMin())) {
                                        return false;
                                    }
                                }
                                if (null != param.getAdminCountMax()) {
                                    if (role.getAdminCount() > (param.getAdminCountMax())) {
                                        return false;
                                    }
                                }
                                return true;
                            }
                    )
                    .forEach(role -> {
                        //incrementAndGet() 先加1再获取当前值
                        if (currentCount.incrementAndGet() > count) {
                            //将role放入 集合中 用户返回前端
                            if (resultRoles.size()<param.getPage().getMaxSize()){
                                resultRoles.add(role);
                            }
//                            resultRoles.add(role);
                        }

                    });
            PageInfo pageInfo = new PageInfo(resultRoles);
            pageInfo.setTotal(currentCount.get());
            Long pages = currentCount.get()/Long.valueOf(String.valueOf(param.getPage().getMaxSize()));
            pageInfo.setPages(pages.intValue());
            if (currentCount.get()%Long.valueOf(String.valueOf(param.getPage().getMaxSize()))>0){
                pages++;
                pageInfo.setPages(pages.intValue());
            }

            System.out.println("roles from redis");
            return Result.success(pageInfo);
        } else {
            PageHelper.startPage(param.getPage().getCurrentPage(), param.getPage().getMaxSize());
            List<Role> roles = roleMapper.findRolesBySearch(param);
            for (Role role : roles) {
                role.setPermissions(permissionService.findPermissionsByRoleId(role.getId()));
            }
            PageInfo pageInfo = new PageInfo<Role>(roles);

            List<Role> allRoles = roleMapper.findAllRoles();
            allRoles.stream().forEach(x->{
                x.setPermissions(permissionService.findPermissionsByRoleId(x.getId()));
            });

            redisService.set("RBAC_SYSTEM:WX_ROLE:ROLES",allRoles);
            System.out.println("Roles from jdbc");
            return Result.success(pageInfo);
        }
    }

    @Override
    public Integer updateRoleAndTeamLeaderRelation(Long oldTeamLeaderUserId, Long newTeamLeaderUserId) {

        return roleMapper.updateRoleAndTeamLeaderRelation(oldTeamLeaderUserId,newTeamLeaderUserId);
    }

    @Override
    public Integer updateRoleAndTeamLeaderRelation(Long newTeamLeaderUserId) {
        return roleMapper.addRoleTeamLeaderToUser(newTeamLeaderUserId);
    }


}
