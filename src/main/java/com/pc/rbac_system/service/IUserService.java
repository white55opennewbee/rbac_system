package com.pc.rbac_system.service;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.vo.LoginParam;
import com.pc.rbac_system.vo.UserAndAuthentication;
import com.pc.rbac_system.vo.UserAndRoleParam;
import com.pc.rbac_system.vo.UserSearchParam;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    Result findUserByUsernameAndCheckPassword(LoginParam param) throws IOException;
    User findUserByUsername(String username);

    User addUser(User user);

    List<User> findAllBySearch(UserSearchParam userSearchParam);

    boolean updateUser(User user);

    boolean deleteUser(Long id);

    boolean updateUserAuthentication(UserAndAuthentication userAndAuthentication);

    Result findBanedAccount();

    Result deBannedAccountByUsername(String username);

    boolean resaveUserById(Long id);

    Long findPagesBySearch(UserSearchParam param);

    User findUserByUserId(Long id);

    Boolean updateUserRoles(UserAndRoleParam param);

    Result findusersByRole(String roleName);
}
