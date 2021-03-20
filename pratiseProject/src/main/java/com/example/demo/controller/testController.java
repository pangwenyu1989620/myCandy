package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import com.example.demo.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value="测试redis存放admin账号信息")
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
    @ApiOperation(value="测试redis获取admin账号信息")
    public Map<String,Object> getRedisAdminInfo(){
        String admin = redisUtil.get("admin").toString();
        String adminPasswork = redisUtil.get("adminPassword").toString();
        Map<String,Object> adminInfo = new HashMap<>();
        adminInfo.put("admin",admin);
        adminInfo.put("adminPassword",adminPasswork);
        return adminInfo;
    }

    @RequestMapping(path = "/login-shiro", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="shrio测试用户登录")
    public String loginShiro(String username,String password) {
        Subject subject = SecurityUtils.getSubject();//获取连接
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);//身份验证
        try {
            subject.login(token);//正确  --realm
        }catch(Exception e){
            System.out.println("登陆异常："+e.getMessage());
            return "登陆异常："+e.getMessage();
        }
        return "登陆成功！+token:"+token;
    }


    //@RequiresPermissions("管理员:操作")
    @RequiresRoles("管理员")
    @GetMapping(value="/getShiroPermissionTest")
    @ApiOperation(value="测试shiro权限管控")
    public String getShiroPermissionTest(){

        return "您好，您已成功获取shrio管理的服务访问权限！";
    }

}
