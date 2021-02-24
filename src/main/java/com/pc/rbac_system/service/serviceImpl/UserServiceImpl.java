package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.JwtTokenUtil;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.mapper.UserMapper;
import com.pc.rbac_system.mapper.UserPermissionRelationMapper;
import com.pc.rbac_system.model.EsLoginInfo;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.service.IESLoginInfoService;
import com.pc.rbac_system.service.IRedisService;
import com.pc.rbac_system.service.IUserService;
import com.pc.rbac_system.vo.LoginParam;
import com.pc.rbac_system.vo.UserAndAuthentication;
import com.pc.rbac_system.vo.UserAndRoleParam;
import com.pc.rbac_system.vo.UserSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/***
 * @author pc
 * @date 2021/1/20
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserPermissionRelationMapper userPermissionRelationMapper;
    @Autowired
    IRedisService redisService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    IESLoginInfoService esLoginInfoService;


    @Value("${jwt.tokenHeader}")
    String tokenHeader;
    @Value("${jwt.tokenHead}")
    String tokenHead;
    @Value("${jwt.expiration}")
    Long redisUserInfoExpirationTime;
    @Override
    public Result findUserByUsernameAndCheckPassword(LoginParam param) throws IOException {
        // 是否存在key
        if (redisService.hasKey("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername())){
            int count = (int)redisService.get("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername());
        //每次登录对当前账户的登录次数 做缓存 登录60分钟内 登录错误次数超过3次，账户锁定24小时
        if (count > 2){
            // 等于3 时代表开始冻结账户，设置过期时间为24小时，并且对 value+1
            if (count == 3){
                redisService.setWithExpire("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername(),count+1,3600L*24);
            }
            return Result.errorWithData(CodeMsg.USER_LOGIN_LOCK,redisService.getExpireTime("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername()));
        }
        redisService.setWithExpire("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername(),count+1,3600L);
    }else {
        redisService.setWithExpire("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername(),1,3600L);
    }


        User userByUsername = this.findUserByUsername(param.getUsername());
        //登录成功 删除redis中的账户登录次数计数，创建token,将用户存入redis
        if (null != userByUsername&&passwordEncoder.matches(param.getPassword(),userByUsername.getPassword())){
            redisService.del("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:"+param.getUsername());
            redisService.setWithExpire("RBAC_SYSTEM:WX_USER:LOGIN:USERINFO:"+userByUsername.getUsername(),userByUsername,redisUserInfoExpirationTime);
           //创建token
            String s = jwtTokenUtil.generatedToken(userByUsername);
            //token存入map中用于返回
            Map map = new HashMap();
            map.put("tokenHeader",tokenHeader);
            map.put("token",s);
            map.put("tokenHead",tokenHead);
            map.put("username",userByUsername.getUsername());
            map.put("id",userByUsername.getId());

            return Result.tokenSuccess(map);
        }else {
            //登录失败返回账户或密码错误
            return Result.error(CodeMsg.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        }
    }




    @Override
    public User findUserByUsername(String username) {
        User user;
        if (redisService.hasKey("RBAC_SYSTEM:WX_USER:LOGIN:USERINFO:"+username)){
            user = redisService.get("RBAC_SYSTEM:WX_USER:LOGIN:USERINFO:" + username);
            System.out.println("tokenCheckUser from redis");
        }else {
            user =  userMapper.findUserByUsername(username);
            if (null!=user){
                redisService.setWithExpire("RBAC_SYSTEM:WX_USER:LOGIN:USERINFO:"+user.getUsername(),user,redisUserInfoExpirationTime);
                System.out.println("tokenCheckUser from jdbc");
            }
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        userMapper.addUser(user);
        return user;
    }

    @Override
    public List<User> findAllBySearch(UserSearchParam userSearchParam) {
        PageHelper.startPage(userSearchParam.getPage().getCurrentPage(),userSearchParam.getPage().getMaxSize());
        List<User> users = userMapper.findAllUserBySearch(userSearchParam);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        //判断密码是否修改，修改则加密，未修改不加密
        User userByUserId = userMapper.findUserByUserId(user.getId());
        if (user.getPassword().equals(userByUserId)||passwordEncoder.matches(user.getPassword(),userByUserId.getPassword())){
            user.setPassword(null);
        }
        Integer effect = userMapper.updateUser(user);
        return effect>0;
    }

    @Override
    public boolean deleteUser(Long id) {
        Integer integer = userMapper.deleteUser(id);
        return integer>0;
    }

    @Override
    public boolean updateUserAuthentication(UserAndAuthentication userAndAuthentication) {
        Integer num = 0;
        Integer integer = userPermissionRelationMapper.deleteUserPermission(userAndAuthentication);
        if (integer>0){
            num = userPermissionRelationMapper.setPermission(userAndAuthentication);
        }
        return num>0;
    }

    @Override
    public Result findBanedAccount() {
        Set<String> set = redisService.patternKeys("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:");
        List<User> users = new LinkedList<>();
        for (String x: set) {
            Integer count = redisService.get(x);
            if (count>3){
                String username = x.substring(x.lastIndexOf(":") + 1);
                User userByUsername = userMapper.findUserByUsername(username);
                if (userByUsername!=null){
                    users.add(userByUsername);
                }
            }
        }
        return Result.success(users);
    }

    @Override
    public Result deBannedAccountByUsername(String username) {
        boolean del = false;
        if (redisService.hasKey("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:" + username)){
             del = redisService.del("RBAC_SYSTEM:WX_USER:LOGIN:USERNAME:" + username);
        }
        return Result.success(del);
    }

    @Override
    public boolean resaveUserById(Long id) {
        Integer res = userMapper.resaveUserById(id);
        return res>0;
    }

    @Override
    public Long findPagesBySearch(UserSearchParam param) {
       return  userMapper.findPagesBySearch(param);
    }

    @Override
    public User findUserByUserId(Long id) {
        User userByUserId = userMapper.findUserByUserId(id);
        return userByUserId;
    }

    @Override
    @Transactional
    public Boolean updateUserRoles(UserAndRoleParam param) {
        try {
            Integer re = userMapper.deleteUserRoles(param.getUser().getId());
            Integer res = userMapper.InsertUserRoles(param);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public Result findusersByRole(String roleName) {
        List<User> users = userMapper.findusersByRole(roleName);
        return Result.success(users);
    }

    @Override
    public List<User> findAllByRoleName(String roleName) {
        return userMapper.findAllByRoleName(roleName);
    }

    @Override
    public User findTeacherByTeamId(Long id) {
        return userMapper.findTeacherByTeamId(id);
    }
}
