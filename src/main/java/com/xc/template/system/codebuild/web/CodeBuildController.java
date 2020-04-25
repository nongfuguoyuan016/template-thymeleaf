package com.xc.template.system.codebuild.web;

import com.xc.template.common.utils.JSONResult;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.common.web.BaseController;
import com.xc.template.system.codebuild.dao.CodeBuildDao;
import com.xc.template.system.codebuild.entity.BuildParam;
import com.xc.template.system.codebuild.service.CodeBuildService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author: xuchang
 * @date: 2019/9/29
 */
@Controller
@RequestMapping(value = "/sys/codebuild")
public class CodeBuildController extends BaseController {

    @Autowired
    private CodeBuildDao mapper;

    @Autowired
    private CodeBuildService service;

    @RequiresPermissions(value = "sys:codebuild")
    @RequestMapping(value = {"","/","index"})
    public String index(){
        return "system/codeBuild";
    }

    @RequiresPermissions(value = "sys:codebuild")
    @RequestMapping("/table/data")
    @ResponseBody
    public List<Map<String, Object>> tableData(){
        return mapper.getTableData();
    }

    @RequiresPermissions(value = "sys:codebuild")
    @RequestMapping(value = "/build",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult build(@Valid BuildParam param, Model model){
        String msg = service.build(param);
        return StringUtils.isNotEmpty(msg) ? JSONResult.ok(msg) : JSONResult.fail("生成失败");
    }
}
