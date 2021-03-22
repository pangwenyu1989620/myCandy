package com.example.demo.shrio.realm;

import com.example.demo.util.JWTToken;
import com.example.demo.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Resource
    RedisUtil redisUtil;

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    // 用于认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        //如果客户已经登录，考虑分布式服务情况，还需到redis检验token是否存在,存在则比较是否相等相等即为同一次登陆，如果是同一次登陆，允许该token进行其他接口访问
        //如果没有token说明是第一次登陆，允许做后面的校验
        JWTToken jwtToken = (JWTToken) token;
        String username = jwtToken.getUsername();
        boolean hasKey = redisUtil.hasKey(username);
        if(hasKey) {
            String redisToken = redisUtil.get(username).toString();
            if (!redisToken.equalsIgnoreCase(token.getCredentials().toString())) {
                return null;
            }
        }

        if(token==null){
            return null;
        }
        // token是用户输入的
        // 第一步从token中取出身份信息
        String userCode = username;
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


        // 模拟从数据库根据userCode查询到密码(使用jwttoken可以直接根据token获取密码，无需去数据库取)
//        try {
//            String password = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("pwy")).parseClaimsJws(token.getCredentials().toString()).getBody().get("password").toString();
//        } catch (ExpiredJwtException e) {
//            log.error("jwt 过期！", e);
//            return null;
//        } catch (SignatureException e) {
//            log.error("jwt 解码失败！", e);
//            return null;
//        } catch (Exception e) {
//            log.error("jwt 未知异常！",e);
//            return null;
//        }

        String password = jwtToken.getCredentials().toString();//取token作为密码，应对assertCredentialsMatch报错

        // 从数据库获取salt
        String salt = "pwy";

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

        String user01 = token;
        System.out.println("principal获取的用户名为："+user01);
        //模拟数据端根据用户名拿取数据
        String role01 = user01;
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

    public boolean supports(AuthenticationToken authenticationToken) {
        return true;
    }
}

