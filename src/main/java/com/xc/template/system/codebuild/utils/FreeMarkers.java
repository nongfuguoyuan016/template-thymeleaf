package com.xc.template.system.codebuild.utils;

import com.xc.template.system.codebuild.entity.GenData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.StringWriter;

public class FreeMarkers {

    public static String buildEntity(GenData genData) throws Exception{
        return build(genData,"EntityTpl.ftl");
    }

    public static String buildController(GenData genData) throws Exception{
        return build(genData,"ControllerTpl.ftl");
    }

    public static String buildService(GenData genData) throws Exception{
        return build(genData,"ServiceTpl.ftl");
    }

    public static String buildDao(GenData genData) throws Exception{
        return build(genData,"DaoTpl.ftl");
    }

    public static String buildXml(GenData genData) throws Exception{
        return build(genData,"XmlTpl.ftl");
    }

    public static String buildListHtml(GenData genData) throws Exception{
        return build(genData,"ListTpl.ftl");
    }

    public static String buildFormHtml(GenData genData) throws Exception{
        return build(genData,"FormTpl.ftl");
    }

    public static String build(GenData genData,String tplName)  throws Exception {
        Configuration configuration = new Configuration();
        Resource path = new DefaultResourceLoader().getResource("classpath:/templates/codebuild/");
        configuration.setNumberFormat("#");
        configuration.setDirectoryForTemplateLoading(path.getFile());
        Template template = configuration.getTemplate(tplName,"UTF-8");
        StringWriter stringWriter = new StringWriter();
        template.process(genData,stringWriter);
        return stringWriter.toString();
    }

}
