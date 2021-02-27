package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.ExportWord;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.common.WordHtmlGeneratorHelper;
import com.pc.rbac_system.dto.TodayDailyPutState;
import com.pc.rbac_system.mapper.DailyMapper;
import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.model.Student;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.service.IDailyService;
import com.pc.rbac_system.service.IStudentService;
import com.pc.rbac_system.service.ITeamService;
import com.pc.rbac_system.service.IUserService;
import com.pc.rbac_system.vo.DailySearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DailyServiceImpl implements IDailyService {
    @Autowired
    DailyMapper dailyMapper;
    @Autowired
    IStudentService studentService;
    @Autowired
    ITeamService teamService;
    @Autowired
    IUserService userService;

    @Override
    public PageInfo findAllDailyBySearch(DailySearchParam param) {
        PageHelper.startPage(param.getPage().getCurrentPage(),param.getPage().getMaxSize());
        List<Daily> allDailyByStudentId = dailyMapper.findAllDailyBySearch(param);
        PageInfo info = new PageInfo(allDailyByStudentId);
        return info;
    }

    @Override
    public Result updateDaily(Daily daily) {
        Integer effect = dailyMapper.updateDaily(daily);
        if (effect>0){
            return Result.success(true);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    @Transactional
    public Result addDaily(Daily daily, String username){
        daily.setCreateTime(new Date(System.currentTimeMillis()));
        dailyMapper.addDaily(daily);
        Integer integer = dailyMapper.insertWxUserDailyRelation(username, daily.getId());
        return Result.success(integer>0);
    }

    @Override
    public Result deleteDaily(Long dailyId) {
        Integer effect = dailyMapper.deleteDailyById(dailyId);
        return Result.success(effect>0);
    }

    @Override
    public Result updateDailyStatus(String statusName, Long dailyId) {
        Integer integer;
        if (statusName.equalsIgnoreCase("已提交")){
             integer = updateDailyStatus(statusName,dailyId,new Date(System.currentTimeMillis()));
        }else {
             integer = dailyMapper.updateDailyStatus(dailyId,statusName);
        }

        return Result.success(integer>0);
    }

    @Override
    public Integer updateDailyStatus(String statusName, Long dailyId, Date date) {
        return dailyMapper.updateDailyStatusAndPutTime(statusName,dailyId,date);
    }

    @Override
    public Result findDailyByDailyId(Long id) {
        Daily daily = dailyMapper.findDailyByDailyId(id);
        return Result.success(daily);
    }

    @Override
    public PageInfo findDailyByTeacherId(Long teacherId,DailySearchParam dailySearchParam) {
        PageHelper.startPage(dailySearchParam.getPage().getCurrentPage(),dailySearchParam.getPage().getMaxSize());
        List<Daily> dailies = dailyMapper.findDailyByTeacherId(teacherId,dailySearchParam);
        PageInfo pageInfo = new PageInfo(dailies);
        return pageInfo;
    }

    @Override
    public PageInfo findTodayDailyPutStatus(Long teacherId, Integer currentPage, Integer maxSize) {

        PageInfo students = studentService.findStudentsByTeacherId(teacherId,currentPage,maxSize);
        List<TodayDailyPutState> todayDailies = new ArrayList<>();

        DailySearchParam param = new DailySearchParam();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = format.parse(format.format(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        param.setStartTime(parse);
        for (Object s: students.getList()) {

            Daily daily = dailyMapper.findDailyByUserId(((Student)s).getUserId(),param);
            TodayDailyPutState todayDailyPutState = new TodayDailyPutState();
            todayDailyPutState.setStudent((Student) s);
            todayDailyPutState.setDaily(daily);
            todayDailies.add(todayDailyPutState);
        }
        students.setList(todayDailies);

        return students;
    }

    @Override
    public void CreateDailyWord(Long dailyId, HttpServletResponse response) {
        Map<String,Object> map = new HashMap();
        Daily daily = dailyMapper.findDailyByDailyId(dailyId);
        Student student = studentService.findStudentByDailyId(dailyId);
        Result teamById = teamService.findTeamById(student.getTeamId());
        Team team = (Team) teamById.getData();
        User coach = userService.findUserByUserId(team.getCoachId());
        User teacher = userService.findTeacherByTeamId(team.getId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(daily.getDailyTime());
        map.put("teacherName",teacher.getUsername());
        map.put("coachName",coach.getUsername());
        map.put("dailyTitleValue",daily.getDailyTitle());
        map.put("body",daily.getDailyBody());
        map.put("studentNameValue", student.getStudentName());
        map.put("teamNameValue",team.getTeamName());

        map.put("subDateValue",WordHtmlGeneratorHelper.string2Ascii(format));
        ExportWord exportWord = new ExportWord();
        exportWord.createWord(map,response);

    }


}
