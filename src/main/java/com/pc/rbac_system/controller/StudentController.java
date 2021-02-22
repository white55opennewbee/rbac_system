package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Student;
import com.pc.rbac_system.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
//@PreAuthorize("hasAuthority('wx:student:read')")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/findStudentByTeamId/{teamId}/{currentPage}/{maxSize}")
    public Result findStudentsByTeamId(@PathVariable Long teamId,@PathVariable Integer currentPage,@PathVariable Integer maxSize){
      return studentService.findStudentsByTeamId(teamId,currentPage,maxSize);
    }

    @GetMapping("/findNoTeamStudents/{currentPage}/{maxSize}")
    public Result findNoTeamStudents(@PathVariable Integer currentPage,@PathVariable Integer maxSize){
        return  studentService.findNoTeamStudents(currentPage,maxSize);
    }

    @PostMapping("/moveStudentToTeam/{studentId}/{teamId}")
    public Result moveStudentToTeam(@PathVariable Long studentId,@PathVariable Long teamId){
        Result result = studentService.changeStudentTeam(studentId, teamId);
        return result;
    }


    @GetMapping("/findStudentById/{id}")
    public Result findStudentById(@PathVariable Long id){
        return studentService.findStudentById(id);
    }

    @GetMapping("/findStudentByUserId/{userId}")
    public Result findStudentByUserId(@PathVariable Long userId){
        Student student = studentService.findStudentByUserId(userId);
        return Result.success(student);
    }


    @PostMapping("/updateStudent")
    public Result updateStudent(@RequestBody Student student){
       return studentService.updateStudent(student);
    }

    @PutMapping("/setStudentNoTeam/{studentId}")
    public Result setStudentNoTeam(@PathVariable Long studentId){
       Boolean success =  studentService.setStudentNoTeam(studentId);
       return Result.success(success);
    }


}
