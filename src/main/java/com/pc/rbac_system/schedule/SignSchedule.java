package com.pc.rbac_system.schedule;

import com.pc.rbac_system.model.Sign;
import com.pc.rbac_system.model.Student;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.service.IRedisService;
import com.pc.rbac_system.service.ISignService;
import com.pc.rbac_system.service.IStudentService;
import com.pc.rbac_system.service.ITeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Component
public class SignSchedule {
    @Autowired
    IRedisService redisService;
    @Autowired
    IStudentService studentService;
    @Autowired
    ISignService signService;
    @Autowired
    ITeamService teamService;


    @Scheduled(cron = "0 0 6 ? * 1,2,3,4,5")
    public void createSign(){
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



    @Scheduled(cron = "0 0 10 ? * 1,2,3,4,5")
    public void submitSign(){
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
