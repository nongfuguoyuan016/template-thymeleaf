package com.xc.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

@SpringBootApplication(exclude = FreeMarkerAutoConfiguration.class)
public class TemplateThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateThymeleafApplication.class, args);
    }

}
