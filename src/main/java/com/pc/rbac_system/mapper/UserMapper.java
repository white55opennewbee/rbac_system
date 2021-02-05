package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.User;
import com.pc.rbac_system.vo.UserAndRoleParam;
import com.pc.rbac_system.vo.UserSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    User findUserByUsername(String username);

    void addUser(User user);

    List<User> findAllUserBySearch(UserSearchParam userSearchParam);

    Integer updateUser(User user);

    Integer deleteUser(Long id);

    Integer resaveUserById(Long id);

    Long findPagesBySearch(UserSearchParam param);

    User findUserByUserId(Long id);

    Integer InsertUserRoles(UserAndRoleParam param);

    Integer deleteUserRoles(Long userId);

    List<User> findusersByRole(String roleName);
}
