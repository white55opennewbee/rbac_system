package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.service.IRoleService;
import com.pc.rbac_system.vo.RoleSearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/role")
@RestController
@Api("角色接口")
@PreAuthorize("hasAuthority('wx:role:read')")
public class RoleController {
    @Autowired
    IRoleService roleService;

    @GetMapping("/findAllRoles")
    @ApiOperation("查询所有角色")
    @PreAuthorize("hasAuthority('wx:role:read')")
    public Result findAllRoles(){
        Result allRoles = roleService.findAllRoles();
        return allRoles;
    }
    @PostMapping("/findAllRolesBySearch")
    @ApiOperation("按条件分页查询所有角色")
    @PreAuthorize("hasAuthority('wx:role:read')")
    public Result findRolesBySearch(@RequestBody RoleSearchParam param){
       Result result =  roleService.findRolesBySearch(param);
       return result;
    }

    @PostMapping("/updateRole")
    @ApiOperation("更新角色")
    @PreAuthorize("hasAuthority('wx:role:update')")
    public Result updateRoleById(@RequestBody Role role){
        Result result = roleService.updateRole(role);
        return result;
    }

    @DeleteMapping("/deleteRoleByRoleId/{id}")
    @PreAuthorize("hasAuthority('wx:role:delete')")
    public Result deleteRoleById(@PathVariable("id") Long id){
        Result result = roleService.deleteRoleById(id);
        return result;
    }

    @GetMapping("/findRoleByRoleId/{id}")
    @PreAuthorize("hasAuthority('wx:role:read')")
    public Result findRoleByRoleId(@PathVariable("id")Long id){
        Result roleById = roleService.findRoleById(id);
        return roleById;
    }

    @PostMapping("/addRoleWithAuthentications")
    @PreAuthorize("hasAuthority('wx:role:create')")
    public Result addRole(@RequestBody Role role){
        role.setCreateTime(new Date(System.currentTimeMillis()));
        Result result = roleService.addRole(role);
        return result;
    }

}
