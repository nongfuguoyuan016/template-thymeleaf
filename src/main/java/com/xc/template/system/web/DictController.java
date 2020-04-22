
package com.xc.template.system.web;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xc.template.common.utils.JSONResult;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.common.web.BaseController;
import com.xc.template.system.entity.Dict;
import com.xc.template.system.service.DictService;
import com.xc.template.system.utils.DictUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 字典Controller
 * @author ganjinhua
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
		return "system/dictList";
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "list/data")
	@ResponseBody
	public Map<String,Object> listData(Dict dict, int pageNo, int pageSize) {
		Map<String,Object> map = Maps.newHashMap();
		Page<Dict> page = dictService.findPage(pageNo, pageSize, dict);
		map.put("total",page.getTotal());
		map.put("rows",page.getResult());
		return map;
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		model.addAttribute("firstDicts", DictUtils.getAllFirstDictList());
		return "system/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")//@Valid
	@ResponseBody
	public JSONResult save(@Valid Dict dict) {
		dictService.save(dict);
		return JSONResult.ok("保存字典'" + dict.getLabel() + "'成功");
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONResult delete(Dict dict) {
		dictService.delete(dict);
		return JSONResult.ok("删除字典成功");
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> list = dictService.findList(dict);
		for (int i=0; i<list.size(); i++){
			Dict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required=false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return dictService.findList(dict);
	}
}
