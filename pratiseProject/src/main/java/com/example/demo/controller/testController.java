package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import com.example.demo.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@Api(value = "testController", description = "test接口")
public class testController {

    @Autowired
    StudentService studentService;
    @Resource
    RedisUtil redisUtil;

    @GetMapping(value="/getStudentInfo")
    @ApiOperation(value="获取student信息")
    public List<Student> getStudentInfo(){
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        return studentService.list(queryWrapper);
    }

    @GetMapping(value="/setRedisAdminInfo")
    @ApiOperation(value="存放admin账号信息")
    public String setRedisAdminInfo(@RequestParam(value = "admin") String admin,@RequestParam(value = "adminPassword") String adminPassword){
        try {
            redisUtil.set("admin", admin, 0);
            redisUtil.set("adminPassword", adminPassword, 0);
            return "Redis存放Admin账号信息成功！";
        }catch(Exception e){
            return "RedisRedis存放Admin账号信息失败:"+e.getMessage();
        }
    }

    @GetMapping(value="/getRedisAdminInfo")
    @ApiOperation(value="获取admin账号信息")
    public Map<String,Object> getRedisAdminInfo(){
        String admin = redisUtil.get("admin").toString();
        String adminPasswork = redisUtil.get("adminPassword").toString();
        Map<String,Object> adminInfo = new HashMap<>();
        adminInfo.put("admin",admin);
        adminInfo.put("adminPassword",adminPasswork);
        return adminInfo;
    }

}
