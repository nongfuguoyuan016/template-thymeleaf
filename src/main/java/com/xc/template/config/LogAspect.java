package com.xc.template.config;

import com.xc.template.system.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    // 拦截所有controller
    @Pointcut("execution(public * com.xc.template..*Controller.*(..))")
    public void requestLog(){}

    // 仅仅拦截 RequestMapping 注解的方法, 方法正常返回时调用
    @AfterReturning("requestLog() && @annotation(requestMapping)")
    public void afterReturning(JoinPoint pjp, RequestMapping requestMapping) {
        doLog(pjp,null);
    }

    // 仅仅拦截 RequestMapping 注解的方法, 方法出现异常时调用
    @AfterThrowing(value = "requestLog() && @annotation(requestMapping)", throwing = "ex")
    public void afterThrowing(JoinPoint pjp, RequestMapping requestMapping, Exception ex) {
        doLog(pjp,ex);
    }

    private void doLog(JoinPoint pjp, Exception ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RequiresPermissions requirePermissions = method.getAnnotation(RequiresPermissions.class);
        String[] permissions = null;
        if(requirePermissions != null) {
            permissions = requirePermissions.value();
        }
        logService.saveLog(request,permissions,ex,null);
    }

}
