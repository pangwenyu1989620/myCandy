package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@Api(value = "testController", description = "test接口")
public class testController {

    @Autowired
    StudentService studentService;

    @GetMapping(value="/getStudentInfo")
    public List<Student> getStudentInfo(){
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        return studentService.list(queryWrapper);
    }

}
