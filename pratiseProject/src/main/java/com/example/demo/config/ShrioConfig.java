package com.example.demo.config;

import com.example.demo.shrio.realm.CustomRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro权限配置类
 */
@Configuration
public class ShrioConfig {
    public ShrioConfig() {
        System.out.println("ShiroConfig init ......");
    }

    /**
     * shiro过滤器配置
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);/*拦截器*/
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();/*权限配置*/
//        filterChainDefinitionMap.put("/user/adduser", "perms[ClinetUser:name]");/*配置不会被拦截的链接 顺序判断 相关静态资源*/
          filterChainDefinitionMap.put("/test/login-shiro", "anon");
          filterChainDefinitionMap.put("/test/loginAndGetJWT", "anon");
          filterChainDefinitionMap.put("/swagger-ui.html", "anon");
//        filterChainDefinitionMap.put("/assets/**", "anon");
//        filterChainDefinitionMap.put("/css/**", "anon");
//        filterChainDefinitionMap.put("/font/**", "anon");
//        filterChainDefinitionMap.put("/images/**", "anon");
//        filterChainDefinitionMap.put("/js/**", "anon");
//        filterChainDefinitionMap.put("/products/**", "anon");
//        filterChainDefinitionMap.put("/Widget/**", "anon");/*配置退出其中退出的代码shiro 已经帮我们实现了*/
//        filterChainDefinitionMap.put("/logout", "logout");/*所有的url都必须认证通过才可以访问*/
 //       filterChainDefinitionMap.put("/**", "authc");/*未授权界面*/
       filterChainDefinitionMap.put("/test/*", "authc");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 加密方式配置      
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
       hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    /**
     * 认证器配置    
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    /**
     * 安全管理配置    
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    private Realm CustomRelam(CredentialsMatcher matcher) {
        return customRealm();
    }

    /**
     * 开启@RequirePermission注解的配置  
     * @param securityManager    
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor auththorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor auththorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        auththorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return auththorizationAttributeSourceAdvisor;
    }

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     *  解决注解不生效的问题
     * @return
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }

}
