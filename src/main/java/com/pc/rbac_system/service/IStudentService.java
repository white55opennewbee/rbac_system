package com.pc.rbac_system.service;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Student;

import java.util.List;

public interface IStudentService {
    Result findStudentsByTeamId(Long teamId,Integer currentPage,Integer maxSize);

    List<Student> findStudentsByTeamId(Long teamId);

    Result findNoTeamStudents(Integer currentPage,Integer maxSize);

    Result findStudentById(Long id);

    Result updateStudent(Student student);

    Result changeStudentTeam(Long stuId,Long teamId);

    List<Student> findAllStudentId();

    Boolean setStudentNoTeam(Long studentId);
}
