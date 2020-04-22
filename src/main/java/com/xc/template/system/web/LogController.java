
package com.xc.template.system.web;

import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.xc.template.common.web.BaseController;
import com.xc.template.system.entity.Log;
import com.xc.template.system.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志Controller
 * @author ganjinhua
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "system/logList";
	}

	@RequiresPermissions("sys:log:view")
	@RequestMapping("/list/data")
	@ResponseBody
	public Map<String,Object> listData(Log log, int pageNo, int pageSize){
		Map<String,Object> map = Maps.newHashMap();
		Page<Log> page = logService.findPage(pageNo,pageSize, log);
		map.put("total",page.getTotal());
		map.put("rows",page.getResult());
		return map;
	}

}
