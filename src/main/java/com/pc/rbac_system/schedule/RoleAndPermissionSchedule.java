package com.pc.rbac_system.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Permission;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.service.IPermissionService;
import com.pc.rbac_system.service.IRedisService;
import com.pc.rbac_system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoleAndPermissionSchedule {

    @Autowired
    IRoleService roleService;
    @Autowired
    IRedisService redisService;
    @Autowired
    IPermissionService permissionService;

    /***
     *
     * @deprecated 每小时更新redis中角色信息
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void UpdateAllRolesAndPermissionsInRedisPerHour() {
        redisService.del("RBAC_SYSTEM:WX_ROLE:ROLES","RBAC-SYSTEM:WX_PERMISSION:ALLPERMISSIONS");

        Result allRoles = roleService.findAllRoles();
//        List<Role> roles = (List<Role>) allRoles.getData();
//        roles.stream().forEach(x -> {
//            List<Permission> permissionsByRoleId = permissionService.findPermissionsByRoleId(x.getId());
//            HashSet<Permission> permissions = CollectionUtil.newHashSet(permissionsByRoleId);
//            x.setPermissions(permissions);
//        });
//        redisService.set("RBAC_SYSTEM:WX_ROLE:ROLES", roles);


        List<Permission> permissions = permissionService.findAllPermission();
//        redisService.set("RBAC-SYSTEM:WX_PERMISSION:ALLPERMISSIONS",permissions);

    }


}
