package com.xc.template.config;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CacheConfig {

    @Bean("ehCacheManagerFactoryBean")
    @Profile("dev")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBeanDev() {
        return buildEhCacheManagerFactoryBean("cache/ehcache-dev.xml");
    }

    @Bean("ehCacheManagerFactoryBean")
    @Profile("prod")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBeanProd() {
        return buildEhCacheManagerFactoryBean("cache/ehcache-prod.xml");
    }

    public EhCacheManagerFactoryBean buildEhCacheManagerFactoryBean(String path) {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setShared(true);
        factory.setConfigLocation(new ClassPathResource(path));
        return factory;
    }
}
