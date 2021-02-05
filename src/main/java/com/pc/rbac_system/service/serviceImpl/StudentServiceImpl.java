package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.mapper.AllocateRecordMapper;
import com.pc.rbac_system.mapper.StudentMapper;
import com.pc.rbac_system.mapper.TeamMapper;
import com.pc.rbac_system.model.AllocateRecord;
import com.pc.rbac_system.model.Student;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.service.IStudentService;
import com.pc.rbac_system.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    AllocateRecordMapper allocateRecordMapper;
    @Autowired
    TeamMapper teamMapper;

    @Override
    public Result findStudentsByTeamId(Long teamId,Integer currentPage,Integer maxSize) {
        PageHelper.startPage(currentPage,maxSize);
        List<Student> studentList = studentMapper.findStudentsByTeamId(teamId);
        PageInfo info = new PageInfo(studentList);
        return Result.success(info);
    }

    @Override
    public List<Student> findStudentsByTeamId(Long teamId) {
        List<Student> studentList = studentMapper.findStudentsByTeamId(teamId);
        return studentList;
    }

    @Override
    public Result findNoTeamStudents(Integer currentPage,Integer maxSize) {
        PageHelper.startPage(currentPage,maxSize);
        List<Student> students = studentMapper.findNoTeamStudents();
        PageInfo info = new PageInfo(students);
        return Result.success(info);
    }

    @Override
    public Result findStudentById(Long id) {
        Student student = studentMapper.findStudentById(id);
        return Result.success(student);
    }

    @Override
    public Result updateStudent(Student student) {
        Integer integer = studentMapper.updateStudent(student);
        return Result.success(integer>0);
    }

    @Override
    @Transactional
    public Result changeStudentTeam(Long stuId, Long teamId) {
        studentMapper.changeStudentTeam(stuId,teamId);
        Team newTeam = teamMapper.findTeamById(teamId);
        Team oldTeam = teamMapper.findTeamById(studentMapper.findStudentById(stuId).getTeamId());
        AllocateRecord allocateRecord  = new AllocateRecord();
        allocateRecord.setOldTeam(oldTeam.getTeamName());
        allocateRecord.setNewTeam(newTeam.getTeamName());
        allocateRecord.setStudentId(stuId);
        allocateRecord.setCreateTime(new Date(System.currentTimeMillis()));
        Integer integer = allocateRecordMapper.createAllocateRecord(allocateRecord);
        return Result.success(integer>0);
    }

    @Override
    public List<Student> findAllStudentId() {
       List<Student> longs =  studentMapper.findAllStudentId();
       return longs;
    }
}
