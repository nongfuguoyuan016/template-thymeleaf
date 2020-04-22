package com.xc.template.system.web;

import com.xc.template.common.config.Global;
import com.xc.template.common.utils.FastJsonUtils;
import com.xc.template.common.utils.JSONResult;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.common.web.BaseController;
import com.xc.template.system.entity.Role;
import com.xc.template.system.service.OfficeService;
import com.xc.template.system.service.SystemService;
import com.xc.template.system.utils.DictUtils;
import com.xc.template.system.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色Controller
 * @author ganjinhua
 * @version 2013-12-05
 */
@Controller
@RequestMapping(value = "sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		return "system/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "list/data")
	@ResponseBody
	public List<Role> listData() {
		return systemService.findAllRole();
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		if (role.getOffice()==null){
			role.setOffice(UserUtils.getUser().getOffice());
		}
		if(role.getSort()==null){
			role.setSort(systemService.getMaxSortNumber() + 1);
		}
		List<Map<String, String>> menus = systemService.findAllMenu().stream().map(m -> {
			Map<String, String> map = new HashMap<>();
			map.put("id", m.getId());
			map.put("pId", m.getParentId());
			map.put("name", m.getParent() != null && m.getParent().getId() != null ? m.getName() : "权限列表");
			return map;
		}).collect(Collectors.toList());
		List<Map<String, String>> offices = officeService.findAll().stream().map(o -> {
			Map<String, String> map = new HashMap<>();
			map.put("id", o.getId());
			map.put("pId", o.getParentId());
			map.put("name", o.getName());
			return map;
		}).collect(Collectors.toList());
		model.addAttribute("role", role);
		model.addAttribute("menus", FastJsonUtils.toJsonArrayIncludeProperties(menus));
		model.addAttribute("offices", FastJsonUtils.toJsonArrayIncludeProperties(offices));
		model.addAttribute("scopes", DictUtils.getDictList("sys_data_scope"));
		// 数据是否可以修改, 系统数据只能管理员修改
		model.addAttribute("changeable", ("1".equals(role.getSysData()) && UserUtils.getUser().isAdmin())
				|| !"1".equals(role.getSysData()));
		return "system/roleForm";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public JSONResult save(@Valid Role role, Model model) {
		if(!UserUtils.getUser().isAdmin()&&role.getSysData().equals(Global.YES)){
			return JSONResult.fail("越权操作，只有超级管理员才能修改此数据！");
		}
		if (!"true".equals(checkName(role.getOldName(), role.getName()))){
			return JSONResult.fail("保存角色'" + role.getName() + "'失败, 角色名已存在");
		}
		if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))){
			return JSONResult.fail("保存角色'" + role.getName() + "'失败, 英文名已存在");
		}
		systemService.saveRole(role);
		return JSONResult.ok("保存角色'" + role.getName() + "'成功");
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONResult delete(Role role, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
			return JSONResult.fail("越权操作，只有超级管理员才能修改此数据！");
		}
		systemService.deleteRole(role);
		return JSONResult.ok("删除角色成功");
	}

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && systemService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 验证角色英文名是否有效
	 * @param oldEnname
	 * @param enname
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
			return "true";
		}
		return "false";
	}

}
