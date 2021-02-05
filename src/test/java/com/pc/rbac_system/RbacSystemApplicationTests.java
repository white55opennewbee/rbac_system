package com.pc.rbac_system;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.EsLoginInfo;
import com.pc.rbac_system.schedule.SignSchedule;
import com.pc.rbac_system.service.IESLoginInfoService;
import com.pc.rbac_system.service.IRoleService;
import com.pc.rbac_system.vo.Page;
import com.pc.rbac_system.vo.RoleSearchParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
class RbacSystemApplicationTests {
    @Autowired
    IRoleService roleService;
    @Autowired
    IESLoginInfoService service;
    @Test
    void contextLoads() {

        RoleSearchParam param = new RoleSearchParam();
        param.setPage(new Page());
        param.getPage().setCurrentPage(1);
        param.getPage().setMaxSize(2);
        param.setName("学");
        Result rolesBySearch = roleService.findRolesBySearch(param);
        System.out.println(rolesBySearch.getData());

    }

    @Test
    void ESPutTest(){
        EsLoginInfo info = new EsLoginInfo();
        info.setUsername("法外狂徒");
        info.setNickName("哈皮");
        info.setAddr("成都武侯区");
        info.setIp("127.0.0.1");
        info.setLoginTime(new Date(System.currentTimeMillis()));
        try {
            service.putEsLogin(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void ESGetTest(){
        try {
            service.search(null,1,2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    SignSchedule signSchedule;
    @Test
    void SignTest(){
        signSchedule.createSign();
    }

}
