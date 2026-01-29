package com.sila.config.log;

import com.sila.config.context.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class IpLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String ip = getClientIp(httpServletRequest);
        String path = httpServletRequest.getRequestURI();

        var authorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        boolean isAnonymous = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ANONYMOUS"));

        if (isAnonymous) {
            System.out.println("=====> Login As [ROLE_ANONYMOUS] Request IP: " + ip + " - " + path);
        } else {
            UserContext.findUser().ifPresentOrElse(
                    user -> System.out.println("=====> Login As [" + user.getRole() + "] Request IP: " + ip + " - " + path),
                    () -> System.out.println("=====> Login As [UNKNOWN ROLE] Request IP: " + ip + " - " + path)
            );
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader == null || xfHeader.isEmpty()) ? request.getRemoteAddr() : xfHeader.split(",")[0].trim();
    }
}
