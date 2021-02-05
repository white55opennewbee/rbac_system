package com.pc.rbac_system.controller;

import com.github.pagehelper.PageHelper;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.service.IPermissionService;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@PreAuthorize("hasAuthority('wx:role:read')")
public class PermissionController {
    @Autowired
    IPermissionService permissionService;

    @GetMapping("/findAllPermissions")
    public Result findAllPermission(){
       return Result.success(permissionService.findAllPermission());
    }

    @PostMapping("/updateRolePermissions")
    @PreAuthorize("hasAuthority('wx:role:update')")
    public Result updateRolePermissions(){
        return null;
    }

    @GetMapping("/findPermissionByRoleId/{roleId}")
    public Result findPermissionByRoleId(@PathVariable("roleId")Long id){
        return Result.success(permissionService.findPermissionsByRoleId(id));
    }

}
