package com.xc.template;

import com.xc.template.common.utils.StringUtils;
import com.xc.template.system.codebuild.dao.CodeBuildDao;
import com.xc.template.system.codebuild.entity.BuildParam;
import com.xc.template.system.codebuild.entity.Column;
import com.xc.template.system.codebuild.entity.GenData;
import com.xc.template.system.codebuild.service.CodeBuildService;
import com.xc.template.system.codebuild.utils.CodeBuildUtils;
import com.xc.template.system.codebuild.utils.FreeMarkers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ActiveProfiles("dev")
@SpringBootTest(classes = TemplateThymeleafApplication.class)
class TemplateThymeleafApplicationTests {

    @Autowired
    private CodeBuildDao codeBuildMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testCodeBuild() {
        BuildParam param = new BuildParam();
        param.setTableName("sys_menu");
        param.setModuleName("sys:menu");
        param.setComment("");
        GenData genData = buildGenData(param);
        try {
            System.out.println(FreeMarkers.buildFormHtml(genData));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private GenData buildGenData(BuildParam param){
        GenData genData = new GenData();
        List<Column> cols = codeBuildMapper.getColumnsByTableNameForMySQL(param.getTableName());
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
