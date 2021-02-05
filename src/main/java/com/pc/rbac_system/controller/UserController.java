package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.dto.UsersAndRoles;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.service.IRoleService;
import com.pc.rbac_system.service.IUserService;
import com.pc.rbac_system.vo.UserAndAuthentication;
import com.pc.rbac_system.vo.UserAndRoleParam;
import com.pc.rbac_system.vo.UserSearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@CrossOrigin
@RestController
@Api("用户接口")
@RequestMapping("/user")
@PreAuthorize("hasAuthority('wx:user:read')")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;


    @ApiOperation("根据查询信息查询用户")
    @PreAuthorize("hasAuthority('wx:user:read')")
    @PostMapping("/findAllBySearch")
    public Result findAllUserBySearch(@RequestBody UserSearchParam param) {
        List<UsersAndRoles> list = new ArrayList<>();
        List<User> allBySearch = userService.findAllBySearch(param);
        for (User user : allBySearch) {
            UsersAndRoles usersAndRoles = new UsersAndRoles();
            List<Role> roles = roleService.findRolesByUserId(user.getId());
            usersAndRoles.setUser(user);
            usersAndRoles.setRoles(roles);
            list.add(usersAndRoles);
        }
        return Result.success(list);
    }

    @ApiOperation("根据查询信息查询用户总量")
    @PreAuthorize("hasAuthority('wx:user:read')")
    @PostMapping("/findPagesBySearch")
    public Result findPagesBySearch(@RequestBody UserSearchParam param) {
        Long pages = userService.findPagesBySearch(param);
        return Result.success(pages);
    }


    @ApiOperation("更新用户信息")
    @PreAuthorize("hasAuthority('wx:user:update')")
    @PostMapping("/update")
    public Result updateUser(@RequestBody User user) {
        boolean effict = userService.updateUser(user);
        return Result.success(effict);
    }

    /***
     * @deprecated 用户管理员对于用户进行删除操作
     * @param id
     * @return
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('wx:user:delete')")
    public Result deleteUser(@RequestParam("id") Long id) {
        boolean b = userService.deleteUser(id);
        return Result.success(b);
    }

    /***
     * @deprecated 用户管理员对用户额外权限进行更新
     * @param userAndAuthentication
     * @return
     */
    @ApiOperation("更新用户额外权限")
    @PostMapping("/updateAuthentication")
    @PreAuthorize("hasAuthority('wx:user:updateAuthentication')")
    public Result updateAuthentication(@RequestBody UserAndAuthentication userAndAuthentication) {
        boolean effict = userService.updateUserAuthentication(userAndAuthentication);
        return Result.success(effict);
    }

    @ApiOperation("查询被冻结24h的用户")
    @GetMapping("/findBanedAccount")
    @PreAuthorize("hasAuthority('wx:user:read')")
    public Result findBannedAccount() {
        Result result = userService.findBanedAccount();
        return result;
    }


    @ApiOperation("解冻被冻结24h的用户")
    @GetMapping("/deBannedAccountByUsername/{username}")
    @PreAuthorize("hasAuthority('wx:user:update')")
    public Result deBannedAccountByUsername(@PathVariable("username") String username) {
        Result result = userService.deBannedAccountByUsername(username);
        return result;
    }

    @ApiOperation("封禁用户")
    @DeleteMapping("/banUserById/{id}")
    @PreAuthorize("hasAuthority('wx:user:delete')")
    public Result banUserById(@PathVariable("id") Long id) {
        boolean b = userService.deleteUser(id);
        if (b) {
            return Result.success(b);
        }
        return Result.error(CodeMsg.SERVER_ERROR);

    }

    @ApiOperation("解封用户")
    @DeleteMapping("/deBanUserById/{id}")
    @PreAuthorize("hasAuthority('wx:user:delete')")
    public Result deBanUserById(@PathVariable("id") Long id) {
        boolean b = userService.resaveUserById(id);
        if (b) {
            return Result.success(b);
        }
        return Result.error(CodeMsg.SERVER_ERROR);

    }

    @ApiOperation("根据用户id查找用户以及用户角色")
    @GetMapping("/findUserByUserId/{id}")
    @PreAuthorize("hasAuthority('wx:user:read')")
    public Result findUserByUserId(@PathVariable Long id) {
        HashMap map = new HashMap();
        User user = userService.findUserByUserId(id);
        List<Role> rolesByUserId = roleService.findRolesByUserId(id);
        map.put("user",user);
        map.put("roles",rolesByUserId);
        if (null != user) {
            return Result.success(map);
        } else {
            return Result.error(CodeMsg.INFO_NOT_EXITS);
        }

    }

    @ApiOperation("设置用户角色")
    @PostMapping("/updateUserRoles")
    @PreAuthorize("hasAuthority('wx:user:updateAuthentication')")
    public Result updateUserRoles(@RequestBody UserAndRoleParam param){
        boolean a = userService.updateUser(param.getUser());
        boolean b = userService.updateUserRoles(param);
        if (a&&b){
            return Result.success(a);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }


}
