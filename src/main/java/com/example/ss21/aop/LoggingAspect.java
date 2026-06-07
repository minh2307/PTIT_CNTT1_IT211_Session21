package com.example.ss21.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.example.ss21.service.AuthService.login(..))")
    public void loginPointcut() {}

    @Pointcut("execution(* com.example.ss21.service.AuthService.refreshToken(..))")
    public void refreshPointcut() {}

    @Pointcut("execution(* com.example.ss21.service.AuthService.logout(..))")
    public void logoutPointcut() {}

    @Pointcut("within(com.example.ss21..*)")
    public void applicationPackagePointcut() {}

    @AfterReturning(pointcut = "loginPointcut()", returning = "result")
    public void logAfterLogin(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        String username = args.length > 0 ? ((com.example.ss21.dto.request.LoginRequest) args[0]).getUsername() : "unknown";
        log.info("Token granted successfully for user: {}", username);
    }

    @AfterReturning(pointcut = "refreshPointcut()", returning = "result")
    public void logAfterRefresh(JoinPoint joinPoint, Object result) {
        log.info("Access token refreshed successfully.");
    }

    @AfterReturning(pointcut = "logoutPointcut()")
    public void logAfterLogout(JoinPoint joinPoint) {
        log.info("Token revoked/logged out successfully.");
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in class: {} method: {} with message: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getMessage() != null ? e.getMessage() : "NULL");
    }
}
