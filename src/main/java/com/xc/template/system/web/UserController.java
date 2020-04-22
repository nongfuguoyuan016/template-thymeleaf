package com.xc.template.system.web;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xc.template.common.config.Global;
import com.xc.template.common.utils.FastJsonUtils;
import com.xc.template.common.utils.JSONResult;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.common.web.BaseController;
import com.xc.template.system.entity.Office;
import com.xc.template.system.entity.Role;
import com.xc.template.system.entity.User;
import com.xc.template.system.service.SystemService;
import com.xc.template.system.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户Controller
 * @author ganjinhua
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"index"})
	public String index(User user, Model model) {
		return "system/userIndex";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(User user, Model model) {
		model.addAttribute("user",user);
		return "system/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping("/list/data")
	@ResponseBody
	public Map<String,Object> listData(User user, int pageNo, int pageSize) {
		Map<String,Object> map = Maps.newHashMap();
		Page<User> page = systemService.findUser(pageNo, pageSize, user);
		map.put("total",page.getTotal());
		map.put("rows", FastJsonUtils.toJsonArrayIncludeProperties(page.getResult(),"id","office","name","loginName","mobile"));
		return map;
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		if (user.getId() != null) {
			model.addAttribute("hasRoles",user.getRoleList() != null && user.getRoleList().size() > 0 ? user.getRoleList().stream().map(a -> a.getId())
					.collect(Collectors.joining(",")) : null);
		}
		return "system/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public JSONResult save(@Valid User user, HttpServletRequest request, Model model) {
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
			return JSONResult.fail("保存用户'" + user.getLoginName() + "'失败，登录名已存在");
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 保存用户信息
		systemService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
			//UserUtils.getCacheMap().clear();
		}
		return JSONResult.ok( "保存用户'" + user.getLoginName() + "'成功");

	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONResult delete(User user, RedirectAttributes redirectAttributes) {
		if (UserUtils.getUser().getId().equals(user.getId())){
			return JSONResult.fail( "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(user.getId())){
			return JSONResult.fail( "删除用户失败, 不允许删除超级管理员用户");
		}else{
			systemService.deleteUser(user);
		}
		return JSONResult.ok("删除用户成功");
	}

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示及保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "system/userInfo";
	}

	/**
	 * 用户信息保存
	 * @param user
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info", method = RequestMethod.POST)
	@ResponseBody
	public JSONResult info(User user) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			return JSONResult.ok("保存用户信息成功");
		}
		return JSONResult.fail("保存用户信息失败,请稍后重试");
	}


	/**
	 * 返回用户信息
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		return UserUtils.getUser();
	}
	
	/**
	 * 修改个人用户密码
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd", method = RequestMethod.GET)
	public String modifyPwd(Model model) {
		User user = UserUtils.getUser();
		model.addAttribute("user", user);
		return "system/userModifyPwd";
	}

	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public JSONResult modifyPwd(String oldPassword, String newPassword) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				return JSONResult.ok("修改密码成功");
			}else{
				return JSONResult.fail("修改密码失败，旧密码错误");
			}
		}
		return JSONResult.fail("修改密码失败, 密码为空");
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, @RequestParam(required = false)String userJob, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			map.put("loginName",e.getLoginName());
			map.put("officeName",e.getOffice().getName());
			map.put("officeId",e.getOffice().getId());
			mapList.add(map);
		}
		return mapList;
	}


	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "/info/phone")
	public String phone(User user) {
		return user.getMobile();
	}

}
