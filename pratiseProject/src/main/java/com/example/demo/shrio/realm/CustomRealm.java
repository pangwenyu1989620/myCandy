package com.example.demo.shrio.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    // 用于认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {

        if(token==null){
            return null;
        }
        // token是用户输入的
        // 第一步从token中取出身份信息
        String userCode = (String) token.getPrincipal();
        if(userCode==null){
            return null;
        }

        // 第二步：根据用户输入的userCode从数据库查询
        // ....


        // 如果查询不到返回null
        //数据库中用户账号是zhangsansan
        /*if(!userCode.equals("zhangsansan")){//
            return null;
        }*/


        // 模拟从数据库查询到密码
        String password = "111112";

        // 从数据库获取salt
        String salt = "qwerty";

        // 如果查询到返回认证信息AuthenticationInfo

//        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
//                userCode, password, this.getName());

        // 如果查询到返回认证信息AuthenticationInfo
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                userCode, password, ByteSource.Util.bytes(salt), this.getName());


        return simpleAuthenticationInfo;
    }

    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        // TODO Auto-generated method stub
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String token = principals.getPrimaryPrincipal().toString();
        if (token == null) {
            log.error("授权失败，用户信息为空！！！");
            return null;
        }
        //模拟数据端拿取数据
        String user01 = "admin";
        String role01 = "管理员";
        String permission01 = "管理员:操作";

        try {
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            roles.add(role01);
            permissions.add(permission01);
            simpleAuthorizationInfo.setRoles(roles);
            simpleAuthorizationInfo.setStringPermissions(permissions);
        }catch(Exception e){
            log.error("授权失败，请检查系统内部错误!!!", e);
        }
        return simpleAuthorizationInfo;
    }
}

