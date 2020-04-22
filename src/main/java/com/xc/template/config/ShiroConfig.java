package com.xc.template.config;

import com.xc.template.common.security.shiro.session.CacheSessionDAO;
import com.xc.template.common.security.shiro.session.SessionManager;
import com.xc.template.system.security.*;
import com.xc.template.system.service.SystemService;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: xuchang
 * @date: 2019/10/18
 */
@Configuration
public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager")SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/index");
        // 配置拦截器
        Map<String, String> filterMap = new LinkedHashMap<>(3);
        filterMap.put("/static/**","anon");
        filterMap.put("/login","authc");
        filterMap.put("/error","anon");
        filterMap.put("/logout","logout");
        filterMap.put("/**","user");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        // 自定义拦截器
        Map<String, Filter> customFilter = new LinkedHashMap<>(4);
        // 登录拦截器
        customFilter.put("authc", new FormAuthenticationFilter());
        // 添加到shiro过滤器中
        shiroFilter.setFilters(customFilter);
        return shiroFilter;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("shiroCacheManager") EhCacheManager shiroCacheManager,
                                           @Qualifier("sessionManager") SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(shiroCacheManager);
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

    @Bean("shiroCacheManager")
    public EhCacheManager shiroCacheManager(CacheManager cacheManager) {
        EhCacheManager shiroCacheManager = new EhCacheManager();
        shiroCacheManager.setCacheManager(cacheManager);
        return shiroCacheManager;
    }

    @Bean
    public Realm customRealm(){
        SystemAuthorizingRealm systemRealm = new SystemAuthorizingRealm();
        // 设定密码校验的Hash算法与迭代次数
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
        matcher.setHashIterations(SystemService.HASH_INTERATIONS);
        systemRealm.setCredentialsMatcher(matcher);
        return systemRealm;
    }

    @Bean("sessionManager")
    public SessionManager sessionManager(@Qualifier("sessionDao") SessionDAO sessionDao) {
        SessionManager shiroSession = new SessionManager();
        // 多tomcat部署,使用shiro-redis开源插件管理session,或者nginx
        shiroSession.setSessionDAO(sessionDao);
        shiroSession.setSessionIdCookie(new SimpleCookie("app.session"));
        shiroSession.setSessionIdCookieEnabled(true);
        // 会话超时时间,毫秒
        shiroSession.setGlobalSessionTimeout(30*60*1000);
        return shiroSession;
    }


    @Bean
    public RememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        // 7天 = 604800s = 7*24*60*60
        cookie.setMaxAge(604800);
        cookieRememberMeManager.setCookie(cookie);
        return cookieRememberMeManager;
    }

    @Bean("sessionDao")
    public SessionDAO sessionDao(@Qualifier ("shiroCacheManager")EhCacheManager shiroCacheManager) {
        CacheSessionDAO sessionDAO = new CacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        sessionDAO.setCacheManager(shiroCacheManager);
        return sessionDAO;
    }

    /**
     * 开启AOP注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager")SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}
