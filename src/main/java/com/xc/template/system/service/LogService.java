package com.xc.template.system.service;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xc.template.common.service.CrudService;
import com.xc.template.common.utils.CacheUtils;
import com.xc.template.common.utils.DateUtils;
import com.xc.template.common.utils.Exceptions;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.system.dao.LogDao;
import com.xc.template.system.dao.MenuDao;
import com.xc.template.system.entity.Log;
import com.xc.template.system.entity.Menu;
import com.xc.template.system.entity.User;
import com.xc.template.system.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 日志Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, Log> {

	private static final Logger logger = LoggerFactory.getLogger(LogService.class);

	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

	@Autowired
	private MenuDao menuDao;

	@Override
	public Page<Log> findPage(int pageNo, int pageSize, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null){
			log.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null){
			log.setEndDate(DateUtils.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(pageNo, pageSize, log);
		
	}

	/**
	 * 异步保存日志
	 */
	@Async("taskExecutor")
	public void saveLog(HttpServletRequest request, String[] permissions, Exception ex, String title){
		long start = System.currentTimeMillis();
		User user = UserUtils.getUser();
		if (user != null && user.getId() != null){
			Log log = new Log();
			log.setTitle(title);
			log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			log.setMethod(request.getMethod());
			// 获取日志标题
			log.setTitle(getMenuNamePath(log.getRequestUri(), permissions));
			// 如果有异常，设置异常信息
			log.setException(Exceptions.getStackTraceAsString(ex));
			// 如果无标题并无异常日志，则不保存信息
			if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
				return;
			}
			// 保存日志信息
			save(log);
			if (logger.isDebugEnabled()) {
				logger.debug("save log for request uri: {}, cost time: {} ms", log.getRequestUri(),System.currentTimeMillis() - start);
			}
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	private String getMenuNamePath(String requestUri, String[] permissions){
//		String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>) CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<Menu> menuList = menuDao.findAllList(new Menu());
			for (Menu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						if (Menu.getRootId().equals(id)){
							continue; // 过滤跟节点
						}
						for (Menu m : menuList){
							if (m.getId().equals(id)){
								namePathList.add(m.getName());
								break;
							}
						}
					}
					namePathList.add(menu.getName());
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}

			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
//		String menuNamePath = menuMap.get(href);
		String menuNamePath = menuMap.get(requestUri);
		if (menuNamePath == null & permissions != null && permissions.length != 0) {
			for (String p : permissions){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}
	
}
