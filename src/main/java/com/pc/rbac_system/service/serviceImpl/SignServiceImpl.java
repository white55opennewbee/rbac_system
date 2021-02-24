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

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SignServiceImpl implements ISignService {
    @Autowired
    IRedisService redisService;
    @Autowired
    IStudentService studentService;
    @Autowired
    SignMapper signMapper;

    @Override
    public Result findTodaySignByTeamId(Long teamId, Integer currentPage, Integer maxSize) {
        if (redisService.hasKey("RBAC_SYSTEM:SIGN:TODAY:TEAM:"+teamId)){
//            List<Student> students = studentService.findStudentsByTeamId(teamId);
            Long startCount =  (currentPage-1L)*maxSize;
            List<Sign> result = new ArrayList<>();
            List<Sign> todayList = redisService.get("RBAC_SYSTEM:SIGN:TODAY:TEAM:"+teamId);
            AtomicLong count = new AtomicLong(0);
            todayList.stream().filter(x->{
                if (result.size()<maxSize){
                    return true;
                }
                return false;
            }).forEach(sign -> {
                if (count.addAndGet(1)>=startCount){
                        result.add(sign);
                }
            });

            return Result.success(result);
        }
        return Result.error(CodeMsg.SIGN_NOT_EXIST);

    }




    @Override
    public Integer putAll(List<Sign> signs) {
        Integer column = signMapper.putAll(signs);
        return column;
    }

    @Override
    public boolean SetTodaySignType(Integer type,Long studentId,Long teamId) {
        boolean set = false;
        if(redisService.hasKey("BAC_SYSTEM:SIGN:TODAY:TEAM:"+teamId)){
            List<Sign> signs = redisService.get("BAC_SYSTEM:SIGN:TODAY:TEAM:" + teamId);
            for (Sign sign:signs){
                if (sign.getStudentId().longValue() == studentId.longValue()){
                    sign.setLateTime(new Date(System.currentTimeMillis()));
                    sign.setSignIn(type);
//                    switch (type){
//                        case "签到" : sign.setSignIn(1);break;
//                        case "迟到" : sign.setLateTime(new Date(System.currentTimeMillis()));sign.setSignIn(2);break;
//                        case "请假" : sign.setSignIn(3);break;
//                        default:break;
//                    }
                }
            }
            set = redisService.set("BAC_SYSTEM:SIGN:TODAY:TEAM:" + teamId, signs);

        }else {
            System.out.println("无小组签到信息");
        }
        return set;
    }
}
