package com.pc.rbac_system;

import com.pc.rbac_system.common.ExportWord;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.EsLoginInfo;
import com.pc.rbac_system.model.Sign;
import com.pc.rbac_system.model.Student;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.schedule.SignSchedule;
import com.pc.rbac_system.service.*;
import com.pc.rbac_system.vo.Page;
import com.pc.rbac_system.vo.RoleSearchParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
class RbacSystemApplicationTests {
    @Autowired
    IRoleService roleService;
    @Autowired
    IESLoginInfoService service;
    @Autowired
    ITeamService teamService;
    @Autowired
    IStudentService studentService;
    @Autowired
    IRedisService redisService;
    @Autowired
    ISignService signService;

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

    @Test
    void testWord(){

    }


    @Test
    void todaySignTest(){
        List<Team> allTeam = teamService.findAllTeam();
        allTeam.stream().forEach(team -> {
            List<Student> list = studentService.findStudentsByTeamId(team.getId());
            List<Sign> signs = new ArrayList<>();
            for (Student student:list){
                Sign sign = new Sign();
                sign.setStudentId(student.getId());
                sign.setSignIn(0);
                sign.setStudentName(student.getStudentName());
                sign.setCreateTime(new Date(System.currentTimeMillis()));
                signs.add(sign);
            }
            redisService.set("RBAC_SYSTEM:SIGN:TODAY:TEAM:"+team.getId(),signs);
        });
    }

    @Test
    void todatSignPutTest(){
        List<Sign> allSigns = new ArrayList<>();
        Set<String> keys = redisService.patternKeys("RBAC_SYSTEM:SIGN:TODAY:TEAM:");
        if (keys!=null&&keys.size()>0){
            keys.stream().forEach(x->{
                List<Sign> signs = redisService.get(x);
                signs.stream().forEach(y->{
                    if (y.getSignIn() == 0){
                        y.setSignIn(4);// 标记旷课
                    }
                });
                allSigns.addAll(signs);
            });
            Integer integer = signService.putAll(allSigns);
            redisService.del("RBAC_SYSTEM:SIGN:TODAY:TEAM");
        }else {
            System.out.println("今日无签到");
        }
    }

}
