package ${packageName}.web;

import com.xc.template.common.utils.StringUtils;
import ${packageName}.entity.${className};
import ${packageName}.service.${className}Service;
import com.xc.template.common.utils.JSONResult;
import com.xc.template.common.web.BaseController;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.Map;
import java.util.HashMap;
import javax.validation.Valid;

/**
 * ${comment}Controller
 * @author xuchang
 * @Date ${createDate}
 */
@Controller
@RequestMapping(value = "/${path}")
public class ${className}Controller extends BaseController {

    @Autowired
    private ${className}Service ${camelTableName}Service;

    @ModelAttribute
	public ${className} get(@RequestParam(required=false) String id) {
        ${className} entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ${camelTableName}Service.get(id);
		}
		if (entity == null){
			entity = new ${className}();
		}
		return entity;
	}

    @RequiresPermissions("${moduleName}:view")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/${path}/${className}List";
    }

    @RequiresPermissions("${moduleName}:view")
    @RequestMapping(value = "list/data")
    @ResponseBody
    public Map<String,Object> listData(${className} ${camelTableName},int pageNo, int pageSize) {
        Map<String,Object> map = new HashMap<>(2);
        Page<${className}> page = ${camelTableName}Service.findPage(pageNo, pageSize, ${camelTableName});
        map.put("total", page.getTotal());
        map.put("rows", page.getResult()) ;
        return map;
    }

	@RequiresPermissions("${moduleName}:view")
	@RequestMapping(value = "form")
	public String form(${className} ${camelTableName}, Model model) {
		model.addAttribute("${camelTableName}", ${camelTableName});
		return "modules/${path}/${className}Form";
	}

    @RequiresPermissions("${moduleName}:edit")
	@RequestMapping(value = "save")
    @ResponseBody
	public JSONResult save(@Valid ${className} ${camelTableName}, Model model) {
        int result = ${camelTableName}Service.save(${camelTableName});
        return (0 == result) ? JSONResult.fail("保存失败") : JSONResult.ok("保存成功");
	}

    @RequiresPermissions("${moduleName}:edit")
	@RequestMapping(value = "delete")
    @ResponseBody
	public JSONResult delete(${className} ${camelTableName}) {
		int result = ${camelTableName}Service.delete(${camelTableName});
        return (0 == result) ? JSONResult.fail("删除失败") : JSONResult.ok("删除成功");
	}

}