package com.xc.template.config;

import com.xc.template.common.utils.JSONResult;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = AuthorizationException.class)
    public Object UnAuthorizedException(HttpServletRequest request, Exception ex) {
        log.error("GlobalExceptionHandler catch UnAuthorizedException: ",ex);
        if(isAjax(request)) {
            return JSONResult.ex("越权操作");
        }else return new ModelAndView("/error/403");
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Object noHandlerException(HttpServletRequest request,Exception ex) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException: ",ex);
        if(isAjax(request)) {
            return JSONResult.ex("无效的地址");
        }else return new ModelAndView("/error/404");
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        log.error("GlobalExceptionHandler catch HttpMessageNotReadableException: ",ex);
        if(isAjax(request)) {
            return JSONResult.ex("请求参数无效");
        }else return new ModelAndView("/error/4xx");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidException(HttpServletRequest request,MethodArgumentNotValidException ex) {
        log.error("GlobalExceptionHandler catch MethodArgumentNotValidException: ",ex);
        if(isAjax(request)) {
            return JSONResult.ex(ex.getBindingResult().getFieldError().getDefaultMessage());
        }else return new ModelAndView("/error/5xx");
    }

    @ExceptionHandler(value = Exception.class)
    public Object catchException(HttpServletRequest request, Exception ex) {
        log.error("GlobalExceptionHandler catch Exception: ",ex);
        if(isAjax(request)) {
            return JSONResult.ex("系统异常,请稍后再试");
        }else return new ModelAndView("/error/5xx");
    }

    private boolean isAjax(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString());
    }

}
