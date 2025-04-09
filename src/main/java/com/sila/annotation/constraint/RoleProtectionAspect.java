package com.sila.annotation.constraint;

import com.sila.exception.AccessDeniedException;
import com.sila.utlis.enums.USER_ROLE;
import com.sila.annotation.PreAuthorization;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RoleProtectionAspect {

    @Before("within(@com.sila.annotation.PreAuthorization *) || @annotation(com.sila.annotation.PreAuthorization)")
    public void checkRole(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PreAuthorization roleProtected = method.getAnnotation(PreAuthorization.class);

        if (roleProtected == null) {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            roleProtected = targetClass.getAnnotation(PreAuthorization.class);
        }

        if (roleProtected != null) {
            validateRoles(roleProtected.value());
        }
    }

    private void validateRoles(USER_ROLE[] allowedRoles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<String> allowed = Arrays.stream(allowedRoles)
                .map(USER_ROLE::name)
                .toList();

        if (auth == null || auth.getAuthorities() == null) {
            throw new AccessDeniedException("Unauthenticated access.");
        }



        boolean hasRole = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(allowed::contains);

        if (!hasRole) {
            throw new AccessDeniedException("Access denied. Only this roles : "+allowed+" are allowed. Your role is : "+auth.getAuthorities());
        }
    }
}
