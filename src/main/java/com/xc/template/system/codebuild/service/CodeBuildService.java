package com.xc.template.system.codebuild.service;

import com.xc.template.common.utils.StringUtils;
import com.xc.template.system.codebuild.dao.CodeBuildDao;
import com.xc.template.system.codebuild.entity.BuildParam;
import com.xc.template.system.codebuild.entity.Column;
import com.xc.template.system.codebuild.entity.GenData;
import com.xc.template.system.codebuild.utils.CodeBuildUtils;
import com.xc.template.system.codebuild.utils.FreeMarkers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CodeBuildService {

    @Autowired
    private CodeBuildDao mapper;

    public String build(BuildParam param){
        StringBuilder sb = new StringBuilder();
        GenData genData = buildGenData(param);
        String directory = param.getFilePath();
        CodeBuildUtils.createDirectoryIfNotExist(directory);
        try{
            if (param.isNeedEntity()) {
                String entity = FreeMarkers.buildEntity(genData);
                CodeBuildUtils.createDirectoryIfNotExist(directory + "/entity/");
                String entityPath = directory + "/entity/" + genData.getClassName() + ".java";
                CodeBuildUtils.writeToFile(entityPath,entity);
                sb.append("entity生成成功;");
            }
            if (param.isNeedController()) {
                String controller = FreeMarkers.buildController(genData);
                CodeBuildUtils.createDirectoryIfNotExist(directory + "/web/");
                String controllerPath = directory + "/web/" + genData.getClassName() + "Controller.java";
                CodeBuildUtils.writeToFile(controllerPath,controller);
                sb.append("controller生成成功;");
            }
            if (param.isNeedService()) {
                String service = FreeMarkers.buildService(genData);
                CodeBuildUtils.createDirectoryIfNotExist(directory + "/service/");
                String servicePath = directory + "/service/" + genData.getClassName() + "Service.java";
                CodeBuildUtils.writeToFile(servicePath,service);
                sb.append("service生成成功;");
            }
            if (param.isNeedDao()) {
                String dao = FreeMarkers.buildDao(genData);
                CodeBuildUtils.createDirectoryIfNotExist(directory + "/dao/");
                String daoPath = directory + "/dao/" + genData.getClassName() + "Dao.java";
                CodeBuildUtils.writeToFile(daoPath,dao);
                sb.append("dao生成成功;");
            }
            if (param.isNeedXml()) {
                String xml = FreeMarkers.buildXml(genData);
                String xmlPath = directory + "/" + genData.getClassName() + "Dao.xml";
                CodeBuildUtils.writeToFile(xmlPath,xml);
                sb.append("xml生成成功;");
            }
            if (param.isNeedListPage()) {
                String list = FreeMarkers.buildListHtml(genData);
                String htmlPath = directory  + "/" + param.getModuleName().replaceAll(":","/");
                CodeBuildUtils.createDirectoryIfNotExist(htmlPath);
                String listPath = htmlPath + "/"  + genData.getClassName() + "List.html";
                CodeBuildUtils.writeToFile(listPath,list);
                sb.append("List Page生成成功;");
            }
            if (param.isNeedFormPage()) {
                String form = FreeMarkers.buildFormHtml(genData);
                String htmlPath = directory  + "/" + param.getModuleName().replaceAll(":","/");
                CodeBuildUtils.createDirectoryIfNotExist(htmlPath);
                String formPath = htmlPath + "/"  + genData.getClassName() + "Form.html";
                CodeBuildUtils.writeToFile(formPath,form);
                sb.append("Form Page生成成功;");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    private GenData buildGenData(BuildParam param){
        GenData genData = new GenData();
        List<Column> cols = "MYSQL".equals(param.getDbName()) ? mapper.getColumnsByTableNameForMySQL(param.getTableName()):null;
        boolean dateImport = false;
        if (cols != null) {
            for (Column col : cols) {
                resolveCol(col);
                if(!dateImport) {
                    dateImport = col.isShow() && "Date".equals(col.getType());
                }
            }
        }
        if (StringUtils.isNotBlank(param.getModuleName())) {
            genData.setPath(param.getModuleName().replaceAll(":","/"));
        }
        genData.setTableName(param.getTableName());
        genData.setCamelTableName(CodeBuildUtils.toCamelCase(param.getTableName()));
        genData.setClassName(CodeBuildUtils.upperFirstCase(genData.getCamelTableName()));
        genData.setCols(cols);
        genData.setDateImport(dateImport);
        genData.setComment(param.getComment());
        genData.setModuleName(param.getModuleName());
        genData.setPackageName(param.getPackageName());
        genData.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return genData;
    }

    private void resolveCol(Column col){
        switch (col.getName()){
            case "id":
            case "del_flag":
            case "create_date":
            case "create_by":
            case "update_date":
            case "remarks":
            case "update_by": col.setShow(false);break;
            default:col.setShow(true);
        }
        col.setCamelName(CodeBuildUtils.toCamelCase(col.getName()));
        col.setUpCamelName(CodeBuildUtils.upperFirstCase(col.getCamelName()));
//        System.out.println(col);
    }

}
