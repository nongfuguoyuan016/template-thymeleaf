package com.xc.template.system.web;

import com.xc.template.common.utils.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ExceptionController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public JSONResult error(HttpServletRequest request, HttpServletResponse response) {
        DispatcherServletWebRequest webRequest = new DispatcherServletWebRequest(request);
        log.info("ExceptionController: {}", errorAttributes.getErrorAttributes(webRequest,true));
        response.setStatus(200);
        return JSONResult.ex("请求失败, 请稍后重试");
    }
}
