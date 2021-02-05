package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {
    List<Student> findStudentsByTeamId(Long teamId);

    List<Student> findNoTeamStudents();

    Student findStudentById(Long id);

    Integer updateStudent(Student student);

    Integer addStudent(Student student);

    Integer changeStudentTeam(@Param("stuId") Long stuId,@Param("teamId") Long teamId);

    List<Student> findAllStudentId();
}
