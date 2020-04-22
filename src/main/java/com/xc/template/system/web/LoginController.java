package com.xc.template.system.web;

import com.xc.template.common.config.Global;
import com.xc.template.common.security.shiro.session.SessionDAO;
import com.xc.template.common.utils.CacheUtils;
import com.xc.template.common.utils.StringUtils;
import com.xc.template.common.web.BaseController;
import com.xc.template.system.entity.Menu;
import com.xc.template.system.entity.MenuDto;
import com.xc.template.system.security.FormAuthenticationFilter;
import com.xc.template.system.security.SystemAuthorizingRealm.Principal;
import com.xc.template.system.utils.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 管理登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        Principal principal = UserUtils.getPrincipal();
        if (logger.isDebugEnabled()){
            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }
        // 如果已经登录，则跳转到管理首页
        if(principal != null && !principal.isMobileLogin()){
            return "redirect:index";
        }
        return "system/login";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = UserUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if(principal != null){
            return "redirect:index";
        }

        String username = WebUtils.getCleanParam(request, org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String)request.getAttribute(org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        if (logger.isDebugEnabled()){
            logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }
        return "system/login";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "/index")
    public String index(Model model) {
        List<MenuDto> dtoList = null;
        List<Menu> firstMenuList = UserUtils.getFirstMenuList();
        if(CollectionUtils.isNotEmpty(firstMenuList)) {
            // 转换成 dto
            dtoList = firstMenuList.stream().map(menu -> {
                MenuDto menuDto = new MenuDto();
                BeanUtils.copyProperties(menu, menuDto);
                return menuDto;
            }).collect(Collectors.toList());
            // 设置子菜单
            // 二级菜单
            List<MenuDto> subMenuDtoList = null;
            List<Menu> subMenus = null;
            // 三级菜单
            List<MenuDto> thirdMenuDtoList = null;
            List<Menu> thirdMenus = null;
            for (MenuDto menuDto : dtoList) {
                subMenus = UserUtils.getSubMenuList(menuDto.getId());
                if(CollectionUtils.isEmpty(subMenus)) {continue;}
                // 二级菜单转换成dto
                subMenuDtoList = subMenus.stream().map(menu -> {
                    MenuDto subMenuDto = new MenuDto();
                    BeanUtils.copyProperties(menu, subMenuDto);
                    return subMenuDto;
                }).collect(Collectors.toList());
                // 设置三级菜单
                for (MenuDto subMenuDto : subMenuDtoList) {
                    if(UserUtils.hasSubMenu(subMenuDto.getId())) { // 是否有三级菜单
                        thirdMenus = UserUtils.getSubMenuList(subMenuDto.getId());
                        // 三级菜单转换成dto
                        thirdMenuDtoList = thirdMenus.stream().map(menu -> {
                            MenuDto thirdMenuDto = new MenuDto();
                            BeanUtils.copyProperties(menu, thirdMenuDto);
                            return thirdMenuDto;
                        }).collect(Collectors.toList());
                        subMenuDto.setSubMenus(thirdMenuDtoList);
                    }
                }
                menuDto.setSubMenus(subMenuDtoList);
            }
        }
        model.addAttribute("menus", dtoList == null ? Collections.emptyList() : dtoList);
        model.addAttribute("user", UserUtils.getUser().getName());
        model.addAttribute("productName", Global.getConfig("product.name"));
        return "system/index";
    }

}
