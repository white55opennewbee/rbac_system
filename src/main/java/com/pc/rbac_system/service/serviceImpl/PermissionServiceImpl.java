package com.pc.rbac_system.service.serviceImpl;

import com.pc.rbac_system.mapper.PermissionMapper;
import com.pc.rbac_system.model.Permission;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.service.IPermissionService;
import com.pc.rbac_system.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    IRedisService redisService;
    @Value("${jwt.expiration}")
    Long redisPermissionInfoExpirationTime;

    @Override
    public List<Permission> findPermissionsByUserId(Long userId) {
        List<Permission> list;
        if (redisService.hasKey("RBAC_SYSTEM:WX_PERMISSION:LOGIN:INFO:" + userId)) {
            list = redisService.get("RBAC_SYSTEM:WX_PERMISSION:LOGIN:INFO:" + userId);
            System.out.println("tokenCheckUserPermission from redis");
        } else {
            list = permissionMapper.findPermissionsByUserId(userId);
            redisService.setWithExpire("RBAC_SYSTEM:WX_PERMISSION:LOGIN:INFO:" + userId, list,redisPermissionInfoExpirationTime);
            System.out.println("tokenCheckUserPermission from redis");
        }
        return list;
    }



    @Override
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        List<Permission> list = permissionMapper.findPermissionsByRoleId(roleId);
        return list;
    }

    @Override
    public List<Permission> findAllPermission() {
        List<Permission> permissions;
        if (redisService.hasKey("RBAC-SYSTEM:WX_PERMISSION:AllPERMISSIONS")){
             permissions = redisService.get("RBAC-SYSTEM:WX_PERMISSION:AllPERMISSIONS");
        }else {
            permissions = permissionMapper.findAllPermissions();
            redisService.set("RBAC-SYSTEM:WX_PERMISSION:AllPERMISSIONS",permissions);
        }
//        List<Permission> list = permissionMapper.findAllPermissions();
        return permissions;
    }

    @Override
    @Transactional
    public Integer updateRolePermissions(Role role) {
        Integer del = permissionMapper.deletePermissionByRole(role);
        if (role.getPermissions().size()>0){
            permissionMapper.insertPermissionsByRole(role);
        }
        return del;

    }


}
