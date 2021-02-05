package com.pc.rbac_system.service.serviceImpl;

import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.mapper.SignMapper;
import com.pc.rbac_system.model.Sign;
import com.pc.rbac_system.service.IRedisService;
import com.pc.rbac_system.service.ISignService;
import com.pc.rbac_system.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SignServiceImpl implements ISignService {
    @Autowired
    IRedisService redisService;
    @Autowired
    IStudentService studentService;
    @Autowired
    SignMapper signMapper;

    @Override
    public Result findTodaySignByTeamId(Long teamId) {
        if (redisService.hasKey("RBAC_SYSTEM:SIGN:TODAY:TEAM:"+teamId)){
//            List<Student> students = studentService.findStudentsByTeamId(teamId);
            List<Sign> todayList = redisService.get("RBAC_SYSTEM:SIGN:TODAY:TEAM:"+teamId);
//            List<Sign> collect = todayList.stream().filter(sign -> {
//                for (Student s :students){
//                    if (s.getId().longValue() == sign.getStudentId().longValue()){
//                        students.remove(s);
//                        return true;
//                    }
//                }
//                return false;
//            }).collect(Collectors.toList());
            return Result.success(todayList);
        }
        return Result.error(CodeMsg.SIGN_NOT_EXIST);

    }




    @Override
    public Integer putAll(List<Sign> signs) {
        Integer column = signMapper.putAll(signs);
        return column;
    }

    @Override
    public boolean SetSignType(String type,Long studentId,Long teamId) {
        boolean set = false;
        if(redisService.hasKey("BAC_SYSTEM:SIGN:TODAY:TEAM:"+teamId)){
            List<Sign> signs = redisService.get("BAC_SYSTEM:SIGN:TODAY:TEAM:" + teamId);
            for (Sign sign:signs){
                if (sign.getStudentId().longValue() == studentId.longValue()){
                    switch (type){
                        case "签到" : sign.setSignIn(1);break;
                        case "迟到" : sign.setLateTime(new Date(System.currentTimeMillis()));sign.setSignIn(2);break;
                        case "请假" : sign.setSignIn(3);break;
                        default:break;
                    }
                }
            }
            set = redisService.set("BAC_SYSTEM:SIGN:TODAY:TEAM:" + teamId, signs);

        }else {
            System.out.println("无小组签到信息");
        }
        return set;
    }
}
